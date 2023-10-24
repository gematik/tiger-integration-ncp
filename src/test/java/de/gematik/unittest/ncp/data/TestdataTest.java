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

import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.screenplay.PatientActor;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestdataTest {

  private final Testdata tstObj = Testdata.instance();

  private static String epkaTemplateConfigKey;

  @BeforeAll
  static void init() {
    epkaTemplateConfigKey =
        Testdata.instance().epkaTemplates().keySet().stream()
            .findFirst()
            .orElseThrow(
                () ->
                    new TigerConfigurationException(
                        "No path to an ePKA template configured in the testdata configuration"));
  }

  @Test
  void loadFileFromPathInConfig() {
    var configKey =
        Testdata.EPKA_TEMPLATES_CONFIG_PATH + Utils.ensureStartsWith(epkaTemplateConfigKey, ".");

    var tstResult = assertDoesNotThrow(() -> tstObj.loadFileFromPathInConfig(configKey));
    assertNotNull(tstResult);
    assertTrue(tstResult.exists());
  }

  @Test
  void createEpkaFromTemplate() {
    var patient = new PatientActor("Dr. John Doedeldie", "X190471029", LocalDate.of(1987, 11, 23));
    var tstResult =
        assertDoesNotThrow(() -> tstObj.createEpkaFromTemplate(patient, epkaTemplateConfigKey));
    assertNotNull(tstResult);

    var tstResultString = new String(tstResult, StandardCharsets.UTF_8);
    assertTrue(tstResultString.contains(patient.name()));
    assertTrue(tstResultString.contains(patient.kvnr()));
    assertTrue(
        tstResultString.contains(patient.birthDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
  }

  @Test
  void createEpkaFromDefaultTemplate() {
    var patient = new PatientActor("Dr. John Doedeldie", "X190471029", LocalDate.of(1987, 11, 23));
    var tstResult = assertDoesNotThrow(() -> tstObj.createEpkaFromTemplate(patient));
    assertNotNull(tstResult);

    var tstResultString = new String(tstResult, StandardCharsets.UTF_8);
    assertTrue(tstResultString.contains(patient.name()));
    assertTrue(tstResultString.contains(patient.kvnr()));
    assertTrue(
        tstResultString.contains(patient.birthDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
  }

  @Test
  void getNameParts() {
    var title = "Graf";
    var firstnames = "Hubert Herbert Harald";
    var lastname = "Herford";
    var name = title + " " + firstnames + " " + lastname;
    var tstResult = assertDoesNotThrow(() -> tstObj.getNameParts(name));
    assertEquals(title, tstResult.getLeft());
    assertEquals(firstnames, tstResult.getMiddle());
    assertEquals(lastname, tstResult.getRight());
  }

  @Test
  void patientsTestdata() {
    var tstResult = assertDoesNotThrow(tstObj::patientsTestdata);
    assertNotNull(tstResult);
    assertFalse(tstResult.isEmpty());

    var patient = assertDoesNotThrow(() -> tstResult.stream().findFirst().orElseThrow());
    assertNotNull(patient.birthDate());
    assertNotNull(patient.name());
    assertNotNull(patient.kvnr());
  }

  @Test
  void euPractitioners() {
    var tstResult = assertDoesNotThrow(tstObj::euPractitioners);

    assertNotNull(tstResult);
    assertFalse(tstResult.isEmpty());

    var practitioner = assertDoesNotThrow(() -> tstResult.stream().findFirst().orElseThrow());
    assertNotNull(practitioner.name());
    assertNotNull(practitioner.country());
    assertNotNull(practitioner.profileName());
  }

  @Test
  void knownTitles() {
    var tstResult = assertDoesNotThrow(tstObj::knownTitles);

    assertNotNull(tstResult);
    assertFalse(tstResult.isEmpty());

    var title = assertDoesNotThrow(() -> tstResult.stream().findFirst().orElseThrow());
    assertNotNull(title);
  }

  @Test
  void knownPrefixes() {
    var tstResult = assertDoesNotThrow(tstObj::knownPrefixes);

    assertNotNull(tstResult);
    assertFalse(tstResult.isEmpty());

    var prefix = assertDoesNotThrow(() -> tstResult.stream().findFirst().orElseThrow());
    assertNotNull(prefix);
  }

  @Test
  void testdataProfiles() {
    var tstResult = assertDoesNotThrow(tstObj::ncpehSimTestdataProfiles);

    assertNotNull(tstResult);
    assertFalse(tstResult.isEmpty());

    var testdataProfile =
        assertDoesNotThrow(() -> tstResult.entrySet().stream().findFirst().orElseThrow());
    assertNotNull(testdataProfile.getValue());
  }

  @Test
  void konnektorAddressesTest() {
    var tstResult = assertDoesNotThrow(tstObj::konnektorAddresses);

    assertNotNull(tstResult);
    assertFalse(tstResult.isEmpty());

    var example = tstResult.values().stream().flatMap(Collection::stream).findFirst().orElse(null);

    assertNotNull(example);

    var exampleUri = assertDoesNotThrow(() -> URI.create(example));
    assertDoesNotThrow(exampleUri::toURL);
  }
}
