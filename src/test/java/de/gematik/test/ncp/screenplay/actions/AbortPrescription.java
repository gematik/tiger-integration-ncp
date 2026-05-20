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

import static net.serenitybdd.screenplay.Tasks.instrumented;

import de.gematik.test.erezept.primsys.data.actors.ActorType;
import de.gematik.test.ncp.screenplay.abilities.UseThePrimsysRestApi;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class AbortPrescription implements Performable {

  private final ActorType role;
  private final String id;
  private final String taskId;
  private final String accessCode;

  @Override
  @Step("{0} aborts the prescription with ID #taskId")
  public <T extends Actor> void performAs(final T actor) {

    final var client = actor.usingAbilityTo(UseThePrimsysRestApi.class).getClient();

    switch (role) {
      case DOCTOR -> client.abortPrescriptionAsDoc(id, taskId, accessCode);
      case null -> throw new IllegalArgumentException("Actor ID cannot be null");
      default -> throw new UnsupportedOperationException("Unsupported actor type: " + role);
    }
  }

  public static class Builder {
    private final ActorType role;
    private final String id;

    public Builder(final ActorType role, final String id) {
      this.role = role;
      this.id = id;
    }

    public AbortPrescription withTaskIdAndAccessCode(final String taskId, final String accessCode) {
      return instrumented(AbortPrescription.class, role, id, taskId, accessCode);
    }
  }

  public static Builder as(final ActorType role, final String id) {
    return new Builder(role, id);
  }
}
