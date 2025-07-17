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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PersonNameTest {

  @ParameterizedTest
  @CsvSource({
    "Dr., John, Doe, Dr. John Doe",
    "Mr., Jane, Smith, Mr. Jane Smith",
    ", John, Doe, John Doe",
    "Dr., , Doe, Dr. Doe",
    "Dr., John, , Dr. John",
    ", John, , John"
  })
  void testToString(
      final String titles, final String givenNames, final String lastNames, final String expected) {
    // Arrange
    final PersonName personName =
        new PersonName().titles(titles).givenNames(givenNames).lastNames(lastNames);

    // Act
    // Assert
    assertEquals(expected, personName.toString());
  }

  @Test
  void testFromString() {
    // Arrange
    final String name = "Dr. John Doe";

    // Act
    final PersonName personName = PersonName.fromString(name);

    // Assert
    assertEquals("Dr.", personName.titles());
    assertEquals("John", personName.givenNames());
    assertEquals("Doe", personName.lastNames());
  }
}
