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

package de.gematik.unittest.ncp.ps;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.ps.PsException;
import org.junit.jupiter.api.Test;

class PsExceptionTest {

  private final de.gematik.epa.dto.response.ResponseDTO responseTestdata =
      new de.gematik.epa.dto.response.ResponseDTO(false, "Something went wrong");

  private final String operationTestdata = "unittest";

  @Test
  void psExceptionNoOp() {
    var tstObj = new PsException(responseTestdata.statusMessage());
    assertTrue(
        tstObj.getMessage().contains(responseTestdata.statusMessage()),
        "response status message is not part of the PsException message");
    assertNull(tstObj.getOperation(), "Operation is not null");
  }

  @Test
  void psExceptionOp() {
    var tstObj = new PsException(responseTestdata.statusMessage(), operationTestdata);
    assertTrue(
        tstObj.getMessage().contains(responseTestdata.statusMessage()),
        "response status message is not part of the PsException message");
    assertEquals(operationTestdata, tstObj.getOperation(), "Operation value is wrong");
  }
}
