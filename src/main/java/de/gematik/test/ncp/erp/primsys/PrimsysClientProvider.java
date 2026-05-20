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

package de.gematik.test.ncp.erp.primsys;

import de.gematik.test.erezept.primsys.PrimSysClientFactory;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.erezept.ErpFdvProvider;
import de.gematik.test.ncp.erp.primsys.impl.DoctorClient;
import de.gematik.test.ncp.erp.primsys.impl.DoctorClientMock;
import de.gematik.test.ncp.erp.primsys.impl.PrimsysClient;
import de.gematik.test.ncp.erp.primsys.impl.PrimsysClientMock;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import io.ktor.client.HttpClient;
import io.ktor.client.HttpClientConfig;
import io.ktor.client.engine.cio.CIO;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import kotlin.Unit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Provider for PrimSys client implementations that supports both real and mock clients. This class
 * uses lazy initialization to create client instances and provides access to {@link
 * PrimsysRestService} and {@link PrimsysDoctorService} through static factory methods. The
 * implementation selection (real vs mock) is controlled by configuration properties.
 */
@NoArgsConstructor
@Slf4j
public class PrimsysClientProvider {

  /** Key for the configuration property that indicates whether the PrimSys service is active. */
  public static final String PRIMSYS_ACTIVE_KEY = "tiger.servers.primsysUrl.active";

  /** Name of the HTTP header used to provide the API key for PrimSys REST */
  public static final String PRIMSYS_APIKEY_HEADER_NAME = "apikey";

  /** Name of the environment variable holding the API key for PrimSys REST */
  public static final String PRIMSYS_APIKEY_ENV_VAR = "ERPIONE_API_KEY";

  /** Key for the configuration property that specifies on which port the Tiger proxy is running */
  public static final String TIGER_PROXY_PORT_KEY = "tiger.ports.tigerProxy";

  /** Key for the configuration property that specifies the PrimSys REST service configuration */
  public static final String PRIMSYS_CONFIG_KEY =
      ExternalServerConfig.EXTERNAL_SERVER_CONFIG_TIGER_KEY + ".primsys-rest";

  private static final long REQUEST_TIMEOUT = 25000L;

  @Getter(lazy = true)
  private static final PrimsysClientProvider instance = new PrimsysClientProvider();

  @Getter(lazy = true)
  private final ExternalServerConfig config =
      Utils.loadConfig(ExternalServerConfig.class, PRIMSYS_CONFIG_KEY);

  @Getter(lazy = true)
  private final PrimsysClientMock primsysClientMock = new PrimsysClientMock();

  @Getter(lazy = true)
  private final String apikey = TigerGlobalConfiguration.readString(PRIMSYS_APIKEY_ENV_VAR);

  @Getter(lazy = true)
  private final int tigerProxyPort =
      TigerGlobalConfiguration.readIntegerOptional(TIGER_PROXY_PORT_KEY).orElseThrow();

  @Getter(lazy = true)
  private final HttpClient httpClient = initHttpClient();

  @Getter(lazy = true)
  private final PrimSysClientFactory clientFactory =
      PrimSysClientFactory.forRemote(buildBaseUrlString(getConfig()))
          .apiKey(getApikey())
          .build(getHttpClient());

  @Getter private DoctorClientMock doctorClientMock;

  /**
   * Provides a {@link PrimsysRestService} implementation based on configuration. Returns a mock
   * implementation if mocking is enabled, otherwise returns a real client.
   *
   * @return The appropriate {@link PrimsysRestService} implementation
   */
  public static PrimsysRestService getPrimsysRestClient() {
    return getInstance().getRestClient();
  }

  /**
   * Provides a {@link PrimsysDoctorService} implementation based on configuration. Returns a mock
   * implementation if mocking is enabled, otherwise returns a real client.
   *
   * @return The appropriate {@link PrimsysDoctorService} implementation
   * @param id
   */
  public static PrimsysDoctorService getPrimsysDoctorClientForId(final String id) {
    return getInstance().getDoctorClientForId(id);
  }

  private boolean isMockingEnabled() {
    return !TigerGlobalConfiguration.readBoolean(PRIMSYS_ACTIVE_KEY);
  }

  private void initDoctorClientMock() {
    doctorClientMock = new DoctorClientMock();
    getPrimsysClientMock().setDoctorClientMock(doctorClientMock);
  }

  private HttpClient initHttpClient() {
    final var proxy = new Proxy(Type.HTTP, new InetSocketAddress("localhost", getTigerProxyPort()));
    final var cioEngine =
        CIO.INSTANCE.create(
            cioEngineConfig -> {
              cioEngineConfig.setProxy(proxy);
              cioEngineConfig.setRequestTimeout(REQUEST_TIMEOUT);
              return Unit.INSTANCE;
            });
    return new HttpClient(cioEngine, new HttpClientConfig<>());
  }

  private PrimsysRestService getRestClient() {
    PrimsysRestService client;

    if (isMockingEnabled()) {
      log.info("##### USING MOCKED PRIMSYS REST CLIENT #####");
      client = getPrimsysClientMock();
    } else {
      client = new PrimsysClient(getClientFactory(), getConfig(), getTigerProxyPort(), getApikey());
    }
    ErpFdvProvider.getInstance().setPrimsysClient(client);
    return client;
  }

  private PrimsysDoctorService getDoctorClientForId(final String id) {
    if (isMockingEnabled()) {
      log.info("##### USING MOCKED PRIMSYS DOCTOR CLIENT #####");
      initDoctorClientMock();
      return doctorClientMock;
    }
    return new DoctorClient(getClientFactory().getDoctorClient(id));
  }

  private String buildBaseUrlString(final ExternalServerConfig serverConfig) {
    return UriComponentsBuilder.newInstance()
        .scheme(serverConfig.getScheme())
        .host(serverConfig.getHostname())
        .path(serverConfig.getBasePath())
        .build()
        .toUriString();
  }
}
