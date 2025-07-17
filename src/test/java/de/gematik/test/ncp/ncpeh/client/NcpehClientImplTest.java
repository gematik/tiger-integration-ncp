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

package de.gematik.test.ncp.ncpeh.client;

import static de.gematik.test.ncp.ncpeh.client.NcpehClientImpl.REQUEST_HEADER_X_NCPEHMOCK_PATIENT;
import static de.gematik.test.ncp.ncpeh.client.NcpehClientImpl.REQUEST_HEADER_X_NCPEHMOCK_RESPONSE;
import static de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils.readPatientDataFromIdentifyPatientResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.response.ErrorInformation;
import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.ncpeh.NcpehProvider;
import de.gematik.test.ncp.utils.TestUtils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.cxf.jaxrs.client.Client;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class NcpehClientImplTest {

  private static final String KVNR = "X110571344";

  public static final PatientAccessData PATIENT_ACCESS_DATA =
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

  static {
    try {
      // Use Tiger object mapper, because it is configured to handle json attributes lowercase
      PATIENT_JSON = TigerGlobalConfiguration.getObjectMapper().writeValueAsString(PATIENT);
    } catch (final JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private static final String COUNTRY = EuCountryCode.DENMARK.name();
  private static final String IDENTIFY_PATIENT_RESPONSE_FILE_NAME = "identifyPatientResponse.json";
  private static final String FIND_DOCUMENT_RESPONSE_FILE_NAME = "findDocumentResponse.json";
  private static final String RETRIEVE_DOCUMENT_RESPONSE_FILE_NAME =
      "retrieveDocumentResponse.json";

  private static final String NCPEH_HEADER_VALUE = "header";
  private final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

  public static final String ADHOC_QUERY_RESPONSE_FILE_NAME = "AdhocQueryResponse.xml";

  private final ExternalServerConfig config = NcpehProvider.getInstance().getNcpehConfig();

  @Test
  void identifyPatient() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();

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
                client.identifyPatient(PATIENT_ACCESS_DATA, "default", COUNTRY, NCPEH_HEADER_VALUE),
            "method identifyPatient threw exception");

    // Assert
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    final var patient = readPatientDataFromIdentifyPatientResponse(result.ncpehFdResponseContent());

    assertNotNull(patient);
    assertTrue(PATIENT.samePerson(patient));
    headers.put(REQUEST_HEADER_X_NCPEHMOCK_PATIENT, List.of(""));
    headers.put(REQUEST_HEADER_X_NCPEHMOCK_RESPONSE, List.of(NCPEH_HEADER_VALUE));
    verify((Client) clientProxy).headers(any());
  }

  @Test
  void identifyPatientHeaderIsNullTest() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();

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
            () -> client.identifyPatient(PATIENT_ACCESS_DATA, "default", COUNTRY, null),
            "method identifyPatient threw exception");

    // Assert
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    final var patient = readPatientDataFromIdentifyPatientResponse(result.ncpehFdResponseContent());
    assertNotNull(patient);
    assertTrue(PATIENT.samePerson(patient));

    headers.put(REQUEST_HEADER_X_NCPEHMOCK_RESPONSE, List.of(""));
    headers.put(REQUEST_HEADER_X_NCPEHMOCK_PATIENT, List.of(""));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void identifyPatientThrows() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
    final var errorInfo = new ErrorInformation("a spectacular error occurred");
    when(clientProxy.identifyPatient(any()))
        .thenReturn(Response.status(Response.Status.BAD_REQUEST).entity(errorInfo).build());

    // Act
    final var exception =
        assertThrows(
            NcpehException.class,
            () -> client.identifyPatient(PATIENT_ACCESS_DATA, "default", COUNTRY, null),
            "method identifyPatient did not throw exception");

    // Assert
    assertTrue(exception.getMessage().contains(errorInfo.errorMessage()));
  }

  @Test
  void findPatientSummaryTest() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
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
                client.findPatientSummary(
                    PATIENT_ACCESS_DATA, "default", COUNTRY, NCPEH_HEADER_VALUE),
            "method findPatientSummaryData threw exception");

    // Assert
    assertNotNull(result, "method findPatientSummaryData returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    headers.put(REQUEST_HEADER_X_NCPEHMOCK_RESPONSE, List.of(NCPEH_HEADER_VALUE));
    headers.put(REQUEST_HEADER_X_NCPEHMOCK_PATIENT, List.of(""));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void findPatientSummaryHeaderIsNullTest() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
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
            () -> client.findPatientSummary(PATIENT_ACCESS_DATA, "default", COUNTRY, null),
            "method findPatientSummaryData threw exception");

    // Assert
    assertNotNull(result, "method findPatientSummaryData returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());

    headers.put(REQUEST_HEADER_X_NCPEHMOCK_RESPONSE, List.of(""));
    headers.put(REQUEST_HEADER_X_NCPEHMOCK_PATIENT, List.of(""));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void findPatientSummaryThrowsTest() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
    when(clientProxy.findDocuments(any()))
        .thenReturn(
            Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorInformation("error"))
                .build());

    // Act
    // Assert
    assertThrows(
        NcpehException.class,
        () -> client.findPatientSummary(PATIENT_ACCESS_DATA, "default", COUNTRY, null),
        "method findPatientSummaryData did not throw exception");
  }

  @Test
  void retrievePatientSummaryTest() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
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
        REQUEST_HEADER_X_NCPEHMOCK_PATIENT,
        List.of(Base64.getEncoder().encodeToString(PATIENT_JSON.getBytes(StandardCharsets.UTF_8))));
    headers.put(REQUEST_HEADER_X_NCPEHMOCK_RESPONSE, List.of(NCPEH_HEADER_VALUE));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void retrievePatientSummaryHeaderIsNullTest() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
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
    headers.put(REQUEST_HEADER_X_NCPEHMOCK_RESPONSE, List.of(""));
    headers.put(
        REQUEST_HEADER_X_NCPEHMOCK_PATIENT,
        List.of(Base64.getEncoder().encodeToString(PATIENT_JSON.getBytes(StandardCharsets.UTF_8))));
    verify((Client) clientProxy).headers(headers);
  }

  @Test
  void retrievePatientSummaryThrowsTest() {
    // Arrange
    final var clientProxy =
        mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));
    final var client = NcpehClientImpl.builder().config(config).clientProxy(clientProxy).build();
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
}
