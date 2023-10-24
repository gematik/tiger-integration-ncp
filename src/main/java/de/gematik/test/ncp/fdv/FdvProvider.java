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
import de.gematik.test.ncp.util.Utils;
import lombok.Getter;

public class FdvProvider {

  public static final String FDV_SIM_CONFIG_KEY =
      ExternalServerConfig.INFRASTRUCTURE_KEY + ".epafdv";

  @Getter(lazy = true)
  private static final FdvProvider instance = new FdvProvider();

  public static FdvInterface getDefaultFdvClient() {
    return getInstance().getFdvClient();
  }

  @Getter(lazy = true)
  private final ExternalServerConfig epaFdvConfig =
      Utils.loadConfig(ExternalServerConfig.class, FDV_SIM_CONFIG_KEY);

  @Getter(lazy = true)
  // Hier wird zwar die FdvInterfaceImpl erstellt, aber letzlich führt diese nichts aus.
  private final FdvInterface fdvClient = new FdvInterfaceImpl(getEpaFdvConfig());
}
