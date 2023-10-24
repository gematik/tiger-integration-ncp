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

package de.gematik.test.ncp.ps.epaps.data;

import de.gematik.epa.conversion.internal.enumerated.ClassCode;
import de.gematik.epa.conversion.internal.enumerated.CodeInterface;
import de.gematik.epa.conversion.internal.enumerated.ConfidentialityCode;
import de.gematik.epa.conversion.internal.enumerated.EventCode;
import de.gematik.epa.conversion.internal.enumerated.FormatCode;
import de.gematik.epa.conversion.internal.enumerated.HealthcareFacilityCode;
import de.gematik.epa.conversion.internal.enumerated.PracticeSettingCode;
import de.gematik.epa.conversion.internal.enumerated.TypeCode;
import de.gematik.epa.dto.request.PutDocumentsRequestDTO;
import de.gematik.epa.ihe.model.simple.AuthorInstitution;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/** Utility class to hold static methods for the generation of test data. */
@UtilityClass
public final class PsTestdata {

  public static PutDocumentsRequestDTO createPutDocumentRequestForEPKA(String kvnr, byte[] epka) {
    return createPutDocumentRequestForEPKA(kvnr, epka, createDocumentMetadata());
  }

  public static PutDocumentsRequestDTO createPutDocumentRequestForEPKA(
      @NonNull String kvnr,
      byte[] epka,
      de.gematik.test.ncp.ps.epaps.data.DocumentMetadata documentMetadata) {
    return new PutDocumentsRequestDTO(
        kvnr,
        List.of(
            new de.gematik.epa.ihe.model.document.Document(
                epka, documentMetadata.toPsDocumentMetadata(), null)));
  }

  /**
   * Create the formatted String to the given code, as it is required in the Slot of an AdhocQuery
   * request
   *
   * @param code {@link CodeInterface} code for which to create the formatted String
   * @return the formatted String
   */
  public static String createFindQueryValueString(@NonNull CodeInterface code) {
    return "'" + code.getValue() + "^^" + code.getCodingScheme() + "'";
  }

  /**
   * Create the formatted String, representing a list of alternatives, as it is required in the Slot
   * of an AdhocQuery request
   *
   * @param codes {@link CodeInterface} array - codes for which to create the formatted String
   * @return the formatted String
   */
  public static String createFindQueryValueList(CodeInterface... codes) {
    return "("
        + Optional.ofNullable(codes).stream()
            .flatMap(Arrays::stream)
            .map(PsTestdata::createFindQueryValueString)
            .reduce((result, code) -> result + "," + code)
            .orElse("")
        + ")";
  }

  public static de.gematik.test.ncp.ps.epaps.data.DocumentMetadata createDocumentMetadata() {
    return de.gematik.test.ncp.ps.epaps.data.DocumentMetadata.builder()
        .title("Patientenkurzakte_".concat(LocalDateTime.now().toString()))
        .classCode(ClassCode.MEDIZINISCHER_AUSWEIS.getValue())
        .confidentialityCode(ConfidentialityCode.NORMAL.getValue())
        .creationTime(LocalDateTime.now().minusHours(3))
        .eventCode(EventCode.PATIENTEN_MITGEBRACHT.getValue())
        // TODO: As soon as the Aktensystem knows it, use the correct format code here
        // .formatCode(FormatCode.PATIENTEN_KURZ_AKTE.getValue())
        .formatCode(FormatCode.NOTFALLDATENSATZ.getValue())
        .healthcareFacilityTypeCode(HealthcareFacilityCode.ARZTPRAXIS.getValue())
        .languageCode("de-DE")
        .mimeType(MediaType.APPLICATION_XML)
        .practiceSettingCode(PracticeSettingCode.ALLGEMEINMEDIZIN.getValue())
        .serviceStartTime(LocalDateTime.now().minusHours(4))
        .serviceStopTime(LocalDateTime.now().minusHours(3))
        .typeCode(TypeCode.ERGEBNISSE_DIAGNOSTIK.getValue())
        .uri("patientenkurzakte.xml")
        .author(
            Author.builder()
                .identifier("165746304")
                .familyName("Urlaub")
                .givenName("Warin")
                .title("Prof")
                .authorRole("4^^^&1.3.6.1.4.1.19376.3.276.1.5.13&ISO")
                .authorSpecialty("3^^^&1.2.276.0.76.5.492&ISO")
                .authorInstitution(
                    new AuthorInstitution(
                        "Arztpraxis Prof. Warin Urlaub", "1-SMC-B-Testkarte-883110000092399"))
                .build())
        .build();
  }
}
