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
import de.gematik.test.ncp.glue.UseCaseScope;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.UseErpFdv;
import de.gematik.test.ncp.screenplay.abilities.UseFdv;
import lombok.NoArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/**
 * Represents an action to authorize access to healthcare data for a specific EU country via the
 * patient's personal client. This can be done for either the Patient Summary or ePrescription /
 * eDispensation scope.
 */
@NoArgsConstructor(force = true)
public class AuthorizeEuCountry implements Performable {

  private final String country;
  private final UseCaseScope scope;

  public AuthorizeEuCountry(final String country, final UseCaseScope scope) {
    if (country == null || country.isBlank()) {
      throw new IllegalArgumentException("Country must not be null or blank");
    }
    this.country = country;
    this.scope = scope;
  }

  @Override
  @Step("{0} authorizes #country for health data access with scope #scope")
  public <T extends Actor> void performAs(final T actor) {
    switch (scope) {
      case EPEDA -> authorizeEped(actor);
      case PSA -> authorizePsa(actor);
      default -> throw new IllegalArgumentException("Unexpected scope: " + scope);
    }
  }

  private <T extends Actor> void authorizePsa(final T actor) {
    var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    var fdv = actor.usingAbilityTo(UseFdv.class);
    var response = fdv.authorizeEuCountry(patientData.kvnr(), country).orElseThrow();
    patientData.accessCode(response.getAccesscode());
  }

  private <T extends Actor> void authorizeEped(final T actor) {
    var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    var erpFdvClient = actor.usingAbilityTo(UseErpFdv.class).getClient();
    erpFdvClient
        .getEuDispensationConsent()
        .or(erpFdvClient::createEuDispensationConsent)
        .orElseThrow(
            () -> new IllegalStateException("Failed to create consent for EU dispensation"));
    var euAccessAuthorization =
        erpFdvClient
            .grantEuAccessPermission(EuCountryCode.fromCountryName(country).getCountryCode(), null)
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "Failed to authorize access to ePrescriptions for country " + country));
    patientData.accessCode(euAccessAuthorization.getAccessCode());
  }

  /**
   * Factory method to create an AuthorizeEuCountry action for the Patient Summary scope.
   *
   * @param country The country to authorize.
   * @return An instance of AuthorizeEuCountry with {@linkplain UseCaseScope#PSA}.
   */
  public static AuthorizeEuCountry forPatientSummary(final String country) {
    return new AuthorizeEuCountry(country, UseCaseScope.PSA);
  }

  /**
   * Factory method to create an AuthorizeEuCountry action for the ePrescription / eDispensation
   * scope.
   *
   * @param country The country to authorize.
   * @return An instance of AuthorizeEuCountry with {@linkplain UseCaseScope#EPEDA}.
   */
  public static AuthorizeEuCountry forEped(final String country) {
    return new AuthorizeEuCountry(country, UseCaseScope.EPEDA);
  }
}
