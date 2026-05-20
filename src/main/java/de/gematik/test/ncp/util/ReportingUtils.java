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

import static de.gematik.test.ncp.data.Testdata.TESTDATA_BASEKEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gematik.test.ncp.reporting.Environment;
import de.gematik.test.ncp.reporting.UseCase;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceMessage;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReport;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportDWHResponseEntry;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportEntry;
import de.gematik.test.ncp.reporting.model.Pair;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ReportingUtils {

  private static final String NCPEH_SIMULATION_PERFORMANCE_LOG_FILENAME_KEY =
      TESTDATA_BASEKEY + ".reporting.fileName";

  public static final String NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME =
      "target/reporting/performance.log";
  public static final String CSV_SEPARATOR = ";";

  public static final String DWH_GET_PERFORMANCE_REPORT_RESPONSE_BODY =
      "dwhGetPerformanceReportResponseBody";
  public static final int DWH_MOCK_TIMESHIFT_MS_BELOW_MAX_DELTA = 1;
  public static final int DWH_MOCK_DURATION_MS_BELOW_MAX_DELTA = 1;
  public static final int DWH_MOCK_BKDUR_MS_BELOW_MAX_DELTA = 2;

  /**
   * Retrieves the performance log file name from the configuration. If the configuration value is
   * blank, returns the default file name.
   *
   * @return the performance log file name
   */
  public static String getPerformanceLogFileName() {
    final var fileName =
        TigerGlobalConfiguration.readString(
            NCPEH_SIMULATION_PERFORMANCE_LOG_FILENAME_KEY,
            NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME);
    return fileName.isBlank() ? NCPEH_SIMULATION_PERFORMANCE_LOG_DEFAULT_FILENAME : fileName;
  }

  /**
   * Generates a file name for the given scenario name. The file name format is:
   * {currentTimeMillis}-{scenarioName}-perf.log If the scenario name is null or blank, throws an
   * IllegalArgumentException.
   *
   * @param scenarioName the name of the scenario
   * @return the generated file name
   * @throws IllegalArgumentException if the scenario name is null or blank
   */
  public static String generateFileName(final String scenarioName) {
    return Optional.ofNullable(scenarioName)
        .filter(name -> !name.isBlank())
        .map(
            name ->
                String.format(
                    "%d-%s-perf.log", System.currentTimeMillis(), name.replaceAll("\\W+", "_")))
        .orElseThrow(() -> new IllegalArgumentException("Scenario Name is null or blank"));
  }

  /**
   * Retrieves the reporting folder path. If the parent directory of the performance log file name
   * is null, returns the current directory.
   *
   * @return the reporting folder path
   */
  static Path getReportingFolder() {
    return Optional.ofNullable(Paths.get(getPerformanceLogFileName()).getParent())
        .orElse(Paths.get("."));
  }

  /**
   * Finds the performance log file for the given test case ID in the reporting folder. Searches for
   * regular files containing the test case ID in their file name. Returns the file with the latest
   * name (lexicographically).
   *
   * @param testCaseId the ID of the test case
   * @return an Optional containing the path to the performance log file, or an empty Optional if no
   *     file is found
   */
  @SneakyThrows
  public static Optional<Path> getPerformanceLogFile(final String testCaseId) {
    return Optional.ofNullable(testCaseId)
        .map(
            caseId -> {
              final var reportingFolder = getReportingFolder();

              try (final Stream<Path> files =
                  Files.find(
                      reportingFolder,
                      1,
                      (path, attr) ->
                          attr.isRegularFile()
                              && path.getFileName().toString().contains(testCaseId))) {
                return files.max(Comparator.comparing(path -> path.getFileName().toString()));
              } catch (final IOException e) {
                log.error("Error while searching for performance log file", e);
              }
              return Optional.<Path>empty();
            })
        .orElse(Optional.empty());
  }

  @SneakyThrows
  private Stream<String> readFileLines(final Path performanceLogFile) {
    log.debug("Reading performance log file: {}", performanceLogFile);
    return Files.lines(performanceLogFile);
  }

  @SneakyThrows
  public static NcpehPerformanceReport readPerformanceLogFile(
      final Optional<Path> performanceLogFile) {
    return performanceLogFile.filter(Files::isRegularFile).stream()
        .flatMap(ReportingUtils::readFileLines)
        .map(ReportingUtils::parsePerformanceLogLine)
        .filter(Objects::nonNull)
        .collect(
            Collector.of(
                NcpehPerformanceReport::new,
                NcpehPerformanceReport::addEntry,
                (left, right) -> {
                  left.getEntries().addAll(right.getEntries());
                  return left;
                }));
  }

  private static NcpehPerformanceReportEntry parsePerformanceLogLine(final String line) {
    log.debug("Parsing performance log line: {}", line);
    if (line.isBlank()) {
      return null;
    }
    final var parts = line.split(CSV_SEPARATOR);
    if (parts.length != 5) {
      return null;
    }
    try {
      return new NcpehPerformanceReportEntry()
          .timestamp(Long.parseLong(parts[0]))
          .duration(Long.parseLong(parts[1]))
          .operation(UseCase.fromValue(parts[2]))
          .status(parts[3])
          .message(
              TigerGlobalConfiguration.getObjectMapper()
                  .readValue(parts[4], NcpehPerformanceMessage.class));
    } catch (final JsonProcessingException e) {
      log.error("Error while parsing performance log line", e);
    }
    return null;
  }

  /**
   * Matches performance report entries from two different reports based on a given acceptable time
   * delta.
   *
   * @param performanceReport the first performance report containing the entries to be matched
   * @param dwhPerformanceReport the second performance report containing the entries to be matched
   *     against
   * @param acceptableDelta the acceptable time delta in milliseconds for matching entries
   * @return a list of pairs of matched performance report entries
   */
  public static List<Pair<NcpehPerformanceReportEntry, NcpehPerformanceReportEntry>>
      matchPerformanceReportEntries(
          final NcpehPerformanceReport performanceReport,
          final NcpehPerformanceReport dwhPerformanceReport,
          final int acceptableDelta) {
    // Hint: there is a risk, that in the same time exists more as one pair
    // It can happen if similar scenarios executed in the same time
    // for now implemented simple solution: use first found pair

    final var akku = new HashSet<NcpehPerformanceReportEntry>();
    return performanceReport.getEntries().stream()
        .map(entry -> findPair(entry, dwhPerformanceReport.getEntries(), acceptableDelta, akku))
        .toList();
  }

  static Pair<NcpehPerformanceReportEntry, NcpehPerformanceReportEntry> findPair(
      final NcpehPerformanceReportEntry entry,
      final List<NcpehPerformanceReportEntry> dwhEntries,
      final int acceptableDelta,
      final Set<NcpehPerformanceReportEntry> akku) {
    // for each expected PerformanceUsecase
    //    start-time of expected PerformanceUsecase == reported start-time +/- acceptable delta
    //    operation == operation
    //    status == status
    //    message.reqc == message.reqc
    // in sequence of the expectations
    // Error, if there is no correlation between imported report(s) and data of imported testcase
    // found

    return dwhEntries.stream()
        .filter(
            dwhEntry ->
                Math.abs(entry.timestamp() - dwhEntry.timestamp()) <= acceptableDelta
                    && entry.operation().equals(dwhEntry.operation())
                    && entry.status().equals(dwhEntry.status())
                    && entry.message().reqc().equals(dwhEntry.message().reqc())
                    && !akku.contains(dwhEntry))
        .findFirst()
        .map(
            dwhEntry -> {
              akku.add(dwhEntry);
              return new Pair<>(entry, dwhEntry);
            })
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "No matching entry found for entry: " + entry + " in DWH entries!"));
  }

  /**
   * Checks if the given status code matches the expected code range.
   *
   * @param status the status code to check, e.g., "200"
   * @param expectedCodeRange the expected code range, e.g., "2xx"
   * @return true if the status code matches the expected code range, false otherwise
   */
  public static boolean isStatusInExpectedCodeRange(
      final String status, final String expectedCodeRange) {
    return null != status
        && Optional.ofNullable(expectedCodeRange)
            .map(expected -> expected.replace("x", "\\d"))
            .map(Pattern::compile)
            .map(pattern -> pattern.matcher(status))
            .map(Matcher::matches)
            .orElse(false);
  }

  @SneakyThrows
  public static void prepareMockResponseFromDwh(
      final NcpehPerformanceReport report, final int delta) {
    // prepare mock response from DWH
    log.debug(
        "original report: {}",
        TigerGlobalConfiguration.getObjectMapper().writeValueAsString(report));

    final var effectiveDelta = delta > 0 ? delta - DWH_MOCK_TIMESHIFT_MS_BELOW_MAX_DELTA : 0;
    final List<NcpehPerformanceReportDWHResponseEntry> responseEntries =
        report.getEntries().stream()
            .map(entry -> createDWHResponceEntry(entry, effectiveDelta))
            .toList();

    final var response =
        TigerGlobalConfiguration.getObjectMapper().writeValueAsString(responseEntries);
    log.debug("DWH mock response: {}", response);
    TigerGlobalConfiguration.putValue(DWH_GET_PERFORMANCE_REPORT_RESPONSE_BODY, response);
  }

  static NcpehPerformanceReportDWHResponseEntry createDWHResponceEntry(
      final NcpehPerformanceReportEntry entry, final int effectiveDelta) {
    return NcpehPerformanceReportDWHResponseEntry.builder()
        .bu(Environment.TU)
        // Shift the timestamps of the report entries by the acceptable delta -
        // DWH_MOCK_DURATION_MS_BELOW_MAX_DELTA
        .timestamp(Instant.ofEpochMilli(entry.timestamp() + effectiveDelta))
        .duration(entry.duration() - DWH_MOCK_DURATION_MS_BELOW_MAX_DELTA)
        .usecase(entry.operation())
        .statuscode(entry.status())
        .reqc(entry.message().reqc())
        .err(entry.message().err())
        .bkdur(entry.message().bkdur() - DWH_MOCK_BKDUR_MS_BELOW_MAX_DELTA)
        .build();
  }
}
