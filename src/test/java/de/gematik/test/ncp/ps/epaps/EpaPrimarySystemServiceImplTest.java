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

package de.gematik.test.ncp.ps.epaps;

import static java.net.HttpURLConnection.HTTP_BAD_GATEWAY;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.gematik.epa.api.ConfigurationApi;
import de.gematik.epa.api.DocumentsApi;
import de.gematik.epa.api.SignatureApi;
import de.gematik.epa.api.authentication.LoginLogoutApi;
import de.gematik.epa.api.authentication.dto.LoginResponseDTO;
import de.gematik.epa.api.entitlement.EntitlementApi;
import de.gematik.epa.api.entitlement.dto.PostEntitlementResponseDTO;
import de.gematik.epa.api.information.InformationApi;
import de.gematik.epa.api.information.dto.GetRecordStatusResponseDTO;
import de.gematik.epa.dto.request.DeleteObjectsRequestDTO;
import de.gematik.epa.dto.request.FindRequestDTO;
import de.gematik.epa.dto.request.PutDocumentsRequestDTO;
import de.gematik.epa.dto.request.SignDocumentRequest.SignatureAlgorithm;
import de.gematik.epa.dto.response.FindObjectsResponseDTO;
import de.gematik.epa.dto.response.ResponseDTO;
import de.gematik.epa.dto.response.SignDocumentResponse;
import de.gematik.epa.dto.response.SignDocumentResponse.SignatureForm;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.epa.ihe.model.response.RegistryObjectLists;
import de.gematik.epa.ihe.model.simple.ByteArray;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.ps.PsException;
import de.gematik.test.ncp.ps.PsProvider;
import de.gematik.test.ncp.ps.epaps.data.PsTestdata;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;
import net.serenitybdd.rest.stubs.ResponseStub;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EpaPrimarySystemServiceImplTest {

  private final ExternalServerConfig config = PsProvider.getInstance().getEpaPsConfig();
  private static final String KVNR = "X110987654";
  private static final String TELEMATIK_ID = "123456";
  private static final String SUCCESS_STATUS_MSG = "All went well!";
  private static final ResponseDTO STD_RESPONSE = new ResponseDTO(Boolean.TRUE, SUCCESS_STATUS_MSG);
  public static final de.gematik.test.ncp.ps.epaps.data.DocumentMetadata DOCUMENT_METADATA =
      PsTestdata.createDocumentMetadata();
  private static final DocumentMetadata FIND_RESPONSE_DOCUMENT_DATA =
      DOCUMENT_METADATA.withEntryUUID("urn:uuid:2305gnwo8rh2").toPsDocumentMetadata();

  @Test
  void testPsIsUpAndRunning() {
    // Arrange
    final var service = EpaPrimarySystemServiceImpl.builder().config(config).build();
    final var spyService = spy(service);
    when(spyService.restAssuredGet(Mockito.anyString()))
        .thenReturn(
            new ResponseStub() {
              @Override
              public int statusCode() {
                return HTTP_OK;
              }
            });

    // Act
    final var isUpAndRunning = assertDoesNotThrow(spyService::psIsUpAndRunning);

    // Assert
    assertTrue(isUpAndRunning);
  }

  @Test
  void testPsIsUpAndRunningException() {
    // Arrange
    final var service = EpaPrimarySystemServiceImpl.builder().config(config).build();
    final var spyService = spy(service);
    when(spyService.restAssuredGet(Mockito.anyString()))
        .thenReturn(
            new ResponseStub() {
              @Override
              public int statusCode() {
                return HTTP_BAD_GATEWAY;
              }
            });

    // Act
    final var isUpAndRunning = assertDoesNotThrow(spyService::psIsUpAndRunning);

    // Assert
    assertFalse(isUpAndRunning);
  }

  @Test
  void testAuthorizeLeForKvnr() {
    // Arrange
    final var loginLogoutApi = Mockito.mock(LoginLogoutApi.class);
    when(loginLogoutApi.login(eq(TELEMATIK_ID), any(), any()))
        .thenReturn(new LoginResponseDTO().success(true));
    final var entitlementApi = Mockito.mock(EntitlementApi.class);
    when(entitlementApi.postEntitlement(any()))
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
    when(entitlementApi.postEntitlement(any()))
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
    when(entitlementApi.postEntitlement(any())).thenThrow(IllegalFormatException.class);
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
              return STD_RESPONSE;
            });
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();

    // Act
    final var testResult =
        assertDoesNotThrow(
            () -> service.putDocument(KVNR, "epka".getBytes(StandardCharsets.UTF_8)),
            "Method EpaPsInterfaceImpl.putDocument threw an exception");

    // Assert
    assertNotNull(testResult, "Return value is null");
    assertEquals(
        testResult,
        capturedRequest.get().documentSets().getFirst().documentMetadata().title(),
        "Document title does not have the expected value");
  }

  @Test
  void testPutDocumentFail() {
    // Arrange
    final var documentsApi = Mockito.mock(DocumentsApi.class);
    when(documentsApi.putDocuments(anyString(), any(PutDocumentsRequestDTO.class)))
        .thenReturn(new ResponseDTO(false, "This was not right"));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();
    final var epka = "epka".getBytes(StandardCharsets.UTF_8);

    // Act
    // Assert
    assertThrows(
        PsException.class,
        () -> service.putDocument(KVNR, epka),
        "Method EpaPsInterfaceImpl.putDocument did not throw an exception");
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
            new FindObjectsResponseDTO(
                Boolean.TRUE,
                SUCCESS_STATUS_MSG,
                new RegistryObjectLists(null, List.of(FIND_RESPONSE_DOCUMENT_DATA), null, null)));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).documentsProxy(documentsApi).build();

    // Act
    final var testResult = assertDoesNotThrow(() -> service.findEpka(KVNR));

    // Assert
    assertNotNull(testResult);
    assertEquals(1, testResult.size());
    assertTrue(
        testResult.stream()
            .allMatch(entryUUID -> entryUUID.equals(FIND_RESPONSE_DOCUMENT_DATA.entryUUID())));
  }

  @Test
  void testDeleteExistingDocuments() {
    // Arrange
    final var documentsApi = Mockito.mock(DocumentsApi.class);
    when(documentsApi.deleteObjects(anyString(), any(DeleteObjectsRequestDTO.class)))
        .thenReturn(STD_RESPONSE);
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
            new SignDocumentResponse(
                true,
                null,
                "I am signed".getBytes(StandardCharsets.UTF_8),
                SignatureForm.DOCUMENT_WITH_SIGNATURE));
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
            new SignDocumentResponse(
                true,
                null,
                "I am signed".getBytes(StandardCharsets.UTF_8),
                SignatureForm.DOCUMENT_WITH_SIGNATURE));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).signatureProxy(signatureApi).build();
    final var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    // Act
    final var tstResult =
        assertDoesNotThrow(
            () -> service.signDocument(document, false, SignatureAlgorithm.ECC.name()));

    // Assert
    assertNotNull(tstResult);
  }

  @SneakyThrows
  @Test
  void testConfigureKonnektor() {
    // Arrange
    final var configurationApi = Mockito.mock(ConfigurationApi.class);
    when(configurationApi.configureKonnektor(any())).thenReturn(STD_RESPONSE);
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .configurationProxy(configurationApi)
            .build();
    final var addressUrl =
        new URI("https", null, "testKonnektor", 8080, "/services", null, null).toURL();

    // Act
    // Assert
    assertDoesNotThrow(() -> service.configureKonnektor(addressUrl));
  }

  @SneakyThrows
  @Test
  void testConfigureKonnektorException() {
    // Arrange
    final var failureMsg = "I'm the expected failure message";
    final var configurationApi = Mockito.mock(ConfigurationApi.class);
    when(configurationApi.configureKonnektor(any())).thenReturn(new ResponseDTO(false, failureMsg));
    final var service =
        EpaPrimarySystemServiceImpl.builder()
            .config(config)
            .configurationProxy(configurationApi)
            .build();
    final var addressUrl =
        new URI("https", null, "testKonnektor", 8080, "/services", null, null).toURL();

    // Act
    final var exception =
        assertThrows(PsException.class, () -> service.configureKonnektor(addressUrl));

    // Assert
    assertTrue(exception.getMessage().contains(failureMsg));
  }

  @Test
  void testSignDocumentNoSignature() {
    // Arrange
    final var signatureApi = Mockito.mock(SignatureApi.class);
    when(signatureApi.signDocument(any()))
        .thenReturn(
            new SignDocumentResponse(Boolean.TRUE, SUCCESS_STATUS_MSG, (ByteArray) null, null));
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
        .thenReturn(new SignDocumentResponse(Boolean.FALSE, "Big mistake", (ByteArray) null, null));
    final var service =
        EpaPrimarySystemServiceImpl.builder().config(config).signatureProxy(signatureApi).build();
    final var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    // Act
    // Assert
    assertThrows(PsException.class, () -> service.signDocument(document));
  }
}
