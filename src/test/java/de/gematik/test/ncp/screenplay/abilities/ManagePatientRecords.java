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

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.test.ncp.data.record.PatientRecord;
import java.util.HashMap;
import java.util.Map;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;

/** Ability to manage patient data records. */
public class ManagePatientRecords implements Ability {
  private final Map<Actor, PatientRecord> patientRecords = new HashMap<>();

  /**
   * Adds a patient record for the given actor.
   *
   * @param actor the patient actor to associate the record with
   * @param record the patient record to add
   */
  public void addRecord(final Actor actor, final PatientRecord record) {
    patientRecords.put(actor, record);
  }

  /**
   * Retrieves the patient record for the given actor.
   *
   * @param actor the patient actor whose record is to be retrieved
   * @return the patient record associated with the actor, or null if none exists
   */
  public PatientRecord getRecordFor(final Actor actor) {
    return patientRecords.get(actor);
  }

  /** Factory method to create the ability with no existing patient records. */
  public static ManagePatientRecords startingWithNoExistingRecords() {
    return new ManagePatientRecords();
  }
}
