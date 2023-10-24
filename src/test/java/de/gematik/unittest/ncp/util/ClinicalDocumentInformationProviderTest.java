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

import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.util.ClinicalDocumentInformationProvider;
import de.gematik.unittest.testutil.TestUtils;
import org.hl7.v3.ClinicalDocument;
import org.junit.jupiter.api.Test;

class ClinicalDocumentInformationProviderTest {

  private static final ClinicalDocument clinicalDocument =
      DataUtils.readPatientSummaryLvl3(TestUtils.retrievePatientSummaryDO());

  @Test
  void kvnrFromCDA3DocumentTest() {
    var kvnr =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.kvnrFromCDA3Document(clinicalDocument));

    assertNotNull(kvnr);
    assertEquals(10, kvnr.length());
    assertTrue(kvnr.startsWith("X"));
  }

  @Test
  void nameFromCDA3DocumentTest() {
    var name =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.nameFromCDA3Document(clinicalDocument));

    assertNotNull(name);
    assertFalse(name.isEmpty());
  }

  @Test
  void birthDataFromCDA3DocumentTest() {
    var birthDate =
        assertDoesNotThrow(
            () -> ClinicalDocumentInformationProvider.birthDataFromCDA3Document(clinicalDocument));

    assertNotNull(birthDate);
  }
}
