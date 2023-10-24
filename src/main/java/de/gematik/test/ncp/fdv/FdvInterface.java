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

public interface FdvInterface {

  /**
   * Check, whether the FdV is available
   *
   * @return true if it is, false otherwise
   */
  boolean isUpAndRunning();

  /**
   * Authorize the NCPeH of another EU country for the given Aktenkonto
   *
   * @param kvnr identifier of the Aktenkonto
   * @param euCountry name of the country whose NCPeH is to be authorized
   * @return the access code created for access to the patients Aktenkonto by a practitioner in the
   *     given country
   */
  String authorizeEuCountry(String kvnr, String euCountry);
}
