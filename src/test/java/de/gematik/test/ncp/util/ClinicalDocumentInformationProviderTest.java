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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.gematik.test.ncp.utils.TestUtils;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.hl7.v3.ClinicalDocument;
import org.junit.jupiter.api.Test;

class ClinicalDocumentInformationProviderTest {

  private static final String CLINICAL_DOCUMENT_XML_FILE_NAME = "ClinicalDocument.xml";
  private static final String PRESCRIPTION_CDA1_FILE_NAME = "Prescription_CDA1.xml";

  private static final ClinicalDocument clinicalDocument =
      TestUtils.loadFromXMLResource(
          ClinicalDocument.class,
          ClinicalDocumentInformationProviderTest.class,
          CLINICAL_DOCUMENT_XML_FILE_NAME);

  @Test
  void kvnrFromCDA3DocumentTest() {
    // Arrange & Act
    final var kvnr =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.getPatientKvnr(clinicalDocument));

    // Assert
    assertNotNull(kvnr);
    assertEquals(10, kvnr.length());
    assertTrue(kvnr.startsWith("X"));
  }

  @Test
  void nameFromCDA3DocumentTest() {
    // Arrange & Act
    final var name =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.getPatientName(clinicalDocument));

    // Assert
    assertNotNull(name);
    assertFalse(name.toString().isEmpty());
  }

  @Test
  void birthDataFromCDA3DocumentTest() {
    // Arrange & Act
    final var birthDate =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.getPatientDateOfBirth(clinicalDocument));

    // Assert
    assertNotNull(birthDate);
  }

  @Test
  void getB64PdfPayload_shouldReturnNullForCda3Document() {
    // Arrange & Act & Assert
    assertNull(ClinicalDocumentInformationProvider.getB64PdfPayload(clinicalDocument));
  }

  @Test
  void getB64PdfPayload_shouldReturnBase64EncodedPdfOfCda1Document() {
    // Arrange
    var cda1Document =
        TestUtils.loadFromXMLResource(
            ClinicalDocument.class,
            ClinicalDocumentInformationProviderTest.class,
            PRESCRIPTION_CDA1_FILE_NAME);

    // Act
    var b64 = ClinicalDocumentInformationProvider.getB64PdfPayload(cda1Document);

    // Assert
    assertNotNull(b64);
    assertFalse(b64.isEmpty());
    assertDoesNotThrow(() -> Base64.getDecoder().decode(b64.getBytes(StandardCharsets.UTF_8)));
  }
}
