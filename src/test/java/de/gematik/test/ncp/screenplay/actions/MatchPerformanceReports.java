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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePerformanceReportData;
import de.gematik.test.ncp.util.ReportingUtils;
import lombok.AllArgsConstructor;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@AllArgsConstructor
public class MatchPerformanceReports implements Performable {

  private final int acceptableDelta;

  @Override
  public <T extends Actor> void performAs(final T actor) {
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

    final var reportsData = actor.usingAbilityTo(ProvidePerformanceReportData.class);
    reportsData.setMatchedEntries(
        ReportingUtils.matchPerformanceReportEntries(
            reportsData.getPerformanceReport(),
            reportsData.getDwhPerformanceReport(),
            acceptableDelta));
  }

  public static MatchPerformanceReports withAcceptableDelta(final int acceptableDelta) {
    return Instrumented.instanceOf(MatchPerformanceReports.class).withProperties(acceptableDelta);
  }
}
