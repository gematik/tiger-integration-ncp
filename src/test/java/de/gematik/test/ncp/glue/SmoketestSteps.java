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

package de.gematik.test.ncp.glue;

import static de.gematik.test.ncp.screenplay.EuPracticeActions.*;
import static de.gematik.test.ncp.screenplay.GermanPracticeActions.*;

import de.gematik.test.ncp.fdv.FdvInterface;
import de.gematik.test.ncp.fdv.FdvProvider;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class SmoketestSteps {

  @Getter(lazy = true)
  private final FdvInterface fdv = FdvProvider.getDefaultFdvClient();

  @Wenn("Das Frontend des Versicherten läuft")
  public void fdvIsUpAndRunning() {
    if (!fdv().isUpAndRunning()) {
      assert false;
    }
  }

  /*  @Wenn("Das Primärsystem läuft")
  public void psIsUpAndRunning() {
    Practice.germanPractice().practitioner().acknowledgeThat(psIsAvailable());
  }*/

  @Dann("War der Smoketest erfolgreich")
  public void smoketestPassed() {}
}
