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

import static de.gematik.test.ncp.erezept.ErpFdvProvider.ERP_FDV_ACTIVE_KEY;
import static de.gematik.test.ncp.erezept.ErpFdvProvider.ERP_FDV_APIKEY_ENV_VAR;
import static de.gematik.test.ncp.erezept.ErpFdvProvider.ERP_FDV_CONFIG_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.erezept.impl.ErpFdvClient;
import de.gematik.test.ncp.erezept.impl.ErpFdvMockImpl;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ErpFdvProviderTest {

  @AfterEach
  void tearDown() {
    TigerGlobalConfiguration.reset();
    ErpFdvProvider.reset();
  }

  @Test
  void whenErpFdvIsActive_getDefaultErpFdvClientShouldReturnClient() {
    // Arrange
    final var apiKey = "123";
    TigerGlobalConfiguration.putValue(ERP_FDV_APIKEY_ENV_VAR, apiKey);
    TigerGlobalConfiguration.putValue(ERP_FDV_ACTIVE_KEY, true);
    // Act
    final var erpClient = ErpFdvProvider.getDefaultErpFdvClient();
    // Assert
    assertInstanceOf(ErpFdvClient.class, erpClient);
  }

  @Test
  void whenCallingGetDefaultErpFdvClient_shouldReturnSingleton() {
    // Arrange
    final var apiKey = "123";
    TigerGlobalConfiguration.putValue(ERP_FDV_APIKEY_ENV_VAR, apiKey);
    TigerGlobalConfiguration.putValue(ERP_FDV_ACTIVE_KEY, true);
    // Act
    final var instance = ErpFdvProvider.getDefaultErpFdvClient();
    // Assert
    assertNotNull(instance);
    assertSame(instance, ErpFdvProvider.getDefaultErpFdvClient());
  }

  @Test
  void whenErpFdvIsInactive_getDefaultErpFdvClientShouldReturnMock() {
    TigerGlobalConfiguration.putValue(ERP_FDV_ACTIVE_KEY, false);
    final var erpClient = ErpFdvProvider.getDefaultErpFdvClient();
    assertInstanceOf(ErpFdvMockImpl.class, erpClient);
  }

  @Test
  void whenCallingGetInstance_shouldReturnSingleton() {
    final var instance = ErpFdvProvider.getInstance();
    assertNotNull(instance);
    assertInstanceOf(ErpFdvProvider.class, instance);
    assertSame(instance, ErpFdvProvider.getInstance());
  }

  @Test
  void shouldLoadExternalServerConfigViaUtils() {
    // Arrange
    final var config = new ExternalServerConfig();
    config.setHostname("mockhost");
    try (final MockedStatic<Utils> ignored = Mockito.mockStatic(Utils.class)) {
      when(Utils.loadConfig(ExternalServerConfig.class, ERP_FDV_CONFIG_KEY)).thenReturn(config);
      // Act & Assert
      final var erpFdvConfig = ErpFdvProvider.getInstance().getErpFdvConfig();
      assertNotNull(erpFdvConfig);
      assertEquals("mockhost", erpFdvConfig.getHostname());
    }
  }
}
