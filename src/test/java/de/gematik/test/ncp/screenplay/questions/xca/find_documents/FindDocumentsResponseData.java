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

package de.gematik.test.ncp.screenplay.questions.xca.find_documents;

import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.EPED_TESTACTOR_NAME;

import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.RememberAuthoredPrescriptions;
import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import de.gematik.test.ncp.util.IheUtils;
import de.gematik.test.ncp.util.PrescriptionUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.actors.OnStage;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/** Question for querying relevant data from the AdhocQueryResponse of a findDocuments request. */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindDocumentsResponseData {

  @Getter private final AdhocQueryResponse metadata;

  /**
   * Factory method to create a new instance of {@link FindDocumentsResponseData} based on the given
   * metadata.
   *
   * @param metadata the AdhocQueryResponse containing the metadata to be queried
   */
  public static FindDocumentsResponseData from(final AdhocQueryResponse metadata) {
    return new FindDocumentsResponseData(metadata);
  }

  /**
   * Question to retrieve the document unique IDs from the AdhocQueryResponse that match the given
   * CDA level
   *
   * @param level the CDA level to filter the document unique IDs by
   * @return a Question that can be answered by an Actor to get the set of unique IDs matching the
   *     given CDA level
   */
  public Question<Set<String>> documentUidsMatchingCdaLevel(final PrescriptionCdaLevel level) {
    return Question.about(
            "the unique IDs referring to the CDA level %d representation of available prescriptions"
                .formatted(level.getIntValue()))
        .answeredBy(
            actor ->
                IheUtils.extractDocumentIdsFromAdhocQueryResponse(metadata).stream()
                    .filter(level::appliesTo)
                    .collect(Collectors.toSet()));
  }

  public Question<List<String>> documentUids() {
    return Question.about("the unique IDs of available documents")
        .answeredBy(actor -> IheUtils.extractDocumentIdsFromAdhocQueryResponse(metadata));
  }

  /**
   * Question to retrieve the response status from the AdhocQueryResponse metadata.
   *
   * @return a Question that can be answered by an Actor to get the response status of the
   *     findDocuments response
   */
  public Question<String> responseStatus() {
    return Question.about("the response status of the findDocuments response")
        .answeredBy(
            actor ->
                Optional.ofNullable(metadata).map(RegistryResponseType::getStatus).orElse(null));
  }

  /**
   * Question to count the number of prescription document references in the AdhocQueryResponse
   *
   * @return a Question that can be answered by an Actor to get the count of prescription document
   *     references in the findDocuments response
   */
  public Question<Integer> documentCount() {
    return Question.about(
            "the number of prescription document references in the AdhocQueryResponse")
        .answeredBy(actor -> IheUtils.countExtrinsicObjects(metadata));
  }

  public Question<Set<String>> mostRecentDocumentUidsForLevel(
      final int n, final PrescriptionCdaLevel cdaLevel) {
    return Question.about(
            "the unique IDs of the %d most recent prescriptions with CDA level %d"
                .formatted(n, cdaLevel.getIntValue()))
        .answeredBy(
            actor -> {
              var patient = actor.asksFor(CurrentPatient.asActor());
              List<Medication> euPrescriptions =
                  patient.usingAbilityTo(ProvidePatientData.class).getEuRedeemablePrescriptions();

              // use last N prescriptions from list in test step
              List<String> targetPzns =
                  euPrescriptions.reversed().stream().limit(n).map(Medication::pzn).toList();

              var testactor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
              Map<String, PrescriptionDto> prescribed =
                  testactor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions();

              // find ids of prescriptions matching target PZNs
              Set<String> targetIds =
                  prescribed.entrySet().stream()
                      .filter(
                          entry -> {
                            String pzn = entry.getValue().getMedication().getPzn();
                            return targetPzns.contains(pzn);
                          })
                      .map(Entry::getKey)
                      .collect(Collectors.toSet());

              // find documentUids matching target prescription ids and CDA level
              Set<String> uidsForLevel = actor.asksFor(documentUidsMatchingCdaLevel(cdaLevel));

              return uidsForLevel.stream()
                  .filter(
                      uid ->
                          targetIds.contains(
                              PrescriptionUtils.extractPrescriptionIdFromDocumentUid(uid)))
                  .collect(Collectors.toSet());
            });
  }
}
