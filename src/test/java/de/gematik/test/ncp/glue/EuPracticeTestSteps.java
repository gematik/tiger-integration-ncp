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
import static de.gematik.test.ncp.screenplay.Practice.*;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;

/** Class for implementing glue code, which deals with NCPeH operations */
public class EuPracticeTestSteps {

  @Angenommen("Der NCPeH Simulator läuft")
  @Wenn("Der NCPeH läuft")
  public void isNcpehSimAvailabe() {
    euPractice().practitioner().acknowledgeThat(ncpehIsAvailable());
  }

  @Dann("^Identifiziere den Patienten (.)$")
  @Wenn("^Der Patient (.+) identifiziert werden konnte$")
  public void identifyPatient(String name) {
    euPractice()
        .practitioner()
        .treat(euPractice().callWaitingPatientWithName(name))
        .attemptsTo(retrievePatientIdentificationData())
        .acknowledgeThat(patientDataAreCorrect());
  }

  @Wenn("Die Patient Summary des Patienten gefunden werden konnte")
  public void findPatientSummaryToPatient() {
    euPractice().practitioner().wasAbleTo(findPatientSummaryData());
  }

  @Dann("Lade die Patient Summary des Patienten herunter")
  public void downloadPatientSummaryToPatient() {
    euPractice().practitioner().wasAbleTo(retrieveAllPatientSummaryData());
  }
}
