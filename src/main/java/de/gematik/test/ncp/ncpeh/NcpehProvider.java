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

package de.gematik.test.ncp.ncpeh;

import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.GeneralFactory;
import de.gematik.test.ncp.ncpeh.client.NcpehClientImpl;
import de.gematik.test.ncp.util.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Singleton class to provide the instance of an {@link NcpehService} implementation. Currently,
 * quite trivial, as only one implementation exists ({@link NcpehClientImpl}).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NcpehProvider {

  public static final String NCPEH_SIMULATION_CONFIG_KEY =
      ExternalServerConfig.INFRASTRUCTURE_KEY + ".ncpehSim";

  @Getter(lazy = true)
  private static final NcpehProvider instance = new NcpehProvider();

  @Getter(lazy = true)
  private final NcpehService ncpehImpl =
      NcpehClientImpl.builder()
          .config(getNcpehConfig())
          .clientProxy(
              GeneralFactory.createJAXRSClientProxy(NcpehSimulatorApi.class, getNcpehConfig()))
          .build();

  @Getter(lazy = true)
  private final ExternalServerConfig ncpehConfig =
      Utils.loadConfig(ExternalServerConfig.class, NCPEH_SIMULATION_CONFIG_KEY);

  public static NcpehService getNcpehService() {
    return getInstance().getNcpehImpl();
  }
}
