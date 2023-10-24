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

import de.gematik.test.tiger.common.config.TigerConfigurationException;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.xml.stream.XMLInputFactory;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.Base64;
import org.junit.jupiter.api.function.ThrowingSupplier;

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
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
    return factory;
  }

  /**
   * Transform a {@link Function} such, that if it throws the given exception type, the exception
   * won't be thrown, but instead the given alternative value will be returned.<br>
   * Exceptions extending the given exception type, will also be catched. Other exception types will
   * still be thrown.
   *
   * @param code {@link Function} to be transformed
   * @param exceptionType {@link Exception} to be catched, instead of thrown
   * @param insteadValue the value to be returned in case an exception of the given type was
   *     catched.
   * @return the transformed {@link Function}
   * @param <T> parameter type of the function
   * @param <R> return type of the function
   */
  public static <T, R> Function<T, R> swallowExceptionFunction(
      @NonNull Function<T, R> code,
      @NonNull Class<? extends Exception> exceptionType,
      R insteadValue) {
    return t -> {
      try {
        return code.apply(t);
      } catch (Exception e) {
        if (exceptionType.isAssignableFrom(e.getClass())) {
          return insteadValue;
        }
        throw e;
      }
    };
  }

  /**
   * Transform a {@link Function} such, that if it throws an exception, the exception won't be
   * thrown, but instead the given alternative value will be returned.
   *
   * @param code {@link Function} to be transformed
   * @param insteadValue the value to be returned in case an exception was catched.
   * @return the transformed {@link Function}
   * @param <T> parameter type of the function
   * @param <R> return type of the function
   */
  public static <T, R> Function<T, R> swallowExceptionFunction(
      @NonNull Function<T, R> code, R insteadValue) {
    return swallowExceptionFunction(code, Exception.class, insteadValue);
  }

  /**
   * Transform a {@link Function} such, that if it throws an exception, the exception won't be
   * thrown, but instead {@code null} will be returned.
   *
   * @param code {@link Function} to be transformed
   * @return the transformed {@link Function}
   * @param <T> parameter type of the function
   * @param <R> return type of the function
   */
  public static <T, R> Function<T, R> swallowExceptionFunction(@NonNull Function<T, R> code) {
    return swallowExceptionFunction(code, null);
  }

  /**
   * Wrapper around the {@link Response#readEntity(Class)} function, which returns null, in case
   * readEntity was not successful.
   *
   * @param response {@link Response} from which to read the entity
   * @param entityType Class type of the entity
   * @return the entity read from the response or null, if it couldn't be read
   * @param <T> type of the entity
   */
  public static <T> T readEntityFromResponse(Response response, Class<T> entityType) {
    return swallowExceptionFunction((Function<Response, T>) resp -> resp.readEntity(entityType))
        .apply(response);
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
  public static <T> T supplyOrThrowSneaky(ThrowingSupplier<T> supplier) {
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
  public static <T> T loadConfig(Class<T> configClass, String configPath) {
    return TigerGlobalConfiguration.instantiateConfigurationBean(configClass, configPath)
        .orElseThrow(
            () -> new TigerConfigurationException("No configuration data found for " + configPath));
  }

  /**
   * Build an {@link URI} object from the given data.
   *
   * @param scheme {@link String} the scheme, e.g. "http" of the URI. If the given value is "://"
   *     missing, it will be added to the scheme.
   * @param hostname {@link String} hostname for the URI. May also be an IP address.
   * @param port {@link Integer} port number. Optional, null can be used for this parameter, if no
   *     port number shall be provided
   * @param paths {@link String}[] Paths of the URI. The are added in the order they are provided.
   *     Missing "/" will be added.
   * @return {@link URI} the created URI
   */
  public static URI buildUri(
      @NonNull String scheme, @NonNull String hostname, Integer port, String... paths) {
    var formattedScheme = ensureEndsWith(scheme, "://");
    var result =
        URI.create(
            formattedScheme
                .concat(hostname)
                .concat(Optional.ofNullable(port).map(p -> ":" + p).orElse("")));

    return Stream.of(paths)
        .filter(Objects::nonNull)
        .map(path -> ensureStartsWith(path, "/"))
        .reduce(String::concat)
        .map(result::resolve)
        .orElse(result);
  }

  /**
   * Same as {@link #buildUri(String, String, Integer, String...)}, but with "http" for the scheme
   * and no port number
   *
   * @param hostname {@link String} hostname for the URI. May also be an IP address.
   * @param paths {@link String}[] Paths of the URI. The are added in the order they are provided.
   *     Missing "/" will be added.
   * @return {@link URI} the created URI
   */
  public static URI buildUri(@NonNull String hostname, String... paths) {
    return buildUri("http://", hostname, null, paths);
  }

  /**
   * Convenience String function to make sure the given String ends with the given suffix.
   *
   * @param string {@link String} the string to check
   * @param suffix {@link String} the suffix which shall be at the end of the String
   * @return {@link String} If it already ends with the suffix, the original string is returned, if
   *     not the suffix is added to the end of the string and the concatenated String is returned
   */
  public static String ensureEndsWith(@NonNull String string, @NonNull String suffix) {
    if (!string.endsWith(suffix)) return string.concat(suffix);
    return string;
  }

  /**
   * Convenience String function to make sure the given String starts with the given prefix.
   *
   * @param string {@link String} the string to check
   * @param prefix {@link String} the prefix which shall be at the start of the String
   * @return {@link String} If it already starts with the prefix, the original string is returned,
   *     if not the prefix is added at the start of the string and the concatenated String is
   *     returned
   */
  public static String ensureStartsWith(@NonNull String string, @NonNull String prefix) {
    if (!string.startsWith(prefix)) return prefix.concat(string);
    return string;
  }

  /**
   * Remove character(s) from the beginning and end of the given String.
   *
   * @param value String from which to remove given character(s)
   * @param characters character(s) to remove from the given String
   * @return the trimmed String
   */
  public static String trimCharacters(@NonNull String value, @NonNull String characters) {
    var charactersLength = characters.length();
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
  public static String trimCharacters(@NonNull String value, @NonNull String... characters) {
    var newValue = value;
    for (String chars : characters) {
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
  public static String[] splitConsideringQuotes(@NonNull String value, @NonNull String delimiter) {
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
  public static <T> T unmarshalXml(@NonNull Class<T> objectType, byte[] marshalledObject) {
    var jaxbUnmarshaller = JAXBContext.newInstance(objectType).createUnmarshaller();
    var inputStream = new ByteArrayInputStream(marshalledObject);
    return jaxbUnmarshaller
        .unmarshal(xmlInputFactory().createXMLStreamReader(inputStream), objectType)
        .getValue();
  }

  /**
   * Unmarshal an XML given as Base64 encoded byte array into the corresponding Java object.
   *
   * @param objectType class instance of the type of the Java result object
   * @param xmlEncoded Base64 encoded byte array with the actual XML
   * @return the Java object created from the XML
   * @param <T> type of the Java result object
   */
  public static <T> T unmarshalBase64EncodedXml(Class<T> objectType, byte[] xmlEncoded) {

    byte[] xmlDecoded = xmlEncoded;

    while (Base64.isArrayByteBase64(xmlDecoded)) {
      xmlDecoded = Base64.decodeBase64(xmlDecoded);
    }

    return Utils.unmarshalXml(objectType, xmlDecoded);
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
      List<T> list, Function<T, String> toStringFunction) {
    return Optional.ofNullable(list).stream().flatMap(List::stream).map(toStringFunction).toList();
  }
}
