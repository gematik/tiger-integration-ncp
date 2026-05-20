/*
 * Copyright (Change Date see Readme), gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes
 * by gematik, find details in the "Readme" file.
 */

package de.gematik.test.ncp.util;

import static de.gematik.test.ncp.util.ReportingUtils.DWH_GET_PERFORMANCE_REPORT_RESPONSE_BODY;
import static de.gematik.test.ncp.util.ReportingUtils.NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME;
import static de.gematik.test.ncp.utils.TestUtils.mapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.core.type.TypeReference;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReport;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportDWHResponseEntry;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportEntry;
import de.gematik.test.ncp.utils.TestUtils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Slf4j
class ReportingUtilsTest {

  private static final String CUSTOM_PERFORMANCE_LOG_FILE_NAME = "custom/performance.log";

  @BeforeEach
  void setup() {
    TigerGlobalConfiguration.reset();
  }

  @Test
  void performanceLogFileNameReturnsDefaultWhenNoConfig() {
    // Arrange & Act
    final var testee = assertDoesNotThrow(ReportingUtils::getPerformanceLogFileName);

    // Assert
    assertEquals(NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME, testee);
  }

  @Test
  void performanceLogFileNameReturnsConfiguredValue() {
    // Arrange
    TigerGlobalConfiguration.putValue(
        "testdata.reporting.fileName", CUSTOM_PERFORMANCE_LOG_FILE_NAME);

    // Act
    final var testee = assertDoesNotThrow(ReportingUtils::getPerformanceLogFileName);

    // Assert
    assertEquals(CUSTOM_PERFORMANCE_LOG_FILE_NAME, testee);
  }

  @Test
  void performanceLogFileNameHandlesEmptyConfig() {
    // Arrange
    TigerGlobalConfiguration.putValue("testdata.reporting.fileName", "");

    // Act
    final var testee = assertDoesNotThrow(ReportingUtils::getPerformanceLogFileName);

    // Assert
    assertEquals(NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME, testee);
  }

  @ParameterizedTest
  @CsvSource({"null", " ,"})
  void generateFileNameHandlesEmptyOrNullScenarioName(final String scenarioName) {
    // Arrange
    final var actualScenarioName = "null".equals(scenarioName) ? null : scenarioName;

    // Act
    final var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> ReportingUtils.generateFileName(actualScenarioName));

    // Assert
    assertEquals("Scenario Name is null or blank", exception.getMessage());
  }

  @ParameterizedTest
  @CsvSource({
    "Test Scenario, \\d+-Test_Scenario-perf\\.log",
    "Scenario with special characters!@#, \\d+-Scenario_with_special_characters_-perf\\.log"
  })
  void scenarioFileNameIsGeneratedCorrectly(
      final String scenarioName, final String expectedPattern) {

    // Act
    final var testee = assertDoesNotThrow(() -> ReportingUtils.generateFileName(scenarioName));

    // Assert
    assertTrue(testee.matches(expectedPattern));
  }

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({
    "testCase123, 1, 0, true",
    "testCase123, 1, 3, true",
    "testCase123, 3, 3, true",
    "testCase456, 1, 3, true",
    "testCase123, 0, 0, false",
    ", 0, 1, false",
  })
  void performanceLogFileIsFoundWhenFileExists(
      final String testCaseId, final int count, final int acount, final boolean expected) {
    // Arrange
    TigerGlobalConfiguration.putValue(
        "testdata.reporting.fileName", CUSTOM_PERFORMANCE_LOG_FILE_NAME);

    final Path reportingFolder = ReportingUtils.getReportingFolder();
    Files.createDirectories(reportingFolder);
    Path expectedPath = null;
    for (int i = 0; i < acount; i++) {
      expectedPath = createFile(reportingFolder, "someOtherTestCaseId");
    }
    for (int i = 0; i < count; i++) {
      expectedPath = createFile(reportingFolder, testCaseId);
    }

    // Act
    final Optional<Path> testee = ReportingUtils.getPerformanceLogFile(testCaseId);

    // Assert
    assertEquals(expected, testee.isPresent());
    if (expected) {
      assertEquals(expectedPath, testee.get());
    }

    cleanup();
  }

  @SneakyThrows
  private static @NotNull Path createFile(final Path reportingFolder, final String testCaseId) {
    Path expectedPath;
    String fileName = ReportingUtils.generateFileName(testCaseId);
    expectedPath = reportingFolder.resolve(fileName);
    while (expectedPath.toFile().exists()) {
      fileName = ReportingUtils.generateFileName(testCaseId);
      expectedPath = reportingFolder.resolve(fileName);
    }
    Files.createFile(expectedPath);
    return expectedPath;
  }

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({
    "1-Success-NCP1_IOP_PSA_UC3_030-perf.log, 1-Success-NCP1_IOP_PSA_UC3_030-perf.json",
    "2-Mesage_is_missing-NCP1_IOP_PSA_UC3_030-perf.log, 2-Mesage_is_missing-NCP1_IOP_PSA_UC3_030-perf.json",
    "3-Invalid_line-NCP1_IOP_PSA_UC3_030-perf.log, 3-Invalid_line-NCP1_IOP_PSA_UC3_030-perf.json"
  })
  void readPerformanceLogFileTest(final String logFileName, final String expectedFileName) {
    // Arrange
    final var logFile =
        Optional.of(
            Path.of(Objects.requireNonNull(this.getClass().getResource(logFileName)).toURI()));
    final var expected =
        TestUtils.loadFromJsonResource(
            NcpehPerformanceReport.class, this.getClass(), expectedFileName);

    // Act
    final var testee = assertDoesNotThrow(() -> ReportingUtils.readPerformanceLogFile(logFile));

    log.info(mapper.writeValueAsString(testee));

    // Assert
    assertThat(testee).usingRecursiveComparison().isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void readPerformanceLogFileInvalidPath() {
    // Arrange
    final var logFile = Optional.of(Path.of("invalidPath"));

    // Act
    final var testee = assertDoesNotThrow(() -> ReportingUtils.readPerformanceLogFile(logFile));

    // Assert
    assertTrue(testee.getEntries().isEmpty());
  }

  @SneakyThrows
  static void cleanup() {
    TigerGlobalConfiguration.putValue(
        "testdata.reporting.fileName", CUSTOM_PERFORMANCE_LOG_FILE_NAME);
    final Path reportingFolder = ReportingUtils.getReportingFolder();
    // Delete the temporary directory and its contents
    try (final Stream<Path> files = Files.walk(reportingFolder)) {
      files
          .map(Path::toFile)
          .forEach(
              file -> {
                if (!file.delete()) {
                  file.deleteOnExit();
                }
              });
    }
  }

  @ParameterizedTest
  @CsvSource({
    "11-Success-ReportEntity.json, 0",
    "11-Success-ReportEntity.json, 300",
    "12-SuccessWithTimeShiftOf150-ReportEntity.json, 250"
  })
  void findPairMatchesEntriesCorrectly(final String input, final int acceptableDelta) {
    // Arrange
    final var dwhReport =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), "dwh.json");
    final var entry =
        TestUtils.loadFromJsonResource(NcpehPerformanceReportEntry.class, this.getClass(), input);

    // Act
    final var testee =
        assertDoesNotThrow(
            () ->
                ReportingUtils.findPair(
                    entry, dwhReport.getEntries(), acceptableDelta, new HashSet<>()));

    // Assert
    assertThat(testee).isNotNull();
    assertThat(testee.key()).isEqualTo(entry);
    assertThat(testee.value()).isEqualTo(dwhReport.getEntries().get(1));
  }

  @ParameterizedTest
  @CsvSource({
    "12-SuccessWithTimeShiftOf150-ReportEntity.json, 100",
    "13-WrongOperation-ReportEntity.json, 300",
    "14-WrongStatus-ReportEntity.json, 300",
    "15-WrongCountry-ReportEntity.json, 300",
  })
  void findPairThrowsExceptionOnMismatch(final String input, final int acceptableDelta) {
    // Arrange
    final var dwhEntries =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), "dwh.json")
            .getEntries();
    final var entry =
        TestUtils.loadFromJsonResource(NcpehPerformanceReportEntry.class, this.getClass(), input);
    final var akku = new HashSet<NcpehPerformanceReportEntry>();

    // Act
    final var exception =
        assertThrows(
            NoSuchElementException.class,
            () -> ReportingUtils.findPair(entry, dwhEntries, acceptableDelta, akku));

    // Assert
    assertEquals(
        "No matching entry found for entry: " + entry + " in DWH entries!", exception.getMessage());
  }

  @Test
  void findPairThrowsExceptionOnDuplicateMatch() {
    // Arrange
    final var dwhEntries =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), "dwh.json")
            .getEntries();
    final var entry =
        TestUtils.loadFromJsonResource(
            NcpehPerformanceReportEntry.class,
            this.getClass(),
            "12-SuccessWithTimeShiftOf150-ReportEntity.json");
    final var akku = new HashSet<NcpehPerformanceReportEntry>();
    akku.add(dwhEntries.get(1));

    // Act
    final var exception =
        assertThrows(
            NoSuchElementException.class,
            () -> ReportingUtils.findPair(entry, dwhEntries, 300, akku));

    // Assert
    assertEquals(
        "No matching entry found for entry: NcpehPerformanceReportEntry(timestamp=1739554789870, duration=22, operation=NCPeH.UC_2, status=200, message=NcpehPerformanceMessage[reqc=NL, err=null, bkdur=22]) in DWH entries!",
        exception.getMessage());
  }

  @ParameterizedTest
  @CsvSource({
    "21-Success-Report.json, 100, 3",
    "21-SuccessWithTimeShiftOf100-Report.json, 100, 3",
    "22-Success2Entry-Report.json, 100, 2"
  })
  void matchPerformanceReportsIdentifiesCorrespondingReports(
      final String input, final int acceptableDelta, final int expected) {
    // Arrange
    final var performanceReport =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), input);
    final var dwhPReport =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), "dwh.json");

    // Act
    final var testee =
        assertDoesNotThrow(
            () ->
                ReportingUtils.matchPerformanceReportEntries(
                    performanceReport, dwhPReport, acceptableDelta));

    // Assert
    assertThat(testee).isNotNull().hasSize(expected);
  }

  @ParameterizedTest
  @CsvSource({
    "21-SuccessWithTimeShiftOf100-Report.json, 99, 0",
    "23-MissingEntry-Report.json, 100, 2",
  })
  void matchPerformanceReportsThrowsExceptionOnMismatch(
      final String input, final int acceptableDelta, final int expected) {
    // Arrange
    final var performanceReport =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), input);
    final var dwhPReport =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), "dwh.json");

    // Act
    final var exception =
        assertThrows(
            NoSuchElementException.class,
            () ->
                ReportingUtils.matchPerformanceReportEntries(
                    performanceReport, dwhPReport, acceptableDelta));

    // Assert
    assertEquals(
        "No matching entry found for entry: "
            + performanceReport.getEntries().get(expected)
            + " in DWH entries!",
        exception.getMessage());
  }

  @ParameterizedTest
  @CsvSource({
    "200, 2xx, true",
    "201, 2xx, true",
    ", 2xx, false",
    "404, 4xx, true",
    "200, 4xx, false",
    "500, 5xx, true",
    "501, 5xx, true",
    "599, 5xx, true",
    "600, 5xx, false",
    "499, 5xx, false",
    "200,, false",
    ",, false"
  })
  void isStatusInExpectedCodeRangeTest(
      final String status, final String expectedCodeRange, final boolean expected) {
    // Arrange

    // Act
    final boolean result = ReportingUtils.isStatusInExpectedCodeRange(status, expectedCodeRange);

    // Assert
    assertEquals(expected, result);
  }

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({
    "31-Report.json, 5, 31-Response-Delta5.json",
    "31-Report.json, 0, 31-Response-Delta0.json"
  })
  void prepareMockResponseFromDwhTest(final String report, final int delta, final String expected) {
    // Arrange
    final var performanceReport =
        TestUtils.loadFromJsonResource(NcpehPerformanceReport.class, this.getClass(), report);

    // Act
    assertDoesNotThrow(() -> ReportingUtils.prepareMockResponseFromDwh(performanceReport, delta));

    // Assert
    final var expectedReport =
        TestUtils.loadFromJsonResource(
            new TypeReference<List<NcpehPerformanceReportDWHResponseEntry>>() {},
            this.getClass(),
            expected);
    final var testeeStr =
        TigerGlobalConfiguration.readString(DWH_GET_PERFORMANCE_REPORT_RESPONSE_BODY);
    final var testee =
        mapper.readValue(
            testeeStr, new TypeReference<List<NcpehPerformanceReportDWHResponseEntry>>() {});
    log.debug(mapper.writeValueAsString(testee));
    assertThat(testee).isNotNull();
    assertThat(testee).usingRecursiveComparison().isEqualTo(expectedReport);
  }

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({
    "41-ReportEntity.json, 5, 41-Response-Delta5.json",
    "41-ReportEntity.json, 0, 41-Response-Delta0.json"
  })
  void createDWHResponceEntryTest(final String entity, final int delta, final String expected) {
    // Arrange
    final var performanceReportEntry =
        TestUtils.loadFromJsonResource(NcpehPerformanceReportEntry.class, this.getClass(), entity);

    // Act
    final var testee = ReportingUtils.createDWHResponceEntry(performanceReportEntry, delta);

    // Assert
    log.debug(mapper.writeValueAsString(testee));
    final var expectedReportEntry =
        TestUtils.loadFromJsonResource(
            NcpehPerformanceReportDWHResponseEntry.class, this.getClass(), expected);
    assertThat(testee).isNotNull();
    assertThat(testee).usingRecursiveComparison().isEqualTo(expectedReportEntry);
  }
}
