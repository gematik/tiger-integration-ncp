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

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

public class StoreEpkaInAktenkonto implements Performable {
  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);

    final var epka =
        patientData.getSignedEpka() != null ? patientData.getSignedEpka() : patientData.getEpka();
    if (epka == null) {
      throw new IllegalStateException("No ePKA document to store in Aktenkonto");
    }

    final var ps = actor.usingAbilityTo(UsePrimarySystem.class);
    final var documentId = ps.putDocument(patientData.kvnr(), epka);
    patientData.setEpkaAktenkontoId(documentId);
    ps.addKvnrToCleanEpka(patientData.kvnr());
  }

  public static StoreEpkaInAktenkonto instance() {
    return Instrumented.instanceOf(StoreEpkaInAktenkonto.class).newInstance();
  }
}
