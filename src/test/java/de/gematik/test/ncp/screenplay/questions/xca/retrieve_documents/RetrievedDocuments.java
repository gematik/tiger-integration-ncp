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

import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Question;
import org.hl7.v3.ClinicalDocument;

/** Questions about the documents retrieved from the document repository */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RetrievedDocuments {

  final Map<String, ClinicalDocument> documentsById;

  /**
   * Factory method to create a new instance of {@link RetrievedDocuments}
   *
   * @param documentsById a map of document IDs to the corresponding ClinicalDocument objects
   *     retrieved from the document repository
   * @return a new instance of {@link RetrievedDocuments} initialized with the provided documents
   */
  public static RetrievedDocuments from(final Map<String, ClinicalDocument> documentsById) {
    return new RetrievedDocuments(documentsById);
  }

  /**
   * Returns a question for checking the number of documents retrieved from the document repository
   */
  public Question<Integer> documentCount() {
    return Question.about("the number of documents retrieved from the document repository")
        .answeredBy(actor -> documentsById.size());
  }

  public Question<Set<String>> documentUidsMatchingCdaLevel(final PrescriptionCdaLevel level) {
    return Question.about(
            "the unique IDs referring to the CDA level %d representation of retrieved prescriptions"
                .formatted(level.getIntValue()))
        .answeredBy(
            actor ->
                documentsById.keySet().stream()
                    .filter(level::appliesTo)
                    .collect(Collectors.toSet()));
  }
}
