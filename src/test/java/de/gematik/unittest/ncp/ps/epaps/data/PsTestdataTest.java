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

package de.gematik.unittest.ncp.ps.epaps.data;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.epa.conversion.internal.enumerated.ClassCode;
import de.gematik.epa.conversion.internal.enumerated.CodeInterface;
import de.gematik.epa.conversion.internal.enumerated.ConfidentialityCode;
import de.gematik.epa.conversion.internal.enumerated.EventCode;
import de.gematik.test.ncp.ps.epaps.data.PsTestdata;
import java.util.Arrays;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

class PsTestdataTest {

  @Test
  void createFindQueryValueStringTest() {
    var testdata = ClassCode.DOK_ADMINISTRATIV;
    var result = assertDoesNotThrow(() -> PsTestdata.createFindQueryValueString(testdata));

    assertNotNull(result);
    assertQueryValueString(result, testdata);
  }

  @Test
  void createFindQueryValueListNullTest() {
    var result =
        assertDoesNotThrow(() -> PsTestdata.createFindQueryValueList((CodeInterface[]) null));

    assertNotNull(result);
    assertTrue(result.startsWith("("));
    assertTrue(result.endsWith(")"));
  }

  @Test
  void createFindQueryValueListOneValueTest() {
    var testdata = ConfidentialityCode.RESTRICTED;
    var result = assertDoesNotThrow(() -> PsTestdata.createFindQueryValueList(testdata));

    assertNotNull(result);
    assertTrue(result.startsWith("("));
    assertTrue(result.endsWith(")"));
    assertQueryValueString(result.substring(1, result.length() - 1), testdata);
  }

  @Test
  void createFindQueryValueListSeveralValueTest() {
    CodeInterface[] testdata = {
      EventCode.AMBULANTE_OP,
      EventCode.STATIONAERE_ENTLASSUNG_NACH_REHA,
      EventCode.EXTERNE_VERLEGUNG_PSYCHIATRIE
    };
    var result = assertDoesNotThrow(() -> PsTestdata.createFindQueryValueList(testdata));

    assertNotNull(result);
    assertTrue(result.startsWith("("));
    assertTrue(result.endsWith(")"));

    var resultValues = result.substring(1, result.length() - 1).split(",");
    assertEquals(testdata.length, resultValues.length);
    assertAll(
        Arrays.stream(testdata)
            .map(
                codeInterface ->
                    Arrays.stream(resultValues)
                        .filter(value -> value.contains(codeInterface.getValue()))
                        .findAny()
                        .map(value -> ImmutablePair.of(value, codeInterface))
                        .orElseThrow(
                            () ->
                                new AssertionError(
                                    codeInterface + " is missing in generated value string")))
            .map(
                dataPair -> () -> assertQueryValueString(dataPair.getLeft(), dataPair.getRight())));
  }

  private void assertQueryValueString(String queryValueString, CodeInterface testdata) {
    assertTrue(queryValueString.startsWith("'"));
    assertTrue(queryValueString.endsWith("'"));
    assertTrue(0 < queryValueString.indexOf(testdata.getValue()));
    assertTrue(queryValueString.indexOf(testdata.getValue()) < queryValueString.indexOf("^^"));
    assertTrue(
        queryValueString.indexOf("^^") < queryValueString.indexOf(testdata.getCodingScheme()));
  }
}
