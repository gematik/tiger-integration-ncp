/*
 * Copyright 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.*;

/** The purposeId is the key value of the research data purpose */
public enum PurposeIdType {
  PURPOSE1("purpose1"),

  PURPOSE2("purpose2"),

  PURPOSE3("purpose3"),

  PURPOSE4("purpose4"),

  PURPOSE5("purpose5"),

  PURPOSE6("purpose6"),

  PURPOSE7("purpose7"),

  PURPOSE8("purpose8"),

  PURPOSE9("purpose9"),

  PURPOSE10("purpose10");

  private String value;

  PurposeIdType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static PurposeIdType fromValue(String value) {
    for (PurposeIdType b : PurposeIdType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
