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

package de.gematik.test.ncp.screenplay.questions.practitioner;

import de.gematik.test.ncp.data.record.PatientRecord;
import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/** Questions about the patient currently being treated by the actor. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CurrentPatient {

  /** Returns the patient currently being treated by the actor */
  public static Question<Actor> asActor() {
    return Question.about("the patient currently being treated by the actor")
        .answeredBy(actor -> actor.usingAbilityTo(TreatPatient.class).getPatient());
  }

  /** Returns the patient record of the current patient */
  public static Question<PatientRecord> patientRecord() {
    return Question.about("the patient record of the current patient")
        .answeredBy(
            actor -> {
              var patient = actor.asksFor(CurrentPatient.asActor());
              return actor.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);
            });
  }
}
