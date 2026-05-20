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

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.test.ncp.fdv.FdvProvider;
import de.gematik.test.ncp.fdv.FdvService;
import de.gematik.test.ncp.gen.eu.fdv.model.GetEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehResponseDTO;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.HasTeardown;

public class UseFdv implements Ability, HasTeardown {

  public static final String FDV_MOCK_RESPONSE_TIGER_CONFIGURATION_KEY = "fdVMockResponse";
  private final FdvService service;
  private final List<String> fdvMockRequestCriterions;

  private String fqdn;

  public UseFdv(final List<String> fdvMockRequestCriterions) {
    this(FdvProvider.getDefaultFdvClient(), fdvMockRequestCriterions);
  }

  public UseFdv(final FdvService service, final List<String> fdvMockRequestCriterions) {
    this.service = service;
    this.fdvMockRequestCriterions = new ArrayList<>(fdvMockRequestCriterions);
  }

  public Optional<GetEntitlementNcpehResponseDTO> getEntitlementNcpeh(final String kvnr) {
    login(kvnr);
    final var requestCriterion =
        fdvMockRequestCriterions.isEmpty() ? "" : fdvMockRequestCriterions.removeFirst();
    TigerGlobalConfiguration.putValue(FDV_MOCK_RESPONSE_TIGER_CONFIGURATION_KEY, requestCriterion);
    return service.getEntitlementNcpeh(kvnr);
  }

  public Optional<PutEntitlementNcpehResponseDTO> authorizeEuCountry(
      final String kvnr, final String euCountry) {
    login(kvnr);
    return service.authorizeEuCountry(kvnr, euCountry);
  }

  public void deleteEntitlementNcpeh(final String kvnr) {
    login(kvnr);
    service.deleteEntitlementNcpeh(kvnr);
  }

  private void login(final String kvnr) {
    if (isLoggedIn()) return;
    this.fqdn =
        service
            .login(kvnr)
            .orElseThrow(() -> new IllegalStateException("Login failed for KVNR: " + kvnr));
  }

  private boolean isLoggedIn() {
    return fqdn != null;
  }

  @Override
  public void tearDown() {
    service.logout(fqdn);
  }
}
