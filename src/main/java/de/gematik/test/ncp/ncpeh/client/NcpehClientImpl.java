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
package de.gematik.test.ncp.ncpeh.client;

import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.common.PrescriptionDispenseData;
import de.gematik.ncpeh.api.request.DocumentRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.NcpehSimTestdataProfile;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.ncpeh.NcpehService;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.FindDocumentsResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.ProvideAndRegisterDocumentSetResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrieveDocumentsResponseDTO;
import de.gematik.test.ncp.ncpeh.data.ProvideAndRegisterSetOfDocumentsRequestBuilder;
import de.gematik.test.ncp.ncpeh.data.RetrieveSetOfDocumentsRequestBuilder;
import de.gematik.test.ncp.ncpeh.data.TestdataFactory;
import de.gematik.test.ncp.util.Utils;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.cxf.jaxrs.client.Client;
import org.springframework.http.HttpStatus;

/**
 * Implementation of the {@link NcpehService} using the NCPeH Trigger Interface ({@link
 * NcpehSimulatorApi}) to trigger operations at an NCPeH server implementing the NCPeH Trigger
 * Interface.<br>
 */
@Slf4j
@Builder
@RequiredArgsConstructor
public class NcpehClientImpl implements NcpehService {

  public static final String CUSTOM_HEADER_PREFIX = "X-NCPeHMock-";
  public static final String REQUEST_HEADER_RESPONSE_FILE = CUSTOM_HEADER_PREFIX + "Response";
  public static final String REQUEST_HEADER_PATIENT = CUSTOM_HEADER_PREFIX + "Patient";
  public static final String REQUEST_HEADER_MEDICATION = CUSTOM_HEADER_PREFIX + "Medication";

  @NonNull private final ExternalServerConfig config;

  @Accessors(fluent = true)
  @Getter
  private final NcpehSimulatorApi clientProxy;

  @Override
  public IdentifyPatientResponseDTO identifyPatient(
      final PatientAccessData patientAccessData,
      final String testdataProfileName,
      final String providerCountry,
      final String accessCodeAssigningAuthority,
      final String ncpehMockControlRequestHeader,
      final Patient patient) {

    NcpehSimTestdataProfile testdata =
        Testdata.instance().getNcpehSimTestdataProfile(testdataProfileName);
    IdentifyPatientRequest request =
        TestdataFactory.buildStandardIdentifyPatientRequest(
            patientAccessData,
            EuCountryCode.valueOf(providerCountry.toUpperCase()),
            accessCodeAssigningAuthority,
            testdata);

    return identifyPatient(request, ncpehMockControlRequestHeader, patient);
  }

  @Override
  public IdentifyPatientResponseDTO identifyPatient(
      final IdentifyPatientRequest identifyPatientRequest,
      final String ncpehMockControlRequestHeader,
      final Patient patient) {

    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader, patient);

    try (var response = clientProxy().identifyPatient(identifyPatientRequest)) {
      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForIdentifyPatient(response);
      }

      throw new NcpehException(
          "identifyPatient operation did not return with a success status", response);
    }
  }

  @Override
  public FindDocumentsResponseDTO findDocuments(
      final PatientAccessData patientAccessData,
      final String testdataProfileName,
      final String providerCountry,
      final String xdsDocumentEntryClassCode,
      final String ncpehMockControlRequestHeader,
      final Map<String, Medication> medicationByPrescriptionId) {
    var testdata = Testdata.instance().getNcpehSimTestdataProfile(testdataProfileName);

    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader, medicationByPrescriptionId);

    try (final var response =
        clientProxy()
            .findDocuments(
                TestdataFactory.buildStandardFindDocumentsRequest(
                    patientAccessData,
                    EuCountryCode.valueOf(providerCountry.toUpperCase()),
                    xdsDocumentEntryClassCode,
                    testdata))) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForFindDocuments(response);
      }

      throw new NcpehException("findDocuments operation did not run successfully", response);
    }
  }

  @Override
  public RetrieveDocumentsResponseDTO retrieveDocuments(
      final PatientAccessData patientAccessData,
      final Patient patient,
      final String testdataProfileName,
      final String providerCountry,
      final Set<DocumentRequest> documentRequestSet,
      final String ncpehMockControlRequestHeader,
      final Map<String, Medication> medicationByPrescriptionId) {
    var testdata = Testdata.instance().getNcpehSimTestdataProfile(testdataProfileName);
    setNcpehMockControlRequestHeader(
        ncpehMockControlRequestHeader, patient, medicationByPrescriptionId);

    var request =
        RetrieveSetOfDocumentsRequestBuilder.newInstance()
            .documentRequestSet(documentRequestSet)
            .trcAssertionProfileName(testdata.trcProfileName())
            .kvnr(patientAccessData.getKvnr())
            .accessCode(patientAccessData.getAccessCode())
            .euCountryCode(EuCountryCode.fromCountryName(providerCountry))
            .idaAssertionProfileName(testdata.idaProfileName())
            .build();

    try (var response = clientProxy().retrieveSetOfDocuments(request)) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForRetrieveDocuments(response);
      }

      throw new NcpehException("retrieveDocuments operation did not run successfully", response);
    }
  }

  @Override
  public RetrieveDocumentsResponseDTO retrievePatientSummary(
      final PatientAccessData patientAccessData,
      final Patient patient,
      final String testdataProfileName,
      final String leiCountry,
      final AdhocQueryResponse metadata,
      final String ncpehMockControlRequestHeader,
      final PatientSummaryLevel... patientSummaryLevels) {
    var testdata = Testdata.instance().getNcpehSimTestdataProfile(testdataProfileName);

    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader, patient);

    try (final var response =
        clientProxy()
            .retrieveDocument(
                TestdataFactory.buildStandardRetrieveDocumentRequest(
                    patientAccessData,
                    EuCountryCode.valueOf(leiCountry.toUpperCase()),
                    testdata,
                    metadata,
                    patientSummaryLevels))) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForRetrieveDocuments(response);
      }

      throw new NcpehException(
          "retrievePatientSummary operation did not run successfully", response);
    }
  }

  @Override
  public ProvideAndRegisterDocumentSetResponseDTO provideAndRegisterDocumentSet(
      final PatientAccessData patientAccessData,
      final String testdataProfileName,
      final String providerCountry,
      final List<PrescriptionDispenseData> dispensations,
      final String formatCode,
      final String typeCode,
      final String classCode,
      final String ncpehMockControlRequestHeader) {
    final var testdata = Testdata.instance().getNcpehSimTestdataProfile(testdataProfileName);

    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader);

    try (final var response =
        clientProxy()
            .provideAndRegisterSetOfDocuments(
                ProvideAndRegisterSetOfDocumentsRequestBuilder.newInstance()
                    .trcAssertionProfileName(testdata.trcProfileName())
                    .formatCode(formatCode)
                    .typeCode(typeCode)
                    .classCode(classCode)
                    .dispensations(dispensations)
                    .kvnr(patientAccessData.getKvnr())
                    .accessCode(patientAccessData.getAccessCode())
                    .euCountryCode(EuCountryCode.valueOf(providerCountry.toUpperCase()))
                    .idaAssertionProfileName(testdata.idaProfileName())
                    .build())) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForProvideAndRegisterDocumentSet(response);
      }

      throw new NcpehException(
          "provideAndRegisterDocumentSet operation did not run successfully", response);
    }
  }

  private void setNcpehMockControlRequestHeader(final String ncpehMockControlRequestHeader) {
    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader, null, null);
  }

  private void setNcpehMockControlRequestHeader(
      final String ncpehMockControlRequestHeader, final Patient patient) {
    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader, patient, null);
  }

  private void setNcpehMockControlRequestHeader(
      final String ncpehMockControlRequestHeader,
      final Map<String, Medication> medicationByPrescriptionId) {
    setNcpehMockControlRequestHeader(
        ncpehMockControlRequestHeader, null, medicationByPrescriptionId);
  }

  private void setNcpehMockControlRequestHeader(
      final String ncpehMockControlRequestHeader,
      final Patient patient,
      final Map<String, Medication> medicationByPrescriptionId) {
    MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

    if (ncpehMockControlRequestHeader != null) {
      headers.put(REQUEST_HEADER_RESPONSE_FILE, List.of(ncpehMockControlRequestHeader));
    }
    if (patient != null) {
      headers.put(REQUEST_HEADER_PATIENT, List.of(Utils.getB64Json(patient)));
    }
    if (medicationByPrescriptionId != null && !medicationByPrescriptionId.isEmpty()) {
      headers.put(REQUEST_HEADER_MEDICATION, List.of(Utils.getB64Json(medicationByPrescriptionId)));
    }

    // cast to Client to modify headers
    ((Client) clientProxy()).reset().headers(headers);
  }
}
