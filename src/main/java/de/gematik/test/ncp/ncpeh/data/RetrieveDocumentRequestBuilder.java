/*
 * Copyright 2023 gematik GmbH
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
 */

package de.gematik.test.ncp.ncpeh.data;

import de.gematik.ncpeh.api.common.BaseAssertionProfile;
import de.gematik.ncpeh.api.common.TrcAssertionProfile;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
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
public class RetrieveDocumentRequestBuilder extends RequestBaseBuilder<RetrieveDocumentRequest> {

  private String repositoryUniqueId;

  private String documentUniqueId;

  @Setter(AccessLevel.PROTECTED)
  private String additionalDocumentUniqueId;

  private String homeCommunityId;

  private String trcAssertionProfileName;

  @Override
  public RetrieveDocumentRequest build() {
    return new RetrieveDocumentRequest(
        super.buildRequestBase(),
        new TrcAssertionProfile(
            new BaseAssertionProfile(trcAssertionProfileName, null, null, null, null), null),
        super.buildPatientId(),
        accessCode(),
        repositoryUniqueId(),
        documentUniqueId(),
        additionalDocumentUniqueId(),
        homeCommunityId());
  }

  public RetrieveDocumentRequestBuilder documentUniqueId(@NonNull String docUniqueId) {
    Optional.ofNullable(documentUniqueId).ifPresent(duid -> additionalDocumentUniqueId = duid);
    documentUniqueId = docUniqueId;
    return this;
  }
}
