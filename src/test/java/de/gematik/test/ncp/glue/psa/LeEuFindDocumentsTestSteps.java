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

package de.gematik.test.ncp.glue.psa;

import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import de.gematik.test.ncp.screenplay.actions.ConfirmThatSearchForPatientSummaryFails;
import de.gematik.test.ncp.screenplay.actions.FindDocuments;
import de.gematik.test.ncp.screenplay.questions.FoundPatientSummaryDocument;
import de.gematik.test.ncp.screenplay.questions.GetNumberOfPatientSummaryDocuments;
import de.gematik.test.ncp.screenplay.questions.GetRegistryErrorCodesFromAdhocQueryResponse;
import de.gematik.test.ncp.screenplay.questions.IsFindPatientSummaryResponseStatusFailure;
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

  private static final String ERROR_NO_CONSENT = "ERROR_NO_CONSENT";

  @Und(
      "der LE-EU hat erfolgreich eine Suche des Patient Summary der versicherten Person durchgeführt")
  @Und(
      "der LE-EU führt danach erfolgreich eine Suche des Patient Summary der versicherten Person durch")
  public void leEuFindsTwoDocumentReferencesForPatientSummary() {
    // kompletter Positiv-Ablauf von FindDocuments und den Prüfungen

    final var leEuActor = OnStage.theActorInTheSpotlight();
    leEuActor.attemptsTo(FindDocuments.forPsa());
    and(leEuActor).attemptsTo(Ensure.that(new GetNumberOfPatientSummaryDocuments()).isEqualTo(2L));
    and(leEuActor).attemptsTo(Ensure.that(FoundPatientSummaryDocument.ofCdaLevel(1)).isTrue());
    and(leEuActor).attemptsTo(Ensure.that(FoundPatientSummaryDocument.ofCdaLevel(3)).isTrue());
  }

  @Dann(
      "^kann der LE-EU (.+) aus dem EU-Land (.+) keine erfolgreiche Suche nach dem Patient Summary der versicherten Person durchführen$")
  public void leEuFailsToGetDocumentReferences(final String leEuName, final String euCountry) {
    // First LE-EU actor with outdated accesscode tries to search for Patient summary of the patient
    // The FindDocument operation of the given LE-EU must fail
    // expected error: IHE Registry Error with errorCode "ERROR_NO_CONSENT"
    //   see "der LE-EU erhält einen FindDocuments RegistryError mit errorCode ERROR_NO_CONSENT"

    final var leEuActor = OnStage.theActorCalled(leEuName);
    leEuActor.attemptsTo(FindDocuments.forPsa());
    and(leEuActor)
        .attemptsTo(ConfirmThatSearchForPatientSummaryFails.withErrorCode(ERROR_NO_CONSENT));
  }

  @Wenn("^der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft$")
  @Und(
      "^danach der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft$")
  public void leEuStartsFindDocuments() {

    final var leEuActor = OnStage.theActorInTheSpotlight();
    when(leEuActor).attemptsTo(FindDocuments.forPsa());
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
    and(leEuActor)
        .attemptsTo(Ensure.that(FoundPatientSummaryDocument.ofCdaLevel(cdaLevel)).isTrue());
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

    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor)
        .attemptsTo(Ensure.that(new IsFindPatientSummaryResponseStatusFailure()).isTrue());
    then(leEuActor)
        .attemptsTo(
            Ensure.that(new GetRegistryErrorCodesFromAdhocQueryResponse()).contains(errorCode));
  }

  @Dann("erhält der LE-EU keine Liste von Dokumentreferenzen")
  public void leEuReceivesFindDocumentResponseErrorWithoutDocumentlist() {
    // verify, that the find document response contains no list with document references
    final var leEuActor = OnStage.theActorInTheSpotlight();
    then(leEuActor).attemptsTo(Ensure.that(new GetNumberOfPatientSummaryDocuments()).isEqualTo(0L));
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
