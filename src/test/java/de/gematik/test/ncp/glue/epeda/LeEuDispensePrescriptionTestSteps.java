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

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;

import de.gematik.test.ncp.screenplay.actions.dispense.DispensePrescriptions;
import de.gematik.test.ncp.screenplay.questions.ErpFdvData;
import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import de.gematik.test.ncp.screenplay.questions.practitioner.DispensedPrescriptions;
import de.gematik.test.ncp.screenplay.questions.practitioner.RegistryResponseData;
import de.gematik.test.ncp.util.IheUtils;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import java.util.Set;
import net.serenitybdd.screenplay.actors.OnStage;

public class LeEuDispensePrescriptionTestSteps {

  @Wenn(
      "^der EU-Apotheker das Medikament des (?:abgerufenen |)Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet$")
  @Wenn(
      "der EU-Apotheker die Medikamente aller abgerufenen Rezepte an die versicherte Person abgibt und die zugehörigen Dispensierinformationen sendet")
  @Und("der Apotheker sendet die Dispensierinformation erneut an das Land A")
  public void pharmacistStartsDispensationOfPrescriptions() {
    // take prescriptions to dispense from the remembered, retrieved prescriptions
    // and initiate the dispense operation with these prescriptions
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());

    when(euPharmacist).attemptsTo(DispensePrescriptions.forAllAvailable(patientRecord));
  }

  @Wenn(
      "der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und für beide CDA-Level eine Dispensierinformation sendet")
  public void pharmacistStartsDispensationOfPrescriptionForBothCdaLevels() {
    // check entry condition for this step:
    //   one prescription was retrieved with both CDA Levels, if not, cancel teststep with error

    // send Dispense request with two Dispense-Information for same ePrescription by using
    // Prescription Id with extension for CDA Level 3 and one with extension for CDA Level 1
  }

  @Wenn(
      "der EU-Apotheker eine Dispensierung von E-Rezepten mit falscher Prüfziffer in der KVNR der TRC Assertion durchführt")
  public void dispensePrescriptionsWithTrcAssertionWithWrongCheckDigitInKvnr() {
    // take KVNR of patient and modify the number the way, that the last digit in the String
    // ("check digit") is being modified - increment by 1 or set to 0, if original digit is 9.
    // Perform the dispense operation with that special TRC Assertion.
  }

  @Wenn(
      "der EU-Apotheker die Rezepte der versicherten Person dispensiert und dabei der Zugriffscode in der TRC Assertion fehlt")
  public void dispensePrescriptionsWithoutAccesscodeInTrc() {
    // Perform the dispensation operation with empty accesscode in TRC (pipe symbol and six digits
    // are missing in patient identifier element in TRC)
  }

  @Wenn(
      "der EU-Apotheker die Rezepte der versicherten Person dispensiert und ihm das Recht für eDispensation in der IdA fehlt")
  public void dispensePrescriptionsWithMissingPermissionCode() {
    // prepare permissionCodes in identity assertion for eu pharmacist so that no rights for
    // eDispensation are present but other codes are available according to eHDSI_SAML_Profile#2.5:
    // - "PRD-006" is given (Patient Identification)
    // - "PRD-004" and "PRD-010" are given (ePrescription)
    // - "PPD-046" is missing (eDispensation)
    // perform the dispense operation with that special identity assertion
  }

  @Wenn(
      "^ein LE-EU mit der Rolle (.+) und ohne PermissionCodes die Rezepte der versicherten Person dispensiert")
  public void dispensePrescriptionsWithSpecialIdentityRole(final String roleName) {
    // prepare Identity Assertion as following:
    // * permission codes are not given
    // * for the urn:oasis:names:tc:xacml:2.0:subject:role the code according to the given parameter
    // is given:
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

    // perform the dispense operation with that special identity assertion
  }

  @Wenn(
      "der EU-Apotheker die Rezepte der versicherten Person dispensiert und der LE-Identifier in der TRC Assertion nicht zu dem in der Identity Assertion passt")
  public void findPrescriptionsWithPharmacistIdentifierStringDoesNotMatch() {
    // prepare the element value of 'NameId' of the pharmacist, so that they do not match between
    // Identity Assertion and TRC Assertion,
    // i.e. define NameId value from IdA and switch the last letter between lower and capital letter
    // for TRCs NameId value (change as less as possible compared to the IdA value)
    // References: [eHDSI_SAML_Profile#4.1 "Generic Structure of the Treatment Relationship
    //   Assertion"], element NameID
    // and https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#4.1.6
    //   TAB_NCPeH_TRC Assertion und Begleittext

    // perform the dispense operation with that special identity assertion
  }

  @Dann(
      "^erhält der EU-Apotheker eine Information zur erfolgreichen Verarbeitung der (?:Dispensierinformation|Dispensierinformationen) zurück$")
  @Dann(
      "verarbeitet der NCPeH-FD die Dispensierinformation erfolgreich und sendet eine Information zur erfolgreichen Verarbeitung an das Land B zurück")
  public void checkNcpXdrDispenseResponseSuccess() {
    // check success status of XDR Response
    /*
      Reference: "TUC_NCPeH_033: Cross-Enterprise Document Reliable Interchange Response für eD-A senden"
          https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.1.5.3
        Expected Response content:
        ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"
    */
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());
    var response =
        RegistryResponseData.fromNcpehResponse(patientRecord.getDispensePrescriptionsResponse());

    // RegistryErrorList has to be empty or contain only entries with severity "Warning"
    then(euPharmacist)
        .should(
            seeThat(
                response.errorCodes(),
                is(either(empty()).or(everyItem(equalTo(IheUtils.ERROR_SEVERITY_WARNING))))));
    then(euPharmacist).should(seeThat(response.status(), is(IheUtils.SUCCESS_RESPONSE)));
  }

  @Dann(
      "enthält das Ergebnis der Dispensieroperation eine Information zur erfolgreichen Verarbeitung der ersten Dispensierinformation zurück")
  public void checkNcpXdrDispenseResponseForOnePrescriptionOk() {
    // check, that XDR-Response contains success-Information for one ePrescription
    // with prescription Id of retrieved ePrescription (either as CDA L1 or L3)

    // check overall status of response:
    // RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:PartialSuccess"
  }

  /* calling scenario is in state Zurückgestellt
  @Wenn(
      "^der EU-Apotheker ([1-9][0-9]*) Minuten nach der Berechtigungserteilung die Dispensierung des ausgewählten Rezeptes startet$")
  public void pharmacistStartsDispensationAfterMinutesOfPermissionGrant(int minutes) {
    // step probably not needed
  } */

  @Und("^das Ergebnis für die Dispensierung der ePrescriptions enthält die Fehlermeldung für (.+)$")
  @Dann(
      "^enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für (.+)$")
  public void checkNcpXdrDispenseResponseErrorMessageContent(
      final String expectedXdrErrorSituation) {
    // Type String for expectedXdrErrorSituation could also become an enumeration

    /* Correlation between expected error situations and their expected error message content:
     * checking codeContext & location shall NOT be case-sensitive

       open Point: Handling of expected error content, when multiple documents shall be retrieved,
       we do not expect, that given sequence of documents from testinterface would be kept
       Working assumption: check the first entry in RegistryErrorList

       (Once data structures are coded, details about content should be removed, only Reference to
        specification should be kept here)
       Expectable error situations
     * 1. epedaDispenseMissingErpAccessrights
         Reference: "TUC_NCPeH_038: Dispensierdokumente an E-Rezept-FD übermitteln"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27268
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_NO_CONSENT"
         ** RegistryErrorList/RegistryError@codeContext="There is no valid access authorisation for
                         the requesting country in the german eDispensation service or the prescription was
                         not retrieved by a health professional before dispensing. Please ask the patient for
                         access authorisation or retrieve the prescription before dispensing."
            (attention: the codeContext text is slightly different to epedaSearchMissingErpAccessrights)
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The german eDispensation service did respond with
                         HTTP status code 403."
            (attention: the location text is slightly different to epedaSearchMissingErpAccessrights)

     * 2. epedaDispensePrescriptionsPartialNotFound
         Reference: "TUC_NCPeH_038: Dispensierdokumente an E-Rezept-FD übermitteln"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27241
         Expected error content:
         ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:PartialSuccess"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_ED_EPRESCRIPTION_NOT_IDENTIFIABLE"
         ** RegistryErrorList/RegistryError@codeContext="An ePrescription is not available anymore for the patient.
                         Affected ePrescription ID= " + Angabe der E-Rezept-ID aus dem an den E-Rezept-FD übersandten
                         Dispensierdokument
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The german eDispensation service did respond with HTTP status
                         code 404. Affected ePrescription ID= " + Angabe der E-Rezept-ID aus dem jeweiligen
                         Dispensierdokument, für das der NCPeH-FD eine Antwort vom E-Rezept-FD erhalten hat
         + Check, that the string-value "E-Rezept-ID" is equal to the expected unavailable prescription in codeContext
           and location

       * 3. epedaDispenseInvalidPatientIdentifier
         Reference: "TUC_NCPeH_031: Überprüfung des Cross-Enterprise Document Reliable Interchange Request für eD-A auf
           allgemeine Vorgaben", Afo A_27204-*
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27204
         Expected error content:
         ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:PartialSuccess"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_ED_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Please make sure that the health insurance identifier is given
                         and correct"
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="Health insurant number is missing or invalid."

       * 4. epedaDispenseInvalidCountryScenario
         Reference: "TUC_NCPeH_031: Überprüfung des Cross-Enterprise Document Reliable Interchange Request für eD-A auf
           allgemeine Vorgaben", Afo A_27203-01
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27203-01
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_ED_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="The german eDispensation service is not agreed with the
                         requesting country. Please contact your service provider or administrator."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The DN structure in the subject of the received TLS certificate
                         does not contain a valid EU country code or the addressed service is not agreed between the
                         requesting country and germany. Received country code= " + Wert der Variable tls_country
         + Check, that the string-value "tls_country" is equal to the code for the active country

       * 5. epedaDispensePrescriptionNotFound
         Reference: "TUC_NCPeH_038: Dispensierdokumente an E-Rezept-FD übermitteln", Tabelle
           "TAB_NCPeH_RegistryError_Dispensierdokumente_an_E-Rezept-FD_übermitteln"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27241
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_ED_EPRESCRIPTION_NOT_IDENTIFIABLE"
         ** RegistryErrorList/RegistryError@codeContext="An ePrescription is not available anymore for the patient.
                         Affected ePrescription ID=" + Angabe der E-Rezept-ID aus dem an den E-Rezept-FD
                         übersandten Dispensierdokument
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The german eDispensation service did respond with HTTP status
                         code 404. Affected ePrescription ID= " + Angabe der E-Rezept-ID aus dem jeweiligen
                         Dispensierdokument, für das der NCPeH-FD eine Antwort vom E-Rezept-FD erhalten hat
            (attention: the location text is slightly different to epedaSearchMissingErpAccessrights)
         + Check, that the string-value "E-Rezept-ID" is equal to the expected unavailable prescription in codeContext
           and location

       * 6. epedaDispenseInvalidAccesscode
         Reference: "TUC_NCPeH_031: Überprüfung des Cross-Enterprise Document Reliable Interchange Request für eD-A auf
           allgemeine Vorgaben", Afo A_27206
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27206
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_ED_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Access denied. The patients access code is missing or
                         malformed. Please ask the patient for access authorisation and the proper access code."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location=(empty or missing - no textual requirement defined)

       * 7. epedaDispenseIncompletePermissionCodes
         Reference: "TUC_NCPeH_031: Überprüfung des Cross-Enterprise Document Reliable Interchange Request für eD-A auf
           allgemeine Vorgaben", Afo A_26652
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_26652
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_ED_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Access denied. Please contact your service provider or
                         administrator and check the access rights for your health professional role in your country."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="Received permission codes=" + Wert der Variable ida_permissions.
         + Check, that the string-value "ida_permissions" match the list of given permission codes
           (ignore whitespaces between '=' and permission code list)

       * 8. epedaDispenseInvalidHpRoleCode
         Reference: "TUC_NCPeH_031: Überprüfung des Cross-Enterprise Document Reliable Interchange Request für eD-A auf
           allgemeine Vorgaben", Afo A_26699
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_26699
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_ED_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Access denied. The information provided about the role of the
                         health professional does not comply with German regulations."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="No permission codes received and the access cannot be granted for
                         the received practitioner role code in accordance with national regulations. Received
                         practitioner role code= " + Wert der Variable ida_practitioner_role_code.
         + Check, that the string-value "ida_practitioner_role_code" is equal to the given role code
    */

  }

  @Und(
      "^die versicherte Person kann im FdV die Dispensierinformationen über (?:das letzte dispensierte E-Rezept|die letzten dispensierten E-Rezepte) abrufen$")
  public void checkSelectedPrescriptionsAreDispensed() {
    // check, that each dispensed prescription is/are in german prescription service in state
    // 'completed' and has/have a dispense information available

    // for each dispensed prescription, do:
    // 1. as insurant check the prescription state, using FdV test driver interface in
    // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
    // -> /erp/testdriver/api/v1/prescription/{id}: operation get
    // expected: prescription status is 'completed'
    // 2. as insurant check the dispense information, using FdV test driver interface in
    // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
    // -> /erp/testdriver/api/v1/medicationdispense: operation get
    // and in FdV-Response search for information matching the prescriptionId
    // expected: dispense information for the prescription is available
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patient = euPharmacist.asksFor(CurrentPatient.asActor());
    Set<String> prescriptionIds = euPharmacist.asksFor(DispensedPrescriptions.prescriptionIds());

    for (var id : prescriptionIds) {
      //      TODO: uncomment when NCPeH simulation is available
      //      patient.should(seeThat(ErpFdvData.statusOfPrescription(id), equalTo(IN_PROGRESS)));
      patient.should(seeThat(ErpFdvData.hasEuDispenseInformationForId(id)));
    }
  }

  /* for an e-prescription it is not possible to have a state 'in-progress' AND a dispense information, therefore a
     check only for state 'in-progress' is sufficient. This step is already available in LeEuRetrievePrescriptionsTestSteps.java.
     The step here is not needed.
     TODO: Cleanup at the end of implementation of negative tests
    @Und("^(?:das Rezept wurde|die Rezepte wurden) im E-Rezept-Fachdienst nicht dispensiert$")
    @Und("^die versicherte Person sieht im FdV (?:das für die EU selektierte E-Rezept|die für die EU selektierten E-Rezepte) im Status in-progress$")
    public void checkSelectedPrescriptionsAreNotDispensed() {
      // check, that the selected prescription is/are in german prescription service not in state
      // 'completed', but still in 'in-progress' and has/have no dispense information available

      // for each dispensed prescription, do:
      // 1. as insurant check the prescription state, using FdV test driver interface in
      // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
      // -> /erp/testdriver/api/v1/prescription/{id}: operation get
      // expected: prescription status is 'in-progress'
      // 2. as insurant check the dispense information, using FdV test driver interface in
      // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
      // -> /erp/testdriver/api/v1/medicationdispense: operation get
      // and in FdV-Response search for information matching the prescriptionId
      // expected: dispense information for the prescription is not found
    }
  */

  @Wenn(
      "im Land B ein Problem mit dem Empfang des Dispensierergebnisses entsteht und der Apotheker keine information erhält")
  public void nothingToDo() {}
}
