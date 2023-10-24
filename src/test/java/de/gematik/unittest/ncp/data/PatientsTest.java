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

package de.gematik.unittest.ncp.data;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.Patients;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.screenplay.PatientActor;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PatientsTest {

  private static final String name = "Mr. Dude Mate";

  private static final String kvnr = "X190471028";

  private static final Patient patient = new PatientActor(name, kvnr, LocalDate.of(1987, 11, 23));

  private final Patients tstObj = new Patients();

  @BeforeEach
  void init() {
    tstObj.add(patient);
  }

  @Test
  void findByNameTest() {
    var tstResult = assertDoesNotThrow(() -> tstObj.findByName(PersonName.fromString(name)));
    assertNotNull(tstResult);
    assertEquals(patient, tstResult);
  }

  @Test
  void findByNameFailTest() {
    var _name = name + "n";
    var personName = PersonName.fromString(_name);
    var result =
        assertThrows(TigerConfigurationException.class, () -> tstObj.findByName(personName));
    assertTrue(result.getMessage().contains(_name));
  }

  @Test
  void findByKvnrTest() {
    var tstResult = assertDoesNotThrow(() -> tstObj.findByKvnr(kvnr));
    assertNotNull(tstResult);
    assertEquals(patient, tstResult);
  }

  @Test
  void findByKvnrFailTest() {
    var _kvnr = kvnr.replace("8", "3");
    var result = assertThrows(TigerConfigurationException.class, () -> tstObj.findByKvnr(_kvnr));
    assertTrue(result.getMessage().contains(_kvnr));
  }
}
