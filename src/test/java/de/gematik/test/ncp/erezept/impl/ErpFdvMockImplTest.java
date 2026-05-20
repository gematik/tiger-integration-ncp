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

package de.gematik.test.ncp.erezept.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import de.gematik.test.erezept.primsys.data.PatientDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.PznMedicationDto;
import de.gematik.test.erezept.primsys.data.actors.DoctorDto;
import de.gematik.test.erezept.primsys.data.actors.DoctorNumber;
import de.gematik.test.ncp.erp.primsys.PrimsysRestService;
import de.gematik.test.ncp.gen.erp.fdv.model.ConsentCategory;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ErpFdvMockImplTest {

  private AutoCloseable openMocks;

  private ErpFdvMockImpl erpFdvMock;
  @Mock private PrimsysRestService primsysClient;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    erpFdvMock = ErpFdvMockImpl.builder().primsysClient(primsysClient).build();
  }

  @AfterEach
  public void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void shouldNotThrowOnStart() {
    assertDoesNotThrow(() -> erpFdvMock.start());
  }

  @Test
  void shouldNotThrowOnStop() {
    assertDoesNotThrow(() -> erpFdvMock.stop());
  }

  @Test
  void shouldNotThrowOnStartThenStop() {
    assertDoesNotThrow(
        () -> {
          erpFdvMock.start();
          erpFdvMock.stop();
        });
  }

  @Test
  void shouldNotKnowKvnrInitially() {
    assertNull(erpFdvMock.getKvnr());
  }

  @Test
  void shouldReturnAccessTokenOnLogin() {
    final var result = erpFdvMock.login("X123456789");
    assertTrue(result.isPresent());
    assertFalse(result.get().getAccessToken().isEmpty());
  }

  @Test
  void shouldStoreKvnrOnLogin() {
    final var kvnr = "X123456789";
    erpFdvMock.login(kvnr);
    assertEquals(kvnr, erpFdvMock.getKvnr());
  }

  @Test
  void shouldNotHaveEuDispensationConsentInitially() {
    assertTrue(erpFdvMock.getEuDispensationConsent().isEmpty());
  }

  @Test
  void shouldReturnConsentOnCreateEuDispensationConsent() {
    final var category = ConsentCategory.EUDISPCONS;
    final var consent = erpFdvMock.createEuDispensationConsent();
    assertTrue(consent.isPresent());
    assertEquals(category, consent.get().getCategory());
  }

  @Test
  void shouldStoreConsent() {
    final var category = ConsentCategory.EUDISPCONS;
    erpFdvMock.createEuDispensationConsent();
    final var result = erpFdvMock.getEuDispensationConsent();
    assertTrue(result.isPresent());
    assertEquals(category, result.get().getCategory());
  }

  @Test
  void shouldReturnFalseOnRevokeEuDispensationConsentWithoutExistingConsent() {
    assertFalse(erpFdvMock.revokeEuDispensationConsent());
  }

  @Test
  void shouldReturnTrueOnRevokeEuDispensationConsentWithExistingConsent() {
    erpFdvMock.createEuDispensationConsent();
    assertTrue(erpFdvMock.revokeEuDispensationConsent());
  }

  @Test
  void shouldRemoveConsentOnRevoke() {
    erpFdvMock.createEuDispensationConsent();
    erpFdvMock.revokeEuDispensationConsent();
    assertTrue(erpFdvMock.getEuDispensationConsent().isEmpty());
  }

  @Test
  void shouldNotHaveEuAccessAuthorizationInitially() {
    assertTrue(erpFdvMock.getEuAccessPermission().isEmpty());
  }

  @Test
  void shouldReturnEuAuthorizationWithNonNullAccessCodeOnGrantEuAccessPermission() {
    // Arrange
    final var country = "NL";
    // Act
    final var result = erpFdvMock.grantEuAccessPermission(country, null);
    // Assert
    assertTrue(result.isPresent());
    assertEquals(country, result.get().getCountry());
    assertNotNull(result.get().getAccessCode());
  }

  @Test
  void shouldReturnEuAuthorizationWithProvidedAccessCodeOnGrantEuAccessPermission() {
    // Arrange
    final var country = "NL";
    final var accessCode = "code123";
    // Act
    final var result = erpFdvMock.grantEuAccessPermission(country, accessCode);
    // Assert
    assertTrue(result.isPresent());
    assertEquals(country, result.get().getCountry());
    assertEquals(accessCode, result.get().getAccessCode());
  }

  @Test
  void shouldStoreEuAuthorization() {
    // Arrange
    final var country = "NL";
    final var accessCode = "code123";
    // Act
    erpFdvMock.grantEuAccessPermission(country, accessCode);
    final var result = erpFdvMock.getEuAccessPermission();
    // Assert
    assertTrue(result.isPresent());
    assertEquals(country, result.get().getCountry());
    assertEquals(accessCode, result.get().getAccessCode());
  }

  @ParameterizedTest
  @NullAndEmptySource
  void grantEuAccessPermission_shouldReturnEmptyWhenCountryIsNullOrEmpty(final String country) {
    final var result = erpFdvMock.grantEuAccessPermission(country, "code123");
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnFalseOnRevokeEuAccessPermissionWhenAuthorizationNotGranted() {
    assertFalse(erpFdvMock.revokeEuAccessPermission());
  }

  @Test
  void shouldReturnTrueOnRevokeEuAccessPermissionWhenAuthorizationGranted() {
    erpFdvMock.grantEuAccessPermission("NL", "code123");
    assertTrue(erpFdvMock.revokeEuAccessPermission());
  }

  @Test
  void shouldRemoveEuAuthorizationOnRevokeEuAccessPermission() {
    erpFdvMock.grantEuAccessPermission("NL", "code123");
    assertTrue(erpFdvMock.revokeEuAccessPermission());
    assertTrue(erpFdvMock.getEuAccessPermission().isEmpty());
  }

  @Test
  void shouldReturnEmptyListOfPrescriptionsInitially() {
    // Arrange + Act
    erpFdvMock.login("X123456789");
    final var prescriptions = erpFdvMock.getPrescriptions().orElseThrow();
    assertTrue(prescriptions.isEmpty());
  }

  @Test
  void getPrescriptionsShouldReturnListOfAvailablePrescriptionsAfterLogin() {
    // Arrange
    final var kvnr = "X123456789";
    final var prescriptionId = "id1";
    final var pzn = "4711";
    final var doc = new DoctorDto();
    doc.setId("doc1");
    doc.setName("Dr. Mock");
    doc.setAnr(DoctorNumber.from("LANR", "anr999"));
    doc.setTid("tid456");
    doc.setQualificationType("foo");
    final var builder =
        PrescriptionDto.builder()
            .patient(PatientDto.withKvnr(kvnr).withFirstName("John").withLastName("Doe").build())
            .medication(PznMedicationDto.medicine(pzn, "Testmedikation").asPrescribed())
            .practitioner(doc)
            .accessCode("secret")
            .authoredOn(Date.from(Instant.now()));
    final var pre1 = builder.prescriptionId(prescriptionId).build();
    final var pre2 = builder.prescriptionId("id2").build();

    when(primsysClient.getPrescribed(kvnr)).thenReturn(List.of(pre1, pre2));
    // Act
    erpFdvMock.login(kvnr);
    final var prescriptions = erpFdvMock.getPrescriptions().orElseThrow();
    // Assert
    assertEquals(2, prescriptions.size());
    assertTrue(prescriptions.stream().anyMatch(p -> prescriptionId.equals(p.getPrescriptionId())));
    assertTrue(prescriptions.stream().anyMatch(p -> "id2".equals(p.getPrescriptionId())));
  }

  @Test
  void getPrescriptionShouldReturnEmptyOptionalInitially() {
    erpFdvMock.login("X123456789");
    assertTrue(erpFdvMock.getPrescription("id1").isEmpty());
  }

  @Test
  void getPrescriptionShouldReturnExistingPrescriptionForKvnr() {
    // Arrange
    final var kvnr = "X123456789";
    final var prescriptionId = "id1";
    final var pzn = "4711";
    final var doc = new DoctorDto();
    doc.setId("doc1");
    doc.setName("Dr. Mock");
    doc.setAnr(DoctorNumber.from("LANR", "anr999"));
    doc.setTid("tid456");
    doc.setQualificationType("foo");
    final var prescriptionDto =
        PrescriptionDto.builder()
            .prescriptionId(prescriptionId)
            .patient(PatientDto.withKvnr(kvnr).withFirstName("John").withLastName("Doe").build())
            .medication(PznMedicationDto.medicine(pzn, "Testmedikation").asPrescribed())
            .practitioner(doc)
            .accessCode("secret")
            .authoredOn(Date.from(Instant.now()))
            .build();

    when(primsysClient.getPrescribed(kvnr)).thenReturn(List.of(prescriptionDto));

    // Act
    erpFdvMock.login(kvnr);
    final var prescription = erpFdvMock.getPrescription(prescriptionId);
    // Assert
    assertTrue(prescription.isPresent());
    assertEquals(prescriptionId, prescription.get().getPrescriptionId());
    assertEquals(pzn, prescription.get().getMedication().getCode());
    assertEquals("secret", prescription.get().getAccessCode());
  }

  @Test
  void setEuRedeemableShouldReturnEmptyOptionalInitially() {
    erpFdvMock.login("X123456789");
    assertTrue(erpFdvMock.setEuRedeemable("id1", true).isEmpty());
  }

  @Test
  void shouldStoreEuRedeemableSetting() {
    // Arrange
    final var kvnr = "X123456789";
    final var prescriptionId = "id1";
    final var pzn = "4711";
    final var doc = new DoctorDto();
    doc.setId("doc1");
    doc.setName("Dr. Mock");
    doc.setAnr(DoctorNumber.from("LANR", "anr999"));
    doc.setTid("tid456");
    doc.setQualificationType("foo");
    final var prescriptionDto =
        PrescriptionDto.builder()
            .prescriptionId(prescriptionId)
            .patient(PatientDto.withKvnr(kvnr).withFirstName("John").withLastName("Doe").build())
            .medication(PznMedicationDto.medicine(pzn, "Testmedikation").asPrescribed())
            .practitioner(doc)
            .accessCode("secret")
            .authoredOn(Date.from(Instant.now()))
            .build();

    when(primsysClient.getPrescribed(kvnr)).thenReturn(List.of(prescriptionDto));
    // Act
    erpFdvMock.login("X123456789");
    final var prescriptionBefore = erpFdvMock.getPrescription(prescriptionId);
    final var updatedPrescription = erpFdvMock.setEuRedeemable(prescriptionId, true);
    final var prescriptionAfter = erpFdvMock.getPrescription(prescriptionId);
    // Assert
    assertTrue(prescriptionBefore.isPresent());
    assertFalse(prescriptionBefore.get().getEuRedeemableByPatient());
    assertTrue(updatedPrescription.isPresent());
    assertTrue(updatedPrescription.get().getEuRedeemableByPatient());
    assertTrue(prescriptionAfter.isPresent());
    assertEquals(prescriptionAfter, updatedPrescription);
  }

  @Test
  void shouldReturnEmptyListOfDispensedPrescriptionIdsInitially() {
    var result = erpFdvMock.getMedicationDispenseInformation();
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnListOfDispensedPrescriptionIdsAfterSetterCalled() {
    // Arrange
    var ids = Set.of("dispensed1", "dispensed2");
    erpFdvMock.setDispensedPrescriptionIds(ids);
    // Act
    var result = erpFdvMock.getMedicationDispenseInformation();
    // Assert
    assertNotNull(result);
    assertEquals(ids.size(), result.size());
    assertTrue(
        ids.stream()
            .allMatch(id -> result.stream().anyMatch(info -> id.equals(info.getPrescriptionId()))));
  }
}
