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

import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.util.EpkaProcessor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class EpkaProcessorTest {

  private EpkaProcessor tstObj;

  @SneakyThrows
  @BeforeEach
  void init() {
    tstObj =
        new EpkaProcessor(ResourceUtils.getURL(Testdata.instance().getDefaultEpkaTemplatePath()));
  }

  @Test
  void updateKvnr() {
    var newKvnr = "X987654321";
    assertDoesNotThrow(() -> tstObj.updateKvnr(newKvnr));
    assertEquals(newKvnr, tstObj.retrieveNode(EpkaProcessor.kvnrXpath()).getNodeValue());
  }

  @Test
  void updateBirthDate() {
    var newBirthDate = LocalDate.now();
    assertDoesNotThrow(() -> tstObj.updateBirthDate(newBirthDate));
    assertEquals(
        newBirthDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
        tstObj.retrieveNode(EpkaProcessor.birthDatePath()).getNodeValue());
  }

  @Test
  void updateName() {
    var newName =
        PersonName.fromString(
            "Prof. Freifrau Sybille Gertrud von Oberammergau und Niederrheindings");
    assertDoesNotThrow(() -> tstObj.updateName(newName));
    assertEquals(
        newName.toString(), tstObj.retrieveNode(EpkaProcessor.fullNamePath()).getNodeValue());

    assertEquals(
        newName.titles(), tstObj.retrieveNode(EpkaProcessor.namePrefixPath()).getNodeValue());
    assertEquals(
        newName.givenNames(), tstObj.retrieveNode(EpkaProcessor.givenNamePath()).getNodeValue());
    assertEquals(
        newName.lastNames(), tstObj.retrieveNode(EpkaProcessor.lastNamePath()).getNodeValue());
  }

  @Test
  void that() {
    var alsoTstObj =
        assertDoesNotThrow(
            () -> tstObj.updateNode(EpkaProcessor.fullNamePath(), "Mr. Bli Bla Blubs"));
    assertInstanceOf(EpkaProcessor.class, alsoTstObj);
  }
}
