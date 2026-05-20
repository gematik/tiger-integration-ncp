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

package de.gematik.test.ncp.erp.primsys.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.gematik.test.erezept.primsys.PrimSysDoctor;
import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.rest.DoctorRequests;
import de.gematik.test.erezept.primsys.rest.PrescribeRequest;
import de.gematik.test.erezept.primsys.rest.PrimSysResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class DoctorClientTest {

  @Mock private PrimSysDoctor primsysClientMock;

  private AutoCloseable openMocks;
  private DoctorClient doctorClient;
  private MockedStatic<DoctorRequests> doctorRequestsMock;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    doctorClient = new DoctorClient(primsysClientMock);
    doctorRequestsMock = Mockito.mockStatic(DoctorRequests.class);
  }

  @AfterEach
  public void tearDown() throws Exception {
    doctorRequestsMock.close();
    openMocks.close();
  }

  @Test
  void whenIssuingPrescription_shouldDelegateToPrimsysClient() {
    // Arrange
    final var prescribeRequest = new PrescribeRequestDto();
    final var restPrescribeRequest = new PrescribeRequest(prescribeRequest, false);
    final var primsysResponseMock = mock(PrimSysResponse.class);
    final var expected = new PrescriptionDto();

    when(DoctorRequests.prescribe(prescribeRequest, false)).thenReturn(restPrescribeRequest);
    doReturn(primsysResponseMock).when(primsysClientMock).performBlocking(restPrescribeRequest);
    when(primsysResponseMock.asExpectedPayload()).thenReturn(expected);

    // Act
    final var response = doctorClient.issuePrescription(prescribeRequest);

    // Assert
    assertSame(expected, response);
  }

  @Test
  void whenIssuingPrescriptionAsDirectAssignment_shouldDelegateToPrimsysClient() {
    // Arrange
    final var prescribeRequest = new PrescribeRequestDto();
    final var direct = true;
    final var restPrescribeRequest = new PrescribeRequest(prescribeRequest, false);
    final var primsysResponseMock = mock(PrimSysResponse.class);
    final var expected = new PrescriptionDto();

    when(DoctorRequests.prescribe(prescribeRequest, direct)).thenReturn(restPrescribeRequest);
    doReturn(primsysResponseMock).when(primsysClientMock).performBlocking(restPrescribeRequest);
    when(primsysResponseMock.asExpectedPayload()).thenReturn(expected);

    // Act
    final var response = doctorClient.issuePrescription(direct, prescribeRequest);

    // Assert
    assertSame(expected, response);
  }
}
