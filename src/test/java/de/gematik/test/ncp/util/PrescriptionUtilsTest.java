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

package de.gematik.test.ncp.util;

import static de.gematik.ncpeh.ehdsi.valuesets.EhdsiSubstitutionCode.GENERIC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.gematik.ncpeh.ehdsi.valuesets.EhdsiSubstitutionCode;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.SneakyThrows;
import org.hl7.v3.ClinicalDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PrescriptionUtilsTest {

  private static final String MEDICATION_NAME = "Metoprololsuccinat AL 47,5mg Retardtabletten";
  private static final String PRESCRIPTION_UID = "1.2.276.0.76.4.299^160.000.000.000.123.76|eP.XML";
  private static final String PZN = "07097020";

  private static final String PRESCRIPTION_CDA3_FILE_NAME = "Prescription_CDA3.xml";

  private static final ClinicalDocument clinicalDocument = readPrescriptionFromFile();

  @SneakyThrows
  private static ClinicalDocument readPrescriptionFromFile() {
    return Utils.unmarshalXml(
        ClinicalDocument.class,
        Objects.requireNonNull(
            PrescriptionUtilsTest.class.getResourceAsStream(PRESCRIPTION_CDA3_FILE_NAME)));
  }

  @Test
  void getSubstitutionValueFromCda3_shouldReturnSubstitutionValue() {
    var substitutionValue = PrescriptionUtils.getSubstitutionValueFromCda3(clinicalDocument);
    assertEquals(GENERIC.getCode(), substitutionValue.getCode());
    assertEquals(GENERIC.getDisplayName(), substitutionValue.getDisplayName());
    assertEquals(EhdsiSubstitutionCode.CODE_SYSTEM_OID, substitutionValue.getCodeSystem());
    assertEquals(EhdsiSubstitutionCode.CODE_SYSTEM_NAME, substitutionValue.getCodeSystemName());
  }

  @Test
  void getDocumentUidFromCda3_shouldReturnDocumentUid() {
    assertEquals(PRESCRIPTION_UID, PrescriptionUtils.getDocumentUidFromCda3(clinicalDocument));
  }

  @ParameterizedTest
  @CsvSource({
    "1.2.276.0.76.4.299^160.000.000.000.123.76|eP.PDF, 1.2.276.0.76.4.299^160.000.000.000.123.76",
    "1.2.276.0.76.4.299^160.000.000.000.123.76|eP.XML, 1.2.276.0.76.4.299^160.000.000.000.123.76",
    "160.000.000.000.123.76^PS.PDF, 160.000.000.000.123.76",
    "160.000.000.000.123.76^PS.XML, 160.000.000.000.123.76"
  })
  void stripLevelMarkerFromDocumentUid_shouldStripMarkerCorrectly(
      final String documentUid, final String expectedResult) {
    // Act
    var stripped = IheUtils.stripLevelMarkerFromDocumentUid(documentUid);

    // Assert
    assertEquals(expectedResult, stripped);
  }

  @Test
  void stripLevelMarkerFromDocumentUid_shouldThrowWhenNoMarkerPresent() {
    // Arrange
    var docUidNoMarker = "1.2.276.0.76.4.299^160.000.000.000.123.76";

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> IheUtils.stripLevelMarkerFromDocumentUid(docUidNoMarker));
  }

  @Test
  void stripLevelMarkerFromDocumentUid_shouldThrowOnInvalidMarkerFormat() {
    // Arrange
    var docUidInvalidMarker = "1.2.276.0.76.4.299^160.000.000.000.123.76|INVALID";

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> IheUtils.stripLevelMarkerFromDocumentUid(docUidInvalidMarker));
  }

  @ParameterizedTest
  @CsvSource({
    "1.2.276.0.76.4.299^160.000.000.000.123.76|eP.PDF, 160.000.000.000.123.76",
    "1.2.276.0.76.4.299^160.000.000.000.123.76|eP.XML, 160.000.000.000.123.76"
  })
  void extractPrescriptionIdFromDocumentUid_shouldExtractPrescriptionIdCorrectly(
      final String documentUid, final String expectedId) {
    assertEquals(expectedId, PrescriptionUtils.extractPrescriptionIdFromDocumentUid(documentUid));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"1.2.276.0.76.4.299^160.000.000.000.123.76", "1.2.276.0.76.4.299^|eP.XML"})
  void extractPrescriptionIdFromDocumentUid_shouldThrowOnInvalidUidFormat(final String invalidUid) {
    assertThrows(
        IllegalArgumentException.class,
        () -> PrescriptionUtils.extractPrescriptionIdFromDocumentUid(invalidUid));
  }

  @Test
  void getPznFromCda3_shouldReturnPzn() {
    assertEquals(PZN, PrescriptionUtils.getPznFromCda3(clinicalDocument));
  }

  @Test
  void getPznDisplayNameFromCda3_shouldReturnPznDisplayName() {
    assertEquals(MEDICATION_NAME, PrescriptionUtils.getPznDisplayNameFromCda3(clinicalDocument));
  }

  @Test
  void getMedicationNameFromCda3_shouldReturnMedicationName() {
    assertEquals(MEDICATION_NAME, PrescriptionUtils.getMedicationNameFromCda3(clinicalDocument));
  }

  @Test
  void getAtcCode_shouldThrowIfNoAtcCodeFound() {
    assertThrows(NoSuchElementException.class, () -> PrescriptionUtils.getAtcCode(null));
  }

  @Test
  void getAtcCode_shouldReturnAtcCode() {
    assertEquals("C07AB02", PrescriptionUtils.getAtcCode(clinicalDocument));
  }
}
