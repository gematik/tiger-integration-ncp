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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

@Slf4j
public class IsEpkaCanBeFoundInAktenkonto implements Question<Boolean> {

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var kvnr = patient.usingAbilityTo(ProvidePatientData.class).kvnr();
    final var ps = actor.usingAbilityTo(UsePrimarySystem.class);
    final var epkas = ps.findEpka(kvnr);
    log.debug("Found ePKAs for kvnr {}: {}", kvnr, String.join(", ", epkas));
    return !epkas.isEmpty();
  }
}
