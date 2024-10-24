/*
 * Copyright (c) 2024. gematik GmbH
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

package de.gematik.unittest.ncp.ps;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.ps.PsProvider;
import de.gematik.test.ncp.ps.epaps.EpaPrimarySystemServiceImpl;
import de.gematik.test.ncp.ps.epaps.PrimarySystemServiceMockImpl;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import org.junit.jupiter.api.Test;

class PsProviderTest {

  @Test
  void standardImpl() {
    final var origCfgValue = TigerGlobalConfiguration.readBoolean(PsProvider.PS_JAR_ACTIVE_KEY);
    try {
      TigerGlobalConfiguration.putValue(PsProvider.PS_JAR_ACTIVE_KEY, true);
      assertEquals(
          EpaPrimarySystemServiceImpl.class,
          PsProvider.getPrimarySystemService().getClass(),
          "Default PsInterface implementation is of unexpected class type");
    } finally {
      TigerGlobalConfiguration.putValue(PsProvider.PS_JAR_ACTIVE_KEY, origCfgValue);
    }
  }

  @Test
  void customImpl() {
    final var psProvider = new PsProviderForTest();
    psProvider.defaultPsImpl.set(PrimarySystemServiceMockImpl::instance);
    final var origJarCfgValue = TigerGlobalConfiguration.readBoolean(PsProvider.PS_JAR_ACTIVE_KEY);
    final var origExtCfgValue = TigerGlobalConfiguration.readBoolean(PsProvider.PS_EXT_ACTIVE_KEY);
    try {
      TigerGlobalConfiguration.putValue(PsProvider.PS_JAR_ACTIVE_KEY, false);
      TigerGlobalConfiguration.putValue(PsProvider.PS_EXT_ACTIVE_KEY, false);
      assertEquals(
          PrimarySystemServiceMockImpl.class,
          psProvider.getPsImpl().getClass(),
          "PsInterface implementation is of unexpected class type");
    } finally {
      TigerGlobalConfiguration.putValue(PsProvider.PS_JAR_ACTIVE_KEY, origJarCfgValue);
      TigerGlobalConfiguration.putValue(PsProvider.PS_EXT_ACTIVE_KEY, origExtCfgValue);
    }
  }

  private static class PsProviderForTest extends PsProvider {}
}
