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

package de.gematik.test.ncp.ps.epaps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.Practice;
import de.gematik.test.ncp.data.PracticeImpl;
import de.gematik.test.ncp.gen.epa.api.authentication.LoginLogoutApi;
import de.gematik.test.ncp.gen.epa.api.authentication.dto.LoginResponseDTO;
import de.gematik.test.ncp.gen.epa.api.card.CardApi;
import de.gematik.test.ncp.gen.epa.api.card.dto.GetCardsInfoResponseDTO;
import de.gematik.test.ncp.gen.epa.api.card.dto.SmbInformationDTO;
import de.gematik.test.ncp.gen.epa.api.documents.DocumentsApi;
import de.gematik.test.ncp.gen.epa.api.documents.dto.DeleteObjectsRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.DocumentMetadata;
import de.gematik.test.ncp.gen.epa.api.documents.dto.FindObjectsResponseDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.FindRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.PutDocumentsRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.RegistryObjectLists;
import de.gematik.test.ncp.gen.epa.api.documents.dto.ResponseDTO;
import de.gematik.test.ncp.gen.epa.api.entitlement.EntitlementApi;
import de.gematik.test.ncp.gen.epa.api.entitlement.dto.PostEntitlementResponseDTO;
import de.gematik.test.ncp.gen.epa.api.information.InformationApi;
import de.gematik.test.ncp.gen.epa.api.information.dto.GetRecordStatusResponseDTO;
import de.gematik.test.ncp.gen.epa.api.signature.SignatureApi;
import de.gematik.test.ncp.gen.epa.api.signature.dto.SignDocumentRequest;
import de.gematik.test.ncp.gen.epa.api.signature.dto.SignDocumentResponse;
import de.gematik.test.ncp.ps.PsException;
import de.gematik.test.ncp.ps.PsProvider;
import de.gematik.test.ncp.ps.epaps.data.PsTestdata;
import de.gematik.test.ncp.utils.TestUtils;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EpaPrimarySystemServiceImplTest {

  private final ExternalServerConfig config = PsProvider.getInstance().getEpaPsConfig();
  private static final String KVNR = "X110987654";
  private static final String TELEMATIK_ID = "123456";
  private static final String SUCCESS_STATUS_MSG = "All went well!";
  private static final ResponseDTO STD_DOCUMENT_RESPONSE =
      new ResponseDTO().success(true).statusMessage(SUCCESS_STATUS_MSG);

  public static final Practice PRACTICE =
      TestUtils.loadFromJsonResource(
          PracticeImpl.class, EpaPrimarySystemServiceImplTest.class, "practice.json");
  public static final DocumentMetadata DOCUMENT_METADATA =
      PsTestdata.createDocumentMetadata(PRACTICE);
  private static final DocumentMetadata FIND_RESPONSE_DOCUMENT_DATA =
      DOCUMENT_METADATA.entryUUID("urn:uuid:2305gnwo8rh2");

  @Test
  void testAuthorizeLeForKvnr() {
    // Arrange
    final var loginLogoutApi = Mockito.mock(LoginLogoutApi.class);
    when(loginLogoutApi.login(eq(TELEMATIK_ID), any(), any()))
        .thenReturn(new LoginResponseDTO().success(true));
    final var entitlementApi = Mockito.mock(EntitlementApi.class);
    when(entitlementApi.postEntitlement(any(), any()))
        .thenReturn(new PostEntitlementResponseDTO().success(true));
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .loginLogoutProxy(loginLogoutApi)
            .entitlementProxy(entitlementApi)
            .build();

    // Act
    // Assert
    assertDoesNotThrow(
        () -> service.authorizeLeForKvnr(TELEMATIK_ID, KVNR),
        "Method EpaPsInterfaceImpl.authorizeLeForKvnr threw an exception");
  }

  @Test
  void testAuthorizeLeForKvnrSuccessIsFalse() {
    // Arrange
    final var loginLogoutApi = Mockito.mock(LoginLogoutApi.class);
    when(loginLogoutApi.login(eq(TELEMATIK_ID), any(), any()))
        .thenReturn(new LoginResponseDTO().success(true));
    final var entitlementApi = Mockito.mock(EntitlementApi.class);
    when(entitlementApi.postEntitlement(any(), any()))
        .thenReturn(new PostEntitlementResponseDTO().success(false));
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .loginLogoutProxy(loginLogoutApi)
            .entitlementProxy(entitlementApi)
            .build();

    // Act
    // Assert
    assertThrows(
        PsException.class,
        () -> service.authorizeLeForKvnr(TELEMATIK_ID, KVNR),
        "Method EpaPsInterfaceImpl.authorizeLeForKvnr did not throw an exception");
  }

  @Test
  void testAuthorizeLeForKvnrThrows() {
    // Arrange
    final var loginLogoutApi = Mockito.mock(LoginLogoutApi.class);
    when(loginLogoutApi.login(eq(TELEMATIK_ID), any(), any()))
        .thenReturn(new LoginResponseDTO().success(true));
    final var entitlementApi = Mockito.mock(EntitlementApi.class);
    when(entitlementApi.postEntitlement(any(), any())).thenThrow(IllegalFormatException.class);
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .loginLogoutProxy(loginLogoutApi)
            .entitlementProxy(entitlementApi)
            .build();

    // Act
    // Assert
    assertThrows(
        IllegalFormatException.class,
        () -> service.authorizeLeForKvnr(TELEMATIK_ID, KVNR),
        "Method EpaPsInterfaceImpl.authorizeLeForKvnr did not throw an exception");
  }

  @Test
  void testAuthorizeLeForKvnrLoginIsFalse() {
    // Arrange
    final var loginLogoutApi = Mockito.mock(LoginLogoutApi.class);
    when(loginLogoutApi.login(eq(TELEMATIK_ID), any(), any()))
        .thenReturn(new LoginResponseDTO().success(false));
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .loginLogoutProxy(loginLogoutApi)
            .build();

    // Act
    // Assert
    assertThrows(
        PsException.class,
        () -> service.authorizeLeForKvnr(TELEMATIK_ID, KVNR),
        "Method EpaPsInterfaceImpl.authorizeLeForKvnr did not throw an exception");
  }

  @Test
  void testAuthorizeLeForKvnrLoginThrows() {
    // Arrange
    final var loginLogoutApi = Mockito.mock(LoginLogoutApi.class);
    when(loginLogoutApi.login(eq(TELEMATIK_ID), any(), any()))
        .thenThrow(IllegalFormatException.class);
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .loginLogoutProxy(loginLogoutApi)
            .build();

    // Act
    // Assert
    assertThrows(
        IllegalFormatException.class,
        () -> service.authorizeLeForKvnr(TELEMATIK_ID, KVNR),
        "Method EpaPsInterfaceImpl.authorizeLeForKvnr did not throw an exception");
  }

  @Test
  void testPutDocument() {
    // Arrange
    final var documentsApi = Mockito.mock(DocumentsApi.class);
    final AtomicReference<PutDocumentsRequestDTO> capturedRequest = new AtomicReference<>();
    when(documentsApi.putDocuments(anyString(), any(PutDocumentsRequestDTO.class)))
        .thenAnswer(
            invocation -> {
              capturedRequest.set(invocation.getArgument(1));
              return STD_DOCUMENT_RESPONSE;
            });
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();

    // Act
    final var testResult =
        assertDoesNotThrow(
            () -> service.putDocument(KVNR, PRACTICE, "epka".getBytes(StandardCharsets.UTF_8)),
            "Method EpaPsInterfaceImpl.putDocument threw an exception");

    // Assert
    assertNotNull(testResult, "Return value is null");
    assertEquals(
        testResult,
        capturedRequest.get().getDocumentSets().getFirst().getDocumentMetadata().getTitle(),
        "Document title does not have the expected value");
  }

  @Test
  void testPutDocumentFail() {
    // Arrange
    final var documentsApi = Mockito.mock(DocumentsApi.class);
    when(documentsApi.putDocuments(anyString(), any(PutDocumentsRequestDTO.class)))
        .thenReturn(new ResponseDTO().success(false).statusMessage("This was not right"));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();
    final var epka = "epka".getBytes(StandardCharsets.UTF_8);

    // Act
    // Assert
    assertThrows(
        PsException.class,
        () -> service.putDocument(KVNR, PRACTICE, epka),
        "Method EpaPsInterfaceImpl.putDocument did not throw an exception");
  }

  @Test
  void testReplaceDocumentNotFound() {
    // Arrange
    final var service = EpaPrimarySystemServiceImpl.builder().config(config).build();
    final var spyService = spy(service);
    doReturn(List.of()).when(spyService).findEpka(KVNR);
    final var epka = "epka".getBytes(StandardCharsets.UTF_8);
    final var title = "title";
    doReturn(title).when(spyService).putDocument(KVNR, PRACTICE, epka);

    // Act
    final var testResult =
        assertDoesNotThrow(
            () -> spyService.replaceDocument(KVNR, PRACTICE, epka),
            "Method EpaPsInterfaceImpl.replaceDocument threw an exception");

    // Assert
    assertEquals(title, testResult);
    verify(spyService).findEpka(KVNR);
    verify(spyService).putDocument(KVNR, PRACTICE, epka);
  }

  @Test
  void testReplaceDocument() {
    // Arrange
    final var service = EpaPrimarySystemServiceImpl.builder().config(config).build();
    final var spyService = spy(service);
    final var uuids = List.of("123546");
    doReturn(uuids).when(spyService).findEpka(KVNR);
    doNothing().when(spyService).deleteExistingDocuments(KVNR, uuids);
    final var epka = "epka".getBytes(StandardCharsets.UTF_8);
    final var title = "title";
    doReturn(title).when(spyService).putDocument(KVNR, PRACTICE, epka);

    // Act
    final var testResult =
        assertDoesNotThrow(
            () -> spyService.replaceDocument(KVNR, PRACTICE, epka),
            "Method EpaPsInterfaceImpl.replaceDocument threw an exception");

    // Assert
    assertEquals(title, testResult);
    verify(spyService).findEpka(KVNR);
    verify(spyService).deleteExistingDocuments(KVNR, uuids);
    verify(spyService).putDocument(KVNR, PRACTICE, epka);
  }

  @Test
  void testAktenKontoIsPresent() {
    // Arrange
    final var informationApi = Mockito.mock(InformationApi.class);
    when(informationApi.getRecordStatus(Mockito.anyString()))
        .thenReturn(new GetRecordStatusResponseDTO().success(true));
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .informationProxy(informationApi)
            .build();

    // Act
    final var result = assertDoesNotThrow(() -> service.aktenkontoIsPresent(KVNR));

    // Assert
    assertTrue(result);
  }

  @Test
  void testAktenKontoIsPresentNegativ() {
    // Arrange
    final var informationApi = Mockito.mock(InformationApi.class);
    when(informationApi.getRecordStatus(Mockito.anyString()))
        .thenReturn(new GetRecordStatusResponseDTO().success(false));
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .informationProxy(informationApi)
            .build();

    // Act
    final var result = assertDoesNotThrow(() -> service.aktenkontoIsPresent(KVNR));

    // Assert
    assertFalse(result);
  }

  @Test
  void testAktenKontoIsPresentFail() {
    // Arrange
    final var informationApi = Mockito.mock(InformationApi.class);
    when(informationApi.getRecordStatus(Mockito.anyString()))
        .thenThrow(new RuntimeException("Something went wrong"));
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .informationProxy(informationApi)
            .build();

    // Act
    final var result = assertDoesNotThrow(() -> service.aktenkontoIsPresent(KVNR));

    // Assert
    assertFalse(result);
  }

  @Test
  void testFindEpka() {
    // Arrange
    final var documentsApi = Mockito.mock(DocumentsApi.class);
    when(documentsApi.find(anyString(), any(FindRequestDTO.class)))
        .thenReturn(
            new FindObjectsResponseDTO()
                .success(true)
                .statusMessage(SUCCESS_STATUS_MSG)
                .registryObjectLists(
                    new RegistryObjectLists()
                        .documentsMetadata(List.of(FIND_RESPONSE_DOCUMENT_DATA))));

    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();

    // Act
    final var testResult = assertDoesNotThrow(() -> service.findEpka(KVNR));

    // Assert
    assertNotNull(testResult);
    assertEquals(1, testResult.size());
    assertTrue(
        testResult.stream()
            .allMatch(entryUUID -> entryUUID.equals(FIND_RESPONSE_DOCUMENT_DATA.getEntryUUID())));
  }

  @Test
  void testFindAllDocuments() {
    // Arrange
    final var documentsApi = Mockito.mock(DocumentsApi.class);
    when(documentsApi.find(anyString(), any(FindRequestDTO.class)))
        .thenReturn(
            new FindObjectsResponseDTO()
                .success(true)
                .statusMessage(SUCCESS_STATUS_MSG)
                .registryObjectLists(
                    new RegistryObjectLists()
                        .documentsMetadata(List.of(FIND_RESPONSE_DOCUMENT_DATA))));

    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();

    // Act
    final var testResult = assertDoesNotThrow(() -> service.findAllDocuments(KVNR));

    // Assert
    assertNotNull(testResult);
    assertEquals(1, testResult.size());
    assertTrue(
        testResult.stream()
            .allMatch(entryUUID -> entryUUID.equals(FIND_RESPONSE_DOCUMENT_DATA.getEntryUUID())));
  }

  @Test
  void testDeleteExistingDocuments() {
    // Arrange
    final var documentsApi = Mockito.mock(DocumentsApi.class);
    when(documentsApi.deleteObjects(anyString(), any(DeleteObjectsRequestDTO.class)))
        .thenReturn(STD_DOCUMENT_RESPONSE);
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();

    // Act
    // Assert
    assertDoesNotThrow(
        () -> service.deleteExistingDocuments(KVNR, List.of(UUID.randomUUID().toString())));
  }

  @Test
  void testSignDocument() {
    // Arrange
    final var signatureApi = Mockito.mock(SignatureApi.class);
    when(signatureApi.signDocument(any()))
        .thenReturn(
            new SignDocumentResponse()
                .success(true)
                .signatureObject("I am signed".getBytes(StandardCharsets.UTF_8))
                .signatureForm(SignDocumentResponse.SignatureFormEnum.DOCUMENT_WITH_SIGNATURE));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).signatureProxy(signatureApi).build();
    final var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    // Act
    final var tstResult = assertDoesNotThrow(() -> service.signDocument(document));

    // Assert
    assertNotNull(tstResult);
  }

  @Test
  void testSignDocumentWithParameters() {
    // Arrange
    final var signatureApi = Mockito.mock(SignatureApi.class);
    when(signatureApi.signDocument(any()))
        .thenReturn(
            new SignDocumentResponse()
                .success(true)
                .signatureObject("I am signed".getBytes(StandardCharsets.UTF_8))
                .signatureForm(SignDocumentResponse.SignatureFormEnum.DOCUMENT_WITH_SIGNATURE));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).signatureProxy(signatureApi).build();
    final var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    // Act
    final var tstResult =
        assertDoesNotThrow(
            () ->
                service.signDocument(
                    document, false, SignDocumentRequest.SignatureAlgorithmEnum.ECC.value()));

    // Assert
    assertNotNull(tstResult);
  }

  @Test
  void testSignDocumentNoSignature() {
    // Arrange
    final var signatureApi = Mockito.mock(SignatureApi.class);
    when(signatureApi.signDocument(any()))
        .thenReturn(new SignDocumentResponse().success(true).statusMessage(SUCCESS_STATUS_MSG));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).signatureProxy(signatureApi).build();
    final var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    // Act
    // Assert
    assertThrows(PsException.class, () -> service.signDocument(document));
  }

  @Test
  void testSignDocumentFail() {
    // Arrange
    final var signatureApi = Mockito.mock(SignatureApi.class);
    when(signatureApi.signDocument(any()))
        .thenReturn(new SignDocumentResponse().success(false).statusMessage("Big mistake"));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).signatureProxy(signatureApi).build();
    final var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    // Act
    // Assert
    assertThrows(PsException.class, () -> service.signDocument(document));
  }

  @SneakyThrows
  @Test
  void getSmbCardReturnsSmbCard() {
    // Arrange
    final var cardApi = Mockito.mock(CardApi.class);
    when(cardApi.getCardsInfo())
        .thenReturn(
            new GetCardsInfoResponseDTO()
                .success(true)
                .smbInfo(
                    List.of(
                        new SmbInformationDTO()
                            .cardHolderName("Dr. Smith")
                            .telematikId("123456"))));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).cardApiProxy(cardApi).build();

    // Act
    final var testee = assertDoesNotThrow(service::getSmbCard);

    // Assert
    assertNotNull(testee);
    assertEquals("Dr. Smith", testee.name());
    assertEquals("123456", testee.telematikId());
  }

  @SneakyThrows
  @Test
  void getSmbCardThrowsPsExceptionWhenResponseNotSuccessful() {
    // Arrange
    final var cardApi = Mockito.mock(CardApi.class);
    when(cardApi.getCardsInfo())
        .thenReturn(new GetCardsInfoResponseDTO().success(false).statusMessage("Error"));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).cardApiProxy(cardApi).build();

    // Act
    final var exeption = assertThrows(PsException.class, service::getSmbCard);

    // Assert
    assertEquals("Operation getPractice, Message: Error", exeption.getMessage());
  }

  @SneakyThrows
  @Test
  void getSmbCardThrowsPsExceptionWhenNoSmbInfoFound() {
    // Arrange
    final var cardApi = Mockito.mock(CardApi.class);
    when(cardApi.getCardsInfo())
        .thenReturn(new GetCardsInfoResponseDTO().success(true).smbInfo(List.of()));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).cardApiProxy(cardApi).build();

    // Act
    final var exeption = assertThrows(PsException.class, service::getSmbCard);

    // Assert
    assertEquals("Operation getPractice, Message: No practice found", exeption.getMessage());
  }
}
