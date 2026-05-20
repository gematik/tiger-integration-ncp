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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class IheUtilsTest {

  private static final String ADHOC_QUERY_RESPONSE_XML_FILE_NAME = "AdhocQueryResponse.xml";
  private static final AdhocQueryResponse testdata = getAdhocQueryResponse();

  @Test
  void readDocumentMetadataFromAdhocQueryResponse() {
    // Arrange
    var patientSummaryLvl = PatientSummaryLevel.LEVEL_3;

    // Act
    var documentMetadata =
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
    var patientSummaryLvl = PatientSummaryLevel.LEVEL_1;

    // Act
    var extrinsicObject =
        assertDoesNotThrow(
            () -> IheUtils.retrieveExtrinsicObjectToCdaLevel(testdata, patientSummaryLvl.level()));

    // Assert
    assertNotNull(extrinsicObject);
  }

  @Test
  void extractDocumentIdsFromAdhocQueryResponse_shouldReturnCorrectNumberOfDocumentIds() {
    // Act
    var documentIds = IheUtils.extractDocumentIdsFromAdhocQueryResponse(testdata);

    // Assert
    assertNotNull(documentIds);
    assertEquals(2, documentIds.size());
  }

  @Test
  void countExtrinsicObjects_shouldReturnCorrectCount() {
    // Act
    var count = IheUtils.countExtrinsicObjects(testdata);

    // Assert
    assertEquals(2, count);
  }

  @Test
  void countExtrinsicObjects_shouldReturnZeroWhenNoExtrinsicObjects() {
    // Arrange
    var emptyResponse = new AdhocQueryResponse();
    emptyResponse.setRegistryObjectList(new RegistryObjectListType());

    // Act
    var count = IheUtils.countExtrinsicObjects(emptyResponse);

    // Assert
    assertEquals(0, count);
  }

  @ParameterizedTest
  @MethodSource("levelMarkerProvider")
  void xdsDocumentEntryUniqueIdLevelMarkerPattern_shouldMatchKnownLevelMarkers(
      final String levelMarker) {
    // Act
    var matcher = IheUtils.XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_PATTERN.matcher(levelMarker);

    // Assert
    assertTrue(matcher.matches());
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

  @SneakyThrows
  private static AdhocQueryResponse getAdhocQueryResponse() {
    return Utils.unmarshalXml(
        AdhocQueryResponse.class,
        Objects.requireNonNull(
            IheUtilsTest.class.getResourceAsStream(ADHOC_QUERY_RESPONSE_XML_FILE_NAME)));
  }

  static Stream<String> levelMarkerProvider() {
    return Stream.concat(
        Arrays.stream(PatientSummaryLevel.values()).map(PatientSummaryLevel::idMarker),
        Arrays.stream(PrescriptionCdaLevel.values()).map(PrescriptionCdaLevel::getSuffix));
  }
}
