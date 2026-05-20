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
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.actions.FindDocuments;
import de.gematik.test.ncp.screenplay.actions.find_documents.VerifyMandatoryPrescriptionMetadata;
import de.gematik.test.ncp.screenplay.questions.xca.find_documents.FindDocumentsResponseData;
import de.gematik.test.ncp.screenplay.questions.xca.find_documents.FoundPrescriptionData;
import de.gematik.test.ncp.util.IheUtils;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
public class LeEuFindPrescriptionTestSteps {

  @Und(
      "der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet")
  public void successfulListingOfPrescriptions() {
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patient = euPharmacist.usingAbilityTo(TreatPatient.class).getPatient();
    var patientRecord =
        euPharmacist.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);
    var prescriptionCount =
        patient.usingAbilityTo(ProvidePatientData.class).getEuRedeemablePrescriptions().size();

    givenThat(euPharmacist).wasAbleTo(FindDocuments.forEped());

    var foundDocuments = FindDocumentsResponseData.from(patientRecord.getPrescriptionMetadata());
    List<String> availableIds = euPharmacist.asksFor(foundDocuments.documentUids());
    log.info("document ids available for retrieval: \n{}", String.join("\n", availableIds));

    and(euPharmacist)
        .wasAbleTo(
            Ensure.that(foundDocuments.responseStatus()).isEqualTo(IheUtils.SUCCESS_RESPONSE));

    // there are two documents per prescription (CDA L1 + CDA L3)
    and(euPharmacist)
        .wasAbleTo(Ensure.that(foundDocuments.documentCount()).isEqualTo(prescriptionCount * 2));
  }

  @Wenn(
      "der EU-Apotheker die Suche nach den E-Rezepten mit einer KVNR nur mit Ziffern in der TRC Assertion aufruft")
  public void findPrescriptionsWithTrcAssertionWithOnlyDigitsInKvnr() {
    // take KVNR of patient and modify the number the way, that the starting letter is exchanged by
    // a random digit.
    // perform the findDocuments operation with that special TRC assertion
  }

  @Wenn(
      "der EU-Apotheker die Suche nach E-Rezepten mit einem zu langen Zugriffscode in der TRC Assertion aufruft")
  public void findPrescriptionsWithTrcAssertionWithTooLongAccesscode() {
    // take AccessCode of patient and modify the number the way, that an 'A' is being added to the
    // string.
    // perform the findDocuments operation with that special TRC assertion
  }

  @Wenn(
      "der EU-Apotheker nach den Rezepten der versicherten Person sucht und dabei ein anderer Zugriffscode als in der TRC Assertion zum Einsatz kommt")
  public void findPrescriptionsWithAccessCodeDoesNotMatchTrcContent() {
    // take AccessCode of patient and modify the number the way, that the first and the second digit
    // are being exchanged. Take care, that
    // * the original AccessCode is being used for TRC Assertion and
    // * the modified AccessCode is being used in the XCA Query Request
    // Perform the XCA Query operation with described data conditions
  }

  @Wenn(
      "der EU-Apotheker nach den Rezepten der versicherten Person sucht und ihm das Recht für ePrescription in der IdA fehlt")
  public void findPrescriptionsWithMissingPermissionCode() {
    // ensure, that for next ePrescription operation (search or retrieve) the permission codes of eu
    // pharmacist has nor rights for ePrescription but other codes are available according to
    // eHDSI_SAML_Profile#2.5:
    // - "PRD-006" is given (Patient Identification)
    // - "PRD-004" and "PRD-010" are missing (ePrescription)
    // - "PPD-046" is given (eDispensation)
    // perform the findDocument operation with that special identity assertion
  }

  @Wenn(
      "^ein LE-EU mit der Rolle (.+) und ohne PermissionCodes die Rezepte der versicherten Person sucht$")
  public void findPrescriptionsWithSpecialIdentityRole(final String roleName) {
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

    // perform the findDocument operation with that special identity assertion
  }

  @Wenn(
      "der EU-Apotheker nach Rezepten der versicherten Person sucht und der LE-Identifier in der TRC Assertion nicht zu dem in der Identity Assertion passt")
  public void findPrescriptionsWithPharmacistIdentifierStringDoesNotMatch() {
    // prepare the element value of 'NameId' of the pharmacist, so that they do not match between
    // Identity Assertion and TRC Assertion,
    // i.e. define NameId value from IdA and switch the last letter between lower and capital letter
    // for TRCs NameId value (change as less as possible compared to the IdA value)
    // References: [eHDSI_SAML_Profile#4.1 "Generic Structure of the Treatment Relationship
    //   Assertion"], element NameID
    // and https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#4.1.6
    //   TAB_NCPeH_TRC Assertion und Begleittext

    // perform the findDocument operation with these special assertions
  }

  @Wenn(
      "^der EU-Apotheker die Suche nach den E-Rezepten mit CountryCode für (.+) im Signerzertifikat der TRC aufruft$")
  public void doFindPrescriptionsWithTrcSignerCertFrom(final String signerCountryCode) {
    // perform a FindDocument request, where country code in subject of TLS certificate does not
    // match the country code in subject of identity assertion signer certificate

    // The country B simulator has to take the TLS certificate according to the parameter
    // EuCountryCode on the test interface.
    // Provoke the mismatch at the test interface by setting the TRCAssertionProfile name according
    // to the parameter signerCountryCode.
  }

  @Angenommen("der EU-Apotheker ruft die Suche nach den E-Rezepten der versicherten Person auf")
  // syntax hint: when using a RegEx with empty string, it must directly follow a word. A space in
  // between does not work.
  @Wenn(
      "^der(?: neue|) EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft$")
  public void leEuPharmacistStartsFindDocuments() {
    // run the FindDocuments-Request according to eHDSI-Rules with the available patient and EU
    // pharmacist data
    final var euPharmacist = OnStage.theActorInTheSpotlight();
    when(euPharmacist).attemptsTo(FindDocuments.forEped());
  }

  @Und("^der EU-Apotheker erhält eine Liste mit ([1-9][0-9]*) E-Rezept Referenzen zurück$")
  @Dann(
      "^erhält der(?: neue|) EU-Apotheker eine Liste mit ([1-9][0-9]*) E-Rezept Referenzen zurück$")
  public void leEuPharmacistReceivesDocumentListWithTwoDocumentReferences(
      final int prescriptionCount) {
    // check the response is success
    // check the count of the listed documents is equal to the given number
    // remember the prescription IDs (more needed? with or without CDA Level information?)
    var euPharmacist = OnStage.theActorInTheSpotlight();
    var patient = euPharmacist.usingAbilityTo(TreatPatient.class).getPatient();
    var patientRecord =
        euPharmacist.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);
    var foundDocuments = FindDocumentsResponseData.from(patientRecord.getPrescriptionMetadata());

    and(euPharmacist)
        .attemptsTo(
            Ensure.that(foundDocuments.responseStatus()).isEqualTo(IheUtils.SUCCESS_RESPONSE),
            Ensure.that(foundDocuments.documentCount()).isEqualTo(prescriptionCount));
  }

  @Und(
      "^für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level ([1,3]) gelistet$")
  public void forEveryPrescriptionOfPatientExistsDocumentWithCdaLevel(final int cdaLevel) {
    // check for every remembered prescription ID, that the Document type with the given CDA Level
    // is available in the response
    var euPharmacist = OnStage.theActorInTheSpotlight();

    and(euPharmacist)
        .attemptsTo(
            Ensure.that(
                    FoundPrescriptionData.allMatchLevel(
                        PrescriptionCdaLevel.fromIntValue(cdaLevel)))
                .isTrue());
  }

  /* calling scenario is in state Zurückgestellt
  @Wenn(
      "der EU-Apotheker ([1-9][0-9]*) Minuten nach der Berechtigungserteilung die Suche nach den E-Rezepten dieser Person aufruft")
  public void pharmacistStartsFindDocumentsAfterMinutesOfPermissionGrant(int minutes) {
    // the according to the given number of minutes

    // run the FindDocuments-Request according to eHDSI-Rules with the available patient and EU
    // pharmacist data (see leEuPharmacistStartsFindDocuments())
  } */

  @Und("mit jeder gelisteten Dokumentreferenz werden die geforderten Metadaten geliefert")
  public void checkMandatoryMetadataContentForPrescriptions() {
    final var euPharmacist = OnStage.theActorInTheSpotlight();
    and(euPharmacist).attemptsTo(VerifyMandatoryPrescriptionMetadata.inFindDocumentsResponse());
  }

  @Dann("erhält der EU-Apotheker keine Rezepte gelistet")
  public void euPharmacistReceivesFindDocumentResponseErrorWithoutDocumentlist() {
    // verify, that the find document response contains no list with document references
  }

  @Und("^das Suchergebnis für den EU-Apotheker enthält die (?:Fehlermeldung|Warnung) für (.+)$")
  public void checkNcpXcaFinddocumentsResponseErrorMessageContent(
      final String expectedXcaErrorSituation) {
    // Type String for expectedXcaErrorSituation could also become an enumeration

    // Tasks:
    // * verify presence of RegistryErrorList in XCA response
    // * verify error message content matches the given expected error situation
    // * as described below / specified in gemSpec_NCPeH-FD

    /* Correlation between expected error situations and their expected error message content:
     * checking codeContext & location shall NOT be case-sensitive

       (Once data structures are coded, details about content should be removed, only Reference to
        specification should be kept here)
       Expectable error situations
     * 1. epedaSearchMissingErpAccessrights
         Reference: "TUC_NCPeH_036: Liste der einlösbaren E-Rezepte des Versicherten aus dem E-Rezept-FD abrufen"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27942
           TAB_NCPeH_Allgemeine_Fehlerfälle_Auflistung_von_einlösbaren_E-Rezepten, HTTP Status Code 403
         Expected error content:
         ** AdhocQueryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_NO_CONSENT"
         ** RegistryErrorList/RegistryError@codeContext="There is no valid access authorisation for
                         the requesting country in the german ePrescription service. Please ask the
                         patient for access authorisation."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The ePrescription service did respond with
                         HTTP status code 403."

      * 2. epedaSearchNoPrescriptions
         Reference: "TUC_NCPeH_036: Liste der einlösbaren E-Rezepte des Versicherten aus dem E-Rezept-FD abrufen"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27942
           TAB_NCPeH_Allgemeine_Fehlerfälle_Auflistung_von_einlösbaren_E-Rezepten, HTTP Status Code 404
         Expected error content:
         ** AdhocQueryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_NOT_FOUND"
         ** RegistryErrorList/RegistryError@codeContext="No ePrescription for the patient found,
                         which is dispensable in EU-countries"
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The ePrescription service did respond with
                         HTTP status code 404."

      * 3. epedaSearchInvalidPatientIdentifier
         Reference: "TUC_NCPeH_025: Cross Gateway Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
           TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung der Variable trc_kvnr & XDSDocumentEntryPatientId
         Expected error content:
         ** AdhocQueryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Please make sure the health insurant identifier is correct."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="Received XDSDocumentEntryPatientId= " + Wert aus dem
                         XDSDocumentEntryPatientId-Slot
         + Check, that the string-value "Wert aus dem XDSDocumentEntryPatientId-Slot" is equal to the
           given, invalid KVNR (ignore whitspaces between '=' and KVNR)

      * 4. epedaSearchIncompletePermissionCodes
         Reference: "TUC_NCPeH_025: Cross Gateway Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
           TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, check of ida_permissions, when not empty
         Expected error content:
         ** AdhocQueryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Access denied. Please contact your service provider or
                         administrator and check the access rights for your health care professional role in
                         your country."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="Received permission codes= " + Wert der Variable ida_permissions
         + Check, that the string-value "ida_permissions" match the list of given permission codes
           (ignore whitespaces between '=' and permission code list)

      * 5. epedaSearchInvalidHpRoleCode
         Reference: "TUC_NCPeH_025: Cross Gateway Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
           TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, check when ida_permissions is empty
         Expected error content:
         ** AdhocQueryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Access denied. The information provided about the role
                         of the health professional does not comply with German regulations."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="No permission codes received and the access cannot be granted
                         for the received practitioner role code in accordance with national regulations.
                         Received practitioner role code=" + Wert der Variable ida_practitioner_role_code
         + Check, that the string-value "ida_practitioner_role_code" is equal to the given, invalid HP role code

      * 6. epedaSearchInvalidCountryScenario
         Reference: "TUC_NCPeH_025: Cross Gateway Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
           TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung LOINC-Code in ALLOWEDLIST_NCPeH_COUNTRY-B
         Expected error content:
         ** AdhocQueryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="The german ePrescription service is not agreed with the
                         requesting country. Please contact your service provider or administrator."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location="The requesting country is not approved to access the ePrescription
                         service. Received country code= " + Wert der Variable tls_country
         + Check, that the string-value "tls_country" is equal to the code for the active country

      * 7. epedaSearchInvalidAccesscode
         Reference: "TUC_NCPeH_025: Cross Gateway Query Request für ePeD-A verarbeiten"
           https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
           TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, check of trc_accesscode
         Expected error content:
         ** AdhocQueryResponse@status="urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
         ** RegistryErrorList/RegistryError@errorCode="ERROR_EP_GENERIC"
         ** RegistryErrorList/RegistryError@codeContext="Access denied. The patients access code is missing or
                         malformed. Please ask the patient for access authorisation and the proper access code."
         ** RegistryErrorList/RegistryError@severity="urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error"
         ** RegistryErrorList/RegistryError@location=(empty or missing - no textual requirement defined)
    */
  }

  @Und("^der EU-Apotheker erhält eine allgemeine Fehlermeldung für (.+)$")
  @Dann("^erhält der EU-Apotheker eine allgemeine Fehlermeldung für (.+)$")
  public void checkStandardSoapFaultWithSubcode(final String subcode) {
    // Check Soap Fault according to Soap 1.2 standard with following subcode content:
    // * 1. noMatchingNameIDs, noMatchingNameIdFormats:
    //   Check, that SOAP Fault according to Soap 1.2 standard has been sent by NCPeH-FD
    //     References: [eHDSI_SAML_Profile#4.1 "Generic Structure of the Treatment Relationship
    //                Assertion"], Element NameID;
    //     [eHDSI_NCPeH_Components_Specifications#4.4.2.4 Security Header Encoding and Consistency
    //                Errors];
    //     [eHDSI_NCPeH_Components_Specifications#4.4.2.1	SOAP Error Profile];
    //     https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28561
    //   ** Code=Sender
    //   ** Subcode="HP Missing Attributes"
    //   ** Reason="The identifier of the health professional from the treatment relationship
    //              confirmation does not match the equivalent value from the identity assertion."
    //   ** Detail = Keine Angaben
    //
    // * 2. idaCountryCodeMismatch
    //   Check, that SOAP Fault according to Soap 1.2 standard has been sent by NCPeH-FD
    //     References:
    //     [eHDSI_NCPeH_Components_Specifications#4.4.2.4 Security Header Encoding and Consistency
    //                Errors];
    //     [eHDSI_NCPeH_Components_Specifications#4.4.2.1	SOAP Error Profile];
    //     https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28457
    //   ** Code=Sender
    //   ** Subcode=Invalid Security Token
    //   ** Reason="The country of origin in the request is inconsistent. Please contact your
    //                service provider or administrator."
    //   ** Detail="Country code received from the public TLS key=" + Wert der Variable tls_country
    //                + ".Country code received from the public key of HP Identity Assurance= " +
    //                Ländercode aus dem Element "Subject: C (Country)" des öffentlichen und validen
    //                Zertifikats der Identity Assertion des LE-EU
    //   + check value of Variable tls_country is equal to EUCountryCode given on test interface
    //   + check value of IdA signer country code is equal to countryCode of IdAAssertionProfile
    //
    // * 3. trcCountryCodeMismatch
    //   Check, that SOAP Fault according to Soap 1.2 standard has been sent by NCPeH-FD
    //     References:
    //     [eHDSI_NCPeH_Components_Specifications#4.4.2.4 Security Header Encoding and Consistency
    //                Errors];
    //     [eHDSI_NCPeH_Components_Specifications#4.4.2.1	SOAP Error Profile];
    //     https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28561
    //   ** Code=Sender
    //   ** Subcode=Invalid Security Token
    //   ** Reason="The country of origin in the request is inconsistent. Please contact your
    //                service provider or administrator."
    //   ** Detail="Country code received from the public TLS key=" + Wert der Variable tls_country
    //                + ".Country code received from the public key of TRC Assurance=" +
    //                Ländercode aus dem Element "Subject: C (Country)" des öffentlichen und validen
    //                Zertifikats der TRC Assertion
    //   + check value of Variable tls_country is equal to EUCountryCode given on test interface
    //   + check value of TRC signer country code is equal to countryCode of TRCAssertionProfile
  }
}
