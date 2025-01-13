/*
 * Copyright (c) 2024-2025 gematik GmbH
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

import static de.gematik.test.ncp.ncpeh.client.dataobject.SoapUtils.extractBodyAsDocument;
import static de.gematik.test.ncp.util.Utils.unmarshalXml;

import de.gematik.ncpeh.api.common.WrappedHttpMessage;
import de.gematik.ncpeh.api.common.WrappedHttpRequest;
import de.gematik.ncpeh.api.common.WrappedHttpResponse;
import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.data.AcknowledgementDetail;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.NcpehService;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
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
import java.util.Base64;
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
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.io.FileUtils;
import org.hl7.v3.CD;
import org.hl7.v3.ClinicalDocument;
import org.hl7.v3.ED;
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
import org.hl7.v3.ST;
import org.jetbrains.annotations.NotNull;
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

  public static final List<String> CODE_SYSTEM_XCPD_ERRORS =
      List.of("1.3.6.1.4.1.19376.1.2.27.3", "1.3.6.1.4.1.12559.11.10.1.3.2.2.1");

  private static final DateTimeFormatter BASIC_DATE_TIME_FORMAT =
      new DateTimeFormatterBuilder().appendPattern("yyyyMMddHHmmss").toFormatter();

  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private final File testsuiteTempDir = getOrCreateTestsuiteTmpFolder();

  public static final String REPOSITORY_UNIQUE_ID_SLOT_NAME = "repositoryUniqueId";

  /**
   * Read the patient data name, KVNR and birth date from the given {@link PRPAIN201306UV02} object.
   *
   * @param response {@link PRPAIN201306UV02} as received in an identifyPatient operation from the
   *     NCPeH FD
   * @return {@link Patient} object containing the patient data
   */
  public static Patient readPatientDataFromIdentifyPatientResponse(
      final PRPAIN201306UV02 response) {
    return readPatientDataFromControlActProcess(response.getControlActProcess());
  }

  /**
   * Reads the acknowledgement detail from the given PRPAIN201306UV02 response.
   *
   * @param response the PRPAIN201306UV02 response object received in an identifyPatient operation
   *     from the NCPeH FD
   * @return an AcknowledgementDetail object containing the code, text, and location of the
   *     acknowledgement detail, or null if no acknowledgement detail is found
   */
  public static AcknowledgementDetail readAcknowledgementDetailFromIdentifyPatientResponse(
      final PRPAIN201306UV02 response) {
    return response.getAcknowledgement().stream()
        .flatMap(ack -> ack.getAcknowledgementDetail().stream())
        .findFirst()
        .map(
            ackDetail ->
                new AcknowledgementDetail(
                    Optional.ofNullable(ackDetail.getCode()).map(CD::getCode).orElse(null),
                    Optional.ofNullable(ackDetail.getText())
                        .map(ED::getContent)
                        .map(List::getFirst)
                        .orElse(null),
                    Optional.ofNullable(ackDetail.getLocation())
                        .map(List::getFirst)
                        .map(ST::getContent)
                        .map(List::getFirst)
                        .orElse(null)))
        .orElse(null);
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
      final PRPAIN201306UV02 response) {
    try {
      return readReasonEncodingFromControlActProcess(response.getControlActProcess());
    } catch (final Exception e) {
      return Collections.emptyList();
    }
  }

  /**
   * Read the structured patient summary in the CDA3 format ({@link ClinicalDocument}) from an NCPeH
   * retrieveDocument response
   *
   * @param response {@link RetrievePatientSummaryDO} response object of the {@link
   *     NcpehService#retrievePatientSummary(Patient, String, String, AdhocQueryResponse, String,
   *     PatientSummaryLevel...)} operation
   * @return {@link ClinicalDocument} patient summary, or null if non was found
   */
  public static ClinicalDocument readPatientSummaryLvl3(
      final RetrieveDocumentSetResponseType response) {
    return Optional.ofNullable(getPatientSummaryOfLevel(response, PatientSummaryLevel.LEVEL_3))
        .map(dr -> unmarshalXml(ClinicalDocument.class, dr))
        .orElse(null);
  }

  /**
   * Read the patient summary in the CDA1 format (PDF) from an NCPeH retrieveDocument response
   *
   * @param response {@link RetrievePatientSummaryDO} response object of the {@link
   *     NcpehService#retrievePatientSummary(Patient, String, String, AdhocQueryResponse, String,
   *     PatientSummaryLevel...)} operation
   * @return {@link Pdf} patient summary, or null if non was found
   */
  public static Pdf readPatientSummaryLvl1(final RetrieveDocumentSetResponseType response) {
    return Optional.ofNullable(getPatientSummaryOfLevel(response, PatientSummaryLevel.LEVEL_1))
        .map(doc -> new Pdf(Base64.getEncoder().encodeToString(doc)))
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
  public static String writePatientSummaryToLocalFile(
      final Patient patient, final Pdf cda1Document) {
    final var tempFile = createTempFile(".pdf", patient.kvnr(), "Patient_Summary_Level_1");

    final var pdfBytes = cda1Document.getContent().getBytes(StandardCharsets.UTF_8);
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
  public static IdentifyPatientDO convertResponseDataForIdentifyPatient(final Response response) {
    final var simulatorComData = response.readEntity(SimulatorCommunicationData.class);

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
  public static FindPatientSummaryDO convertResponseDataForFindPatientSummary(
      final Response response) {
    final var simulatorComData = response.readEntity(SimulatorCommunicationData.class);

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
      final Response response) {
    final var simulatorComData = response.readEntity(SimulatorCommunicationData.class);

    return new RetrievePatientSummaryDO(
        HttpStatus.valueOf(response.getStatus()),
        parseHttpRequest(simulatorComData.requestSend(), RetrieveDocumentSetRequestType.class),
        parseHttpResponse(
            simulatorComData.responseReceived(), RetrieveDocumentSetResponseType.class));
  }

  @SuppressWarnings("unchecked")
  public static PersonName createNameFromPNList(final List<PN> nameList) {
    final var names =
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

  public static AdhocQueryResponse setRepositoryUniqueId(
      final AdhocQueryResponse psaMetadata, final String repositoryUniqueId) {
    // Replace repositoryUniqueId for all ExtrinsicObjects
    psaMetadata.getRegistryObjectList().getIdentifiable().stream()
        .filter(d -> d.getValue() instanceof ExtrinsicObjectType)
        .map(JAXBElement::getValue)
        .map(IdentifiableType::getSlot)
        .flatMap(Collection::stream)
        .filter(s -> REPOSITORY_UNIQUE_ID_SLOT_NAME.equals(s.getName()))
        .forEach(
            s -> {
              s.getValueList().getValue().clear();
              s.getValueList().getValue().add(repositoryUniqueId);
            });

    return psaMetadata;
  }

  public static @NotNull Optional<RegistryResponseType> getRegistryResponseType(
      final RetrievePatientSummaryDO retrievePatientSummaryDO) {
    return Optional.ofNullable(retrievePatientSummaryDO)
        .map(NcpehInterfaceResponse::ncpehFdResponseContent)
        .map(RetrieveDocumentSetResponseType::getRegistryResponse);
  }

  // region private

  private static File createTempFile(
      @NonNull final String fileNameSuffix,
      @NonNull final String firstNamePart,
      final String... furtherNameParts)
      throws IOException {
    final var nameParts = new ArrayList<String>();
    nameParts.add(firstNamePart);
    Optional.ofNullable(furtherNameParts).ifPresent(fnp -> nameParts.addAll(Arrays.asList(fnp)));
    nameParts.add(LocalDateTime.now().format(BASIC_DATE_TIME_FORMAT));

    final var file =
        File.createTempFile(String.join("_", nameParts), fileNameSuffix, testsuiteTempDir());
    final var fileRestricted = file.setWritable(true, true) && file.setExecutable(true, true);
    if (!fileRestricted) {
      log.warn("Access rights of temporary file could not be set as requested");
    }
    file.deleteOnExit();

    return file;
  }

  @SneakyThrows
  private static File getOrCreateTestsuiteTmpFolder() {
    final var tempDirPath = Paths.get(System.getProperty("java.io.tmpdir"));
    final var testsuiteTempDirPath = tempDirPath.resolve(TESTSUITE_PATH_PART);

    if (Files.notExists(testsuiteTempDirPath)) {
      Files.createDirectory(testsuiteTempDirPath);
    }

    return testsuiteTempDirPath.toFile();
  }

  private static Patient readPatientDataFromControlActProcess(
      final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess) {
    final var pats =
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

    final var pat = pats.getFirst();

    final var patient = new PatientImpl();

    patient.kvnr(readKvnrFromPATPatient(pat));

    patient.accessCode(readAccessCodeFromPATPatient(pat));

    final var patPerson = pat.getPatientPerson().getValue();

    patient.name(readNameFromPatientPerson(patPerson));

    patient.birthDate(readBirthDateFromPatientPerson(patPerson));

    return patient;
  }

  private static List<String> readReasonEncodingFromControlActProcess(
      final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess) {
    return controlActProcess.getReasonOf().stream()
        .map(MFMIMT700711UV01Reason::getDetectedIssueEvent)
        .map(MCAIMT900001UV01DetectedIssueEvent::getMitigatedBy)
        .flatMap(Collection::stream)
        .map(MCAIMT900001UV01SourceOf::getDetectedIssueManagement)
        .map(MCAIMT900001UV01DetectedIssueManagement::getCode)
        .filter(code -> CODE_SYSTEM_XCPD_ERRORS.contains(code.getCodeSystem()))
        .map(CD::getCode)
        .toList();
  }

  @SneakyThrows
  private static String readKvnrFromPATPatient(final PRPAMT201310UV02Patient patPatient) {
    final var value = readKvnrConcAccessCodeFromPATPatient(patPatient)[0];

    if (Objects.isNull(value) || value.isEmpty())
      throw new ParseException("No extension (KVNR) found in patient data", 0);

    return value;
  }

  private static PersonName readNameFromPatientPerson(final PRPAMT201310UV02Person patientPerson) {
    return createNameFromPNList(patientPerson.getName());
  }

  private static LocalDate readBirthDateFromPatientPerson(
      final PRPAMT201310UV02Person patientPerson) {
    return LocalDate.parse(
        patientPerson.getBirthTime().getValue(),
        new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter());
  }

  @SneakyThrows
  private static String readAccessCodeFromPATPatient(final PRPAMT201310UV02Patient patPatient) {
    final var value = readKvnrConcAccessCodeFromPATPatient(patPatient)[1];

    if (Objects.isNull(value) || value.isEmpty())
      throw new ParseException("No access code found in patient data", 0);

    return value;
  }

  @SneakyThrows
  private static String[] readKvnrConcAccessCodeFromPATPatient(
      final PRPAMT201310UV02Patient patPatient) {
    final var kvnrAccessCode =
        patPatient.getId().stream()
            .map(II::getExtension)
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No extension (KVNR) and access code found in patient data"));

    final var parts = kvnrAccessCode.split("\\|");

    if (parts.length != 2)
      throw new ParseException(
          "The element supposed to hold KVNR and access code does not have the expected format! Received element: "
              + kvnrAccessCode,
          10);

    return parts;
  }

  private static Hashtable<String, String> jaxbElementToTable(
      final JAXBElement<? extends ENXP> jaxbElement) {
    final var res = new Hashtable<String, String>();
    res.put(
        jaxbElement.getName().getLocalPart(),
        jaxbElement.getValue().getContent().stream()
            .reduce((result, value) -> result + " " + value)
            .orElse(""));
    return res;
  }

  private static byte[] getPatientSummaryOfLevel(
      @NonNull final RetrieveDocumentSetResponseType response, final PatientSummaryLevel level) {
    return response.getDocumentResponse().stream()
        .filter(dr -> level.documentIsOfLevel(dr.getDocumentUniqueId()))
        .map(DocumentResponse::getDocument)
        .findFirst()
        .orElse(null);
  }

  private static <T> HttpRequestData<T> parseHttpRequest(
      final WrappedHttpRequest request, final Class<T> requestBodyType) {
    final var requestContent = parseHttpBody(request.messageContent(), requestBodyType);
    final var requestHeader = parseHttpHeaders(request.messageContent());
    final var requestLineSeparatorPosition = request.requestLine().indexOf(" ");
    return new HttpRequestDataRecord<>(
        HttpMethod.valueOf(request.requestLine().substring(0, requestLineSeparatorPosition).trim()),
        request.requestLine().substring(requestLineSeparatorPosition).trim(),
        requestHeader,
        requestContent);
  }

  private static <T> HttpResponseData<T> parseHttpResponse(
      final WrappedHttpResponse response, final Class<T> responseBodyType) {
    final var responseContent = parseHttpBody(response.messageContent(), responseBodyType);
    final var responseHeader = parseHttpHeaders(response.messageContent());

    return new HttpResponseDataRecord<>(
        HttpStatus.valueOf(Integer.parseInt(response.statusLine().substring(0, 3))),
        responseHeader,
        responseContent);
  }

  private static <T> T parseHttpBody(
      final WrappedHttpMessage httpMessage, final Class<T> bodyType) {
    final var soapMessage = httpMessage.httpBody();
    final var doc = extractBodyAsDocument(soapMessage);
    return unmarshalXml(bodyType, doc);
  }

  private static Map<String, String> parseHttpHeaders(final WrappedHttpMessage httpMessage) {
    var headersAsString = new String(httpMessage.httpHeader(), StandardCharsets.UTF_8);
    headersAsString = Utils.trimCharacters(headersAsString, "[", "]");

    return headersAsString
        .lines()
        .flatMap(line -> Arrays.stream(Utils.splitConsideringQuotes(line, ",")))
        .map(
            headerLine -> {
              final var colonIndex = headerLine.indexOf(":");
              final var headerMap = new HashMap<String, String>();
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
