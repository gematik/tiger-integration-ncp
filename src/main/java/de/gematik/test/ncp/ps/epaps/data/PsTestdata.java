/*
 * Copyright 2024-2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.ps.epaps.data;

import de.gematik.epa.conversion.internal.enumerated.CodeInterface;
import de.gematik.test.ncp.data.Practice;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.gen.epa.api.documents.dto.AuthorInstitution;
import de.gematik.test.ncp.gen.epa.api.documents.dto.Document;
import de.gematik.test.ncp.gen.epa.api.documents.dto.DocumentMetadata;
import de.gematik.test.ncp.gen.epa.api.documents.dto.PutDocumentsRequestDTO;
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
   * @param practice {@link Practice} german practice
   * @param epka {@link byte[]} epka to be put into the request
   * @return the created PutDocumentsRequestDTO
   */
  public static PutDocumentsRequestDTO createPutDocumentRequestForEPKA(
      final String kvnr, final Practice practice, final byte[] epka) {
    return new PutDocumentsRequestDTO()
        .kvnr(kvnr)
        .documentSets(
            List.of(
                new Document()
                    .documentData(epka)
                    .documentMetadata(createDocumentMetadata(practice))));
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

  public static DocumentMetadata createDocumentMetadata(final Practice practice) {
    final var smbInfo = practice.getSmbCard();
    final var practitioner = practice.getPractitioners().getFirst();
    final var meta =
        Testdata.loadClassFromJson(DocumentMetadata.class, DOCUMENT_METADATA_TEMPLATE_FILE_NAME);

    return meta.title("Patientenkurzakte_".concat(LocalDateTime.now().toString()))
        .creationTime(LocalDateTime.now().minusHours(3))
        .serviceStartTime(LocalDateTime.now().minusHours(4))
        .serviceStopTime(LocalDateTime.now().minusHours(3))
        .author(
            List.of(
                meta.getAuthor()
                    .getFirst()
                    .title(practitioner.titles())
                    .familyName(practitioner.lastNames())
                    .givenName(practitioner.givenNames())
                    .authorInstitution(
                        List.of(
                            new AuthorInstitution()
                                .name(smbInfo.name())
                                .identifier(smbInfo.telematikId())))));
  }
}
