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

package de.gematik.unittest.ncp.ncpeh.data;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.ncpeh.data.IdentifyPatientRequestBuilder;
import org.junit.jupiter.api.Test;

class IdentifyPatientRequestBuilderTest {

  @Test
  void build() {
    var tstObj =
        IdentifyPatientRequestBuilder.newInstance()
            .accessCodeAssigningAuthority("2.25.1593158")
            .euCountryCode(EuCountryCode.AUSTRIA)
            .idaAssertionProfileName("ida")
            .kvnr("X123456789")
            .accessCode("ABC123");

    var result = assertDoesNotThrow(tstObj::build, "Method build threw exception");
  }
}
