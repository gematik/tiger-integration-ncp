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

package de.gematik.test.ncp.screenplay.questions.xca.retrieve_documents;

import de.gematik.test.ncp.util.ClinicalDocumentInformationProvider;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Question;
import org.hl7.v3.ClinicalDocument;

/** Questions to query data from a ClinicalDocument */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClinicalDocumentData {

  private final ClinicalDocument clinicalDocument;

  /**
   * Factory method to create an instance of ClinicalDocumentDataQuestions for a given
   * ClinicalDocument
   *
   * @param clinicalDocument the ClinicalDocument to query
   * @return an instance of ClinicalDocumentDataQuestions for the given ClinicalDocument
   */
  public static ClinicalDocumentData from(final ClinicalDocument clinicalDocument) {
    return new ClinicalDocumentData(clinicalDocument);
  }

  /**
   * Question to query the patient's date of birth from the ClinicalDocument
   *
   * @return a Question that can be answered by an Actor to get the patient's date of birth from the
   *     ClinicalDocument
   */
  public Question<LocalDate> patientDateOfBirth() {
    return Question.about(getSubjectDescription("date of birth"))
        .answeredBy(
            actor -> ClinicalDocumentInformationProvider.getPatientDateOfBirth(clinicalDocument));
  }

  /**
   * Question to query the patient's first name from the ClinicalDocument
   *
   * @return a Question that can be answered by an Actor to get the patient's first name from the
   *     ClinicalDocument
   */
  public Question<String> patientFirstName() {
    return Question.about(getSubjectDescription("first name"))
        .answeredBy(
            actor ->
                ClinicalDocumentInformationProvider.getPatientName(clinicalDocument).givenNames());
  }

  /**
   * Question to query the patient's last name from the ClinicalDocument
   *
   * @return a Question that can be answered by an Actor to get the patient's last name from the
   *     ClinicalDocument
   */
  public Question<String> patientLastName() {
    return Question.about(getSubjectDescription("last name"))
        .answeredBy(
            actor ->
                ClinicalDocumentInformationProvider.getPatientName(clinicalDocument).lastNames());
  }

  /**
   * Question to query the patient's name prefix from the ClinicalDocument
   *
   * @return a Question that can be answered by an Actor to get the patient's name prefix from the
   *     ClinicalDocument
   */
  public Question<String> patientPrefix() {
    return Question.about(getSubjectDescription("name prefix"))
        .answeredBy(
            actor -> ClinicalDocumentInformationProvider.getPatientName(clinicalDocument).titles());
  }

  private static String getSubjectDescription(final String subject) {
    return "the patient's " + subject + " stated in the clinical document";
  }
}
