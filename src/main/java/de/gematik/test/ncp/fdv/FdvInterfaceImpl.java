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

package de.gematik.test.ncp.fdv;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.Testdata;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Accessors(fluent = true)
public class FdvInterfaceImpl implements FdvInterface {

  // TODO: Remove once a proper FdV integration has been implemented
  private static final String ACCESS_CODE = Testdata.HARD_CODED_ACCESS_CODE;

  protected final ExternalServerConfig config;

  @Override
  public boolean isUpAndRunning() {
    return true;
    // TODO: Activate actual implementation once a jakarta based implementation of
    //  epa-fdv-td-api is integrated in the project

  }

  @Override
  public String authorizeEuCountry(String kvnr, String euCountry) {
    // TODO: Implement a proper mechanism to retrieve an actual access code,
    // once an FdV is properly integrated
    return ACCESS_CODE;
  }
}
