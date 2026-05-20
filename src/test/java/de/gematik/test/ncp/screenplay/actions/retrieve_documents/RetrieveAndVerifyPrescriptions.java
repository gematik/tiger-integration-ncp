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

package de.gematik.test.ncp.screenplay.actions.retrieve_documents;

import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.EPED_TESTACTOR_NAME;
import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.actions.testactor.VerifyRetrievedCda1Prescriptions;
import de.gematik.test.ncp.screenplay.actions.testactor.VerifyRetrievedCda3Prescriptions;
import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import de.gematik.test.ncp.screenplay.questions.xca.find_documents.FindDocumentsResponseData;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.RetrieveDocumentsResponseData;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.RetrievedDocuments;
import de.gematik.test.ncp.util.IheUtils;
import java.util.Objects;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actors.OnStage;
import org.hamcrest.Matchers;

/**
 * Action to retrieve prescriptions with a specified CDA level and verify that the retrieved data
 * matches the expected data based on the patient information and the prescriptions that were issued
 * during the test
 */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class RetrieveAndVerifyPrescriptions implements Performable {

  private final PrescriptionCdaLevel cdaLevel;
  private final int nPrescriptionsToRetrieve;

  @Step("{0} retrieves prescriptions with CDA level #cdaLevel and verifies the retrieved data")
  @Override
  public <T extends Actor> void performAs(final T actor) {
    var patient = actor.asksFor(CurrentPatient.asActor());
    var patientRecord = actor.asksFor(CurrentPatient.patientRecord());
    var foundDocuments = FindDocumentsResponseData.from(patientRecord.getPrescriptionMetadata());

    Set<String> targetUids;
    int prescriptionCount;

    if (nPrescriptionsToRetrieve > 0) {
      targetUids =
          actor.asksFor(
              foundDocuments.mostRecentDocumentUidsForLevel(nPrescriptionsToRetrieve, cdaLevel));
      prescriptionCount = nPrescriptionsToRetrieve;
    } else {
      targetUids = actor.asksFor(foundDocuments.documentUidsMatchingCdaLevel(cdaLevel));
      prescriptionCount =
          patient.usingAbilityTo(ProvidePatientData.class).getEuRedeemablePrescriptions().size();
    }

    givenThat(actor).wasAbleTo(RetrievePrescriptions.byId(targetUids));
    verifyPrescriptions(actor, prescriptionCount);
    OnStage.stage().shineSpotlightOn(actor.getName());
  }

  private <T extends Actor> void verifyPrescriptions(final T actor, final int prescriptionCount) {
    var patient = actor.asksFor(CurrentPatient.asActor());
    var patientRecord = actor.asksFor(CurrentPatient.patientRecord());
    var patientData = patient.usingAbilityTo(ProvidePatientData.class);

    var retrievedDocuments = RetrievedDocuments.from(patientRecord.getPrescriptionsByDocumentId());
    actor.should(seeThat(retrievedDocuments.documentCount(), Matchers.equalTo(prescriptionCount)));

    var retrievedMatchingLevel = retrievedDocuments.documentUidsMatchingCdaLevel(cdaLevel);
    and(actor).should(seeThat(retrievedMatchingLevel, Matchers.hasSize(prescriptionCount)));

    var testactor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    var verifyPrescriptionsWithStrategyForCdaLevel =
        PrescriptionCdaLevel.LEVEL_1.equals(cdaLevel)
            ? VerifyRetrievedCda1Prescriptions.withPatientRecord(patientRecord)
            : VerifyRetrievedCda3Prescriptions.byMatchingDataFrom(patientData, patientRecord);
    and(testactor).wasAbleTo(verifyPrescriptionsWithStrategyForCdaLevel);

    var response =
        RetrieveDocumentsResponseData.from(patientRecord.getRetrievePrescriptionsResponse());
    and(actor).should(seeThat(response.status(), Matchers.equalTo(IheUtils.SUCCESS_RESPONSE)));
    and(actor).should(seeThat(response.errorCodes(), Matchers.empty()));
    //    TODO: uncomment when NCPeH simulation is available
    //    var prescriptionIds = actor.asksFor(RetrievedPrescriptions.ids());
    //    for (String id : prescriptionIds) {
    //      patient.should(
    //          seeThat(ErpFdvData.statusOfPrescription(id),
    // Matchers.equalTo(StatusEnum.IN_PROGRESS)));
    //    }
  }

  /**
   * Factory method to create a new instance of {@link RetrieveAndVerifyPrescriptions} with the
   * specified CDA level.
   *
   * @param cdaLevel the CDA level to be used for retrieving prescriptions
   * @return a new instance of {@link RetrieveAndVerifyPrescriptions} with the specified CDA level
   */
  public static RetrieveAndVerifyPrescriptions withCdaLevel(final PrescriptionCdaLevel cdaLevel) {
    return new RetrieveAndVerifyPrescriptions(Objects.requireNonNull(cdaLevel), 0);
  }

  /**
   * Creates a task that retrieves the most recent N prescriptions for the specified CDA level and
   * verifies them.
   *
   * @param n Number of prescriptions to retrieve, starting with the most recent
   * @param cdaLevel the CDA level to be used for retrieving prescriptions
   * @return a new instance of {@link RetrieveAndVerifyPrescriptions} with the specified CDA level
   *     and number of prescriptions to verify
   */
  public static RetrieveAndVerifyPrescriptions mostRecentNForCdaLevel(
      final int n, final PrescriptionCdaLevel cdaLevel) {
    return new RetrieveAndVerifyPrescriptions(Objects.requireNonNull(cdaLevel), n);
  }
}
