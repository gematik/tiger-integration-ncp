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

package de.gematik.test.ncp.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.gematik.test.ncp.util.Utils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import javax.xml.namespace.QName;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/** Utility functions, needed in integration or unit tests. */
@UtilityClass
public class TestUtils {

  public static final ObjectMapper mapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

  /**
   * Loads an object from a JSON resource file.
   *
   * @param <T> the type of the object to be loaded
   * @param <R> the type of the class loader
   * @param clazz the class of the object to be loaded
   * @param loader the class loader used to load the resource
   * @param name the name of the resource file
   * @return the object loaded from the JSON resource file
   */
  @SneakyThrows
  public static <T, R> T loadFromJsonResource(
      final Class<T> clazz, final Class<R> loader, final String name) {
    return mapper.readValue(loader.getResourceAsStream(name), clazz);
  }

  @SneakyThrows
  public static <T, R> T loadFromJsonResource(
      final TypeReference<T> valueTypeRef, final Class<R> loader, final String name) {
    return mapper.readValue(loader.getResourceAsStream(name), valueTypeRef);
  }

  /**
   * Loads an object from an XML resource file.
   *
   * @param <T> the type of the object to be loaded
   * @param <R> the type of the class loader
   * @param clazz the class of the object to be loaded
   * @param loader the class loader used to load the resource
   * @param name the name of the resource file
   * @return the object loaded from the XML resource file
   */
  @SneakyThrows
  public static <T, R> T loadFromXMLResource(
      final Class<T> clazz, final Class<R> loader, final String name) {
    return Utils.unmarshalXml(clazz, Objects.requireNonNull(loader.getResourceAsStream(name)));
  }

  /**
   * Reads the content of a resource file as a String.
   *
   * @param <T> the type of the class
   * @param loader the class used to load the resource
   * @param name the name of the resource file
   * @return the content of the resource file as a String
   */
  @SneakyThrows
  public static <T> String readResourceFile(final Class<T> loader, final String name) {
    return new String(
        Objects.requireNonNull(loader.getResourceAsStream(name)).readAllBytes(),
        StandardCharsets.UTF_8);
  }

  public static <T> String marshalXml(@NonNull final T obj) {
    return marshalXml(obj, null);
  }

  @SuppressWarnings("unchecked")
  @SneakyThrows
  public static <T> String marshalXml(@NonNull final T obj, final QName rootQName) {
    final var marshaller = JAXBContext.newInstance(obj.getClass()).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
    final StringWriter writer = new StringWriter();

    if (!obj.getClass().isAnnotationPresent(XmlRootElement.class)) {
      final JAXBElement<T> root =
          new JAXBElement<>(
              Optional.ofNullable(rootQName)
                  .orElse(
                      new QName(obj.getClass().getPackageName(), obj.getClass().getSimpleName())),
              (Class<T>) obj.getClass(),
              obj);

      marshaller.marshal(root, writer);
    } else {
      marshaller.marshal(obj, writer);
    }
    return writer.toString();
  }
}
