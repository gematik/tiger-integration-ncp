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

package de.gematik.test.ncp.reporting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
  ERROR_PI_NO_MATCH("ERROR_PI_NO_MATCH"),
  ERROR_PI_GENERIC("ERROR_PI_GENERIC"),
  ERROR_GENERIC_DOCUMENT_MISSING("ERROR_GENERIC_DOCUMENT_MISSING"),
  ERROR_NO_CONSENT("ERROR_NO_CONSENT");

  private final String value;

  ErrorCode(final String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  @Override
  @JsonValue
  public String toString() {
    return value;
  }

  @JsonCreator
  public static ErrorCode fromValue(final String value) {
    for (final ErrorCode b : ErrorCode.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}
