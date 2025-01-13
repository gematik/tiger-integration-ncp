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

import static de.gematik.epa.conversion.internal.enumerated.FormatCode.PATIENTEN_KURZ_AKTE;
import static de.gematik.test.ncp.util.IheUtils.STATUS_APPROVED;

import de.gematik.epa.api.ConfigurationApi;
import de.gematik.epa.api.DocumentsApi;
import de.gematik.epa.api.SignatureApi;
import de.gematik.epa.api.authentication.LoginLogoutApi;
import de.gematik.epa.api.entitlement.EntitlementApi;
import de.gematik.epa.api.entitlement.dto.PostEntitlementRequestDTO;
import de.gematik.epa.api.information.InformationApi;
import de.gematik.epa.config.AddressConfig;
import de.gematik.epa.config.KonnektorConnectionConfigurationDTO;
import de.gematik.epa.conversion.internal.enumerated.ClassCode;
import de.gematik.epa.conversion.internal.enumerated.TypeCode;
import de.gematik.epa.dto.request.DeleteObjectsRequestDTO;
import de.gematik.epa.dto.request.FindRequestDTO;
import de.gematik.epa.dto.request.KonnektorConfigurationRequestDTO;
import de.gematik.epa.dto.request.SignDocumentRequest;
import de.gematik.epa.dto.request.SignDocumentRequest.SignatureAlgorithm;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.epa.ihe.model.query.Query;
import de.gematik.epa.ihe.model.query.QueryKey;
import de.gematik.epa.ihe.model.query.ReturnType;
import de.gematik.epa.ihe.model.response.RegistryObjectLists;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.ps.PrimarySystemService;
import de.gematik.test.ncp.ps.PsException;
import de.gematik.test.ncp.ps.epaps.data.PsTestdata;
import io.restassured.response.Response;
import java.net.URI;
import java.net.URL;
import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import org.springframework.http.HttpStatus;

/**
 * Implementation of the {@link PrimarySystemService} using the <a
 * href="https://nexus.prod.ccs.gematik.solutions/#browse/browse:gematik_releases:de%2Fgematik%2Ftitus%2Fepa-ps">epa-ps</a>.
 */
@Slf4j
@Builder
@RequiredArgsConstructor
public class EpaPrimarySystemServiceImpl implements PrimarySystemService {

  public static final String HEALTH_PATH = "/health";

  public static final Integer HEALTH_PORT = 8016;

  @NonNull private final ExternalServerConfig config;

  @Accessors(fluent = true)
  @Getter
  private final DocumentsApi documentsProxy;

  @Accessors(fluent = true)
  @Getter
  private final EntitlementApi entitlementProxy;

  @Accessors(fluent = true)
  @Getter
  private final ConfigurationApi configurationProxy;

  @Accessors(fluent = true)
  @Getter
  private final SignatureApi signatureProxy;

  @Accessors(fluent = true)
  @Getter
  private final InformationApi informationProxy;

  @Accessors(fluent = true)
  @Getter
  private final LoginLogoutApi loginLogoutProxy;

  @Override
  public boolean psIsUpAndRunning() {
    try {
      final var returnStatus = HttpStatus.valueOf(restAssuredGet(HEALTH_PATH).statusCode());

      // This is more in line how Tiger handles the health check
      return returnStatus.is2xxSuccessful()
          || returnStatus.is1xxInformational()
          || returnStatus.is4xxClientError();
    } catch (final Exception e) {
      log.debug("Operation isUpAndRunning failed with Exception", e);
      return false;
    }
  }

  @Override
  public void authorizeLeForKvnr(@NonNull final String telematikId, @NonNull final String kvnr) {
    log.info("authorizeLeForKvnr for TelematikId: {} KVNR: {}", telematikId, kvnr);

    final var loginResponse = loginLogoutProxy.login(telematikId, kvnr, null);
    if (Boolean.FALSE.equals(loginResponse.getSuccess())) {
      throw new PsException(loginResponse.getStatusMessage(), "login");
    }

    final var request = new PostEntitlementRequestDTO().telematikId(telematikId).kvnr(kvnr);
    final var response = entitlementProxy.postEntitlement(request);
    if (Boolean.FALSE.equals(response.getSuccess())) {
      throw new PsException(response.getStatusMessage(), "postEntitlement");
    }
  }

  @Override
  public String putDocument(@NonNull final String kvnr, final byte[] epka) {
    final var request = PsTestdata.createPutDocumentRequestForEPKA(kvnr, epka);
    final var response = documentsProxy().putDocuments(kvnr, request);
    if (!response.success()) throw new PsException(response.statusMessage(), "putDocuments");
    return request.documentSets().stream()
        .findFirst()
        .map(doc -> doc.documentMetadata().title())
        .orElse(null);
  }

  @Override
  public boolean aktenkontoIsPresent(@NonNull final String kvnr) {
    log.info("Checking if an active Aktenkonto is present for KVNR: {}", kvnr);
    try {
      final var response = informationProxy.getRecordStatus(kvnr);

      if (Boolean.FALSE.equals(response.getSuccess())) {
        log.warn(
            "getRecordStatus() did not run successfully! Status message is: {}",
            response.getStatusMessage());
        return false;
      }
      return true;
    } catch (final RuntimeException e) {
      log.warn("Caught exception, so no active Aktenkonto was found", e);
      return false;
    }
  }

  @Override
  public List<String> findEpka(final String kvnr) {
    final var queryMetadata = new EnumMap<QueryKey, List<String>>(QueryKey.class);
    queryMetadata.put(
        QueryKey.XDS_DOCUMENT_ENTRY_CLASS_CODE,
        List.of(PsTestdata.createFindQueryValueList(ClassCode.MEDIZINISCHER_AUSWEIS)));
    queryMetadata.put(
        QueryKey.XDS_DOCUMENT_ENTRY_FORMAT_CODE,
        List.of(PsTestdata.createFindQueryValueList(PATIENTEN_KURZ_AKTE)));
    queryMetadata.put(
        QueryKey.XDS_DOCUMENT_ENTRY_TYPE_CODE,
        List.of(PsTestdata.createFindQueryValueList(TypeCode.ERGEBNISSE_DIAGNOSTIK)));

    final var findResponse = findDocuments(ReturnType.LEAF_CLASS, kvnr, queryMetadata);

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
  public void deleteExistingDocuments(final String kvnr, final List<String> documentEntryUUIDs) {
    final var request = new DeleteObjectsRequestDTO(kvnr, documentEntryUUIDs);

    final var response = documentsProxy().deleteObjects(kvnr, request);

    if (!response.success()) {
      throw new PsException(response.statusMessage(), "deleteObjects");
    }
  }

  @SneakyThrows
  @Override
  public byte[] signDocument(final byte[] document) {
    return signDocument(document, true, SignatureAlgorithm.RSA_ECC);
  }

  @SneakyThrows
  @Override
  public byte[] signDocument(
      final byte[] document, final boolean asQES, final String signatureAlgorithm) {
    return signDocument(document, asQES, SignatureAlgorithm.valueOf(signatureAlgorithm));
  }

  @Override
  public void configureKonnektor(final URL konnektorAddress) {
    final var address =
        new AddressConfig(
            konnektorAddress.getHost(),
            konnektorAddress.getPort(),
            konnektorAddress.getProtocol(),
            konnektorAddress.getPath());
    final var request =
        new KonnektorConfigurationRequestDTO(
            new KonnektorConnectionConfigurationDTO(address, null, null, null, Boolean.TRUE),
            null,
            Boolean.TRUE);

    final var response = configurationProxy().configureKonnektor(request);

    if (!response.success()) {
      throw new PsException(response.statusMessage(), "configureKonnektor");
    }
  }

  public Response restAssuredGet(final String path) {
    return SerenityRest.get(URI.create("http://" + config.getHostname()).resolve(path));
  }

  private de.gematik.epa.dto.response.FindObjectsResponseDTO findDocuments(
      final ReturnType returnType,
      final String kvnr,
      final Map<QueryKey, List<String>> queryMetadata) {

    queryMetadata.computeIfAbsent(
        QueryKey.XDS_DOCUMENT_ENTRY_STATUS, absentStatus -> List.of(STATUS_APPROVED));
    final var findRequest =
        new FindRequestDTO(kvnr, Query.FIND_DOCUMENTS, queryMetadata, returnType);

    return documentsProxy().find(kvnr, findRequest);
  }

  @SneakyThrows
  private byte[] signDocument(
      final byte[] document, final boolean asQES, final SignatureAlgorithm signatureAlgorithm) {
    final var request = new SignDocumentRequest(document, asQES, signatureAlgorithm);

    final var response = signatureProxy().signDocument(request);

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
}
