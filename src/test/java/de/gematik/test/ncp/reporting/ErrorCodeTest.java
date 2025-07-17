/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.reporting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ErrorCodeTest {

  @ParameterizedTest
  @CsvSource({
    "ERROR_PI_NO_MATCH, ERROR_PI_NO_MATCH, ERROR_GENERIC_DOCUMENT_MISSING, ERROR_NO_CONSENT",
    "ERROR_PI_GENERIC, ERROR_PI_GENERIC, ERROR_GENERIC_DOCUMENT_MISSING, ERROR_NO_CONSENT"
  })
  void fromValueReturnsCorrectErrorCode(final String input, final String expected) {
    // Arrange & Act
    final ErrorCode errorCode = ErrorCode.fromValue(input);
    // Assert
    assertEquals(expected, errorCode.value());
  }

  @Test
  void fromValueThrowsExceptionForUnexpectedValue() {
    // Arrange & Act & Assert
    assertThrows(IllegalArgumentException.class, () -> ErrorCode.fromValue("UNEXPECTED_VALUE"));
  }

  @ParameterizedTest
  @CsvSource({
    "ERROR_PI_NO_MATCH, ERROR_PI_NO_MATCH, ERROR_GENERIC_DOCUMENT_MISSING, ERROR_NO_CONSENT",
    "ERROR_PI_GENERIC, ERROR_PI_GENERIC, ERROR_GENERIC_DOCUMENT_MISSING, ERROR_NO_CONSENT"
  })
  void toStringReturnsCorrectString(final String input, final String expected) {
    // Arrange & Act
    final ErrorCode errorCode = ErrorCode.valueOf(input);
    // Assert
    assertEquals(expected, errorCode.toString());
  }

  @ParameterizedTest
  @CsvSource({
    "ERROR_PI_NO_MATCH, ERROR_PI_NO_MATCH, ERROR_GENERIC_DOCUMENT_MISSING, ERROR_NO_CONSENT",
    "ERROR_PI_GENERIC, ERROR_PI_GENERIC, ERROR_GENERIC_DOCUMENT_MISSING, ERROR_NO_CONSENT"
  })
  void valueReturnsCorrectString(final String input, final String expected) {
    // Arrange & Act
    final ErrorCode errorCode = ErrorCode.valueOf(input);
    // Assert
    assertEquals(expected, errorCode.value());
  }
}
