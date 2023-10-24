/*
 * Copyright 2023 gematik GmbH
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

package de.gematik.test.ncp.screenplay;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * This is the base class, from where the chained glue code starts.<br>
 * This is not yet thread safe, as no need has yet arisen. If this should be thread safe, the Lombok
 * generated {@link AtomicReference}s ({@code @Getter(lazy = true)}) must be replaced by {@link
 * ThreadLocal} or something similar
 */
@Slf4j
@RequiredArgsConstructor
@Accessors(fluent = true)
public class Practice {

  private static final Map<PersonName, PatientActor> patients = new ConcurrentHashMap<>();

  private static final Map<String, LeiActor> practitioners = new ConcurrentHashMap<>();

  @Getter(lazy = true)
  private static final Practice germanPractice =
      new Practice().practitioner(GermanPracticeActions.lei());

  @Getter(lazy = true)
  private static final Practice euPractice =
      new Practice()
          .practitioner(
              practitioners.computeIfAbsent(
                  EuPracticeActions.defaultEuPractitioner().name(),
                  n -> new LeiActor(EuPracticeActions.defaultEuPractitioner())));

  /**
   * Get the practitioner of the german practice
   *
   * @return {@link LeiActor} practitioner of the german practice
   */
  public static LeiActor germanLei() {
    return germanPractice().practitioner();
  }

  /**
   * Get the current patient of the german practice
   *
   * @return {@link Patient} the current patient of the german practice
   */
  public static Patient patientFromGermany() {
    return germanPractice().currentPatient();
  }

  @Getter
  @Setter(value = AccessLevel.PROTECTED)
  private LeiActor practitioner;

  @Getter private PatientActor currentPatient;

  /**
   * Make the given patient the currently treated.
   *
   * @param patient {@link Patient} to treat
   * @return {@link PatientActor} object of the current patient
   */
  public PatientActor callPatient(Patient patient) {
    currentPatient =
        patients.computeIfAbsent(patient.name(), absentName -> PatientActor.ofPatient(patient));
    practitioner.patient(currentPatient);
    return currentPatient;
  }

  /**
   * Make a patient of the given name the currently treated.
   *
   * @param name {@link String} name of the patient
   * @return {@link PatientActor}
   */
  public PatientActor callWaitingPatientWithName(@NonNull String name) {
    return callWaitingPatientWithName(PersonName.fromString(name));
  }

  /**
   * Make a patient of the given name the currently treated.
   *
   * @param name {@link PersonName} name of the patient
   * @return {@link PatientActor}
   */
  public PatientActor callWaitingPatientWithName(@NonNull PersonName name) {
    return callPatient(Testdata.instance().patientsTestdata().findByName(name));
  }

  /**
   * Make a patient whose medical records are hosted in the given record system the currently
   * treated.
   *
   * @param aktensystem name of the record system
   * @return {@link PatientActor}
   */
  public PatientActor callWaitingPatientWithKontoInAktensystem(@NonNull String aktensystem) {
    var patient = findPatientWithKontoInAktensystem(aktensystem);
    return callPatient(patient);
  }

  /**
   * Set the practitioner of the practice, identifying it with the given name
   *
   * @param name name of the practitioner
   * @return {@link LeiActor} of the practitioner
   */
  public Practice ofPractitioner(@NonNull String name) {
    practitioner =
        practitioners.computeIfAbsent(
            name,
            absentName ->
                new LeiActor(Testdata.instance().euPractitioners().findByName(absentName)));
    return this;
  }

  /**
   * Set the country of the practice
   *
   * @param country country to set
   * @return {@link Practice} the practice
   */
  public Practice inCountry(String country) {
    practitioner.country(country);
    return this;
  }

  public String country() {
    return practitioner.country();
  }

  private Patient findPatientWithKontoInAktensystem(String aktensystem) {
    return Optional.ofNullable(Testdata.instance().recordSystems().get(aktensystem.toLowerCase()))
        .stream()
        .flatMap(Collection::stream)
        .map(kvnr -> Testdata.instance().patientsTestdata().findByKvnr(kvnr))
        .filter(Objects::nonNull)
        .findFirst()
        .orElseThrow(
            () ->
                new TigerConfigurationException(
                    "No patient found whose medical records are hosted by " + aktensystem));
  }
}
