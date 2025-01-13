/*
 * Copyright (c) 2024-2025 gematik GmbH
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

import de.gematik.test.tiger.common.config.TigerConfigurationException;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import jakarta.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.xml.stream.XMLInputFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.w3c.dom.Document;

/**
 * The space for utility functions. Only place functions here, which have no better place in a more
 * specific class.
 */
@Slf4j
@UtilityClass
@Accessors(fluent = true)
public class Utils {

  @Getter(lazy = true)
  private static final XMLInputFactory xmlInputFactory = newXmlInputFactory();

  private static XMLInputFactory newXmlInputFactory() {
    final XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
    return factory;
  }

  /**
   * Run a supplier such, that the caller does not need to worry about throws clauses of the used
   * functions
   *
   * @param supplier the supplier
   * @return the result of the {@code get()} function of the supplier
   * @param <T> the return type of the suppliers {@code get()} function.
   */
  @SneakyThrows
  public static <T> T supplyOrThrowSneaky(final ThrowingSupplier<T> supplier) {
    return supplier.get();
  }

  /**
   * Load a config from the tiger configuration into the given class.
   *
   * @param configClass {@link Class} type into which to load the config data
   * @param configPath {@link String} YAML path (e.g. {@code tiger.server.epaps}) of the config to
   *     load.
   * @return the loaded data in an instance of the given class
   * @param <T> return type
   */
  public static <T> T loadConfig(final Class<T> configClass, final String configPath) {
    return TigerGlobalConfiguration.instantiateConfigurationBean(configClass, configPath)
        .orElseThrow(
            () -> new TigerConfigurationException("No configuration data found for " + configPath));
  }

  /**
   * Remove character(s) from the beginning and end of the given String.
   *
   * @param value String from which to remove given character(s)
   * @param characters character(s) to remove from the given String
   * @return the trimmed String
   */
  public static String trimCharacters(
      @NonNull final String value, @NonNull final String characters) {
    final var charactersLength = characters.length();
    var newValue = value;

    while (newValue.startsWith(characters)) {
      newValue = newValue.substring(charactersLength);
    }

    while (newValue.endsWith(characters)) {
      newValue = newValue.substring(0, value.length() - charactersLength);
    }

    return newValue;
  }

  /**
   * Remove one or several character(s) from the beginning and end of the given String.<br>
   * Note, that the order of the given trim characters does matter. E.g.:<br>
   * {@code trimCharacters("[(abc)]", "[", "]", "(", ")"} will produce "abc", whereas {@code
   * trimCharacters("[(abc)]", "(", ")", "[", "]"} will produce "(abc)".
   *
   * @param value String from which to remove given character(s)
   * @param characters character(s) to remove from the given String
   * @return the trimmed String
   */
  public static String trimCharacters(
      @NonNull final String value, @NonNull final String... characters) {
    var newValue = value;
    for (final String chars : characters) {
      newValue = trimCharacters(newValue, chars);
    }
    return newValue;
  }

  /**
   * Run the split operation of {@link String} with delimiter character(s), but do not split if the
   * character(s) is/are within single or double quotes.
   *
   * @param value String to split
   * @param delimiter character(s) at which to split
   * @return array of the parts of the given value
   */
  public static String[] splitConsideringQuotes(
      @NonNull final String value, @NonNull final String delimiter) {
    return value.split(delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)(?=(?:[^']*'[^']*')*[^']*$)");
  }

  /**
   * Unmarshal an XML given as byte array into the corresponding Java object.
   *
   * @param objectType class instance of the type of the Java result object
   * @param marshalledObject byte array with the actual XML
   * @return the Java object created from the XML
   * @param <T> type of the Java result object
   */
  @SneakyThrows
  public static <T> T unmarshalXml(
      @NonNull final Class<T> objectType, final byte[] marshalledObject) {
    final var inputStream = new ByteArrayInputStream(marshalledObject);
    return unmarshalXml(objectType, inputStream);
  }

  @SneakyThrows
  public static <T> T unmarshalXml(
      @NonNull final Class<T> objectType, final InputStream inputStream) {
    final var jaxbUnmarshaller = JAXBContext.newInstance(objectType).createUnmarshaller();
    return jaxbUnmarshaller
        .unmarshal(xmlInputFactory().createXMLStreamReader(inputStream), objectType)
        .getValue();
  }

  @SneakyThrows
  public static <T> T unmarshalXml(@NonNull final Class<T> objectType, final Document document) {
    final var jaxbUnmarshaller = JAXBContext.newInstance(objectType).createUnmarshaller();
    return jaxbUnmarshaller.unmarshal(document, objectType).getValue();
  }

  /**
   * Convert the elements in a list to Strings and populate a new list with them.<br>
   * This function can be used if the content of a list shall be logged / printed, but its elements
   * {@link Object#toString()} method does not suffice.
   *
   * @param list of elements to convert to Strings
   * @param toStringFunction {@link Function} for the conversion of a single element to a String
   * @return {@link List} of Strings
   * @param <T> type of the list elements
   */
  public static <T> List<String> toStringListElements(
      final List<T> list, final Function<T, String> toStringFunction) {
    return Optional.ofNullable(list).stream().flatMap(List::stream).map(toStringFunction).toList();
  }
}
