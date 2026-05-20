/*
 * Copyright 2026 gematik GmbH
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

package de.gematik.test.ncp.screenplay.actions.patient;

import de.gematik.test.ncp.screenplay.abilities.UseErpFdv;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.markers.CanBeSilent;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/** Action performed by a patient to delete a prescription via the E-prescription personal client */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class DeletePrescription implements Performable, CanBeSilent {

  private final String prescriptionId;
  private boolean silent;

  @Step(
      "{0} deletes the prescription with id '#prescriptionId' using the E-prescription personal client")
  @Override
  public <T extends Actor> void performAs(final T actor) {
    var fdv = actor.usingAbilityTo(UseErpFdv.class).getClient();
    var success = fdv.deletePrescription(prescriptionId);
    if (!success) {
      throw new RuntimeException("Failed to delete the prescription with id " + prescriptionId);
    }
  }

  /**
   * Delete the prescription with the given id via the E-prescription personal client
   *
   * @param prescriptionId id of the prescription to delete
   * @return the performable to delete the prescription
   */
  public static DeletePrescription withId(final String prescriptionId) {
    return new DeletePrescription(prescriptionId);
  }

  /**
   * Prevent this task from appearing in the report.
   *
   * @return a silenced version of this task
   */
  public DeletePrescription silently() {
    silent = true;
    return this;
  }

  @Override
  public boolean isSilent() {
    return silent;
  }
}
