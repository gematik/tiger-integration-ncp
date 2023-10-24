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

package de.gematik.test.ncp.ps.epaps;

import static de.gematik.epa.conversion.internal.enumerated.FormatCode.NOTFALLDATENSATZ;
import static de.gematik.test.ncp.util.IheUtils.STATUS_APPROVED;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.gematik.epa.api.ConfigurationApi;
import de.gematik.epa.api.DocumentsApi;
import de.gematik.epa.api.PermissionApi;
import de.gematik.epa.config.AddressConfig;
import de.gematik.epa.config.KonnektorConnectionConfigurationDTO;
import de.gematik.epa.conversion.internal.enumerated.ClassCode;
import de.gematik.epa.conversion.internal.enumerated.TypeCode;
import de.gematik.epa.dto.request.DeleteObjectsRequestDTO;
import de.gematik.epa.dto.request.FindRequestDTO;
import de.gematik.epa.dto.request.FolderCode;
import de.gematik.epa.dto.request.GetAuthorizationStateRequest;
import de.gematik.epa.dto.request.KonnektorConfigurationRequestDTO;
import de.gematik.epa.dto.request.PermissionHcpoRequest;
import de.gematik.epa.dto.request.SignDocumentRequest;
import de.gematik.epa.dto.request.SignDocumentRequest.SignatureAlgorithm;
import de.gematik.epa.dto.response.GetAuthorizationStateResponseDTO;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.epa.ihe.model.query.Query;
import de.gematik.epa.ihe.model.query.QueryKey;
import de.gematik.epa.ihe.model.query.ReturnType;
import de.gematik.epa.ihe.model.response.RegistryObjectLists;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.ps.PsException;
import de.gematik.test.ncp.ps.PsInterface;
import de.gematik.test.ncp.ps.epaps.data.PsTestdata;
import de.gematik.test.ncp.util.Utils;
import io.restassured.response.Response;
import jakarta.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URL;
import java.util.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;

/**
 * Implementation of the {@link PsInterface} using the <a
 * href="https://nexus.prod.ccs.gematik.solutions/#browse/browse:gematik_releases:de%2Fgematik%2Ftitus%2Fepa-ps">epa-ps</a>.
 */
@Slf4j
@RequiredArgsConstructor
public class EpaPsInterfaceImpl implements PsInterface {

  public static final String HEALTH_PATH = "/health";

  public static final Integer HEALTH_PORT = 8016;

  protected final ExternalServerConfig config;

  @Accessors(fluent = true)
  @Getter(lazy = true)
  private final JacksonJsonProvider jacksonJsonProvider = createJacksonJsonProvider();

  @Accessors(fluent = true)
  @Getter(lazy = true)
  private final DocumentsApi documentsProxy = instantiateRestProxyImpl(DocumentsApi.class);

  @Accessors(fluent = true)
  @Getter(lazy = true)
  private final PermissionApi permissionsProxy = instantiateRestProxyImpl(PermissionApi.class);

  @Accessors(fluent = true)
  @Getter(lazy = true)
  private final ConfigurationApi configurationProxy =
      instantiateRestProxyImpl(ConfigurationApi.class);

  @Override
  public boolean psIsUpAndRunning() {
    try {
      var returnStatus = HttpStatus.valueOf(restAssuredGet(HEALTH_PATH).statusCode());

      // This is more in line how Tiger handles the health check
      return returnStatus.is2xxSuccessful()
          || returnStatus.is1xxInformational()
          || returnStatus.is4xxClientError();
    } catch (Exception e) {
      log.debug("Operation isUpAndRunning failed with Exception", e);
      return false;
    }
  }

  @Override
  public boolean isLeAuthorizedForKvnr(@NonNull String kvnr) {
    var request = new GetAuthorizationStateRequest(kvnr);

    GetAuthorizationStateResponseDTO psResponse = null;

    try {
      psResponse = permissionsProxy().getAuthorizationState(request);
      if (!psResponse.success()) return false;
    } catch (Exception e) {
      log.warn("Exception caught while executing authorization check", e);
      return false;
    }

    return Optional.ofNullable(psResponse.authorizedApplications()).stream()
        .flatMap(Collection::stream)
        .anyMatch(fa -> fa.applicationName().toLowerCase().contains("epa"));
  }

  @Override
  public void authorizeLeForKvnr(@NonNull String kvnr) {

    var request =
        new PermissionHcpoRequest(
            kvnr,
            Set.of(
                FolderCode.PATIENTDOC,
                FolderCode.NFD,
                FolderCode.PRACTITIONER,
                FolderCode.OTHER_MEDICAL));

    var psResponse = permissionsProxy().permissionHcpo(request);
    if (!psResponse.success()) throw new PsException(psResponse.statusMessage(), "permissionHcpo");
  }

  @Override
  public String putDocument(@NonNull String kvnr, byte[] epka) {
    var request = PsTestdata.createPutDocumentRequestForEPKA(kvnr, epka);
    var response = documentsProxy().putDocuments(request);
    if (!response.success()) throw new PsException(response.statusMessage(), "putDocuments");
    return request.documentSets().stream()
        .findFirst()
        .map(doc -> doc.documentMetadata().title())
        .orElse(null);
  }

  @Override
  @Deprecated(forRemoval = true)
  public boolean documentCanBeFound(@NonNull String kvnr, @NonNull String documentTitle) {
    var findResponse = findDocuments(ReturnType.LEAF_CLASS, kvnr);

    return Optional.ofNullable(findResponse.registryObjectLists())
        .map(RegistryObjectLists::documentsMetadata)
        .stream()
        .flatMap(Collection::stream)
        .anyMatch(entry -> documentTitle.equals(entry.title()));
  }

  @Override
  public boolean aktenkontoIsPresent(@NonNull String kvnr) {
    try {
      return findDocuments(ReturnType.OBJECT_REF, kvnr).success();
    } catch (Exception e) {
      log.debug("Caught exception, so no active Aktenkonto was found", e);
      return false;
    }
  }

  @Override
  public List<String> findEpka(String kvnr) {
    var queryMetadata = new EnumMap<QueryKey, List<String>>(QueryKey.class);
    queryMetadata.put(
        QueryKey.XDS_DOCUMENT_ENTRY_CLASS_CODE,
        List.of(PsTestdata.createFindQueryValueList(ClassCode.MEDIZINISCHER_AUSWEIS)));
    queryMetadata.put(
        QueryKey.XDS_DOCUMENT_ENTRY_FORMAT_CODE,
        // TODO: As soon as the Aktensystem knows it, use the correct format code here
        // List.of(PsTestdata.createFindQueryValueList(PATIENTEN_KURZ_AKTE)));
        List.of(PsTestdata.createFindQueryValueList(NOTFALLDATENSATZ)));
    queryMetadata.put(
        QueryKey.XDS_DOCUMENT_ENTRY_TYPE_CODE,
        List.of(PsTestdata.createFindQueryValueList(TypeCode.ERGEBNISSE_DIAGNOSTIK)));

    var findResponse = findDocuments(ReturnType.LEAF_CLASS, kvnr, queryMetadata);

    if (Objects.requireNonNull(findResponse).success()) {
      return Optional.ofNullable(findResponse.registryObjectLists())
          .map(RegistryObjectLists::documentsMetadata)
          .stream()
          .flatMap(List::stream)
          .map(DocumentMetadata::entryUUID)
          .toList();
    } else {
      log.warn(
          "findDocument did not run successfully! Status message is: {}",
          findResponse.statusMessage());
      return List.of();
    }
  }

  @Override
  public void deleteExistingDocuments(String kvnr, List<String> documentEntryUUIDs) {
    var request = new DeleteObjectsRequestDTO(kvnr, documentEntryUUIDs);

    var response = documentsProxy().deleteObjects(request);

    if (!response.success()) {
      throw new PsException(response.statusMessage(), "deleteObjects");
    }
  }

  @SneakyThrows
  @Override
  public byte[] signDocument(byte[] document) {
    return signDocument(document, true, SignatureAlgorithm.RSA_ECC);
  }

  @SneakyThrows
  @Override
  public byte[] signDocument(byte[] document, boolean asQES, String signatureAlgorithm) {
    return signDocument(document, asQES, SignatureAlgorithm.valueOf(signatureAlgorithm));
  }

  @Override
  public void configureKonnektor(URL konnektorAddress) {
    var address =
        new AddressConfig(
            konnektorAddress.getHost(),
            konnektorAddress.getPort(),
            konnektorAddress.getProtocol(),
            konnektorAddress.getPath());
    var request =
        new KonnektorConfigurationRequestDTO(
            new KonnektorConnectionConfigurationDTO(address, null, null, null, Boolean.TRUE),
            null,
            Boolean.TRUE);

    var response = configurationProxy().configureKonnektor(request);

    if (!response.success()) {
      throw new PsException(response.statusMessage(), "configureKonnektor");
    }
  }

  protected Response restAssuredGet(String path) {
    return SerenityRest.get(URI.create("http://" + config.getHostname()).resolve(path));
  }

  private de.gematik.epa.dto.response.FindObjectsResponseDTO findDocuments(
      ReturnType returnType, String kvnr) {
    return findDocuments(returnType, kvnr, new EnumMap<>(QueryKey.class));
  }

  private de.gematik.epa.dto.response.FindObjectsResponseDTO findDocuments(
      ReturnType returnType, String kvnr, Map<QueryKey, List<String>> queryMetadata) {

    queryMetadata.computeIfAbsent(
        QueryKey.XDS_DOCUMENT_ENTRY_STATUS, absentStatus -> List.of(STATUS_APPROVED));
    var findRequest = new FindRequestDTO(kvnr, Query.FIND_DOCUMENTS, queryMetadata, returnType);

    return documentsProxy().find(findRequest);
  }

  @SneakyThrows
  private byte[] signDocument(
      byte[] document, boolean asQES, SignatureAlgorithm signatureAlgorithm) {
    var request = new SignDocumentRequest(document, asQES, signatureAlgorithm);

    var response = documentsProxy().signDocument(request);

    if (response.success()) {
      return Optional.ofNullable(response.signatureObject())
          .orElseThrow(
              () ->
                  new PsException(response.statusMessage(), "signDocument")
                      .initCause(
                          new NoSuchElementException(
                              "No signature was present in the signDocument response")))
          .value();
    } else {
      throw new PsException(response.statusMessage(), "signDocument");
    }
  }

  private <T> T instantiateRestProxyImpl(@NonNull Class<T> proxyClass) {
    assert config != null;
    return JAXRSClientFactory.create(
        Utils.buildUri(config.getHostname(), config.getBasePath()).toString(),
        proxyClass,
        List.of(jacksonJsonProvider()));
  }

  private JacksonJsonProvider createJacksonJsonProvider() {
    var provider =
        new JacksonJsonProvider()
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    provider
        .locateMapper(DateTime.class, MediaType.APPLICATION_JSON_TYPE)
        .setSerializationInclusion(Include.NON_ABSENT)
        //        .registerModule(new JodaModule())
        .registerModule(new JavaTimeModule());

    return provider;
  }
}
