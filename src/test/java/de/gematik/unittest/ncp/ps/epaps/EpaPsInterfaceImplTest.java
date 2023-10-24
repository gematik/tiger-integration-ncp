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

package de.gematik.unittest.ncp.ps.epaps;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.epa.api.ConfigurationApi;
import de.gematik.epa.api.DocumentsApi;
import de.gematik.epa.api.PermissionApi;
import de.gematik.epa.dto.request.DeleteObjectsRequestDTO;
import de.gematik.epa.dto.request.FindRequestDTO;
import de.gematik.epa.dto.request.PutDocumentsRequestDTO;
import de.gematik.epa.dto.request.SignDocumentRequest;
import de.gematik.epa.dto.request.SignDocumentRequest.SignatureAlgorithm;
import de.gematik.epa.dto.response.AuthorizedApplication;
import de.gematik.epa.dto.response.FindObjectsResponseDTO;
import de.gematik.epa.dto.response.GetAuthorizationStateResponseDTO;
import de.gematik.epa.dto.response.ResponseDTO;
import de.gematik.epa.dto.response.SignDocumentResponse;
import de.gematik.epa.dto.response.SignDocumentResponse.SignatureForm;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.epa.ihe.model.response.RegistryObjectLists;
import de.gematik.epa.ihe.model.simple.ByteArray;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.ps.PsException;
import de.gematik.test.ncp.ps.PsProvider;
import de.gematik.test.ncp.ps.epaps.EpaPsInterfaceImpl;
import de.gematik.test.ncp.ps.epaps.data.PsTestdata;
import io.restassured.response.Response;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import net.serenitybdd.rest.stubs.ResponseStub;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class EpaPsInterfaceImplTest {

  public EpaPsInterfaceImplForUnitTest tstObj =
      new EpaPsInterfaceImplForUnitTest(PsProvider.getPsProvider().getEpaPsConfig());

  @Test
  void psIsUpAndRunningTest() {
    var isUpAndRunning = assertDoesNotThrow(() -> tstObj.psIsUpAndRunning());

    assertTrue(isUpAndRunning);
  }

  @Test
  void psIsUpAndRunningExceptionTest() {
    tstObj.restAssuredStatusCode(13);
    var isUpAndRunning = assertDoesNotThrow(() -> tstObj.psIsUpAndRunning());

    assertFalse(isUpAndRunning);
  }

  @Test
  void isLeAuthorizedForKvnr() {
    var testResult =
        assertDoesNotThrow(
            () -> tstObj.isLeAuthorizedForKvnr(EpaPsInterfaceImplForUnitTest.KVNR),
            "Method EpaPsInterfaceImpl.isLeAuthorizedForKvnr threw an exception");
    assertTrue(testResult, "Method EpaPsInterfaceImpl.isLeAuthorizedForKvnr returned False");
  }

  @Test
  void isLeAuthorizedForKvnrThrows() {
    var permProxyMock = tstObj.permissionsProxy();
    Mockito.when(permProxyMock.getAuthorizationState(Mockito.any()))
        .thenThrow(IllegalArgumentException.class);

    var testResult =
        assertDoesNotThrow(
            () -> tstObj.isLeAuthorizedForKvnr(EpaPsInterfaceImplForUnitTest.KVNR),
            "Method EpaPsInterfaceImpl.isLeAuthorizedForKvnr threw an exception");

    assertFalse(testResult, "Method EpaPsInterfaceImpl.isLeAuthorizedForKvnr returned True");
  }

  @Test
  void authorizeLeForKvnr() {
    assertDoesNotThrow(
        () -> tstObj.authorizeLeForKvnr(EpaPsInterfaceImplForUnitTest.KVNR),
        "Method EpaPsInterfaceImpl.authorizeLeForKvnr threw an exception");
  }

  @Test
  void authorizeLeForKvnrThrows() {
    var permProxyMock = tstObj.permissionsProxy();
    Mockito.when(permProxyMock.permissionHcpo(Mockito.any()))
        .thenThrow(IllegalFormatException.class);

    assertThrows(
        IllegalFormatException.class,
        () -> tstObj.authorizeLeForKvnr(EpaPsInterfaceImplForUnitTest.KVNR),
        "Method EpaPsInterfaceImpl.authorizeLeForKvnr did not throw an exception");
  }

  @Test
  void putDocument() {
    var testResult =
        assertDoesNotThrow(
            () ->
                tstObj.putDocument(
                    EpaPsInterfaceImplForUnitTest.KVNR, "epka".getBytes(StandardCharsets.UTF_8)),
            "Method EpaPsInterfaceImpl.putDocument threw an exception");

    assertNotNull(testResult, "Return value is null");
    assertEquals(
        testResult,
        EpaPsInterfaceImplForUnitTest.PUT_DOCUMENT_DATA
            .documentSets()
            .get(0)
            .documentMetadata()
            .title(),
        "Document title does not have the expected value");
  }

  @Test
  void putDocumentFail() {
    var docProxyMock = tstObj.documentsProxy();
    Mockito.when(docProxyMock.putDocuments(Mockito.any()))
        .thenReturn(new de.gematik.epa.dto.response.ResponseDTO(false, "This was not right"));
    var pseudoEpka = "epka".getBytes(StandardCharsets.UTF_8);

    assertThrows(
        PsException.class,
        () -> tstObj.putDocument(EpaPsInterfaceImplForUnitTest.KVNR, pseudoEpka),
        "Method EpaPsInterfaceImpl.putDocument did not throw an exception");
  }

  @Test
  void aktenKontoIsPresentTest() {
    var result =
        assertDoesNotThrow(() -> tstObj.aktenkontoIsPresent(EpaPsInterfaceImplForUnitTest.KVNR));

    assertTrue(result);
  }

  @Test
  void documentCanBeFound() {
    var testResult =
        assertDoesNotThrow(
            () ->
                tstObj.documentCanBeFound(
                    EpaPsInterfaceImplForUnitTest.KVNR,
                    EpaPsInterfaceImplForUnitTest.PUT_DOCUMENT_DATA
                        .documentSets()
                        .get(0)
                        .documentMetadata()
                        .title()),
            "Method EpaPsInterfaceImpl.epkaCanBeFoundInAktenkonto threw an exception");

    assertTrue(testResult, "Method EpaPsInterfaceImpl.epkaCanBeFoundInAktenkonto returned False");
  }

  @Test
  void documentCanNotBeFound() {
    var docProxyMock = tstObj.documentsProxy();
    Mockito.when(docProxyMock.find(Mockito.any()))
        .thenReturn(
            new de.gematik.epa.dto.response.FindObjectsResponseDTO(
                true,
                "Operation was successful, but no document was found",
                new RegistryObjectLists(List.of(), List.of(), List.of(), List.of())));

    var testResult =
        assertDoesNotThrow(
            () ->
                tstObj.documentCanBeFound(
                    EpaPsInterfaceImplForUnitTest.KVNR,
                    EpaPsInterfaceImplForUnitTest.FIND_RESPONSE_DOCUMENT_DATA.title()),
            "Method EpaPsInterfaceImpl.epkaCanBeFoundInAktenkonto threw an exception");

    assertFalse(testResult, "Method EpaPsInterfaceImpl.epkaCanBeFoundInAktenkonto returned True");
  }

  @Test
  void documentCanBeFoundFails() {
    var docProxyMock = tstObj.documentsProxy();
    Mockito.when(docProxyMock.find(Mockito.any()))
        .thenReturn(
            new de.gematik.epa.dto.response.FindObjectsResponseDTO(
                true, "Operation was successful, but no document was found", null));

    var docTitle = EpaPsInterfaceImplForUnitTest.FIND_RESPONSE_DOCUMENT_DATA.title();

    var result =
        assertDoesNotThrow(
            () -> tstObj.documentCanBeFound(EpaPsInterfaceImplForUnitTest.KVNR, docTitle),
            "Method EpaPsInterfaceImpl.epkaCanBeFoundInAktenkonto did throw an exception");

    assertFalse(result);
  }

  @Test
  void findEpka() {
    var testResult = assertDoesNotThrow(() -> tstObj.findEpka(EpaPsInterfaceImplForUnitTest.KVNR));

    assertNotNull(testResult);
    assertEquals(1, testResult.size());
    assertTrue(
        testResult.stream()
            .allMatch(
                entryUUID ->
                    entryUUID.equals(
                        EpaPsInterfaceImplForUnitTest.FIND_RESPONSE_DOCUMENT_DATA.entryUUID())));
  }

  @Test
  void deleteExistingDocuments() {
    assertDoesNotThrow(
        () ->
            tstObj.deleteExistingDocuments(
                EpaPsInterfaceImplForUnitTest.KVNR, List.of(UUID.randomUUID().toString())));
  }

  @Test
  void testProxyInstantiation() {
    var proxy =
        assertDoesNotThrow(
            () ->
                new EpaPsInterfaceImpl(PsProvider.getPsProvider().getEpaPsConfig())
                    .documentsProxy(),
            "Proxy instantiation threw exception");

    assertNotNull(proxy, "Proxy instance is null");
  }

  @Test
  void signDocumentTest() {
    var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    var tstResult = assertDoesNotThrow(() -> tstObj.signDocument(document));
    assertNotNull(tstResult);
  }

  @Test
  void signDocumentWithParametersTest() {
    var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    var tstResult =
        assertDoesNotThrow(
            () -> tstObj.signDocument(document, false, SignatureAlgorithm.ECC.name()));
    assertNotNull(tstResult);
  }

  @SneakyThrows
  @Test
  void configureKonnektorTest() {
    var addressUrl = new URL("https", "testKonnektor", 8080, "services");

    assertDoesNotThrow(() -> tstObj.configureKonnektor(addressUrl));
  }

  @SneakyThrows
  @Test
  void configureKonnektorExceptionTest() {
    var addressUrl = new URL("https", "testKonnektor", 8080, "services");
    var failureMsg = "I'm the expected failure message";
    Mockito.when(tstObj.configurationProxy().configureKonnektor(Mockito.any()))
        .thenReturn(new ResponseDTO(false, failureMsg));

    var exception = assertThrows(PsException.class, () -> tstObj.configureKonnektor(addressUrl));

    assertTrue(exception.getMessage().contains(failureMsg));
  }

  @Test
  void signDocumentNoSignature() {
    Mockito.when(tstObj.documentsProxy().signDocument(Mockito.any(SignDocumentRequest.class)))
        .thenReturn(
            new SignDocumentResponse(
                Boolean.TRUE,
                EpaPsInterfaceImplForUnitTest.SUCCESS_STATUS_MSG,
                (ByteArray) null,
                null));
    var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    assertThrows(PsException.class, () -> tstObj.signDocument(document));
  }

  @Test
  void signDocumentFail() {
    Mockito.when(tstObj.documentsProxy().signDocument(Mockito.any(SignDocumentRequest.class)))
        .thenReturn(new SignDocumentResponse(Boolean.FALSE, "Big mistake", (ByteArray) null, null));
    var document = "I'm a test document".getBytes(StandardCharsets.UTF_8);

    assertThrows(PsException.class, () -> tstObj.signDocument(document));
  }

  @Accessors(fluent = true)
  private static class EpaPsInterfaceImplForUnitTest extends EpaPsInterfaceImpl {

    public static String SUCCESS_STATUS_MSG = "All went well!";

    public static final String KVNR = "X110987654";

    public static final ResponseDTO STD_RESPONSE =
        new ResponseDTO(Boolean.TRUE, SUCCESS_STATUS_MSG);

    public static de.gematik.test.ncp.ps.epaps.data.DocumentMetadata DOCUMENT_METADATA =
        PsTestdata.createDocumentMetadata();

    public static PutDocumentsRequestDTO PUT_DOCUMENT_DATA =
        PsTestdata.createPutDocumentRequestForEPKA(
            KVNR, "epka".getBytes(StandardCharsets.UTF_8), DOCUMENT_METADATA);

    public static DocumentMetadata FIND_RESPONSE_DOCUMENT_DATA =
        DOCUMENT_METADATA.withEntryUUID("urn:uuid:2305gnwo8rh2").toPsDocumentMetadata();

    public static GetAuthorizationStateResponseDTO GET_AUTHORIZATION_STATE_RESPONSE =
        new GetAuthorizationStateResponseDTO(
            Boolean.TRUE,
            SUCCESS_STATUS_MSG,
            List.of(new AuthorizedApplication("ePA Anwendung", LocalDate.now().plusMonths(1))));

    @Setter private int restAssuredStatusCode = 404;

    @Getter(lazy = true, onMethod_ = @Override)
    private final DocumentsApi documentsProxy = initDocumentsProxy();

    @Getter(lazy = true, onMethod_ = @Override)
    private final PermissionApi permissionsProxy = initPermissionsProxy();

    @Getter(lazy = true, onMethod_ = @Override)
    private final ConfigurationApi configurationProxy = initConfigurationProxy();

    public EpaPsInterfaceImplForUnitTest(ExternalServerConfig config) {
      super(config);
    }

    @Override
    protected Response restAssuredGet(String path) {
      return new ResponseStub() {
        @Override
        public int statusCode() {
          return restAssuredStatusCode;
        }
      };
    }

    private DocumentsApi initDocumentsProxy() {
      var mock = Mockito.mock(DocumentsApi.class);
      Mockito.doAnswer(
              invocation -> {
                PutDocumentsRequestDTO request = invocation.getArgument(0);
                PUT_DOCUMENT_DATA = request;
                FIND_RESPONSE_DOCUMENT_DATA =
                    DOCUMENT_METADATA
                        .withEntryUUID("urn:uuid:" + UUID.randomUUID())
                        .withTitle(request.documentSets().get(0).documentMetadata().title())
                        .toPsDocumentMetadata();
                return STD_RESPONSE;
              })
          .when(mock)
          .putDocuments(Mockito.any(PutDocumentsRequestDTO.class));
      //      Mockito.when(mock.putDocuments(Mockito.any(PutDocumentsRequestDTO.class)))
      //          .thenReturn(STD_RESPONSE);
      Mockito.when(mock.find(Mockito.any(FindRequestDTO.class)))
          .thenReturn(
              new FindObjectsResponseDTO(
                  Boolean.TRUE,
                  SUCCESS_STATUS_MSG,
                  new RegistryObjectLists(null, List.of(FIND_RESPONSE_DOCUMENT_DATA), null, null)));
      Mockito.when(mock.deleteObjects(Mockito.any(DeleteObjectsRequestDTO.class)))
          .thenReturn(STD_RESPONSE);
      Mockito.when(mock.signDocument(Mockito.any()))
          .thenReturn(
              new SignDocumentResponse(
                  true,
                  null,
                  "I am signed".getBytes(StandardCharsets.UTF_8),
                  SignatureForm.DOCUMENT_WITH_SIGNATURE));

      return mock;
    }

    private de.gematik.epa.api.PermissionApi initPermissionsProxy() {
      var mock = Mockito.mock(PermissionApi.class);
      Mockito.when(mock.permissionHcpo(Mockito.any())).thenReturn(STD_RESPONSE);
      Mockito.when(mock.getAuthorizationState(Mockito.any()))
          .thenReturn(GET_AUTHORIZATION_STATE_RESPONSE);

      return mock;
    }

    private ConfigurationApi initConfigurationProxy() {
      var mock = Mockito.mock(ConfigurationApi.class);
      Mockito.when(mock.configureKonnektor(Mockito.any())).thenReturn(STD_RESPONSE);

      return mock;
    }
  }
}
