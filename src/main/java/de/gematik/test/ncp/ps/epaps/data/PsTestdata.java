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

package de.gematik.test.ncp.ps.epaps.data;

import de.gematik.epa.conversion.internal.enumerated.CodeInterface;
import de.gematik.epa.dto.request.PutDocumentsRequestDTO;
import de.gematik.epa.ihe.model.document.Document;
import de.gematik.epa.ihe.model.simple.AuthorInstitution;
import de.gematik.test.ncp.data.Testdata;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/** Utility class to hold static methods for the generation of test data. */
@UtilityClass
public final class PsTestdata {

  private static final String DOCUMENT_METADATA_TEMPLATE_FILE_NAME = "documentMetadata.json";

  /**
   * Create a PutDocumentsRequestDTO with a document containing the given epka
   *
   * @param kvnr {@link String} kvnr of the patient
   * @param epka {@link byte[]} epka to be put into the request
   * @return the created PutDocumentsRequestDTO
   */
  public static PutDocumentsRequestDTO createPutDocumentRequestForEPKA(
      final String kvnr, final byte[] epka) {
    return new PutDocumentsRequestDTO(
        kvnr, List.of(new Document(epka, createDocumentMetadata().toPsDocumentMetadata(), null)));
  }

  /**
   * Create the formatted String to the given code, as it is required in the Slot of an AdhocQuery
   * request
   *
   * @param code {@link CodeInterface} code for which to create the formatted String
   * @return the formatted String
   */
  public static String createFindQueryValueString(@NonNull final CodeInterface code) {
    return "'" + code.getValue() + "^^" + code.getCodingScheme() + "'";
  }

  /**
   * Create the formatted String, representing a list of alternatives, as it is required in the Slot
   * of an AdhocQuery request
   *
   * @param codes {@link CodeInterface} array - codes for which to create the formatted String
   * @return the formatted String
   */
  public static String createFindQueryValueList(final CodeInterface... codes) {
    return "("
        + Optional.ofNullable(codes).stream()
            .flatMap(Arrays::stream)
            .map(PsTestdata::createFindQueryValueString)
            .reduce((result, code) -> result + "," + code)
            .orElse("")
        + ")";
  }

  public static DocumentMetadata createDocumentMetadata() {
    final var practice = Testdata.instance().practice();
    final var practitioner = practice.practitioners().getFirst();
    final var meta =
        Testdata.loadClassFromJson(DocumentMetadata.class, DOCUMENT_METADATA_TEMPLATE_FILE_NAME);
    return meta.withTitle("Patientenkurzakte_".concat(LocalDateTime.now().toString()))
        .withCreationTime(LocalDateTime.now().minusHours(3))
        .withServiceStartTime(LocalDateTime.now().minusHours(4))
        .withServiceStopTime(LocalDateTime.now().minusHours(3))
        .withAuthors(
            List.of(
                meta.getAuthors()
                    .getFirst()
                    .withTitle(practitioner.titles())
                    .withFamilyName(practitioner.lastNames())
                    .withGivenName(practitioner.givenNames())
                    .withAuthorInstitutions(
                        List.of(new AuthorInstitution(practice.name(), practice.telematikId())))));
  }
}
