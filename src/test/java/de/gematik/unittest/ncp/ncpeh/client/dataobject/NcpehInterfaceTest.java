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

package de.gematik.unittest.ncp.ncpeh.client.dataobject;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.ncpeh.NcpehInterface;
import de.gematik.test.ncp.ncpeh.NcpehInterface.PatientSummaryLevel;
import org.junit.jupiter.api.Test;

class NcpehInterfaceTest {

  private static final String DOC_UNIQUE_ID = "2.25.23827589^PS.XML";

  private static final Integer LEVEL = 1;

  @Test
  void patientSummaryLevelFromDocumentUniqueIdTest() {
    var result =
        assertDoesNotThrow(
            () -> NcpehInterface.PatientSummaryLevel.fromDocumentUniqueId(DOC_UNIQUE_ID));

    assertNotNull(result);
    assertEquals(PatientSummaryLevel.LEVEL_3, result);
  }

  @Test
  void patientSummaryLevelFromDocumentUniqueIdThrowsTest() {
    var invalidDocId = "2.25.12436";

    var result =
        assertThrows(
            IllegalArgumentException.class,
            () -> NcpehInterface.PatientSummaryLevel.fromDocumentUniqueId(invalidDocId));

    assertTrue(result.getMessage().contains(invalidDocId));
  }

  @Test
  void patientSummaryLevelFromValueTest() {
    var result = assertDoesNotThrow(() -> NcpehInterface.PatientSummaryLevel.fromValue(LEVEL));

    assertNotNull(result);
    assertEquals(PatientSummaryLevel.LEVEL_1, result);
  }

  @Test
  void patientSummaryLevelFromValueThrowsTest() {
    Integer invalidLevel = 2;

    var result =
        assertThrows(
            IllegalArgumentException.class,
            () -> NcpehInterface.PatientSummaryLevel.fromValue(invalidLevel));

    assertTrue(result.getMessage().contains(invalidLevel.toString()));
  }
}
