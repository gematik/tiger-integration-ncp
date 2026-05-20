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

import de.gematik.test.ncp.erezept.ErpFdvProvider;
import de.gematik.test.ncp.erezept.ErpFdvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.HasTeardown;
import net.serenitybdd.screenplay.RefersToActor;

/**
 * Ability to use an E-prescription personal client (eRp FdV). This client enables an insurant to
 * interact with the E-prescription service and manage their available prescriptions.
 */
@Slf4j
@RequiredArgsConstructor
public class UseErpFdv implements Ability, HasTeardown, RefersToActor {

  private final ErpFdvService client;

  private Actor actor;
  private boolean isInitialized = false;

  public ErpFdvService getClient() {
    if (!isInitialized) {
      initClient();
    }
    return client;
  }

  private void initClient() {
    final var data = actor.usingAbilityTo(ProvidePatientData.class);
    log.debug("Initializing Erp FdV for KVNR {}", data.kvnr());
    client.start();
    client
        .login(data.kvnr())
        .orElseThrow(
            () -> new IllegalStateException("Could not login to Erp FdV with KVNR " + data.kvnr()));
    isInitialized = true;
  }

  /**
   * Factory method for creating the Ability to use the default client.
   *
   * @return Ability to use the default E-prescription personal client
   */
  public static UseErpFdv withDefaultConfiguration() {
    return new UseErpFdv(ErpFdvProvider.getDefaultErpFdvClient());
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Ability> T asActor(final Actor actor) {
    this.actor = actor;
    return (T) this;
  }

  @Override
  public void tearDown() {
    log.debug("Stopping Erp FdV");
    client.stop();
  }
}
