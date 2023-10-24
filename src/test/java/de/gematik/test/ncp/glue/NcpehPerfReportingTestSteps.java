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

package de.gematik.test.ncp.glue;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;

public class NcpehPerfReportingTestSteps {

  @Wenn("der NCPeH für {int} Minuten Idle ist")
  public void ncpehIsIdleForMinutes(int idleInterval) {
    // display a message for the tester to identify a timeframe of idleInterval minutes,
    // were the NCPeH has been idle
    // the tester should take a note, which time interval has been chosen by him

    // Hint: for this test step it is not needed to record the chosen time interval
  }

  @Dann(
      "liefert er an den BDEv2 Dienst mindestens einen Performance Report mit einer Leer-Information")
  public void checkReceivedEmptyPerfReport() {
    // display a message to put ONE report file to be checked and fits into the given idle interval
    // of the ncp
    // the report file has to be exported from BDE / data warehouse
    // Display information to support the tester:
    // - file name convention
    // - expected file location (configurable)

    // ToDo: define file name convention

    // import report file content from expected file
    // Error, if file is not found or content can not be imported

    // move the file to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error, if file can not be moved

    // check, that the only file content of every file is "leer" (as exact String)

    // Hint: a time intervall is not being checked here. Here it is only important, that a proper
    // idle-Information can be delivered by the NCPeH
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
  }

  @Angenommen("^der Testfall (NCP1[A-Za-z0-9_]*) wurde zu einem bekannten Zeitpunkt ausgeführt$")
  public void ncpehTestcaseHasBeenExecutedAtPointOfTime(String testCaseId) {
    // display a message with the request to confirm, that the given testcase has been executed
    // the message shall include information to support the tester:
    // - which testcase must have been executed

    // remember the given test case name as reference information for subsequent test steps
  }

  @Angenommen("es wurden in einem {int} Minuten Zeitintervall mehrere NCPeH Testfälle ausgeführt")
  public void executeMultipleTestcasesWithinIntervall(
      int executionInterval, DataTable testCaseTable) {
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

  @Und("die Daten der Performance Use Cases aus dem Testfall sind bekannt")
  public void importTestcaseExecutionDataOfPerformanceUsecases(DataTable PerformanceUsecaseTable) {
    // PerformanceUsecaseTable: | country | PerformanceUsecases (comma separated list) |
    //    Possible PerformanceUsecases and their meaning
    //    "NCPeH.UC_1": Identify Patient
    //    "NCPeH.UC_2": FindDocuments
    //    "NCPeH.UC_3": Retrieve Documents CDA L3
    //    "NCPeH.UC_4": Retrieve Documents CDA L1 (PDF)

    // display a message to put a file, which contains the needed reference data from the executed
    // testcase
    // start-time, duration, status and expected message-content per executed PerformanceUsecase
    // the message shall include information to support the tester:
    // - file name convention
    // - expected file location (configurable)
    // - (if it becomes useful: Syntax of file content)

    // ToDo: define file name convention
    // ToDo: define syntax for file content

    // search for available files, check the filename syntax
    // Error, if none is found

    // import the data from the given file (start-time, duration, status and expected
    // message-content per executed PerformanceUsecase from the test case results)
    // Error, if data can not be imported or is incomplete

    // move the file(s) to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error, if file can not be moved

    // calculate the testcase execution interval
    //  - begin: start-time of first PerformanceUsecase within the measured test case,
    //  - end: end-time of last PerformanceUsecase within the measured test case

    // remember the given PerformanceUsecases and their data for subsequent teststeps
    // remember the calculated testcase execution interval
  }

  @Und("die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt")
  public void importTestcaseExecutionDataOfNegativeUseCases(DataTable PerformanceUsecaseTable) {
    // PerformanceUsecaseTable: | country | PerformanceUsecases (comma separated list) |
    // ehdsiErrorcodes (comma separated list) |
    // Possible PerformanceUsecases and their meaning:
    //    "NCPeH.UC_1": Identify Patient
    //    "NCPeH.UC_2": FindDocuments
    //    "NCPeH.UC_3": Retrieve Documents CDA L3
    //    "NCPeH.UC_4": Retrieve Documents CDA L1 (PDF)
    // ehdsiErrorcodes: "none" if no eHDSI error is related to that PerformanceUsecase, otherwise
    //    expected error-String

    // display a message to put a file, which contains the needed reference data from the executed
    // testcase
    // start-time, duration, status and expected message-content per executed PerformanceUsecase
    // Display information to support the tester:
    // - file name convention
    // - expected file location (configurable)
    // - (if it becomes useful: Syntax of file content)

    // ToDo: define file name convention
    // ToDo: define syntax for file content

    // search for available files, check the filename syntax
    // Error, if none is found

    // import the data from the given file
    // Error, if data can not be imported or is incomplete

    // move the file(s) to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error, if file can not be moved

    // take over the expected ehdsiErrorcode for each expected performance use case (from
    // PerformanceUsecaseTable)
    // Hint: usually only the last perf use case should contain an ehdsiErrorcode, all others
    //       should have "none"
    // ??? may be a simplification is possible, by directly taking the expected errorCode from the
    //     executed testcase ???

    // calculate the testcase execution interval
    // - begin: start-time of first PerformanceUsecase within the measured test case,
    // - end: end-time of last PerformanceUsecase within the measured test case

    // remember the given PerformanceUsecases and their related data for subsequent teststeps
    // remember the calculated testcase execution interval
  }

  @Wenn(
      "zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden")
  public void importReceivedPerfReportsForTimeInterval() {
    // display a message to place one or more performance report files exported from BDE
    // (it might happen, that the testcase crossed one report interval border)
    // - ask for export from the data warehouse / BDE
    // - show the file name convention
    // - show the expected file location (configurable)
    // - show the testcase execution interval
    // Error, if testcase execution interval ist not available

    // ToDo: define file name convention

    // search for available report files, check the filename syntax
    // Error, if none is found

    // process the filenames, to take the report time intervals from them (unix timestamps)
    // ToDo:
    //    syntax for file name on export from BDE/data warehouse must be given by operations
    //    it might be, that it does not fully comply with the syntax for report file names

    // consistency check, if given report files cover the testcase execution interval:
    // - start-time of the first performance report <= start-time of testcase execution interval
    // - end-time of the last performance report >= end-time of testcase execution interval
    // - no gaps between the report intervals, for this take into account:
    //   end-time interval n == start-time interval n+1

    // import the report files in proper time sequence
    // expected format for each line (PerformanceUsecase) of a report file (csv syntax)
    //    timestamp;duration_in_ms;operation;status;message
    //    - operation identifies the PerformanceUsecase
    //    - the content of the message string is in json format
    // Error, if data can not be imported or is incomplete

    // move the file(s) to a position, where it will become part of LogFiles
    // Hint: move to ensure, that no old files are left over and wrongly evaluated by chance
    // Error, if file can not be moved

    // Hint: there is a risk, that the clock from the NCPeH is to much out of sync with the test
    // system. Due to the test setup, anyhow there will be quite a difference between SUT and
    // test driver
    // Todo: This can cause an incorrect fail (or pass?) - check possibilities, how to deal with it
  }

  @Dann("enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case")
  public void checkExpectedPerformanceReportEntriesArePresent() {
    // check the available operation information from the imported report files

    // for each expected PerformanceUsecase:
    //    name of PerformanceUsecase == operation from the imported report entry
    // in sequence of the expectations

    // Hint: there is a risk, that someone performs tests in parallel. This leads to mix-ups in the
    // report
    // ToDo: There could be several ways, to deal with it:
    //    a) raise error and ask to execute the reference testcase again (new measurement)
    //    b) find a way to identify the entries being out of scope.
  }

  @Und("das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode")
  public void checkStatusFieldOfPerformanceReportEntries(DataTable expectedCodeRange) {
    // check whether the status fields of the performance report entries are within the given range

    // (currently) possible ranges: 1xx, 2xx, 3xx, 4xx, 5xx
    // Error, if an unknown status range is given

    // for each report entry the status is in range of expectedCodeRange
  }

  @Und(
      "jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land")
  public void checkRequestingCountryOfPerformanceReportEntries() {
    // check whether the message field contains the expected requesting country

    // for each expected PerformanceUsecase
    //    expected country == content of "reqc" field within message field from the imported report
    //    entries
    // Error, if there is no correlation between imported report(s) and data of imported testcase
    // found

    // ToDo:
    //    currently, the Country information coming from the NCPeH does not necessarily be in the
    //    two digit format. A_23118 gives no requirements about the format.
    //    Still, the initial expectation is a two-character coded country as in
    //    ISO 3166-1 Alpha 2 (similar to definition in eHDSI_X509_Certificates_Profile#3.1,
    //    definition of country in Element Subject)
  }

  @Und(
      "^die Startzeiten der erwarteten Einträge (?:des|der) Performance Reports liegen im erwarteten Ausführungsintervall$")
  public void checkStarttimeOfPerformanceReportEntries() {
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

    // for each expected PerformanceUsecase
    //    duration of expected PerformanceUsecase == reported duration +/- acceptable delta
    // (configurable)
    // in sequence of the expectations
    // Error, if there is no correlation between imported report(s) and data of imported testcase
    // found
  }

  @Und("jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode")
  public void checkPerformanceReportEntryWithoutErrorcode() {
    // check the reported eHDSI errorCode
    // Hint: according to A_22513-01 a non-existing value in the json structure of the message field
    // must be given as <keyname>:null

    // for each report entry
    //    check, that the ecode entry in the message field is empty

  }

  @Und(
      "jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode")
  public void checkPerformanceReportEntryWithErrorcode() {
    // check that the ecode entry in the message field for each perf use case according to its given
    // expectation
    // Hint: according to A_22513-01 a non-existing value in the json structure of the message field
    // must be given as <keyname>:null

    // for each expected PerformanceUsecase
    //    expected eHDSI errorCode matches the reported ecode entry
    // Error, if there is no correlation between imported report(s) and data of imported testcase
    // found

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
