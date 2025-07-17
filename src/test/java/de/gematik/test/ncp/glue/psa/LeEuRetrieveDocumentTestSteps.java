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
import static net.serenitybdd.screenplay.GivenWhenThen.then;

import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import de.gematik.test.ncp.screenplay.actions.RetrievePatientSummary;
import de.gematik.test.ncp.screenplay.questions.GetRegistryErrorCodesFromRegistryResponse;
import de.gematik.test.ncp.screenplay.questions.GetRetrievedPatientSummaryDocuments;
import de.gematik.test.ncp.screenplay.questions.IsRetrievedPatientSummaryResponseStatusFailure;
import de.gematik.test.ncp.screenplay.questions.IsRetrievedPatientSummaryResponseStatusSuccess;
import de.gematik.test.ncp.screenplay.questions.PdfDocumentBelongsToPatient;
import de.gematik.test.ncp.screenplay.questions.RetrievedPatientSummaryDocumentsHcidBelongsToNcpeh;
import de.gematik.test.ncp.screenplay.questions.RetrievedPatientSummaryDocumentsIsOfLevel;
import de.gematik.test.ncp.screenplay.questions.RetrievedPatientSummaryDocumentsMatchRequested;
import de.gematik.test.ncp.screenplay.questions.StructuredDocumentBelongsToPatient;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@CucumberOptions(monochrome = true)
public class LeEuRetrieveDocumentTestSteps {

  @Wenn(
      "^der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level (\\d) abruft$")
  @Und(
      "^der LE-EU ruft das Patient Summary Dokument der versicherten Person als CDA Level (\\d) ab$")
  @Und(
      "^danach der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level (\\d) abruft$")
  public void leEuStartsRetrieveDocumentsForOneDocumentType(final Integer cdaLevel) {
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU for
    // assigned insurant and use the documentUniqueId according to the given CDA Level

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(RetrievePatientSummary.ofLevel(PatientSummaryLevel.fromValue(cdaLevel)));
  }

  @Wenn(
      "^der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 und Level 1 abruft$")
  // TODO: The same implementation in
  // de.gematik.test.ncp.glue.psa.EuPracticeTestSteps.downloadPatientSummaryToPatient
  public void leEuStartsRetrieveDocumentsForBothDocumentTypes() {
    // call of IHE operation RetrieveDocuments with IDA of LE-EU and TRC Assertions of LE-EU for
    // assigned insurance and use BOTH documentUniqueIds to get both documents in one response
    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(
            RetrievePatientSummary.ofLevel(
                PatientSummaryLevel.LEVEL_1, PatientSummaryLevel.LEVEL_3));
  }

  @Wenn(
      "^der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level (\\d) mit einer falschen RepositoryUniqueId abruft$")
  public void leEuStartsRetrieveDocumentsWithInvalidRepositoryUniqueId(final Integer cdaLevel) {
    // falsificate the known RepositoryUniqueId (UUID) of the preceding successful search result and
    // then
    // call of IHE operation RetrieveDocuments for CDA Level 3 with the false RepositoryUniqueId
    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(
            RetrievePatientSummary.fromLevelAndRepositoryUniqueId(
                "2.16.620.1.101.10.3.29.123456", PatientSummaryLevel.fromValue(cdaLevel)));
  }

  @Dann("^erhält der LE-EU beide Patient Summary Dokumente der versicherten Person zurück$")
  public void leEuReceivesTwoDocumentsOfPatient() {
    // verify, that ResponseStatus is "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"
    // verify, that two documents have been returned and
    // verify, that the Metadata along with the documents belong to the patient
    // * HomeCommunityId of the NCPeH
    // * RepositoryUniqueId of the ePA service
    // * DocumentUniqueIds of the document plus postfix for CDA3 / CDA1

    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor)
        .attemptsTo(Ensure.that(new IsRetrievedPatientSummaryResponseStatusSuccess()).isTrue());
    then(leEuActor).attemptsTo(Ensure.that(new GetRetrievedPatientSummaryDocuments()).hasSize(2));
    then(leEuActor)
        .attemptsTo(Ensure.that(new RetrievedPatientSummaryDocumentsHcidBelongsToNcpeh()).isTrue());
    then(leEuActor)
        .attemptsTo(Ensure.that(new RetrievedPatientSummaryDocumentsMatchRequested()).isTrue());
  }

  @Dann("^erhält der LE-EU ein Dokument der versicherten Person zurück$")
  public void leEuReceivesOneDocumentOfPatient() {
    // verify, that ResponseStatus is "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success"
    // verify, that exactly one document has been returned and
    // verify, that the Metadata along with the document belongs to the patient
    // * HomeCommunityId of the NCPeH
    // * RepositoryUniqueId of the ePA service
    // * DocumentUniqueIds of the document without postfix
    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor)
        .attemptsTo(Ensure.that(new IsRetrievedPatientSummaryResponseStatusSuccess()).isTrue());
    then(leEuActor).attemptsTo(Ensure.that(new GetRetrievedPatientSummaryDocuments()).hasSize(1));
    then(leEuActor)
        .attemptsTo(Ensure.that(new RetrievedPatientSummaryDocumentsHcidBelongsToNcpeh()).isTrue());
    then(leEuActor)
        .attemptsTo(Ensure.that(new RetrievedPatientSummaryDocumentsMatchRequested()).isTrue());
  }

  @Und("^(?:das|ein) Dokument ist im Format CDA Level (\\d)$")
  public void documentIsOfCdaLevel(final Integer cdaLevel) {
    // verify, that the received document is of the given CDA level
    // 1: PDF (i.e. file-header of the extracted PDF file)
    //      documentUniqueId is extended by postfix "^2.25.2.PS.1"
    // 3: XML (some CDA L3-unique, mandatory XML structure)
    //      documentUniqueId is extended by postfix "^2.25.2.PS.2"
    // gazelle checks possible?
    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(
            Ensure.that(RetrievedPatientSummaryDocumentsIsOfLevel.ofLevel(cdaLevel)).isTrue());
  }

  @Und("^das strukturierte Dokument enthält die persönlichen Daten der versicherten Person$")
  public void structuredDocumentContainsPersonalDataOfPatient() {
    // verify, that the structured document contains the known personal data
    // -> full name, birthdate, KVNR
    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor).attemptsTo(Ensure.that(new StructuredDocumentBelongsToPatient()).isTrue());
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

    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor).attemptsTo(Ensure.that(new PdfDocumentBelongsToPatient()).isTrue());
  }

  @Dann("^erhält der LE-EU kein Dokument der versicherten Person zurück$")
  public void ncpXcaResponseContainsNoDocument() {
    // verify, that no document is included in the XCA-Response
    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor).attemptsTo(Ensure.that(new GetRetrievedPatientSummaryDocuments()).isEmpty());
  }

  @Und(
      "^das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode (.+)$")
  public void ncpXcaResponseForRetrieveDocumentsContainsError(final String expectedErrorCode) {
    // verify presence of RegistryErrorList
    // verify, that ResponseStatus is "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
    // verify, that received errorCode element matches the expectedErrorCode
    // more information please see
    // * eHDSI_XCA_Profile#2.4 CrossGatewayRetrieve Messages
    // * eHealth DSI Error Codes,
    //   https://webgate.ec.europa.eu/fpfis/wikis/display/EHDSI/Exception+Handling+in+MyHealth@EU
    // * https://profiles.ihe.net/ITI/TF/Volume2/ITI-39.html
    // * https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor)
        .attemptsTo(Ensure.that(new IsRetrievedPatientSummaryResponseStatusFailure()).isTrue());
    then(leEuActor)
        .attemptsTo(
            Ensure.that(new GetRegistryErrorCodesFromRegistryResponse())
                .contains(expectedErrorCode));
  }
}
