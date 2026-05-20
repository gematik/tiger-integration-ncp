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

import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.MultiPrescription;
import java.util.List;
import java.util.Optional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/** Action to prepare available prescriptions from MVO definitions. */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class SetAvailableMultiPrescriptions implements Performable {

  private final Actor patient;
  private final List<MultiPrescription> multiPrescriptions;
  private final Actor doctor;

  @Override
  @Step("{0} sets available MVO prescriptions for #patient")
  public <T extends Actor> void performAs(final T actor) {
    final var mvoDefinitions =
        Optional.ofNullable(multiPrescriptions).map(List::copyOf).orElseGet(List::of);
    final var activeMedications = determineActiveMedications(mvoDefinitions);

    actor.attemptsTo(
        SetAvailablePrescriptions.forPatient(patient)
            .usingMedicationList(activeMedications)
            .andPrescriber(doctor));
  }

  private static List<Medication> determineActiveMedications(
      final List<MultiPrescription> multiPrescriptions) {
    if (multiPrescriptions.isEmpty()) {
      return List.of();
    }
    return multiPrescriptions.stream()
        .filter(multi -> multi.index() != null && multi.index() > 0)
        .map(multi -> new Medication(multi.name(), multi.pzn(), true))
        .toList();
  }

  public static Builder forPatient(final Actor patient) {
    return new Builder(patient);
  }

  public static final class Builder {
    private final Actor patient;
    private List<MultiPrescription> multiPrescriptions;

    private Builder(final Actor patient) {
      this.patient = patient;
    }

    public Builder usingMultiPrescriptions(final List<MultiPrescription> multiPrescriptions) {
      this.multiPrescriptions = multiPrescriptions;
      return this;
    }

    public SetAvailableMultiPrescriptions andPrescriber(final Actor doctor) {
      return new SetAvailableMultiPrescriptions(patient, multiPrescriptions, doctor);
    }
  }
}
