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

package de.gematik.test.ncp.glue.psa;

import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.andThat;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;

import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.ncpeh.NcpehProvider;
import de.gematik.test.ncp.screenplay.abilities.KnowCurrentPractitioner;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePerformanceReportData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePracticeData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UseDwh;
import de.gematik.test.ncp.screenplay.abilities.UseFdv;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import de.gematik.test.ncp.screenplay.actions.AuthorizeLeForEpaAccount;
import de.gematik.test.ncp.screenplay.questions.FindPatientWithKontoInAktensystem;
import de.gematik.test.ncp.screenplay.questions.GetSmbCard;
import de.gematik.test.ncp.screenplay.questions.IsAktenkontoActive;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Und;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
public class ActorsInitializationSteps {

  public static final String LE_DE_ACTOR_NAME = "Leistungserbringer DE";

  public static final String TEST_ACTOR_NAME = "Max Testermann";

  private static final String TAG_SEPARATOR = ":";
  private static final String CURRENT_TIMESTAMP_PLUS_ONE_HOUR_TIGER_CONFIGURATION_KEY =
      "currentTimestampPlusOneHour";
  private static final String FDV_RESPONSE_CURRENT_COUNTRY_TIGER_CONFIGURATION_KEY =
      "fdvResponseCurrentCountry";
  private static final int ONE_HOUR = 1;
  private static final String NCPEHRESPONSE_TAG_PREFIX = "@NCPEH-RESPONSE-";
  private static final String FDV_RESPONSE_TAG_NAME = "@FDV-RESPONSE";
  public static final String VALUE_SEPARATOR = ",";

  private static final String SCENARIO_NAME_TAG_PREFIX = "@TCID:";

  private final Testdata testdata = Testdata.instance();

  private Map<UCHeaders, String> ncpehMockControlRequestHeaders;
  private List<String> fdvMockRequestCriterions = List.of();

  @Before("not @PerfRep")
  public void setUp(final Scenario scenario) {
    // Set currentTimestampPlusOneHour in TigerGlobalConfiguration
    final var nowPlusOneHour = Instant.now().plus(ONE_HOUR, ChronoUnit.HOURS);
    TigerGlobalConfiguration.putValue(
        CURRENT_TIMESTAMP_PLUS_ONE_HOUR_TIGER_CONFIGURATION_KEY, nowPlusOneHour);

    ncpehMockControlRequestHeaders =
        scenario.getSourceTagNames().stream()
            .filter(tag -> tag.startsWith(NCPEHRESPONSE_TAG_PREFIX))
            .collect(
                Collectors.toMap(
                    ActorsInitializationSteps::getTagKey, ActorsInitializationSteps::getTagValue));

    fdvMockRequestCriterions =
        scenario.getSourceTagNames().stream()
            .filter(tag -> tag.startsWith(FDV_RESPONSE_TAG_NAME))
            .findFirst()
            .map(ActorsInitializationSteps::getTagValue)
            .map(r -> r.split(VALUE_SEPARATOR))
            .map(List::of)
            .orElse(List.of());

    final var scenarioNameTag =
        scenario.getSourceTagNames().stream()
            .filter(tag -> tag.startsWith(SCENARIO_NAME_TAG_PREFIX))
            .findFirst()
            .map(ActorsInitializationSteps::getTagValue)
            .orElse(null);
    NcpehProvider.getInstance().getNcpehContext().setScenarioName(scenarioNameTag);

    OnStage.setTheStage(Cast.ofStandardActors());

    // Create LE_DE actor
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    givenThat(leDeActor).whoCan(new UsePrimarySystem());
    final var smbCard = leDeActor.asksFor(GetSmbCard.instance());
    andThat(leDeActor)
        .whoCan(
            ProvidePracticeData.fromSmbCardAndPractitioners(smbCard, testdata.dePractitioners()));
  }

  @Before("@PerfRep")
  public void setUpPerformanceReportActors(final Scenario scenario) {
    // Set currentTimestampPlusOneHour in TigerGlobalConfiguration
    final var nowPlusOneHour = Instant.now().plus(ONE_HOUR, ChronoUnit.HOURS);
    TigerGlobalConfiguration.putValue(
        CURRENT_TIMESTAMP_PLUS_ONE_HOUR_TIGER_CONFIGURATION_KEY, nowPlusOneHour);

    ncpehMockControlRequestHeaders =
        scenario.getSourceTagNames().stream()
            .filter(tag -> tag.startsWith(NCPEHRESPONSE_TAG_PREFIX))
            .collect(
                Collectors.toMap(
                    ActorsInitializationSteps::getTagKey, ActorsInitializationSteps::getTagValue));

    final var scenarioNameTag =
        scenario.getSourceTagNames().stream()
            .filter(tag -> tag.startsWith(SCENARIO_NAME_TAG_PREFIX))
            .findFirst()
            .map(ActorsInitializationSteps::getTagValue)
            .orElse(null);
    NcpehProvider.getInstance().getNcpehContext().setScenarioName(scenarioNameTag);

    OnStage.setTheStage(Cast.ofStandardActors());
    // Create LE_DE actor
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME).whoCan(new UsePrimarySystem());
    final var smbCard = leDeActor.asksFor(GetSmbCard.instance());
    leDeActor.can(
        ProvidePracticeData.fromSmbCardAndPractitioners(smbCard, testdata.dePractitioners()));

    OnStage.theActorCalled(TEST_ACTOR_NAME)
        .whoCan(new ProvidePerformanceReportData())
        .whoCan(new UseDwh());
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
        .can(ProvidePatientData.fromPatient(this.testdata.patientsTestdata().findByName(name)))
        .can(new UseFdv(fdvMockRequestCriterions));

    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    andThat(leDeActor).can(new TreatPatient(patient));

    leDeActor.attemptsTo(Ensure.that(new IsAktenkontoActive()).isTrue());
    and(leDeActor).attemptsTo(AuthorizeLeForEpaAccount.getInstance());

    // set patient in the spotlight
    OnStage.stage().shineSpotlightOn(patientName);
  }

  // man könnte hier den Enum vom EU Country Code aus der Triggerschnittstelle nutzen
  // @Angenommen("^(?:der|die) Versicherte (.+) begibt sich in dem EU-Land (BE|BG|CZ|DK|EE|IE|EL|ES|
  // ...) in Behandlung$")
  // darf/sollte die Funktion dazu trotzdem als Parameter einfach String benutzen?
  @Und("^die versicherte Person begibt sich in dem EU-Land (.+) bei LE-EU (.+) in Behandlung$")
  @Und(
      "^die versicherte Person begibt sich in dem weiteren EU-Land (.+) bei LE-EU (.+) in Behandlung$")
  public void patientEntersTreatmentInEU(final String euCountry, final String leEuName) {
    // Add information to patient data, that he/she is in EU country xy
    // Prepare TRC Data for request to NCP B simulation (straight forward, treatment context ist
    // "TREATMENT")

    TigerGlobalConfiguration.putValue(
        FDV_RESPONSE_CURRENT_COUNTRY_TIGER_CONFIGURATION_KEY, euCountry);

    final var patient = OnStage.theActorInTheSpotlight();
    final var leEuActor = OnStage.theActorCalled(leEuName);

    patient.can(KnowCurrentPractitioner.with(leEuActor));

    leEuActor
        .can(
            ProvidePractitionerData.fromPractitionerAndCountry(
                this.testdata.euPractitioners().findByName(leEuName), euCountry))
        .can(new UseNcpeh(ncpehMockControlRequestHeaders))
        .can(new ProvidePatientAccessData())
        .can(new TreatPatient(patient));
  }

  @Angenommen(
      "^eine versicherte Person hat ein aktives ePA Konto im Aktensystem des Betreibers (IBM|Rise)$")
  public void getPatientConfigurationForAktensystemInstance(final String aktensystem) {
    // get a patient name from config, that is assigned to the given Aktensystem instance
    // retrieved patientName must contain Name, KVNR and birthdate

    final var patient = OnStage.aNewActor();
    final var patientData =
        patient.asksFor(
            FindPatientWithKontoInAktensystem.fromTestdataAndAktensystem(
                this.testdata, aktensystem));
    patient.assignName(patientData.name().toString());
    patient.can(ProvidePatientData.fromPatient(patientData));

    // For different Aktensystem instances, different FDV have to be used, implement in the future
    and(patient).can(new UseFdv(fdvMockRequestCriterions));

    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    leDeActor.can(new TreatPatient(patient));
    and(leDeActor).attemptsTo(Ensure.that(new IsAktenkontoActive()).isTrue());

    // set patient in the spotlight
    OnStage.stage().shineSpotlightOn(patient.getName());
  }

  @After
  public void tearDown() {
    log.debug("Tear down the stage");
    OnStage.drawTheCurtain();
  }

  private static String getTagValue(final String tag) {
    final String[] parts = tag.split(TAG_SEPARATOR);
    return parts.length > 1 ? parts[1] : "";
  }

  private static UCHeaders getTagKey(final String tag) {
    final String[] parts = tag.split(TAG_SEPARATOR);
    return parts.length > 0
        ? UCHeaders.valueOf(parts[0].substring(NCPEHRESPONSE_TAG_PREFIX.length()))
        : null;
  }
}
