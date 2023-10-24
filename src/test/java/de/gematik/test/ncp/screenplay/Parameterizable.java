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

public interface Parameterizable<T extends Parameterizable<?>> {

  T useParameter(String name, Object value);

  Object getParameter(String name);

  /**
   * Return the parameter value to the given parameter name.<br>
   * If the value is not of the expected type, null will be returned
   *
   * @param name {@link String} the name of the parameter
   * @return the value of the parameter, or null if it's either not present or not of the expected
   *     type
   * @param <S> Type of the value object
   */
  default <S> S getTypedParameter(String name) {
    try {
      return (S) getParameter(name);
    } catch (ClassCastException cce) {
      return null;
    }
  }
}
