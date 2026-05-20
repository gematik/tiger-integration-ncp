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

import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.ActorType;
import de.gematik.test.erezept.primsys.data.actors.DoctorDto;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PrimsysClientMockTest {

  private AutoCloseable openMocks;

  @Mock private DoctorClientMock doctorClientMock;

  private PrimsysClientMock primsysMockClient;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);

    primsysMockClient = new PrimsysClientMock();
    primsysMockClient.setDoctorClientMock(doctorClientMock);
  }

  @AfterEach
  void teardown() throws Exception {
    openMocks.close();
  }

  @Test
  void getActors_returnsActorWithRoleAndName_whenRoleAndNameAreNull() {
    // Arrange
    // Act
    final List<ActorDto> result = primsysMockClient.getActors(null, null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertInstanceOf(ActorType.class, result.getFirst().getType());
    assertNotNull(result.getFirst().getName());
    assertNotEquals("", result.getFirst().getName());
  }

  @Test
  void getActors_returnsDoctor_whenGivenDoctorRole() {
    // Arrange
    // Act
    final var result = primsysMockClient.getActors(ActorType.DOCTOR, null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(ActorType.DOCTOR, result.getFirst().getType());
    assertNotEquals("", result.getFirst().getName());
    assertInstanceOf(DoctorDto.class, result.getFirst());
    assertNotNull(((DoctorDto) result.getFirst()).getOfficeName());
    assertNotEquals("", ((DoctorDto) result.getFirst()).getOfficeName());
  }

  @Test
  void getActors_returnsPharmacy_whenGivenPharmacyRole() {
    // Arrange
    // Act
    final var result = primsysMockClient.getActors(ActorType.PHARMACY, null);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(ActorType.PHARMACY, result.getFirst().getType());
    assertNotEquals("", result.getFirst().getName());
  }

  @Test
  void getActors_returnsDoctorWithName_whenGivenDoctorRoleAndName() {
    // Arrange
    final var role = ActorType.DOCTOR;
    final var name = "Dr. Doktor";
    // Act
    final var result = primsysMockClient.getActors(role, name);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(role, result.getFirst().getType());
    assertEquals(name, result.getFirst().getName());
  }

  @Test
  void getActors_returnsPharmacyWithName_whenGivenPharmacyRoleAndName() {
    // Arrange
    final var role = ActorType.PHARMACY;
    final var name = "Miraculix";
    // Act
    final var result = primsysMockClient.getActors(role, name);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(role, result.getFirst().getType());
    assertEquals(name, result.getFirst().getName());
  }

  @Test
  void issuePrescription_returnsPractitionerWithOfficeName() {
    // Arrange
    final var request = new PrescribeRequestDto();

    // Act
    final PrescriptionDto result = primsysMockClient.issuePrescription("doctorId", false, request);

    // Assert
    assertNotNull(result.getPractitioner());
    assertNotNull(result.getPractitioner().getOfficeName());
    assertNotEquals("", result.getPractitioner().getOfficeName());
  }
}
