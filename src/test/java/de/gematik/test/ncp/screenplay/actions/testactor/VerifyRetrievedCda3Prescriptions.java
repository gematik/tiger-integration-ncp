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

package de.gematik.test.ncp.screenplay.actions.testactor;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.data.record.PatientRecord;
import de.gematik.test.ncp.screenplay.abilities.RememberAuthoredPrescriptions;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.Cda3PrescriptionData;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.ClinicalDocumentData;
import de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents.RequestedDocuments;
import de.gematik.test.ncp.util.PrescriptionUtils;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Question;
import org.hamcrest.Matchers;

/**
 * Action to verify that retrieved prescriptions match the expected data based on the patient
 * information and the prescriptions that were issued during the test
 */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class VerifyRetrievedCda3Prescriptions implements Performable {

  private final Patient patient;
  private final PatientRecord patientRecord;

  /**
   * Factory method to create a VerifyRetrievedPrescriptions action based on a Patient and their
   * PatientRecord
   */
  public static VerifyRetrievedCda3Prescriptions byMatchingDataFrom(
      final Patient patient, final PatientRecord patientRecord) {
    return new VerifyRetrievedCda3Prescriptions(patient, patientRecord);
  }

  @Step("{0} verifies that the retrieved prescriptions match the expected data")
  @Override
  public <T extends Actor> void performAs(final T actor) {
    var requestSet = patientRecord.getRequestedDocuments();
    var retrievedPrescriptions = patientRecord.getPrescriptionsByDocumentId();
    var issuedPrescriptions =
        actor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions();

    retrievedPrescriptions.entrySet().stream()
        // filter CDA 3 prescriptions by format identifier
        .filter(entry -> PrescriptionCdaLevel.LEVEL_3.appliesTo(entry.getKey()))
        .forEach(
            entry -> {
              var uid = entry.getKey();
              var document = entry.getValue();
              var documentDataFor = ClinicalDocumentData.from(document);
              var presId = PrescriptionUtils.extractPrescriptionIdFromDocumentUid(uid);
              var substitutionAllowedByPrescriber =
                  issuedPrescriptions.get(presId).getMedicationRequest().isSubstitutionAllowed();

              // uid of received prescription is present in document request
              actor.should(seeThat(RequestedDocuments.from(requestSet).contain(uid)));

              // uid matches expected pattern
              actor.should(
                  seeThat(
                      Question.about("the document UID " + uid).answeredBy(a -> uid),
                      Matchers.matchesPattern(
                          PrescriptionUtils.XDS_DOCUMENT_ENTRY_UNIQUE_ID_EPED_PATTERN)));

              // patient name in prescription matches name in identification data
              actor.should(
                  seeThat(
                      documentDataFor.patientPrefix(),
                      Matchers.equalToIgnoringCase(patient.name().titles())));
              actor.should(
                  seeThat(
                      documentDataFor.patientFirstName(),
                      Matchers.equalToIgnoringCase(patient.name().givenNames())));
              actor.should(
                  seeThat(
                      documentDataFor.patientLastName(),
                      Matchers.equalToIgnoringCase(patient.name().lastNames())));

              // patient birthdate matches birthdate in identification data
              actor.should(
                  seeThat(
                      documentDataFor.patientDateOfBirth(), Matchers.equalTo(patient.birthDate())));

              // substitution flag matches prescriber instructions
              actor.should(
                  seeThat(
                      Cda3PrescriptionData.from(document).allowsSubstitution(),
                      Matchers.equalTo(substitutionAllowedByPrescriber)));
            });
  }
}
