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

package de.gematik.test.ncp.screenplay.questions;

import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@Slf4j
public class PdfDocumentBelongsToPatient implements Question<Boolean> {

  @Override
  public Boolean answeredBy(final Actor actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var cda1Document = patientData.getPatientSummaryLvl1();

    try (final PDDocument document = Loader.loadPDF(cda1Document)) {
      // Instantiate PDFTextStripper
      final PDFTextStripper pdfStripper = new PDFTextStripper();

      // Extract text from the document
      final String text = pdfStripper.getText(document);

      log.debug("Patient {}", patientData);
      log.debug("Extracted text from PDF: {}", text);

      return text.contains(patientData.name().givenNames())
          && text.contains(patientData.name().lastNames())
          // Date can be printed in different formats
          && text.contains(String.valueOf(patientData.birthDate().getDayOfMonth()))
          && text.contains(String.valueOf(patientData.birthDate().getMonthValue()))
          && text.contains(String.valueOf(patientData.birthDate().getYear()));

    } catch (final IOException e) {
      log.error("Error while reading PDF", e);
    }

    return Boolean.FALSE;
  }
}
