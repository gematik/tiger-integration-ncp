/*
 * Copyright 2026 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.erezept;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentMatchers;

class ErpFdvServiceTest {

  private ErpFdvService fdvMock;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() {
    fdvMock = mock();
    when(fdvMock.getPrescriptions(ArgumentMatchers.nullable(Collection.class)))
        .thenCallRealMethod();
  }

  @ParameterizedTest
  @EnumSource(StatusEnum.class)
  void shouldReturnPrescriptionsFilteredByStatus(final StatusEnum expectedStatus) {
    // Arrange
    List<Prescription> prescriptions =
        Arrays.stream(StatusEnum.values())
            .map(
                status ->
                    (Prescription)
                        Prescription.builder()
                            .prescriptionId(status.toString())
                            .status(status)
                            .build())
            .toList();
    when(fdvMock.getPrescriptions()).thenReturn(Optional.of(prescriptions));

    // Act
    Optional<List<Prescription>> result = fdvMock.getPrescriptions(List.of(expectedStatus));

    // Assert
    assertNotNull(result);
    assertTrue(result.isPresent());
    var filteredPrescriptions = result.get();
    assertEquals(1, filteredPrescriptions.size());
    assertEquals(expectedStatus, filteredPrescriptions.getFirst().getStatus());
    assertEquals(expectedStatus.toString(), filteredPrescriptions.getFirst().getPrescriptionId());
  }

  @Test
  void shouldReturnEmptyListWhenNoPrescriptionsMatchStatus() {
    // Arrange
    List<Prescription> prescriptions =
        List.of(
            Prescription.builder()
                .prescriptionId(StatusEnum.CANCELLED.toString())
                .status(StatusEnum.CANCELLED)
                .build());
    when(fdvMock.getPrescriptions()).thenReturn(Optional.of(prescriptions));

    // Act
    Optional<List<Prescription>> result = fdvMock.getPrescriptions(List.of(StatusEnum.READY));

    // Assert
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertTrue(result.get().isEmpty());
  }

  @Test
  void shouldReturnUnionOfAppliedFilters() {
    // Arrange
    var statusValues = List.of(StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED);
    List<Prescription> prescriptions =
        statusValues.stream()
            .map(
                status ->
                    (Prescription)
                        Prescription.builder()
                            .prescriptionId(status.toString())
                            .status(status)
                            .build())
            .toList();
    when(fdvMock.getPrescriptions()).thenReturn(Optional.of(prescriptions));

    // Act
    Optional<List<Prescription>> result = fdvMock.getPrescriptions(statusValues);

    // Assert
    assertNotNull(result);
    assertTrue(result.isPresent());
    var filteredPrescriptions = result.get();
    assertEquals(2, filteredPrescriptions.size());
    assertTrue(
        filteredPrescriptions.stream()
            .anyMatch(
                p ->
                    p.getStatus() == StatusEnum.IN_PROGRESS
                        && p.getPrescriptionId().equals(StatusEnum.IN_PROGRESS.toString())));
    assertTrue(
        filteredPrescriptions.stream()
            .anyMatch(
                p ->
                    p.getStatus() == StatusEnum.COMPLETED
                        && p.getPrescriptionId().equals(StatusEnum.COMPLETED.toString())));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldReturnAllPrescriptionsWhenFilterIsNullOrEmpty(
      final Collection<StatusEnum> nullOrEmpty) {
    // Arrange
    var statusValues =
        List.of(
            StatusEnum.READY, StatusEnum.IN_PROGRESS, StatusEnum.COMPLETED, StatusEnum.CANCELLED);
    List<Prescription> prescriptions =
        statusValues.stream()
            .map(
                status ->
                    (Prescription)
                        Prescription.builder()
                            .prescriptionId(status.toString())
                            .status(status)
                            .build())
            .toList();
    when(fdvMock.getPrescriptions()).thenReturn(Optional.of(prescriptions));

    // Act
    Optional<List<Prescription>> result = fdvMock.getPrescriptions(nullOrEmpty);

    // Assert
    assertNotNull(result);
    assertTrue(result.isPresent());
    var filteredPrescriptions = result.get();
    assertEquals(statusValues.size(), filteredPrescriptions.size());
    statusValues.forEach(
        status ->
            assertTrue(
                filteredPrescriptions.stream()
                    .anyMatch(
                        p ->
                            p.getStatus() == status
                                && p.getPrescriptionId().equals(status.toString()))));
  }
}
