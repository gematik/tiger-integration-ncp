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

package de.gematik.test.ncp.ncpeh;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.ncpeh.api.response.ErrorInformation;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class NcpehExceptionTest {

  @Test
  void testConstructor() {
    // Arrange
    // Act
    // Assert
    assertDoesNotThrow(
        () -> new NcpehException("exception message"), "constructor threw unexpected exception");

    assertDoesNotThrow(
        () -> new NcpehException("exception message", new Exception()),
        "constructor threw unexpected exception");

    assertDoesNotThrow(
        () -> new NcpehException("exception message", Response.ok().build()),
        "constructor threw unexpected exception");
  }

  @Test
  void getMessage() {
    // Arrange
    final ErrorInformation errInf = new ErrorInformation("error information message");
    var exception =
        assertDoesNotThrow(
            () -> new NcpehException("exception message", Response.ok().entity(errInf).build()),
            "constructor threw unexpected exception");

    // Act
    // Assert
    assertEquals(
        errInf.errorMessage(),
        exception.getAdditionalErrorInfo(),
        "Additional error information did not have the expected value");
    assertTrue(
        exception.getMessage().contains(errInf.errorMessage()),
        "additional error info was not part of the exception message");

    exception =
        assertDoesNotThrow(
            () ->
                new NcpehException("exception message", Response.ok().entity("some data").build()),
            "constructor threw unexpected exception");
    assertNull(exception.getAdditionalErrorInfo(), "additional error information was not null");
  }
}
