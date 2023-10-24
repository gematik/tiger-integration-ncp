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
public class LeEuFindDocumentsTestSteps {

  @Und(
      "^der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt$")
  public void leEuGetsSuccessfulTwoDocumentReferences() {
    // kompletter Positiv-Ablauf von FindDocuments und den Prüfungen
    euPractice()
        .practitioner()
        .attemptsTo(findPatientSummaryData())
        .verifyThat(twoDocumentsAreFound())
        .verifyThat(documentFoundOfLevel(1))
        .verifyThat(documentFoundOfLevel(3));
  }

  @Wenn("^der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft$")
  @Und(
      "^danach der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft$")
  public void leEuStartsFindDocuments() {
    euPractice()
        .practitioner()
        .treat(euPractice().currentPatient())
        .attemptsTo(findPatientSummaryData());
  }

  @Dann("erhält der LE-EU eine Liste mit zwei Dokumentreferenzen zurück")
  public void leEuReceivesDocumentListWithTwoDocumentReferences() {
    euPractice().practitioner().acknowledgeThat(twoDocumentsAreFound());
  }

  @Und(
      "^ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level (.)$")
  public void aDocumentIsPatientSummaryOfPatientWithCdaLevel(int cdaLevel) {
    // verify, that a document of the returned list
    euPractice()
        .practitioner()
        // * has the homeCommunityId of the NCPeH country A
        // * has the repositoryUniqueId of the ePA service containing the ePKA document of the
        // patient
        // * element sourcePatientId references the patients id
        // * the first part of the documents uniqueId is equal to the unique id of the ePKA document
        // of
        // the patient
        // * the second part of the documents uniqueId marks the given CDA level
        .verifyThat(documentFoundOfLevel(cdaLevel))
        .verifyThat(sourcePatientIdIsCorrectForDocumentOfLevel(cdaLevel));
    // * a creationTime element is present
    // * the element languageCode is equal to "de-DE" (or only contains it???)
    // * the element classCode only contains "60591-5
    // see gemSpec_NCPeH_FD#TUC_NCPeH_004: Cross Gateway Query Response versenden

    // TODO: check, if it is better to split this checklist into different steps?
  }

  @Dann("^der LE-EU erhält einen FindDocuments RegistryError mit errorCode (.+)$")
  public void leEuReceivesFindDocumentRegistryErrorWithErrorcode(String errorCode) {
    // verify, that the find document response state is
    // "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure"
    // verify, that the find document response contains an AcknowledgementDetail with the given
    // errorCode
  }

  @Dann("erhält der LE-EU keine Liste von Dokumentreferenzen")
  public void leEuReceivesFindDocumentResponseErrorWithoutDocumentlist() {
    // verify, that the find document response contains no list with document references
  }

  @Wenn("der LE-EU {int} Minuten anderweitig beschäftigt ist")
  public void leEuIsBusyForMinutes(int minutes) {
    // wait for the given number of minutes
  }
}
