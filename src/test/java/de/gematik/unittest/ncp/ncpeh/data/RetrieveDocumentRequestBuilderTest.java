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

package de.gematik.unittest.ncp.ncpeh.data;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.ncpeh.data.RetrieveDocumentRequestBuilder;
import org.junit.jupiter.api.Test;

class RetrieveDocumentRequestBuilderTest {

  public static final String REPO_UNIQUE_ID = "312.6.23.90.3.0";

  public static final String HOME_COMMUNITY_ID = "312.6.23.90.3.1";

  public static final String DOC_UNIQUE_ID = "1.34.56.356.5464.9";

  public static final String SECOND_DOC_UNIQUE_ID = "1.34.56.356.5464.8";

  public static final String TRC_APN = "Trc";

  @Test
  void build() {
    var tstObj =
        RetrieveDocumentRequestBuilder.newInstance()
            .repositoryUniqueId(REPO_UNIQUE_ID)
            .homeCommunityId(HOME_COMMUNITY_ID)
            .documentUniqueId(DOC_UNIQUE_ID)
            .documentUniqueId(SECOND_DOC_UNIQUE_ID)
            .trcAssertionProfileName(TRC_APN)
            .euCountryCode(EuCountryCode.BULGARIA)
            .idaAssertionProfileName("ida")
            .kvnr("X124250984")
            .accessCode("ABC123");

    var result = assertDoesNotThrow(tstObj::build, "Method build threw exception");

    assertEquals(
        REPO_UNIQUE_ID,
        result.repositoryUniqueId(),
        "Repository unique ID does not have the expected value");
    assertEquals(
        HOME_COMMUNITY_ID,
        result.homeCommunityId(),
        "Home community ID does not have the expected value");
    assertEquals(
        SECOND_DOC_UNIQUE_ID,
        result.documentUniqueId(),
        "Document unique ID does not have the expected value");
    assertEquals(
        DOC_UNIQUE_ID,
        result.additionalDocumentUniqueId(),
        "Additional Document unique ID does not have the expected value");
    assertEquals(
        TRC_APN,
        result.trcAssertionProfile().assertionProperties().profileName(),
        "TRC assertion profile name does not have the expected value");
  }
}
