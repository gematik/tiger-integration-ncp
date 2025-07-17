/*
 * Copyright 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.ncpeh;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.ncpeh.client.dataobject.FindPatientSummaryResponseDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientResponseDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryResponseDO;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Interface describing the functions to interact with the NCPeH (Simulation). This should define
 * the single point of interaction, between the screenplay / glue code of the test suite and the
 * NCPeH components like a simulator, a mock or even a real implementation. Thus, if functionality
 * is missing here, add it! Don't go sideways!
 */
public interface NcpehService {

  /**
   * Retrieve the patientAccessData information necessary for identification purposes.<br>
   * Note: This operation will likely be subject to non-backward compatible changes in the future
   *
   * @param patientAccessData The health insurance identification number of the patientAccessData
   *     (a.k.a. KVNR)
   * @param testdataProfileName key of the testdata profile configured in the testdata.yaml to use
   * @param leiCountry name of the country, where the EU-Lei is situated
   * @param ncpehMockControlRequestHeader control header for the NCPeH-Simulation-Mock response
   * @return {@link IdentifyPatientResponseDO} the patientAccessData information necessary for
   *     identification purposes
   */
  IdentifyPatientResponseDO identifyPatient(
      PatientAccessData patientAccessData,
      String testdataProfileName,
      String leiCountry,
      String ncpehMockControlRequestHeader);

  /**
   * Find the patients ePKA and return its metadata. Note: This operation will likely be subject to
   * non-backward compatible changes in the future
   *
   * @return The return type here is not yet defined and will likely change in the future
   */
  FindPatientSummaryResponseDO findPatientSummary(
      PatientAccessData patientAccessData,
      String testdataProfileName,
      String leiCountry,
      String ncpehMockControlRequestHeader);

  /**
   * Retrieve the patient's ePKA. Note: This operation will likely be subject to non-backward
   * compatible changes in the future.
   *
   * @param patientAccessData The health insurance identification number of the patient (a.k.a.
   *     KVNR)
   * @param patient The patient object containing patient details
   * @param testdataProfileName Key of the testdata profile configured in the testdata.yaml to use
   * @param leiCountry Name of the country where the EU-Lei is situated
   * @param metadata The metadata of the ePKA as returned by the {@link
   *     NcpehService#findPatientSummary(PatientAccessData, String, String, String)} operation
   * @param ncpehMockControlRequestHeader Control header for the NCPeH-Simulation-Mock response
   * @param patientSummaryLevels Varargs parameter specifying the levels of patient summary to
   *     retrieve
   * @return {@link RetrievePatientSummaryResponseDO} The patient's ePKA
   */
  RetrievePatientSummaryResponseDO retrievePatientSummary(
      PatientAccessData patientAccessData,
      Patient patient,
      String testdataProfileName,
      String leiCountry,
      AdhocQueryResponse metadata,
      String ncpehMockControlRequestHeader,
      PatientSummaryLevel... patientSummaryLevels);
}
