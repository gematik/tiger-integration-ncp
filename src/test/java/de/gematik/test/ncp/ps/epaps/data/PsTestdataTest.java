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

package de.gematik.test.ncp.ps.epaps.data;

import static de.gematik.test.ncp.utils.TestUtils.mapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import de.gematik.epa.conversion.internal.enumerated.ClassCode;
import de.gematik.epa.conversion.internal.enumerated.CodeInterface;
import de.gematik.test.ncp.data.PracticeImpl;
import de.gematik.test.ncp.gen.epa.api.documents.dto.DocumentMetadata;
import de.gematik.test.ncp.gen.epa.api.documents.dto.PutDocumentsRequestDTO;
import de.gematik.test.ncp.utils.TestUtils;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@Slf4j
class PsTestdataTest {

  @ParameterizedTest
  @CsvSource({
    "DOK_ADMINISTRATIV, '''ADM^^1.3.6.1.4.1.19376.3.276.1.5.8'''",
    "BEFUNDBERICHT, '''BEF^^1.3.6.1.4.1.19376.3.276.1.5.8'''",
    "FORSCHUNG, '''FOR^^1.3.6.1.4.1.19376.3.276.1.5.8'''"
  })
  void createFindQueryValueStringTest(final String codeName, final String expected) {
    // Arrange
    final var code = ClassCode.valueOf(codeName);

    // Act
    final var testee = assertDoesNotThrow(() -> PsTestdata.createFindQueryValueString(code));

    // Assert
    log.info(testee);
    assertNotNull(testee);
    assertEquals(expected, testee);
  }

  @ParameterizedTest
  @CsvSource({
    "null, '()'",
    "'DOK_ADMINISTRATIV', ('ADM^^1.3.6.1.4.1.19376.3.276.1.5.8')",
    "'DOK_ADMINISTRATIV,BEFUNDBERICHT,FORSCHUNG', '(''ADM^^1.3.6.1.4.1.19376.3.276.1.5.8'',''BEF^^1.3.6.1.4.1.19376.3.276.1.5.8'',''FOR^^1.3.6.1.4.1.19376.3.276.1.5.8'')'"
  })
  void createFindQueryValueListNullTest(final String codeNames, final String expected) {
    // Arrange
    final CodeInterface[] codes =
        "null".equals(codeNames)
            ? null
            : Arrays.stream(codeNames.split(","))
                .map(ClassCode::valueOf)
                .toArray(CodeInterface[]::new);

    // Act
    final var testee = assertDoesNotThrow(() -> PsTestdata.createFindQueryValueList(codes));

    // Assert
    log.info(testee);
    assertNotNull(testee);
    assertEquals(expected, testee);
  }

  @ParameterizedTest
  @CsvSource({
    "practice_1.json, documentMetadata_1.json",
    "practice_2.json, documentMetadata_2.json"
  })
  @SneakyThrows
  void createDocumentMetadataTest(final String practiceData, final String expectedData) {
    // Arrange
    final var practice =
        TestUtils.loadFromJsonResource(PracticeImpl.class, this.getClass(), practiceData);
    final var expected =
        TestUtils.loadFromJsonResource(DocumentMetadata.class, this.getClass(), expectedData);

    // Act
    final var testee = PsTestdata.createDocumentMetadata(practice);

    // Assert
    log.info(mapper.writeValueAsString(testee));
    assertThat(testee)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(LocalDateTime.class)
        .ignoringFields("title")
        .isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void createPutDocumentRequestForEPKATest() {
    // Arrange
    final var practice =
        TestUtils.loadFromJsonResource(PracticeImpl.class, this.getClass(), "practice_1.json");
    final var expected =
        TestUtils.loadFromJsonResource(
            PutDocumentsRequestDTO.class, this.getClass(), "putDocumentsRequestDto.json");

    // Act
    final var testee =
        PsTestdata.createPutDocumentRequestForEPKA(
            "kvnr", practice, "epka".getBytes(StandardCharsets.UTF_8));

    log.info(mapper.writeValueAsString(testee));

    // Assert
    assertThat(testee)
        .usingRecursiveComparison()
        .ignoringFieldsOfTypes(LocalDateTime.class)
        .ignoringFieldsMatchingRegexes(".*\\.title")
        .isEqualTo(expected);
  }
}
