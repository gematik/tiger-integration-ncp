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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.glue.psa.NcpehPerfReportingTestSteps;
import de.gematik.test.ncp.reporting.ErrorCode;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportEntry;
import de.gematik.test.ncp.screenplay.abilities.ProvidePerformanceReportData;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.AllArgsConstructor;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

@AllArgsConstructor
public class IsPerformanceUseCasesDataKnown implements Question<Boolean> {

  final NcpehPerfReportingTestSteps.PerformanceUseCases performanceUsecase;

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var entries =
        actor
            .usingAbilityTo(ProvidePerformanceReportData.class)
            .getPerformanceReport()
            .getEntries();
    return entries.size() == performanceUsecase.getPerformanceUseCases().size()
        && IntStream.range(0, entries.size())
            .allMatch(
                i ->
                    isCountryCodeEqual(entries.get(i))
                        && isOperationEqual(entries.get(i), i)
                        && isErrorCodeEqual(entries.get(i), i));
  }

  private boolean isOperationEqual(final NcpehPerformanceReportEntry entry, final int i) {
    return entry.operation().equals(performanceUsecase.getPerformanceUseCases().get(i));
  }

  private boolean isCountryCodeEqual(final NcpehPerformanceReportEntry entry) {
    return entry
        .message()
        .reqc()
        .equals(
            EuCountryCode.valueOf(performanceUsecase.getCountry().toUpperCase()).getCountryCode());
  }

  private boolean isErrorCodeEqual(final NcpehPerformanceReportEntry entry, final int i) {
    final var errorCode =
        Optional.ofNullable(
                performanceUsecase.getErrorCodes().size() > i
                    ? performanceUsecase.getErrorCodes().get(i)
                    : null)
            .map(ErrorCode::toString)
            .orElse(null);
    return Objects.equals(entry.message().err(), errorCode);
  }

  public static IsPerformanceUseCasesDataKnown fromPerformanceUseCases(
      final NcpehPerfReportingTestSteps.PerformanceUseCases performanceUsecase) {
    return Instrumented.instanceOf(IsPerformanceUseCasesDataKnown.class)
        .withProperties(performanceUsecase);
  }
}
