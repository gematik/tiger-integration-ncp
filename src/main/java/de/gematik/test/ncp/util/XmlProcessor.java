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

import static de.gematik.test.ncp.util.Utils.supplyOrThrowSneaky;

import java.io.StringWriter;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
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
import lombok.NonNull;
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
public class XmlProcessor<T extends XmlProcessor<T>> {
  // The Sonar warning can be ignored, as the created Getter method serializes all calls
  //  and therefore is thread safe.
  @lombok.Generated private static final Object $LOCK = new Object[0];
  private static final java.util.concurrent.atomic.AtomicReference<Object> documentBuilder =
      new java.util.concurrent.atomic.AtomicReference<>();
  private static final java.util.concurrent.atomic.AtomicReference<Object> xpathProcessor =
      new java.util.concurrent.atomic.AtomicReference<>();
  private static final java.util.concurrent.atomic.AtomicReference<Object> transformer =
      new java.util.concurrent.atomic.AtomicReference<>();
  private final URL xmlFileUrl;
  private final java.util.concurrent.atomic.AtomicReference<Object> xmlDocument =
      new java.util.concurrent.atomic.AtomicReference<>();

  public Node retrieveNode(@NonNull final XPathExpression xpath) {
    try {
      return (Node) xpath.evaluate(xmlDocument(), XPathConstants.NODE);
    } catch (final java.lang.Throwable $ex) {
      throw lombok.Lombok.sneakyThrow($ex);
    }
  }

  @SuppressWarnings("unchecked")
  public T updateNode(@NonNull final XPathExpression xpath, @NonNull final String newValue) {
    Optional.ofNullable(retrieveNode(xpath)).ifPresent(node -> node.setNodeValue(newValue));
    return (T) this;
  }

  /**
   * Write the XML file to a string
   *
   * @return {@link String} the XML as String.
   */
  public String toXmlString() {
    try {
      final var writer = new StringWriter();
      Objects.requireNonNull(transformer())
          .transform(new DOMSource(xmlDocument()), new StreamResult(writer));
      return writer.toString();
    } catch (final java.lang.Throwable $ex) {
      throw lombok.Lombok.sneakyThrow($ex);
    }
  }

  @lombok.Generated
  public XmlProcessor(final URL xmlFileUrl) {
    this.xmlFileUrl = xmlFileUrl;
  }

  @lombok.Generated
  protected static DocumentBuilder documentBuilder() {
    Object value = XmlProcessor.documentBuilder.get();
    if (value == null) {
      synchronized (XmlProcessor.documentBuilder) {
        value = XmlProcessor.documentBuilder.get();
        if (value == null) {
          final DocumentBuilder actualValue =
              supplyOrThrowSneaky(
                  () -> {
                    var dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    return dbf.newDocumentBuilder();
                  });
          value = actualValue == null ? XmlProcessor.documentBuilder : actualValue;
          XmlProcessor.documentBuilder.set(value);
        }
      }
    }
    return (DocumentBuilder) (value == XmlProcessor.documentBuilder ? null : value);
  }

  @lombok.Generated
  protected static XPath xpathProcessor() {
    synchronized (XmlProcessor.$LOCK) {
      Object value = XmlProcessor.xpathProcessor.get();
      if (value == null) {
        synchronized (XmlProcessor.xpathProcessor) {
          value = XmlProcessor.xpathProcessor.get();
          if (value == null) {
            final XPath actualValue = XPathFactory.newInstance().newXPath();
            actualValue.setNamespaceContext(
                new javax.xml.namespace.NamespaceContext() {
                  @Override
                  public String getNamespaceURI(final String prefix) {
                    return "fhir".equals(prefix)
                        ? "http://hl7.org/fhir"
                        : javax.xml.XMLConstants.NULL_NS_URI;
                  }

                  @Override
                  public String getPrefix(final String uri) {
                    return null;
                  }

                  @Override
                  public java.util.Iterator<String> getPrefixes(final String uri) {
                    return null;
                  }
                });
            value = actualValue;
            XmlProcessor.xpathProcessor.set(value);
          }
        }
      }
      return (XPath) (value == XmlProcessor.xpathProcessor ? null : value);
    }
  }

  @lombok.Generated
  protected static Transformer transformer() {
    Object value = XmlProcessor.transformer.get();
    if (value == null) {
      synchronized (XmlProcessor.transformer) {
        value = XmlProcessor.transformer.get();
        if (value == null) {
          final Transformer actualValue =
              supplyOrThrowSneaky(() -> TransformerFactory.newInstance().newTransformer());
          value = actualValue == null ? XmlProcessor.transformer : actualValue;
          XmlProcessor.transformer.set(value);
        }
      }
    }
    return (Transformer) (value == XmlProcessor.transformer ? null : value);
  }

  @lombok.Generated
  public Document xmlDocument() {
    Object value = this.xmlDocument.get();
    if (value == null) {
      synchronized (this.xmlDocument) {
        value = this.xmlDocument.get();
        if (value == null) {
          final Document actualValue =
              supplyOrThrowSneaky(
                  () ->
                      Objects.requireNonNull(documentBuilder())
                          .parse(new InputSource(new XmlStreamReader(xmlFileUrl))));
          value = actualValue == null ? this.xmlDocument : actualValue;
          this.xmlDocument.set(value);
        }
      }
    }
    return (Document) (value == this.xmlDocument ? null : value);
  }
}
