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

package de.gematik.test.ncp.fdv;

import de.gematik.test.ncp.gen.eu.fdv.model.GetEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehResponseDTO;
import java.util.Optional;

public interface FdvService {

  /**
   * Login to the FdV with the given KVNR
   *
   * @param kvnr identifier of the Aktenkonto
   * @return the fqdn of the FdV
   */
  Optional<String> login(String kvnr);

  /**
   * Logout from the FdV with the given FQDN
   *
   * @param fqdn fqdn of the FdV
   */
  void logout(String fqdn);

  /**
   * Get the entitlements of the given Aktenkonto
   *
   * @param kvnr identifier of the Aktenkonto
   * @return Optional<GetEntitlementNcpehResponseDTO> the entitlement for ncpeh
   */
  Optional<GetEntitlementNcpehResponseDTO> getEntitlementNcpeh(String kvnr);

  /**
   * Authorize the NCPeH of another EU country for the given Aktenkonto
   *
   * @param kvnr identifier of the Aktenkonto
   * @param euCountry name of the country whose NCPeH is to be authorized
   * @return the Optional<PutEntitlementNcpehResponseDTO> for the patients Aktenkonto by a
   *     practitioner in the given country
   */
  Optional<PutEntitlementNcpehResponseDTO> authorizeEuCountry(String kvnr, String euCountry);

  /**
   * Delete the entitlement for the given Aktenkonto
   *
   * @param kvnr identifier of the Aktenkonto
   */
  void deleteEntitlementNcpeh(String kvnr);
}
