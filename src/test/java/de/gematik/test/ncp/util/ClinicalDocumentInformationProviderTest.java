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

package de.gematik.test.ncp.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import lombok.SneakyThrows;
import org.hl7.v3.ClinicalDocument;
import org.junit.jupiter.api.Test;

class ClinicalDocumentInformationProviderTest {

  private static final String CLINICAL_DOCUMENT_XML_FILE_NAME = "ClinicalDocument.xml";

  private static final ClinicalDocument clinicalDocument = getClinicalDocument();

  @Test
  void kvnrFromCDA3DocumentTest() {
    // Arrange & Act
    final var kvnr =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.kvnrFromCDA3Document(clinicalDocument));

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
            () -> ClinicalDocumentInformationProvider.nameFromCDA3Document(clinicalDocument));

    // Assert
    assertNotNull(name);
    assertFalse(name.toString().isEmpty());
  }

  @Test
  void birthDataFromCDA3DocumentTest() {
    // Arrange & Act
    final var birthDate =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.birthDataFromCDA3Document(clinicalDocument));

    // Assert
    assertNotNull(birthDate);
  }

  @SneakyThrows
  private static ClinicalDocument getClinicalDocument() {
    return Utils.unmarshalXml(
        ClinicalDocument.class,
        Objects.requireNonNull(
            ClinicalDocumentInformationProviderTest.class.getResourceAsStream(
                CLINICAL_DOCUMENT_XML_FILE_NAME)));
  }
}
