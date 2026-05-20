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

package de.gematik.test.ncp.glue.epeda;

import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.EPED_TESTACTOR_NAME;
import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.PRACTITIONER_DE_NAME;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;

import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.MultiPrescription;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UseErpFdv;
import de.gematik.test.ncp.screenplay.actions.SetAvailableMultiPrescriptions;
import de.gematik.test.ncp.screenplay.actions.SetAvailablePrescriptions;
import io.cucumber.java.DataTableType;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Wenn;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;

@Slf4j
@Getter
public class GermanPractice4EpedTeststeps {

  private final Testdata testdata = Testdata.instance();

  @DataTableType
  public Medication medicationEntryTransformer(final Map<String, String> entry) {
    return new Medication(
        entry.get("Name"),
        entry.get("PZN"),
        entry.get("Substitutionsverbot").equalsIgnoreCase("nein"));
  }

  @DataTableType
  public MultiPrescription multiPrescriptionTransformer(final Map<String, String> entry) {
    return new MultiPrescription(
        entry.get("Name"),
        entry.get("PZN"),
        entry.get("Rezeptanzahl") != null ? Integer.valueOf(entry.get("Rezeptanzahl")) : null,
        entry.get("Aktivindex") != null ? Integer.valueOf(entry.get("Aktivindex")) : null);
  }

  @SneakyThrows
  @Angenommen(
      "^für (?:den Versicherten|die Versicherte) (.+) (?:ist folgendes|sind folgende) in der EU einlösbare (?:E-Rezept|E-Rezepte) im Fachdienst abrufbar:$")
  public void preparePatientDataAndPrescriptions(
      final String patientName, final List<Medication> medications) {
    final var patient = enterPatient(patientName);
    final var testActor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    final var practitionerDe =
        OnStage.theActorCalled(PRACTITIONER_DE_NAME).whoCan(new TreatPatient(patient));

    givenThat(testActor)
        .wasAbleTo(
            SetAvailablePrescriptions.forPatient(patient)
                .usingMedicationList(medications)
                .andPrescriber(practitionerDe));

    OnStage.stage().shineSpotlightOn(patient.getName());
  }

  private Actor enterPatient(final String patientName) {
    final var patientData =
        testdata.patientsTestdata().findByName(PersonName.fromString(patientName));
    return OnStage.theActorCalled(patientName)
        .whoCan(ProvidePatientData.fromPatient(patientData))
        .whoCan(UseErpFdv.withDefaultConfiguration());
  }

  @Angenommen("^für die versicherte Person (.+) ist folgende Mehrfachverordnung abrufbar:$")
  public void preparePatientDataAndMvoPrescriptions(
      final String patientName, final List<MultiPrescription> medications) {
    final var patient = enterPatient(patientName);
    final var testActor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    final var practitionerDe =
        OnStage.theActorCalled(PRACTITIONER_DE_NAME).whoCan(new TreatPatient(patient));
    // TODO: Issue prescription needs to be adjusted

    // From configuration select any valid german, general practitioner
    // As german practitioner,
    // * store the multi prescription
    // * take care to activate the prescription part according to given index "Aktivindex"
    //   0 all prescription not active (activation date in future)
    //   1 first prescription is active, others are not yet active
    //   2 second prescription is active, first is dispensed/outdated, others are not yet active
    //   3 third prescription is active, first&second are dispensed/outdated, fourth is not yet
    // active
    //   4 fourth prescription is active, others are dispensed/outdated
    // * remember basic prescription data
    //   * id,
    //   * creation date and time,
    //   * textual description

    // Take care, that no other prescriptions are available for the active patient

    givenThat(testActor)
        .wasAbleTo(
            SetAvailableMultiPrescriptions.forPatient(patient)
                .usingMultiPrescriptions(medications)
                .andPrescriber(practitionerDe));

    OnStage.stage().shineSpotlightOn(patient.getName());
  }

  @Wenn("der ausstellende deutsche Arzt ein E-Rezept löscht")
  public void deleteOnePrescriptionOfPatient() {
    // delete the first available prescription of the patient
  }

  @Angenommen(
      "der ausstellende deutsche Arzt löscht alle offenen E-Rezepte der versicherten Person")
  @Wenn("der ausstellende deutsche Arzt alle offenen E-Rezepte der versicherten Person löscht")
  public void deleteAllPrescriptionsOfPatient() {
    // delete all presciptions for the insurant with LE-DE identity, that issued the prescription
    // before
    // Hint: deletion requires the accesscode from eprescription Service, that is being genereted
    // when the prescription(s) is/are being created by the german practitioner.
    // (accesscode in context of prescription service, not in EU context)
  }
}
