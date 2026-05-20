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

import de.gematik.test.ncp.data.Testdata;
import jakarta.xml.soap.Node;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class XmlProcessorTest {

  private XmlProcessor<?> tstObj;

  @SneakyThrows
  @BeforeEach
  void init() {
    tstObj =
        new XmlProcessor<>(ResourceUtils.getURL(Testdata.instance().getDefaultEpkaTemplatePath()));
  }

  @Test
  void retrieveNode() {
    // Arrange & Act
    final var tstResult = assertDoesNotThrow(() -> tstObj.retrieveNode(EpkaProcessor.kvnrXpath()));

    // Assert
    assertNotNull(tstResult);
    assertEquals(Node.ATTRIBUTE_NODE, tstResult.getNodeType());
  }

  @Test
  void updateNode() {
    // Arrange
    final var newValue = "X123456789";

    // Act
    assertDoesNotThrow(() -> tstObj.updateNode(EpkaProcessor.kvnrXpath(), newValue));

    // Assert
    assertEquals(newValue, tstObj.retrieveNode(EpkaProcessor.kvnrXpath()).getNodeValue());
  }

  @Test
  void toXmlString() {
    // Arrange & Act
    final var xmlString = assertDoesNotThrow(() -> tstObj.toXmlString());

    // Assert
    assertNotNull(xmlString);
    assertTrue(xmlString.startsWith("<"));
    assertTrue(xmlString.endsWith(">"));
  }
}
