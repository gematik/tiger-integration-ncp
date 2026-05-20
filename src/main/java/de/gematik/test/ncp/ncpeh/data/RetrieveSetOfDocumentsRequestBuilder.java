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

package de.gematik.test.ncp.ncpeh.data;

import de.gematik.ncpeh.api.common.BaseAssertionProfile;
import de.gematik.ncpeh.api.common.TrcAssertionProfile;
import de.gematik.ncpeh.api.request.DocumentRequest;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import de.gematik.ncpeh.api.request.RetrieveSetOfDocumentsRequest;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Builder to build a request for the {@link
 * de.gematik.ncpeh.api.NcpehSimulatorApi#retrieveDocument(RetrieveDocumentRequest)} operation.<br>
 * Note: Thus far only mandatory fields are set. Further fields might need to be added in the
 * future.
 */
@Accessors(fluent = true)
@Data(staticConstructor = "newInstance")
@EqualsAndHashCode(callSuper = true)
public final class RetrieveSetOfDocumentsRequestBuilder
    extends RequestBaseBuilder<RetrieveSetOfDocumentsRequest> {

  private String trcAssertionProfileName;
  private Set<DocumentRequest> documentRequestSet;

  @Override
  public RetrieveSetOfDocumentsRequest build() {
    return new RetrieveSetOfDocumentsRequest(
        super.buildRequestBase(),
        new TrcAssertionProfile(
            new BaseAssertionProfile(trcAssertionProfileName, null, null, null, null), null, null),
        super.buildPatientId(),
        accessCode(),
        documentRequestSet);
  }
}
