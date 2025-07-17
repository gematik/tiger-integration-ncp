/*
 * Copyright 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.UseFdv;
import de.gematik.test.ncp.screenplay.questions.AuthorizationIsActive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@Slf4j
@AllArgsConstructor
public class UnauthorizeEuCountry implements Performable {

  private final String country;

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    if (Boolean.TRUE.equals(actor.asksFor(AuthorizationIsActive.forCountry(country)))) {
      final var fdv = actor.usingAbilityTo(UseFdv.class);
      fdv.deleteEntitlementNcpeh(patientData.kvnr());
    }
    patientData.accessCode(null);
  }

  public static UnauthorizeEuCountry forCountry(final String country) {
    return Instrumented.instanceOf(UnauthorizeEuCountry.class).withProperties(country);
  }
}
