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
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import de.gematik.test.ncp.screenplay.actions.IdentifyPatient;
import de.gematik.test.ncp.screenplay.actions.MakeAccessCodeDefect;
import de.gematik.test.ncp.screenplay.actions.ProvidePatientAccessDataToLeEu;
import de.gematik.test.ncp.screenplay.actions.UnauthorizeEuCountry;
import de.gematik.test.ncp.screenplay.questions.AuthorizationIsActive;
import de.gematik.test.ncp.screenplay.questions.GetAcknowledgementDetailErrorcodeFromIdentifyPatientResponse;
import de.gematik.test.ncp.screenplay.questions.GetAcknowledgementDetailErrortextFromIdentifyPatientResponse;
import de.gematik.test.ncp.screenplay.questions.GetAcknowledgementDetailLocationtextFromIdentifyPatientResponse;
import de.gematik.test.ncp.screenplay.questions.GetPractitionerData;
import de.gematik.test.ncp.screenplay.questions.GetReasonEncodingFromIdentifyPatientResponse;
import de.gematik.test.ncp.screenplay.questions.IsIdentifyPatientDataCorrect;
import de.gematik.test.ncp.screenplay.questions.IsNoIdentifyPatientData;
import de.gematik.test.ncp.screenplay.questions.WhoIsTreatedPatient;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@CucumberOptions(monochrome = true)
public class LeEuPatientIdentificationTestSteps {

  @Wenn("die versicherte Person die Berechtigung des EU-Landes erneuert")
  public void reassignAuthorizationForEuCountry() {
    // write a new access right to the active EU country via ePA FdV, so that a new AccessCode is
    // being generated

    // ToDo: clarify: new AccessCode should not be remembered, so that old one would still be used
    // be LE-EU
  }

  @Und("^die versicherte Person dem EU-Land die (?:Zugriffsberechtigung|Befugnis) entzieht$")
  @Und(
      "^die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto NICHT (?:berechtigt|befugt)$")
  public void ensureNoAuthorizationForEuCountry() {
    // ensure, that there is no authorization in ePA Aktensystem for the current country of
    // treatment

    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var country = leEuActor.asksFor(new GetPractitionerData()).country();
    final var patient = leEuActor.asksFor(new WhoIsTreatedPatient());

    andThat(patient).attemptsTo(UnauthorizeEuCountry.forCountry(country));
    andThat(patient).attemptsTo(Ensure.that(AuthorizationIsActive.forCountry(country)).isFalse());
    // set the leEuActor in the spotlight again
    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Wenn("^die versicherte Person ihre KVNR und einen falschen AccessCode an den LE-EU übergibt$")
  public void handoverKvnrAndInvalidAccesscodeToLeEu() {
    // generate invalid accessCode
    //   * if available from FdV, then falsificate it (i.e. change the first or last character of
    // the string)
    //   * if NOT available from FdV, generate any code matching the accessCode pattern:
    // [A-Za-z0-9]{6}

    // bring actor LeEu on stage
    // make patient data including invalid accessCode and KVNR available to actor LeEu

    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var patient = leEuActor.asksFor(new WhoIsTreatedPatient());
    when(patient).attemptsTo(MakeAccessCodeDefect.instance());
    and(patient).attemptsTo(ProvidePatientAccessDataToLeEu.withLeEu(leEuActor));

    // set the leEuActor in the spotlight again
    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Wenn("^die versicherte Person die falsche KVNR (.+) und ihren AccessCode an den LE-EU übergibt$")
  public void handoverInvalidKvnrAndAccesscodeToLeEu(final String otherKvnr) {
    // bring actor LeEu on stage
    // make patient data including accessCode and invalid KVNR available to actor LeEu

    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var patient = leEuActor.asksFor(new WhoIsTreatedPatient());

    when(patient).attemptsTo(ProvidePatientAccessDataToLeEu.withKvnrAndLeEu(otherKvnr, leEuActor));

    // set the leEuActor in the spotlight again
    OnStage.stage().shineSpotlightOn(leEuActor.getName());
  }

  @Und(
      "^der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt$")
  @Wenn("der LE-EU erfolgreich die Patient Identification der versicherten Person durchführt")
  public void leEuSuccessfullyPerformsPatientIdentification() {
    // possible alternative: do nothing here, as both resulting assertions will be constructed in
    // subsequent steps by simulator anyhow.
    // But what would this alternative mean for NCPeH internally? Could it result in different
    // behaviour?

    final var leEuActor = OnStage.theActorInTheSpotlight();
    leEuActor.attemptsTo(
        IdentifyPatient.instance().then(Ensure.that(new IsIdentifyPatientDataCorrect()).isTrue()));
  }

  @Wenn("^der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft$")
  public void leEuStartsPatientIdentification() {
    // prepare call of NCPeH country B simulation
    // call NCPeH country B simulation with data from a patient, that has been called in a step
    // before. Get and store response.

    final var leEuActor = OnStage.theActorInTheSpotlight();
    when(leEuActor).attemptsTo(IdentifyPatient.instance());
  }

  @Dann(
      "^erhält der LE-EU als Antwort die KVNR mit dem AccessCode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem ePKA-Dokument der versicherten Person zurück$")
  public void checkNcpResponsePatientData() {
    // extract patient data from simulators response
    // validate received patient data against configured patients data: KVNR, Name(s), birthdate
    // https://polarion.int.gematik.de/polarion/redirect/project/Mainline_OPB1/wiki/Euro%20Vision/gemSpec_NCPeH_FD?selection=ML-131797

    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor).attemptsTo(Ensure.that(new IsIdentifyPatientDataCorrect()).isTrue());
  }

  @Dann("^erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück$")
  public void checkNcpXcpdResponseContainsNoPatientData() {
    // check, that no patient data is included in the XCPD-Response

    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor).attemptsTo(Ensure.that(new IsNoIdentifyPatientData()).isTrue());
  }

  @Und("^das Identifikationsergebnis enthält Reason Encoding (.+)$")
  public void checkNcpXcpdResponseReasonEncoding(final String expectedReasonEncoding) {
    // extract ReasonEncoding from received XCPD-Response
    // check, that received ReasonEncoding matches expectedReasonEncoding

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(
            Ensure.that(new GetReasonEncodingFromIdentifyPatientResponse())
                .contains(expectedReasonEncoding));
  }

  @Und("^das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode (.+)$")
  public void checkNcpXcpdResponseAcknowledgementDetailErrorcode(final String expectedErrorcode) {
    // extract acknowledgementDetail from received XCPD-Response
    // check, that received acknowledgementDetail informations matches the identifier
    // expectedAcknowledgementDetail
    // more information about the structure acknowledgementDetail please see
    // * eHDSI_XCPD_Profile#2.3.3 eResponse Message if No Patient ID was Discovered
    // * eHealth DSI Error Codes,
    //   https://webgate.ec.europa.eu/fpfis/wikis/display/EHDSI/Exception+Handling+in+MyHealth@EU

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(
            Ensure.that(new GetAcknowledgementDetailErrorcodeFromIdentifyPatientResponse())
                .contains(expectedErrorcode));
  }

  @Und("^das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext \"(.+)\"$")
  public void checkNcpXcpdResponseAcknowledgementDetailErrortext(final String expectedErrortext) {
    // extract acknowledgementDetail from received XCPD-Response
    // check, that received acknowledgementDetail informations matches the identifier
    // expectedAcknowledgementDetail
    // more information about the structure acknowledgementDetail please see
    // * eHDSI_XCPD_Profile#2.3.3 eResponse Message if No Patient ID was Discovered

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(
            Ensure.that(new GetAcknowledgementDetailErrortextFromIdentifyPatientResponse())
                .contains(expectedErrortext));
  }

  @Und("^das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext \"(.+)\"$")
  public void checkNcpXcpdResponseAcknowledgementDetailLocation(final String expectedLocationtext) {
    // extract acknowledgementDetail from received XCPD-Response
    // check, that received acknowledgementDetail informations matches the identifier
    // expectedAcknowledgementDetail
    // more information about the structure acknowledgementDetail please see
    // * eHDSI_XCPD_Profile#2.3.3 eResponse Message if No Patient ID was Discovered
    // * eHealth DSI Error Codes,
    //   https://webgate.ec.europa.eu/fpfis/wikis/display/EHDSI/Exception+Handling+in+MyHealth@EU

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(
            Ensure.that(new GetAcknowledgementDetailLocationtextFromIdentifyPatientResponse())
                .contains(expectedLocationtext));
  }

  @Wenn("die versicherte Person den LE-EU ohne Behandlung verlässt")
  public void patientLeavesEUpracticeWithoutTreatment() {
    // no need to do anything here except for moving the patient into the spotlight for the next
    // step
    final var leEuActor = OnStage.theActorInTheSpotlight();
    final var patient = leEuActor.asksFor(new WhoIsTreatedPatient());
    OnStage.stage().shineSpotlightOn(patient.getName());
  }
}
