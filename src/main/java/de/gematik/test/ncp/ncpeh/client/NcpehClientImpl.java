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
package de.gematik.test.ncp.ncpeh.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.ncpeh.NcpehService;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.FindPatientSummaryResponseDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientResponseDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryResponseDO;
import de.gematik.test.ncp.ncpeh.data.TestdataFactory;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
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

  public static final String REQUEST_HEADER_X_NCPEHMOCK_RESPONSE = "X-NCPeHMock-Response";
  public static final String REQUEST_HEADER_X_NCPEHMOCK_PATIENT = "X-NCPeHMock-Patient";

  @NonNull private final ExternalServerConfig config;

  @Accessors(fluent = true)
  @Getter
  private final NcpehSimulatorApi clientProxy;

  @Override
  public IdentifyPatientResponseDO identifyPatient(
      final PatientAccessData patientAccessData,
      final String testdataProfileName,
      final String leiCountry,
      final String ncpehMockControlRequestHeader) {
    final var testdata = Testdata.instance().ncpehSimTestdataProfiles().get(testdataProfileName);

    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader);

    try (final var response =
        clientProxy()
            .identifyPatient(
                TestdataFactory.buildStandardIdentifyPatientRequest(
                    patientAccessData,
                    EuCountryCode.valueOf(leiCountry.toUpperCase()),
                    testdata))) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForIdentifyPatient(response);
      }

      throw new NcpehException(
          "identifyPatient operation did not return with a success status", response);
    }
  }

  private void setNcpehMockControlRequestHeader(final String ncpehMockControlRequestHeader) {
    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader, null);
  }

  private void setNcpehMockControlRequestHeader(
      final String ncpehMockControlRequestHeader, final Patient patient) {
    // Cast to Client to add headers
    final Client client = (Client) clientProxy();
    final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
    headers.put(
        REQUEST_HEADER_X_NCPEHMOCK_RESPONSE,
        List.of(Optional.ofNullable(ncpehMockControlRequestHeader).orElse("")));

    headers.put(
        REQUEST_HEADER_X_NCPEHMOCK_PATIENT,
        List.of(
            Optional.ofNullable(patient)
                .map(
                    p -> {
                      try {
                        return Base64.getEncoder()
                            .encodeToString(
                                TigerGlobalConfiguration.getObjectMapper()
                                    .writeValueAsString(patient)
                                    .getBytes(StandardCharsets.UTF_8));
                      } catch (final JsonProcessingException e) {
                        throw new NcpehException("Exception by writing json", e);
                      }
                    })
                .orElse("")));

    client.headers(headers);
  }

  @Override
  public FindPatientSummaryResponseDO findPatientSummary(
      final PatientAccessData patientAccessData,
      final String testdataProfileName,
      final String leiCountry,
      final String ncpehMockControlRequestHeader) {
    final var testdata = Testdata.instance().ncpehSimTestdataProfiles().get(testdataProfileName);

    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader);

    try (final var response =
        clientProxy()
            .findDocuments(
                TestdataFactory.buildStandardFindDocumentsRequest(
                    patientAccessData,
                    EuCountryCode.valueOf(leiCountry.toUpperCase()),
                    testdata))) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForFindPatientSummary(response);
      }

      throw new NcpehException("findDocuments operation did not run successfully", response);
    }
  }

  @Override
  public RetrievePatientSummaryResponseDO retrievePatientSummary(
      final PatientAccessData patientAccessData,
      final Patient patient,
      final String testdataProfileName,
      final String leiCountry,
      final AdhocQueryResponse metadata,
      final String ncpehMockControlRequestHeader,
      final PatientSummaryLevel... patientSummaryLevels) {
    final var testdata = Testdata.instance().ncpehSimTestdataProfiles().get(testdataProfileName);

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
        return DataUtils.convertResponseDataForRetrievePatientSummary(response);
      }

      throw new NcpehException(
          "retrievePatientSummary operation did not run successfully", response);
    }
  }
}
