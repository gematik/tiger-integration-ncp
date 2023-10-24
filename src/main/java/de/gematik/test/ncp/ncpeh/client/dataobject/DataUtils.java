/*
 * Copyright 2023 gematik GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gematik.test.ncp.ncpeh.client.dataobject;

import static de.gematik.test.ncp.util.Utils.unmarshalBase64EncodedXml;

import de.gematik.ncpeh.api.common.WrappedHttpMessage;
import de.gematik.ncpeh.api.common.WrappedHttpRequest;
import de.gematik.ncpeh.api.common.WrappedHttpResponse;
import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.NcpehInterface.PatientSummaryLevel;
import de.gematik.test.ncp.util.Utils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBElement;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.util.Base64;
import org.hl7.v3.CD;
import org.hl7.v3.ClinicalDocument;
import org.hl7.v3.ENXP;
import org.hl7.v3.II;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueEvent;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueManagement;
import org.hl7.v3.MCAIMT900001UV01SourceOf;
import org.hl7.v3.MFMIMT700711UV01Reason;
import org.hl7.v3.PN;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.openqa.selenium.Pdf;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@UtilityClass
@Accessors(fluent = true)
@Slf4j
public class DataUtils {

  private static final String NAME_GIVEN = "given";
  private static final String NAME_FAMILY = "family";
  private static final String NAME_PREFIX = "prefix";
  private static final String TESTSUITE_PATH_PART = "tiger-integration-ncp";

  public static final String CODE_SYSTEM_XCPD_ERROR = "1.3.6.1.4.1.19376.1.2.27.3";

  private static final DateTimeFormatter BASIC_DATE_TIME_FORMAT =
      new DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmmss").toFormatter();

  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private final File testsuiteTempDir = getOrCreateTestsuiteTmpFolder();

  /**
   * Read the patient data name, KVNR and birth date from the given {@link PRPAIN201306UV02} object.
   *
   * @param response {@link PRPAIN201306UV02} as received in an identifyPatient operation from the
   *     NCPeH FD
   * @return {@link Patient} object containing the patient data
   */
  public static Patient readPatientDataFromIdentifyPatientResponse(PRPAIN201306UV02 response) {
    return readPatientDataFromControlActProcess(response.getControlActProcess());
  }

  /**
   * Read the reason encoding from a {@link PRPAIN201306UV02} object, in case an error occurred
   * while populating the object.
   *
   * @param response {@link PRPAIN201306UV02} as received in an identifyPatient operation from the
   *     NCPeH FD
   * @return {@link List} of found reason encodings.
   */
  public static List<String> readReasonEncodingFromIdentifyPatientResponse(
      PRPAIN201306UV02 response) {
    try {
      return readReasonEncodingFromControlActProcess(response.getControlActProcess());
    } catch (Exception e) {
      return Collections.emptyList();
    }
  }

  /**
   * Read the structured patient summary in the CDA3 format ({@link ClinicalDocument}) from an NCPeH
   * retrieveDocument response
   *
   * @param psdo {@link RetrievePatientSummaryDO} response object of the {@link
   *     de.gematik.test.ncp.ncpeh.NcpehInterface#retrievePatientSummary(Patient, String, String,
   *     AdhocQueryResponse, PatientSummaryLevel...)} operation
   * @return {@link ClinicalDocument} patient summary, or null if non was found
   */
  public static ClinicalDocument readPatientSummaryLvl3(RetrievePatientSummaryDO psdo) {
    return Optional.ofNullable(getPatientSummaryOfLevel(psdo, PatientSummaryLevel.LEVEL_3))
        .map(dr -> Utils.unmarshalXml(ClinicalDocument.class, dr))
        .orElse(null);
  }

  /**
   * Read the patient summary in the CDA1 format (PDF) from an NCPeH retrieveDocument response
   *
   * @param retrievePatientSummaryDO {@link RetrievePatientSummaryDO} response object of the {@link
   *     de.gematik.test.ncp.ncpeh.NcpehInterface#retrievePatientSummary(Patient, String, String,
   *     AdhocQueryResponse, PatientSummaryLevel...)} operation
   * @return {@link Pdf} patient summary, or null if non was found
   */
  public static Pdf readPatientSummaryLvl1(RetrievePatientSummaryDO retrievePatientSummaryDO) {
    return Optional.ofNullable(
            getPatientSummaryOfLevel(retrievePatientSummaryDO, PatientSummaryLevel.LEVEL_1))
        .map(doc -> new Pdf(Base64.encodeBase64String(doc)))
        .orElse(null);
  }

  /**
   * Write a CDA1 patient summary (PDF format) to a file in the temp folder, subfolder {@value
   * TESTSUITE_PATH_PART} of the local file system (might be user specific).
   *
   * @param patient {@link Patient} information, which are used to create the file name.
   * @param cda1Document {@link Pdf} CDA1 patient summary
   * @return {@link String} the absolute path of the file, where the patient summary was written to.
   */
  @SneakyThrows
  public static String writePatientSummaryToLocalFile(Patient patient, Pdf cda1Document) {
    var tempFile = createTempFile(".pdf", patient.kvnr(), "Patient_Summary_Level_1");

    var pdfBytes = Base64.decodeBase64(cda1Document.getContent());
    while (Base64.isArrayByteBase64(pdfBytes)) pdfBytes = Base64.decodeBase64(pdfBytes);

    FileUtils.writeByteArrayToFile(tempFile, pdfBytes);

    return tempFile.getAbsolutePath();
  }

  /**
   * Read the {@link SimulatorCommunicationData} from the body of a HTTP {@link Response} and
   * generate an {@link IdentifyPatientDO} object from its contents
   *
   * @param response {@link Response} as received in an HTTP communication
   * @return {@link IdentifyPatientDO} object created from the response body contents
   */
  public static IdentifyPatientDO convertResponseDataForIdentifyPatient(Response response) {
    var simulatorComData = response.readEntity(SimulatorCommunicationData.class);

    return new IdentifyPatientDO(
        HttpStatus.valueOf(response.getStatus()),
        parseHttpRequest(simulatorComData.requestSend(), PRPAIN201305UV02.class),
        parseHttpResponse(simulatorComData.responseReceived(), PRPAIN201306UV02.class));
  }

  /**
   * Read the {@link SimulatorCommunicationData} from the body of a HTTP {@link Response} and
   * generate an {@link FindPatientSummaryDO} object from its contents
   *
   * @param response {@link Response} as received in an HTTP communication
   * @return {@link FindPatientSummaryDO} object created from the response body contents
   */
  public static FindPatientSummaryDO convertResponseDataForFindPatientSummary(Response response) {
    var simulatorComData = response.readEntity(SimulatorCommunicationData.class);

    return new FindPatientSummaryDO(
        HttpStatus.valueOf(response.getStatus()),
        parseHttpRequest(simulatorComData.requestSend(), AdhocQueryRequest.class),
        parseHttpResponse(simulatorComData.responseReceived(), AdhocQueryResponse.class));
  }

  /**
   * Read the {@link SimulatorCommunicationData} from the body of a HTTP {@link Response} and
   * generate an {@link RetrievePatientSummaryDO} object from its contents
   *
   * @param response {@link Response} as received in an HTTP communication
   * @return {@link RetrievePatientSummaryDO} object created from the response body contents
   */
  public static RetrievePatientSummaryDO convertResponseDataForRetrievePatientSummary(
      Response response) {
    var simulatorComData = response.readEntity(SimulatorCommunicationData.class);

    return new RetrievePatientSummaryDO(
        HttpStatus.valueOf(response.getStatus()),
        parseHttpRequest(simulatorComData.requestSend(), RetrieveDocumentSetRequestType.class),
        parseHttpResponse(
            simulatorComData.responseReceived(), RetrieveDocumentSetResponseType.class));
  }

  public static PersonName createNameFromPNList(List<PN> nameList) {
    var names =
        nameList.stream()
            .flatMap(pn -> pn.getContent().stream())
            .filter(content -> content instanceof JAXBElement<?>)
            .map(content -> (JAXBElement<?>) content)
            .filter(content -> ENXP.class.isAssignableFrom(content.getDeclaredType()))
            .map(content -> jaxbElementToTable((JAXBElement<? extends ENXP>) content))
            .reduce(
                (result, content) -> {
                  result.putAll(content);
                  return result;
                })
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No patient name found in response from the NCPeH FD"));

    return new PersonName()
        .titles(names.get(NAME_PREFIX))
        .givenNames(names.get(NAME_GIVEN))
        .lastNames(names.get(NAME_FAMILY));
  }

  // region private

  private static File createTempFile(
      @NonNull String fileNameSuffix, @NonNull String firstNamePart, String... furtherNameParts)
      throws IOException {
    var nameParts = new ArrayList<String>();
    nameParts.add(firstNamePart);
    Optional.ofNullable(furtherNameParts).ifPresent(fnp -> nameParts.addAll(Arrays.asList(fnp)));
    nameParts.add(LocalDateTime.now().format(BASIC_DATE_TIME_FORMAT));

    var file = File.createTempFile(String.join("_", nameParts), fileNameSuffix, testsuiteTempDir());
    var fileRestricted = file.setWritable(true, true) && file.setExecutable(true, true);
    if (!fileRestricted) {
      log.warn("Access rights of temporary file could not be set as requested");
    }
    file.deleteOnExit();

    return file;
  }

  @SneakyThrows
  private static File getOrCreateTestsuiteTmpFolder() {
    var tempDirPath = Paths.get(System.getProperty("java.io.tmpdir"));
    var testsuiteTempDirPath = tempDirPath.resolve(TESTSUITE_PATH_PART);

    if (Files.notExists(testsuiteTempDirPath)) {
      Files.createDirectory(testsuiteTempDirPath);
    }

    return testsuiteTempDirPath.toFile();
  }

  private static Patient readPatientDataFromControlActProcess(
      PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess) {
    var pats =
        controlActProcess.getSubject().stream()
            .map(PRPAIN201306UV02MFMIMT700711UV01Subject1::getRegistrationEvent)
            .map(PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent::getSubject1)
            .map(PRPAIN201306UV02MFMIMT700711UV01Subject2::getPatient)
            .filter(Objects::nonNull)
            .toList();

    if (pats.isEmpty())
      throw new IllegalArgumentException(
          "No patient data were found in the identifyPatient response");
    if (pats.size() > 1)
      throw new IllegalArgumentException(
          "Data to more than one patient were found in the identifyPatient response");

    var pat = pats.get(0);

    var patient = new PatientImpl();

    patient.kvnr(readKvnrFromPATPatient(pat));

    patient.accessCode(readAccessCodeFromPATPatient(pat));

    var patPerson = pat.getPatientPerson().getValue();

    patient.name(readNameFromPatientPerson(patPerson));

    patient.birthDate(readBirthDateFromPatientPerson(patPerson));

    return patient;
  }

  private static List<String> readReasonEncodingFromControlActProcess(
      PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess) {
    return controlActProcess.getReasonOf().stream()
        .map(MFMIMT700711UV01Reason::getDetectedIssueEvent)
        .map(MCAIMT900001UV01DetectedIssueEvent::getMitigatedBy)
        .flatMap(Collection::stream)
        .map(MCAIMT900001UV01SourceOf::getDetectedIssueManagement)
        .map(MCAIMT900001UV01DetectedIssueManagement::getCode)
        .filter(code -> CODE_SYSTEM_XCPD_ERROR.equals(code.getCodeSystem()))
        .map(CD::getCode)
        .toList();
  }

  @SneakyThrows
  private static String readKvnrFromPATPatient(PRPAMT201310UV02Patient patPatient) {
    var value = readKvnrConcAccessCodeFromPATPatient(patPatient)[0];

    if (Objects.isNull(value) || value.isEmpty())
      throw new ParseException("No extension (KVNR) found in patient data", 0);

    return value;
  }

  private static PersonName readNameFromPatientPerson(PRPAMT201310UV02Person patientPerson) {
    return createNameFromPNList(patientPerson.getName());
  }

  private static LocalDate readBirthDateFromPatientPerson(PRPAMT201310UV02Person patientPerson) {
    return LocalDate.parse(
        patientPerson.getBirthTime().getValue(),
        new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter());
  }

  @SneakyThrows
  private static String readAccessCodeFromPATPatient(PRPAMT201310UV02Patient patPatient) {
    var value = readKvnrConcAccessCodeFromPATPatient(patPatient)[1];

    if (Objects.isNull(value) || value.isEmpty())
      throw new ParseException("No access code found in patient data", 0);

    return value;
  }

  @SneakyThrows
  private static String[] readKvnrConcAccessCodeFromPATPatient(PRPAMT201310UV02Patient patPatient) {
    var kvnrAccessCode =
        patPatient.getId().stream()
            .map(II::getExtension)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No extension (KVNR) and access code found in patient data"));

    var parts = kvnrAccessCode.split("\\|");

    if (parts.length != 2)
      throw new ParseException(
          "The element supposed to hold KVNR and access code does not have the expected format! Received element: "
              + kvnrAccessCode,
          10);

    return parts;
  }

  private static Hashtable<String, String> jaxbElementToTable(
      JAXBElement<? extends ENXP> jaxbElement) {
    var res = new Hashtable<String, String>();
    res.put(
        jaxbElement.getName().getLocalPart(),
        jaxbElement.getValue().getContent().stream()
            .reduce((result, value) -> result + " " + value)
            .orElse(""));
    return res;
  }

  private static byte[] getPatientSummaryOfLevel(
      @NonNull RetrievePatientSummaryDO psdo, PatientSummaryLevel level) {
    var retrieveResponse =
        Objects.requireNonNull(
            psdo.ncpehFdResponseContent(),
            "No response of the NCPeH FD is present in the data received from the NCPeH Simulator");

    return retrieveResponse.getDocumentResponse().stream()
        .filter(dr -> level.documentIsOfLevel(dr.getDocumentUniqueId()))
        .map(DocumentResponse::getDocument)
        .findFirst()
        .orElse(null);
  }

  private static <T> HttpRequestData<T> parseHttpRequest(
      WrappedHttpRequest request, Class<T> requestBodyType) {
    var requestContent = parseHttpBody(request.messageContent(), requestBodyType);
    var requestHeader = parseHttpHeaders(request.messageContent());
    var requestLineSeparatorPosition = request.requestLine().indexOf("\s");
    return new HttpRequestDataRecord<>(
        HttpMethod.valueOf(request.requestLine().substring(0, requestLineSeparatorPosition).trim()),
        request.requestLine().substring(requestLineSeparatorPosition).trim(),
        requestHeader,
        requestContent);
  }

  private static <T> HttpResponseData<T> parseHttpResponse(
      WrappedHttpResponse response, Class<T> responseBodyType) {
    var responseContent = parseHttpBody(response.messageContent(), responseBodyType);
    var responseHeader = parseHttpHeaders(response.messageContent());

    return new HttpResponseDataRecord<>(
        HttpStatus.valueOf(Integer.parseInt(response.statusLine().substring(0, 3))),
        responseHeader,
        responseContent);
  }

  private static <T> T parseHttpBody(WrappedHttpMessage httpMessage, Class<T> bodyType) {
    var bodyEncoded = httpMessage.httpBody();

    return unmarshalBase64EncodedXml(bodyType, bodyEncoded);
  }

  private static Map<String, String> parseHttpHeaders(WrappedHttpMessage httpMessage) {
    byte[] headersDecoded = httpMessage.httpHeader();

    while (Base64.isArrayByteBase64(headersDecoded)) {
      headersDecoded = Base64.decodeBase64(headersDecoded);
    }

    var headersAsString = new String(headersDecoded, StandardCharsets.UTF_8);
    headersAsString = Utils.trimCharacters(headersAsString, "[", "]");

    return headersAsString
        .lines()
        .flatMap(line -> Arrays.stream(Utils.splitConsideringQuotes(line, ",")))
        .map(
            headerLine -> {
              var colonIndex = headerLine.indexOf(":");
              var headerMap = new HashMap<String, String>();
              headerMap.put(
                  headerLine.substring(0, colonIndex).trim(),
                  headerLine.substring(colonIndex).trim());
              return headerMap;
            })
        .reduce(
            (headersMap, headerMap) -> {
              headersMap.putAll(headerMap);
              return headersMap;
            })
        .orElse(new HashMap<>());
  }

  // endregion private
}
