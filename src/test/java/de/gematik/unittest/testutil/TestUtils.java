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

package de.gematik.unittest.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryDO;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.io.FileUtils;
import org.hl7.v3.CD;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueEvent;
import org.hl7.v3.MCAIMT900001UV01DetectedIssueManagement;
import org.hl7.v3.MCAIMT900001UV01SourceOf;
import org.hl7.v3.MFMIMT700711UV01Reason;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;

@Accessors(fluent = true)
public class TestUtils {

  private static final String UNIT_TEST_RESOURCES_PATH = "src/test/resources/unittest/";

  public static final String IDENTIFY_PATIENT_RESPONSE_PATH =
      UNIT_TEST_RESOURCES_PATH + "identifyPatientResponse.json";

  public static final String FIND_DOCUMENT_RESPONSE_PATH =
      UNIT_TEST_RESOURCES_PATH + "findDocumentResponse.json";

  public static final String RETRIEVE_DOCUMENT_RESPONSE_PATH =
      UNIT_TEST_RESOURCES_PATH + "retrieveDocumentResponse.json";

  public static final String UNKNOWN_KVNR_RESPONSE_REASON = "AnswerNotAvailable";

  private static ObjectMapper objMapper;

  @Getter(lazy = true)
  private static final SimulatorCommunicationData identifyPatientResponse =
      loadDtoFromJsonFile(SimulatorCommunicationData.class, IDENTIFY_PATIENT_RESPONSE_PATH);

  @Getter(lazy = true)
  private static final SimulatorCommunicationData findDocumentResponse =
      loadDtoFromJsonFile(SimulatorCommunicationData.class, FIND_DOCUMENT_RESPONSE_PATH);

  @Getter(lazy = true)
  private static final SimulatorCommunicationData retrieveDocumentResponse =
      loadDtoFromJsonFile(SimulatorCommunicationData.class, RETRIEVE_DOCUMENT_RESPONSE_PATH);

  @Getter(lazy = true)
  private static final AdhocQueryResponse adhocQueryResponse =
      DataUtils.convertResponseDataForFindPatientSummary(
              Response.ok(findDocumentResponse()).build())
          .ncpehFdResponseContent();

  @Getter(lazy = true)
  private static final RetrievePatientSummaryDO retrievePatientSummaryDO =
      DataUtils.convertResponseDataForRetrievePatientSummary(
          Response.ok(retrieveDocumentResponse()).build());

  @Getter(lazy = true)
  private static final PRPAIN201306UV02 unknownKvnrResponseContent =
      createUnknownKvnrResponseContent();

  @SneakyThrows
  public static <T> T loadDtoFromJsonFile(final Class<T> dtoClass, final String path) {
    final String fileContent = readFileContentFromResource(path);
    return getObjectMapper().readValue(fileContent, dtoClass);
  }

  @SneakyThrows
  public static String readFileContentFromResource(final String filePath) {
    Objects.requireNonNull(filePath, "The path of the file must not be null!");
    var file = FileUtils.getFile(filePath);
    return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
  }

  public static ObjectMapper getObjectMapper() {
    return Optional.ofNullable(objMapper)
        .orElseGet(
            () -> {
              objMapper = new ObjectMapper();
              //
              // objMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
              //
              // PsTdApiParserHelper.configureJsonObjectMapper(objMapper);
              return objMapper;
            });
  }

  private static PRPAIN201306UV02 createUnknownKvnrResponseContent() {
    var result =
        DataUtils.convertResponseDataForIdentifyPatient(
                Response.ok(identifyPatientResponse()).build())
            .ncpehFdResponseContent();

    Optional.ofNullable(result.getControlActProcess())
        .map(PRPAIN201306UV02MFMIMT700711UV01ControlActProcess::getSubject)
        .stream()
        .flatMap(Collection::stream)
        .map(PRPAIN201306UV02MFMIMT700711UV01Subject1::getRegistrationEvent)
        .map(PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent::getSubject1)
        .forEach(subj1 -> subj1.setPatient(null));

    var cd = new CD();
    cd.setCode(UNKNOWN_KVNR_RESPONSE_REASON);
    cd.setCodeSystem(DataUtils.CODE_SYSTEM_XCPD_ERROR);

    var detectedIssueMgmt = new MCAIMT900001UV01DetectedIssueManagement();
    detectedIssueMgmt.setCode(cd);

    var sourceOf = new MCAIMT900001UV01SourceOf();
    sourceOf.setDetectedIssueManagement(detectedIssueMgmt);

    var detectedIssueEvent = new MCAIMT900001UV01DetectedIssueEvent();
    detectedIssueEvent.getMitigatedBy().add(sourceOf);

    var reason = new MFMIMT700711UV01Reason();
    reason.setDetectedIssueEvent(detectedIssueEvent);

    result.getControlActProcess().getReasonOf().add(reason);

    return result;
  }
}
