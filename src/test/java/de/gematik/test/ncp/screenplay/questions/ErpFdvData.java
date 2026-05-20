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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.ncp.erezept.impl.EuDispenseInformation;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum;
import de.gematik.test.ncp.screenplay.abilities.UseErpFdv;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.serenitybdd.screenplay.Question;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErpFdvData {

  public static Question<StatusEnum> statusOfPrescription(final String id) {
    return Question.about("the status of the prescription with id " + id)
        .answeredBy(
            actor -> {
              var fdv = actor.usingAbilityTo(UseErpFdv.class).getClient();
              return fdv.getPrescription(id).map(Prescription::getStatus).orElse(null);
            });
  }

  public static Question<Boolean> hasEuDispenseInformationForId(final String id) {
    return Question.about(
            "EU dispense information for the prescription with id %s is available via the Erp-FdV"
                .formatted(id))
        .answeredBy(
            actor -> {
              var fdv = actor.usingAbilityTo(UseErpFdv.class).getClient();
              return fdv.getMedicationDispenseInformation().stream()
                  .anyMatch(
                      info ->
                          info instanceof EuDispenseInformation
                              && id.equals(info.getPrescriptionId()));
            });
  }
}
