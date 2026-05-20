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

package de.gematik.test.ncp.ncpeh.data;

import static de.gematik.test.ncp.utils.TestUtils.loadFromJsonResource;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
  private static final String OID_AC_EPKA_ASSIGNING_AUTHORITY = "1.2.276.0.76.4.298";
  private static final String XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA =
      "('60591-5^^2.16.840.1.113883.6.1')";

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
                    PATIENT_ACCESS_DATA,
                    COUNTRY,
                    OID_AC_EPKA_ASSIGNING_AUTHORITY,
                    TESTDATA_PROFILE));
    // Assert
    assertEquals(KVNR, request.patientId().kvnr());

    log.info(mapper.writeValueAsString(request));
    final var expected =
        loadFromJsonResource(
            IdentifyPatientRequest.class, this.getClass(), "IdentifyPatientRequest.json");
    assertEquals(expected, request);
  }

  @Test
  void buildStandardIdentifyPatientRequest_shouldThrowOnNullArguments() {
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardIdentifyPatientRequest(
                null, COUNTRY, "auth", TESTDATA_PROFILE));
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardIdentifyPatientRequest(
                PATIENT_ACCESS_DATA, null, "auth", TESTDATA_PROFILE));
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardIdentifyPatientRequest(
                PATIENT_ACCESS_DATA, COUNTRY, null, TESTDATA_PROFILE));
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardIdentifyPatientRequest(
                PATIENT_ACCESS_DATA, COUNTRY, "auth", null));
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
                    PATIENT_ACCESS_DATA,
                    COUNTRY,
                    XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA,
                    TESTDATA_PROFILE));
    // Assert
    assertEquals(KVNR, request.patientId().kvnr());

    log.info(mapper.writeValueAsString(request));
    final var expected =
        loadFromJsonResource(
            FindDocumentsRequest.class, this.getClass(), "FindDocumentsRequest.json");
    assertEquals(expected, request);
  }

  @Test
  void buildStandardFindDocumentsRequest_shouldThrowOnNullArguments() {
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardFindDocumentsRequest(
                null, COUNTRY, XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA, TESTDATA_PROFILE));
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardFindDocumentsRequest(
                PATIENT_ACCESS_DATA, null, XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA, TESTDATA_PROFILE));
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardFindDocumentsRequest(
                PATIENT_ACCESS_DATA, COUNTRY, null, TESTDATA_PROFILE));
    assertThrows(
        NullPointerException.class,
        () ->
            TestdataFactory.buildStandardFindDocumentsRequest(
                PATIENT_ACCESS_DATA, COUNTRY, XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA, null));
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
