/*
 * Copyright (Change Date see Readme), gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes
 * by gematik, find details in the "Readme" file.
 */

package de.gematik.test.ncp.ncpeh.client;

import static de.gematik.test.ncp.ncpeh.client.NcpehClientImpl.REQUEST_HEADER_MEDICATION;
import static de.gematik.test.ncp.ncpeh.client.NcpehClientImpl.REQUEST_HEADER_PATIENT;
import static de.gematik.test.ncp.ncpeh.client.NcpehClientImpl.REQUEST_HEADER_RESPONSE_FILE;
import static de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils.readPatientDataFromIdentifyPatientResponse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.common.PrescriptionDispenseData;
import de.gematik.ncpeh.api.request.DocumentRequest;
import de.gematik.ncpeh.api.response.ErrorInformation;
import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.GeneralFactory;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.ncpeh.NcpehProvider;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.ncp.utils.TestUtils;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.cxf.jaxrs.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class NcpehClientImplTest {

  private static final String KVNR = "X110571344";

  private static final PatientAccessData PATIENT_ACCESS_DATA =
      new PatientAccessData() {
        @Override
        public String getKvnr() {
          return KVNR;
        }

        @Override
        public String getAccessCode() {
          return "123456";
        }
      };

  private static final Patient PATIENT =
      new PatientImpl(
          PersonName.fromString("Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY"),
          KVNR,
          LocalDate.of(1967, 6, 30));

  private static final String PATIENT_JSON;

  private static final String ADHOC_QUERY_RESPONSE_FILE_NAME = "AdhocQueryResponse.xml";
  private static final String COUNTRY = EuCountryCode.DENMARK.name();
  private static final String FIND_DOCUMENT_RESPONSE_FILE_NAME = "findDocumentResponse.json";
  private static final String IDENTIFY_PATIENT_RESPONSE_FILE_NAME = "identifyPatientResponse.json";
  private static final String NCPEH_HEADER_VALUE = "header";
  private static final String OID_AC_EPKA_ASSIGNING_AUTHORITY = "1.2.276.0.76.4.298";
  private static final String RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME =
      "retrieveDocumentResponse.json";
  private static final String XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA =
      "('60591-5^^2.16.840.1.113883.6.1')";
  private static final String PROVIDE_AND_REGISTER_DOCUMENT_SET_RESPONSE_FILE_NAME =
      "provideAndRegisterDocumentSetResponse.json";

  static {
    try {
      // Use Tiger object mapper, because it is configured to handle json attributes lowercase
      PATIENT_JSON = GeneralFactory.getObjectMapper().writeValueAsString(PATIENT);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
  private final ExternalServerConfig config = NcpehProvider.getInstance().getNcpehConfig();

  private NcpehSimulatorApi clientProxy;
  private NcpehClientImpl client;

  @BeforeEach
  void setup() {
    headers.clear();
    clientProxy = mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    when(((Client) clientProxy).reset()).thenReturn(((Client) clientProxy));
    client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
  }

  @Test
  void identifyPatient() {
    // Arrange
    when(clientProxy.identifyPatient(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        IDENTIFY_PATIENT_RESPONSE_FILE_NAME))
                .build());

    // Act
    final var result =
        assertDoesNotThrow(
            () ->
                client.identifyPatient(
                    PATIENT_ACCESS_DATA,
                    "default",
                    COUNTRY,
                    OID_AC_EPKA_ASSIGNING_AUTHORITY,
                    NCPEH_HEADER_VALUE,
                    PATIENT),
            "method identifyPatient threw exception");

    // Assert
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    final var patient = readPatientDataFromIdentifyPatientResponse(result.ncpehFdResponseContent());

    assertNotNull(patient);
    assertTrue(PATIENT.samePerson(patient));
    headers.put(REQUEST_HEADER_RESPONSE_FILE, List.of(NCPEH_HEADER_VALUE));
    verify((Client) clientProxy).headers(any());
  }

  @Test
  void identifyPatientThrowsWhenProfileIsMissing() {
    final var missingProfileName = "pharmacistPowelsBelgium";

    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                client.identifyPatient(
                    PATIENT_ACCESS_DATA,
                    missingProfileName,
                    COUNTRY,
                    OID_AC_EPKA_ASSIGNING_AUTHORITY,
                    NCPEH_HEADER_VALUE,
                    PATIENT));

    assertTrue(exception.getMessage().contains(missingProfileName));
    assertTrue(exception.getMessage().contains("testdata.profiles"));
    verifyNoInteractions(clientProxy);
  }

  @Test
  void identifyPatientHeaderIsNullTest() {
    // Arrange
    when(clientProxy.identifyPatient(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        IDENTIFY_PATIENT_RESPONSE_FILE_NAME))
                .build());

    // Act
    final var result =
        assertDoesNotThrow(
            () ->
                client.identifyPatient(
                    PATIENT_ACCESS_DATA,
                    "default",
                    COUNTRY,
                    OID_AC_EPKA_ASSIGNING_AUTHORITY,
                    null,
                    null),
            "method identifyPatient threw exception");

    // Assert
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    final var patient = readPatientDataFromIdentifyPatientResponse(result.ncpehFdResponseContent());
    assertNotNull(patient);
    assertTrue(PATIENT.samePerson(patient));

    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void identifyPatientThrows() {
    // Arrange
    final var errorInfo = new ErrorInformation("a spectacular error occurred");
    when(clientProxy.identifyPatient(any()))
        .thenReturn(Response.status(Response.Status.BAD_REQUEST).entity(errorInfo).build());

    // Act
    final var exception =
        assertThrows(
            NcpehException.class,
            () ->
                client.identifyPatient(
                    PATIENT_ACCESS_DATA,
                    "default",
                    COUNTRY,
                    OID_AC_EPKA_ASSIGNING_AUTHORITY,
                    null,
                    PATIENT),
            "method identifyPatient did not throw exception");

    // Assert
    assertTrue(exception.getMessage().contains(errorInfo.errorMessage()));
  }

  @Test
  void findDocumentsTest() {
    // Arrange
    when(clientProxy.findDocuments(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        FIND_DOCUMENT_RESPONSE_FILE_NAME))
                .build());

    // Act
    final var result =
        assertDoesNotThrow(
            () ->
                client.findDocuments(
                    PATIENT_ACCESS_DATA,
                    "default",
                    COUNTRY,
                    XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA,
                    NCPEH_HEADER_VALUE,
                    null),
            "method findDocuments threw exception");

    // Assert
    assertNotNull(result, "method findDocuments returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    headers.put(REQUEST_HEADER_RESPONSE_FILE, List.of(NCPEH_HEADER_VALUE));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void findDocumentsHeaderIsNullTest() {
    // Arrange
    when(clientProxy.findDocuments(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        FIND_DOCUMENT_RESPONSE_FILE_NAME))
                .build());

    // Act
    final var result =
        assertDoesNotThrow(
            () ->
                client.findDocuments(
                    PATIENT_ACCESS_DATA,
                    "default",
                    COUNTRY,
                    XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA,
                    null,
                    null),
            "method findDocuments threw exception");

    // Assert
    assertNotNull(result, "method findDocuments returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void findDocumentsThrowsTest() {
    // Arrange
    when(clientProxy.findDocuments(any()))
        .thenReturn(
            Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorInformation("error"))
                .build());

    // Act
    // Assert
    assertThrows(
        NcpehException.class,
        () ->
            client.findDocuments(
                PATIENT_ACCESS_DATA,
                "default",
                COUNTRY,
                XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA,
                null,
                null),
        "method findDocuments did not throw exception");
  }

  @Test
  void retrievePatientSummaryTest() {
    // Arrange
    when(clientProxy.retrieveDocument(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME))
                .build());
    final var metadata =
        TestUtils.loadFromXMLResource(
            AdhocQueryResponse.class, this.getClass(), ADHOC_QUERY_RESPONSE_FILE_NAME);

    // Act
    final var result =
        assertDoesNotThrow(
            () ->
                client.retrievePatientSummary(
                    PATIENT_ACCESS_DATA, PATIENT, "default", COUNTRY, metadata, NCPEH_HEADER_VALUE),
            "method retrievePatientSummary threw exception");

    // Assert
    assertNotNull(result, "method retrievePatientSummary returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
    headers.put(
        REQUEST_HEADER_PATIENT,
        List.of(Base64.getEncoder().encodeToString(PATIENT_JSON.getBytes(StandardCharsets.UTF_8))));
    headers.put(REQUEST_HEADER_RESPONSE_FILE, List.of(NCPEH_HEADER_VALUE));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void retrievePatientSummaryHeaderIsNullTest() {
    // Arrange
    when(clientProxy.retrieveDocument(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME))
                .build());
    final var metadata =
        TestUtils.loadFromXMLResource(
            AdhocQueryResponse.class, this.getClass(), ADHOC_QUERY_RESPONSE_FILE_NAME);

    // Act
    final var result =
        assertDoesNotThrow(
            () ->
                client.retrievePatientSummary(
                    PATIENT_ACCESS_DATA, PATIENT, "default", COUNTRY, metadata, null),
            "method retrievePatientSummary threw exception");

    // Assert
    assertNotNull(result, "method retrievePatientSummary returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
    headers.put(
        REQUEST_HEADER_PATIENT,
        List.of(Base64.getEncoder().encodeToString(PATIENT_JSON.getBytes(StandardCharsets.UTF_8))));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void retrievePatientSummaryThrowsTest() {
    // Arrange
    when(clientProxy.retrieveDocument(any()))
        .thenReturn(
            Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorInformation("error"))
                .build());
    final var metadata =
        TestUtils.loadFromXMLResource(
            AdhocQueryResponse.class, this.getClass(), ADHOC_QUERY_RESPONSE_FILE_NAME);

    // Act
    // Assert
    assertThrows(
        NcpehException.class,
        () ->
            client.retrievePatientSummary(
                PATIENT_ACCESS_DATA, PATIENT, "default", COUNTRY, metadata, null),
        "method retrievePatientSummary did not throw exception");
  }

  @Test
  void retrieveSetOfDocumentsTest() {
    // Arrange
    when(clientProxy.retrieveSetOfDocuments(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME))
                .build());

    var prescriptionId = "1.2.3.4.5.6";
    var medication = new Medication("DummyMed", "12345678", true);
    var docRequest = new DocumentRequest("hcId", "repoUid", prescriptionId);

    // Act
    var medicationByPrescriptionId = Map.of(prescriptionId, medication);
    var result =
        assertDoesNotThrow(
            () ->
                client.retrieveDocuments(
                    PATIENT_ACCESS_DATA,
                    PATIENT,
                    "default",
                    COUNTRY,
                    Set.of(docRequest),
                    NCPEH_HEADER_VALUE,
                    medicationByPrescriptionId),
            "method retrieveDocuments threw exception");

    // Assert
    assertNotNull(result, "method retrieveSetOfDocuments returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
    headers.put(
        REQUEST_HEADER_PATIENT,
        List.of(Base64.getEncoder().encodeToString(PATIENT_JSON.getBytes(StandardCharsets.UTF_8))));
    headers.put(REQUEST_HEADER_RESPONSE_FILE, List.of(NCPEH_HEADER_VALUE));
    headers.put(REQUEST_HEADER_MEDICATION, List.of(Utils.getB64Json(medicationByPrescriptionId)));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void retrieveSetOfDocuments_medicationHeaderIsNullTest() {
    // Arrange
    when(clientProxy.retrieveSetOfDocuments(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME))
                .build());

    var prescriptionId = "1.2.3.4.5.6";
    var docRequest = new DocumentRequest("hcId", "repoUid", prescriptionId);

    // Act
    var result =
        assertDoesNotThrow(
            () ->
                client.retrieveDocuments(
                    PATIENT_ACCESS_DATA,
                    PATIENT,
                    "default",
                    COUNTRY,
                    Set.of(docRequest),
                    NCPEH_HEADER_VALUE,
                    null),
            "method retrieveDocuments threw exception");

    // Assert
    assertNotNull(result, "method retrievePatientSummary returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
    headers.put(
        REQUEST_HEADER_PATIENT,
        List.of(Base64.getEncoder().encodeToString(PATIENT_JSON.getBytes(StandardCharsets.UTF_8))));
    headers.put(REQUEST_HEADER_RESPONSE_FILE, List.of(NCPEH_HEADER_VALUE));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void retrieveSetOfDocumentsThrowsTest() {
    // Arrange
    when(clientProxy.retrieveSetOfDocuments(any()))
        .thenReturn(
            Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorInformation("error"))
                .build());

    // Act
    // Assert
    assertThrows(
        NcpehException.class,
        () ->
            client.retrieveDocuments(
                PATIENT_ACCESS_DATA, null, "default", COUNTRY, null, null, null),
        "method retrieveDocuments did not throw exception");
  }

  @Test
  void provideAndRegisterDocumentSet_shouldThrowOnFailedRequest() {
    // Arrange
    when(clientProxy.provideAndRegisterSetOfDocuments(any()))
        .thenReturn(
            Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorInformation("error"))
                .build());

    var p1 = new PrescriptionDispenseData("aid", true);
    var p2 = new PrescriptionDispenseData("bid", false);

    // Act & Assert
    assertThrows(
        NcpehException.class,
        () ->
            client.provideAndRegisterDocumentSet(
                PATIENT_ACCESS_DATA,
                "default",
                COUNTRY,
                List.of(p1, p2),
                "dummyFormat",
                "dummyType",
                "dummyClass",
                null),
        "method provideAndRegisterDocumentSet did not throw exception");
  }

  @Test
  void provideAndRegisterDocumentSet_() {
    // Arrange
    when(clientProxy.provideAndRegisterSetOfDocuments(any()))
        .thenReturn(
            Response.ok()
                .entity(
                    TestUtils.loadFromJsonResource(
                        SimulatorCommunicationData.class,
                        this.getClass(),
                        PROVIDE_AND_REGISTER_DOCUMENT_SET_RESPONSE_FILE_NAME))
                .build());

    var p1 = new PrescriptionDispenseData("aid", true);
    var p2 = new PrescriptionDispenseData("bid", false);

    // Act & Assert
    var result =
        assertDoesNotThrow(
            () ->
                client.provideAndRegisterDocumentSet(
                    PATIENT_ACCESS_DATA,
                    "default",
                    COUNTRY,
                    List.of(p1, p2),
                    "dummyFormat",
                    "dummyType",
                    "dummyClass",
                    NCPEH_HEADER_VALUE),
            "method provideAndRegisterDocumentSet threw exception");

    assertNotNull(result, "method provideAndRegisterDocumentSet returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
    headers.put(REQUEST_HEADER_RESPONSE_FILE, List.of(NCPEH_HEADER_VALUE));
    verify((Client) clientProxy).headers(headers);
  }
}
