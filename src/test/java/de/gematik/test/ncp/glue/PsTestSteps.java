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

import static de.gematik.test.ncp.screenplay.GermanPracticeActions.*;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.screenplay.GermanPracticeActions;
import de.gematik.test.ncp.screenplay.Practice;
import de.gematik.test.tiger.glue.RBelValidatorGlue;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;

@Deprecated
public class PsTestSteps {

  public static final String psEndpointBasePath = "/services/rs/";
  public static final String psEndpointMatchAllPaths = psEndpointBasePath.concat(".*");

  private static final RBelValidatorGlue rbelValidator = new RBelValidatorGlue();

  @Dann("Prüfe, dass die Antwort vom Primärsystem positiv war")
  public void checkTheAnswerFromPrimaersystemWasPositive() {
    rbelValidator.findLastRequestToPath(psEndpointMatchAllPaths);
    rbelValidator.printCurrentResponse();
    rbelValidator.currentResponseMessageAttributeMatches("$.responseCode", "200");
    rbelValidator.currentResponseMessageAttributeMatches("$.body.success", Boolean.TRUE.toString());
  }

  @Angenommen("Das Primärsystem läuft")
  public void primaersystemAvailable() {
    GermanPracticeActions.lei().acknowledgeThat(GermanPracticeActions.psIsAvailable());
  }

  @Angenommen("^Der Leistungserbringer ist für das Aktenkonto mit der KVNR (\\w{10}) berechtigt$")
  @Wenn("^Der Leistungserbringer für das Aktenkonto mit der KVNR (\\w{10}) berechtigt ist$")
  public void leistungserbringerIsAuthorizedForPatientEpa(String kvnr) {
    Patient patient =
        Testdata.instance().patientsTestdata().stream()
            .filter(pat -> kvnr.equals(pat.kvnr()))
            .findFirst()
            .orElseThrow();
    Practice.germanPractice()
        .practitioner()
        .treat(Practice.germanPractice().callWaitingPatientWithName(patient.name()))
        .asksFor(isLeistungserbringerAuthorizedForAktenkonto())
        .ifNotTry(
            () ->
                GermanPracticeActions.lei()
                    .attemptsTo(authorizeLeistungserbringerForAktenkonto())
                    .acknowledgeThat(isLeistungserbringerAuthorizedForAktenkonto()));
  }

  @Wenn("^Eine Dokument in das Aktenkonto mit der KVNR (\\w{10}) eingestellt wurde$")
  @Dann("^Kann ein Dokument in das Aktenkonto mit der KVNR (\\w{10}) eingestellt werden$")
  public void storeDocumentforKvnr(String kvnr) {
    GermanPracticeActions.lei()
        .attemptsTo(storeEpkaInAktenkonto())
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());
  }
}
