/*
 * Copyright (c) 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.fdv.impl;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.fdv.FdvService;
import de.gematik.test.ncp.gen.eu.fdv.api.EntitleManagementEuApi;
import de.gematik.test.ncp.gen.eu.fdv.model.GetEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehRequestDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.fdv.api.LoginLogoutApi;
import de.gematik.test.ncp.gen.fdv.model.LoginResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.ResponseDTO;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
@RequiredArgsConstructor
public class FdvServiceImpl implements FdvService {

  private static final String SUCCESSFUL = "successful";
  private static final String NOT_SUCCESSFUL = "not successful";
  @NonNull private final ExternalServerConfig config;

  @Accessors(fluent = true)
  @Getter
  private final EntitleManagementEuApi entitleManagementEuApiProxy;

  @Accessors(fluent = true)
  @Getter
  private final LoginLogoutApi loginLogoutApiProxy;

  @Override
  public boolean isUpAndRunning() {
    final var response = entitleManagementEuApiProxy.getEntitlementNcpeh("recordId");
    return response.getSuccess();
  }

  /**
   * Login to the FdV with the given KVNR
   *
   * @param kvnr identifier of the Aktenkonto
   * @return the Optional<String> fqdn of the FdV
   */
  @Override
  public Optional<String> login(final String kvnr) {
    final LoginResponseDTO response = loginLogoutApiProxy.login(kvnr, null, null);

    log.info(
        "Login to FdV for KVNR: {} was {} {}",
        kvnr,
        response.getSuccess() ? SUCCESSFUL : NOT_SUCCESSFUL,
        response.getSuccess() ? "" : response.getStatusMessage());

    return response.getSuccess() ? Optional.of(response.getFqdn()) : Optional.empty();
  }

  /**
   * Logout from the FdV with the given FQDN
   *
   * @param fqdn fqdn of the FdV
   */
  @Override
  public void logout(final String fqdn) {
    final ResponseDTO response = loginLogoutApiProxy.logout(fqdn);
    log.info(
        "Logout from FdV with FQDN: {} was {} {}",
        fqdn,
        response.getSuccess() ? SUCCESSFUL : NOT_SUCCESSFUL,
        response.getSuccess() ? "" : response.getStatusMessage());
  }

  /**
   * Get the entitlements of the given Aktenkonto
   *
   * @param kvnr identifier of the Aktenkonto
   * @return Optional<GetEntitlementNcpehResponseDTO> the entitlement for ncpeh
   */
  @Override
  public Optional<GetEntitlementNcpehResponseDTO> getEntitlementNcpeh(final String kvnr) {
    final GetEntitlementNcpehResponseDTO response =
        entitleManagementEuApiProxy.getEntitlementNcpeh(kvnr);

    log.info(
        "Get Entitlement NCPeH for KVNR: {} was {} {}",
        kvnr,
        response.getSuccess() ? SUCCESSFUL : NOT_SUCCESSFUL,
        response.getSuccess() ? "" : response.getStatusMessage());

    return response.getSuccess() ? Optional.of(response) : Optional.empty();
  }

  /**
   * Authorize the NCPeH of another EU country for the given Aktenkonto
   *
   * @param kvnr identifier of the Aktenkonto
   * @param euCountry name of the country whose NCPeH is to be authorized
   * @return the PutEntitlementNcpehResponseDTO for the patients Aktenkonto by a practitioner in the
   *     given country
   */
  @Override
  public Optional<PutEntitlementNcpehResponseDTO> authorizeEuCountry(
      final String kvnr, final String euCountry) {

    final PutEntitlementNcpehResponseDTO response =
        entitleManagementEuApiProxy.putEntitlementNcpeh(
            new PutEntitlementNcpehRequestDTO().recordId(kvnr).countryName(euCountry));

    log.info(
        "Authorization of NCPeH for country {} for KVNR: {} was {} {}",
        euCountry,
        kvnr,
        response.getSuccess() ? SUCCESSFUL : NOT_SUCCESSFUL,
        response.getSuccess() ? "" : response.getStatusMessage());

    return response.getSuccess() ? Optional.of(response) : Optional.empty();
  }

  /**
   * Delete the entitlement for the given Aktenkonto
   *
   * @param kvnr identifier of the Aktenkonto
   */
  @Override
  public void deleteEntitlementNcpeh(final String kvnr) {
    final var response = entitleManagementEuApiProxy.deleteEntitlementNcpeh(kvnr);
    log.info(
        "Delete Entitlement NCPeH for KVNR: {} was {} {}",
        kvnr,
        response.getSuccess() ? SUCCESSFUL : NOT_SUCCESSFUL,
        response.getSuccess() ? "" : response.getStatusMessage());
  }
}
