/*
 * Copyright (c) 2024. gematik GmbH
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

package de.gematik.unittest.ncp.ncpeh.client.dataobject;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.NcpehProvider;
import de.gematik.test.ncp.ncpeh.NcpehService.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.NcpehInterfaceResponse;
import de.gematik.unittest.testutil.NcpehClientImplForUnittest;
import de.gematik.unittest.testutil.TestUtils;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.apache.commons.net.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

class DataUtilsTest {

  public static final String KVNR = "X110405394";

  public static final Patient PATIENT =
      new PatientImpl(
          PersonName.fromString("Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY"),
          KVNR,
          LocalDate.of(1967, 6, 30));

  public static final String COUNTRY = EuCountryCode.SPAIN.name();

  @Test
  void readPatientDataFromIdentifyPatientResponseTest() {
    final var testdata =
        new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig())
            .identifyPatient(PATIENT, "default", COUNTRY);

    final var testResult =
        assertDoesNotThrow(
            () ->
                DataUtils.readPatientDataFromIdentifyPatientResponse(
                    testdata.ncpehFdResponseContent()));

    assertNotNull(testResult);
    assertTrue(PATIENT.samePerson(testResult));
  }

  @Test
  void readPatientDataFromIdentifyPatientResponseTestBadCase() {
    final var testdata =
        new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig())
            .identifyPatient(PATIENT, "default", COUNTRY)
            .ncpehFdResponseContent();
    testdata
        .getControlActProcess()
        .getSubject()
        .get(0)
        .getRegistrationEvent()
        .getSubject1()
        .getPatient()
        .getId()
        .get(0)
        .setExtension(null);

    final var testResult =
        assertThrows(
            IllegalArgumentException.class,
            () -> DataUtils.readPatientDataFromIdentifyPatientResponse(testdata));

    assertTrue(testResult.getMessage().contains("KVNR"));
  }

  @Test
  void readPatientDataFromIdentifyPatientResponseNoPatientTest() {
    final var testdata =
        new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig())
            .identifyPatient(PATIENT, "default", COUNTRY)
            .ncpehFdResponseContent();
    testdata
        .getControlActProcess()
        .getSubject()
        .get(0)
        .getRegistrationEvent()
        .getSubject1()
        .setPatient(null);

    final var testResult =
        assertThrows(
            IllegalArgumentException.class,
            () -> DataUtils.readPatientDataFromIdentifyPatientResponse(testdata));

    assertTrue(testResult.getMessage().contains("No patient data were found"));
  }

  @Test
  void readPatientDataFromIdentifyPatientResponseTwoPatientTest() {
    final var testdata =
        new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig())
            .identifyPatient(PATIENT, "default", COUNTRY)
            .ncpehFdResponseContent();
    testdata
        .getControlActProcess()
        .getSubject()
        .add(testdata.getControlActProcess().getSubject().get(0));

    final var testResult =
        assertThrows(
            IllegalArgumentException.class,
            () -> DataUtils.readPatientDataFromIdentifyPatientResponse(testdata));

    assertTrue(testResult.getMessage().contains("Data to more than one patient were found"));
  }

  @Test
  void readReasonEncodingFromIdentifyPatientResponseTest() {
    final var testdata = TestUtils.unknownKvnrResponseContent();

    final var result =
        assertDoesNotThrow(() -> DataUtils.readReasonEncodingFromIdentifyPatientResponse(testdata));

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains(TestUtils.UNKNOWN_KVNR_RESPONSE_REASON));
  }

  @Test
  void readPatientSummaryLvl3Test() {
    final var testdata =
        new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig())
            .retrievePatientSummary(
                PATIENT,
                "default",
                COUNTRY,
                TestUtils.adhocQueryResponse(),
                PatientSummaryLevel.LEVEL_3);

    final var testResult = assertDoesNotThrow(() -> DataUtils.readPatientSummaryLvl3(testdata));

    assertNotNull(testResult);
    assertFalse(testResult.getRecordTarget().isEmpty());

    final var recordTarget = testResult.getRecordTarget().get(0);
    assertNotNull(recordTarget.getPatientRole());
    assertNotNull(recordTarget.getPatientRole().getPatient());
    assertFalse(recordTarget.getPatientRole().getElementId().isEmpty());
  }

  @Test
  void readPatientSummaryLvl1Test() {
    final var testdata =
        new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig())
            .retrievePatientSummary(
                PATIENT,
                "default",
                COUNTRY,
                TestUtils.adhocQueryResponse(),
                PatientSummaryLevel.LEVEL_1);

    final var testResult = assertDoesNotThrow(() -> DataUtils.readPatientSummaryLvl1(testdata));

    assertNotNull(testResult);
    assertNotNull(testResult.getContent());
    assertTrue(Base64.isArrayByteBase64(testResult.getContent().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  void writePatientSummaryToLocalFile() {
    final var testdata =
        new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig())
            .retrievePatientSummary(
                PATIENT,
                "default",
                COUNTRY,
                TestUtils.adhocQueryResponse(),
                PatientSummaryLevel.LEVEL_1);

    final var cda1Document = DataUtils.readPatientSummaryLvl1(testdata);

    final var filePath = DataUtils.writePatientSummaryToLocalFile(PATIENT, cda1Document);

    assertNotNull(filePath);

    final var tempFile = new File(filePath);
    tempFile.deleteOnExit();
    assertTrue(tempFile.exists());
    assertTrue(tempFile.isFile());
    assertTrue(tempFile.canRead());
    tempFile.delete();
  }

  @Test
  void convertResponseDataForIdentifyPatient() {
    final var response = Response.ok(TestUtils.identifyPatientResponse()).build();

    final var result =
        assertDoesNotThrow(() -> DataUtils.convertResponseDataForIdentifyPatient(response));

    assertNcpehInterfaceResponse(result);
  }

  @Test
  void convertResponseDataForFindPatientSummary() {
    final var response = Response.ok(TestUtils.findDocumentResponse()).build();

    final var result =
        assertDoesNotThrow(() -> DataUtils.convertResponseDataForFindPatientSummary(response));

    assertNcpehInterfaceResponse(result);
  }

  @Test
  void convertResponseDataForRetrievePatientSummary() {
    final var response = Response.ok(TestUtils.retrieveDocumentResponse()).build();

    final var result =
        assertDoesNotThrow(() -> DataUtils.convertResponseDataForRetrievePatientSummary(response));

    assertNcpehInterfaceResponse(result);
  }

  private void assertNcpehInterfaceResponse(
      final NcpehInterfaceResponse<?, ?> ncpehInterfaceResponse) {
    assertNotNull(ncpehInterfaceResponse);
    assertEquals(HttpStatus.OK, ncpehInterfaceResponse.ncpehResponseStatus());

    assertNotNull(ncpehInterfaceResponse.ncpehFdRequest());
    assertEquals(HttpMethod.POST, ncpehInterfaceResponse.ncpehFdRequest().method());
    assertNotNull(ncpehInterfaceResponse.ncpehFdRequest().address());
    assertNotNull(ncpehInterfaceResponse.ncpehFdRequestContent());

    assertNotNull(ncpehInterfaceResponse.ncpehFdResponse());
    assertEquals(HttpStatus.OK, ncpehInterfaceResponse.ncpehFdResponse().status());
    assertNotNull(ncpehInterfaceResponse.ncpehFdResponseContent());
  }
}
