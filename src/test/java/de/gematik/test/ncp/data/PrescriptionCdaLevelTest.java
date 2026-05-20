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

package de.gematik.test.ncp.data;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PrescriptionCdaLevelTest {

  private static final int LEVEL_1_VALUE = 1;
  private static final int LEVEL_3_VALUE = 3;
  private static final String LEVEL_1_SUFFIX = "|eP.PDF";
  private static final String LEVEL_3_SUFFIX = "|eP.XML";

  @Test
  void appliesTo_shouldReturnTrueForMatchingSuffix() {
    assertTrue(PrescriptionCdaLevel.LEVEL_1.appliesTo("someId" + LEVEL_1_SUFFIX));
    assertTrue(PrescriptionCdaLevel.LEVEL_3.appliesTo("someId" + LEVEL_3_SUFFIX));
  }

  @Test
  void appliesTo_shouldReturnFalseForNonMatchingSuffix() {
    assertFalse(PrescriptionCdaLevel.LEVEL_1.appliesTo("someId" + LEVEL_3_SUFFIX));
    assertFalse(PrescriptionCdaLevel.LEVEL_3.appliesTo("someId" + LEVEL_1_SUFFIX));
    assertFalse(PrescriptionCdaLevel.LEVEL_1.appliesTo("someId"));
  }

  @Test
  void appliesTo_shouldReturnFalseWhenDocumentIdIsOnlySuffix() {
    assertFalse(PrescriptionCdaLevel.LEVEL_1.appliesTo(LEVEL_1_SUFFIX));
    assertFalse(PrescriptionCdaLevel.LEVEL_3.appliesTo(LEVEL_3_SUFFIX));
  }

  @Test
  void fromDocumentUniqueId_shouldReturnCorrectLevel() {
    assertEquals(
        PrescriptionCdaLevel.LEVEL_1,
        PrescriptionCdaLevel.fromDocumentUniqueId("123" + LEVEL_1_SUFFIX));
    assertEquals(
        PrescriptionCdaLevel.LEVEL_3,
        PrescriptionCdaLevel.fromDocumentUniqueId("123" + LEVEL_3_SUFFIX));
  }

  @Test
  void fromDocumentUniqueId_shouldThrowExceptionForUnknownLevel() {
    assertThrows(
        IllegalArgumentException.class,
        () -> PrescriptionCdaLevel.fromDocumentUniqueId("123^eP.TXT"));
    assertThrows(
        IllegalArgumentException.class, () -> PrescriptionCdaLevel.fromDocumentUniqueId("123"));
  }

  @Test
  void fromIntValue_shouldReturnCorrectLevel() {
    assertEquals(PrescriptionCdaLevel.LEVEL_1, PrescriptionCdaLevel.fromIntValue(LEVEL_1_VALUE));
    assertEquals(PrescriptionCdaLevel.LEVEL_3, PrescriptionCdaLevel.fromIntValue(LEVEL_3_VALUE));
  }

  @Test
  void fromIntValue_shouldThrowExceptionForUnknownValue() {
    assertThrows(IllegalArgumentException.class, () -> PrescriptionCdaLevel.fromIntValue(2));
    assertThrows(IllegalArgumentException.class, () -> PrescriptionCdaLevel.fromIntValue(99));
  }

  @Test
  void getIntValue_shouldReturnMatchingIntValue() {
    assertEquals(LEVEL_1_VALUE, PrescriptionCdaLevel.LEVEL_1.getIntValue());
    assertEquals(LEVEL_3_VALUE, PrescriptionCdaLevel.LEVEL_3.getIntValue());
  }

  @Test
  void getSuffix_shouldReturnMatchingSuffix() {
    assertEquals(LEVEL_1_SUFFIX, PrescriptionCdaLevel.LEVEL_1.getSuffix());
    assertEquals(LEVEL_3_SUFFIX, PrescriptionCdaLevel.LEVEL_3.getSuffix());
  }
}
