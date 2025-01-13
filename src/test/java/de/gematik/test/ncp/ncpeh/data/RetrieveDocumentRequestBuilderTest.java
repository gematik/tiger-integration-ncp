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

import static de.gematik.test.ncp.utils.TestUtils.loadFromJsonResource;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class RetrieveDocumentRequestBuilderTest {

  public static final String REPO_UNIQUE_ID = "312.6.23.90.3.0";

  public static final String DOC_UNIQUE_ID = "1.34.56.356.5464.9";

  public static final String SECOND_DOC_UNIQUE_ID = "1.34.56.356.5464.8";

  public static final String TRC_APN = "Trc";

  private final ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  @Test
  void build() {
    // Arrange
    final var builder =
        RetrieveDocumentRequestBuilder.newInstance()
            .repositoryUniqueId(REPO_UNIQUE_ID)
            .documentUniqueId(DOC_UNIQUE_ID)
            .documentUniqueId(SECOND_DOC_UNIQUE_ID)
            .trcAssertionProfileName(TRC_APN)
            .euCountryCode(EuCountryCode.BULGARIA)
            .idaAssertionProfileName("ida")
            .kvnr("X124250984")
            .accessCode("ABC123");

    // Act
    final var testee = assertDoesNotThrow(builder::build, "Method build threw exception");

    // Assert
    log.info(mapper.writeValueAsString(testee));
    final var expected =
        loadFromJsonResource(
            RetrieveDocumentRequest.class,
            this.getClass(),
            "RetrieveDocumentRequestBuilderTest.json");
    assertEquals(expected, testee);
  }
}
