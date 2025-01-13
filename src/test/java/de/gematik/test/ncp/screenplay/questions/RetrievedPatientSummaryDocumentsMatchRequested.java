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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.util.Utils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

@Slf4j
public class RetrievedPatientSummaryDocumentsMatchRequested implements Question<Boolean> {

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var patientSummaryDO =
        Objects.requireNonNull(patientData.getPatientSummaryDO(), "No patientSummary data present");

    final var retrieveDocumentsRequest =
        Objects.requireNonNull(
            patientSummaryDO.ncpehFdRequestContent(), "No retrieveDocuments request present");
    final var retrieveDocumentsResponse =
        Objects.requireNonNull(
            patientSummaryDO.ncpehFdResponseContent(), "No retrieveDocuments response present");

    log.debug(
        "Requested and received document data!\nRequested:\n{}\nReceived:{}",
        Utils.toStringListElements(
            Optional.ofNullable(retrieveDocumentsRequest.getDocumentRequest()).orElse(List.of()),
            dr ->
                String.format(
                    "hcid: %s; repositoryUniqueId: %s; documentUniqueId: %s",
                    dr.getHomeCommunityId(), dr.getRepositoryUniqueId(), dr.getDocumentUniqueId())),
        Utils.toStringListElements(
            Optional.ofNullable(retrieveDocumentsResponse.getDocumentResponse()).orElse(List.of()),
            dr ->
                String.format(
                    "hcid: %s; repositoryUniqueId: %s; documentUniqueId: %s",
                    dr.getHomeCommunityId(),
                    dr.getRepositoryUniqueId(),
                    dr.getDocumentUniqueId())));

    return Optional.ofNullable(retrieveDocumentsRequest.getDocumentRequest()).stream()
        .flatMap(List::stream)
        .allMatch(
            dreq -> {
              final List<RetrieveDocumentSetResponseType.DocumentResponse> responses =
                  Optional.ofNullable(retrieveDocumentsResponse.getDocumentResponse())
                      .orElse(List.of());
              return responses.stream()
                  .anyMatch(
                      dresp ->
                          dreq.getDocumentUniqueId().equals(dresp.getDocumentUniqueId())
                              && dreq.getHomeCommunityId().equals(dresp.getHomeCommunityId())
                              && dreq.getRepositoryUniqueId()
                                  .equals(dresp.getRepositoryUniqueId()));
            });
  }
}
