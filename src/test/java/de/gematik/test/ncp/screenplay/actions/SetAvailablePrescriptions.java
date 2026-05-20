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

import static de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum.IN_PROGRESS;
import static de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum.READY;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gematik.test.ncp.GeneralFactory;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.RememberAuthoredPrescriptions;
import de.gematik.test.ncp.screenplay.actions.patient.DeletePrescription;
import de.gematik.test.ncp.screenplay.actions.patient.SetEuAvailability;
import de.gematik.test.ncp.screenplay.questions.patient.ExistingPrescriptions;
import java.util.List;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/**
 * Action to ensure that a given patient has exactly the given prescriptions available. If other
 * prescriptions are present, they will be aborted first.
 */
@Slf4j
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class SetAvailablePrescriptions implements Performable {

  private final Actor patient;
  private final List<Medication> medications;
  private final Actor doctor;

  @Override
  @Step("{0} sets available prescriptions for #patient")
  public <T extends Actor> void performAs(final T actor) {
    // Make sure no other prescriptions are available for this patient
    List<Prescription> activePrescriptions =
        patient.asksFor(ExistingPrescriptions.withStatus(READY, IN_PROGRESS));

    if (!activePrescriptions.isEmpty()) {
      log.atInfo()
          .setMessage("Aborting {} active prescriptions.")
          .addArgument(activePrescriptions::size)
          .log();
      log.atDebug()
          .setMessage("Active prescriptions:\n{}")
          .addArgument(
              () -> {
                try {
                  return GeneralFactory.getObjectMapper().writeValueAsString(activePrescriptions);
                } catch (JsonProcessingException jpe) {
                  throw new IllegalArgumentException(
                      "Failed to serialize active prescriptions to JSON", jpe);
                }
              })
          .log();

      activePrescriptions.forEach(
          prescription ->
              patient.attemptsTo(DeletePrescription.withId(prescription.getPrescriptionId())));
    } else {
      log.info("No active prescriptions found");
    }

    doctor.attemptsTo(IssueMultiplePrescriptions.to(patient).withMedications(medications));
    patient.usingAbilityTo(ProvidePatientData.class).setEuRedeemablePrescriptions(medications);

    // make redeemable in EU via FdV
    Set<String> prescriptionIds =
        actor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions().keySet();

    prescriptionIds.forEach(id -> patient.attemptsTo(SetEuAvailability.allow(id)));
  }

  /**
   * Start building the action to set available prescriptions for a given patient
   *
   * @param patient the patient actor to set the prescriptions for
   * @return builder step to add medications and prescriber
   */
  public static Builder forPatient(final Actor patient) {
    return new Builder(patient);
  }

  public static final class Builder {
    private final Actor patient;

    private List<Medication> medications;

    private Builder(final Actor patient) {
      this.patient = patient;
    }

    /**
     * Define the list of medications to be made available as prescriptions
     *
     * @param medications list of medications to be made available
     * @return builder step to add prescriber
     */
    public Builder usingMedicationList(final List<Medication> medications) {
      this.medications = medications;
      return this;
    }

    /**
     * Final step to define the prescriber (doctor) of the prescriptions
     *
     * @param doctor the doctor actor who will issue the prescriptions
     * @return the complete action to be performed by an almighty test actor
     */
    public SetAvailablePrescriptions andPrescriber(final Actor doctor) {
      return new SetAvailablePrescriptions(patient, medications, doctor);
    }
  }
}
