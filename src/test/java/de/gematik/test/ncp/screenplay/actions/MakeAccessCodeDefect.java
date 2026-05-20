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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

public class MakeAccessCodeDefect implements Performable {

  // the accessCode pattern: [A-Za-z0-9]{6}
  private static final String ACCESS_CODE_V1 = "111111";
  private static final String ACCESS_CODE_V2 = "222222";

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var patientData = actor.usingAbilityTo(ProvidePatientData.class);
    patientData.accessCode(generateAccessCodeDefect(patientData.accessCode()));
  }

  public static MakeAccessCodeDefect instance() {
    return Instrumented.instanceOf(MakeAccessCodeDefect.class).newInstance();
  }

  private String generateAccessCodeDefect(final String accessCode) {
    return ACCESS_CODE_V1.equals(accessCode) ? ACCESS_CODE_V2 : ACCESS_CODE_V1;
  }
}
