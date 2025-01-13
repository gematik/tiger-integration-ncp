/*
 * Copyright (c) 2024-2025 gematik GmbH
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

import static de.gematik.test.ncp.glue.psa.ActorsInitializationSteps.LE_DE_ACTOR_NAME;
import static net.serenitybdd.screenplay.GivenWhenThen.andThat;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.screenplay.actions.AuthorizeEuCountry;
import de.gematik.test.ncp.screenplay.actions.CreateEpka;
import de.gematik.test.ncp.screenplay.actions.DeleteEpkaInAktenkonto;
import de.gematik.test.ncp.screenplay.actions.MakeSignEpkaDefect;
import de.gematik.test.ncp.screenplay.actions.ProvidePatientAccessDataToLeEu;
import de.gematik.test.ncp.screenplay.actions.SignEpka;
import de.gematik.test.ncp.screenplay.actions.StoreEpkaInAktenkonto;
import de.gematik.test.ncp.screenplay.questions.GetPractitionerData;
import de.gematik.test.ncp.screenplay.questions.IsAuthorizeEuCountry;
import de.gematik.test.ncp.screenplay.questions.IsEpkaCanBeFoundInAktenkonto;
import de.gematik.test.ncp.screenplay.questions.WhoIsTreatedPatient;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import io.cucumber.junit.CucumberOptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
@CucumberOptions(monochrome = true)
@Getter
public class GermanPracticeTestSteps {

  private static final String DEFECT_011_EPKA_TEMPLATE = "defect011";
  private static final String DEFECT_012_EPKA_TEMPLATE = "defect012";
  private static final String DEFECT_020_EPKA_TEMPLATE = "defect020";
  private final Testdata testdata = Testdata.instance();

  @Und(
      "^die versicherte Person hat den NCPeH Fachdienst für den Zugriff (?:dieses|des) EU-Landes auf ihr ePA Konto (?:berechtigt|befugt)$")
  public void patientAssignsAccessRightsForEuCountry() {
    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var country = leEuActor.asksFor(new GetPractitionerData()).country();
    final var patient = leEuActor.asksFor(new WhoIsTreatedPatient());
    andThat(patient).attemptsTo(AuthorizeEuCountry.forCountry(country));
    andThat(patient).attemptsTo(Ensure.that(IsAuthorizeEuCountry.forCountry(country)).isTrue());
    // set the leEuActor in the spotlight again
    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Und("^die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben$")
  public void handoverKvnrAndAccesscodeToLeEu() {
    // make patient data including accessCode and KVNR available to actor LeEu
    // see: @Und("^die versicherte Person begibt sich in dem EU-Land (.+) bei LE-EU (.+) in
    // Behandlung$")

    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var patient = leEuActor.asksFor(new WhoIsTreatedPatient());

    andThat(patient).attemptsTo(ProvidePatientAccessDataToLeEu.withLeEu(leEuActor));

    // set the leEuActor in the spotlight again
    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Angenommen("^(?:der|die) Versicherte (.+) hat ein ePA Aktenkonto im Status (.+)$")
  public void getPatientConfigurationWithEpaAccountState(
      final String patientName, final String epaAccountState) {
    // not to be implemented, as teststep is fully manual and only occures during connectathon.
  }

  @Und(
      "^ein LE-DE hat ein ePKA-Dokument in das ePA-Konto der versicherten Person eingestellt, in dem Name und Geburtsdatum der versicherten Person enthalten ist$")
  @Und(
      "^in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist$")
  public void prepareAndPutEpkaDocument() {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE identity
    // store constructed and signed ePKA document in the ePA account of the patient

    final var actorInTheSpotlight = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    andThat(leDeActor)
        .attemptsTo(
            CreateEpka.fromTestdata(testdata)
                .then(SignEpka.instance())
                .then(StoreEpkaInAktenkonto.instance()));

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isTrue());

    OnStage.stage().shineSpotlightOn(actorInTheSpotlight.getName());
  }

  @Wenn("ein LE-DE das ePKA Dokument der versicherten Person überschreibt")
  public void searchAndOverwriteEpkaDocument() {
    // ensure access rights for the LE-DE
    // search for the document
    // write a new ePKA document (can even be exactly the same - just ensure a new DocumentUniqueId
    // being is generated)

    final var actorInTheSpotlight = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    when(leDeActor).attemptsTo(StoreEpkaInAktenkonto.instance());

    OnStage.stage().shineSpotlightOn(actorInTheSpotlight.getName());
  }

  @Wenn("von einem LE-DE das ePKA Dokument der versicherten Person gelöscht wird")
  @Angenommen("in dem ePA Konto der versicherten Person ist kein ePKA Dokument vorhanden")
  public void searchAndDeleteEpkaDocument() {

    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    andThat(leDeActor).attemptsTo(DeleteEpkaInAktenkonto.instance());
    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isFalse());

    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Und("das ePKA Dokument der versicherten Person ist nicht konform zum FHIR Schema des ePKA MIO")
  @Wenn(
      "das ePKA Dokument der versicherten Person von einem LE-DE ersetzt wird, dass nicht konform zum FHIR Schema des ePKA MIO ist")
  public void prepareSchemaDefectiveEpkaDocumentAndPutDocument() {
    // take data from patient being on stage
    // fill a schema-defect ePKA document template (missing mandatory element gender) with KVNR,
    // name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE identity
    // store constructed and signed ePKA document in the ePA account of the patient

    final var actorInTheSpotlight = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    andThat(leDeActor)
        .attemptsTo(
            CreateEpka.fromTestdata(testdata, DEFECT_020_EPKA_TEMPLATE)
                .then(SignEpka.instance())
                .then(StoreEpkaInAktenkonto.instance()));

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isTrue());

    OnStage.stage().shineSpotlightOn(actorInTheSpotlight.getName());
  }

  @Und(
      "ein LE-DE hat ein ePKA-Dokument mit einer mathematisch ungültigen Signatur in das ePA-Konto der versicherten Person eingestellt")
  public void prepareSignatureDefectiveEpkaDocumentAndPutDocument() {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE identity
    // manipulate the signature of the ePKA document
    // store constructed and signed ePKA document in the ePA account of the patient

    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isFalse());
    andThat(leDeActor)
        .attemptsTo(
            CreateEpka.fromTestdata(this.testdata)
                .then(SignEpka.instance())
                .then(MakeSignEpkaDefect.instance())
                .then(StoreEpkaInAktenkonto.instance()));

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isTrue());

    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Und(
      "ein LE-DE hat ein ePKA-Dokument ohne Signatur in das ePA-Konto der versicherten Person eingestellt")
  public void prepareSignatureMissingEpkaDocumentAndPutDocument() {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // store constructed and NOT SIGNED ePKA document in the ePA account of the patient

    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isFalse());
    andThat(leDeActor)
        .attemptsTo(CreateEpka.fromTestdata(this.testdata).then(StoreEpkaInAktenkonto.instance()));

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isTrue());

    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Wenn(
      "das ePKA Dokument der versicherten Person von einem LE-DE ersetzt wird, in dem keine NFD Composition enthalten ist")
  @Und(
      "^in dem Konto befindet sich ein valides ePKA-Dokument, in dem nur eine DPE Composition enthalten ist$")
  public void prepareAndPutEpkaDocumentWithoutNfdComposition() {
    // take data from patient being on stage
    // fill a ePKA document template
    // - with KVNR, name and birthdate of the patient in the DPE composition
    // - without NFD composition
    // !!! sign the constructed ePKA document with an LE-DE identity
    // store constructed and signed ePKA document in the ePA account of the patient

    final var actorInTheSpotlight = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    andThat(leDeActor)
        .attemptsTo(
            CreateEpka.fromTestdata(testdata, DEFECT_011_EPKA_TEMPLATE)
                .then(SignEpka.instance())
                .then(StoreEpkaInAktenkonto.instance()));

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isTrue());

    OnStage.stage().shineSpotlightOn(actorInTheSpotlight.getName());
  }

  @Angenommen(
      "in dem Konto befindet sich ein valides ePKA-Dokument, in dem das Geburtsdatum der versicherten Person fehlt")
  public void prepareAndPutEpkaDocumentWithBirthdateAbsentReason() {
    // take data from patient being on stage
    // fill a ePKA document template with KVNR and name of the patient
    // >> the special prepared template shall not have the element
    //    KBV_PR_MIO_NFD_Patient_NFD:Patient.birthDate.value and instead contain
    //    - as part of the template - within birthdate the extension data-absent-reason.
    // see https://simplifier.net/packages/kbv.mio.patientenkurzakte/1.0.0/files/611117/~details
    //   "Patient.birthDate.extension:data-absent-reason"
    // signature of the document is not mandatory
    // store constructed ePKA document in the ePA account of the patient

    final var actorInTheSpotlight = OnStage.theActorInTheSpotlight();
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);

    andThat(leDeActor)
        .attemptsTo(
            CreateEpka.fromTestdata(testdata, DEFECT_012_EPKA_TEMPLATE)
                .then(SignEpka.instance())
                .then(StoreEpkaInAktenkonto.instance()));

    andThat(leDeActor).attemptsTo(Ensure.that(new IsEpkaCanBeFoundInAktenkonto()).isTrue());

    OnStage.stage().shineSpotlightOn(actorInTheSpotlight.getName());
  }
}
