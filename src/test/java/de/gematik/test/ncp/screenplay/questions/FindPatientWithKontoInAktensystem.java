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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class FindPatientWithKontoInAktensystem implements Question<Patient> {

  private final Testdata testdata;
  private final String aktensystem;

  public FindPatientWithKontoInAktensystem(final Testdata testdata, final String aktensystem) {
    this.testdata = testdata;
    this.aktensystem = aktensystem;
  }

  @Override
  public Patient answeredBy(final Actor actor) {
    return Optional.ofNullable(this.testdata.recordSystems().get(aktensystem.toLowerCase()))
        .stream()
        .flatMap(Collection::stream)
        .map(kvnr -> this.testdata.patientsTestdata().findByKvnr(kvnr))
        .filter(Objects::nonNull)
        .findFirst()
        .orElseThrow(
            () ->
                new TigerConfigurationException(
                    "No patient found whose medical records are hosted by " + aktensystem));
  }

  public static FindPatientWithKontoInAktensystem fromTestdataAndAktensystem(
      final Testdata testData, final String aktensystem) {
    return new FindPatientWithKontoInAktensystem(testData, aktensystem);
  }
}
