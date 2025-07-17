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

package de.gematik.test.ncp.data;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PatientTest {

  @Test
  void samePersonTest() {
    // Arrange
    final Patient patient1 =
        new PatientImpl(
            PersonName.fromString("Dr. John Doedeldie"), "X190471029", LocalDate.of(1987, 11, 23));
    final Patient patient2 =
        new PatientImpl(
            PersonName.fromString("Dr. John Doedeldie"), "X190471029", LocalDate.of(1987, 11, 23));
    final Patient patient3 =
        new PatientImpl(
            PersonName.fromString("Dr. Jane Doe"), "X190471030", LocalDate.of(1990, 5, 15));

    // Act & Assert
    assertTrue(patient1.samePerson(patient2)); // Same person
    assertFalse(patient1.samePerson(patient3)); // Different person
    assertFalse(patient1.samePerson(null)); // Null check
  }

  @Test
  void toStringTest() {
    // Arrange
    final Patient patient =
        new PatientImpl(
            PersonName.fromString("Dr. John Doedeldie"), "X190471029", LocalDate.of(1987, 11, 23));

    // Act
    final String result = patient.toString();

    // Assert
    assertEquals("Name: Dr. John Doedeldie; KVNR: X190471029; Birth Date: 1987-11-23", result);
  }
}
