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

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

// TODO: Clarify if needed
public class SignEpkaUsingConfiguration implements Performable {

  private final boolean asQES;
  private final String signatureAlgorithm;

  public SignEpkaUsingConfiguration(final boolean asQES, final String signatureAlgorithm) {
    this.asQES = asQES;
    this.signatureAlgorithm = signatureAlgorithm;
  }

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var ps = actor.usingAbilityTo(UsePrimarySystem.class);

    final var signedEpka = ps.signDocument(patientData.getEpka(), asQES, signatureAlgorithm);
    patientData.setSignedEpka(signedEpka);
  }

  public static SignEpkaUsingConfiguration fromAsQESAndSignatureAlgorithm(
      final boolean asQES, final String signatureAlgorithm) {
    return Instrumented.instanceOf(SignEpkaUsingConfiguration.class)
        .withProperties(asQES, signatureAlgorithm);
  }
}
