/*
 * Copyright (c) 2024. gematik GmbH
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

package de.gematik.unittest.ncp.ncpeh.data;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.ncpeh.data.IdentifyPatientRequestBuilder;
import de.gematik.test.ncp.ncpeh.data.RequestBaseBuilder;
import org.junit.jupiter.api.Test;

class IdentifyPatientRequestBuilderTest {

  @Test
  void build() {
    final var tstObj =
        IdentifyPatientRequestBuilder.newInstance()
            .accessCode(RequestBaseBuilder.KVNR_ASSIGNING_AUTHORITY)
            .euCountryCode(EuCountryCode.AUSTRIA)
            .idaAssertionProfileName("ida")
            .kvnr("X123456789")
            .accessCode("ABC123");

    final var result = assertDoesNotThrow(tstObj::build, "Method build threw exception");
  }
}
