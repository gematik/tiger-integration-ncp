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

package de.gematik.test.ncp.ps;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.gen.epa.api.documents.dto.ResponseDTO;
import org.junit.jupiter.api.Test;

class PsExceptionTest {

  private final ResponseDTO responseTestdata =
      new ResponseDTO().success(false).statusMessage("Something went wrong");

  @Test
  void psExceptionNoOp() {
    // Arrange
    // Act
    final var testee = new PsException(responseTestdata.getStatusMessage());

    // Assert
    assertTrue(
        testee.getMessage().contains(responseTestdata.getStatusMessage()),
        "response status message is not part of the PsException message");
    assertNull(testee.getOperation(), "Operation is not null");
  }

  @Test
  void psExceptionOp() {
    // Arrange
    final String operationTestdata = "unittest";

    // Act
    final var testee = new PsException(responseTestdata.getStatusMessage(), operationTestdata);

    // Assert
    assertTrue(
        testee.getMessage().contains(responseTestdata.getStatusMessage()),
        "response status message is not part of the PsException message");
    assertEquals(operationTestdata, testee.getOperation(), "Operation value is wrong");
  }
}
