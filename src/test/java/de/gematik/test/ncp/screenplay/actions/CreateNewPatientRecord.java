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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.data.record.PatientRecord;
import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/** Action to create a new patient record for a given patient. */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class CreateNewPatientRecord implements Performable {

  private final Actor patient;

  @Override
  @Step("{0} creates a new patient record for #patient")
  public <A extends Actor> void performAs(final A actor) {
    actor.usingAbilityTo(ManagePatientRecords.class).addRecord(patient, new PatientRecord());
  }

  /**
   * Factory method to create a new {@link CreateNewPatientRecord} action for a specific patient.
   *
   * @param patient the actor representing the patient for whom the record is to be created
   * @return a new instance of {@link CreateNewPatientRecord} for the specified patient
   */
  public static CreateNewPatientRecord forPatient(final Actor patient) {
    return new CreateNewPatientRecord(patient);
  }
}
