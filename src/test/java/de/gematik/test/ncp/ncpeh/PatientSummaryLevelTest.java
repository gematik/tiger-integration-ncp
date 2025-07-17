/*
 * Copyright 2024-2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.ncpeh;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PatientSummaryLevelTest {

  private static final String DOC_UNIQUE_ID = "2.25.23827589^PS.XML";

  private static final Integer LEVEL = 1;

  @Test
  void patientSummaryLevelFromDocumentUniqueIdTest() {
    // Arrange
    // Act
    final var result =
        assertDoesNotThrow(() -> PatientSummaryLevel.fromDocumentUniqueId(DOC_UNIQUE_ID));

    // Assert
    assertNotNull(result);
    assertEquals(PatientSummaryLevel.LEVEL_3, result);
  }

  @Test
  void patientSummaryLevelFromDocumentUniqueIdThrowsTest() {
    // Arrange
    final var invalidDocId = "2.25.12436";

    // Act
    final var result =
        assertThrows(
            IllegalArgumentException.class,
            () -> PatientSummaryLevel.fromDocumentUniqueId(invalidDocId));

    // Assert
    assertTrue(result.getMessage().contains(invalidDocId));
  }

  @Test
  void patientSummaryLevelFromValueTest() {
    // Arrange
    // Act
    final var result = assertDoesNotThrow(() -> PatientSummaryLevel.fromValue(LEVEL));

    // Assert
    assertNotNull(result);
    assertEquals(PatientSummaryLevel.LEVEL_1, result);
  }

  @Test
  void patientSummaryLevelFromValueThrowsTest() {
    // Arrange
    final Integer invalidLevel = 2;

    // Act
    final var result =
        assertThrows(
            IllegalArgumentException.class, () -> PatientSummaryLevel.fromValue(invalidLevel));

    // Assert
    assertTrue(result.getMessage().contains(invalidLevel.toString()));
  }
}
