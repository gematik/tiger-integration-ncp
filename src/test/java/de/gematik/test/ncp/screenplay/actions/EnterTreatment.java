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

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.screenplay.abilities.KnowCurrentPractitioner;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/** Enter a treatment relationship with a practitioner. */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class EnterTreatment implements Performable {

  private final Actor practitioner;
  private final String euCountry;

  @Override
  @Step("{0} enters treatment with practitioner #practitioner in #euCountry")
  public <T extends Actor> void performAs(final T actor) {

    var countryCode = EuCountryCode.fromCountryName(euCountry);

    actor.usingAbilityTo(ProvidePatientData.class).setCurrentLocationCountry(countryCode);
    actor.can(KnowCurrentPractitioner.as(practitioner));

    practitioner.can(new TreatPatient(actor));
    practitioner.attemptsTo(CreateNewPatientRecord.forPatient(actor));
  }

  /**
   * Creates the action to enter a treatment relationship with a practitioner from another EU
   * country.
   *
   * @param practitioner the practitioner actor
   * @param country the practitioner's country
   * @return the {@link EnterTreatment} action
   */
  public static EnterTreatment withEuPractitioner(final Actor practitioner, final String country) {
    return new EnterTreatment(practitioner, country);
  }
}
