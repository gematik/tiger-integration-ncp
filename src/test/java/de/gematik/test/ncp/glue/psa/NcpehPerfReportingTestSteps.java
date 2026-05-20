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

package de.gematik.test.ncp.glue.psa;

import static de.gematik.test.ncp.util.ReportingUtils.DWH_GET_PERFORMANCE_REPORT_RESPONSE_BODY;
import static de.gematik.test.ncp.util.ReportingUtils.prepareMockResponseFromDwh;
import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import de.gematik.test.ncp.reporting.ErrorCode;
import de.gematik.test.ncp.reporting.UseCase;
import de.gematik.test.ncp.screenplay.abilities.ProvidePerformanceReportData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePerformanceReportTechData;
import de.gematik.test.ncp.screenplay.actions.FetchDataFromDwh;
import de.gematik.test.ncp.screenplay.actions.MatchPerformanceReports;
import de.gematik.test.ncp.screenplay.actions.ReadPerformanceLogFile;
import de.gematik.test.ncp.screenplay.questions.FindPerformanceLogFile;
import de.gematik.test.ncp.screenplay.questions.HasPerformanceReportEntryErrorcode;
import de.gematik.test.ncp.screenplay.questions.IsBackendDurationConsistent;
import de.gematik.test.ncp.screenplay.questions.IsDurationBiggerZeroAndSmallerLocalDuration;
import de.gematik.test.ncp.screenplay.questions.IsDwhPerformanceReportDataEmpty;
import de.gematik.test.ncp.screenplay.questions.IsDwhPerformanceReportDataNotEmpty;
import de.gematik.test.ncp.screenplay.questions.IsPerformanceReportDataNotEmpty;
import de.gematik.test.ncp.screenplay.questions.IsPerformanceReportEntriesStatusInExpectedCodeRange;
import de.gematik.test.ncp.screenplay.questions.IsPerformanceUseCasesDataKnown;
import de.gematik.test.ncp.screenplay.questions.PerformanceReportEntriesHaveExpectedErrorCode;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
public class NcpehPerfReportingTestSteps {

  private static final String DWH_URL_ACTIVE_KEY = "tiger.servers.dwhUrl.active";
  private static final String DWH_ACCEPTABLE_DELTA_KEY = "testdata.reporting.acceptableDelta";
  private static final String PERFORMANCE_USE_CASES_KEY_COUNTRY = "country";
  private static final String PERFORMANCE_USE_CASES_KEY_PERFORMANCE_USECASES =
      "PerformanceUsecases";
  private static final String SEPARATOR = ",";
  private static final String PERFORMANCE_USE_CASES_KEY_EHDSI_ERRORCODES = "ehdsiErrorcodes";
  private final Optional<Integer> acceptableDelta =
      TigerGlobalConfiguration.readIntegerOptional(DWH_ACCEPTABLE_DELTA_KEY);

  @AllArgsConstructor
  @Getter
  public static class PerformanceUseCases {
    private final String country;
    private final List<UseCase> performanceUseCases;
    private final List<ErrorCode> errorCodes;
  }

  @DataTableType
  public PerformanceUseCases performanceUseCasesEntry(final Map<String, String> entry) {
    return new PerformanceUseCases(
        entry.get(PERFORMANCE_USE_CASES_KEY_COUNTRY),
        Arrays.stream(entry.get(PERFORMANCE_USE_CASES_KEY_PERFORMANCE_USECASES).split(SEPARATOR))
            .map(UseCase::fromValue)
            .toList(),
        Arrays.stream(
                Optional.ofNullable(entry.get(PERFORMANCE_USE_CASES_KEY_EHDSI_ERRORCODES))
                    .orElse("")
                    .split(SEPARATOR))
            .filter(s -> !s.isBlank())
            .map(value -> "null".equals(value) ? null : ErrorCode.fromValue(value))
            .toList());
  }

  @SneakyThrows
  @Wenn("der NCPeH für {int} Minuten Idle ist")
  public void ncpehIsIdleForMinutes(final int idleInterval) {
    // Hint: for this test step it is not needed to record the chosen time interval

    // Save idleInterval by testActor
    final var testActor = OnStage.theActorInTheSpotlight();
    testActor.whoCan(ProvidePerformanceReportTechData.withIdleInterval(idleInterval));

    // Wait idleInterval minutes
    TimeUnit.MINUTES.sleep(idleInterval);
  }

  @Dann("liefert er an den BDEv2 Dienst mindestens einen leeren Performance Report")
  public void checkReceivedEmptyPerfReport() {
    // the report data has to be exported from BDE / data warehouse

    // Hint: a time intervall is not being checked here. Here it is only important, that a proper
    // idle-Information can be delivered by the NCPeH

    // Fetch the NCPeH report data from DWH for last 10 minutes, the answer should be empty report
    if (!TigerGlobalConfiguration.readBoolean(DWH_URL_ACTIVE_KEY)) {
      // prepare empty mock response from DWH
      TigerGlobalConfiguration.putValue(DWH_GET_PERFORMANCE_REPORT_RESPONSE_BODY, "[]");
    }
    final var testActor = OnStage.theActorInTheSpotlight();

    final var idleInterval =
        testActor.usingAbilityTo(ProvidePerformanceReportTechData.class).getIdleInterval();
    final var start = Instant.now().minus(idleInterval, ChronoUnit.MINUTES).toEpochMilli();
    final var end = Instant.now().toEpochMilli();

    then(testActor)
        .attemptsTo(
            FetchDataFromDwh.fromStartAndEnd(start, end)
                .then(Ensure.that(IsDwhPerformanceReportDataEmpty.instance()).isTrue()));
  }

  @Und("in Grafana können die erwarteten Report Daten nachvollzogen werden")
  public void checkTheReportContentIsVisibleInGrafana() {
    // display a message, that
    // 1. the tester confirms the visibility of the received report data
    // 2. the tester places a screenshot file for documentation
    // the message shall include information to support the tester:
    // - file name convention (e.g. *.png)
    // - expected file location (configurable)
    // - link to the NCPeH grafana page (configurable)

    // ToDo: define file name convention

    // search for available files, check the filename syntax
    // Error, if none is found

    // move the screenshot file to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error, if file can not be moved

    // TODO Clarify:
    // Clarify Grafana REST API to get the report data
    // we can make it automatic
    // see:
    // http://192.168.233.4:3000/d/ee5s9d4jbtjpca/serenety-details?var-uploadName=tiger-integration-ncp_136_20250212_113956916&orgId=1
    // one problem to solved -> mapping Tag Scenario Name to Scenario Name, because in Grafana saved
    // Scenario Name

  }

  @Angenommen("^der Testfall (NCP1[A-Za-z0-9_]*) wurde zu einem bekannten Zeitpunkt ausgeführt$")
  @Angenommen("^der Testfall (NCP2[A-Za-z0-9_]*) wurde zu einem bekannten Zeitpunkt ausgeführt$")
  public void ncpehTestcaseHasBeenExecutedAtPointOfTime(final String testCaseId) {
    // Check if exists log with testCaseId name in reporting folder
    // Find & parse founded the youngest scenarios log file and saved by Test Actor ability

    final var testActor = OnStage.theActorInTheSpotlight();

    final var performanceLogFile =
        FindPerformanceLogFile.forTestCaseId(testCaseId).answeredBy(testActor);
    Ensure.that(performanceLogFile.isPresent()).isTrue();
    testActor.attemptsTo(
        ReadPerformanceLogFile.fromFile(performanceLogFile)
            .then(Ensure.that(new IsPerformanceReportDataNotEmpty()).isTrue()));
  }

  @Angenommen("es wurden in einem {int} Minuten Zeitintervall mehrere NCPeH Testfälle ausgeführt")
  public void executeMultipleTestcasesWithinIntervall(
      final int executionInterval, final DataTable testCaseTable) {
    // testCaseTable: | Testfallindex | Testfall (interne Id) | PerformanceUsecases (comma separated
    // list) |

    // calculate a possible timing sequence from executionInterval(minutes) and number of testcases
    // in DataTable

    // execute the given test cases according to DataTable and calculated timing sequence
    // Error, if any of the testcases could not be successfully executed
    // ToDO:
    //    find out, if this way of test execution is implementable, otherwise a testcase redesign
    //    is required

    // get execution time of use cases from the executed testcases to produce reference data for
    // later validation of performance report(s)
    // Error, if any execution time of PerformanceUsecases could not be taken from the test results
  }

  @Und(
      "^die (?:erwarteten |)Daten der Performance Use Cases aus dem (?:Negativ |)Testfall sind bekannt$")
  public void importTestcaseExecutionDataOfPerformanceUsecases(
      final PerformanceUseCases performanceUsecase) {
    // PerformanceUsecaseTable: | country | PerformanceUsecases (comma separated list) |
    // ehdsiErrorcodes (comma separated list) |
    // Possible PerformanceUsecases and their meaning:
    //    "NCPeH.UC_1": IdentifyPatient for Patient Summary
    //    "NCPeH.UC_2": FindDocuments for Patient Summary
    //    "NCPeH.UC_3": RetrieveDocuments CDA L3 for Patient Summary
    //    "NCPeH.UC_4": RetrieveDocuments CDA L1 (PDF) for Patient Summary
    //    "NCPeH.UC_9": IdentifyPatient for ePrescription
    //    "NCPeH.UC_10": FindDocuments for ePrescription
    //    "NCPeH.UC_11": RetrieveDocuments for ePrescription
    //    "NCPeH.UC_12": ProvideAndRegisterDocumentSet for eDispensation
    // ehdsiErrorcodes: "null" if no eHDSI error is related to that PerformanceUsecase, otherwise
    //    expected error-String

    // Check country, error code & use cases by saved data in ability
    final var testActor = OnStage.theActorInTheSpotlight();
    and(testActor)
        .attemptsTo(
            Ensure.that(IsPerformanceUseCasesDataKnown.fromPerformanceUseCases(performanceUsecase))
                .isTrue());
  }

  @Wenn(
      "zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden")
  public void importReceivedPerfReportsForTimeInterval() {
    // import the report data in proper time sequence from DWH
    // expected format for each line (PerformanceUsecase) of a report file (csv syntax)
    //    timestamp;duration_in_ms;operation;status;message
    //    - operation identifies the PerformanceUsecase
    //    - the content of the message string is in json format
    // Error, if data can not be imported or is incomplete

    // Use time filter: start time is timestamp from first row in report - acceptable delta
    // end time ist timestamp from last row in report + duration + acceptable delta

    // save fetch data in the ability by Test Actor

    // Hint: there is a risk, that the clock from the NCPeH is to much out of sync with the test
    // system. Due to the test setup, anyhow there will be quite a difference between SUT and
    // test driver

    final var testActor = OnStage.theActorInTheSpotlight();

    if (!TigerGlobalConfiguration.readBoolean(DWH_URL_ACTIVE_KEY)) {
      // prepare mock response from DWH
      prepareMockResponseFromDwh(
          testActor.usingAbilityTo(ProvidePerformanceReportData.class).getPerformanceReport(),
          acceptableDelta.orElse(0));
    }
    final var report =
        testActor.usingAbilityTo(ProvidePerformanceReportData.class).getPerformanceReport();
    final var delta = acceptableDelta.orElse(0);
    final long start = report.getEntries().getFirst().timestamp() - delta;
    final long end =
        report.getEntries().getLast().timestamp()
            + report.getEntries().getLast().duration()
            + delta;

    when(testActor).attemptsTo(FetchDataFromDwh.fromStartAndEnd(start, end));
    and(testActor).attemptsTo(Ensure.that(IsDwhPerformanceReportDataNotEmpty.instance()).isTrue());
  }

  @Dann("enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case")
  public void checkExpectedPerformanceReportEntriesArePresent() {
    // check the available operation information from the imported report data

    // for each expected PerformanceUsecase:
    //    name of PerformanceUsecase == operation from the imported report entry
    // in sequence of the expectations

    // Try match data from log with data from DWH
    // Save match paars as ability by Test Actor

    // Match functionality
    // check whether the start-times of the performance report entries are within the known
    // intervals. Reference time is taken from the information about expected PerformanceUsecases

    // Hint: there is a risk, that the clock from the NCPeH is too much out of sync with the test
    // system. Also, due to the test setup, there will be quite a time difference between SUT
    // and test driver measurement.
    // Therefore, an acceptable time Delta is introduced into the check (via configuration).
    // Which delta will be acceptable must be found out by trial and error.

    // for each expected PerformanceUsecase
    //    start-time of expected PerformanceUsecase == reported start-time +/- acceptable delta
    //    (configurable)
    // in sequence of the expectations
    // Error, if there is no correlation between imported report(s) and data of imported testcase
    // found

    // check whether the message field contains the expected requesting country

    OnStage.theActorInTheSpotlight()
        .attemptsTo(MatchPerformanceReports.withAcceptableDelta(acceptableDelta.orElse(0)));
  }

  @Und("^das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form ([1-5]xx)$")
  public void checkStatusFieldOfPerformanceReportEntries(final String expectedCodeRange) {
    // check whether the status fields of the performance report entries are within the given range

    // (currently) possible ranges: 1xx, 2xx, 3xx, 4xx, 5xx
    // Error, if an unknown status range is given

    // for each report entry the status is in range of expectedCodeRanges

    OnStage.theActorInTheSpotlight()
        .attemptsTo(
            Ensure.that(
                    IsPerformanceReportEntriesStatusInExpectedCodeRange.withExpectedCodeRange(
                        expectedCodeRange))
                .isTrue());
  }

  @Und(
      "die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit")
  public void checkDurationOfPerformanceReportEntries() {
    // check whether the duration of the performance report entries fit to the related duration
    // taken from the imported reference testcase data (e.g. end time - start time)

    // Hint: there is a risk, that the clock from the NCPeH is too much out of sync with the test
    // system. Also, due to the test setup, there will be quite a time difference between SUT
    // and test driver measurement.
    // Therefore, an acceptable time Delta is introduced into the check (via configuration).
    // Which delta will be acceptable must be found out by trial and error.

    // Check 0 < DWH duration time < local duration time per use case
    OnStage.theActorInTheSpotlight()
        .attemptsTo(Ensure.that(IsDurationBiggerZeroAndSmallerLocalDuration.instance()).isTrue());
  }

  @Und("jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode")
  public void checkPerformanceReportEntryWithoutErrorcode() {
    // check the reported eHDSI errorCode
    // Hint: according to A_22513-01 a non-existing value in the json structure of the message field
    // must be given as <keyname>:null

    // for each report entry
    //    check, that the "err" entry in the message field is empty

    OnStage.theActorInTheSpotlight()
        .attemptsTo(Ensure.that(HasPerformanceReportEntryErrorcode.instance()).isFalse());
  }

  @Und(
      "jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode")
  public void checkPerformanceReportEntryWithErrorcode() {
    // check that the "err" entry in the message field for each perf use case according to its given
    // expectation
    // Hint: according to A_22513-01 a non-existing value in the json structure of the message field
    // must be given as <keyname>:null

    // for each expected PerformanceUsecase
    //    expected eHDSI errorCode matches the reported "err" entry
    // Error, if there is no correlation between imported report(s) and data of imported testcase
    // found

    OnStage.theActorInTheSpotlight()
        .attemptsTo(Ensure.that(PerformanceReportEntriesHaveExpectedErrorCode.instance()).isTrue());
  }

  @Und(
      "jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration")
  public void checkPerformanceReportEntryForBackendduration() {
    // check that the bkdur entry in the message field for each perf use case
    // * is available and
    // * its value ist greater than zero and
    // * its value is smaller than the own measured overall time of the operation
    // see https://gemspec.gematik.de/docs/gemSpec/gemSpec_Perf/latest/#A_23118-02

    OnStage.theActorInTheSpotlight()
        .attemptsTo(Ensure.that(IsBackendDurationConsistent.instance()).isTrue());
  }

  @Und("es ist eine Übersicht der entsprechend durchgeführten Performance Use Cases vorhanden")
  public void buildOverviewOfExecutedTestcases() {
    // in case of multiple execution of testcases for report evaluation, we need a list of expected
    // PerformanceUsecases with its related data, to be taken from the testcase results.

    // from the list of executed testcases construct a list of expected PerformanceUsecases with its
    //    start-time,duration,operation
    // Hint: take care to keep the correct sequence of expected PerformanceUsecases
    // Error, if list can not be build up

    // export the constructed list into a text file (for logging and to support the tester for later
    // steps)
    // Error, if file can not be written
    // ToDo: define the format of the content (human readable)

    // calculate an overall testcase execution interval (to help identify the needed reports)
    //  - start of interval == start time of first expected PerformanceUsecase
    //  - end of interval == end time of last expected PerformanceUsecase
  }

  @Dann(
      "enthalten die Reports insgesamt je einen Eintrag für jeden erwarteten Performance Use Case")
  public void checkPerformanceReportEntriesSummary() {
    // check the available time information from the imported report files
    // for each entry in the list of expected PerformanceUsecases:
    //    expected PerformanceUsecase == operation from the imported report entries AND
    //    start time of expected PerformanceUsecase == reported start time +/- acceptable delta
    //      (configurable) AND
    //    duration of expected PerformanceUsecase == reported duration +/- acceptable delta
    //      (configurable)
    // in sequence of the expectations

    // Hint: there is a risk, that someone performs tests in parallel. This leads to mix-ups in the
    // report
    // ToDO: There could be several ways, to deal with it:
    //    a) raise error and ask to execute the reference testcase again (new measurement)
    //    b) find a way to identify the entries being out of scope.
  }

  @Angenommen("die erwarteten Daten zur Selbstauskunft sind bekannt")
  public void importSelbstauskunftReferenceData() {
    // display a message to put a file containing the reference data of Selbstauskunft
    // - show the file name convention
    // - show file content syntax convention
    // - show the expected file location (configurable)

    // Content of the expected data must be clarified with gematik operations / test management /
    // product team AND the provider of NCPeH
    // Hint: required for later validation:
    // 1. InformationDate
    // 2. ProductType (gematik)
    // 3. ProductTypeVersion (gematik)
    // 4. ProductVersion (provider)

    // ToDo: define file name and content syntax convention

    // search for available Selbstauskunft reference file, check the filename syntax
    // Error, if none is found

    // import the data from that file
    // Error if content can not be imported

    // move the file to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error, if file can not be moved
  }

  @Wenn("der NCPeH im 60 Minuten Intervall eine Selbstauskunft an den BDEv2 Dienst sendet")
  public void importSelbstauskunft() {
    // display a message to place a file containing the Selbstauskunft exported from BDE
    // - ask for export from the data warehouse / BDE
    // - show the file name convention
    // - show the expected file location (configurable)

    // ToDo: define file name convention

    // search for available Selbstauskunft file, check the filename syntax
    // Error, if none is found

    // import the xml structure from that file
    // Error if content can not be imported

    // move the file to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error, if file can not be moved
  }

  @Dann(
      "enthält er die mit gematik Operations und dem Betreiber abgestimmten Informationen über den NCPeH Fachdienst")
  public void checkContentOfSelbstauskunft() {
    // check for equality: the reference data with the content from the imported selbstauskunft
    // structure:
    // 1. InformationDate
    // 2. ProductType
    // 3. ProductTypeVersion
    // 4. ProductVersion
  }

  @Und("in Grafana können die erwarteten Selbstauskunft Daten nachvollzogen werden")
  public void checkSelbstauskunftContentIsVisibleInGrafana() {
    // display a message, that
    // 1. the tester confirms the visibility of the Selbstauskunft
    //    for comparison
    //    - show expected producttype version
    //    - show expected product version
    // 2. the tester places a screenshot file for documentation
    //    - show the file name convention
    //    - show the expected file location (configurable)
    //    - a link to the NCPeH grafana page (configurable)

    // ToDo: define file name convention (i.e. *.png)

    // move the screenshot file to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error if file is not found or can not be moved
  }
}
