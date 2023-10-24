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

import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Consequence;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Question;

/**
 * Extension of the {@link net.serenitybdd.screenplay.Actor} class to make its API more chained.
 *
 * @param <T> Class type of the actual implementation of this class
 */
public abstract class Actor<T extends Actor<?>> extends net.serenitybdd.screenplay.Actor {

  public Actor(String name) {
    super(name);
  }

  protected abstract T that();

  public T attemptsTo(Performable task) {
    super.attemptsTo(task);
    return that();
  }

  public T wasAbleTo(Performable task) {
    super.wasAbleTo(task);
    return that();
  }

  /**
   * Returns the Answer only if it has the expected value, otherwise it fails
   *
   * @param question the {@link Question}
   * @return the expected {@link Answer}
   * @param <S> inner type of the Answer
   */
  public <S> Answer<S> acknowledgeThat(Question<Answer<S>> question) {
    return this.asksFor(question).failOtherwise();
  }

  public <S> T verifyThat(Question<Answer<S>> question) {
    acknowledgeThat(question);
    return that();
  }

  public <S> T verifyThat(Consequence<S> consequence) {
    this.should(consequence);
    return that();
  }

  @Override
  public <S extends Ability> T can(S doSomething) {
    super.can(doSomething);
    return that();
  }
}
