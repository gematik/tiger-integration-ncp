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

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** Enumeration of CDA Levels for ePrescriptions */
@Getter
@RequiredArgsConstructor
public enum PrescriptionCdaLevel {
  /** CDA Level 1: PDF Document */
  LEVEL_1(1, "|eP.PDF"),
  /** CDA Level 3: XML Document */
  LEVEL_3(3, "|eP.XML");

  private final int intValue;
  private final String suffix;

  /**
   * Checks whether the given DocumentUniqueId indicates this CDA Level
   *
   * @param documentUniqueId the DocumentUniqueId to check
   * @return {@code true} if the DocumentUniqueId ends with the suffix matching this CDA Level
   */
  public boolean appliesTo(final String documentUniqueId) {
    return documentUniqueId.endsWith(suffix) && !suffix.equals(documentUniqueId);
  }

  /**
   * Determines the CDA Level for the given DocumentUniqueId
   *
   * @param documentUniqueId the DocumentUniqueId to create the CDA Level for
   * @return the matching PrescriptionCdaLevel
   * @throws IllegalArgumentException if no matching CDA Level could be found
   */
  public static PrescriptionCdaLevel fromDocumentUniqueId(final String documentUniqueId) {
    return Arrays.stream(PrescriptionCdaLevel.values())
        .filter(level -> level.appliesTo(documentUniqueId))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "DocumentUniqueId " + documentUniqueId + " is of no known CDA Level"));
  }

  /**
   * Determines the CDA Level for the given integer value
   *
   * @param value the integer value to find the CDA Level for
   * @return the matching PrescriptionCdaLevel
   * @throws IllegalArgumentException if no matching CDA Level could be found
   */
  public static PrescriptionCdaLevel fromIntValue(final int value) {
    return Arrays.stream(PrescriptionCdaLevel.values())
        .filter(level -> level.intValue == value)
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Value '" + value + "' is no known PrescriptionCdaLevel"));
  }
}
