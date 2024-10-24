/*
 * Copyright (c) 2024. gematik GmbH
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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.UseFdv;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class IsAuthorizeEuCountry implements Question<Boolean> {

  private final String country;

  public IsAuthorizeEuCountry(final String country) {
    this.country = country;
  }

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    final var fdv = actor.usingAbilityTo(UseFdv.class);

    final var ac = fdv.authorizeEuCountry(patientData.kvnr(), country);
    patientData.accessCode(ac);
    return ac != null && !ac.isBlank() ? Boolean.TRUE : Boolean.FALSE;
  }

  public static IsAuthorizeEuCountry forCountry(final String country) {
    return new IsAuthorizeEuCountry(country);
  }
}
