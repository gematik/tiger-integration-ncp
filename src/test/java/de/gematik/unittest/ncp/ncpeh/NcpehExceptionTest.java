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

package de.gematik.unittest.ncp.ncpeh;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.ncpeh.api.response.ErrorInformation;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.utils.TestUtils;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class NcpehExceptionTest {

  private static final AtomicReference<Level> jaxrsUtilsLogLevel = new AtomicReference<>();

  @BeforeAll
  static void before() {
    jaxrsUtilsLogLevel.lazySet(
        TestUtils.overwriteLoglevel(JAXRSUtils.class.getCanonicalName(), Level.OFF));
  }

  @Test
  void testConstructor() {
    assertDoesNotThrow(
        () -> new NcpehException("exception message"), "constructor threw unexpected exception");

    assertDoesNotThrow(
        () -> new NcpehException("exception message", new Exception()),
        "constructor threw unexpected exception");

    assertDoesNotThrow(
        () -> new NcpehException("exception message", Response.ok().build()),
        "constructor threw unexpected exception");
  }

  @Test
  void getMessage() {
    ErrorInformation errInf = new ErrorInformation("error information message");

    var tstObj =
        assertDoesNotThrow(
            () -> new NcpehException("exception message", Response.ok().entity(errInf).build()),
            "constructor threw unexpected exception");

    assertEquals(
        errInf.errorMessage(),
        tstObj.getAdditionalErrorInfo(),
        "Additional error information did not have the expected value");
    assertTrue(
        tstObj.getMessage().contains(errInf.errorMessage()),
        "additional error info was not part of the exception message");

    tstObj =
        assertDoesNotThrow(
            () ->
                new NcpehException("exception message", Response.ok().entity("some data").build()),
            "constructor threw unexpected exception");

    assertNull(tstObj.getAdditionalErrorInfo(), "additional error information was not null");
  }

  @AfterAll
  static void tearDown() {
    TestUtils.overwriteLoglevel(JAXRSUtils.class.getCanonicalName(), jaxrsUtilsLogLevel.get());
  }
}
