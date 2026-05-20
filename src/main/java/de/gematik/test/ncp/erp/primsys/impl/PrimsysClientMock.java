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

import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.ActorType;
import de.gematik.test.erezept.primsys.data.actors.DoctorDto;
import de.gematik.test.erezept.primsys.data.actors.DoctorNumber;
import de.gematik.test.erezept.primsys.data.actors.PharmacyDto;
import de.gematik.test.ncp.erp.primsys.PrimsysRestService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrimsysClientMock implements PrimsysRestService {

  private static final String DEFAULT_OFFICE_NAME = "Praxis Münchhausen-BohlenTEST-ONLY";
  @Getter private static final DoctorDto doctor = new DoctorDto();
  private static final PharmacyDto pharmacy = new PharmacyDto();

  private final List<PrescriptionDto> issuedPrescriptions;

  @Getter @Setter private DoctorClientMock doctorClientMock;

  static {
    // ActorDto data
    doctor.setId(UUID.randomUUID().toString());
    doctor.setName("Dr. Mock");
    doctor.setType(ActorType.DOCTOR);
    doctor.setOfficeName(DEFAULT_OFFICE_NAME);
    doctor.setSmcb("MOCKSMCB");
    doctor.setTid("MOCKTID");
    // DoctorDto-specific data
    doctor.setHba("MOCKHBA");
    doctor.setAnr(DoctorNumber.from("LANR", "123456"));
    doctor.setQualificationType("mock");

    pharmacy.setId(UUID.randomUUID().toString());
    pharmacy.setName("Mock-Apotheke");
    pharmacy.setType(ActorType.PHARMACY);
  }

  public PrimsysClientMock() {
    issuedPrescriptions = new ArrayList<>();
  }

  @Override
  public List<ActorDto> getActors(final ActorType role, final String name) {
    ActorDto response = doctor;
    if (ActorType.PHARMACY.equals(role)) {
      response = pharmacy;
    }
    if (name != null) {
      response.setName(name);
    }

    return List.of(response);
  }

  @Override
  public PrescriptionDto issuePrescription(
      final String doctorId, final Boolean direct, final PrescribeRequestDto prescribeRequestDto) {
    final var prescription =
        PrescriptionDto.builder()
            .patient(prescribeRequestDto.getPatient())
            .medication(prescribeRequestDto.getMedication())
            .medicationRequest(prescribeRequestDto.getMedicationRequest())
            .prescriptionId(UUID.randomUUID().toString())
            .taskId(UUID.randomUUID().toString())
            .accessCode("abc")
            .practitioner(doctor)
            .build();

    issuedPrescriptions.add(prescription);
    return prescription;
  }

  @Override
  public void abortPrescriptionAsDoc(final String doctorId, final String taskId, final String ac) {
    issuedPrescriptions.removeIf(prescription -> taskId.equals(prescription.getTaskId()));
  }

  @Override
  public List<PrescriptionDto> getPrescribed(final String kvnr) {
    final List<PrescriptionDto> allPrescriptions = new ArrayList<>(issuedPrescriptions);
    if (doctorClientMock != null) {
      allPrescriptions.addAll(doctorClientMock.getPrescriptions());
    }
    return allPrescriptions;
  }
}
