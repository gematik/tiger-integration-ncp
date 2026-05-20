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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.*;

/** A technical identifier of a category */
public enum CategoryIdType {
  REPORTS("reports"),

  EMP("emp"),

  EMERGENCY("emergency"),

  EAB("eab"),

  DENTAL("dental"),

  CHILDSRECORD("childsrecord"),

  CHILD("child"),

  PREGNANCY_CHILDBIRTH("pregnancy_childbirth"),

  VACCINATION("vaccination"),

  PATIENT("patient"),

  RECEIPT("receipt"),

  CARE("care"),

  EAU("eau"),

  REHAB("rehab"),

  DIGA("diga"),

  TRANSCRIPTS("transcripts"),

  OTHER("other");

  private String value;

  CategoryIdType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CategoryIdType fromValue(String value) {
    for (CategoryIdType b : CategoryIdType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
