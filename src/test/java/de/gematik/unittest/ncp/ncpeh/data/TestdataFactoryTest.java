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

package de.gematik.unittest.ncp.ncpeh.data;

import static org.junit.jupiter.api.Assertions.*;

import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.data.NcpehSimTestdataProfile;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.NcpehInterface.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.data.TestdataFactory;
import de.gematik.unittest.testutil.TestUtils;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TestdataFactoryTest {

  public static final String KVNR = "X112309471";

  public static final Patient PATIENT =
      new PatientImpl(
          PersonName.fromString("Dr. Wilhem Friedrich zu Klopsberg Güsten"),
          KVNR,
          LocalDate.of(1995, 12, 2));

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

  @Test
  void buildStandardIdentifyPatientRequest() {
    var result =
        assertDoesNotThrow(
            () ->
                TestdataFactory.buildStandardIdentifyPatientRequest(
                    PATIENT, COUNTRY, TESTDATA_PROFILE));

    assertEquals(KVNR, result.patientId().kvnr());
  }

  @Test
  void buildStandardFindDocumentsRequest() {
    var result =
        assertDoesNotThrow(
            () ->
                TestdataFactory.buildStandardFindDocumentsRequest(
                    PATIENT, COUNTRY, TESTDATA_PROFILE));

    assertEquals(KVNR, result.patientId().kvnr());
  }

  @Test
  void buildStandardRetrieveDocumentRequest() {
    var adhocQueryResponse = TestUtils.adhocQueryResponse();

    var retrieveDocRequest =
        assertDoesNotThrow(
            () ->
                TestdataFactory.buildStandardRetrieveDocumentRequest(
                    PATIENT,
                    COUNTRY,
                    TESTDATA_PROFILE,
                    adhocQueryResponse,
                    PatientSummaryLevel.values()));

    assertNotNull(retrieveDocRequest);

    var metadata = ResponseUtils.toProxyFindResponse(adhocQueryResponse);
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
  }
}
