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

import static de.gematik.test.ncp.utils.TestUtils.loadFromJsonResource;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class IdentifyPatientRequestBuilderTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  @Test
  void build() {
    // Arrange
    final var builder =
        IdentifyPatientRequestBuilder.newInstance()
            .accessCodeAssigningAuthority("1.2.276.0.76.4.298")
            .euCountryCode(EuCountryCode.AUSTRIA)
            .idaAssertionProfileName("ida")
            .accessCode("ABC123")
            .kvnr("X123456789");

    // Act
    final var testee = assertDoesNotThrow(builder::build, "Method build threw exception");

    // Assert
    log.info(mapper.writeValueAsString(testee));
    final var expected =
        loadFromJsonResource(
            IdentifyPatientRequest.class,
            this.getClass(),
            "IdentifyPatientRequestBuilderTest.json");
    assertEquals(expected, testee);
  }
}
