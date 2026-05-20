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

package de.gematik.test.ncp.erp.primsys;

import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.ActorType;
import java.util.List;

public interface PrimsysRestService {

  /**
   * Get actors, filter by type and name
   *
   * @param role the type of actor to filter by (e.g. Doctors, Pharmacies, etc.)
   * @param name the name to filter by (e.g. Adelheid Ulmenwald)
   * @return a list of actors of the specified type and with the specified name
   */
  List<ActorDto> getActors(ActorType role, String name);

  /**
   * Issues a prescription using the provided {@link PrescribeRequestDto}
   *
   * @param doctorId ID of the doctor issuing the prescription
   * @param direct direct prescription flag
   * @param prescribeRequestDto the prescription request
   * @return the issued prescription
   */
  PrescriptionDto issuePrescription(
      String doctorId, Boolean direct, PrescribeRequestDto prescribeRequestDto);

  /**
   * Abort a prescription as a doctor
   *
   * @param doctorId ID of the doctor who issued the prescription
   * @param taskId ID of the task associated with the prescription
   * @param ac access code for the prescription
   */
  void abortPrescriptionAsDoc(String doctorId, String taskId, String ac);

  /**
   * Fetch all open prescriptions for a given kvnr
   *
   * @param kvnr the kvnr of the patient
   * @return a list of open prescriptions
   */
  List<PrescriptionDto> getPrescribed(String kvnr);
}
