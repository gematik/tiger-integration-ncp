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

package de.gematik.test.ncp.reporting;

import static de.gematik.test.ncp.utils.TestUtils.loadFromJsonResource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.request.FindDocumentsRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.GeneralFactory;
import de.gematik.test.ncp.utils.TestUtils;
import jakarta.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.SneakyThrows;
import org.apache.cxf.jaxrs.client.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NcpehTimeLoggingHandlerTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final NcpehSimulatorApi service =
      mock(NcpehSimulatorApi.class, withSettings().extraInterfaces(Client.class));

  private final NcpehContext ncpehContextSpy = spy(new NcpehContext());
  private final NcpehTimeLoggingHandler handlerSpy =
      spy(new NcpehTimeLoggingHandler(service, ncpehContextSpy));
  private final Response responseOkSpy = spy(Response.ok().build());

  @BeforeEach
  public void setUp() {
    // Redirect System.out to capture output for testing
    System.setOut(new PrintStream(outContent));
  }

  @AfterEach
  void tearDown() {
    // Restore the original System.out
    System.setOut(originalOut);
  }

  @Test
  void testTimeLoggingThrowException() {
    // Arrange
    // Create the mock service and proxy
    final var proxyService =
        GeneralFactory.createNcpehTimeLoggingProxy(service, null, NcpehSimulatorApi.class);
    when(service.identifyPatient(any())).thenThrow(new RuntimeException("Test exception"));

    // Act
    assertThrows(
        RuntimeException.class,
        () -> proxyService.identifyPatient(null),
        "method identifyPatient did not throw exception");

    // Assert
    final String output = outContent.toString();
    assertTrue(output.contains("Method identifyPatient took"));
  }

  @Test
  void testTimeLogging() {
    // Arrange
    // Create the mock service and proxy
    final var proxyService =
        GeneralFactory.createNcpehTimeLoggingProxy(service, null, NcpehSimulatorApi.class);

    // Act
    proxyService.identifyPatient(null);

    // Assert
    final String output = outContent.toString();
    assertTrue(output.contains("Method identifyPatient took"));
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "1000# 100# identifyPatient# IdentifyPatientRequest.json# identifyPatientResponse.json# 1000;100;NCPeH.UC_1;200;{\"reqc\":\"NL\",\"bkdur\":100}",
        "1000# 101# identifyPatient# IdentifyPatientRequest.json# identifyPatientResponse_010.json# 1000;101;NCPeH.UC_1;404;{\"reqc\":\"NL\",\"err\":\"ERROR_PI_NO_MATCH\",\"bkdur\":101}",
        "2000# 200# findDocuments# FindDocumentsRequest.json# findDocumentResponse.json# 2000;200;NCPeH.UC_2;200;{\"reqc\":\"IT\",\"bkdur\":200}",
        "2000# 201# findDocuments# # findDocumentResponse_010.json# 2000;201;NCPeH.UC_2;405;{\"err\":\"ERROR_GENERIC_DOCUMENT_MISSING\",\"bkdur\":201}",
        "3000# 300# retrieveDocument# RetrieveDocumentRequest_xml.json# retrieveDocumentResponse.json# 3000;300;NCPeH.UC_3;200;{\"reqc\":\"CY\",\"bkdur\":300}",
        "3001# 301# retrieveDocument# RetrieveDocumentRequest_pdf.json# retrieveDocumentResponse.json# 3001;301;NCPeH.UC_4;200;{\"reqc\":\"ES\",\"bkdur\":301}",
        "3001# 301# retrieveDocument# RetrieveDocumentRequest_pdf.json# retrieveDocumentResponse_020.json# 3001;301;NCPeH.UC_4;200;{\"reqc\":\"ES\",\"err\":\"ERROR_GENERIC_DOCUMENT_MISSING\",\"bkdur\":301}"
      },
      delimiter = '#')
  void testGetPerformanceData(
      final long timestamp,
      final long duration,
      final String methodName,
      final String input,
      final String responseData,
      final String expected) {
    // Arrange
    final var args =
        Optional.ofNullable(input)
            .map(
                i -> {
                  if ("IdentifyPatientRequest.json".equals(input)) {
                    return loadFromJsonResource(
                        IdentifyPatientRequest.class, this.getClass(), input);
                  }
                  if ("FindDocumentsRequest.json".equals(input)) {
                    return loadFromJsonResource(FindDocumentsRequest.class, this.getClass(), input);
                  }
                  return loadFromJsonResource(
                      RetrieveDocumentRequest.class, this.getClass(), input);
                })
            .map(i -> new Object[] {i})
            .orElse(new Object[] {});

    final var response =
        Response.ok(
                TestUtils.loadFromJsonResource(
                    SimulatorCommunicationData.class, this.getClass(), responseData))
            .build();
    final var responseSpy = spy(response);
    // should return true
    when(responseSpy.bufferEntity()).thenReturn(true);

    // Act
    final var testee =
        handlerSpy
            .getPerformanceData(timestamp, duration, methodName, args, responseSpy)
            .orElse("");

    // Assert
    assertEquals(expected, testee);
  }

  @SneakyThrows
  @Test
  void writePerformanceFileCreatesFilesWithCorrectData() {
    // Arrange
    when(ncpehContextSpy.getFileName()).thenReturn("perftest.log");
    final var filePath = Paths.get(ncpehContextSpy.getFileName());
    if (Files.exists(filePath)) {
      Files.delete(filePath);
    }
    final var args = new Object[] {};
    when(responseOkSpy.bufferEntity()).thenReturn(true);
    final var data = "test data";
    when(handlerSpy.getPerformanceData(anyLong(), anyLong(), anyString(), any(), any()))
        .thenReturn(Optional.of(data));

    // Act
    handlerSpy.writePerformanceFiles(100, "identifyPatient", args, responseOkSpy);

    // Assert
    assertTrue(Files.exists(filePath));
    final var content = Files.readString(filePath);
    assertEquals(data, content.trim());

    // Clean up
    Files.delete(filePath);
  }

  @SneakyThrows
  @Test
  void writePerformanceFileCreatesScenarioFilesIfPresent() {
    // Arrange
    final var scenarioFilePath = Paths.get("scenario.log");
    if (Files.exists(scenarioFilePath)) {
      Files.delete(scenarioFilePath);
    }
    final var args = new Object[] {};
    when(responseOkSpy.bufferEntity()).thenReturn(true);
    final var data = "test data";
    when(handlerSpy.getPerformanceData(anyLong(), anyLong(), anyString(), any(), any()))
        .thenReturn(Optional.of(data));
    when(ncpehContextSpy.getFileName()).thenReturn("perftest.log");
    when(ncpehContextSpy.getScenarioFileName()).thenReturn(Optional.of("scenario.log"));

    // Act
    handlerSpy.writePerformanceFiles(100, "identifyPatient", args, responseOkSpy);

    // Assert
    assertTrue(Files.exists(scenarioFilePath));
    final var content = Files.readString(scenarioFilePath);
    assertEquals(data, content.trim());

    // Clean up
    Files.delete(Paths.get(ncpehContextSpy.getFileName()));
    Files.delete(scenarioFilePath);
  }

  @Test
  void writePerformanceFilesLogsWarningWhenNoData() {
    // Arrange
    final var args = new Object[] {};
    when(responseOkSpy.bufferEntity()).thenReturn(true);
    when(handlerSpy.getPerformanceData(anyLong(), anyLong(), anyString(), any(), any()))
        .thenReturn(Optional.empty());
    when(ncpehContextSpy.getFileName()).thenReturn("perftest.log");

    // Act
    handlerSpy.writePerformanceFiles(100, "identifyPatient", args, responseOkSpy);

    // Assert
    final var filePath = Paths.get(ncpehContextSpy.getFileName());
    assertFalse(Files.exists(filePath));
    assertTrue(
        outContent.toString().contains("No performance data found for method identifyPatient"));
  }
}
