/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.dwh.impl;

import static de.gematik.test.ncp.dwh.impl.DwhServiceImpl.DWH_URL_ACTIVE_KEY;
import static de.gematik.test.ncp.utils.TestUtils.loadFromJsonResource;
import static de.gematik.test.ncp.utils.TestUtils.mapper;
import static de.gematik.test.ncp.utils.TestUtils.readResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParseException;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReport;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@Slf4j
class DwhServiceImplTest {

  @Mock private RequestSpecification requestSpecification;

  @Mock private Response mockResponse;

  @Mock private ResponseBody responseBody;

  private MockedStatic<SerenityRest> mockedSerenityRest;

  private AutoCloseable closeable;

  @BeforeEach
  public void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
    mockedSerenityRest = Mockito.mockStatic(SerenityRest.class);
    when(requestSpecification.baseUri(anyString())).thenReturn(requestSpecification);
    when(requestSpecification.basePath(anyString())).thenReturn(requestSpecification);
    when(requestSpecification.accept(ContentType.JSON)).thenReturn(requestSpecification);
    when(requestSpecification.contentType(ContentType.JSON)).thenReturn(requestSpecification);
    when(requestSpecification.header(anyString(), any())).thenReturn(requestSpecification);
    when(requestSpecification.queryParams(
            anyString(), any(), anyString(), any(), anyString(), any()))
        .thenReturn(requestSpecification);
    when(requestSpecification.formParam(anyString(), anyString()))
        .thenReturn(
            requestSpecification, requestSpecification, requestSpecification, requestSpecification);
    when(SerenityRest.given()).thenReturn(requestSpecification);
    when(requestSpecification.get()).thenReturn(mockResponse);
    when(requestSpecification.post()).thenReturn(mockResponse);
    when(mockResponse.getBody()).thenReturn(responseBody);
  }

  @SneakyThrows
  @AfterEach
  public void tearDown() {
    // Closing the mockStatic after each test
    mockedSerenityRest.close();
    TigerGlobalConfiguration.reset();
    closeable.close();
  }

  @SneakyThrows
  @Test
  void getPerformanceReportTest() {
    // Arrange
    TigerGlobalConfiguration.putValue(DWH_URL_ACTIVE_KEY, false);
    final var responce = readResourceFile(this.getClass(), "dwh_response.json");
    when(responseBody.asString()).thenReturn(responce);

    // Act
    final var testee =
        assertDoesNotThrow(
            () ->
                DwhServiceImpl.getInstance().getPerformanceReport(1739554790120L, 1739599790120L));

    log.info(mapper.writeValueAsString(testee));

    // Assert
    final var expected =
        loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), "report.json");
    assertThat(testee).isNotNull().isEqualTo(expected);
  }

  @Test
  void getPerformanceReportTestThrowException() {
    // Arrange
    TigerGlobalConfiguration.putValue(DWH_URL_ACTIVE_KEY, false);
    when(responseBody.asString()).thenReturn("invalid json");

    // Act & Assert
    assertThrows(
        JsonParseException.class,
        () -> DwhServiceImpl.getInstance().getPerformanceReport(1739554790120L, 1739599790120L));
  }

  @SneakyThrows
  @Test
  void getPerformanceReportTestEmptyResponse() {
    // Arrange
    TigerGlobalConfiguration.putValue(DWH_URL_ACTIVE_KEY, false);
    when(responseBody.asString()).thenReturn("[]");

    // Act
    final var testee =
        assertDoesNotThrow(
            () ->
                DwhServiceImpl.getInstance().getPerformanceReport(1739554790120L, 1739599790120L));

    log.info(mapper.writeValueAsString(testee));

    // Assert
    final var expected = mapper.readValue("{\"entries\":[]}", NcpehPerformanceReport.class);
    assertThat(testee).isNotNull().isEqualTo(expected);
  }

  @Test
  void fetchTokenReturnsValidToken() {
    // Arrange
    final String validResponse = "{\"access_token\":\"valid_token\"}";
    when(responseBody.asString()).thenReturn(validResponse);
    final var spyService = Mockito.spy((DwhServiceImpl) DwhServiceImpl.getInstance());
    when(spyService.getClientId()).thenReturn("client_id");
    when(spyService.getClientSecret()).thenReturn("client_secret");

    // Act
    final String token = assertDoesNotThrow(spyService::fetchToken);

    // Assert
    assertEquals("valid_token", token);
  }

  @Test
  void fetchTokenThrowsExceptionWhenClientIdIsNotSet() {
    // Arrange
    final var spyService = Mockito.spy((DwhServiceImpl) DwhServiceImpl.getInstance());
    when(spyService.getClientSecret()).thenReturn("client_secret");
    when(spyService.getClientId()).thenReturn(null);

    // Act
    final var exception = assertThrows(IllegalArgumentException.class, spyService::fetchToken);

    // Assert
    assertEquals("ClientID is not set", exception.getMessage());
  }

  @Test
  void fetchTokenThrowsExceptionWhenClientSecretIsNotSet() {
    // Arrange
    final var spyService = Mockito.spy((DwhServiceImpl) DwhServiceImpl.getInstance());
    when(spyService.getClientId()).thenReturn("client_id");
    when(spyService.getClientSecret()).thenReturn(null);

    // Act
    final var exception = assertThrows(IllegalArgumentException.class, spyService::fetchToken);

    // Assert
    assertEquals("ClientSecret is not set", exception.getMessage());
  }
}
