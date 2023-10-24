/*
 * Copyright 2023 gematik GmbH
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

package de.gematik.test.ncp.data;

import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryDO;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.hl7.v3.ClinicalDocument;
import org.openqa.selenium.Pdf;

/** Interface for objects holding all patient bound data, which might also be transient. */
public interface PatientInfo extends Patient {

  String getEpkaAktenkontoId();

  void setEpkaAktenkontoId(String epkaAktenkontoId);

  byte[] getEpka();

  void setEpka(byte[] epka);

  byte[] getSignedEpka();

  void setSignedEpka(byte[] signedEpka);

  Boolean hasAktenkonto();

  void hasAktenkonto(Boolean hasAktenkonto);

  AdhocQueryResponse getPsaMetadata();

  void setPsaMetadata(AdhocQueryResponse psaMetadata);

  RetrievePatientSummaryDO getPatientSummaryDO();

  void setPatientSummaryDO(RetrievePatientSummaryDO patientSummaryDO);

  ClinicalDocument getPatientSummaryLvl3();

  void setPatientSummaryLvl3(ClinicalDocument patientSummary);

  Pdf getPatientSummaryLvl1();

  void setPatientSummaryLvl1(Pdf patientSummary);

  void setAccessCode(String accessCode);
}
