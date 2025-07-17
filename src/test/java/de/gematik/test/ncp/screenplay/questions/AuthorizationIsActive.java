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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.ncp.gen.eu.fdv.model.GetEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.UseFdv;
import java.time.Instant;
import java.util.Date;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class AuthorizationIsActive implements Question<Boolean> {

  private final String country;

  public AuthorizationIsActive(final String country) {
    this.country = country;
  }

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    final var fdv = actor.usingAbilityTo(UseFdv.class);

    return fdv.getEntitlementNcpeh(patientData.kvnr())
        .map(GetEntitlementNcpehResponseDTO::getEntitlement)
        .filter(
            entitlement ->
                country.equalsIgnoreCase(entitlement.getCountryName())
                    && Date.from(Instant.now()).before(entitlement.getValidTo()))
        .isPresent();
  }

  public static AuthorizationIsActive forCountry(final String country) {
    return new AuthorizationIsActive(country);
  }
}
