/*
 * Copyright (c) 2024. gematik GmbH
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

package de.gematik.test.ncp.glue.psa;

import static net.serenitybdd.screenplay.GivenWhenThen.andThat;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;

import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UseFdv;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import de.gematik.test.ncp.screenplay.actions.AuthorizeLeForEpaAccount;
import de.gematik.test.ncp.screenplay.questions.FindPatientWithKontoInAktensystem;
import de.gematik.test.ncp.screenplay.questions.IsAktenkontoActive;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Und;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
public class ActorsInitializationSteps {

  public static final String LE_DE_ACTOR_NAME = "Leistungserbringer DE";

  private final Testdata testdata = Testdata.instance();

  @Before
  public void setUp() {
    OnStage.setTheStage(Cast.ofStandardActors());

    // Create LE_DE actor
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    givenThat(leDeActor).whoCan(new UsePrimarySystem());
  }

  // TODO Implement using "versicherte Person" as synonyme for patient Actor in the "spotLight"

  @Angenommen("^(?:der|die) Versicherte (.+) hat ein aktives ePA Konto$")
  public void getPatientConfiguration(final String patientName) {
    // take patient configuration from file fitting to patient name
    // must contain Name, KVNR and birthdate

    // Create Patient actor
    final var name = PersonName.fromString(patientName);
    final var patient = OnStage.theActorCalled(patientName);
    givenThat(patient)
        .whoCan(ProvidePatientData.fromPatient(this.testdata.patientsTestdata().findByName(name)))
        .whoCan(new UseFdv());

    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    andThat(leDeActor).whoCan(new TreatPatient(patient));

    andThat(leDeActor).attemptsTo(Ensure.that(new IsAktenkontoActive()).isTrue());

    andThat(leDeActor).attemptsTo(AuthorizeLeForEpaAccount.instance());

    // set patient in the spotlight
    OnStage.stage().shineSpotlightOn(patientName);
  }

  // man könnte hier den Enum vom EU Country Code aus der Triggerschnittstelle nutzen
  // @Angenommen("^(?:der|die) Versicherte (.+) begibt sich in dem EU-Land (BE|BG|CZ|DK|EE|IE|EL|ES|
  // ...) in Behandlung$")
  // darf/sollte die Funktion dazu trotzdem als Parameter einfach String benutzen?
  @Und("^die versicherte Person begibt sich in dem EU-Land (.+) bei LE-EU (.+) in Behandlung$")
  public void patientEntersTreatmentInEU(final String euCountry, final String leEuName) {
    // Add information to patient data, that he/she is in EU country xy
    // Prepare TRC Data for request to NCP B simulation (straight forward, treatment context ist
    // "TREATMENT")

    final var patient = OnStage.theActorInTheSpotlight();

    final var leEuActor = OnStage.theActorCalled(leEuName);
    givenThat(leEuActor)
        .whoCan(
            ProvidePractitionerData.fromPractitioner(
                this.testdata.euPractitioners().findByName(leEuName)))
        .whoCan(new UseNcpeh())
        .whoCan(new TreatPatient(patient));

    // TODO Which euCountry (from step oder from testdata.yml) has high priority?
  }

  @Angenommen(
      "^eine versicherte Person hat ein aktives ePA Konto im Aktensystem des Betreibers (IBM|Rise|xtension)$")
  public void getPatientConfigurationForAktensystemInstance(final String aktensystem) {
    // get a patient name from config, that is assigned to the given Aktensystem instance
    // retrieved patientName must contain Name, KVNR and birthdate

    final var patient = OnStage.aNewActor();
    final var patientData =
        patient.asksFor(
            FindPatientWithKontoInAktensystem.fromTestdataAndAktensystem(
                this.testdata, aktensystem));
    givenThat(patient).assignName(patientData.name().toString());
    andThat(patient).whoCan(ProvidePatientData.fromPatient(patientData));

    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    andThat(leDeActor).whoCan(new TreatPatient(patient));

    // set patient in the spotlight
    OnStage.stage().shineSpotlightOn(patient.getName());
  }

  @After
  public void tearDown() {
    log.debug("Tear down the stage");
    OnStage.drawTheCurtain();
  }
}
