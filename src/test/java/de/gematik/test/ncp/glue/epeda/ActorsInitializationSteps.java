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

import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.ActorType;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import de.gematik.test.ncp.screenplay.abilities.ProvideActorData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.RememberAuthoredPrescriptions;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import de.gematik.test.ncp.screenplay.abilities.UseThePrimsysDoctorClient;
import de.gematik.test.ncp.screenplay.abilities.UseThePrimsysRestApi;
import de.gematik.test.ncp.screenplay.actions.AuthorizeEuCountry;
import de.gematik.test.ncp.screenplay.actions.EnterTreatment;
import de.gematik.test.ncp.screenplay.actions.patient.DeletePrescription;
import de.gematik.test.ncp.screenplay.questions.ActorsKnownToPrimsysRestApi;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;

/** This class is used to initialize the actors for the Epeda feature. */
@Slf4j
public class ActorsInitializationSteps {

  /**
   * Name of the test actor used for EPED tests who has management access rights to all relevant
   * systems
   */
  public static final String EPED_TESTACTOR_NAME = "Tarek Testmanager";

  /** Name of the German practitioner used in tests */
  public static final String PRACTITIONER_DE_NAME = "Adelheid Ulmenwald";

  /**
   * Configuration key for storing the current country as ISO 3166 alpha-2 code in Tiger
   * configuration
   */
  public static final String FDV_RESPONSE_CURRENT_COUNTRY_TIGER_CONFIG_KEY =
      "fdvResponseCurrentCountry";

  private static final String TAG_FILTER_EPED_NOPR =
      "@eHDSI-Szenario:ePeDA_PatIdentification "
          + "or @eHDSI-Szenario:ePeDA_FindDocuments "
          + "or @eHDSI-Szenario:ePeDA_RetrieveDocuments "
          + "or @eHDSI-Szenario:ePeDA_DispensePrescription";
  private static final String TAG_FILTER_EPED_PR = "@eHDSI-Szenario:ePeDA_PerfRep";
  private static final String TAG_FILTER_EPED_ALL =
      TAG_FILTER_EPED_NOPR + " or " + TAG_FILTER_EPED_PR;

  private static final String NCPEH_RESPONSE_TAG_PREFIX = "@NCPEH-RESPONSE-";
  private static final String TAG_SEPARATOR = ":";

  private final Testdata testdata = Testdata.instance();

  private Map<UCHeaders, String> ncpehMockControlRequestHeaders;

  private ActorDto practitionerDeDataCache;

  @Before(TAG_FILTER_EPED_NOPR)
  public void setUp(final Scenario scenario) {

    ncpehMockControlRequestHeaders =
        scenario.getSourceTagNames().stream()
            .filter(tag -> tag.startsWith(NCPEH_RESPONSE_TAG_PREFIX))
            .collect(
                Collectors.toMap(
                    tag -> getTagKey(tag, NCPEH_RESPONSE_TAG_PREFIX),
                    ActorsInitializationSteps::getTagValue));

    OnStage.setTheStage(Cast.ofStandardActors());

    // create test actor with access to primsys api
    var testActor =
        OnStage.theActorCalled(EPED_TESTACTOR_NAME)
            .whoCan(UseThePrimsysRestApi.withDefaultConfiguration())
            .whoCan(RememberAuthoredPrescriptions.ofOwnPatients());

    // find german general practitioner by name in primsys and create actor
    var practitionerDeData = getCachedPractitionerDeData(testActor);

    OnStage.theActorCalled(PRACTITIONER_DE_NAME)
        .whoCan(ProvideActorData.of(practitionerDeData))
        .whoCan(UseThePrimsysDoctorClient.withOwnIdentity());
  }

  @After(TAG_FILTER_EPED_ALL)
  public void tearDown() {
    var testActor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    Set<String> prescriptionIds =
        testActor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions().keySet();

    if (!prescriptionIds.isEmpty()) {
      log.info("Cleaning up {} prescriptions", prescriptionIds.size());
      var practitionerDe = OnStage.theActorCalled(PRACTITIONER_DE_NAME);
      var patient = practitionerDe.usingAbilityTo(TreatPatient.class).getPatient();
      prescriptionIds.forEach(id -> patient.attemptsTo(DeletePrescription.withId(id).silently()));
    }
    log.debug("Tear down the stage");
    OnStage.drawTheCurtain();
  }

  @Und(
      "^die versicherte Person begibt sich in dem EU-Land (.+) in eine Apotheke (?:zum Apotheker|zur Apothekerin) (.+)$")
  public void patientEntersTreatmentWithEuPharmacist(
      final String euCountry, final String euPharmacistName) {
    // to be clarified whether:
    // * euCountry name will be taken from profileName (and ignore parameter euCountry)
    //   in this case, parameter euCountry is only needed for flexible use of the teststep
    // * take euCountry name from parameter euCountry and countryname in Profile will not be needed

    final var patient = OnStage.theActorInTheSpotlight();
    final var euPharmacist = enterEuPharmacist(euPharmacistName, euCountry);

    and(patient).attemptsTo(EnterTreatment.withEuPractitioner(euPharmacist, euCountry));
    OnStage.stage().shineSpotlightOn(patient.getName());
  }

  private Actor enterEuPharmacist(final String euPharmacistName, final String euCountry) {
    final var euPractitionerData = testdata.euPractitioners().findByName(euPharmacistName);
    return OnStage.theActorCalled(euPharmacistName)
        .whoCan(ProvidePractitionerData.fromPractitionerAndCountry(euPractitionerData, euCountry))
        .whoCan(new ProvidePatientAccessData())
        .whoCan(ManagePatientRecords.startingWithNoExistingRecords())
        .whoCan(new UseNcpeh(ncpehMockControlRequestHeaders));
  }

  @Wenn(
      "die versicherte Person den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihre E-Rezepte berechtigt")
  @Und(
      "die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt")
  public void assignEpedAuthorizationForEuCountry() {
    final var patient = OnStage.theActorInTheSpotlight();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final EuCountryCode countryCode = patientData.getCurrentLocationCountry();

    givenThat(patient).wasAbleTo(AuthorizeEuCountry.forEped(countryCode.name()));
  }

  @Und(
      "^die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker (.+) übergeben$")
  @Und(
      "^die versicherte Person ihre KVNR und den Zugriffscode an den(?: neuen|) EU-Apotheker (.+) übergibt$")
  public void handoverKvnrAndZugriffscodeToEuPharmacist(final String euPharmacistName) {
    // Hint: in case that in previous test steps has been an EU pharmacist already on stage
    // (pharmacist #1),
    //  the assumption is, that in @insurantChangesToEuPharmacy the pharmacist #1 has been brought
    // 'off-stage'
    //  and its data has been deleted. If this is not possible, this test step probably has to be
    // split
    //  into two different test steps.

    // from configuration read data of given EU pharmacist name
    // bring EU pharmacist on stage
    // remember data of EU pharmacist
    // make patient data Zugriffscode and KVNR available to actor EU pharmacist
    final var patient = OnStage.theActorInTheSpotlight();
    final var country =
        patient.usingAbilityTo(ProvidePatientData.class).getCurrentLocationCountry().name();
    final var euPractitionerData = testdata.euPractitioners().findByName(euPharmacistName);

    OnStage.theActorCalled(euPharmacistName)
        .whoCan(ProvidePractitionerData.fromPractitionerAndCountry(euPractitionerData, country))
        .whoCan(new TreatPatient(patient))
        .can(new ProvidePatientAccessData())
        .whoCan(new UseNcpeh(ncpehMockControlRequestHeaders));
  }

  private static String getTagValue(final String tag) {
    final String[] parts = tag.split(TAG_SEPARATOR);
    return parts.length > 1 ? parts[1] : "";
  }

  private static UCHeaders getTagKey(final String tag, final String prefix) {
    final String[] parts = tag.split(TAG_SEPARATOR);
    return parts.length > 0 ? UCHeaders.valueOf(parts[0].substring(prefix.length())) : null;
  }

  private ActorDto getCachedPractitionerDeData(final Actor testActor) {
    // cache practitioner data to avoid multiple calls to primsys rest api
    if (practitionerDeDataCache == null) {
      practitionerDeDataCache =
          testActor
              .asksFor(
                  ActorsKnownToPrimsysRestApi.withRoleAndName(
                      ActorType.DOCTOR, PRACTITIONER_DE_NAME))
              .getFirst();
    }
    return practitionerDeDataCache;
  }
}
