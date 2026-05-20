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

package de.gematik.test.ncp.erezept.impl;

import static de.gematik.test.ncp.gen.erp.fdv.model.ConsentCategory.EUDISPCONS;
import static jakarta.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import de.gematik.test.ncp.gen.erp.fdv.api.ErpApi;
import de.gematik.test.ncp.gen.erp.fdv.model.Consent;
import de.gematik.test.ncp.gen.erp.fdv.model.EUAccessAuthorization;
import de.gematik.test.ncp.gen.erp.fdv.model.ErpTestdriverApiV1PrescriptionIdPatchRequest;
import de.gematik.test.ncp.gen.erp.fdv.model.LoginSuccess;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status.Family;
import jakarta.ws.rs.core.Response.StatusType;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

class ErpFdvClientTest {

  private final String kvnr = "X123456789";

  private ErpApi apiMock;
  private Response responseMock;
  private StatusType statusInfoMock;

  private ListAppender<ILoggingEvent> logWatcher;
  private ErpFdvClient erpFdvClient;

  @BeforeEach
  void setUp() {
    apiMock = mock();
    erpFdvClient = new ErpFdvClient(apiMock);

    responseMock = mock();
    statusInfoMock = mock();
    when(responseMock.getStatusInfo()).thenReturn(statusInfoMock);

    logWatcher = new ListAppender<>();
    logWatcher.start();
    ((Logger) LoggerFactory.getLogger(ErpFdvClient.class)).addAppender(logWatcher);
    ((Logger) LoggerFactory.getLogger(ErpFdvClient.class)).setAdditive(false);
  }

  @AfterEach
  void tearDown() {
    ((Logger) LoggerFactory.getLogger(ErpFdvClient.class)).detachAppender(logWatcher);
    ((Logger) LoggerFactory.getLogger(ErpFdvClient.class)).setAdditive(true);
    logWatcher.stop();
  }

  @Test
  void start_shouldNotThrowOnSuccess() {
    // Arrange
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(apiMock.erpTestdriverApiV1StartPut()).thenReturn(responseMock);
    // Act & Assert
    assertDoesNotThrow(erpFdvClient::start);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1StartPut();
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void start_shouldThrowWhenNotSuccessful(final int status) {
    testErrorResponseToException(erpFdvClient::start, apiMock::erpTestdriverApiV1StartPut, status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1StartPut();
  }

  @Test
  void stop_shouldNotThrowOnSuccess() {
    // Arrange
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(apiMock.erpTestdriverApiV1StopPut()).thenReturn(responseMock);
    // Act & Assert
    assertDoesNotThrow(erpFdvClient::stop);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1StopPut();
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void stop_shouldThrowWhenNotSuccessful(final int status) {
    testErrorResponseToException(erpFdvClient::stop, apiMock::erpTestdriverApiV1StopPut, status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1StopPut();
  }

  @Test
  void login_shouldReturnLoginSuccessOnSuccess() {
    // Arrange
    final var token = "dummyToken";
    final var loginSuccess = LoginSuccess.builder().accessToken(token).build();
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<LoginSuccess>>any()))
        .thenReturn(loginSuccess);
    when(apiMock.erpTestdriverApiV1LoginPut(kvnr)).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.login(kvnr);
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1LoginPut(kvnr);
    assertTrue(result.isPresent());
    assertEquals(token, result.get().getAccessToken());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void login_shouldReturnEmptyAndLogErrorOnError(final int status) {
    testErrorResponseToOptional(
        () -> erpFdvClient.login(kvnr), () -> apiMock.erpTestdriverApiV1LoginPut(kvnr), status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1LoginPut(kvnr);
  }

  @Test
  void login_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    testMalformedPayloadToOptional(
        () -> erpFdvClient.login(kvnr), () -> apiMock.erpTestdriverApiV1LoginPut(kvnr));
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1LoginPut(kvnr);
  }

  @Test
  void createEuDispensationConsent_shouldReturnConsentOnSuccess() {
    // Arrange
    final var category = EUDISPCONS;
    final var now = Instant.now().atOffset(ZoneOffset.UTC);
    final var consent = Consent.builder().category(category).kvnr(kvnr).dateTime(now).build();
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<Consent>>any())).thenReturn(consent);
    when(apiMock.erpTestdriverApiV1ConsentPost(category)).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.createEuDispensationConsent();
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentPost(category);
    assertTrue(result.isPresent());
    assertEquals(category, result.get().getCategory());
    assertEquals(kvnr, result.get().getKvnr());
    assertEquals(now, result.get().getDateTime());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void createEuDispensationConsent_shouldReturnEmptyAndLogErrorOnError(final int status) {
    testErrorResponseToOptional(
        erpFdvClient::createEuDispensationConsent,
        () -> apiMock.erpTestdriverApiV1ConsentPost(EUDISPCONS),
        status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentPost(EUDISPCONS);
  }

  @Test
  void createEuDispensationConsent_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    testMalformedPayloadToOptional(
        erpFdvClient::createEuDispensationConsent,
        () -> apiMock.erpTestdriverApiV1ConsentPost(EUDISPCONS));
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentPost(EUDISPCONS);
  }

  @Test
  void revokeEuDispensationConsent_shouldReturnTrueOnSuccess() {
    // Arrange
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(apiMock.erpTestdriverApiV1ConsentDelete(EUDISPCONS)).thenReturn(responseMock);
    // Act & Assert
    assertTrue(erpFdvClient::revokeEuDispensationConsent);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentDelete(EUDISPCONS);
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void revokeEuDispensationConsent_shouldReturnFalseAndLogErrorWhenNotSuccessful(final int status) {
    // Arrange
    setErrorStatus(status);
    when(apiMock.erpTestdriverApiV1ConsentDelete(EUDISPCONS)).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.revokeEuDispensationConsent();
    // Assert
    assertFalse(result);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentDelete(EUDISPCONS);
    verify(responseMock).close();
    assertTrue(logWatcher.list.stream().anyMatch(event -> Level.ERROR.equals(event.getLevel())));
  }

  @Test
  void getEuDispensationConsent_shouldReturnConsentOnSuccess() {
    // Arrange
    final var category = EUDISPCONS;
    final var now = Instant.now().atOffset(ZoneOffset.UTC);
    final var consent = Consent.builder().category(category).kvnr(kvnr).dateTime(now).build();
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<Consent>>any())).thenReturn(consent);
    when(apiMock.erpTestdriverApiV1ConsentGet(category)).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.getEuDispensationConsent();
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentGet(category);
    assertTrue(result.isPresent());
    assertEquals(category, result.get().getCategory());
    assertEquals(kvnr, result.get().getKvnr());
    assertEquals(now, result.get().getDateTime());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void getEuDispensationConsent_shouldReturnEmptyAndLogErrorOnError(final int status) {
    testErrorResponseToOptional(
        erpFdvClient::getEuDispensationConsent,
        () -> apiMock.erpTestdriverApiV1ConsentGet(EUDISPCONS),
        status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentGet(EUDISPCONS);
  }

  @Test
  void getEuDispensationConsent_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    testMalformedPayloadToOptional(
        erpFdvClient::getEuDispensationConsent,
        () -> apiMock.erpTestdriverApiV1ConsentGet(EUDISPCONS));
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1ConsentGet(EUDISPCONS);
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc123"})
  @NullSource
  void grantEuAccessPermission_shouldReturnEUAccessAuthorizationOnSuccess(
      final String providedAccessCode) {
    // Arrange
    final var country = "ES";
    final var accessCode = providedAccessCode != null ? providedAccessCode : "foo234";
    final var createdAt = Instant.now().atOffset(ZoneOffset.UTC);
    final var euAuth =
        EUAccessAuthorization.builder()
            .country(country)
            .accessCode(accessCode)
            .createdAt(createdAt)
            .validUntil(createdAt.plusHours(1))
            .build();
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<EUAccessAuthorization>>any()))
        .thenReturn(euAuth);
    when(apiMock.erpTestdriverApiV1EuAccessAuthorizationPost(country, providedAccessCode))
        .thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.grantEuAccessPermission(country, providedAccessCode);
    // Assert
    verify(apiMock, Mockito.times(1))
        .erpTestdriverApiV1EuAccessAuthorizationPost(country, providedAccessCode);
    assertTrue(result.isPresent());
    assertEquals(country, result.get().getCountry());
    assertEquals(accessCode, result.get().getAccessCode());
    assertEquals(createdAt, result.get().getCreatedAt());
    assertEquals(createdAt.plusHours(1), result.get().getValidUntil());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @CsvSource({"abc123, 401", "abc123, 501", "abc123, 302", " , 401", " , 501", " , 302"})
  void grantEuAccessPermission_shouldReturnEmptyAndLogErrorOnError(
      final String providedAccessCode, final int status) {
    final var country = "ES";
    testErrorResponseToOptional(
        () -> erpFdvClient.grantEuAccessPermission(country, providedAccessCode),
        () -> apiMock.erpTestdriverApiV1EuAccessAuthorizationPost(country, providedAccessCode),
        status);
    verify(apiMock, Mockito.times(1))
        .erpTestdriverApiV1EuAccessAuthorizationPost(country, providedAccessCode);
  }

  @Test
  void grantEuAccessPermission_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    final var country = "ES";
    final var accessCode = "abc123";
    testMalformedPayloadToOptional(
        () -> erpFdvClient.grantEuAccessPermission(country, accessCode),
        () -> apiMock.erpTestdriverApiV1EuAccessAuthorizationPost(country, accessCode));
    verify(apiMock, Mockito.times(1))
        .erpTestdriverApiV1EuAccessAuthorizationPost(country, accessCode);
  }

  @Test
  void revokeEuAccessPermission_shouldReturnTrueOnSuccess() {
    // Arrange
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(apiMock.erpTestdriverApiV1EuAccessAuthorizationDelete()).thenReturn(responseMock);
    // Act & Assert
    assertTrue(erpFdvClient::revokeEuAccessPermission);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1EuAccessAuthorizationDelete();
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void revokeEuAccessPermission_shouldReturnFalseAndLogErrorWhenNotSuccessful(final int status) {
    // Arrange
    setErrorStatus(status);
    when(apiMock.erpTestdriverApiV1EuAccessAuthorizationDelete()).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.revokeEuAccessPermission();
    // Assert
    assertFalse(result);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1EuAccessAuthorizationDelete();
    verify(responseMock).close();
    assertTrue(logWatcher.list.stream().anyMatch(event -> Level.ERROR.equals(event.getLevel())));
  }

  @Test
  void getEuAccessPermission_shouldReturnEUAccessAuthorizationOnSuccess() {
    // Arrange
    final var country = "ES";
    final var accessCode = "abc123";
    final var createdAt = Instant.now().atOffset(ZoneOffset.UTC);
    final var euAuth =
        EUAccessAuthorization.builder()
            .country(country)
            .accessCode(accessCode)
            .createdAt(createdAt)
            .validUntil(createdAt.plusHours(1))
            .build();
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<EUAccessAuthorization>>any()))
        .thenReturn(euAuth);
    when(apiMock.erpTestdriverApiV1EuAccessAuthorizationGet()).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.getEuAccessPermission();
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1EuAccessAuthorizationGet();
    assertTrue(result.isPresent());
    assertEquals(country, result.get().getCountry());
    assertEquals(accessCode, result.get().getAccessCode());
    assertEquals(createdAt, result.get().getCreatedAt());
    assertEquals(createdAt.plusHours(1), result.get().getValidUntil());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void getEuAccessPermission_shouldReturnEmptyAndLogErrorOnError(final int status) {
    testErrorResponseToOptional(
        erpFdvClient::getEuAccessPermission,
        apiMock::erpTestdriverApiV1EuAccessAuthorizationGet,
        status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1EuAccessAuthorizationGet();
  }

  @Test
  void getEuAccessPermission_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    testMalformedPayloadToOptional(
        erpFdvClient::getEuAccessPermission, apiMock::erpTestdriverApiV1EuAccessAuthorizationGet);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1EuAccessAuthorizationGet();
  }

  @Test
  void getPrescriptions_shouldReturnPrescriptionListOnSuccess() {
    // Arrange
    final var prescription1 =
        Prescription.builder().prescriptionId(UUID.randomUUID().toString()).build();
    final var prescription2 =
        Prescription.builder().prescriptionId(UUID.randomUUID().toString()).build();
    final var prescriptionList = List.of(prescription1, prescription2);
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<List<Prescription>>>any()))
        .thenReturn(prescriptionList);
    when(apiMock.erpTestdriverApiV1PrescriptionGet()).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.getPrescriptions();
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionGet();
    assertTrue(result.isPresent());
    assertEquals(2, result.get().size());
    assertEquals(prescription1.getPrescriptionId(), result.get().getFirst().getPrescriptionId());
    assertEquals(prescription2.getPrescriptionId(), result.get().getLast().getPrescriptionId());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void getPrescriptions_shouldReturnEmptyAndLogErrorOnError(final int status) {
    testErrorResponseToOptional(
        erpFdvClient::getPrescriptions, apiMock::erpTestdriverApiV1PrescriptionGet, status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionGet();
  }

  @Test
  void getPrescriptions_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    testMalformedPayloadToOptional(
        erpFdvClient::getPrescriptions, apiMock::erpTestdriverApiV1PrescriptionGet);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionGet();
  }

  @Test
  void getPrescription_shouldReturnPrescriptionOnSuccess() {
    // Arrange
    final var id = UUID.randomUUID().toString();
    final var prescription = Prescription.builder().prescriptionId(id).build();
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<Prescription>>any()))
        .thenReturn(prescription);
    when(apiMock.erpTestdriverApiV1PrescriptionIdGet(id)).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.getPrescription(id);
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdGet(id);
    assertTrue(result.isPresent());
    assertEquals(id, result.get().getPrescriptionId());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void getPrescription_shouldReturnEmptyAndLogErrorOnError(final int status) {
    final var id = UUID.randomUUID().toString();
    testErrorResponseToOptional(
        () -> erpFdvClient.getPrescription(id),
        () -> apiMock.erpTestdriverApiV1PrescriptionIdGet(id),
        status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdGet(id);
  }

  @Test
  void getPrescription_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    final var id = UUID.randomUUID().toString();
    testMalformedPayloadToOptional(
        () -> erpFdvClient.getPrescription(id),
        () -> apiMock.erpTestdriverApiV1PrescriptionIdGet(id));
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdGet(id);
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void setEuRedeemable_shouldReturnPrescriptionOnSuccess(final boolean euRedeemable) {
    // Arrange
    final var id = UUID.randomUUID().toString();
    final var prescription =
        Prescription.builder()
            .prescriptionId(id)
            .euRedeemableByProperties(true)
            .euRedeemableByPatient(euRedeemable)
            .build();
    final var request =
        ErpTestdriverApiV1PrescriptionIdPatchRequest.builder()
            .euRedeemableByPatient(euRedeemable)
            .build();
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<Prescription>>any()))
        .thenReturn(prescription);
    when(apiMock.erpTestdriverApiV1PrescriptionIdPatch(id, request)).thenReturn(responseMock);
    // Act
    final var result = erpFdvClient.setEuRedeemable(id, euRedeemable);
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdPatch(id, request);
    assertTrue(result.isPresent());
    assertEquals(id, result.get().getPrescriptionId());
    assertTrue(result.get().getEuRedeemableByProperties());
    assertEquals(euRedeemable, result.get().getEuRedeemableByPatient());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void setEuRedeemable_shouldReturnEmptyAndLogErrorOnError(final int status) {
    final var id = UUID.randomUUID().toString();
    final var euRedeemable = true;
    final var request =
        ErpTestdriverApiV1PrescriptionIdPatchRequest.builder()
            .euRedeemableByPatient(euRedeemable)
            .build();
    testErrorResponseToOptional(
        () -> erpFdvClient.setEuRedeemable(id, euRedeemable),
        () -> apiMock.erpTestdriverApiV1PrescriptionIdPatch(id, request),
        status);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdPatch(id, request);
  }

  @Test
  void setEuRedeemable_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    final var id = UUID.randomUUID().toString();
    final var euRedeemable = true;
    final var request =
        ErpTestdriverApiV1PrescriptionIdPatchRequest.builder()
            .euRedeemableByPatient(euRedeemable)
            .build();
    testMalformedPayloadToOptional(
        () -> erpFdvClient.setEuRedeemable(id, euRedeemable),
        () -> apiMock.erpTestdriverApiV1PrescriptionIdPatch(id, request));
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdPatch(id, request);
  }

  @Test
  void getMedicationDispenseInformation_shouldReturnResultAsListOnSuccess() {
    // Arrange
    var info1 = new EuDispenseInformation();
    var info2 = new EuDispenseInformation();
    info1.setPrescriptionId("ene");
    info2.setPrescriptionId("mene");
    List<BaseDispenseInformation> infoList = List.of(info1, info2);
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(
            ArgumentMatchers.<GenericType<List<BaseDispenseInformation>>>any()))
        .thenReturn(infoList);
    when(apiMock.erpTestdriverApiV1MedicationdispenseGet(any())).thenReturn(responseMock);
    // Act
    var result = erpFdvClient.getMedicationDispenseInformation();
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1MedicationdispenseGet(any());
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("ene", result.getFirst().getPrescriptionId());
    assertEquals("mene", result.getLast().getPrescriptionId());
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void getMedicationDispenseInformation_shouldReturnEmptyAndLogErrorOnError(final int status) {
    // Arrange
    setErrorStatus(status);
    when(apiMock.erpTestdriverApiV1MedicationdispenseGet(any())).thenReturn(responseMock);
    // Act
    var result = erpFdvClient.getMedicationDispenseInformation();
    // Assert
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1MedicationdispenseGet(any());
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(responseMock).close();
    assertTrue(logWatcher.list.stream().anyMatch(event -> Level.ERROR.equals(event.getLevel())));
  }

  @Test
  void
      getMedicationDispenseInformation_shouldReturnEmptyAndLogProcessingExceptionOnMalformedPayload() {
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<?>>any()))
        .thenThrow(new ProcessingException("Malformed payload"));
    when(apiMock.erpTestdriverApiV1MedicationdispenseGet(any())).thenReturn(responseMock);
    // Act & Assert
    var result = erpFdvClient.getMedicationDispenseInformation();
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1MedicationdispenseGet(any());
    assertTrue(result.isEmpty());
    assertTrue(logWatcher.list.stream().anyMatch(event -> Level.ERROR.equals(event.getLevel())));
    verify(responseMock).close();
  }

  @Test
  void deletePrescription_shouldReturnTrueOnSuccess() {
    // Arrange
    var id = "dummyId";
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(apiMock.erpTestdriverApiV1PrescriptionIdDelete(id)).thenReturn(responseMock);
    // Act & Assert
    assertTrue(() -> erpFdvClient.deletePrescription(id));
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdDelete(id);
    verify(responseMock).close();
  }

  @ParameterizedTest
  @ValueSource(ints = {401, 500, 301})
  void deletePrescription_shouldReturnFalseAndLogErrorWhenNotSuccessful(final int status) {
    // Arrange
    var id = "dummyId";
    setErrorStatus(status);
    when(apiMock.erpTestdriverApiV1PrescriptionIdDelete(id)).thenReturn(responseMock);
    // Act
    var result = erpFdvClient.deletePrescription(id);
    // Assert
    assertFalse(result);
    verify(apiMock, Mockito.times(1)).erpTestdriverApiV1PrescriptionIdDelete(id);
    verify(responseMock).close();
    assertTrue(logWatcher.list.stream().anyMatch(event -> Level.ERROR.equals(event.getLevel())));
  }

  private void setErrorStatus(final int status) {
    when(statusInfoMock.getStatusCode()).thenReturn(status);
    when(statusInfoMock.getReasonPhrase()).thenReturn("arbitrary reason");
    when(statusInfoMock.getFamily()).thenReturn(Family.familyOf(status));
  }

  private <T> void testMalformedPayloadToOptional(
      final Supplier<Optional<T>> methodToTest, final Supplier<Response> apiCall) {
    // Arrange
    when(statusInfoMock.getFamily()).thenReturn(SUCCESSFUL);
    when(responseMock.readEntity(ArgumentMatchers.<GenericType<?>>any()))
        .thenThrow(new ProcessingException("Malformed payload"));
    when(apiCall.get()).thenReturn(responseMock);
    // Act & Assert
    final var result = methodToTest.get();
    assertTrue(result.isEmpty());
    assertTrue(logWatcher.list.stream().anyMatch(event -> Level.ERROR.equals(event.getLevel())));
    verify(responseMock).close();
  }

  private <T> void testErrorResponseToOptional(
      final Supplier<Optional<T>> methodToTest,
      final Supplier<Response> apiCall,
      final int status) {
    // Arrange
    setErrorStatus(status);
    when(apiCall.get()).thenReturn(responseMock);
    // Act
    final var result = methodToTest.get();
    // Assert
    assertTrue(result.isEmpty());
    verify(responseMock).close();
    assertTrue(logWatcher.list.stream().anyMatch(event -> Level.ERROR.equals(event.getLevel())));
  }

  private void testErrorResponseToException(
      final Executable methodToTest, final Supplier<Response> apiCall, final int status) {
    // Arrange
    setErrorStatus(status);
    when(apiCall.get()).thenReturn(responseMock);
    // Act & Assert
    assertThrows(IllegalStateException.class, methodToTest);
    verify(responseMock).close();
  }
}
