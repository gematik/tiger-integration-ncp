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

package de.gematik.test.ncp.ncpeh.data;

import static de.gematik.test.ncp.utils.TestUtils.loadFromJsonResource;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.request.FindDocumentsRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import de.gematik.test.ncp.data.NcpehSimTestdataProfile;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import de.gematik.test.ncp.util.Utils;
import java.util.Objects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.junit.jupiter.api.Test;

@Slf4j
class TestdataFactoryTest {

  public static final String KVNR = "X112309471";

  public static final PatientAccessData PATIENT_ACCESS_DATA =
      new PatientAccessData() {
        @Override
        public String getKvnr() {
          return KVNR;
        }

        @Override
        public String getAccessCode() {
          return "123456";
        }
      };

  public static final EuCountryCode COUNTRY = EuCountryCode.SPAIN;

  public static final NcpehSimTestdataProfile TESTDATA_PROFILE =
      new NcpehSimTestdataProfile() {
        @Override
        public String trcProfileName() {
          return "defaultTrc";
        }

        @Override
        public String idaProfileName() {
          return "defaultIda";
        }
      };
  private static final String ADHOC_QUERY_RESPONSE_XML_FILE_NAME = "AdhocQueryResponse.xml";

  private final ObjectMapper mapper = new ObjectMapper();

  @SneakyThrows
  @Test
  void buildStandardIdentifyPatientRequest() {
    // Arrange
    // Act
    final var request =
        assertDoesNotThrow(
            () ->
                TestdataFactory.buildStandardIdentifyPatientRequest(
                    PATIENT_ACCESS_DATA, COUNTRY, TESTDATA_PROFILE));
    // Assert
    assertEquals(KVNR, request.patientId().kvnr());

    log.info(mapper.writeValueAsString(request));
    final var expected =
        loadFromJsonResource(
            IdentifyPatientRequest.class, this.getClass(), "IdentifyPatientRequest.json");
    assertEquals(expected, request);
  }

  @SneakyThrows
  @Test
  void buildStandardFindDocumentsRequest() {
    // Arrange
    // Act
    final var request =
        assertDoesNotThrow(
            () ->
                TestdataFactory.buildStandardFindDocumentsRequest(
                    PATIENT_ACCESS_DATA, COUNTRY, TESTDATA_PROFILE));
    // Assert
    assertEquals(KVNR, request.patientId().kvnr());

    log.info(mapper.writeValueAsString(request));
    final var expected =
        loadFromJsonResource(
            FindDocumentsRequest.class, this.getClass(), "FindDocumentsRequest.json");
    assertEquals(expected, request);
  }

  @SneakyThrows
  @Test
  void buildStandardRetrieveDocumentRequest() {
    // Arrange
    final var adhocQueryResponse =
        Utils.unmarshalXml(
            AdhocQueryResponse.class,
            Objects.requireNonNull(
                TestdataFactoryTest.class.getResourceAsStream(ADHOC_QUERY_RESPONSE_XML_FILE_NAME)));

    // Act
    final var retrieveDocRequest =
        assertDoesNotThrow(
            () ->
                TestdataFactory.buildStandardRetrieveDocumentRequest(
                    PATIENT_ACCESS_DATA,
                    COUNTRY,
                    TESTDATA_PROFILE,
                    adhocQueryResponse,
                    PatientSummaryLevel.values()));

    // Assert
    assertNotNull(retrieveDocRequest);

    log.info(mapper.writeValueAsString(retrieveDocRequest));

    final var metadata = ResponseUtils.toProxyFindResponse(adhocQueryResponse);
    assertTrue(
        metadata.registryObjectLists().documentsMetadata().stream()
            .allMatch(
                dm -> retrieveDocRequest.repositoryUniqueId().equals(dm.repositoryUniqueId())));
    assertTrue(
        metadata.registryObjectLists().documentsMetadata().stream()
            .allMatch(dm -> retrieveDocRequest.homeCommunityId().equals(dm.home())));
    assertTrue(
        metadata.registryObjectLists().documentsMetadata().stream()
            .anyMatch(dm -> retrieveDocRequest.documentUniqueId().equals(dm.uniqueId())));
    assertTrue(
        metadata.registryObjectLists().documentsMetadata().stream()
            .anyMatch(dm -> retrieveDocRequest.additionalDocumentUniqueId().equals(dm.uniqueId())));

    final var expected =
        loadFromJsonResource(
            RetrieveDocumentRequest.class, this.getClass(), "RetrieveDocumentRequest.json");
    assertEquals(expected, retrieveDocRequest);
  }
}
