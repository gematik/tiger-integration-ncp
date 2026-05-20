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
import de.gematik.test.erezept.primsys.data.actors.DoctorDto;
import de.gematik.test.ncp.erp.primsys.PrimsysDoctorService;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DoctorClientMock implements PrimsysDoctorService {

  private static final DoctorDto doc = PrimsysClientMock.getDoctor();

  @Getter private final List<PrescriptionDto> prescriptions = new ArrayList<>();

  private int nextId = 76;

  @Override
  public PrescriptionDto issuePrescription(final PrescribeRequestDto prescribeRequestDto) {
    return issuePrescription(false, prescribeRequestDto);
  }

  @Override
  public PrescriptionDto issuePrescription(
      final Boolean direct, final PrescribeRequestDto prescribeRequestDto) {

    final var prescription =
        PrescriptionDto.builder()
            .patient(prescribeRequestDto.getPatient())
            .medication(prescribeRequestDto.getMedication())
            .medicationRequest(prescribeRequestDto.getMedicationRequest())
            .prescriptionId("160.000.000.000.123." + nextId++)
            .taskId(UUID.randomUUID().toString())
            .accessCode("abc")
            .practitioner(doc)
            .authoredOn(Date.from(Instant.now()))
            .build();

    prescriptions.add(prescription);
    return prescription;
  }
}
