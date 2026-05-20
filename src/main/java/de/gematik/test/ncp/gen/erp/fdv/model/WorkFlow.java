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

/** See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_EX_PrescriptionType */
public enum WorkFlow {
  _160("160"),

  _169("169"),

  _200("200"),

  _209("209"),

  _162("162");

  private String value;

  WorkFlow(String value) {
    this.value = value;
  }

  /**
   * Convert a String into String, as specified in the <a
   * href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS 2.0
   * Specification, section 3.2, p. 12</a>
   */
  public static WorkFlow fromString(String s) {
    for (WorkFlow b : WorkFlow.values()) {
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
  public static WorkFlow fromValue(String value) {
    for (WorkFlow b : WorkFlow.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
