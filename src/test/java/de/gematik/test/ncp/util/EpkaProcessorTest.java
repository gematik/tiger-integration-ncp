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

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.data.Testdata;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class EpkaProcessorTest {

  private EpkaProcessor processor;

  @SneakyThrows
  @BeforeEach
  void init() {
    processor =
        new EpkaProcessor(ResourceUtils.getURL(Testdata.instance().getDefaultEpkaTemplatePath()));
  }

  @Test
  void updateKvnr() {
    // Arrange
    final var newKvnr = "X987654321";

    // Act
    assertDoesNotThrow(() -> processor.updateKvnr(newKvnr));

    // Assert
    assertEquals(newKvnr, processor.retrieveNode(EpkaProcessor.kvnrXpath()).getNodeValue());
  }

  @Test
  void updateBirthDate() {
    // Arrange
    final var newBirthDate = LocalDate.now();

    // Act
    assertDoesNotThrow(() -> processor.updateBirthDate(newBirthDate));

    // Assert
    assertEquals(
        newBirthDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
        processor.retrieveNode(EpkaProcessor.birthDatePath()).getNodeValue());
  }

  @Test
  void updateName() {
    // Arrange
    final var newName =
        PersonName.fromString(
            "Prof. Freifrau Sybille Gertrud von Oberammergau und Niederrheindings");

    // Act
    assertDoesNotThrow(() -> processor.updateName(newName));

    // Assert
    assertEquals(
        newName.toString(), processor.retrieveNode(EpkaProcessor.fullNamePath()).getNodeValue());
    assertEquals(
        newName.titles(), processor.retrieveNode(EpkaProcessor.namePrefixPath()).getNodeValue());
    assertEquals(
        newName.givenNames(), processor.retrieveNode(EpkaProcessor.givenNamePath()).getNodeValue());
    assertEquals(
        newName.lastNames(), processor.retrieveNode(EpkaProcessor.lastNamePath()).getNodeValue());
  }
}
