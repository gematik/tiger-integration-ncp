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
import de.gematik.test.ncp.screenplay.abilities.ProvidePracticeData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@AllArgsConstructor
@Slf4j
public class AuthorizeLeForEpaAccount implements Performable {

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var smbCard = actor.usingAbilityTo(ProvidePracticeData.class).getSmbCard();
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var ps = actor.usingAbilityTo(UsePrimarySystem.class);
    try {
      ps.authorizeLeForKvnr(smbCard.telematikId(), patientData.kvnr());
    } catch (final RuntimeException e) {
      log.error(
          "Authorizing the Leistungserbringer for the Aktenkonto of {} failed",
          patientData.kvnr(),
          e);
      throw e;
    }
  }

  public static AuthorizeLeForEpaAccount getInstance() {
    return Instrumented.instanceOf(AuthorizeLeForEpaAccount.class).newInstance();
  }
}
