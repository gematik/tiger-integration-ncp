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

package de.gematik.test.ncp.screenplay.questions.practitioner;

import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.NcpehInterfaceResponse;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Question;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegistryResponseData {

  private final RegistryResponseType registryResponse;

  public static RegistryResponseData fromNcpehResponse(
      final NcpehInterfaceResponse<?, RegistryResponseType> ncpehResponse) {
    return new RegistryResponseData(ncpehResponse.ncpehFdResponseContent());
  }

  public Question<String> status() {
    return Question.about("the status of the RegistryResponse")
        .answeredBy(
            actor ->
                Optional.ofNullable(registryResponse)
                    .map(RegistryResponseType::getStatus)
                    .orElse(null));
  }

  public Question<Collection<String>> errorCodes() {
    return Question.about("the list of error codes contained in the RegistryResponse")
        .answeredBy(
            actor ->
                Optional.ofNullable(registryResponse)
                    .map(DataUtils::readRegistryErrorCodesFromRegistryResponse)
                    .orElseGet(List::of));
  }
}
