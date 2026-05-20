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

import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.EPED_TESTACTOR_NAME;

import de.gematik.test.erezept.primsys.data.MedicationRequestDto;
import de.gematik.test.erezept.primsys.data.PatientDto;
import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.PznMedicationDto;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.RememberAuthoredPrescriptions;
import de.gematik.test.ncp.screenplay.abilities.UseThePrimsysDoctorClient;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class IssuePrescription implements Performable {

  private final String patientName;
  private final PrescribeRequestDto prescriptionRequest;

  @Override
  @Step("{0} attempts to issue a prescription to #patientName")
  public <T extends Actor> void performAs(final T actor) {
    final var client = actor.usingAbilityTo(UseThePrimsysDoctorClient.class).getClient();

    final var prescription = client.issuePrescription(prescriptionRequest);

    validatePrescription(actor, prescription);

    log.info(
        "issued prescription with id {} for {} (PZN: {}) to KVNR {}",
        prescription.getPrescriptionId(),
        prescription.getMedication().getName(),
        prescription.getMedication().getPzn(),
        prescription.getPatient().getKvnr());

    var testactor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    final var prescriptionArchive =
        testactor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions();
    prescriptionArchive.put(prescription.getPrescriptionId(), prescription);
  }

  private void validatePrescription(final Actor actor, final PrescriptionDto prescription) {
    actor.attemptsTo(
        Ensure.that(prescription.getPatient().getKvnr())
            .isEqualTo(prescriptionRequest.getPatient().getKvnr()),
        Ensure.that(prescription.getPatient().getFirstName())
            .isEqualTo(prescriptionRequest.getPatient().getFirstName()),
        Ensure.that(prescription.getPatient().getLastName())
            .isEqualTo(prescriptionRequest.getPatient().getLastName()),
        Ensure.that(prescription.getMedication().getType())
            .isEqualTo(prescriptionRequest.getMedication().getType()),
        Ensure.that(prescription.getMedication().getPzn())
            .isEqualTo(prescriptionRequest.getMedication().getPzn()),
        Ensure.that(prescription.getMedication().getName())
            .isEqualTo(prescriptionRequest.getMedication().getName()),
        Ensure.that(prescription.getMedicationRequest().isSubstitutionAllowed())
            .isEqualTo(prescriptionRequest.getMedicationRequest().isSubstitutionAllowed()));
  }

  public static Builder to(final Actor patient) {
    return new Builder(patient);
  }

  @RequiredArgsConstructor
  public static class Builder {
    private final Actor patient;

    public IssuePrescription withMedication(final Medication medication) {
      return new IssuePrescription(patient.getName(), buildRequest(patient, medication));
    }

    @SneakyThrows
    private PrescribeRequestDto buildRequest(final Actor patient, final Medication medication) {
      final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
      final var request =
          PrescribeRequestDto.forPatient(
                  PatientDto.withKvnr(patientData.kvnr())
                      .withFirstName(patientData.name().givenNames())
                      .withLastName(patientData.name().lastNames())
                      .build())
              .medication(
                  PznMedicationDto.medicine(medication.pzn(), medication.name()).asPrescribed())
              .medicationRequest(
                  MedicationRequestDto.medicationRequest()
                      .substitutionAllowed(medication.substitutionAllowed())
                      .build())
              .build();

      log.debug(
          "Prescription request: {}",
          TigerGlobalConfiguration.getObjectMapper().writeValueAsString(request));

      return request;
    }
  }
}
