/*
 * Copyright 2026 gematik GmbH
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

package de.gematik.test.ncp.screenplay.questions.patient;

import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum;
import de.gematik.test.ncp.screenplay.abilities.UseErpFdv;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import net.serenitybdd.screenplay.Question;

public class ExistingPrescriptions {

  public static Question<List<Prescription>> withStatus(final StatusEnum... statusCodes) {
    return Question.about(
            "the patient's existing prescriptions with status corresponding to "
                + Arrays.toString(statusCodes))
        .answeredBy(
            patient -> {
              var fdv = patient.usingAbilityTo(UseErpFdv.class).getClient();
              return fdv.getPrescriptions(List.of(statusCodes))
                  .orElseThrow(
                      () ->
                          new NoSuchElementException(
                              "Failed to retrieve prescriptions for status codes: "
                                  + Arrays.toString(statusCodes)));
            });
  }
}
