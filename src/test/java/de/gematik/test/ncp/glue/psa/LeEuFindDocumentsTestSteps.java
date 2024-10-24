/*
 * Copyright (c) 2024. gematik GmbH
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

import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import de.gematik.test.ncp.screenplay.actions.FindPatientSummaryData;
import de.gematik.test.ncp.screenplay.questions.GetNumberOfPatientSummaryDocuments;
import de.gematik.test.ncp.screenplay.questions.IsDocumentFoundOfLevel;
import de.gematik.test.ncp.screenplay.questions.IsSourcePatientIdCorrectForDocument;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import io.cucumber.junit.CucumberOptions;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@CucumberOptions(monochrome = true)
@Slf4j
public class LeEuFindDocumentsTestSteps {

  @Und(
      "der LE-EU hat erfolgreich eine Suche des Patient Summary der versicherten Person durchgeführt")
  @Und(
      "der LE-EU führt danach erfolgreich eine Suche des Patient Summary der versicherten Person durch")
  public void leEuGetsSuccessfulTwoDocumentReferences() {
    // kompletter Positiv-Ablauf von FindDocuments und den Prüfungen

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor).attemptsTo(FindPatientSummaryData.instance());
    and(leEuActor).attemptsTo(Ensure.that(new GetNumberOfPatientSummaryDocuments()).isEqualTo(2L));
    and(leEuActor).attemptsTo(Ensure.that(IsDocumentFoundOfLevel.ofLevel(1)).isTrue());
    and(leEuActor).attemptsTo(Ensure.that(IsDocumentFoundOfLevel.ofLevel(3)).isTrue());
  }

  @Wenn("^der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft$")
  @Und(
      "^danach der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft$")
  public void leEuStartsFindDocuments() {

    final var leEuActor = OnStage.theActorInTheSpotlight();
    when(leEuActor).attemptsTo(FindPatientSummaryData.instance());
  }

  @Und(
      "^ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level (.)$")
  public void aDocumentIsPatientSummaryOfPatientWithCdaLevel(final int cdaLevel) {

    final var leEuActor = OnStage.theActorInTheSpotlight();
    // * has the homeCommunityId of the NCPeH country A
    // * has the repositoryUniqueId of the ePA service containing the ePKA document of the
    // patient
    // * element sourcePatientId references the patients id
    // * the first part of the documents uniqueId is equal to the unique id of the ePKA document
    // of
    // the patient
    // * the second part of the documents uniqueId marks the given CDA level
    and(leEuActor).attemptsTo(Ensure.that(IsDocumentFoundOfLevel.ofLevel(cdaLevel)).isTrue());
    and(leEuActor)
        .attemptsTo(Ensure.that(IsSourcePatientIdCorrectForDocument.ofLevel(cdaLevel)).isTrue());

    // * a creationTime element is present
    // * the element languageCode is equal to "de-DE" (or only contains it???)
    // * the element classCode only contains "60591-5
    // see gemSpec_NCPeH_FD#TUC_NCPeH_004: Cross Gateway Query Response versenden
  }

  @Dann("erhält der LE-EU eine Liste mit zwei Dokumentreferenzen zurück")
  public void leEuReceivesDocumentListWithTwoDocumentReferences() {

    final var leEuActor = OnStage.theActorInTheSpotlight();
    and(leEuActor).attemptsTo(Ensure.that(new GetNumberOfPatientSummaryDocuments()).isEqualTo(2L));
  }

  @Dann("^der LE-EU erhält einen FindDocuments RegistryError mit errorCode (.+)$")
  public void leEuReceivesFindDocumentRegistryErrorWithErrorcode(final String errorCode) {
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
  public void leEuIsBusyForMinutes(final int minutes) {
    try {
      TimeUnit.MINUTES.sleep(minutes);
    } catch (final InterruptedException e) {
      // Handle the interruption
      Thread.currentThread().interrupt();
      log.warn("Thread was interrupted, failed to complete operation", e);
    }
  }
}
