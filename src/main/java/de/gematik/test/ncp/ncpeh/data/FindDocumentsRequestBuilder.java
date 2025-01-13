/*
 * Copyright (c) 2024-2025 gematik GmbH
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

import static de.gematik.test.ncp.util.IheUtils.STATUS_APPROVED;

import de.gematik.ncpeh.api.common.BaseAssertionProfile;
import de.gematik.ncpeh.api.common.TrcAssertionProfile;
import de.gematik.ncpeh.api.request.FindDocumentsRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Builder to build a request for the {@link
 * de.gematik.ncpeh.api.NcpehSimulatorApi#findDocuments(FindDocumentsRequest)} operation.<br>
 * Note: Thus far only mandatory fields are set. Further fields might need to be added in the
 * future.
 */
@Accessors(fluent = true)
@Data(staticConstructor = "newInstance")
@EqualsAndHashCode(callSuper = true)
public final class FindDocumentsRequestBuilder extends RequestBaseBuilder<FindDocumentsRequest> {

  private String trcAssertionProfileName;

  private static final String XDS_DOCUMENT_FORMAT_STRING = "('%s')";
  private static final String XDS_DOCUMENT_ENTRY_CLASS_CODE = "60591-5^^2.16.840.1.113883.6.1";
  private static final String XDS_DOCUMENT_ENTRY_CLASS_CODE_VALUE =
      String.format(XDS_DOCUMENT_FORMAT_STRING, XDS_DOCUMENT_ENTRY_CLASS_CODE);
  private static final String XDS_DOCUMENT_ENTRY_STATUS_VALUE =
      String.format(XDS_DOCUMENT_FORMAT_STRING, STATUS_APPROVED);

  @Override
  public FindDocumentsRequest build() {
    return new FindDocumentsRequest(
        super.buildRequestBase(),
        new TrcAssertionProfile(
            new BaseAssertionProfile(trcAssertionProfileName, null, null, null, null), null),
        super.buildPatientId(),
        accessCode(),
        XDS_DOCUMENT_ENTRY_CLASS_CODE_VALUE,
        XDS_DOCUMENT_ENTRY_STATUS_VALUE,
        null);
  }
}
