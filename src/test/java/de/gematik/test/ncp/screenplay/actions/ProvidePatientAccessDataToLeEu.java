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

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import java.util.Optional;
import lombok.AllArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/**
 * Action to provide patient access data (KVNR, access code) to the EU practitioner (LE-EU).
 *
 * <p>The action retrieves the patient data from the actor performing the action and sets it in the
 * LE-EU actor's abilities.
 */
@AllArgsConstructor
public class ProvidePatientAccessDataToLeEu implements Performable {

  private final Actor leEuActor;
  private final String kvnr;

  @Override
  @Step("{0} provides kvnr and access code to #leEuActor")
  public <T extends Actor> void performAs(final T actor) {
    final var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    final var patientAccessData = leEuActor.usingAbilityTo(ProvidePatientAccessData.class);
    patientAccessData.setKvnr(Optional.ofNullable(kvnr).orElse(patientData.kvnr()));
    patientAccessData.setAccessCode(patientData.accessCode());
  }

  /**
   * Factory method to create the action with only the LE-EU actor. The KVNR will be taken from the
   * actor performing the action.
   *
   * @param leEuActor the EU practitioner actor
   * @return the action instance
   */
  public static ProvidePatientAccessDataToLeEu withLeEu(final Actor leEuActor) {
    return Instrumented.instanceOf(ProvidePatientAccessDataToLeEu.class)
        .withProperties(leEuActor, null);
  }

  /**
   * Factory method to create the action with the LE-EU actor and a given KVNR.
   *
   * @param kvnr the KVNR to provide
   * @param leEuActor the EU practitioner actor
   * @return the action instance
   */
  public static ProvidePatientAccessDataToLeEu withKvnrAndLeEu(
      final String kvnr, final Actor leEuActor) {
    return Instrumented.instanceOf(ProvidePatientAccessDataToLeEu.class)
        .withProperties(leEuActor, kvnr);
  }
}
