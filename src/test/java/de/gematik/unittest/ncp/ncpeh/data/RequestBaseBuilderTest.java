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
import de.gematik.test.ncp.ncpeh.data.FindDocumentsRequestBuilder;
import org.junit.jupiter.api.Test;

class RequestBaseBuilderTest {

  public static final EuCountryCode EU_COUNTRY_CODE = EuCountryCode.FRANCE;

  public static final String IDA_APN = "Ida";

  public static final String KVNR = "X295743851";

  public static final String ACCESS_CODE = "ABC123";

  @Test
  void buildRequestBase() {
    var tstObj =
        FindDocumentsRequestBuilder.newInstance()
            .euCountryCode(EU_COUNTRY_CODE)
            .idaAssertionProfileName(IDA_APN)
            .kvnr(KVNR)
            .accessCode(ACCESS_CODE);

    var requestBase =
        assertDoesNotThrow(tstObj::buildRequestBase, "Method buildRequestBase threw exception");

    assertEquals(
        EU_COUNTRY_CODE,
        requestBase.euCountryCode(),
        "EuCountryCode does not have the expected value");
    assertEquals(
        IDA_APN,
        requestBase.idaAssertionProfile().assertionProperties().profileName(),
        "IDA assertion profile name does not have the expected value");
  }

  @Test
  void buildPatientId() {
    var tstObj =
        FindDocumentsRequestBuilder.newInstance()
            .euCountryCode(EU_COUNTRY_CODE)
            .idaAssertionProfileName(IDA_APN)
            .kvnr(KVNR);

    var patientId =
        assertDoesNotThrow(tstObj::buildPatientId, "Method buildRequestBase threw exception");

    assertEquals(KVNR, patientId.kvnr(), "KVNR does not have the expected value");
  }
}
