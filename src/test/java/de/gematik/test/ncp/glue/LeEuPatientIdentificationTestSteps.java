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

package de.gematik.test.ncp.glue;

import static de.gematik.test.ncp.screenplay.EuPracticeActions.*;
import static de.gematik.test.ncp.screenplay.PatientActions.*;
import static de.gematik.test.ncp.screenplay.Practice.*;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import io.cucumber.junit.CucumberOptions;

@CucumberOptions(monochrome = true)
public class LeEuPatientIdentificationTestSteps {

  // man könnte hier den Enum vom EU Country Code aus der Triggerschnittstelle nutzen
  // @Angenommen("^(?:der|die) Versicherte (.+) begibt sich in dem EU-Land (BE|BG|CZ|DK|EE|IE|EL|ES|
  // ...) in Behandlung$")
  // darf/sollte die Funktion dazu trotzdem als Parameter einfach String benutzen?
  @Angenommen(
      "^die versicherte Person begibt sich in dem EU-Land (.+) bei LE-EU (.+) in Behandlung$")
  public void patientEntersTreatmentInEU(String euCountry, String leEuName) {
    // Add information to patient data, that he/she is in EU country xy
    // Prepare TRC Data for request to NCP B simulation (straight forward, treatment context ist
    // "TREATMENT")
    euPractice().inCountry(euCountry).ofPractitioner(leEuName).callPatient(patientFromGermany());
  }

  @Und(
      "^die versicherte Person mit dem Konto im Aktensystem (IBM|Rise|xtension) begibt sich in dem EU-Land (.+) in Behandlung$")
  public void getPatientAndPrepareSimpleTrcAssertionData(String aktensystem, String euCountry) {
    // get a patient name from config, that is assigned to the given Aktensystem instance

    // Add information to patient data, that he/she is in EU country xy
    // Prepare TRC Data for request to NCP B simulation (straight forward, treatment context ist
    // "TREATMENT")
    euPractice().inCountry(euCountry).callWaitingPatientWithKontoInAktensystem(aktensystem);
  }

  // TODO: Diese Funktion ist eigentlich ein PatientTestStep -> move & adapt?
  @Und(
      "^die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt$")
  public void assignAuthorizationForEuCountry() {
    euPractice()
        .currentPatient()
        .verifyThat(euCountryIsAuthorizedForAktenkonto(euPractice().country()));
  }

  // TODO: Diese Funktion ist eigentlich ein PatientTestStep -> move & adapt?
  // TODO: Klären, ob die Möglichkeit einer zeitlichen Steuerung vom Team Patient zu Verfügung
  // gestellt wird
  @Und(
      "^der NCPeH Fachdienst ist für den Zugriff dieses EU-Landes auf das Konto der versicherten Person noch für mindestens ([1-9][0-9]*) Minuten berechtigt$")
  public void assignAuthorizationForEuCountryWithTimeLimit(int minutes) {
    // check and if necessary grant fresh access rights to the ePA account of the active patient for
    // the active EU-country
    // take into account, that a fresh access right will last fix 1h
  }

  @Wenn("die versicherte Person die Berechtigung des EU-Landes erneuert")
  public void reassignAuthorizationForEuCountry() {
    // write a new access right to the active EU country via ePA FdV, so that a new AccessCode is
    // being generated

    // ToDo: clarify: new AccessCode should not be remembered, so that old one would still be used
    // be LE-EU
  }

  // TODO: Diese Funktion ist eigentlich ein PatientTestStep -> move & adapt?
  @Und("die versicherte Person dem EU-Land die Zugriffsberechtigung entzieht")
  @Und(
      "^die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto NICHT berechtigt$")
  public void ensureNoAuthorizationForEuCountry() {
    // ensure, that there is no authorization in ePA Aktensystem for the current country of
    // treatment
    // euPractice()
    //    .currentPatient()
    //    .verifyThat(leistungserbringerIsAuthorizedForAktenkonto(euPractice().practitioner()));
  }

  @Und("^die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben$")
  public void handoverKvnrAndAccesscodeToLeEu() {
    // make patient data including accessCode and KVNR available to actor LeEu
    euPractice().currentPatient().presentAuthorizationDataTo(euPractice().practitioner());
  }

  @Wenn("^die versicherte Person ihre KVNR und einen falschen AccessCode an den LE-EU übergibt$")
  public void handoverKvnrAndInvalidAccesscodeToLeEu() {
    // ToDo: generate invalid accessCode
    //   * if available from FdV, then falsificate it (i.e. change the first or last character of
    // the string)
    //   * if NOT available from FdV, generate any code matching the accessCode pattern:
    // [A-Za-z0-9]{6}

    // bring actor LeEu on stage
    // euPractice()
    // make patient data including invalid accessCode and KVNR available to actor LeEu
    // .practitioner().treat(euPractice().currentPatient());
  }

  @Wenn("^die versicherte Person die falsche KVNR (.+) und ihren AccessCode an den LE-EU übergibt$")
  public void handoverInvalidKvnrAndAccesscodeToLeEu(String otherKvnr) {
    // ToDo: get Accesscode for initial, valid KVNR

    // bring actor LeEu on stage
    // euPractice()
    // make patient data including accessCode and invalid KVNR available to actor LeEu
    // .practitioner().treat(euPractice().currentPatient());
  }

  @Und(
      "^der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt$")
  public void leEuGetsSuccessfulPatientIdentification() {
    // possible alternative: do nothing here, as both resulting assertions will be constructed in
    // subsequent steps by simulator anyhow.
    // But what would this alternative mean for NCPeH internally? Could it result in different
    // behaviour?
    euPractice()
        .practitioner()
        .attemptsTo(retrievePatientIdentificationData())
        .acknowledgeThat(patientDataAreCorrect());
  }

  @Wenn("^der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft$")
  public void leEuStartsPatientIdentification() {
    // prepare call of NCPeH country B simulation
    // call NCPeH country B simulation with data from a patient, that has been called in a step
    // before. Get and store response.
    euPractice().practitioner().attemptsTo(retrievePatientIdentificationData());
  }

  @Dann(
      "^erhält der LE-EU als Antwort die KVNR mit dem AccessCode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem ePKA-Dokument der versicherten Person zurück$")
  public void checkNcpResponsePatientData() {
    // extract patient data from simulators response
    // validate received patient data against configured patients data: KVNR, Name(s), birthdate
    // https://polarion.int.gematik.de/polarion/redirect/project/Mainline_OPB1/wiki/Euro%20Vision/gemSpec_NCPeH_FD?selection=ML-131797
    euPractice().practitioner().acknowledgeThat(patientDataAreCorrect());
  }

  @Dann("^erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück$")
  public void checkNcpXcpdResponseContainsNoPatientData() {
    // check, that no patient data is included in the XCPD-Response
    euPractice().practitioner().acknowledgeThat(patientDataAreNotPresent());
  }

  @Und("^das Identifikationsergebnis enthält Reason Encoding (.+)$")
  public void checkNcpXcpdResponseReasonEncoding(String expectedReasonEncoding) {
    // extract ReasonEncoding from received XCPD-Response
    // check, that received ReasonEncoding matches expectedReasonEncoding
    euPractice().practitioner().acknowledgeThat(reasonEncodingIs(expectedReasonEncoding));
  }

  @Und("^das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode (.+)$")
  public void checkNcpXcpdResponseAcknowledgementDetailErrorcode(String expectedErrorcode) {
    // extract acknowledgementDetail from received XCPD-Response
    // check, that received acknowledgementDetail informations matches the identifier
    // expectedAcknowledgementDetail
    // more informations about the structure acknowledgementDetail please see
    // * eHDSI_XCPD_Profile#2.3.3 eResponse Message if No Patient ID was Discovered
    // * eHDSI_Components_Specification#6.4 eHealth DSI Error Codes, Table 1

  }

  @Und("^das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext \"(.+)\"$")
  public void checkNcpXcpdResponseAcknowledgementDetailLocation(String expectedLocationtext) {
    // extract acknowledgementDetail from received XCPD-Response
    // check, that received acknowledgementDetail informations matches the identifier
    // expectedAcknowledgementDetail
    // more informations about the structure acknowledgementDetail please see
    // * eHDSI_XCPD_Profile#2.3.3 eResponse Message if No Patient ID was Discovered
    // * eHDSI_Components_Specification#6.4 eHealth DSI Error Codes, Table 1

  }
}
