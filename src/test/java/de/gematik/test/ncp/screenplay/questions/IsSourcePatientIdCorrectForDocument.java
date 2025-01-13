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

import static de.gematik.test.ncp.ncpeh.data.RequestBaseBuilder.KVNR_ASSIGNING_AUTHORITY;

import de.gematik.epa.conversion.internal.requests.factories.slot.SlotFactory;
import de.gematik.epa.conversion.internal.requests.factories.slot.SlotName;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.util.IheUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

@Slf4j
public class IsSourcePatientIdCorrectForDocument implements Question<Boolean> {

  private final Integer level;

  public IsSourcePatientIdCorrectForDocument(final Integer level) {
    this.level = level;
  }

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var psaMetadata = patientData.getPsaMetadata();

    final var extrinsicObject = IheUtils.retrieveExtrinsicObjectToCdaLevel(psaMetadata, level);

    final var sourcePatientId =
        SlotFactory.getValueFromSlot(extrinsicObject.getSlot(), SlotName.SOURCE_PATIENT_ID);

    final var patientIdParts = sourcePatientId.split("[|^]");

    final List<String> failureMessages = new ArrayList<>();

    if (patientIdParts.length < 3) {
      failureMessages.add(
          "SourcePatientId does not have the expected format! Received SourcePatientId: "
              + sourcePatientId);
    }

    if (!patientData.kvnr().equals(patientIdParts[0])) {
      failureMessages.add(
          String.format(
              "KVNR does not have the expected value! Expected: %s Received: %s",
              patientData.kvnr(), patientIdParts[0]));
    }

    if (!patientData.accessCode().equals(patientIdParts[1])) {
      failureMessages.add(
          String.format(
              "Access code does not have the expected value! Expected: %s Received: %s",
              patientData.accessCode(), patientIdParts[1]));
    }

    final var isoIndex = patientIdParts[patientIdParts.length - 1].indexOf("&ISO");

    if (isoIndex > 1) {
      final var root = patientIdParts[patientIdParts.length - 1].substring(1, isoIndex);

      if (!KVNR_ASSIGNING_AUTHORITY.equals(root)) {
        failureMessages.add(
            String.format(
                "Root does not have the expected value! Expected: %s Received: %s",
                KVNR_ASSIGNING_AUTHORITY, root));
      }
    } else {
      failureMessages.add(
          String.format(
              "Unexpected format of the root! Expected: & %s &ISO Received: %s",
              KVNR_ASSIGNING_AUTHORITY, patientIdParts[patientIdParts.length - 1]));
    }

    if (!failureMessages.isEmpty()) {
      log.info("Failure messages: {}", String.join(", ", failureMessages));
    }

    return failureMessages.isEmpty();
  }

  public static IsSourcePatientIdCorrectForDocument ofLevel(final Integer level) {
    return new IsSourcePatientIdCorrectForDocument(level);
  }
}
