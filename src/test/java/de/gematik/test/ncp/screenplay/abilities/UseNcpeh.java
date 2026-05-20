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

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.ncpeh.NcpehProvider;
import de.gematik.test.ncp.ncpeh.NcpehService;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.Delegate;
import net.serenitybdd.screenplay.Ability;

/** Ability to use the NCPeH (Simulation) via an implementation of {@link NcpehService} */
public class UseNcpeh implements Ability {

  @Delegate // lombok.experimental kann aber auch von Hand implementiert werden
  private final NcpehService service;

  @Getter private final Map<UCHeaders, String> ncpehMockControlRequestHeaders;

  /**
   * Default constructor without control headers for the NCPeH-Simulation-Mock
   *
   * @see <a
   *     href="https://github.com/gematik/NCPeH-Simulation-API/blob/main/ncpeh-simulation-mock/README.md">ncpeh-simulation-mock
   *     README.md on GitHub</a>
   */
  public UseNcpeh() {
    this(null);
  }

  /**
   * Creates a UseNcpeh Ability with optional control headers for the NCPeH-Simulation-Mock to
   * influence response behavior. If not set, the default behavior of the mock is used.
   *
   * @param ncpehMockControlRequestHeaders optional control headers for the NCPeH-Simulation-Mock
   * @see <a
   *     href="https://github.com/gematik/NCPeH-Simulation-API/blob/main/ncpeh-simulation-mock/README.md">ncpeh-simulation-mock
   *     README.md on GitHub</a>
   */
  public UseNcpeh(final Map<UCHeaders, String> ncpehMockControlRequestHeaders) {
    this(NcpehProvider.getNcpehService(), ncpehMockControlRequestHeaders);
  }

  /**
   * Creates a UseNcpeh ability with a specific NcpehService implementation and optional control
   * headers for the NCPeH-Simulation-Mock to influence response behavior. If not set, the default
   * behavior of the mock is used.
   *
   * @param service the NcpehService instance to use
   * @param ncpehMockControlRequestHeaders optional control headers for the NCPeH-Simulation-Mock
   * @see <a
   *     href="https://github.com/gematik/NCPeH-Simulation-API/blob/main/ncpeh-simulation-mock/README.md">ncpeh-simulation-mock
   *     README.md on GitHub</a>
   */
  public UseNcpeh(
      final NcpehService service, final Map<UCHeaders, String> ncpehMockControlRequestHeaders) {
    this.service = service;
    this.ncpehMockControlRequestHeaders = ncpehMockControlRequestHeaders;
  }
}
