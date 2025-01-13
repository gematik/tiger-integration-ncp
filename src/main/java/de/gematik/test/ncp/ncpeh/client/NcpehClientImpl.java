/*
 * Copyright (c) 2024-2025 gematik GmbH
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
package de.gematik.test.ncp.ncpeh.client;

import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.ncpeh.NcpehService;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.FindPatientSummaryDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryDO;
import de.gematik.test.ncp.ncpeh.data.TestdataFactory;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
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

  /**
   * Path to the info object of the NCPeH Trigger interface server<br>
   * Note: This assumes it runs in a Spring Boot application. It might make sense to replace this
   * with something, that is not specific to Spring Boot
   */
  public static final String INFO_PATH = "/actuator/info";

  public static final String REQUEST_HEADER_X_NCPEHMOCK_RESPONSE = "X-NCPeHMock-Response";

  @NonNull private final ExternalServerConfig config;

  @Accessors(fluent = true)
  @Getter
  private final NcpehSimulatorApi clientProxy;

  @Override
  public Boolean ncpehIsUpAndRunning() {
    return HttpStatus.valueOf(
            SerenityRest.get(URI.create("http://" + config.getHostname()).resolve(INFO_PATH))
                .statusCode())
        .is2xxSuccessful();
  }

  @Override
  public IdentifyPatientDO identifyPatient(
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
    // Cast to Client to add headers
    final Client client = (Client) clientProxy();
    final MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
    headers.put(
        REQUEST_HEADER_X_NCPEHMOCK_RESPONSE,
        List.of(Optional.ofNullable(ncpehMockControlRequestHeader).orElse("")));

    client.headers(headers);
  }

  @Override
  public FindPatientSummaryDO findPatientSummary(
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
  public RetrievePatientSummaryDO retrievePatientSummary(
      final PatientAccessData patientAccessData,
      final String testdataProfileName,
      final String leiCountry,
      final AdhocQueryResponse metadata,
      final String ncpehMockControlRequestHeader,
      final PatientSummaryLevel... patientSummaryLevels) {
    final var testdata = Testdata.instance().ncpehSimTestdataProfiles().get(testdataProfileName);

    setNcpehMockControlRequestHeader(ncpehMockControlRequestHeader);

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
