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

package de.gematik.test.ncp.ncpeh;

import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.GeneralFactory;
import de.gematik.test.ncp.ncpeh.client.NcpehClientImpl;
import de.gematik.test.ncp.reporting.NcpehContext;
import de.gematik.test.ncp.util.Utils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.cxf.jaxrs.client.Client;

/**
 * Singleton class to provide the instance of an {@link NcpehService} implementation. Currently,
 * quite trivial, as only one implementation exists ({@link NcpehClientImpl}).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NcpehProvider {

  public static final String NCPEH_SIMULATION_CONFIG_KEY =
      ExternalServerConfig.EXTERNAL_SERVER_CONFIG_TIGER_KEY + ".ncpehSim";

  @Getter(lazy = true)
  private static final NcpehProvider instance = new NcpehProvider();

  @Getter(lazy = true)
  private final NcpehService ncpehImpl =
      NcpehClientImpl.builder()
          .config(getNcpehConfig())
          .clientProxy(
              GeneralFactory.createNcpehTimeLoggingProxy(
                  GeneralFactory.createJAXRSClientProxy(NcpehSimulatorApi.class, getNcpehConfig()),
                  getNcpehContext(),
                  NcpehSimulatorApi.class,
                  Client.class))
          .build();

  @Getter(lazy = true)
  private final ExternalServerConfig ncpehConfig =
      Utils.loadConfig(ExternalServerConfig.class, NCPEH_SIMULATION_CONFIG_KEY);

  @Getter(lazy = true)
  private final NcpehContext ncpehContext = new NcpehContext();

  public static NcpehService getNcpehService() {
    return getInstance().getNcpehImpl();
  }
}
