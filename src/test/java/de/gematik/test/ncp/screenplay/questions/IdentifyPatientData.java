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

import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Question to verify that a patient's actual demographic data and access code match the data
 * received from the NCPeH in response to an XCPD request.
 */
@Slf4j
public class IdentifyPatientData implements Question<Boolean> {

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);

    final var patientRecord =
        actor.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);
    final var identifyPatientData = patientRecord.getPatient();

    log.debug(
        "Patient data: {}, identify Patient Data {}", patientData, identifyPatientData.toString());
    log.debug(
        """
        \nAccess Code
        #############
        provided by patient: {}
        received from NCPeH: {}
        """,
        patientData.accessCode(),
        identifyPatientData.accessCode());
    return patientData.samePerson(identifyPatientData)
        && patientData.accessCode().equals(identifyPatientData.accessCode());
  }

  /**
   * Factory method to create an instance of this question. See {@link IdentifyPatientData} for more
   * information.
   *
   * @return a new instance of the question
   */
  public static Question<Boolean> matchesProvidedDataAndAccessCode() {
    return Question.about(
            "that the check for congruency between patient-provided and received data "
                + "and access codes returned a Boolean value")
        .answeredBy(new IdentifyPatientData());
  }
}
