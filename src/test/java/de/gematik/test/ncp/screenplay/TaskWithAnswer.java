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

package de.gematik.test.ncp.screenplay;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Task;

/**
 * Convenience class, that implements both the {@link Question} and the {@link Task} interface. The
 * result of the one method in need of implementation {@link #performWithAnswer(Actor)} is stored in
 * the notepad of the actor and returned when used as Question.<br>
 * This makes it possible to have a task run and deal with result at a later point in the workflow
 *
 * @param <ANSWER>
 */
public abstract class TaskWithAnswer<ANSWER>
    implements Task, Question<ANSWER>, Parameterizable<TaskWithAnswer<ANSWER>> {

  @Getter(lazy = true)
  private final String id = UUID.randomUUID().toString();

  private final Map<String, Object> parameter = new ConcurrentHashMap<>();

  public abstract <T extends Actor> ANSWER performWithAnswer(T actor);

  @Override
  public TaskWithAnswer<ANSWER> useParameter(String name, Object value) {
    parameter.put(name, value);
    return this;
  }

  @Override
  public Object getParameter(String name) {
    return parameter.get(name);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    actor.remember(getId(), this.performWithAnswer(actor));
  }

  @Override
  public ANSWER answeredBy(Actor actor) {
    return actor.recall(getId());
  }
}
