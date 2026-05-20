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

package de.gematik.test.ncp.ncpeh.client.dataobject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

class SoapUtilsTest {

  @Test
  void extractBodyAsDocument_validData_returnsDocument() {
    // Arrange
    final byte[] data =
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><test>data</test></soap:Body></soap:Envelope>"
            .getBytes();

    // Act
    final Document document = SoapUtils.extractBodyAsDocument(data);

    // Assert
    assertNotNull(document);
  }

  @Test
  void extractBodyAsDocument_invalidData_throwsSOAPException() {
    // Arrange
    final byte[] data = "invalid data".getBytes();

    // Act & Assert
    assertThrows(SOAPException.class, () -> SoapUtils.extractBodyAsDocument(data));
  }

  @Test
  void extractBodyAsDocument_emptyData_throwsSOAPException() {
    // Arrange
    final byte[] data = new byte[0];

    // Act & Assert
    assertThrows(SOAPException.class, () -> SoapUtils.extractBodyAsDocument(data));
  }
}
