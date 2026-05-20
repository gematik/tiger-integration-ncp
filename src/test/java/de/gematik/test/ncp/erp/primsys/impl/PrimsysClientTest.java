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

import static de.gematik.test.ncp.erp.primsys.impl.PrimsysClient.ACCEPTED_STATUS_CODES_ON_ABORT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.gematik.test.erezept.primsys.PrimSysClientFactory;
import de.gematik.test.erezept.primsys.PrimSysDoctor;
import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.ActorType;
import de.gematik.test.erezept.primsys.data.actors.DoctorDto;
import de.gematik.test.erezept.primsys.data.actors.PharmacyDto;
import de.gematik.test.erezept.primsys.rest.PrescribeRequest;
import de.gematik.test.erezept.primsys.rest.PrimSysResponse;
import de.gematik.test.ncp.ExternalServerConfig;
import io.restassured.builder.ResponseBuilder;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import lombok.SneakyThrows;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class PrimsysClientTest {

  private AutoCloseable openMocks;

  @Mock private PrimSysClientFactory clientFactory;
  @Mock private PrimSysDoctor doctorClient;

  private ExternalServerConfig config;
  private PrimsysClient primsysClient;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);

    config = new ExternalServerConfig();
    config.setScheme("http");
    config.setHostname("localhost");
    config.setBasePath("");

    when(clientFactory.getDoctorClient(anyString())).thenReturn(doctorClient);

    primsysClient = new PrimsysClient(clientFactory, config, 80, "apikey");
  }

  @SneakyThrows
  @AfterEach
  public void tearDown() {
    openMocks.close();
  }

  @Test
  void getActors_returnsAllActors_whenRoleAndNameAreNull() {
    // Arrange
    final var actors = List.of(new DoctorDto(), new PharmacyDto());
    when(clientFactory.getActorsInfo()).thenReturn(actors);

    // Act
    final List<ActorDto> result = primsysClient.getActors(null, null);

    // Assert
    assertEquals(actors, result);
  }

  @Test
  void getActors_returnsFilteredActors_withGivenRole() {
    // Arrange
    final var actor1 = new ActorDto("1", ActorType.DOCTOR, "doc", null, null, null);
    final var actor2 = new ActorDto("2", ActorType.PHARMACY, "pharm", null, null, null);
    final var actors = List.of(actor1, actor2);

    when(clientFactory.getActorsInfo()).thenReturn(actors);

    // Act
    final var result = primsysClient.getActors(ActorType.DOCTOR, null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(actor1, result.getFirst());
  }

  @Test
  void getActors_returnsFilteredActors_withGivenName() {
    // Arrange
    final var actor1 = new ActorDto("1", ActorType.DOCTOR, "doc", null, null, null);
    final var actor2 = new ActorDto("2", ActorType.PHARMACY, "pharm", null, null, null);
    final var actors = List.of(actor1, actor2);

    when(clientFactory.getActorsInfo()).thenReturn(actors);

    // Act
    final var result = primsysClient.getActors(null, "pharm");

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(actor2, result.getFirst());
  }

  @Test
  void getActors_returnsFilteredActors_withGivenRoleAndName() {
    // Arrange
    final var actor1 = new ActorDto("1", ActorType.DOCTOR, "doc", null, null, null);
    final var actor2 = new ActorDto("2", ActorType.PHARMACY, "pharm", null, null, null);
    final var actors = List.of(actor1, actor2);

    when(clientFactory.getActorsInfo()).thenReturn(actors);

    // Act
    final var result = primsysClient.getActors(ActorType.PHARMACY, "pharm");

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(actor2, result.getFirst());
  }

  @Test
  void getActors_returnsEmptyList_whenNoMatchFound() {
    // Arrange
    final var actor1 = new ActorDto("1", ActorType.DOCTOR, "doc", null, null, null);
    final var actor2 = new ActorDto("2", ActorType.PHARMACY, "pharm", null, null, null);
    final var actors = List.of(actor1, actor2);

    when(clientFactory.getActorsInfo()).thenReturn(actors);

    // Act
    final var result = primsysClient.getActors(ActorType.DOCTOR, "pharm");

    // Assert
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  void issuePrescription_returnsPrescriptionDto() {
    // Arrange
    final var requestDto = new PrescribeRequestDto();
    final var prescriptionDto = new PrescriptionDto();
    final var primsysResponse = mock(PrimSysResponse.class);
    when(primsysResponse.asExpectedPayload()).thenReturn(prescriptionDto);
    doReturn(primsysResponse).when(doctorClient).performBlocking(any(PrescribeRequest.class));

    // Act
    final var result = primsysClient.issuePrescription("doctorId", false, requestDto);

    // Assert
    assertEquals(prescriptionDto, result);
  }

  @SneakyThrows
  @Test
  void abortPrescriptionAsDoc_succeeds() {
    // Arrange
    final var doctorId = "doctorId";
    final var taskId = "taskId";
    final var ac = "ac";

    final var response =
        new ResponseBuilder()
            .setContentType(MediaType.APPLICATION_JSON)
            .setStatusCode(ACCEPTED_STATUS_CODES_ON_ABORT.getFirst())
            .build();

    final var reqSpec = mock(RequestSpecification.class, RETURNS_SELF);
    when(reqSpec.delete("/abort")).thenReturn(response);

    try (final var mockedSerenityRest = Mockito.mockStatic(SerenityRest.class)) {
      mockedSerenityRest.when(SerenityRest::given).thenReturn(reqSpec);

      // Act
      // Assert
      assertDoesNotThrow(() -> primsysClient.abortPrescriptionAsDoc(doctorId, taskId, ac));
    }
  }

  @SneakyThrows
  @Test
  void abortPrescriptionAsDoc_throwsOnHttpErrorCode() {
    // Arrange
    final var doctorId = "doctorId";
    final var taskId = "taskId";
    final var ac = "ac";

    final var response = new ResponseBuilder().setStatusCode(400).build();

    final var reqSpec = mock(RequestSpecification.class, RETURNS_SELF);
    when(reqSpec.delete("/abort")).thenReturn(response);

    try (final var mockedSerenityRest = Mockito.mockStatic(SerenityRest.class)) {
      mockedSerenityRest.when(SerenityRest::given).thenReturn(reqSpec);

      // Act
      // Assert
      assertThrows(
          IOException.class, () -> primsysClient.abortPrescriptionAsDoc(doctorId, taskId, ac));
    }
  }
}
