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
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static org.hamcrest.Matchers.hasSize;

import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.actions.retrieve_documents.RetrieveAndVerifyPrescriptions;
import de.gematik.test.ncp.screenplay.actions.retrieve_documents.RetrievePrescriptions;
import de.gematik.test.ncp.screenplay.actions.testactor.VerifyRetrievedCda1Prescriptions;
import de.gematik.test.ncp.screenplay.actions.testactor.VerifyRetrievedCda3Prescriptions;
import de.gematik.test.ncp.screenplay.questions.ErpFdvData;
import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import de.gematik.test.ncp.screenplay.questions.xca.find_documents.FindDocumentsResponseData;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.RetrieveDocumentsResponseData;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.RetrievedDocuments;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.RetrievedPrescriptions;
import de.gematik.test.ncp.util.IheUtils;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.OnStage;
import org.hamcrest.Matchers;

@Slf4j
public class LeEuRetrievePrescriptionsTestSteps {

  @Und(
      "^der EU-Apotheker hat erfolgreich den Abruf (?:des Rezeptes|der Rezepte) als CDA Level ([1,3]) durchgeführt$")
  public void successfulRetrieveOfPrescriptionsWithCdaLevel(final Integer cdaLevel) {
    // complete execution of RetrieveDocuments for structured document type
    // - get the related prescriptionIds and needed data to form the RetrieveDocuments request
    // - perform the RetrieveDocuments Operation and check, that the expected prescription(s)
    //   has/have been received
    // - ensure success state of the response and that no error message or warning has been given
    //   with the response

    // if technically needed: remember the Identifiers being requested for retrieve incl. their
    // extensions
    var euPharmacist = OnStage.theActorInTheSpotlight();

    and(euPharmacist)
        .attemptsTo(
            RetrieveAndVerifyPrescriptions.withCdaLevel(
                PrescriptionCdaLevel.fromIntValue(cdaLevel)));
  }

  @Und("der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 1 und 3 durchgeführt")
  public void successfulRetrieveOfPrescriptionsBothCdaLevels() {
    // complete execution of RetrieveDocuments for structured document type for both CDA Levels
    // - get the related prescriptionIds and needed data to form the RetrieveDocuments request
    // - perform the RetrieveDocuments Operation and check, that the expected prescription(s)
    //   has/have been received
    // - ensure success state of the response and that no error message or warning has been given
    //   with the response

    // if technically needed: remember the Identifiers being requested for retrieve incl. their
    // extensions
  }

  @Und(
      "^der EU-Apotheker hat erfolgreich ([1-9]) der einlösbaren Rezepte der versicherten Person als CDA Level ([1,3]) abgerufen$")
  public void successfulRetrieveOfSelectedPrescriptions(
      final Integer retrievePrescriptionCount, final Integer cdaLevel) {
    // get the list of prescription Identifiers to retrieve form previous FindDocuments-Operation
    // ensure, that count of prescriptions in the list of prescription is <
    // retrievePrescriptionCount
    // Hint: equal count is excluded, as it does not match the goal of this step !
    // Hint: be aware, that german prescription service starts listing prescriptions sorted by time
    //       with newest first

    // complete execution of RetrieveDocuments for structured document type
    // ->> retrieve given number of prescriptions beginning with the first in the list
    // - get the related prescriptionIds and needed data to form the RetrieveDocuments request
    // - perform the RetrieveDocuments Operation and check, that the expected prescription(s)
    //   has/have been received
    // - ensure success state of the response and that no error message or warning has been given
    //   with the response

    // if technically needed: remember the Identifiers being requested for retrieve incl. their
    // extensions
    var euPharmacist = OnStage.theActorInTheSpotlight();

    and(euPharmacist)
        .attemptsTo(
            RetrieveAndVerifyPrescriptions.mostRecentNForCdaLevel(
                retrievePrescriptionCount, PrescriptionCdaLevel.fromIntValue(cdaLevel)));
  }

  @Wenn(
      "^der EU-Apotheker (?:danach |)(?:das E-Rezept|die E-Rezepte|die aufgelisteten E-Rezepte) der versicherten Person als CDA Level ([1,3]) abruft$")
  public void performEuPharmacistRetrieveDocuments(final Integer cdaLevel) {
    // get the list of prescription Identifiers to retrieve form previous FindDocuments-Operation

    // start the RetrieveDocuments request according to the requested document type (filtered list
    //   of doc-identifiers based on the extension of the identifier)

    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());
    var foundDocuments = FindDocumentsResponseData.from(patientRecord.getPrescriptionMetadata());
    var documentUids =
        euPharmacist.asksFor(
            foundDocuments.documentUidsMatchingCdaLevel(
                PrescriptionCdaLevel.fromIntValue(cdaLevel)));

    when(euPharmacist).attemptsTo(RetrievePrescriptions.byId(documentUids));
  }

  @Wenn(
      "^der EU-Apotheker (?:das E-Rezept|die E-Rezepte) der versicherten Person als CDA Level 1 und 3 abruft$")
  public void performEuPharmacistStartsRetrieveDocumentsForBothDocumentTypes() {
    // get the list of prescription Identifiers to retrieve form previous FindDocuments-Operation

    // start the RetrieveDocuments request for both document types (full list of doc-identifiers)

    // if technically needed: remember the Identifiers being requested for retrieve incl. their
    // extensions
    var euPharmacist = OnStage.theActorInTheSpotlight();

    when(euPharmacist).attemptsTo(RetrievePrescriptions.all());
  }

  @Wenn(
      "^der EU-Apotheker ([1-9]) der einlösbaren Rezepte der versicherten Person als CDA Level ([1,3]) abruft$")
  public void performEuPharmacistStartsRetrieveDocumentsForSingleDocumentType(
      final Integer retrievePrescriptionCount, final Integer cdaLevel) {
    // get the list of prescription Identifiers to retrieve form previous FindDocuments-Operation
    // ensure, that count of prescriptions in the list of prescription is <
    // retrievePrescriptionCount
    // Hint: equal count is excluded, as it does not match the goal of this step !
    // Hint: be aware, that german prescription service starts listing prescriptions sorted by time
    //       with the newest first

    // start the RetrieveDocuments request with prescriptions beginning with the first in the list

    // if technically needed: remember the Identifiers being requested for retrieve incl. their
    // extensions
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());
    var foundDocuments = FindDocumentsResponseData.from(patientRecord.getPrescriptionMetadata());

    var targetIds =
        euPharmacist.asksFor(
            foundDocuments.mostRecentDocumentUidsForLevel(
                retrievePrescriptionCount, PrescriptionCdaLevel.fromIntValue(cdaLevel)));

    when(euPharmacist).attemptsTo(RetrievePrescriptions.byId(targetIds));
  }

  @Wenn(
      "der EU-Apotheker den Abruf von E-Rezepten mit einer zu langen KVNR in der TRC Assertion durchführt")
  public void retrievePrescriptionsWithTrcAssertionWithTooLongKvnr() {
    // take KVNR of patient and modify the number the way, that the String is being extended by
    // a random digit.
    // Perform RetrieveDocuments operation for CDA L3 documents with a TRC Assertion containing the
    // modified KVNR
  }

  @Wenn(
      "der EU-Apotheker die Rezepte der versicherten Person abruft und dabei kein alphanumerischer Zugriffscode in der TRC Assertion zum Einsatz kommt")
  public void retrievePrescriptionsWithNonAlphanumericAccesscodeInTrc() {
    // take accesscode of patient and modify the number the way, that the last digit is being
    // exchanged with "=".
    // Perform the retrieveDocument operation for CDA L3 documents with that special TRC Assertion.
  }

  @Wenn(
      "der EU-Apotheker die Rezepte der versicherten Person abruft und ihm das Recht für ePrescription in der IdA fehlt")
  public void retrievePrescriptionsWithMissingPermissionCode() {
    // ensure, that for next ePrescription operation (search or retrieve) the permission codes of eu
    // pharmacist has nor rights for ePrescription but other codes are available according to
    // eHDSI_SAML_Profile#2.5:
    // - "PRD-006" is given (Patient Identification)
    // - "PRD-004" and "PRD-010" are missing (ePrescription)
    // - "PPD-046" is given (eDispensation)
    // perform the retrieveDocument operation for CDA L3 documents with that special identity
    // assertion
  }

  @Wenn(
      "^ein LE-EU mit der Rolle (.+) und ohne PermissionCodes die Rezepte der versicherten Person abruft")
  public void retrievePrescriptionsWithSpecialIdentityRole(final String roleName) {
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

    // perform the retrieveDocument operation for CDA L3 documents with that special identity
    // assertion
  }

  @Wenn(
      "der EU-Apotheker die Rezepte der versicherten Person abruft und der Typ des LE-Identifiers in der TRC Assertion nicht zu dem in der Identity Assertion passt")
  public void retrievePrescriptionsWithPharmacistIdentifierTypeDoesNotMatch() {
    // prepare the nameId attribute Assertion/Subject/NameID@Format value for the pharmacist do not
    // match between
    // Identity Assertion and TRC Assertion, i.e. use two different valid values according to
    // eHDSI_SAML_profile.
    // Valid values are:
    // - "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified",
    // - "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName" or
    // - "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress"
    // References:
    // [eHDSI_SAML_Profile#4.1 "Generic Structure of the Treatment Relationship
    //   Assertion"], element NameID and attribute NameId@Format
    // and https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#4.1.6
    //   TAB_NCPeH_TRC Assertion und Begleittext

    // perform the retrieveDocument operation for CDA L3 documents with that special identity
    // assertion
  }

  @Und("der EU-Apotheker hat keinen Abruf des Rezeptes durchgeführt")
  public void pharmacistDoesNotRetrievePrescriptions() {
    // ensure, that none of the available prescriptions of the patient are in state "in-progress"
    // any other states for prescriptions are allowed
  }

  @Dann("^erhält der EU-Apotheker ([1-9][0-9]*) Rezept-(?:Dokument|Dokumente) zurück$")
  public void checkEuPharmacistReceivedPrescriptionsOfPatient(final Integer documentCount) {
    // check in received response-metadata the number of retrieved documents (independent of their
    //   document type)
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());
    var retrievedDocuments = RetrievedDocuments.from(patientRecord.getPrescriptionsByDocumentId());

    then(euPharmacist)
        .should(seeThat(retrievedDocuments.documentCount(), Matchers.equalTo(documentCount)));
  }

  @Und("^([1-9][0-9]*) Rezept-(?:Dokument|Dokumente) (?:ist|sind) im Format CDA Level ([1,3])$")
  public void checkPrescriptionsAreOfCdaLevel(final Integer documentCount, final Integer cdaLevel) {
    // compare expected count with count of CDA Level in metadata of Retrieve-Response

    // extract & check received document of format for each identified document of expected document
    //   type
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());
    var prescriptionsById = patientRecord.getPrescriptionsByDocumentId();
    var uidsMatchingCdaLevel =
        RetrievedDocuments.from(prescriptionsById)
            .documentUidsMatchingCdaLevel(PrescriptionCdaLevel.fromIntValue(cdaLevel));

    and(euPharmacist).should(seeThat(uidsMatchingCdaLevel, hasSize(documentCount)));
  }

  @Und(
      "^(?:das|jedes) Rezept-Dokument als CDA ([1,3]) enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes$")
  public void checkPrescriptionDocumentsContainExpectedData(final Integer cdaLevel) {
    // for each received document of ...
    // * CDA Level 1 (PDF) do perform PDF-based checks (manual checks raising a message or
    //   automated by analyzing the PDF content
    //
    // data to be checked / compared with expected document content
    // * prescription ID matches the expected prescriptionId incl. extension for format
    // * Name matches the expected patient data
    // * birthdate matches the expected patient data
    // * attribute for substitution matches the expected value
    // more checks to be done?   --> how about kvnr?
    //   (further checks should be subject to tests of DVKA based on input from BfArM)
    //
    // DONE:
    // * CDA Level 3 (XML) perform XML-bases checks analyzing the received XML document content
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patient = euPharmacist.asksFor(CurrentPatient.asActor());
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());
    var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    var testactor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);

    var verifyPrescriptionsWithStrategyForCdaLevel =
        cdaLevel == 1
            ? VerifyRetrievedCda1Prescriptions.withPatientRecord(patientRecord)
            : VerifyRetrievedCda3Prescriptions.byMatchingDataFrom(patientData, patientRecord);

    and(testactor).attemptsTo(verifyPrescriptionsWithStrategyForCdaLevel);
    OnStage.stage().shineSpotlightOn(euPharmacist.getName());
  }

  @Und("das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung")
  public void checkNcpXcaRetrieveDocumentsResponseNoErrorMessageContent() {
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patientRecord = euPharmacist.asksFor(CurrentPatient.patientRecord());
    var response =
        RetrieveDocumentsResponseData.from(patientRecord.getRetrievePrescriptionsResponse());

    and(euPharmacist)
        .should(seeThat(response.status(), Matchers.equalTo(IheUtils.SUCCESS_RESPONSE)));
    and(euPharmacist).should(seeThat(response.errorCodes(), Matchers.empty()));
  }

  @Und(
      "^der Verarbeitungsstatus (?:des|der) erfolgreich abgerufenen (?:Rezeptes|Rezepte) ist gesetzt$")
  @Und(
      "^die versicherte Person sieht im FdV (?:das für die EU selektierte E-Rezept|die für die EU selektierten E-Rezepte) im Status in-progress$")
  public void checkPresciptionsStateIsInProgress() {
    // check, that each retrieved prescription is/are in german prescription service in state
    // 'in-progress'.

    // for each successfully retrieved prescription, do:
    // as insurant check the prescription state, using FdV test driver interface in
    // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
    // -> /erp/testdriver/api/v1/prescription/{id}: operation get
    // expected: prescription status is 'in-progress'
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patient = euPharmacist.asksFor(CurrentPatient.asActor());
    var prescriptionIds = euPharmacist.asksFor(RetrievedPrescriptions.ids());

    for (String id : prescriptionIds) {
      patient.should(
          seeThat(ErpFdvData.statusOfPrescription(id), Matchers.equalTo(StatusEnum.IN_PROGRESS)));
    }
  }

  @Und(
      "^der Verarbeitungsstatus (?:des|der) nicht (?:erfolgreich |)abgerufenen (?:Rezeptes|Rezepte) ist nicht gesetzt$")
  @Und("^die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready")
  public void checkPresciptionsStateIsReady() {
    // check, that each retrieved prescription is/are in german prescription service in state
    // 'ready'.

    // 1. if prescriptions are not known by PrescriptionId: for each prescription, do:
    // as insurant check the EU selection, using FdV test driver interface in
    // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
    // -> /erp/testdriver/api/v1/prescription: operation get
    // filter result list for expected status: true

    // 2. for each prescription, being selected for processing in EU
    // as insurant check the prescription state, using FdV test driver interface in
    // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
    // -> /erp/testdriver/api/v1/prescription/{id}: operation get
    // expected: prescription status is 'ready'

  }

  @Und(
      "^die versicherte Person sieht im FdV das NICHT für die EU selektierte E-Rezept im Status ready")
  public void checkNonEuSelectedPresciptionsStateIsReady() {
    // check, that in FdV the prescription, which is NOT selected for processing in EU is still in
    // state 'ready'

    // 1. if prescriptions are not known by PrescriptionId: for each prescription, do:
    // as insurant check the EU selection, using FdV test driver interface in
    // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
    // -> /erp/testdriver/api/v1/prescription: operation get
    // filter result list for expected status: false

    // 2. for each prescription, not being selected for processing in EU
    // as insurant check the prescription state, using FdV test driver interface in
    // https://gitlab.prod.ccs.gematik.solutions/e-rezept/fdv-test-driver-interface/-/blob/main/openapi/v2.0.0/ErpTestDriver.yaml
    // -> /erp/testdriver/api/v1/prescription/{id}: operation get
    // expected: prescription status is 'ready'

  }

  @Wenn(
      "die versicherte Person in ihrer E-Rezept-App ein E-Rezept zur Einlösung in der EU deselektiert")
  public void removePreskriptionFromListOfEuDispensablePrescriptions() {
    // remove the tag for EU Access from the first prescription
  }

  @Dann("^erhält der EU-Apotheker (?:kein Rezept|keine Rezepte) ausgegeben$")
  public void checkRetrieveResponseContainsNoPrescription() {
    // check, that retrieveDocuments contains no list of documents
  }

  @Und("^das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für (.+)$")
  public void checkNcpXcaRetrieveDocumentsResponseErrorMessageContent(
      final String expectedXcaErrorSituation) {
    // Type String for expectedXcaErrorSituation could also become an enumeration

    // Tasks:
    // * verify presence of RegistryErrorList in XCA response
    // * verify error message content matches the given expected error situation
    // * as described below / specified in gemSpec_NCPeH-FD

    /* Correlation between expected error situations and their expected error message content:
     * checking codeContext & location shall NOT be case-sensitive

       open Point: Handling of expected error content, when multiple documents shall be retrieved,
       we do not expect, that given sequence of documents from testinterface would be kept
       Working assumption: check the first entry in RegistryErrorList

       (Once data structures are coded, details about content should be removed, only Reference to
        specification should be kept here)
       Expectable error situations
     * 1. epedaRetrieveMissingErpAccessrights
         Reference: "TUC_NCPeH_037: Abzugebende E-Rezepte vom E-Rezept-FD abrufen"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28094
           TAB_NCPeH_Fehlerfälle_Abzugebende_E-Rezepte_vom_E-Rezept-FD_abrufen, HTTP Status Code 403
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_NO_CONSENT"
         ** RegistryErrorList/RegistryError@codeContext="There is no valid access authorisation for
                         the requesting country in the german ePrescription service. Please ask the
                         patient for access authorisation."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The german ePrescription service did respond
                         with HTTP status code 403"
            (attention: the location text is slightly different to epedaSearchMissingErpAccessrights)

      * 2. epedaRetrieveNoPrescriptions
         References: "TUC_NCPeH_037: Abzugebende E-Rezepte vom E-Rezept-FD abrufen"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28094
           TAB_NCPeH_Fehlerfälle_Abzugebende_E-Rezepte_vom_E-Rezept-FD_abrufen, HTTP Status Code 404
         Expected error content:
         ** RegistryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
             -> https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4.2 (table 4.2.4.2-4)
         ** RegistryErrorList/RegistryError@errorCode="WARNING_EP_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="No ePrescription for the patient found,
                         which is dispensable in EU-countries"
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
             -> https://profiles.ihe.net/ITI/TF/Volume2/ITI-43.html#3.43.4.2.3
             -> https://profiles.ihe.net/ITI/TF/Volume2/ITI-43.html#3.43.5
         ** RegistryErrorList/RegistryError@location="The german ePrescription service did respond
                         with HTTP status code 404."

       * 3. epedaRetrievePrescriptionsPartialNotFound
          References:
            TUC_NCPeH_037: Abzugebende E-Rezepte vom E-Rezept-FD abrufen"
              https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28092
              TAB_NCPeH_Error_Not_Found_E-Rezept-ID_UC_ausgewählte_E-Rezepte_abrufen
            TUC_NCPeH_029:
              https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28011
          Expected error content:
          ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:PartialSuccess
             -> https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4.2 (table 4.2.4.2-4)
          ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_NOT_FOUND"
          ** RegistryErrorList/RegistryError@codeContext="No prescription found for the
                         ePrescription ID= " + Wert der betroffenen E-Rezept-ID
          ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
             -> https://profiles.ihe.net/ITI/TF/Volume2/ITI-43.html#3.43.4.2.3
             -> https://profiles.ihe.net/ITI/TF/Volume2/ITI-43.html#3.43.5
          ** RegistryErrorList/RegistryError@location="The german ePrescription service could not
                         find a prescription for the ID= " + Wert der betroffenen E-Rezept-ID
          + Check, that prescriptionId from attributes codeContext and location are equal
          + Check, that prescriptionId from location was part of the RetrieveDocuments-Request

       * 4. epedaRetrieveInvalidPatientIdentifier
          Reference: "TUC_NCPeH_027: Cross Gateway Retrieve Request für ePeD-A auf Einhaltung von allgemeinen
            Kriterien prüfen"
            https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
            TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A
          Expected error content:
          ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:PartialSuccess"
          ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
          ** RegistryErrorList/RegistryError@codeContext="Please make sure that the health insurant identifier is given
                         and correct"
          ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
          ** RegistryErrorList/RegistryError@location="Health insurant number is missing or invalid."

       * 5. epedaRetrieveIncompletePermissionCodes
          Reference: "TUC_NCPeH_027: Cross Gateway Retrieve Request für ePeD-A auf Einhaltung von allgemeinen
            Kriterien prüfen"
            https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
            TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A, Variable ida_permissions,
            falls sie nicht leer ist
          Expected error content:
          ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:PartialSuccess"
          ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
          ** RegistryErrorList/RegistryError@codeContext="Access denied. Please contact your service provider or
                         administrator and check the access rights for your health professional role in your country."
          ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
          ** RegistryErrorList/RegistryError@location="Received permission codes=" + content of ida_permissions
          + Check the expected content of ida_permissions in RegistryErrorList/RegistryError@location:
            "PRD-006" is given (Patient Identification)
            "PRD-004" and "PRD-010" are missing (ePrescription)
            "PPD-046" is given (eDispensation)

       * 6. epedaRetrieveInvalidHpRoleCode
          Reference: "TUC_NCPeH_027: Cross Gateway Retrieve Request für ePeD-A auf Einhaltung von allgemeinen
            Kriterien prüfen"
            https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
            TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A, Die Variable ida_permissions ist leer
          Expected error content:
          ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:PartialSuccess"
          ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
          ** RegistryErrorList/RegistryError@codeContext="Access denied. The information provided about the role of the
                         health professional does not comply with German regulations."
          ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
          ** RegistryErrorList/RegistryError@location="No permission codes received and
                         the access cannot be granted for the received practitioner role code in accordance with
                         national regulations. Received practitioner role code=" + Wert der Variable
                         ida_practitioner_role_code
          + Check, that the string-value "ida_practitioner_role_code" is equal to the given, invalid HP role code

       * 7. epedaRetrieveInvalidCountryScenario
          Reference: "TUC_NCPeH_027: Cross Gateway Retrieve Request für ePeD-A auf Einhaltung von allgemeinen
            Kriterien prüfen"
            https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
            TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A,
            Prüfung LOINC-Code in ALLOWEDLIST_NCPeH_COUNTRY-B
          Expected error content:
          ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:Failure"
          ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
          ** RegistryErrorList/RegistryError@codeContext="The german ePrescription service is not agreed with the
                         requesting country. Please contact your service provider or administrator."
          ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
          ** RegistryErrorList/RegistryError@location="The requesting country is not approved to access the
                         ePrescription service. Received country code= " + Wert der Variable tls_country
          + Check, that the string-value "tls_country" is equal to the code for the active country

       * 8. epedaRetrieveInvalidAccesscode
          Reference: "TUC_NCPeH_027: Cross Gateway Retrieve Request für ePeD-A auf Einhaltung von allgemeinen
            Kriterien prüfen"
            https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
            TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A
          Expected error content:
          ** RegistryResponse@status="urn:ihe:iti:2007:ResponseStatusType:Failure"
          ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
          ** RegistryErrorList/RegistryError@codeContext="Access denied. The patients access code is missing or
                         malformed. Please ask the patient for access authorisation and the proper access code."
          ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
          ** RegistryErrorList/RegistryError@location=(any - no textual requirement defined)

    */

  }
}
