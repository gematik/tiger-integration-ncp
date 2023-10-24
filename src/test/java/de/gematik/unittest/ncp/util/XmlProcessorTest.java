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

import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.util.EpkaProcessor;
import de.gematik.test.ncp.util.XmlProcessor;
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
        XmlProcessor.instance(
            ResourceUtils.getURL(Testdata.instance().getDefaultEpkaTemplatePath()));
  }

  @Test
  void retrieveNode() {
    var tstResult = assertDoesNotThrow(() -> tstObj.retrieveNode(EpkaProcessor.kvnrXpath()));
    assertNotNull(tstResult);
    assertEquals(Node.ATTRIBUTE_NODE, tstResult.getNodeType());
  }

  @Test
  void updateNode() {
    var newValue = "X123456789";
    assertDoesNotThrow(() -> tstObj.updateNode(EpkaProcessor.kvnrXpath(), newValue));
    assertEquals(newValue, tstObj.retrieveNode(EpkaProcessor.kvnrXpath()).getNodeValue());
  }

  @Test
  void toXmlString() {
    var xmlString = assertDoesNotThrow(() -> tstObj.toXmlString());
    assertNotNull(xmlString);
    assertTrue(xmlString.startsWith("<"));
    assertTrue(xmlString.endsWith(">"));
  }
}
