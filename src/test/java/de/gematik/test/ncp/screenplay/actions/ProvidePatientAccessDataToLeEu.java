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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import java.util.Optional;
import lombok.AllArgsConstructor;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@AllArgsConstructor
public class ProvidePatientAccessDataToLeEu implements Performable {

  private final Actor leEuActor;
  private final String kvnr;

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    final var patientAccessData = leEuActor.usingAbilityTo(ProvidePatientAccessData.class);
    patientAccessData.setKvnr(Optional.ofNullable(kvnr).orElse(patientData.kvnr()));
    patientAccessData.setAccessCode(patientData.accessCode());
  }

  public static ProvidePatientAccessDataToLeEu withLeEu(final Actor leEuActor) {
    return Instrumented.instanceOf(ProvidePatientAccessDataToLeEu.class)
        .withProperties(leEuActor, null);
  }

  public static ProvidePatientAccessDataToLeEu withKvnrAndLeEu(
      final String kvnr, final Actor leEuActor) {
    return Instrumented.instanceOf(ProvidePatientAccessDataToLeEu.class)
        .withProperties(leEuActor, kvnr);
  }
}
