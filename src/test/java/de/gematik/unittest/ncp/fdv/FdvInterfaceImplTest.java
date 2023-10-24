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

package de.gematik.unittest.ncp.fdv;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.fdv.FdvInterfaceImpl;
import de.gematik.test.ncp.fdv.FdvProvider;
import org.junit.jupiter.api.Test;

class FdvInterfaceImplTest {

  @Test
  void isUpAndRunning() {
    var fdvInterfaceImpl = new FdvInterfaceImpl(FdvProvider.getInstance().getEpaFdvConfig());

    var result = assertDoesNotThrow(fdvInterfaceImpl::isUpAndRunning);

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  void authorizeEuCountry() {
    var fdvInterfaceImpl = new FdvInterfaceImpl(FdvProvider.getInstance().getEpaFdvConfig());

    var accessCode =
        assertDoesNotThrow(() -> fdvInterfaceImpl.authorizeEuCountry("X1209120947", "SPAIN"));

    assertNotNull(accessCode);
  }
}
