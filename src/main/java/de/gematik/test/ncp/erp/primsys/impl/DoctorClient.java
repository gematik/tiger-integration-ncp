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

import de.gematik.test.erezept.primsys.PrimSysDoctor;
import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.rest.DoctorRequests;
import de.gematik.test.ncp.erp.primsys.PrimsysDoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Client for interacting with the PrimSys REST Doctor's API extending the basic Kotlin client with
 * additional functionality.
 */
@RequiredArgsConstructor
@Slf4j
public class DoctorClient implements PrimsysDoctorService {

  private final PrimSysDoctor client;

  @Override
  public PrescriptionDto issuePrescription(final PrescribeRequestDto prescribeRequestDto) {
    return issuePrescription(false, prescribeRequestDto);
  }

  @Override
  public PrescriptionDto issuePrescription(
      final Boolean direct, final PrescribeRequestDto prescribeRequestDto) {
    return client
        .performBlocking(DoctorRequests.prescribe(prescribeRequestDto, direct))
        .asExpectedPayload();
  }
}
