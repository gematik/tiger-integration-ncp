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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

@Slf4j
public class MakeSignEpkaDefect implements Performable {

  private static final Pattern timestampPattern =
      Pattern.compile("<timestamp\\s+value=\"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z)\"/>");
  private static final DateTimeFormatter formatterWithoutMillis =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var signedEpka = patientData.getSignedEpka();
    final var currentTimestamp = ZonedDateTime.now().format(formatterWithoutMillis);
    final var defectiveSignedEpka =
        timestampPattern
            .matcher(new String(signedEpka))
            .replaceAll("<timestamp value=\"" + currentTimestamp + "\"/>");
    log.trace("Defective ePKA: {}", defectiveSignedEpka);
    patientData.setSignedEpka(defectiveSignedEpka.getBytes());
  }

  public static MakeSignEpkaDefect instance() {
    return Instrumented.instanceOf(MakeSignEpkaDefect.class).newInstance();
  }
}
