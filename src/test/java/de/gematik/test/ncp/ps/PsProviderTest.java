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

package de.gematik.test.ncp.ps;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.ps.epaps.EpaPrimarySystemServiceImpl;
import de.gematik.test.ncp.ps.epaps.PrimarySystemServiceMockImpl;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PsProviderTest {

  private static Boolean origJarCfgValue;
  private static Boolean origExtCfgValue;

  @BeforeAll
  static void saveOriginalConfig() {
    origJarCfgValue = TigerGlobalConfiguration.readBoolean(PsProvider.PS_JAR_ACTIVE_KEY);
    origExtCfgValue = TigerGlobalConfiguration.readBoolean(PsProvider.PS_EXT_ACTIVE_KEY);
  }

  @AfterAll
  static void restoreOriginalConfig() {
    TigerGlobalConfiguration.putValue(PsProvider.PS_JAR_ACTIVE_KEY, origJarCfgValue);
    TigerGlobalConfiguration.putValue(PsProvider.PS_EXT_ACTIVE_KEY, origExtCfgValue);
  }

  @Test
  void standardImpl() {
    // Arrange
    TigerGlobalConfiguration.putValue(PsProvider.PS_JAR_ACTIVE_KEY, true);

    // Act & Assert
    assertEquals(
        EpaPrimarySystemServiceImpl.class,
        PsProvider.getPrimarySystemService().getClass(),
        "Default PsInterface implementation is of unexpected class type");
  }

  @Test
  void customImpl() {
    // Arrange
    final var psProvider = new PsProvider();
    psProvider.defaultPsImpl.set(PrimarySystemServiceMockImpl::instance);
    TigerGlobalConfiguration.putValue(PsProvider.PS_JAR_ACTIVE_KEY, false);
    TigerGlobalConfiguration.putValue(PsProvider.PS_EXT_ACTIVE_KEY, false);

    // Act & Assert
    assertEquals(
        PrimarySystemServiceMockImpl.class,
        psProvider.getPsImpl().getClass(),
        "PsInterface implementation is of unexpected class type");
  }
}
