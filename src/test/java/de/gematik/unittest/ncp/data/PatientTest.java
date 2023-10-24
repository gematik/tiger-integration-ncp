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
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.screenplay.PatientActor;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PatientTest {

  private static final Patient patientExp =
      new PatientActor("Dr. John Doedeldie", "X190471029", LocalDate.of(1987, 11, 23));

  private static final Patient patient1 =
      new PatientImpl(patientExp.name(), patientExp.kvnr(), patientExp.birthDate());

  private static final Patient patient2 =
      new PatientActor(patientExp.name(), "X190471028", patientExp.birthDate());

  @Test
  void samePerson() {
    assertTrue(patientExp.samePerson(patientExp));
    assertFalse(patientExp.samePerson(null));
    assertTrue(patientExp.samePerson(patient1));
    assertFalse(patientExp.samePerson(patient2));
  }

  @Test
  void patientData() {
    var result = assertDoesNotThrow(patient1::patientData);

    assertNotNull(result);
    assertTrue(result.contains(patient1.kvnr()));
    assertTrue(result.contains(patient1.name()));
    assertTrue(result.contains(patient1.birthDate().toString()));
  }
}
