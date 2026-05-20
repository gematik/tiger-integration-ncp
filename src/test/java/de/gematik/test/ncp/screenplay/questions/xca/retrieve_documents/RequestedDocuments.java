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

import de.gematik.ncpeh.api.request.DocumentRequest;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Question;

/**
 * Question factory for questions related to the set of DocumentRequests that were part of the
 * RetrieveDocumentSet request
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestedDocuments {

  private final Set<DocumentRequest> requestedDocuments;

  /**
   * Factory method to create a RequestedDocuments instance from a set of DocumentRequest objects
   *
   * @param requestedDocuments the set of DocumentRequest objects used to answer questions
   * @return a RequestedDocuments instance that can be used to ask questions about the provided set
   *     of DocumentRequest objects
   */
  public static RequestedDocuments from(final Set<DocumentRequest> requestedDocuments) {
    return new RequestedDocuments(requestedDocuments);
  }

  /**
   * Returns a question that checks if a DocumentRequest with the given document unique ID exists
   *
   * @param id the document unique ID to check for
   * @return a Question that can be answered by an Actor to check for the presence of a
   *     DocumentRequest with the specified unique ID
   */
  public Question<Boolean> contain(final String id) {
    return Question.about("for the document UID " + id + " exists a matching DocumentRequest")
        .answeredBy(
            actor -> requestedDocuments.stream().anyMatch(dr -> dr.documentUniqueId().equals(id)));
  }
}
