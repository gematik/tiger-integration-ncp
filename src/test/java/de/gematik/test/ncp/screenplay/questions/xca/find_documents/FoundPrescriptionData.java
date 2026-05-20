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

import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.util.IheUtils;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/** Questions to check the prescription data found in the response to a FindDocuments request. */
@UtilityClass
public class FoundPrescriptionData {

  /**
   * Checks if all prescriptions found in the response match the given CDA level.
   *
   * @param cdaLevel the CDA level to check for
   * @return a Question that returns true if all prescriptions match the given CDA level, false
   *     otherwise
   */
  public Question<Boolean> allMatchLevel(final PrescriptionCdaLevel cdaLevel) {
    return Question.about(
            "the check if all prescriptions are available with CDA level %s returned a boolean value"
                .formatted(cdaLevel))
        .answeredBy(actor -> allPrescriptionsMatchLevel(actor, cdaLevel));
  }

  private Boolean allPrescriptionsMatchLevel(
      final Actor actor, final PrescriptionCdaLevel cdaLevel) {
    var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    var patientRecord = actor.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);

    var metadata =
        Optional.ofNullable(patientRecord.getPrescriptionMetadata())
            .orElseThrow(() -> new NoSuchElementException("No prescription metadata available"));

    var documentIds = IheUtils.extractDocumentIdsFromAdhocQueryResponse(metadata);
    if (documentIds.isEmpty()) {
      throw new NoSuchElementException("No document IDs found in metadata");
    }

    return documentIds.stream()
        .collect(
            // Map a unique ID without level marker to a boolean indicating if the same ID was
            // found with a marker for the desired level. Duplicate IDs with different markers
            // are handled by OR-ing the boolean values.
            Collectors.toMap(
                IheUtils::stripLevelMarkerFromDocumentUid, cdaLevel::appliesTo, (a, b) -> a || b))
        .values()
        .stream()
        .allMatch(v -> v);
  }
}
