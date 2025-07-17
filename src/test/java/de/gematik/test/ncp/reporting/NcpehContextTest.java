/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.reporting;

import static de.gematik.test.ncp.util.ReportingUtils.NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class NcpehContextTest {

  @Test
  void fileNameReturnsDefaultWhenNoConfig() {
    // Arrange
    TigerGlobalConfiguration.reset();
    final NcpehContext context = new NcpehContext();

    // Act
    final var testee = assertDoesNotThrow(context::getFileName);

    // Assert
    assertEquals(NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME, testee);
  }

  @Test
  void fileNameReturnsConfiguredValue() {
    // Arrange
    TigerGlobalConfiguration.putValue("testdata.reporting.fileName", "custom/performance.log");
    final NcpehContext context = new NcpehContext();

    // Act
    final var testee = assertDoesNotThrow(context::getFileName);

    // Assert
    assertEquals("custom/performance.log", testee);
    TigerGlobalConfiguration.reset();
  }

  @ParameterizedTest
  @CsvSource({"null", " ,"})
  void scenarioFileNameIsEmptyWhenScenarioNameIsNullOrBlank(final String scenarioName) {
    // Arrange
    final var actualScenarioName = "null".equals(scenarioName) ? null : scenarioName;
    final NcpehContext context = new NcpehContext();
    context.setScenarioName(actualScenarioName);

    // Act
    final var testee = assertDoesNotThrow(context::getScenarioFileName);

    // Assert
    assertTrue(testee.isEmpty());
  }

  @ParameterizedTest
  @CsvSource({
    "Test Scenario, \\d+-Test_Scenario-perf\\.log",
    "Scenario with special characters!@#, \\d+-Scenario_with_special_characters_-perf\\.log"
  })
  void scenarioFileNameIsGeneratedCorrectly(
      final String scenarioName, final String expectedPattern) {
    // Arrange
    final NcpehContext context = new NcpehContext();
    context.setScenarioName(scenarioName);

    // Act
    final var testee = assertDoesNotThrow(context::getScenarioFileName);

    // Assert
    assertTrue(testee.map(name -> name.matches(expectedPattern)).orElse(false));
  }
}
