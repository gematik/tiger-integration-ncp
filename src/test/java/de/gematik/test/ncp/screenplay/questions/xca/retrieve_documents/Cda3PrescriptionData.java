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

import de.gematik.ncpeh.ehdsi.valuesets.EhdsiSubstitutionCode;
import de.gematik.test.ncp.util.PrescriptionUtils;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Question;
import org.hl7.v3.ClinicalDocument;

/** Question factory for extracting prescription-related data from a CDA3 ClinicalDocument */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Cda3PrescriptionData {

  private final ClinicalDocument clinicalDocument;

  /**
   * Factory method to create a Cda3PrescriptionData for a given ClinicalDocument
   *
   * @param clinicalDocument the ClinicalDocument to extract prescription data from
   * @return a Cda3PrescriptionData instance for the given ClinicalDocument
   */
  public static Cda3PrescriptionData from(final ClinicalDocument clinicalDocument) {
    return new Cda3PrescriptionData(clinicalDocument);
  }

  /**
   * Checks if the prescription allows substitution based on the value of the substitution code
   * provided via an {@code <entryRelationship>} element.
   *
   * @return a Question that answers whether substitution is allowed for this prescription
   * @see <a
   *     href="https://art-decor.ehdsi.eu/ad/#/epsos-/rules/templates/1.3.6.1.4.1.12559.11.10.1.3.1.3.2/2023-07-03T13:52:39">eHDSI
   *     Substance Administration</a>
   */
  public Question<Boolean> allowsSubstitution() {
    return Question.about("the prescription allows substitution")
        .answeredBy(
            ignored ->
                Optional.ofNullable(
                        PrescriptionUtils.getSubstitutionValueFromCda3(clinicalDocument))
                    // substitution is allowed unless the code "N" is explicitly specified
                    .map(value -> !EhdsiSubstitutionCode.NONE.getCode().equals(value.getCode()))
                    .orElse(true));
  }
}
