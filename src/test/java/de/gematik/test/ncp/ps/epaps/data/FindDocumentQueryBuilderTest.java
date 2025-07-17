/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.ps.epaps.data;

import de.gematik.epa.conversion.internal.enumerated.ClassCode;
import de.gematik.epa.conversion.internal.enumerated.FormatCode;
import de.gematik.epa.conversion.internal.enumerated.TypeCode;
import de.gematik.test.ncp.utils.TestUtils;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@Slf4j
class FindDocumentQueryBuilderTest {

  static Stream<Arguments> provideParameters() {
    return Stream.of(
        Arguments.of(null, null, null, "findDocumentQueryBuilder_1.json"),
        Arguments.of(
            ClassCode.MEDIZINISCHER_AUSWEIS, null, null, "findDocumentQueryBuilder_2.json"),
        Arguments.of(null, FormatCode.PATIENTEN_KURZ_AKTE, null, "findDocumentQueryBuilder_3.json"),
        Arguments.of(null, null, TypeCode.ERGEBNISSE_DIAGNOSTIK, "findDocumentQueryBuilder_4.json"),
        Arguments.of(
            ClassCode.BEFUNDBERICHT,
            FormatCode.MUTTERPASS,
            TypeCode.AERZTLICHE_BESCHEINIGUNGEN,
            "findDocumentQueryBuilder_5.json"));
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource("provideParameters")
  void testBuild(
      final ClassCode classCode,
      final FormatCode formatCode,
      final TypeCode typeCode,
      final String expected) {
    // Arrange
    final FindDocumentQueryBuilder findDocumentQueryBuilder =
        FindDocumentQueryBuilder.getInstance()
            .withClassCode(classCode)
            .withFormatCode(formatCode)
            .withTypeCode(typeCode);

    // Act
    final var testee = findDocumentQueryBuilder.build();
    log.info(TestUtils.mapper.writeValueAsString(testee));

    // Assert
    final var expectedJson = TestUtils.readResourceFile(this.getClass(), expected);
    JSONAssert.assertEquals(
        expectedJson, TestUtils.mapper.writeValueAsString(testee), JSONCompareMode.STRICT);
  }
}
