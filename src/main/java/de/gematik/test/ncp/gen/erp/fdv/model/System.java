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

package de.gematik.test.ncp.gen.erp.fdv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Gets or Sets System */
public enum System {
  ERP_FD("ERP-FD"),

  EXPORTER("Exporter");

  private String value;

  System(String value) {
    this.value = value;
  }

  /**
   * Convert a String into String, as specified in the <a
   * href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0
   * Specification, section 3.2, p. 12</a>
   */
  public static System fromString(String s) {
    for (System b : System.values()) {
      // using Objects.toString() to be safe if value type non-object type
      // because types like 'int' etc. will be auto-boxed
      if (java.util.Objects.toString(b.value).equals(s)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected string value '" + s + "'");
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static System fromValue(String value) {
    for (System b : System.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
