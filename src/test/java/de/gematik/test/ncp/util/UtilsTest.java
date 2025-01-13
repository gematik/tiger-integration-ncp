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

package de.gematik.test.ncp.util;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import java.time.LocalDate;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class UtilsTest {

  @Test
  void toStringListElementsTest() {
    final var testdata = new ArrayList<Patient>();
    testdata.add(
        new PatientImpl()
            .name(PersonName.fromString("Max Murks"))
            .kvnr("X124912047")
            .birthDate(LocalDate.now()));
    testdata.add(new PatientImpl().name(PersonName.fromString("Sybille Tille")));

    final var result =
        assertDoesNotThrow(() -> Utils.toStringListElements(testdata, Object::toString));

    assertNotNull(result);
    assertEquals(testdata.size(), result.size());
    assertTrue(
        result.stream()
            .allMatch(str -> testdata.stream().anyMatch(td -> str.equals(td.toString()))));
  }

  @Test
  void toStringListElementsNullTest() {
    final var result = assertDoesNotThrow(() -> Utils.toStringListElements(null, Object::toString));

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
