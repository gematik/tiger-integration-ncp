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

import static de.gematik.test.ncp.erp.primsys.PrimsysClientProvider.PRIMSYS_ACTIVE_KEY;
import static de.gematik.test.ncp.erp.primsys.PrimsysClientProvider.PRIMSYS_APIKEY_ENV_VAR;
import static de.gematik.test.ncp.erp.primsys.PrimsysClientProvider.PRIMSYS_CONFIG_KEY;
import static de.gematik.test.ncp.erp.primsys.PrimsysClientProvider.TIGER_PROXY_PORT_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import de.gematik.test.erezept.primsys.PrimSysClientFactory;
import de.gematik.test.erezept.primsys.PrimSysClientFactory.ClientBuilder;
import de.gematik.test.erezept.primsys.data.actors.DoctorDto;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.erp.primsys.impl.DoctorClient;
import de.gematik.test.ncp.erp.primsys.impl.DoctorClientMock;
import de.gematik.test.ncp.erp.primsys.impl.PrimsysClient;
import de.gematik.test.ncp.erp.primsys.impl.PrimsysClientMock;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import io.ktor.client.HttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PrimsysClientProviderTest {

  AutoCloseable openMocks;

  @Mock ClientBuilder clientBuilder;
  @Mock PrimSysClientFactory primSysClientFactory;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    when(clientBuilder.apiKey(anyString())).thenReturn(clientBuilder);
    when(clientBuilder.build(any(HttpClient.class))).thenReturn(primSysClientFactory);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
    TigerGlobalConfiguration.reset();
  }

  @Test
  void shouldReturnMockRestClientWhenPrimsysInactive() {
    // Arrange
    TigerGlobalConfiguration.putValue(PRIMSYS_ACTIVE_KEY, false);

    // Act
    final var client = PrimsysClientProvider.getPrimsysRestClient();

    // Assert
    assertInstanceOf(
        PrimsysClientMock.class, client, "Client is not a mock despite primsys being inactive");
  }

  @Test
  void shouldReturnMockDoctorClientWhenPrimsysInactive() {
    // Arrange
    TigerGlobalConfiguration.putValue(PRIMSYS_ACTIVE_KEY, false);

    // Act
    final DoctorDto doctorDto = new DoctorDto();
    doctorDto.setId("123");
    final var client = PrimsysClientProvider.getPrimsysDoctorClientForId(doctorDto.getId());

    // Assert
    assertInstanceOf(
        DoctorClientMock.class, client, "Client is not a mock despite primsys being inactive");
  }

  @Test
  void shouldReturnRealRestClientWhenPrimsysActive() {
    // Arrange
    final var apiKey = "123";
    TigerGlobalConfiguration.putValue(PRIMSYS_ACTIVE_KEY, true);
    TigerGlobalConfiguration.putValue(PRIMSYS_APIKEY_ENV_VAR, apiKey);

    try (final MockedStatic<PrimSysClientFactory> primSysClientFactoryMock =
        Mockito.mockStatic(PrimSysClientFactory.class)) {

      when(PrimSysClientFactory.forRemote(any())).thenReturn(clientBuilder);

      // Act
      final var client = PrimsysClientProvider.getPrimsysRestClient();

      // Assert
      assertInstanceOf(
          PrimsysClient.class, client, "Client is a mock despite primsys being active");
    }
  }

  @Test
  void shouldReturnRealDoctorClientWhenPrimsysActive() {
    // Arrange
    final var apiKey = "123";
    TigerGlobalConfiguration.putValue(PRIMSYS_ACTIVE_KEY, true);
    TigerGlobalConfiguration.putValue(PRIMSYS_APIKEY_ENV_VAR, apiKey);

    try (final MockedStatic<PrimSysClientFactory> primSysClientFactoryMock =
            Mockito.mockStatic(PrimSysClientFactory.class);
        final MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class)) {

      when(PrimSysClientFactory.forRemote(any())).thenReturn(clientBuilder);

      // Act
      final DoctorDto doctorDto = new DoctorDto();
      doctorDto.setId("123");
      final var client = PrimsysClientProvider.getPrimsysDoctorClientForId(doctorDto.getId());

      // Assert
      assertInstanceOf(DoctorClient.class, client, "Client is a mock despite primsys being active");
    }
  }

  @Test
  void shouldApplyCorrectConfiguration() {
    // Arrange
    final var apiKey = "123";
    final var proxyPort = 4711;
    final var config = new ExternalServerConfig();
    config.setScheme("http");
    config.setHostname("foo");
    config.setBasePath("bar");
    TigerGlobalConfiguration.putValue(PRIMSYS_ACTIVE_KEY, true);
    TigerGlobalConfiguration.putValue(PRIMSYS_APIKEY_ENV_VAR, apiKey);
    TigerGlobalConfiguration.putValue(TIGER_PROXY_PORT_KEY, proxyPort);

    try (final MockedStatic<PrimSysClientFactory> primSysClientFactoryMock =
            Mockito.mockStatic(PrimSysClientFactory.class);
        final MockedStatic<Utils> utilsMock = Mockito.mockStatic(Utils.class)) {

      when(PrimSysClientFactory.forRemote(any())).thenReturn(clientBuilder);
      when(Utils.loadConfig(ExternalServerConfig.class, PRIMSYS_CONFIG_KEY)).thenReturn(config);

      // Act
      final var client = PrimsysClientProvider.getPrimsysRestClient();

      // Assert
      assertInstanceOf(
          PrimsysClient.class, client, "Client is a mock despite primsys being active");
      assertSame(config, ((PrimsysClient) client).getConfig(), "Config is not set correctly");
      assertEquals(
          proxyPort,
          PrimsysClientProvider.getInstance().getTigerProxyPort(),
          "Proxy port is not set correctly");
    }
  }
}
