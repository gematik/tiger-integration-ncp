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

package de.gematik.test.ncp.screenplay.actions.testactor;

import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.data.record.PatientRecord;
import de.gematik.test.ncp.util.ClinicalDocumentInformationProvider;
import de.gematik.test.tiger.common.web.TigerBrowserUtil;
import de.gematik.test.tiger.lib.TigerDirector;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map.Entry;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import org.hl7.v3.ClinicalDocument;

/** Action to manually verify retrieved CDA1 prescription documents */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class VerifyRetrievedCda1Prescriptions implements Performable {

  private final PatientRecord patientRecord;

  @Step("{0} requests the user to manually verify the retrieved prescriptions")
  @Override
  public <T extends Actor> void performAs(final T actor) {
    var documents =
        Objects.requireNonNull(patientRecord).getPrescriptionsByDocumentId().entrySet().stream()
            .filter(entry -> PrescriptionCdaLevel.LEVEL_1.appliesTo(entry.getKey()))
            .map(Entry::getValue)
            .toList();

    for (ClinicalDocument document : documents) {
      String b64 =
          Objects.requireNonNull(
              ClinicalDocumentInformationProvider.getB64PdfPayload(document),
              "No Base64 PDF payload found in the clinical document");
      byte[] pdf = Base64.getDecoder().decode(b64.getBytes(StandardCharsets.UTF_8));

      try {
        Path path = Files.createTempFile(null, ".pdf");
        Files.write(path, pdf);
        TigerBrowserUtil.openUrlInBrowser(path.toString(), "CDA Level 1 Prescription Document");
        TigerDirector.pauseExecutionAndFailIfDesired(
            "The prescription document will open automatically. Please check the PDF content and decide if it satisfies the requirements.",
            "Manual verification of CDA Level 1 failed");
      } catch (final IOException e) {
        throw new UncheckedIOException("Error while writing PDF to temp file", e);
      }
    }
  }

  /** Factory method to create the action with the given patient record */
  public static VerifyRetrievedCda1Prescriptions withPatientRecord(
      final PatientRecord patientRecord) {
    return new VerifyRetrievedCda1Prescriptions(patientRecord);
  }
}
