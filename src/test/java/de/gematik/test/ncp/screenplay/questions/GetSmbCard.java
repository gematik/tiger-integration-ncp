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

import de.gematik.test.ncp.data.SmbCard;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import lombok.NoArgsConstructor;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

@NoArgsConstructor
public class GetSmbCard implements Question<SmbCard> {

  @Override
  public SmbCard answeredBy(final Actor actor) {
    final var ps = actor.usingAbilityTo(UsePrimarySystem.class);
    return ps.getSmbCard();
  }

  public static GetSmbCard instance() {
    return Instrumented.instanceOf(GetSmbCard.class).newInstance();
  }
}
