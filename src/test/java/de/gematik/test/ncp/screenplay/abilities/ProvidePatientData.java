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

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PatientInfo;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrieveDocumentsResponseDTO;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.serenitybdd.screenplay.Ability;
import org.hl7.v3.ClinicalDocument;

/**
 * Ability to provide the primary identification properties (name, KVNR, birthdate, and access code)
 * and all sorts of additional data related to a patient.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ProvidePatientData extends PatientImpl implements PatientInfo, Ability {

  private String epkaAktenkontoId;
  private byte[] epka;
  private byte[] signedEpka;

  private Boolean hasAktenkonto;

  private RetrieveDocumentsResponseDTO patientSummaryDO;
  private ClinicalDocument patientSummaryLvl3;
  private byte[] patientSummaryLvl1;

  private EuCountryCode currentLocationCountry;
  private List<Medication> euRedeemablePrescriptions;

  public ProvidePatientData(final PersonName name, final String kvnr, final LocalDate birthDate) {
    super(name, kvnr, birthDate);
  }

  /**
   * Creates the Ability to provide patient data from a given Patient instance.
   *
   * @param patient the patient to take the primary identification data from
   * @return Ability to provide patient data
   */
  public static ProvidePatientData fromPatient(final Patient patient) {
    return new ProvidePatientData(patient.name(), patient.kvnr(), patient.birthDate());
  }

  @Override
  public Boolean hasAktenkonto() {
    return hasAktenkonto;
  }

  @Override
  public void hasAktenkonto(final Boolean hasAktenkonto) {
    this.hasAktenkonto = hasAktenkonto;
  }
}
