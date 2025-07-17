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

import de.gematik.epa.conversion.internal.enumerated.ClassCode;
import de.gematik.epa.conversion.internal.enumerated.FormatCode;
import de.gematik.epa.conversion.internal.enumerated.TypeCode;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.Practice;
import de.gematik.test.ncp.data.SmbCard;
import de.gematik.test.ncp.gen.epa.api.authentication.LoginLogoutApi;
import de.gematik.test.ncp.gen.epa.api.card.CardApi;
import de.gematik.test.ncp.gen.epa.api.configuration.ConfigurationApi;
import de.gematik.test.ncp.gen.epa.api.documents.DocumentsApi;
import de.gematik.test.ncp.gen.epa.api.documents.dto.DeleteObjectsRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.DocumentMetadata;
import de.gematik.test.ncp.gen.epa.api.documents.dto.FindObjectsResponseDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.FindRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.RegistryObjectLists;
import de.gematik.test.ncp.gen.epa.api.entitlement.EntitlementApi;
import de.gematik.test.ncp.gen.epa.api.entitlement.dto.PostEntitlementRequestDTO;
import de.gematik.test.ncp.gen.epa.api.information.InformationApi;
import de.gematik.test.ncp.gen.epa.api.signature.SignatureApi;
import de.gematik.test.ncp.gen.epa.api.signature.dto.SignDocumentRequest;
import de.gematik.test.ncp.ps.PrimarySystemService;
import de.gematik.test.ncp.ps.PsException;
import de.gematik.test.ncp.ps.epaps.data.FindDocumentQueryBuilder;
import de.gematik.test.ncp.ps.epaps.data.PsTestdata;
import java.util.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the {@link PrimarySystemService} using the <a
 * href="https://nexus.prod.ccs.gematik.solutions/#browse/browse:gematik_releases:de%2Fgematik%2Ftitus%2Fepa-ps">epa-ps</a>.
 */
@Slf4j
@Builder
@RequiredArgsConstructor
public class EpaPrimarySystemServiceImpl implements PrimarySystemService {

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

  @Accessors(fluent = true)
  @Getter
  private final CardApi cardApiProxy;

  @Override
  public void authorizeLeForKvnr(@NonNull final String telematikId, @NonNull final String kvnr) {
    log.info("authorizeLeForKvnr for TelematikId: {} KVNR: {}", telematikId, kvnr);

    final var loginResponse = loginLogoutProxy.login(telematikId, kvnr, null);
    if (Boolean.FALSE.equals(loginResponse.getSuccess())) {
      throw new PsException(loginResponse.getStatusMessage(), "login");
    }

    final var request = new PostEntitlementRequestDTO().telematikId(telematikId).kvnr(kvnr);
    final var response = entitlementProxy.postEntitlement(kvnr, request);
    if (Boolean.FALSE.equals(response.getSuccess())) {
      throw new PsException(response.getStatusMessage(), "postEntitlement");
    }
  }

  @Override
  public String putDocument(
      @NonNull final String kvnr, final Practice practice, final byte[] epka) {
    final var request = PsTestdata.createPutDocumentRequestForEPKA(kvnr, practice, epka);
    final var response = documentsProxy().putDocuments(kvnr, request);
    if (!response.getSuccess()) throw new PsException(response.getStatusMessage(), "putDocuments");
    return request.getDocumentSets().stream()
        .findFirst()
        .map(doc -> doc.getDocumentMetadata().getTitle())
        .orElse(null);
  }

  @Override
  public String replaceDocument(
      @NonNull final String kvnr, final Practice practice, final byte[] epka) {
    final var epkas = findEpka(kvnr);
    if (!epkas.isEmpty()) {
      log.debug("Remove ePKAs for kvnr {}: {}", kvnr, String.join(", ", epkas));
      deleteExistingDocuments(kvnr, epkas);
    }

    return putDocument(kvnr, practice, epka);
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
    final var queryMetadata =
        FindDocumentQueryBuilder.getInstance()
            .withClassCode(ClassCode.MEDIZINISCHER_AUSWEIS)
            .withFormatCode(FormatCode.PATIENTEN_KURZ_AKTE)
            .withTypeCode(TypeCode.ERGEBNISSE_DIAGNOSTIK)
            .build();

    return findDocumentsUuids(kvnr, queryMetadata);
  }

  @Override
  public List<String> findAllDocuments(final String kvnr) {
    return findDocumentsUuids(kvnr, FindDocumentQueryBuilder.getInstance().build());
  }

  @Override
  public void deleteExistingDocuments(final String kvnr, final List<String> documentEntryUUIDs) {
    final var request = new DeleteObjectsRequestDTO().kvnr(kvnr).entryUUIDs(documentEntryUUIDs);

    final var response = documentsProxy().deleteObjects(kvnr, request);

    if (!response.getSuccess()) {
      throw new PsException(response.getStatusMessage(), "deleteObjects");
    }
  }

  @SneakyThrows
  @Override
  public byte[] signDocument(final byte[] document) {
    return signDocument(document, true, SignDocumentRequest.SignatureAlgorithmEnum.RSA_ECC);
  }

  @SneakyThrows
  @Override
  public byte[] signDocument(
      final byte[] document, final boolean asQES, final String signatureAlgorithm) {
    return signDocument(
        document, asQES, SignDocumentRequest.SignatureAlgorithmEnum.valueOf(signatureAlgorithm));
  }

  @Override
  public SmbCard getSmbCard() {
    final var response = cardApiProxy().getCardsInfo();

    if (!response.getSuccess()) {
      throw new PsException(response.getStatusMessage(), "getPractice");
    }

    return response.getSmbInfo().stream()
        .map(smbInfo -> new SmbCard(smbInfo.getCardHolderName(), smbInfo.getTelematikId()))
        .findFirst()
        .orElseThrow(() -> new PsException("No practice found", "getPractice"));
  }

  private List<String> findDocumentsUuids(
      final String kvnr, final Map<String, List<String>> queryMetadata) {
    final var findResponse = findDocuments(kvnr, queryMetadata);

    return Optional.ofNullable(findResponse.getRegistryObjectLists())
        .map(RegistryObjectLists::getDocumentsMetadata)
        .stream()
        .flatMap(List::stream)
        .map(DocumentMetadata::getEntryUUID)
        .toList();
  }

  private FindObjectsResponseDTO findDocuments(
      final String kvnr, final Map<String, List<String>> queryMetadata) {

    final var findRequest =
        new FindRequestDTO()
            .kvnr(kvnr)
            .query(FindRequestDTO.QueryEnum.FIND_DOCUMENTS)
            .queryData(queryMetadata)
            .returnType(FindRequestDTO.ReturnTypeEnum.LEAF_CLASS);

    return documentsProxy().find(kvnr, findRequest);
  }

  @SneakyThrows
  private byte[] signDocument(
      final byte[] document,
      final boolean asQES,
      final SignDocumentRequest.SignatureAlgorithmEnum signatureAlgorithm) {
    final var request =
        new SignDocumentRequest()
            .document(document)
            .performQES(asQES)
            .signatureAlgorithm(signatureAlgorithm);

    final var response = signatureProxy().signDocument(request);

    if (response.getSuccess()) {
      return Optional.ofNullable(response.getSignatureObject())
          .orElseThrow(
              () ->
                  new PsException(response.getStatusMessage(), "signDocument")
                      .initCause(
                          new NoSuchElementException(
                              "No signature was present in the signDocument response")));
    } else {
      throw new PsException(response.getStatusMessage(), "signDocument");
    }
  }
}
