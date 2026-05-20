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

package de.gematik.test.ncp.erezept;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.GeneralFactory;
import de.gematik.test.ncp.erezept.impl.ErpFdvClient;
import de.gematik.test.ncp.erezept.impl.ErpFdvMockImpl;
import de.gematik.test.ncp.erp.primsys.PrimsysRestService;
import de.gematik.test.ncp.gen.erp.fdv.api.ErpApi;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.cxf.jaxrs.client.Client;

public final class ErpFdvProvider {
  /** Key for the configuration property that specifies the Erp FdV Simulation configuration */
  public static final String ERP_FDV_CONFIG_KEY =
      ExternalServerConfig.EXTERNAL_SERVER_CONFIG_TIGER_KEY + ".erpFdv";

  /** Key for the configuration property that indicates whether the Erp FdV Simulation is active. */
  public static final String ERP_FDV_ACTIVE_KEY = "tiger.servers.erpFdvDocker.active";

  /** Name of the HTTP header used to provide the API key for the Erp FdV */
  public static final String ERP_FDV_AUTH_HEADER_NAME = "Authorization";

  /** Name of the environment variable holding the API key for the Erp FdV */
  public static final String ERP_FDV_APIKEY_ENV_VAR = "ERP_FDV_API_KEY";

  private static ErpFdvProvider instance;

  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private final String apikey = TigerGlobalConfiguration.readString(ERP_FDV_APIKEY_ENV_VAR);

  @Getter(lazy = true)
  private final ExternalServerConfig erpFdvConfig =
      Utils.loadConfig(ExternalServerConfig.class, ERP_FDV_CONFIG_KEY);

  private ErpFdvService erpFdvClient;
  private PrimsysRestService primsysClientMock;

  private ErpFdvProvider() {}

  /** Provides the default Erp FdV client, either mock or real implementation. */
  public static ErpFdvService getDefaultErpFdvClient() {
    return getInstance().getErpFdvServiceImpl();
  }

  /** Provides the singleton instance of the ErpFdvProvider. */
  public static ErpFdvProvider getInstance() {
    if (instance == null) {
      instance = new ErpFdvProvider();
    }
    return instance;
  }

  /**
   * Setter for the PrimSys client which is attached to the Erp FdV mock implementation if used.
   *
   * @param primsysClient the PrimSys client mock to set
   */
  public void setPrimsysClient(final PrimsysRestService primsysClient) {
    this.primsysClientMock = primsysClient;
    if (erpFdvClient instanceof ErpFdvMockImpl) {
      ((ErpFdvMockImpl) erpFdvClient).setPrimsysClient(primsysClient);
    }
  }

  private ErpFdvService getErpFdvServiceImpl() {
    if (erpFdvClient == null) {
      if (useMock()) {
        erpFdvClient = ErpFdvMockImpl.builder().primsysClient(primsysClientMock).build();
      } else {
        final ErpApi erpApi =
            GeneralFactory.createJAXRSClientProxy(ErpApi.class, getErpFdvConfig());
        ((Client) erpApi).header(ERP_FDV_AUTH_HEADER_NAME, getApikey());
        erpFdvClient = new ErpFdvClient(erpApi);
      }
    }
    return erpFdvClient;
  }

  private boolean useMock() {
    return !TigerGlobalConfiguration.readBoolean(ERP_FDV_ACTIVE_KEY);
  }

  /** Resets the singleton instance for testing purposes. */
  static void reset() {
    instance = null;
  }
}
