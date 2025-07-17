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

import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@Slf4j
public class FindPatientSummaryData implements Performable {
  @Override
  public <T extends Actor> void performAs(final T actor) {
    log.debug("Perform FindPatientSummaryData action");
    final var practitionerData = actor.usingAbilityTo(ProvidePractitionerData.class);
    final var patientAccessData = actor.usingAbilityTo(ProvidePatientAccessData.class);

    final var ncpeh = actor.usingAbilityTo(UseNcpeh.class);
    final var patientSummaryDo =
        ncpeh.findPatientSummary(
            patientAccessData,
            practitionerData.profileName(),
            practitionerData.country(),
            ncpeh.getNcpehMockControlRequestHeaders().get(UCHeaders.UC2));

    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    patientData.setPsaMetadata(patientSummaryDo.ncpehFdResponseContent());
    log.info("Found document meta data: " + patientSummaryDo);
  }

  public static FindPatientSummaryData instance() {
    return Instrumented.instanceOf(FindPatientSummaryData.class).newInstance();
  }
}
