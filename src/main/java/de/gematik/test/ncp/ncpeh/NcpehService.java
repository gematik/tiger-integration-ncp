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

package de.gematik.test.ncp.ncpeh;

import de.gematik.ncpeh.api.common.PrescriptionDispenseData;
import de.gematik.ncpeh.api.request.DocumentRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.ncpeh.client.dataobject.FindDocumentsResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.ProvideAndRegisterDocumentSetResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrieveDocumentsResponseDTO;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
   * @param providerCountry name of the country, where the EU-Lei is situated
   * @param accessCodeAssigningAuthority assigning authority OID for the access code
   * @param ncpehMockControlRequestHeader control header for the NCPeH-Simulation-Mock response
   * @param patient A patient object to include in requests to a mock.
   * @return {@link IdentifyPatientResponseDTO} the patientAccessData information necessary for
   *     identification purposes
   */
  IdentifyPatientResponseDTO identifyPatient(
      PatientAccessData patientAccessData,
      String testdataProfileName,
      String providerCountry,
      String accessCodeAssigningAuthority,
      String ncpehMockControlRequestHeader,
      Patient patient);

  /**
   * Retrieve the patientAccessData information necessary for identification purposes.
   *
   * @param identifyPatientRequest The request to be sent to the NCPeH test driver interface
   * @param ncpehMockControlRequestHeader Control header for the NCPeH-Simulation-Mock response
   * @param patient A patient object to include in requests to a mock.
   * @return {@link IdentifyPatientResponseDTO} the patientAccessData information necessary for
   *     identification purposes
   */
  IdentifyPatientResponseDTO identifyPatient(
      IdentifyPatientRequest identifyPatientRequest,
      String ncpehMockControlRequestHeader,
      Patient patient);

  /**
   * Find metadata for documents matching the specified criteria.
   *
   * @param patientAccessData The patient's health insurance identification number (a.k.a. KVNR)
   * @param testdataProfileName Key which refers to a testdata profile configured in testdata.yaml
   * @param providerCountry Name of the country where the EU Healthcare Provider Institution is
   *     situated
   * @param xdsDocumentEntryClassCode Semantic signifier identifying the document class to be
   *     searched for
   * @param ncpehMockControlRequestHeader Control header for the NCPeH-Simulation-Mock response
   * @param medicationByPrescriptionId Map of Prescription IDs to medication data
   * @return {@link FindDocumentsResponseDTO} The simulator communication data containing the
   *     AdhocQueryRequest and AdhocQueryResponse
   */
  FindDocumentsResponseDTO findDocuments(
      PatientAccessData patientAccessData,
      String testdataProfileName,
      String providerCountry,
      String xdsDocumentEntryClassCode,
      String ncpehMockControlRequestHeader,
      Map<String, Medication> medicationByPrescriptionId);

  /**
   * Retrieve the specified documents for the patient.
   *
   * @param patientAccessData The patient's health insurance identification number (a.k.a. KVNR)
   * @param patient The patient object containing patient details
   * @param testdataProfileName Key of the testdata profile configured in the testdata.yaml to use
   * @param providerCountry Name of the country where the EU Healthcare Provider Institution is
   *     situated
   * @param documentRequestSet Set of document requests specifying which documents to retrieve
   * @param ncpehMockControlRequestHeader Control header for the NCPeH-Simulation-Mock response
   * @param medicationByPrescriptionId Map of Prescription IDs to Medication objects for mocking
   * @return {@link RetrieveDocumentsResponseDTO} The retrieved documents
   */
  RetrieveDocumentsResponseDTO retrieveDocuments(
      PatientAccessData patientAccessData,
      Patient patient,
      String testdataProfileName,
      String providerCountry,
      Set<DocumentRequest> documentRequestSet,
      String ncpehMockControlRequestHeader,
      final Map<String, Medication> medicationByPrescriptionId);

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
   *     NcpehService#findDocuments(PatientAccessData, String, String, String, String, Map)}
   *     operation
   * @param ncpehMockControlRequestHeader Control header for the NCPeH-Simulation-Mock response
   * @param patientSummaryLevels Varargs parameter specifying the levels of patient summary to
   *     retrieve
   * @return {@link RetrieveDocumentsResponseDTO} The patient's ePKA
   */
  RetrieveDocumentsResponseDTO retrievePatientSummary(
      PatientAccessData patientAccessData,
      Patient patient,
      String testdataProfileName,
      String leiCountry,
      AdhocQueryResponse metadata,
      String ncpehMockControlRequestHeader,
      PatientSummaryLevel... patientSummaryLevels);

  /**
   * Provide information on dispensed prescriptions
   *
   * @param patientAccessData The patient's health insurance identification number (a.k.a. KVNR)
   * @param testdataProfileName Key of the testdata profile configured in the testdata.yaml to use
   * @param providerCountry Name of the country where the EU Healthcare Provider Institution is
   *     situated
   * @param dispensations Required prescription data used to generate DispenseDocuments
   * @param formatCode The format code for the provided document set
   * @param typeCode The type code for the provided document set
   * @param classCode The class code for the provided document set
   * @param ncpehMockControlRequestHeader Control header for the NCPeH-Simulation-Mock
   * @return {@link ProvideAndRegisterDocumentSetResponseDTO} The response to the transaction
   */
  ProvideAndRegisterDocumentSetResponseDTO provideAndRegisterDocumentSet(
      PatientAccessData patientAccessData,
      String testdataProfileName,
      String providerCountry,
      List<PrescriptionDispenseData> dispensations,
      String formatCode,
      String typeCode,
      String classCode,
      String ncpehMockControlRequestHeader);
}
