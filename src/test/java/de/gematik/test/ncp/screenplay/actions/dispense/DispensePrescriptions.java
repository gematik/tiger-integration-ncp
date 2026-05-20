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

package de.gematik.test.ncp.screenplay.actions.dispense;

import de.gematik.ncpeh.api.common.PrescriptionDispenseData;
import de.gematik.test.ncp.data.record.PatientRecord;
import de.gematik.test.ncp.erezept.impl.ErpFdvMockImpl;
import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.ncpeh.client.dataobject.ProvideAndRegisterDocumentSetResponseDTO;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.UseErpFdv;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import de.gematik.test.ncp.util.PrescriptionUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import org.hl7.v3.ClinicalDocument;

/** Action to dispense prescriptions and send dispensation information to the NCPeH. */
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class DispensePrescriptions implements Performable {

  private final Map<String, ClinicalDocument> prescriptionsByDocumentId;

  @Step("{0} dispenses prescriptions and sends dispensation information to the NCPeH")
  @Override
  public <T extends Actor> void performAs(final T actor) {
    var patientAccessData = actor.usingAbilityTo(ProvidePatientAccessData.class);
    var practitionerData = actor.usingAbilityTo(ProvidePractitionerData.class);
    var ncpeh = actor.usingAbilityTo(UseNcpeh.class);

    // TODO: implement mechanism to request substitution
    List<PrescriptionDispenseData> dispensations =
        prescriptionsByDocumentId.keySet().stream()
            .map(uid -> new PrescriptionDispenseData(uid, false))
            .toList();

    ProvideAndRegisterDocumentSetResponseDTO response =
        ncpeh.provideAndRegisterDocumentSet(
            patientAccessData,
            practitionerData.profileName(),
            practitionerData.country(),
            dispensations,
            null,
            null,
            null,
            ncpeh.getNcpehMockControlRequestHeaders().get(UCHeaders.UC12));

    var patientRecord = actor.asksFor(CurrentPatient.patientRecord());
    patientRecord.setDispensedPrescriptionUids(new HashSet<>(prescriptionsByDocumentId.keySet()));
    patientRecord.setDispensePrescriptionsResponse(response);
    updateFdvMock(actor);
  }

  /**
   * Factory method to create a DispensePrescriptions action for all available prescriptions of a
   * patient.
   */
  public static DispensePrescriptions forAllAvailable(final PatientRecord patientRecord) {
    return new DispensePrescriptions(patientRecord.getPrescriptionsByDocumentId());
  }

  private <T extends Actor> void updateFdvMock(final T actor) {
    var patient = actor.asksFor(CurrentPatient.asActor());
    var fdv = patient.usingAbilityTo(UseErpFdv.class).getClient();
    if (fdv instanceof ErpFdvMockImpl fdvMock) {
      Set<String> dispensedPrescriptionIds =
          prescriptionsByDocumentId.keySet().stream()
              .map(PrescriptionUtils::extractPrescriptionIdFromDocumentUid)
              .collect(Collectors.toSet());

      log.info(
          "updating Erp FdV mock with dispensed prescription ids: {}", dispensedPrescriptionIds);
      fdvMock.setDispensedPrescriptionIds(dispensedPrescriptionIds);
    }
  }
}
