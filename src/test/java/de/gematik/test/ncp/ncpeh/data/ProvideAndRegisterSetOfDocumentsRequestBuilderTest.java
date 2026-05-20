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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.common.PrescriptionDispenseData;
import de.gematik.ncpeh.api.request.ProvideAndRegisterSetOfDocumentsRequest;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ProvideAndRegisterSetOfDocumentsRequestBuilderTest {

  private final ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  @Test
  void build() {
    // Arrange
    var p1 = new PrescriptionDispenseData("aid", true);
    var p2 = new PrescriptionDispenseData("bid", false);

    final var builder =
        ProvideAndRegisterSetOfDocumentsRequestBuilder.newInstance()
            .trcAssertionProfileName("Trc")
            .formatCode("dummyformat")
            .typeCode("dummytype")
            .classCode("dummyclass")
            .dispensations(List.of(p1, p2))
            .kvnr("X123456789")
            .accessCode("ABC123")
            .euCountryCode(EuCountryCode.BELGIUM)
            .idaAssertionProfileName("ida");

    // Act
    final var testee = assertDoesNotThrow(builder::build, "Method build threw exception");

    // Assert
    log.info(mapper.writeValueAsString(testee));
    final var expected =
        loadFromJsonResource(
            ProvideAndRegisterSetOfDocumentsRequest.class,
            this.getClass(),
            "ProvideAndRegisterSetOfDocumentsRequestBuilderTest.json");
    assertEquals(expected, testee);
  }
}
