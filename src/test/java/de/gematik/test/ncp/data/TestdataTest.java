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

import static de.gematik.test.ncp.data.Testdata.DEFAULT_EPKA_KEY_NAME;
import static de.gematik.test.ncp.utils.TestUtils.mapper;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.utils.TestUtils;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.xmlunit.matchers.CompareMatcher;

@Slf4j
class TestdataTest {

  private static final Patient PATIENT =
      new PatientImpl(
          PersonName.fromString("Dr. John Doedeldie"), "X190471029", LocalDate.of(1987, 11, 23));

  private static final String EPKA_MIO_DEFAULT_XML_FILE_NAME = "epka_mio_default.xml";
  private final Testdata testdata = Testdata.instance();

  @Test
  void loadFileFromPathInConfig() {
    // Arrange
    final var configKey = Testdata.EPKA_TEMPLATES_CONFIG_PATH + "." + DEFAULT_EPKA_KEY_NAME;

    // Act
    final var testee = assertDoesNotThrow(() -> testdata.loadFileFromPathInConfig(configKey));

    // Assert
    assertNotNull(testee);
    assertTrue(testee.exists());
  }

  @Test
  void createEpkaFromTemplate() {
    // Arrange
    // Act
    final var testee =
        assertDoesNotThrow(() -> testdata.createEpkaFromTemplate(PATIENT, DEFAULT_EPKA_KEY_NAME));

    // Assert
    assertNotNull(testee);
    final var xmlString = new String(testee, StandardCharsets.UTF_8);

    final var expected =
        TestUtils.readResourceFile(this.getClass(), EPKA_MIO_DEFAULT_XML_FILE_NAME);
    assertThat(xmlString, CompareMatcher.isSimilarTo(expected).ignoreWhitespace().ignoreComments());
  }

  @Test
  void createEpkaFromDefaultTemplate() {
    // Arrange
    // Act
    final var testee = assertDoesNotThrow(() -> testdata.createEpkaFromTemplate(PATIENT, null));

    // Assert
    assertNotNull(testee);
    final var xmlString = new String(testee, StandardCharsets.UTF_8);
    final var expected =
        TestUtils.readResourceFile(this.getClass(), EPKA_MIO_DEFAULT_XML_FILE_NAME);
    assertThat(xmlString, CompareMatcher.isSimilarTo(expected).ignoreWhitespace().ignoreComments());
  }

  @Test
  void getNameParts() {
    // Arrange
    final var title = "Graf";
    final var firstnames = "Hubert Herbert Harald";
    final var lastname = "Herford";
    final var name = title + " " + firstnames + " " + lastname;

    // Act
    final var testeee = assertDoesNotThrow(() -> testdata.getNameParts(name));

    // Assert
    assertEquals(title, testeee.getLeft());
    assertEquals(firstnames, testeee.getMiddle());
    assertEquals(lastname, testeee.getRight());
  }

  @Test
  void patientsTestdata() {
    // Arrange
    // Act
    final var testee = assertDoesNotThrow(testdata::patientsTestdata);

    // Assert
    assertNotNull(testee);
    assertFalse(testee.isEmpty());

    final var patient = assertDoesNotThrow(() -> testee.stream().findFirst().orElseThrow());
    assertNotNull(patient.birthDate());
    assertNotNull(patient.name());
    assertNotNull(patient.kvnr());
  }

  @Test
  void euPractitioners() {
    // Arrange
    // Act
    final var testee = assertDoesNotThrow(testdata::euPractitioners);

    // Assert
    assertNotNull(testee);
    assertFalse(testee.isEmpty());

    final var practitioner = assertDoesNotThrow(() -> testee.stream().findFirst().orElseThrow());
    assertNotNull(practitioner.name());
    assertNull(practitioner.country());
    assertNotNull(practitioner.profileName());
  }

  @Test
  void knownTitles() {
    // Arrange
    // Act
    final var testee = assertDoesNotThrow(testdata::knownTitles);

    // Assert
    assertNotNull(testee);
    assertFalse(testee.isEmpty());

    final var title = assertDoesNotThrow(() -> testee.stream().findFirst().orElseThrow());
    assertNotNull(title);
  }

  @Test
  void knownPrefixes() {
    // Arrange
    // Act
    final var testee = assertDoesNotThrow(testdata::knownPrefixes);

    // Assert
    assertNotNull(testee);
    assertFalse(testee.isEmpty());

    final var prefix = assertDoesNotThrow(() -> testee.stream().findFirst().orElseThrow());
    assertNotNull(prefix);
  }

  @Test
  void testdataProfiles() {
    // Arrange
    // Act
    final var testee = assertDoesNotThrow(testdata::ncpehSimTestdataProfiles);

    // Assert
    assertNotNull(testee);
    assertFalse(testee.isEmpty());

    final var testdataProfile =
        assertDoesNotThrow(() -> testee.entrySet().stream().findFirst().orElseThrow());
    assertNotNull(testdataProfile.getValue());
  }

  @SneakyThrows
  @Test
  void testDePractitioners() {
    // Arrange
    final var expected =
        TestUtils.loadFromJsonResource(Persons.class, this.getClass(), "dePractitioners.json");

    // Act
    final var testee = assertDoesNotThrow(testdata::dePractitioners);

    log.info(mapper.writeValueAsString(testee));

    // Assert
    assertEquals(expected, testee);
  }
}
