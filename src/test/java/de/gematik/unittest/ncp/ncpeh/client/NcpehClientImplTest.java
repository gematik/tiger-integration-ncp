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

package de.gematik.unittest.ncp.ncpeh.client;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.response.ErrorInformation;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.ncpeh.NcpehProvider;
import de.gematik.test.ncp.ncpeh.client.NcpehClientImpl;
import de.gematik.unittest.testutil.NcpehClientImplForUnittest;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class NcpehClientImplTest {

  public static final String KVNR = "X114428538";

  public static final Patient PATIENT =
      new PatientImpl(PersonName.fromString("Petra Rot"), KVNR, LocalDate.of(1935, 6, 22));

  public static final String COUNTRY = EuCountryCode.DENMARK.name();

  @Test
  void identifyPatient() {
    var tstObj = new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig());

    var result =
        assertDoesNotThrow(
            () -> tstObj.identifyPatient(PATIENT, "default", COUNTRY),
            "method identifyPatient threw exception");
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
    //    assertEquals(KVNR, result.kvnr(), "KVNR was not the same");
    //    assertEquals(PATIENT.birthDate(), result.birthDate(), "Birth date was not the same");
    //    assertEquals(PATIENT.name(), result.name(), "Name was not the same");
  }

  @Test
  void identifyPatientThrows() {
    var tstObj = new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig());
    var errorInfo = new ErrorInformation("a spectacular error occurred");

    Mockito.when(tstObj.clientProxy().identifyPatient(Mockito.any()))
        .thenReturn(Response.status(Status.BAD_REQUEST).entity(errorInfo).build());

    var exception =
        assertThrows(
            NcpehException.class,
            () -> tstObj.identifyPatient(PATIENT, "default", COUNTRY),
            "method identifyPatient did not throw exception");

    assertTrue(exception.getMessage().contains(errorInfo.errorMessage()));
  }

  @Test
  void findPatientSummaryTest() {
    var tstObj = new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig());

    var result =
        assertDoesNotThrow(
            () -> tstObj.findPatientSummary(PATIENT, "default", COUNTRY),
            "method findPatientSummaryData threw exception");

    assertNotNull(result, "method findPatientSummaryData returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
  }

  @Test
  void findPatientSummaryThrowsTest() {
    var tstObj = new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig());
    Mockito.when(tstObj.clientProxy().findDocuments(Mockito.any()))
        .thenReturn(
            Response.status(Status.BAD_REQUEST).entity(new ErrorInformation("error")).build());

    assertThrows(
        NcpehException.class,
        () -> tstObj.findPatientSummary(PATIENT, "default", COUNTRY),
        "method findPatientSummaryData did not throw exception");
  }

  @Test
  void retrievePatientSummaryTest() {
    var tstObj = new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig());

    var tstData = tstObj.findPatientSummary(PATIENT, "default", COUNTRY);
    var result =
        assertDoesNotThrow(
            () ->
                tstObj.retrievePatientSummary(
                    PATIENT, "default", COUNTRY, tstData.ncpehFdResponseContent()),
            "method retrievePatientSummary threw exception");

    assertNotNull(result, "method retrievePatientSummary returned null");
    assertEquals(HttpStatus.OK, result.ncpehResponseStatus());
  }

  @Test
  void retrievePatientSummaryThrowsTest() {
    var tstObj = new NcpehClientImplForUnittest(NcpehProvider.getNcpehProvider().getNcpehConfig());
    Mockito.when(tstObj.clientProxy().retrieveDocument(Mockito.any()))
        .thenReturn(
            Response.status(Status.BAD_REQUEST).entity(new ErrorInformation("error")).build());

    var tstData = tstObj.findPatientSummary(PATIENT, "default", COUNTRY);
    var ncpehResponseContent = tstData.ncpehFdResponseContent();
    assertThrows(
        NcpehException.class,
        () -> tstObj.retrievePatientSummary(PATIENT, "default", COUNTRY, ncpehResponseContent),
        "method retrievePatientSummary did not throw exception");
  }

  @Test
  void clientProxy() {
    var tstObj = new NcpehClientImpl(NcpehProvider.getNcpehProvider().getNcpehConfig());

    assertDoesNotThrow(tstObj::clientProxy, "Method clientProxy threw exception");
  }
}
