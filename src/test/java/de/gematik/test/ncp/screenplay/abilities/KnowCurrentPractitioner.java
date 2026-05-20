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

package de.gematik.test.ncp.screenplay.abilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;

/**
 * Ability to know the current practitioner (actor) in the test scenario. This is useful when a
 * patient needs to perform actions that refer to a previously encountered health professional who
 * is currently not on the stage.
 */
@RequiredArgsConstructor
public class KnowCurrentPractitioner implements Ability {

  @Getter private final Actor currentPractitioner;

  /**
   * Factory method to create the ability to know the specified current practitioner.
   *
   * @param acquaintance the actor representing the current practitioner
   * @return a new instance of the KnowCurrentPractitioner ability
   */
  public static KnowCurrentPractitioner as(final Actor acquaintance) {
    return new KnowCurrentPractitioner(acquaintance);
  }
}
