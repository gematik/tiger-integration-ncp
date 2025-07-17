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

package de.gematik.test.ncp.util;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import java.util.Objects;
import lombok.SneakyThrows;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.jupiter.api.Test;

class IheUtilsTest {

  private static final String ADHOC_QUERY_RESPONSE_XML_FILE_NAME = "AdhocQueryResponse.xml";
  private static final AdhocQueryResponse testdata = getAdhocQueryResponse();

  @Test
  void readDocumentMetadataFromAdhocQueryResponse() {
    // Arrange
    final var patientSummaryLvl = PatientSummaryLevel.LEVEL_3;

    // Act
    final var documentMetadata =
        assertDoesNotThrow(
            () ->
                IheUtils.readDocumentMetadataFromAdhocQueryResponse(
                    testdata, patientSummaryLvl.level()));

    // Assert
    assertNotNull(documentMetadata);

    assertEquals(
        patientSummaryLvl, PatientSummaryLevel.fromDocumentUniqueId(documentMetadata.uniqueId()));
  }

  @Test
  void retrieveExtrinsicObjectToCdaLevel() {
    // Arrange
    final var patientSummaryLvl = PatientSummaryLevel.LEVEL_1;

    // Act
    final var extrinsicObject =
        assertDoesNotThrow(
            () -> IheUtils.retrieveExtrinsicObjectToCdaLevel(testdata, patientSummaryLvl.level()));

    // Assert
    assertNotNull(extrinsicObject);
  }

  @SneakyThrows
  private static AdhocQueryResponse getAdhocQueryResponse() {
    return Utils.unmarshalXml(
        AdhocQueryResponse.class,
        Objects.requireNonNull(
            IheUtilsTest.class.getResourceAsStream(ADHOC_QUERY_RESPONSE_XML_FILE_NAME)));
  }
}
