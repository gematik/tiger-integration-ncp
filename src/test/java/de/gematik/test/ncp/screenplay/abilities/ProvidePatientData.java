/*
 * Copyright (c) 2024. gematik GmbH
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
 */

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PatientInfo;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryDO;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.serenitybdd.screenplay.Ability;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.hl7.v3.ClinicalDocument;
import org.openqa.selenium.Pdf;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ProvidePatientData extends PatientImpl implements PatientInfo, Ability {

  private String epkaAktenkontoId;
  private byte[] epka;
  private byte[] signedEpka;

  private Boolean hasAktenkonto;

  private AdhocQueryResponse psaMetadata;
  private RetrievePatientSummaryDO patientSummaryDO;
  private ClinicalDocument patientSummaryLvl3;
  private Pdf patientSummaryLvl1;

  private IdentifyPatientDO identifyPatientDataResponse;
  private Patient identifyPatientData;

  public ProvidePatientData(final PersonName name, final String kvnr, final LocalDate birthDate) {
    super(name, kvnr, birthDate);
  }

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
