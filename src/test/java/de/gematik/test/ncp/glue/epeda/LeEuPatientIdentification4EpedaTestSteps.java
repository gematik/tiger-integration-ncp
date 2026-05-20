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
import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.ehdsi.valuesets.EhdsiXspaRole;
import de.gematik.test.ncp.screenplay.abilities.KnowCurrentPractitioner;
import de.gematik.test.ncp.screenplay.actions.IdentifyPatient;
import de.gematik.test.ncp.screenplay.actions.ProvidePatientAccessDataToLeEu;
import de.gematik.test.ncp.screenplay.actions.testactor.EnsureOnlyGivenScenarioConfiguredForCountry;
import de.gematik.test.ncp.screenplay.questions.IdentifyPatientData;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import java.util.Arrays;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
public class LeEuPatientIdentification4EpedaTestSteps {

  @Angenommen(
      "^das EU-Land (.+) ist für die Szenarien PS-A und ePeD-A freigeschaltet und konfiguriert$")
  public void ensureScenarioConfiguration4PsaAndEpeda(final String countryname) {
    // ATTENTION: manual step, Tiger WEB-GUI with options to choose

    // show message to confirm, whether the NCP and VZD configuration do allow the use of both EU
    // scenarios PS-A and ePeD-A for the given country
    // Options to choose (if possible with Tiger):
    // * "yes, the configuration is active" -> teststep status "success" and continue execution of
    // scenario
    // * "no, the configuration is not active" -> teststep status "error" and stop execution of
    // scenario
    // * "I don't know" -> teststep warning (better status "inconclusive") and continue execution of
    // scenario

    // attention: this step should be usable to prepare the generation of IdA for all UseCases
    // (Patient Identification, FindDocuments, RetrieveDocuments and Dispensation)

    // Question: is it possible to remember the first answer, so that subsequent scenarios with the
    // same question can reuse the first answer?
    // If yes: check, if the answer is available and if so, use this and skip GUI-based question

  }

  @Angenommen(
      "^das EU-Land (.+) ist nur für das Szenario (PS-A|ePeD-A) freigeschaltet und konfiguriert$")
  public void ensureScenarioConfiguration4PsaWithoutEpeda(
      final String countryname, final String scenarioName) {
    // ATTENTION: manual step, Tiger WEB-GUI with options to choose an answer

    // show message to confirm, whether the NCP and VZD configuration do allow the use of only one
    // specific EU scenario: either PS-A and *not* ePeD-A or ePeD-A and *not* PS-A for the given
    // country (given scenarioName defines, which configuration is expected to be active in NCPeH)
    // Options to choose:
    // * "Pass" -> It is certain that the given country is exclusively configured for the chosen
    //             scenario. Teststep status "success" and continue execution
    // * "Fail" -> The given country is possibly available for other scenarios than the one
    //             indicated. Teststep status "error" and stop execution

    // Question: is it possible to remember the first answer, so that subsequent scenarios with the
    // same question/data pair can reuse the first answer?
    // If yes: check, if the answer is available and if so, use this and skip GUI-based question
    // -> not yet implemented
    var testactor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);

    givenThat(testactor)
        .wasAbleTo(
            EnsureOnlyGivenScenarioConfiguredForCountry.withScenarioAndCountry(
                scenarioName, EuCountryCode.fromCountryName(countryname)));
  }

  @Und(
      "die versicherte Person hat irrtümlich den Zugriff dieses EU-Landes auf Patient Summary Daten berechtigt")
  public void patientGrantsAccessToPsaByFault() {
    // ensure, that E-Rezept-FdV does *not* list the active country as available EU-country
    // for the feature ePeD (FdV will check VZD-entries). Alternatively try to grant access
    // to ePeD for that country with the FdV, which must fail.
    // define any accesscode with valid format (6 alphanumeric digits) to be used in the coming test
    // steps

  }

  @Wenn(
      "die versicherte Person den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihre E-Rezepte erneut berechtigt")
  public void refreshEpedAuthorizationForEuCountry() {
    // set again access rights for EU country to E-Rezept service for active insurant
    // check, that a different access code has been generated during this action (compare with
    // old code from patient data)
    // error, if there is no old code available in the patient data

    // ToDo: it is unclear yet, whether the new code needs to be remembered as well, but it must be
    // ensured that the old code will be used in sub-sequent test steps (to provoke an error)
  }

  @Wenn(
      "^die versicherte Person ihre KVNR und den Zugriffscode an (?:den EU-Apotheker|die EU-Apothekerin) übergibt$")
  @Und(
      "^die versicherte Person hat ihre KVNR und den Zugriffscode an (?:den EU-Apotheker|die EU-Apothekerin) übergeben$")
  public void handoverKvnrAndAccesscodeToEuPharmacist() {
    final var patient = OnStage.theActorInTheSpotlight();
    final var euPharmacist =
        patient.usingAbilityTo(KnowCurrentPractitioner.class).getCurrentPractitioner();

    when(patient).wasAbleTo(ProvidePatientAccessDataToLeEu.withLeEu(euPharmacist));
    OnStage.stage().shineSpotlightOn(euPharmacist.getName());
  }

  @Und(
      "^der(?: neue|) EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt$")
  @Und(
      "^der(?: neue|) EU-Apotheker mit der versicherten Person erfolgreich die Patient Identification durchführt$")
  public void euPharmacistGetsSuccessfulPatientIdentification() {
    final var euPharmacist = OnStage.theActorInTheSpotlight();

    and(euPharmacist).wasAbleTo(IdentifyPatient.forEpEd());
    and(euPharmacist)
        .wasAbleTo(Ensure.that(IdentifyPatientData.matchesProvidedDataAndAccessCode()).isTrue());
  }

  @Und(
      "^der EU-Apotheker mit KVNR und (?:AccessCode|Zugriffscode) der versicherten Person die Patient Identifikation aufruft$")
  @Und("^der(?: neue|) EU-Apotheker für die versicherte Person die Patient Identification aufruft$")
  @Wenn("^die EU-Apothekerin für die versicherte Person die Patient Identification aufruft$")
  public void euPharmacistStartsPatientIdentification() {
    final var euPharmacist = OnStage.theActorInTheSpotlight();

    when(euPharmacist).attemptsTo(IdentifyPatient.forEpEd());
  }

  @Wenn("der EU-Apotheker die Patient Identification mit einem Zahlendreher in der KVNR aufruft")
  public void euPharmacistStartsPatientIdentificationWithTransposedDigitsInKvnr() {
    // Collect data for active pharmacist and given patients KVNR & access code
    // Modify the Kvnr the way, that 3rd and 4th digit are being exchanged.
    // Initiate the Patient Identification operation (XCPD) with that modified KVNR.
  }

  @Wenn("der EU-Apotheker die Patient Identification mit einem zu kurzen Zugriffscode aufruft")
  public void euPharmacistStartsPatientIdentificationWithShortAccesscode() {
    // collect data for active pharmacist and given patients KVNR & access code
    // modify given access code by cutting the last digit (length 5 digits instead of 6)
    // initiate the Patient Identification operation (XCPD), but use modified access code instead
    // of configured one
  }

  @Wenn(
      "^der EU-Apotheker ([1-9][0-9]*) Minuten nach der Berechtigungserteilung die Patient Identification für diese Person aufruft$")
  public void euPharmacistStartsPatientIdentificationAfterMinutesOfPermissionGrant(
      final int minutes) {
    // calculate time, when the given time is over
    // wait for that calculated time
    // initiate Patient Identification operation (XCPD) for the active patient and the
    // access code, which was given before
  }

  @Wenn(
      "die versicherte Person die Berechtigung des EU-Landes für den Zugriff auf E-Rezepte löscht")
  public void revokePatientsPermissionOfEuCountryForPrescriptions() {
    // revoke the permission of the Eu country using the FdV of the insurant
  }

  @Wenn(
      "der EU-Apotheker für die versicherte Person die Patient Identification aufruft und ihm das Recht für Patient Identification in der IdA fehlt")
  public void euPharmacistStartsPatientIdentificationWithMissingPermissionCode() {
    // ensure, that the permission codes in Identity Assertion for eu pharmacist
    // has no rights for patient identification but other codes are available according to
    // eHDSI_SAML_Profile#2.5:
    // - "PRD-006" is missing (Patient Identification)
    // - "PRD-004" and "PRD-010" are given (ePrescription)
    // - "PPD-046" is given (eDispensation)
    // perform the patient identification operation with that special Identity Assertion
  }

  @Wenn(
      "^ein LE-EU mit der Rolle (.+) und ohne PermissionCodes die Patient Identifikation aufruft$")
  public void euPharmacistStartsPatientIdentificationWithSpecialIdentityRole(
      final String roleName) {
    // ensure, that for next Patient Identification operation in the Identity Assertion the
    // permission codes are not given and that for the urn:oasis:names:tc:xacml:2.0:subject:role the
    // code according to the given parameter is given:
    // - "Pharmacists": 2262
    // - "Pharmaceutical technicians": 3213
    // - "Medical Doctors": 221
    // - "Dentists": 2261
    // - "Physiotherapists": 2264
    // - "Nursing professionals": 2221
    // Codes are defined according to eHDSI_SAML_Profile#2.3 "HP Identity Attributes"
    // Hint: "Pharmaceutical technicians" is a simplified version of "Pharmaceutical technicians and
    //       assistants" for easier readability.
    // further information on code system available, only based on FHIR and on different scenario
    // scopes:
    // https://build.fhir.org/ig/HL7/fhir-ips/ValueSet-healthcare-professional-roles-uv-ips.html
    // https://fhir.ehdsi.eu/laboratory/0.0.1/ValueSet-eHDSIHealthcareProfessionalRole.html

    // perform the patient identification operation with that special Identity Assertion
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var role =
        Arrays.stream(EhdsiXspaRole.values())
            .filter(value -> value.getDisplayName().toLowerCase().contains(roleName.toLowerCase()))
            .findFirst()
            .get();

    when(euPharmacist)
        .attemptsTo(
            IdentifyPatient.forEpEd()
                .withModifiedRequest(
                    req -> req.idaPermissions(Set.of()).idaStructuralRole(role.getCodeAsString())));
  }

  @Wenn(
      "^der EU-Apotheker die Patient Identification mit CountryCode für (.+) im Signerzertifikat der IdA aufruft$")
  public void doPatientIdentificationWithIdaSignerCertFrom(final String signerCountryCode) {
    // perform a Patient Identification request, where country code in subject of TLS certificate
    // does not match the country code in subject of identity assertion signer certificate

    // The country B simulator has to take the TLS certificate according to the parameter
    // EuCountryCode on the test interface.
    // Provoke the mismatch at the test interface by setting the IdAAssertionProfile name according
    // to the parameter signerCountryCode.
  }

  @Dann(
      "^erhält (?:der EU-Apotheker|die EU-Apothekerin) als Antwort die KVNR mit dem Zugriffscode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem E-Rezept der versicherten Person zurück$")
  public void checkPatientData4Pharmacist() {
    // extract patient data from simulators response
    // validate received patient data against configured patient's data: KVNR concatenated with "|"
    // and access code, Name(s), birthdate
    // https://polarion.int.gematik.de/polarion/redirect/project/FB_NCPeH_FD_2_0/wiki/Spezifikation/gemSpec_NCPeH_FD?selection=FBNCPeH-263
    final var euPharmacist = OnStage.theActorInTheSpotlight();
    then(euPharmacist)
        .attemptsTo(Ensure.that(IdentifyPatientData.matchesProvidedDataAndAccessCode()).isTrue());
  }

  @Dann("erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück")
  public void checkNcpXcpdResponse4PharmacistContainsNoPatientData() {
    // check, that no patient data is included in the XCPD-Response

    // ToDo: check. if this glue function can be realised by
    //       checkNcpXcpdResponseContainsNoPatientData@LeEuPatientIdentificationTestSteps.java
  }

  @Und("^das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für (.+)$")
  public void checkNcpXcpdResponseErrorMessageContent(final String expectedXcpdErrorSituation) {
    // Type String for expectedXcpdErrorSituation could also become an enumeration

    // Tasks:
    // * verify presence of acknowledgementDetail and triggerFor data structures in XCPD response
    // * verify error message content matches the given expected error situation
    // * as described below / specified in gemSpec_NCPeH-FD

    /* Correlation between expected error situations and their expected error message content:
     * checking acknowledgementDetail.Text & acknowledgementDetail.Location shall NOT be case-sensitive
       (Once data structures are coded, details about content should be removed, only Reference to
        specification should be kept here)
       Expectable error situations
     * 1. epedaIdentifyPatientNotFound
         Reference: "TUC_NCPeH_035: Demographische Versichertendaten aus E-Rezept extrahieren"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27933
           TAB_NCPeH_Fehlerfälle_Abruf_von_demographischen_Versichertendaten, HTTP Status Code 404
         Expected error content:
         * Reason Encoding:
         ** mitigatedBy@typeCode="MITGT"
         ** mitigatedBy...code@code="AnswerNotAvailable"
         ** mitigatedBy...code@codeSystem="1.3.6.1.4.1.19376.1.2.27.3"
         * Acknowledgement:
         ** acknowledgement.typeCode= AA
         ** queryAck.queryResponseCode= NF
         ** acknowledgementDetail.Code= ERROR_PI_NO_MATCH
         ** acknowledgementDetail.Text = "Patient Identification Error"
         ** acknowledgementDetail.Location = "No match with an existing patient."

     * 2. epedaIdentifyInvalidPatientIdentifier
         Reference: "TUC_NCPeH_023: Patient Demographics Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881
           TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A, "Die Variable xcpd_kvnr DARF NICHT leer sein."
         Expected error content:
         * Reason Encoding:
         ** triggerFor@typeCode="TRIG" (!)
         ** triggerFor...code@code="DemographicsQueryNotAllowed"
         ** triggerFor...code@codeSystem="1.3.6.1.4.1.12559.11.10.1.3.2.2.1"
         * Acknowledgement:
         ** acknowledgement.typeCode= AA
         ** queryAck.queryResponseCode= AE
         ** acknowledgementDetail.Code= ERROR_PI_GENERIC
         ** acknowledgementDetail.Text = "Patient Identification error"
         ** acknowledgementDetail.Location = "Please make sure the health insurant number is correct."

     * 3. epedaIdentifyMissingErpAccessrights
         Reference: "TUC_NCPeH_035: Demographische Versichertendaten aus E-Rezept extrahieren"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27933
           TAB_NCPeH_Fehlerfälle_Abruf_von_demographischen_Versichertendaten, HTTP Status Code 403
         Expected error content:
         * Reason Encoding:
         ** mitigatedBy@typeCode="MITGT"
         ** mitigatedBy...code@code="InsufficientRights"
         ** mitigatedBy...code@codeSystem="1.3.6.1.4.1.12559.11.10.1.3.2.2.1"
         * Acknowledgement:
         ** acknowledgement.typeCode= AA
         ** queryAck.queryResponseCode= AE
         ** acknowledgementDetail.Code= ERROR_PI_GENERIC
         ** acknowledgementDetail.Text = "Patient Identification Error"
         ** acknowledgementDetail.Location = "The requestor has insufficient rights to query for
                          patient’s identity data. Please ask the patient for access authorisation."

     * 4. epedaIdentifyIncompletePermissionCodes
         Reference: "TUC_NCPeH_023: Patient Demographics Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881
           TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A
         Expected error content:
         * Reason Encoding:
         ** mitigatedBy@typeCode="MITGT"
         ** mitigatedBy...code@code="InsufficientRights"
         ** mitigatedBy...code@codeSystem="1.3.6.1.4.1.12559.11.10.1.3.2.2.1"
         * Acknowledgement:
         ** acknowledgement.typeCode= AA
         ** queryAck.queryResponseCode= AE
         ** acknowledgementDetail.Code= ERROR_PI_GENERIC
         ** acknowledgementDetail.Text = "Patient Identification Error"
         ** acknowledgementDetail.Location = "Access is not permitted. Please check the access rights
                          for your health professional role in your country."

     * 5. epedaIdentifyInvalidHpRoleCode
         Reference: "TUC_NCPeH_023: Patient Demographics Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881
           TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A
         Expected error content:
         * Reason Encoding:
         ** mitigatedBy@typeCode="MITGT"
         ** mitigatedBy...code@code="InsufficientRights"
         ** mitigatedBy...code@codeSystem="1.3.6.1.4.1.12559.11.10.1.3.2.2.1"
         * Acknowledgement:
         ** acknowledgement.typeCode= AA
         ** queryAck.queryResponseCode= AE
         ** acknowledgementDetail.Code= ERROR_PI_GENERIC
         ** acknowledgementDetail.Text = "Patient Identification Error"
         ** acknowledgementDetail.Location = "No permission codes received and the access cannot be
                          granted for the received practitioner role code in accordance with
                          national regulations. Received practitioner role code=" + Wert der
                          Variable ida_practitioner_role_code
         + Check, that the string-value "Wert der Variable ida_practitioner_role_code" is equal to
           given, invalid practitioner role code

      * 6. epedaIdentifyInvalidCountryScenario
         Reference: "TUC_NCPeH_023: Patient Demographics Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881 ,
           TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A
         Expected error content:
         * Reason Encoding:
         ** mitigatedBy@typeCode="MITGT"
         ** mitigatedBy...code@code="PolicyViolation"
         ** mitigatedBy...code@codeSystem="1.3.6.1.4.1.12559.11.10.1.3.2.2.1"
         * Acknowledgement:
         ** acknowledgement.typeCode= AA
         ** queryAck.queryResponseCode= AE
         ** acknowledgementDetail.Code= ERROR_PI_GENERIC
         ** acknowledgementDetail.Text = "The german patient identification service is not agreed
                          with the requesting country. Please contact your service provider or
                          administrator."
         ** acknowledgementDetail.Location = "The requesting country is not approved to access the
                          patient identification service. Received country code= " + Wert der
                          Variable tls_country
         + Check, that the string-value "Wert der Variable tls_country" is equal to the current
           country code assigned to the patient

      * 7. epedaIdentifyInvalidAccessCode
         Reference: "TUC_NCPeH_023: Patient Demographics Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881 ,
           AB_NCPeH_XCPD_Prüfkriterien_ePeD-A und TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A
         Expected error content:
         * Reason Encoding:
         ** mitigatedBy@typeCode="MITGT"
         ** mitigatedBy...code@code="PatientAuthenticationRequired"
         ** mitigatedBy...code@codeSystem="1.3.6.1.4.1.12559.11.10.1.3.2.2.1"
         * Acknowledgement:
         ** acknowledgement.typeCode= AA
         ** queryAck.queryResponseCode= AE
         ** acknowledgementDetail.Code= ERROR_PI_GENERIC
         ** acknowledgementDetail.Text = "Patient Identification Error"
         ** acknowledgementDetail.Location = "A respective access code has not been transmitted or has not been
         *                transmitted properly. Please ask the patient for an access authorisation."

    */
  }
}
