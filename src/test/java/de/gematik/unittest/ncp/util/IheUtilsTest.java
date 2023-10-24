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

package de.gematik.unittest.ncp.util;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.ncpeh.NcpehInterface.PatientSummaryLevel;
import de.gematik.test.ncp.util.IheUtils;
import de.gematik.unittest.testutil.TestUtils;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.jupiter.api.Test;

class IheUtilsTest {

  private final AdhocQueryResponse testdata = TestUtils.adhocQueryResponse();

  @Test
  void readDocumentMetadataFromAdhocQueryResponse() {
    var patientSummaryLvl = PatientSummaryLevel.LEVEL_3;
    var documentMetadata =
        assertDoesNotThrow(
            () ->
                IheUtils.readDocumentMetadataFromAdhocQueryResponse(
                    testdata, patientSummaryLvl.level()));

    assertNotNull(documentMetadata);

    assertEquals(
        patientSummaryLvl, PatientSummaryLevel.fromDocumentUniqueId(documentMetadata.uniqueId()));
  }

  @Test
  void retrieveExtrinsicObjectToCdaLevel() {
    var patientSummaryLvl = PatientSummaryLevel.LEVEL_1;
    var extrinsicObject =
        assertDoesNotThrow(
            () -> IheUtils.retrieveExtrinsicObjectToCdaLevel(testdata, patientSummaryLvl.level()));

    assertNotNull(extrinsicObject);
  }
}
