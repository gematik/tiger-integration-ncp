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

import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrieveDocumentsResponseDTO;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Question;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/** Question factory allowing to query data from a RetrieveDocumentsResponseDTO */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RetrieveDocumentsResponseData {

  private final RetrieveDocumentsResponseDTO retrieveDocumentsResponseDTO;

  /**
   * Factory method to create an instance of ResponseData for a given RetrieveDocumentsResponseDTO
   *
   * @param retrieveDocumentsResponseDTO the RetrieveDocumentsResponseDTO to query
   * @return an instance of ResponseData for the given RetrieveDocumentsResponseDTO
   */
  public static RetrieveDocumentsResponseData from(
      final RetrieveDocumentsResponseDTO retrieveDocumentsResponseDTO) {
    return new RetrieveDocumentsResponseData(retrieveDocumentsResponseDTO);
  }

  /**
   * Creates a Question to retrieve the Response Status of a RetrieveDocumentsResponse
   *
   * @return the Question to retrieve the Response Status
   */
  public Question<String> status() {
    return Question.about("the response status of the RegistryResponse")
        .answeredBy(
            actor ->
                DataUtils.getRegistryResponseType(retrieveDocumentsResponseDTO)
                    .map(RegistryResponseType::getStatus)
                    .orElse(null));
  }

  /**
   * Creates a Question to retrieve the list of error codes from a RetrieveDocumentsResponse
   *
   * @return the Question to retrieve the list of error codes
   */
  public Question<Collection<String>> errorCodes() {
    return Question.about("the list of error codes from the RegistryResponse")
        .answeredBy(
            actor ->
                DataUtils.getRegistryResponseType(retrieveDocumentsResponseDTO)
                    .map(DataUtils::readRegistryErrorCodesFromRegistryResponse)
                    .orElse(null));
  }
}
