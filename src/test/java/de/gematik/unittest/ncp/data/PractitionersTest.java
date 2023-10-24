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

import de.gematik.test.ncp.data.Practitioner;
import de.gematik.test.ncp.data.Practitioners;
import de.gematik.test.ncp.screenplay.LeiActor;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PractitionersTest {

  private static final String name = "Dr. Dude Mate";

  private static final Practitioner practitioner = new LeiActor(name);

  private final Practitioners tstObj = new Practitioners();

  @BeforeEach
  void setUp() {
    tstObj.add(practitioner);
  }

  @Test
  void findByName() {
    var tstResult = assertDoesNotThrow(() -> tstObj.findByName(name));
    assertNotNull(tstResult);
    assertEquals(practitioner, tstResult);
  }

  @Test
  void findByNameFail() {
    assertThrows(TigerConfigurationException.class, () -> tstObj.findByName(name + "n"));
  }
}
