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
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/**
 * Action performed by a patient to define if a given prescription should be available for filling
 * in the EU.
 */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class SetEuAvailability implements Performable {

  private final String id;
  private final boolean allow;

  @Step("{0} sets EU availability for the prescription with id #id to #allow")
  @Override
  public <T extends Actor> void performAs(final T actor) {
    var fdv = actor.usingAbilityTo(UseErpFdv.class).getClient();
    fdv.setEuRedeemable(id, allow)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "Failed to set EU availability for the prescription with id " + id));
  }

  /**
   * Make the prescription with the given id available for filling in the EU.
   *
   * @param id the prescription id
   * @return the action to be performed by the patient
   */
  public static SetEuAvailability allow(final String id) {
    return new SetEuAvailability(id, true);
  }

  /**
   * Prevents the prescription with the given id from being available for filling in the EU.
   *
   * @param id the prescription id
   * @return the action to be performed by the patient
   */
  public static SetEuAvailability disallow(final String id) {
    return new SetEuAvailability(id, false);
  }
}
