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

import de.gematik.test.ncp.data.Testdata;
import java.util.Comparator;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.comparators.BooleanComparator;
import org.jetbrains.annotations.NotNull;

/**
 * Class to be used as answer type in {@link net.serenitybdd.screenplay.Question}s. Offers a chained
 * API, to further run a workflow based on the answer to a question
 *
 * @param <T>
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class Answer<T> implements Comparable<T> {

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private static final Boolean doNotFail = Testdata.instance().doNotFail();

  @Getter private final Comparator<T> comparator;

  @Getter private final T expectation;

  @Getter private final T value;

  @Accessors(chain = true)
  @Setter
  private String failureMessage = "";

  /**
   * Throw an {@link AssertionError} if the Answer does not have the expected value.
   *
   * @return the Answer, in case it is as expected
   */
  public Answer<T> failOtherwise() {
    if (!valueAsExpected() && !getDoNotFail()) {
      throw new AssertionError(failureMessage);
    }
    return this;
  }

  /**
   * Try another option, if the Answer does not have the expected value.<br>
   * The idea behind this function is similar to {@link java.util.Optional#or(Supplier)}.
   *
   * @param code the {@link Supplier to produce the alternative answer}
   * @return this Answer, or the Answer created by the Supplier, if the value of the original Answer
   *     is not as expected.
   */
  public Answer<T> ifNotTry(Supplier<Answer<T>> code) {
    if (valueAsExpected()) {
      return this;
    } else {
      return code.get();
    }
  }

  /**
   * Return a boolean describing whether the answer has the expected value
   *
   * @return {@code boolean} the answer has the expected value (true) or it doesn't (false)
   */
  public boolean valueAsExpected() {
    return comparator.compare(this.expectation, this.value) == 0;
  }

  /**
   * Create an instance of Answer with a Boolean value and the expectation {@link Boolean#TRUE}.
   *
   * @param value {@link Boolean} the value of the Answer
   * @return the created Answer
   */
  public static Answer<Boolean> booleanAnswer(Boolean value) {
    return new Answer<>(BooleanComparator.getTrueFirstComparator(), true, value);
  }

  /**
   * Create an instance of Answer with a Boolean value and the expectation {@link Boolean#TRUE}.
   *
   * @param value {@link Boolean} the value of the Answer
   * @param failureMessage {@link String} the message to use in the AssertionError, in case the
   *     answer is not as expected
   * @return the created Answer
   */
  public static Answer<Boolean> booleanAnswer(Boolean value, @NonNull String failureMessage) {
    return booleanAnswer(value).setFailureMessage(failureMessage);
  }

  @Override
  public int compareTo(@NotNull T o) {
    return comparator.compare(value, o);
  }
}
