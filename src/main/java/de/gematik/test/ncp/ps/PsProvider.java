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

package de.gematik.test.ncp.ps;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.ps.epaps.EpaPsInterfaceImpl;
import de.gematik.test.ncp.util.Utils;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import lombok.*;

/**
 * Singleton class to provide a centralized point to retrieve the implementation of the {@link
 * PsInterface}, which is to be used during testcase execution.<br>
 * The default implementation of the interface provided is {@link EpaPsInterfaceImpl}.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PsProvider {

  public static final String PS_SIM_CONFIG_KEY = ExternalServerConfig.INFRASTRUCTURE_KEY + ".epaps";

  public static final String PS_JAR_ACTIVE_KEY = "tiger.servers.epaPsJar.active";

  public static final String PS_EXT_ACTIVE_KEY = "tiger.servers.epaPsUrl.active";

  @Getter(lazy = true)
  private static final PsProvider psProvider = new PsProvider();

  public final AtomicReference<Supplier<PsInterface>> defaultPsImpl = new AtomicReference<>();

  @Getter(lazy = true)
  private final PsInterface psImpl =
      ((Utils.loadConfig(Boolean.class, PS_JAR_ACTIVE_KEY)
              || Utils.loadConfig(Boolean.class, PS_EXT_ACTIVE_KEY))
          ? new EpaPsInterfaceImpl(getEpaPsConfig())
          : defaultPsImpl.get().get());

  @Getter(lazy = true)
  private final ExternalServerConfig epaPsConfig =
      Utils.loadConfig(ExternalServerConfig.class, PS_SIM_CONFIG_KEY);

  /**
   * Get the {@link PsInterface} implementation to use
   *
   * @return {@link PsInterface} implementation to be used
   */
  @Synchronized
  public static PsInterface getPsImplementation() {
    return getPsProvider().getPsImpl();
  }
}
