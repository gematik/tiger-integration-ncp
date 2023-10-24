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

package de.gematik.test.ncp.ncpeh.client;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.ncpeh.NcpehException;
import de.gematik.test.ncp.ncpeh.NcpehInterface;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.FindPatientSummaryDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryDO;
import de.gematik.test.ncp.ncpeh.data.TestdataFactory;
import de.gematik.test.ncp.util.Utils;
import jakarta.ws.rs.core.MediaType;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.http.HttpStatus;

/**
 * Implementation of the {@link NcpehInterface} using the NCPeH Trigger Interface ({@link
 * NcpehSimulatorApi}) to trigger operations at an NCPeH server implementing the NCPeH Trigger
 * Interface.<br>
 */
@Slf4j
@RequiredArgsConstructor
public class NcpehClientImpl implements NcpehInterface {

  /**
   * Path to the info object of the NCPeH Trigger interface server<br>
   * Note: This assumes it runs in a Spring Boot application. It might make sense to replace this
   * with something, that is not specific to Spring Boot
   */
  public static final String INFO_PATH = "/actuator/info";

  protected final ExternalServerConfig config;

  @Accessors(fluent = true)
  @Getter(lazy = true)
  private final NcpehSimulatorApi clientProxy = initializeNcpehApiProxy();

  @Override
  public Boolean ncpehIsUpAndRunning() {
    return HttpStatus.valueOf(
            SerenityRest.get(URI.create("http://" + config.getHostname()).resolve(INFO_PATH))
                .statusCode())
        .is2xxSuccessful();
  }

  @Override
  public IdentifyPatientDO identifyPatient(
      Patient patient, String testdataProfileName, String leiCountry) {
    var testdata = Testdata.instance().ncpehSimTestdataProfiles().get(testdataProfileName);

    try (var response =
        clientProxy()
            .identifyPatient(
                TestdataFactory.buildStandardIdentifyPatientRequest(
                    patient, EuCountryCode.valueOf(leiCountry.toUpperCase()), testdata))) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForIdentifyPatient(response);
      }

      throw new NcpehException(
          "identifyPatient operation did not return with a success status", response);
    }
  }

  @Override
  public FindPatientSummaryDO findPatientSummary(
      Patient patient, String testdataProfileName, String leiCountry) {
    var testdata = Testdata.instance().ncpehSimTestdataProfiles().get(testdataProfileName);

    try (var response =
        clientProxy()
            .findDocuments(
                TestdataFactory.buildStandardFindDocumentsRequest(
                    patient, EuCountryCode.valueOf(leiCountry.toUpperCase()), testdata))) {

      if (HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
        return DataUtils.convertResponseDataForFindPatientSummary(response);
      }

      throw new NcpehException("findDocuments operation did not run successfully", response);
    }
  }

  @Override
  public RetrievePatientSummaryDO retrievePatientSummary(
      Patient patient,
      String testdataProfileName,
      String leiCountry,
      AdhocQueryResponse metadata,
      PatientSummaryLevel... patientSummaryLevels) {
    var testdata = Testdata.instance().ncpehSimTestdataProfiles().get(testdataProfileName);

    try (var response =
        clientProxy()
            .retrieveDocument(
                TestdataFactory.buildStandardRetrieveDocumentRequest(
                    patient,
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

  // region private

  private NcpehSimulatorApi initializeNcpehApiProxy() {
    var provider =
        new JacksonJsonProvider()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

    provider
        .locateMapper(LocalDate.class, MediaType.APPLICATION_JSON_TYPE)
        .registerModule(new JavaTimeModule())
        .setSerializationInclusion(Include.NON_NULL);

    assert config != null;
    return JAXRSClientFactory.create(
        Utils.buildUri(config.getHostname(), config.getBasePath()).toString(),
        NcpehSimulatorApi.class,
        List.of(provider));
  }

  // endregion private
}
