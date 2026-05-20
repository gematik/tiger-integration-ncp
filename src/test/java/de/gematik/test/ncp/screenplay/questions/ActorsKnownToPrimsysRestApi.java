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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.ActorType;
import de.gematik.test.ncp.screenplay.abilities.UseThePrimsysRestApi;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

/**
 * Question to get a list of actors with predefined identities from the Primsys REST API, optionally
 * filtered by role and name.
 */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Slf4j
public class ActorsKnownToPrimsysRestApi implements Question<List<ActorDto>> {

  private final ActorType role;
  private final String name;

  /**
   * Creates a question instance to retrieve all actors known to the Primsys REST API.
   *
   * @return An instance of {@link ActorsKnownToPrimsysRestApi} without role or name filtering.
   */
  public static ActorsKnownToPrimsysRestApi withAnyRole() {
    return new ActorsKnownToPrimsysRestApi();
  }

  /**
   * Creates a question instance to retrieve actors known to the Primsys REST API filtered by the
   * specified role.
   *
   * @param role The role to filter actors by.
   * @return An instance of {@link ActorsKnownToPrimsysRestApi} filtered by the specified role.
   */
  public static ActorsKnownToPrimsysRestApi withRole(final ActorType role) {
    return new ActorsKnownToPrimsysRestApi(role, null);
  }

  /**
   * Creates a question instance to retrieve actors known to the Primsys REST API filtered by the
   * specified role and name.
   *
   * @param role The role to filter actors by.
   * @param name The name to filter actors by.
   * @return An instance of {@link ActorsKnownToPrimsysRestApi} filtered by the specified role and
   *     name.
   */
  public static ActorsKnownToPrimsysRestApi withRoleAndName(
      final ActorType role, final String name) {
    return new ActorsKnownToPrimsysRestApi(role, name);
  }

  @Override
  public List<ActorDto> answeredBy(final Actor actor) {
    final var client = actor.usingAbilityTo(UseThePrimsysRestApi.class).getClient();
    return client.getActors(role, name);
  }
}
