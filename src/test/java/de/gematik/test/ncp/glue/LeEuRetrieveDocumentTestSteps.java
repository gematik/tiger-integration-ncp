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
import static de.gematik.test.ncp.screenplay.Practice.*;

import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import io.cucumber.junit.CucumberOptions;

@CucumberOptions(monochrome = true)
public class LeEuRetrieveDocumentTestSteps {

  @Wenn(
      "^der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level (\\d) abruft$")
  public void leEuStartsRetrieveDocumentsForOneDocumentType(Integer cdaLevel) {
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU for
    // assigned insurant and use the documentUniqueId according to the given CDA Level
    euPractice().practitioner().attemptsTo(retrievePatientSummary(cdaLevel));
  }

  @Wenn(
      "^der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 und Level 1 abruft$")
  public void leEuStartsRetrieveDocumentsForBothDocumentTypes() {
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU for
    // assigned insurant and use BOTH documentUniqueIds to get both documents in one response
    euPractice().practitioner().attemptsTo(retrieveAllPatientSummaryData());
  }

  @Wenn(
      "^der NCPeH Land B für den LE-EU das Patient Summary Dokument der versicherten Person mit existierender aber falscher RepositoryUniqueId abruft$")
  public void leEuStartsRetrieveDocumentsWithFalseButValidRepositoryUniqueId() {
    // identify actual valid HCID of ePKA document of the patient
    // find HCID, which refers to a valid but different ePA Aktensystem
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU and
    // the different, identified ePA HCID as RepositoryUniqueId for
    // assigned insurant and use the documentUniqueId according to the CDA Level 3
  }

  @Wenn(
      "^der NCPeH Land B für den LE-EU das Patient Summary Dokument der versicherten Person mit nicht existierender RepositoryUniqueId (.+) abruft$")
  public void leEuStartsRetrieveDocumentsWithInvalidRepositoryUniqueId(
      String invalidRepositoryUniqueId) {
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU and
    // the different, identified ePA HCID as RepositoryUniqueId for
    // assigned insurant and use the documentUniqueId according to the CDA Level 3
  }

  @Wenn(
      "^der NCPeH Land B für den LE-EU das Patient Summary Dokument der versicherten Person mit falscher DocumentUniqueId abruft$")
  public void leEuStartsRetrieveDocumentsWithInvalidDocumentUniqueId() {
    // identify actual valid DocumentUniqueId of ePKA document of the patient
    // modify that DocumentUniqueId, so that it will be valid in its format, but not the ID fitting
    // to ePKA document (i.e. increase an ID number-part by 1)
    // DO NOT modify the DocumentUniqueId-extension "^2.25.2.PS.2"
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU and
    // the modified, wrong DocumentUniqueId with the proper extension "^2.25.2.PS.2" for CDA level 3
  }

  @Wenn(
      "^der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 mit korrekter und Level 1 mit falscher DocumentUniqueId abruft$")
  public void leEuStartsRetrieveDocumentsForBothDocumentTypesWithOneInvalidDocumentUniqueId() {
    // identify actual valid DocumentUniqueId of ePKA document of the patient
    // modify that DocumentUniqueId, so that it will be valid in its format, but not the ID fitting
    // to ePKA document (i.e. increase an ID number-part by 1)
    // DO NOT modify the extensions "^2.25.2.PS.2"
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU with
    // * modified, wrong DocumentUniqueId + extension "^2.25.2.PS.1" for CDA level 1
    // * valid DocumentUniqueId + extension "^2.25.2.PS.2" for CDA level 3
  }

  @Dann("^erhält der LE-EU beide Patient Summary Dokumente der versicherten Person zurück$")
  public void leEuReceivesTwoDocumentsOfPatient() {
    // verify, that ResponseStatus is "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"
    // verify, that two documents have been returned and
    // verify, that the Metadata along with the documents belong to the patient
    // * HomeCommunityId of the NCPeH
    // * RepositoryUniqueId of the ePA service
    // * DocumentUniqueIds of the document plus postfix for CDA3 / CDA1
    euPractice()
        .practitioner()
        .verifyThat(retrieveResponseStatusIsSuccess())
        .verifyThat(twoDocumentsAreRetrieved())
        .verifyThat(retrieveDocumentsHcidBelongsToNcpeh())
        .verifyThat(retrievedDocumentsWereRequested());
  }

  @Dann("^erhält der LE-EU ein Dokument der versicherten Person zurück$")
  public void leEuReceivesOneDocumentOfPatient() {
    // verify, that ResponseStatus is "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"
    // verify, that exactly one document has been returned and
    // verify, that the Metadata along with the document belongs to the patient
    // * HomeCommunityId of the NCPeH
    // * RepositoryUniqueId of the ePA service
    // * DocumentUniqueIds of the document without postfix
    euPractice()
        .practitioner()
        .verifyThat(retrieveResponseStatusIsSuccess())
        .verifyThat(oneDocumentIsRetrieved())
        .verifyThat(retrieveDocumentsHcidBelongsToNcpeh())
        .verifyThat(retrievedDocumentsWereRequested());
  }

  @Und("^(?:das|ein) Dokument ist im Format CDA Level (\\d)$")
  public void documentIsOfCdaLevel(Integer cdaLevel) {
    // verify, that the received document is of the given CDA level
    // 1: PDF (i.e. file-header of the extracted PDF file)
    //      documentUniqueId is extended by postfix "^2.25.2.PS.1"
    // 3: XML (some CDA L3-unique, mandatory XML structure)
    //      documentUniqueId is extended by postfix "^2.25.2.PS.2"
    // gazelle checks possible?
    euPractice().practitioner().acknowledgeThat(documentIsOfLevel(cdaLevel));
  }

  @Und("^das strukturierte Dokument enthält die persönlichen Daten der versicherten Person$")
  public void structuredDocumentContainsPersonalDataOfPatient() {
    // verify, that the structured document contains the known personal data
    // -> full name, birthdate, KVNR
    euPractice().practitioner().acknowledgeThat(structuredDocumentBelongsToPatient());
  }

  @Und("^das PDF Dokument enthält die persönlichen Daten der versicherten Person$")
  public void pdfDocumentContainsPersonalDataOfPatient() {
    // !!! manual test step to look into the PDF file !!!
    // raise a message box/page with answer options pass/fail/inconclusive and containing following
    // action information for the tester
    // * the goal of the test step is only to proof, that the document belongs to the patient and is
    // readable
    // 1. where to find the document and to open that PDF document
    //      possible alternative: open it automatically?
    // 2. the reader has to check, that the document has general readable information and
    // 3. the document contains certain personal information (display the expected information)
    //    -> full name, birthdate, KVNR
    euPractice().practitioner().acknowledgeThat(pdfDocumentBelongsToPatient());
  }

  @Dann("^erhält der LE-EU kein Dokument (?:der|des) Versicherten zurück$")
  public void ncpXcaResponseContainsNoDocument() {
    // verify, that no document is included in the XCA-Response
    euPractice().practitioner().acknowledgeThat(noDocumentWasRetrieved());
  }

  @Und(
      "^das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode (.+)$")
  public void ncpXcaResponseForRetrieveDocumentsContainsError(String expectedErrorCode) {
    // verify presence of RegistryErrorList
    // verify, that ResponseStatus is "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
    // verify, that received errorCode element matches the expectedErrorCode
    // more information please see
    // * eHDSI_XCA_Profile#2.4 CrossGatewayRetrieve Messages
    // * eHDSI_Components_Specification#6.4 eHealth DSI Error Codes, Table 1
    // * https://profiles.ihe.net/ITI/TF/Volume2/ITI-39.html
    // * https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4
  }

  @Und(
      "^das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit einer Warnung (.+) oder (.+)$")
  public void ncpXcaResponseForRetrieveDocumentsContainsSecurityWarning(
      String expectedErrorCode1, String expectedErrorCode2) {
    // verify presence of RegistryErrorList
    // verify, that ResponseStatus is "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
    // verify, that received errorCode element matches the expectedErrorCode1 OR expectedErrorCode2
    // more information please see
    // * eHDSI_XCA_Profile#2.4 CrossGatewayRetrieve Messages
    // * eHDSI_Components_Specification#6.4 eHealth DSI Error Codes, Table 1
    // * https://profiles.ihe.net/ITI/TF/Volume2/ITI-39.html
    // * https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4
  }
}
