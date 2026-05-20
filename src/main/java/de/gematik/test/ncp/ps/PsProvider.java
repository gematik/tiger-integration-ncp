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

package de.gematik.test.ncp.ps;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.GeneralFactory;
import de.gematik.test.ncp.gen.epa.api.authentication.LoginLogoutApi;
import de.gematik.test.ncp.gen.epa.api.card.CardApi;
import de.gematik.test.ncp.gen.epa.api.configuration.ConfigurationApi;
import de.gematik.test.ncp.gen.epa.api.documents.DocumentsApi;
import de.gematik.test.ncp.gen.epa.api.entitlement.EntitlementApi;
import de.gematik.test.ncp.gen.epa.api.information.InformationApi;
import de.gematik.test.ncp.gen.epa.api.signature.SignatureApi;
import de.gematik.test.ncp.ps.epaps.EpaPrimarySystemServiceImpl;
import de.gematik.test.ncp.ps.epaps.PrimarySystemServiceMockImpl;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import lombok.*;

/**
 * Singleton class to provide a centralized point to retrieve the implementation of the {@link
 * PrimarySystemService}, which is to be used during testcase execution.<br>
 * The default implementation of the interface provided is {@link EpaPrimarySystemServiceImpl}.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PsProvider {

  public static final String PS_SIM_CONFIG_KEY =
      ExternalServerConfig.EXTERNAL_SERVER_CONFIG_TIGER_KEY + ".epaps";

  public static final String PS_DOCKER_COMPOSE_ACTIVE_KEY = "tiger.servers.epaPsComposeUrl.active";

  public static final String PS_EXT_ACTIVE_KEY = "tiger.servers.epaPsUrl.active";

  private static PsProvider psProvider;

  public final AtomicReference<Supplier<PrimarySystemService>> defaultPsImpl =
      new AtomicReference<>();

  public static PsProvider getInstance() {
    if (PsProvider.psProvider == null) {
      PsProvider.psProvider = new PsProvider();
      PsProvider.psProvider.defaultPsImpl.set(PrimarySystemServiceMockImpl::instance);
    }
    return PsProvider.psProvider;
  }

  @Getter(lazy = true)
  private final PrimarySystemService psImpl =
      ((TigerGlobalConfiguration.readBoolean(PS_DOCKER_COMPOSE_ACTIVE_KEY)
              || TigerGlobalConfiguration.readBoolean(PS_EXT_ACTIVE_KEY))
          ? EpaPrimarySystemServiceImpl.builder()
              .config(getEpaPsConfig())
              .documentsProxy(
                  GeneralFactory.createJAXRSClientProxy(DocumentsApi.class, getEpaPsConfig()))
              .entitlementProxy(
                  GeneralFactory.createJAXRSClientProxy(EntitlementApi.class, getEpaPsConfig()))
              .configurationProxy(
                  GeneralFactory.createJAXRSClientProxy(ConfigurationApi.class, getEpaPsConfig()))
              .signatureProxy(
                  GeneralFactory.createJAXRSClientProxy(SignatureApi.class, getEpaPsConfig()))
              .informationProxy(
                  GeneralFactory.createJAXRSClientProxy(InformationApi.class, getEpaPsConfig()))
              .loginLogoutProxy(
                  GeneralFactory.createJAXRSClientProxy(LoginLogoutApi.class, getEpaPsConfig()))
              .cardApiProxy(GeneralFactory.createJAXRSClientProxy(CardApi.class, getEpaPsConfig()))
              .build()
          : defaultPsImpl.get().get());

  @Getter(lazy = true)
  private final ExternalServerConfig epaPsConfig =
      Utils.loadConfig(ExternalServerConfig.class, PS_SIM_CONFIG_KEY);

  /**
   * Get the {@link PrimarySystemService} implementation to use
   *
   * @return {@link PrimarySystemService} implementation to be used
   */
  @Synchronized
  public static PrimarySystemService getPrimarySystemService() {
    return getInstance().getPsImpl();
  }
}
