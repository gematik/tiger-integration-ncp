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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.screenplay.questions.GetNumberOfPatientSummaryDocuments;
import de.gematik.test.ncp.screenplay.questions.GetRegistryErrorCodesFromAdhocQueryResponse;
import de.gematik.test.ncp.screenplay.questions.IsFindPatientSummaryResponseStatusFailure;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.ensure.Ensure;

@Slf4j
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ConfirmThatSearchForPatientSummaryFails implements Performable {

  private final String errorCode;

  @Override
  public <T extends Actor> void performAs(final T actor) {
    actor.attemptsTo(Ensure.that(new GetNumberOfPatientSummaryDocuments()).isEqualTo(0L));
    actor.attemptsTo(Ensure.that(new IsFindPatientSummaryResponseStatusFailure()).isTrue());
    actor.attemptsTo(
        Ensure.that(new GetRegistryErrorCodesFromAdhocQueryResponse()).contains(errorCode));
  }

  public static ConfirmThatSearchForPatientSummaryFails withErrorCode(final String errorCode) {
    return Instrumented.instanceOf(ConfirmThatSearchForPatientSummaryFails.class)
        .withProperties(errorCode);
  }
}
