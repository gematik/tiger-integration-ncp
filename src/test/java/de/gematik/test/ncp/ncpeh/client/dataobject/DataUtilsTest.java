/*
 * Copyright 2024-2025 gematik GmbH
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
 *
 * ******
 *
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.ncpeh.client.dataobject;

import static de.gematik.test.ncp.utils.TestUtils.marshalXml;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.data.AcknowledgementDetail;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.utils.TestUtils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import javax.xml.namespace.QName;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.xmlunit.matchers.CompareMatcher;

class DataUtilsTest {

  private static final String KVNR = "X110571344";

  private static final Patient PATIENT =
      new PatientImpl(
          PersonName.fromString("Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY"),
          KVNR,
          LocalDate.of(1967, 6, 30));

  private static final String PRPA_IN_201306_UV_02_XML_FILE_NAME = "PRPA_IN201306UV02.xml";
  private static final String PRPA_IN_201306_UV_02_4_XML_FILE_NAME = "PRPA_IN201306UV02_4.xml";
  private static final String PRPA_IN_201306_UV_02_45_XML_FILE_NAME = "PRPA_IN201306UV02_45.xml";
  private static final String PRPA_IN_201306_UV_02_01_XML_FILE_NAME = "PRPA_IN201306UV02_01.xml";
  private static final String RETRIEVE_DOCUMENT_SET_RESPONSE_LVL_3_XML_FILE_NAME =
      "RetrieveDocumentSetResponse_Lvl3.xml";
  private static final String RETRIEVE_DOCUMENT_SET_RESPONSE_LVL_1_XML_FILE_NAME =
      "RetrieveDocumentSetResponse_Lvl1.xml";
  private static final String RETRIEVE_DOCUMENT_SET_RESPONSE_020_XML_FILE_NAME =
      "RetrieveDocumentSetResponse_020.xml";
  private static final String IDENTIFY_PATIENT_RESPONSE_FILE_NAME = "identifyPatientResponse.json";
  private static final String FIND_DOCUMENT_RESPONSE_FILE_NAME = "findDocumentResponse.json";
  private static final String RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME =
      "retrieveDocumentResponse.json";

  private static final String UNKNOWN_KVNR_RESPONSE_REASON = "AnswerNotAvailable";
  private static final String INSUFFICIENT_RIGHTS_RESPONSE_REASON = "InsufficientRights";
  private static final String PRPA_IN_201305_UV_02_298_XML_FILE_NAME = "PRPA_IN201305UV02_298.xml";
  private static final String ADHOC_QUERY_REQUEST_XML_FILE_NAME = "AdhocQueryRequest.xml";
  private static final String ADHOC_QUERY_RESPONSE_XML_FILE_NAME = "AdhocQueryResponse.xml";
  private static final String ADHOC_QUERY_RESPONSE_010_XML_FILE_NAME = "AdhocQueryResponse_010.xml";
  private static final String RETRIEVE_DOCUMENT_SET_REQUEST_XML_FILE_NAME =
      "RetrieveDocumentSetRequest.xml";
  private static final String RETRIEVE_DOCUMENT_SET_RESPONSE_XML_FILE_NAME =
      "RetrieveDocumentSetResponse.xml";
  private static final String ADHOC_QUERY_RESPONSE_SET_REPOSITORY_UNIQUE_ID_EXPEXTED_XML_FILE_NAME =
      "AdhocQueryResponse_setRepositoryUniqueId_Expexted.xml";

  @Test
  void readPatientDataFromIdentifyPatientResponseTest() {
    // Arrange
    final var data =
        TestUtils.loadFromXMLResource(
            PRPAIN201306UV02.class, this.getClass(), PRPA_IN_201306_UV_02_XML_FILE_NAME);

    // Act
    final var patient =
        assertDoesNotThrow(() -> DataUtils.readPatientDataFromIdentifyPatientResponse(data));

    // Assert
    assertNotNull(patient);
    assertTrue(PATIENT.samePerson(patient));
  }

  @Test
  void readPatientDataFromIdentifyPatientResponseNoPatientTest() {
    // Arrange
    final var data =
        TestUtils.loadFromXMLResource(
            PRPAIN201306UV02.class, this.getClass(), PRPA_IN_201306_UV_02_4_XML_FILE_NAME);

    // Act
    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> DataUtils.readPatientDataFromIdentifyPatientResponse(data));

    // Assert
    assertEquals(
        "No patient data were found in the identifyPatient response", exception.getMessage());
  }

  @Test
  void readPatientDataFromIdentifyPatientResponseTwoPatientTest() {
    // Arrange
    final var data =
        TestUtils.loadFromXMLResource(
            PRPAIN201306UV02.class, this.getClass(), PRPA_IN_201306_UV_02_01_XML_FILE_NAME);

    // Act
    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> DataUtils.readPatientDataFromIdentifyPatientResponse(data));

    // Assert
    assertEquals(
        "Data to more than one patient were found in the identifyPatient response",
        exception.getMessage());
  }

  @ParameterizedTest
  @CsvSource({
    PRPA_IN_201306_UV_02_4_XML_FILE_NAME + ", " + UNKNOWN_KVNR_RESPONSE_REASON,
    PRPA_IN_201306_UV_02_45_XML_FILE_NAME + ", " + INSUFFICIENT_RIGHTS_RESPONSE_REASON
  })
  void readReasonEncodingFromIdentifyPatientResponseTest(
      final String input, final String expected) {
    // Arrange
    final var data = TestUtils.loadFromXMLResource(PRPAIN201306UV02.class, this.getClass(), input);

    // Act
    final var result =
        assertDoesNotThrow(() -> DataUtils.readReasonEncodingFromIdentifyPatientResponse(data));

    // Assert
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.contains(expected));
  }

  @Test
  void readPatientSummaryLvl3Test() {
    // Arrange
    final var data =
        TestUtils.loadFromXMLResource(
            RetrieveDocumentSetResponseType.class,
            this.getClass(),
            RETRIEVE_DOCUMENT_SET_RESPONSE_LVL_3_XML_FILE_NAME);

    // Act
    final var patientSummary = assertDoesNotThrow(() -> DataUtils.readPatientSummaryLvl3(data));

    // Assert
    assertNotNull(patientSummary);
    assertFalse(patientSummary.getRecordTarget().isEmpty());

    final var recordTarget = patientSummary.getRecordTarget().getFirst();
    assertNotNull(recordTarget.getPatientRole());
    assertNotNull(recordTarget.getPatientRole().getPatient());
    assertFalse(recordTarget.getPatientRole().getElementId().isEmpty());
  }

  @Test
  void readPatientSummaryLvl1Test() {
    // Arrange
    final var data =
        TestUtils.loadFromXMLResource(
            RetrieveDocumentSetResponseType.class,
            this.getClass(),
            RETRIEVE_DOCUMENT_SET_RESPONSE_LVL_1_XML_FILE_NAME);

    // Act
    final var patientSummary = assertDoesNotThrow(() -> DataUtils.readPatientSummaryLvl1(data));

    // Assert
    assertNotNull(patientSummary);
  }

  @Test
  void readAcknowledgementDetailFromIdentifyPatientResponseTest() {
    // Arrange
    final var data =
        TestUtils.loadFromXMLResource(
            PRPAIN201306UV02.class, this.getClass(), PRPA_IN_201306_UV_02_4_XML_FILE_NAME);

    // Act
    final var result =
        assertDoesNotThrow(
            () -> DataUtils.readAcknowledgementDetailFromIdentifyPatientResponse(data));

    // Assert
    assertNotNull(result);
    assertEquals(
        new AcknowledgementDetail(
            "ERROR_PI_NO_MATCH",
            "Patient Identification Error",
            "No match with an existing patient."),
        result);
  }

  @Test
  void readAcknowledgementDetailFromIdentifyPatientResponseNoAcknowledgementDetailTest() {
    // Arrange
    final var data =
        TestUtils.loadFromXMLResource(
            PRPAIN201306UV02.class, this.getClass(), PRPA_IN_201306_UV_02_XML_FILE_NAME);

    // Act
    final var result =
        assertDoesNotThrow(
            () -> DataUtils.readAcknowledgementDetailFromIdentifyPatientResponse(data));

    // Assert
    assertNull(result);
  }

  @Test
  void convertResponseDataForIdentifyPatient() {
    // Arrange
    final var response =
        Response.ok(
                TestUtils.loadFromJsonResource(
                    SimulatorCommunicationData.class,
                    this.getClass(),
                    IDENTIFY_PATIENT_RESPONSE_FILE_NAME))
            .build();

    // Act
    final var testee =
        assertDoesNotThrow(() -> DataUtils.convertResponseDataForIdentifyPatient(response));

    // Assert
    assertNcpehInterfaceResponse(testee);

    final var expectedRequestContent =
        TestUtils.readResourceFile(this.getClass(), PRPA_IN_201305_UV_02_298_XML_FILE_NAME);
    assertThat(
        marshalXml(testee.ncpehFdRequestContent()),
        CompareMatcher.isSimilarTo(expectedRequestContent).ignoreWhitespace().ignoreComments());

    final var expectedResponseContent =
        TestUtils.readResourceFile(this.getClass(), PRPA_IN_201306_UV_02_XML_FILE_NAME);
    assertThat(
        marshalXml(testee.ncpehFdResponseContent()),
        CompareMatcher.isSimilarTo(expectedResponseContent).ignoreWhitespace().ignoreComments());
  }

  @Test
  void convertResponseDataForFindPatientSummary() {
    // Arrange
    final var response =
        Response.ok(
                TestUtils.loadFromJsonResource(
                    SimulatorCommunicationData.class,
                    this.getClass(),
                    FIND_DOCUMENT_RESPONSE_FILE_NAME))
            .build();

    // Act
    final var testee =
        assertDoesNotThrow(() -> DataUtils.convertResponseDataForFindPatientSummary(response));

    // Assert
    assertNcpehInterfaceResponse(testee);

    final var expectedRequestContent =
        TestUtils.readResourceFile(this.getClass(), ADHOC_QUERY_REQUEST_XML_FILE_NAME);
    assertThat(
        marshalXml(testee.ncpehFdRequestContent()),
        CompareMatcher.isSimilarTo(expectedRequestContent).ignoreWhitespace().ignoreComments());

    final var expectedResponseContent =
        TestUtils.readResourceFile(this.getClass(), ADHOC_QUERY_RESPONSE_XML_FILE_NAME);
    assertThat(
        marshalXml(testee.ncpehFdResponseContent()),
        CompareMatcher.isSimilarTo(expectedResponseContent).ignoreWhitespace().ignoreComments());
  }

  @Test
  void convertResponseDataForRetrievePatientSummary() {
    // Arrange
    final var response =
        Response.ok(
                TestUtils.loadFromJsonResource(
                    SimulatorCommunicationData.class,
                    this.getClass(),
                    RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME))
            .build();

    // Act
    final var testee =
        assertDoesNotThrow(() -> DataUtils.convertResponseDataForRetrievePatientSummary(response));

    // Assert
    assertNcpehInterfaceResponse(testee);

    final var requestRootQName = new QName("urn:ihe:iti:xds-b:2007", "RetrieveDocumentSetRequest");
    final var expectedRequestContent =
        TestUtils.readResourceFile(this.getClass(), RETRIEVE_DOCUMENT_SET_REQUEST_XML_FILE_NAME);
    assertThat(
        marshalXml(testee.ncpehFdRequestContent(), requestRootQName),
        CompareMatcher.isSimilarTo(expectedRequestContent).ignoreWhitespace().ignoreComments());

    final var responseRootQName =
        new QName("urn:ihe:iti:xds-b:2007", "RetrieveDocumentSetResponse");
    final var expectedResponseContent =
        TestUtils.readResourceFile(this.getClass(), RETRIEVE_DOCUMENT_SET_RESPONSE_XML_FILE_NAME);
    assertThat(
        marshalXml(testee.ncpehFdResponseContent(), responseRootQName),
        CompareMatcher.isSimilarTo(expectedResponseContent).ignoreWhitespace().ignoreComments());
  }

  @Test
  void getRegistryResponseTypeTest() {
    // Arrange
    final var response =
        TestUtils.loadFromXMLResource(
            RetrieveDocumentSetResponseType.class,
            this.getClass(),
            RETRIEVE_DOCUMENT_SET_RESPONSE_LVL_1_XML_FILE_NAME);
    final var retrievePatientSummaryDO = mock(RetrievePatientSummaryResponseDO.class);
    when(retrievePatientSummaryDO.ncpehFdResponseContent()).thenReturn(response);

    // Act
    final var result = DataUtils.getRegistryResponseType(retrievePatientSummaryDO);

    // Assert
    assertEquals(response.getRegistryResponse(), result.orElse(null));
  }

  @Test
  void getRegistryResponseTypeEmptyResponseTest() {
    // Arrange
    final var retrievePatientSummaryDO = mock(RetrievePatientSummaryResponseDO.class);
    when(retrievePatientSummaryDO.ncpehFdResponseContent()).thenReturn(null);

    // Act
    final var result = DataUtils.getRegistryResponseType(retrievePatientSummaryDO);

    // Assert
    assertFalse(result.isPresent());
  }

  @Test
  void setRepositoryUniqueIdTest() {
    // Arrange
    final var psaMetadata =
        TestUtils.loadFromXMLResource(
            AdhocQueryResponse.class, this.getClass(), ADHOC_QUERY_RESPONSE_XML_FILE_NAME);

    // Act
    final var testee =
        DataUtils.setRepositoryUniqueId(psaMetadata, "2.16.620.1.101.10.3.29.123456");

    // Assert
    final var expected =
        TestUtils.readResourceFile(
            this.getClass(), ADHOC_QUERY_RESPONSE_SET_REPOSITORY_UNIQUE_ID_EXPEXTED_XML_FILE_NAME);
    assertThat(
        marshalXml(testee),
        CompareMatcher.isSimilarTo(expected).ignoreWhitespace().ignoreComments());
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

  @ParameterizedTest
  @CsvSource({
    ADHOC_QUERY_RESPONSE_XML_FILE_NAME + ", 0,",
    ADHOC_QUERY_RESPONSE_010_XML_FILE_NAME + ", 1, ERROR_GENERIC_DOCUMENT_MISSING",
  })
  void readRegistryErrorCodesFromAdhocQueryResponseTest(
      final String data, final int expected, final String message) {
    // Arrange
    final var adhocQueryResponse =
        TestUtils.loadFromXMLResource(AdhocQueryResponse.class, this.getClass(), data);

    // Act
    final var testee =
        assertDoesNotThrow(
            () -> DataUtils.readRegistryErrorCodesFromAdhocQueryResponse(adhocQueryResponse));

    // Assert
    assertEquals(expected, testee.size());
    assertEquals(message, testee.stream().findFirst().orElse(null));
  }

  @ParameterizedTest
  @CsvSource({
    RETRIEVE_DOCUMENT_SET_RESPONSE_LVL_3_XML_FILE_NAME + ", 0,",
    RETRIEVE_DOCUMENT_SET_RESPONSE_020_XML_FILE_NAME + ", 1, ERROR_GENERIC_DOCUMENT_MISSING"
  })
  void readRegistryErrorCodesFromRegistryResponseTest(
      final String data, final int expected, final String message) {
    // Arrange
    final var registryResponse =
        TestUtils.loadFromXMLResource(RetrieveDocumentSetResponseType.class, this.getClass(), data);

    // Act
    final var testee =
        assertDoesNotThrow(
            () ->
                DataUtils.readRegistryErrorCodesFromRegistryResponse(
                    registryResponse.getRegistryResponse()));

    // Assert
    assertEquals(expected, testee.size());
    assertEquals(message, testee.stream().findFirst().orElse(null));
  }
}
