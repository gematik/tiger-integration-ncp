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

package de.gematik.test.ncp.util;

import static de.gematik.test.ncp.util.Utils.supplyOrThrowSneaky;

import java.io.StringWriter;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.experimental.Accessors;
import org.apache.commons.io.input.XmlStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Class to process XML file, using XPath. As it is abstract it can only be used if extended by a
 * concrete implementation
 *
 * @param <T> Actual class extending this class
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
public abstract class XmlProcessor<T extends XmlProcessor<?>> {

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private static final DocumentBuilder documentBuilder =
      supplyOrThrowSneaky(() -> DocumentBuilderFactory.newInstance().newDocumentBuilder());

  // The Sonar warning can be ignored, as the created Getter method serializes all calls
  //  and therefore is thread safe.
  @Getter(lazy = true, value = AccessLevel.PROTECTED, onMethod_ = @Synchronized)
  private static final XPath xpathProcessor = XPathFactory.newInstance().newXPath();

  @Getter(lazy = true, value = AccessLevel.PROTECTED)
  private static final Transformer transformer =
      supplyOrThrowSneaky(() -> TransformerFactory.newInstance().newTransformer());

  @Getter private final URL xmlFileUrl;

  @Getter(lazy = true)
  private final Document xmlDocument =
      supplyOrThrowSneaky(
          () -> documentBuilder().parse(new InputSource(new XmlStreamReader(xmlFileUrl))));

  protected abstract T that();

  @SneakyThrows
  public Node retrieveNode(@NonNull XPathExpression xpath) {
    return (Node) xpath.evaluate(xmlDocument(), XPathConstants.NODE);
  }

  public T updateNode(@NonNull XPathExpression xpath, @NonNull String newValue) {
    retrieveNode(xpath).setNodeValue(newValue);
    return that();
  }

  /**
   * Write the XML file to a string
   *
   * @return {@link String} the XML as String.
   */
  @SneakyThrows
  public String toXmlString() {
    var writer = new StringWriter();
    transformer().transform(new DOMSource(xmlDocument()), new StreamResult(writer));
    return writer.toString();
  }

  private static class XmlProcessorImpl extends XmlProcessor<XmlProcessorImpl> {

    XmlProcessorImpl(URL xmlFileUrl) {
      super(xmlFileUrl);
    }

    @Override
    protected XmlProcessorImpl that() {
      return this;
    }
  }

  /**
   * Get a default implementation of {@link XmlProcessor}
   *
   * @param xmlFileUrl
   * @return
   */
  public static XmlProcessor<XmlProcessorImpl> instance(@NonNull URL xmlFileUrl) {
    return new XmlProcessorImpl(xmlFileUrl);
  }
}
