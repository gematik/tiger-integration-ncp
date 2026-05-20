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

package de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents;

import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import de.gematik.test.ncp.util.PrescriptionUtils;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.serenitybdd.screenplay.Question;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RetrievedPrescriptions {

  /** Question to query the (E-prescription service) ids of retrieved prescriptions. */
  public static Question<Set<String>> ids() {
    return Question.about(
            "the ids of the retrieved prescriptions assigned by the E-prescription service")
        .answeredBy(
            actor -> {
              var patientRecord = actor.asksFor(CurrentPatient.patientRecord());
              var documentUids = patientRecord.getPrescriptionsByDocumentId().keySet();
              return documentUids.stream()
                  .map(PrescriptionUtils::extractPrescriptionIdFromDocumentUid)
                  .collect(Collectors.toSet());
            });
  }
}
