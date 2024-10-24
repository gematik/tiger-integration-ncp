/*
 * Copyright (c) 2024. gematik GmbH
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

import de.gematik.ncpeh.api.response.ErrorInformation;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.ncp.utils.TestUtils;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UtilsTest {

  public static final Function<String, Boolean> TEST_FUNCTION =
      s ->
          Stream.of(Boolean.TRUE, Boolean.FALSE)
              .filter(b -> b.toString().equalsIgnoreCase(s))
              .findFirst()
              .orElseThrow(() -> new IllegalArgumentException("That ain't no boolean"));

  private static final AtomicReference<Level> jaxrsUtilsLogLevel = new AtomicReference<>();

  @BeforeAll
  static void before() {
    jaxrsUtilsLogLevel.lazySet(
        TestUtils.overwriteLoglevel(JAXRSUtils.class.getCanonicalName(), Level.OFF));
  }

  @Test
  void swallowExceptionFunctionPrm3() {
    final var func =
        assertDoesNotThrow(
            () ->
                Utils.swallowExceptionFunction(
                    TEST_FUNCTION, RuntimeException.class, Boolean.FALSE));

    assertDoesNotThrow(() -> func.apply("true"));
    final var result = assertDoesNotThrow(() -> func.apply("no boolean"));
    assertFalse(result, "Wrong value of result");

    final var func2 =
        assertDoesNotThrow(
            () -> Utils.swallowExceptionFunction(TEST_FUNCTION, IOException.class, Boolean.FALSE));

    assertDoesNotThrow(() -> func2.apply("false"));
    assertThrows(IllegalArgumentException.class, () -> func2.apply("no boolean"));
  }

  @Test
  void swallowExceptionFunctionPrm2() {
    final var func =
        assertDoesNotThrow(() -> Utils.swallowExceptionFunction(TEST_FUNCTION, Boolean.FALSE));

    assertDoesNotThrow(() -> func.apply("true"));
    final var result = assertDoesNotThrow(() -> func.apply("no boolean"));
    assertFalse(result, "Wrong value of result");
  }

  @Test
  void swallowExceptionFunctionPrm1() {
    final var func = assertDoesNotThrow(() -> Utils.swallowExceptionFunction(TEST_FUNCTION));

    assertDoesNotThrow(() -> func.apply("true"));
    final var result = assertDoesNotThrow(() -> func.apply("no boolean"));
    assertNull(result, "result is not null");
  }

  @Test
  void readEntityFromResponse() {
    var result =
        assertDoesNotThrow(
            () ->
                Utils.readEntityFromResponse(
                    Response.ok().entity(new ErrorInformation("error")).build(),
                    ErrorInformation.class),
            "readEntityFromResponse threw unexpected exception");
    assertNotNull(result, "result is null");

    result =
        assertDoesNotThrow(
            () ->
                Utils.readEntityFromResponse(
                    Response.ok().entity("error").build(), ErrorInformation.class),
            "readEntityFromResponse threw unexpected exception");
    assertNull(result, "result is not null");
  }

  @AfterAll
  static void tearDown() {
    TestUtils.overwriteLoglevel(JAXRSUtils.class.getCanonicalName(), jaxrsUtilsLogLevel.get());
  }

  @Test
  void buildUri() {
    final String hostname = "superhost";

    final var tstResult = assertDoesNotThrow(() -> Utils.buildUri(hostname));
    assertEquals("http", tstResult.getScheme());
    assertEquals(hostname, tstResult.getHost());
    assertTrue(Objects.isNull(tstResult.getPath()) || tstResult.getPath().isEmpty());
    assertEquals(-1, tstResult.getPort());
  }

  @Test
  void buildUriWithPath() {
    final String hostname = "superhost";
    final String basePath = "services";
    final String addPath = "/myservice";

    final var tstResult =
        assertDoesNotThrow(() -> Utils.buildUri(hostname, basePath, addPath, null));
    assertEquals("http", tstResult.getScheme());
    assertEquals(hostname, tstResult.getHost());
    assertTrue(
        tstResult.getPath().startsWith("/" + basePath),
        String.format("%s does not start with /%s", tstResult.getPath(), basePath));
    assertTrue(tstResult.getPath().contains(addPath));
    assertFalse(tstResult.getPath().contains("//"));
  }

  @Test
  void buildUriWithPortAndScheme() {
    final String scheme = "https";
    final String hostname = "superhost";
    final Integer port = 8080;
    final String basePath = "rest";

    final var tstResult =
        assertDoesNotThrow(() -> Utils.buildUri(scheme, hostname, port, basePath));
    assertEquals(scheme, tstResult.getScheme());
    assertEquals(port, tstResult.getPort());
    assertEquals(hostname, tstResult.getHost());
    assertTrue(
        tstResult.getPath().startsWith("/" + basePath),
        String.format("%s does not start with /%s", tstResult.getPath(), basePath));
  }

  @Test
  void doesEndWith() {
    final String testString = "teststring";
    final String testSuffix = ":12aB/&";

    final var tstResult = assertDoesNotThrow(() -> Utils.ensureEndsWith(testString, testSuffix));
    assertNotNull(tstResult);
    assertTrue(tstResult.startsWith(testString));
    assertTrue(tstResult.endsWith(testSuffix));

    final var tstResult2 = assertDoesNotThrow(() -> Utils.ensureEndsWith(tstResult, testSuffix));
    assertTrue(tstResult.startsWith(testString));
    assertTrue(tstResult.endsWith(testSuffix));
    assertEquals(tstResult, tstResult2);
  }

  @Test
  void doesStartWith() {
    final String testString = "teststring";
    final String testPrefix = ":/98xY$=";

    final var tstResult = assertDoesNotThrow(() -> Utils.ensureStartsWith(testString, testPrefix));
    assertNotNull(tstResult);
    assertTrue(tstResult.startsWith(testPrefix));
    assertTrue(tstResult.endsWith(testString));

    final var tstResult2 = assertDoesNotThrow(() -> Utils.ensureStartsWith(tstResult, testPrefix));
    assertTrue(tstResult.startsWith(testPrefix));
    assertTrue(tstResult.endsWith(testString));
    assertEquals(tstResult, tstResult2);
  }

  @Test
  void toStringListElementsTest() {
    final var testdata = new ArrayList<Patient>();
    testdata.add(
        new PatientImpl()
            .name(PersonName.fromString("Max Murks"))
            .kvnr("X124912047")
            .birthDate(LocalDate.now()));
    testdata.add(new PatientImpl().name(PersonName.fromString("Sybille Tille")));

    final var result =
        assertDoesNotThrow(() -> Utils.toStringListElements(testdata, Object::toString));

    assertNotNull(result);
    assertEquals(testdata.size(), result.size());
    assertTrue(
        result.stream()
            .allMatch(str -> testdata.stream().anyMatch(td -> str.equals(td.toString()))));
  }

  @Test
  void toStringListElementsNullTest() {
    final var result = assertDoesNotThrow(() -> Utils.toStringListElements(null, Object::toString));

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
