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

package de.gematik.test.ncp.ncpeh.data;

import static de.gematik.test.ncp.util.IheUtils.STATUS_APPROVED;

import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.epa.ihe.model.response.ProxyFindResponse;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.request.FindDocumentsRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import de.gematik.test.ncp.data.NcpehSimTestdataProfile;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.ncpeh.NcpehInterface.PatientSummaryLevel;
import java.util.Arrays;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Utility class to generate test data to be used in {@link de.gematik.ncpeh.api.NcpehSimulatorApi}
 * operations.<br>
 * All the constants are test data, probably to be replaced by configurable data in the future.
 */
@UtilityClass
public class TestdataFactory {

  public static final String NCPEH_HCID = "1.2.276.0.76.4.291";

  public static final String NCPEH_SIGNING_AUTHORITY = "1.2.276.0.76.3.1.580.147";

  /**
   * Build an IdentifyPatientRequest with the given KVNR and some fixed test data (see constants in
   * this class) for the remaining mandatory fields. All optional fields will be null.
   *
   * @param patient Patient Identification Number, a.k.a. KVNR
   * @param country code of the country, where the EU practitioner is located
   * @param testdata configured testdata profile, e.g. for information regarding the ida assertion
   *     profile
   * @return {@link IdentifyPatientRequest}
   */
  public static IdentifyPatientRequest buildStandardIdentifyPatientRequest(
      @NonNull Patient patient,
      @NonNull EuCountryCode country,
      @NonNull NcpehSimTestdataProfile testdata) {
    return IdentifyPatientRequestBuilder.newInstance()
        .accessCode(patient.accessCode())
        .kvnr(patient.kvnr())
        .euCountryCode(country)
        .idaAssertionProfileName(testdata.idaProfileName())
        .build();
  }

  /**
   * Build a FindDocumentsRequest with the given KVNR and some fixed test data (see constants in
   * this class) for the remaining mandatory fields. All optional fields will be null.
   *
   * @param patient patient information e.g. KVNR
   * @param country code of the country, where the EU practitioner is located
   * @param testdata configured testdata profile, e.g. for information regarding ida and trc
   *     assertion profile
   * @return {@link FindDocumentsRequest}
   */
  public static FindDocumentsRequest buildStandardFindDocumentsRequest(
      @NonNull Patient patient,
      @NonNull EuCountryCode country,
      @NonNull NcpehSimTestdataProfile testdata) {
    return FindDocumentsRequestBuilder.newInstance()
        .xdsDocumentEntryClassCode("('60591-5^^2.16.840.1.113883.6.1')")
        .xdsDocumentEntryStatus("('" + STATUS_APPROVED + "')")
        .trcAssertionProfileName(testdata.trcProfileName())
        .kvnr(patient.kvnr())
        .accessCode(patient.accessCode())
        .oidAssigningAuthority(NCPEH_SIGNING_AUTHORITY)
        .euCountryCode(country)
        .idaAssertionProfileName(testdata.idaProfileName())
        .build();
  }

  /**
   * Build a RetrieveDocumentRequest with the given metadata and some fixed test data (see constants
   * in this class) for the remaining mandatory fields. All optional fields will be null.<br>
   *
   * @param patient patient information e.g. KVNR
   * @param country code of the country, where the EU practitioner is located
   * @param testdata configured testdata profile, e.g. for information regarding ida and trc
   *     assertion profile
   * @param metadata data as they are returned by the {@link
   *     de.gematik.test.ncp.ncpeh.NcpehInterface#findPatientSummary(Patient, String, String)}
   *     operation<br>
   *     Note: So far no data are read from metadata, but test data defined in the constants of this
   *     class are used instead
   * @param patientSummaryLevels List of patient summary levels (1 & 3), which shall be retrieved.
   * @return {@link RetrieveDocumentRequest}
   */
  public static RetrieveDocumentRequest buildStandardRetrieveDocumentRequest(
      @NonNull Patient patient,
      @NonNull EuCountryCode country,
      @NonNull NcpehSimTestdataProfile testdata,
      AdhocQueryResponse metadata,
      PatientSummaryLevel... patientSummaryLevels) {

    var structuredMetadata = ResponseUtils.toProxyFindResponse(metadata);

    var builder = RetrieveDocumentRequestBuilder.newInstance();

    Optional.ofNullable(patientSummaryLevels).stream()
        .flatMap(Arrays::stream)
        .forEach(psl -> insertDocumentInformationForRetrieve(builder, structuredMetadata, psl));

    return builder
        .trcAssertionProfileName(testdata.trcProfileName())
        .kvnr(patient.kvnr())
        .accessCode(patient.accessCode())
        .euCountryCode(country)
        .idaAssertionProfileName(testdata.idaProfileName())
        .build();
  }

  private static void insertDocumentInformationForRetrieve(
      RetrieveDocumentRequestBuilder builder,
      ProxyFindResponse metadata,
      PatientSummaryLevel patientSummaryLevel) {

    var docMetadata =
        metadata.registryObjectLists().documentsMetadata().stream()
            .filter(md -> patientSummaryLevel.documentIsOfLevel(md.uniqueId()))
            .findFirst()
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Document metadata to patient summary of level "
                            + patientSummaryLevel.level()
                            + " are missing"));

    builder
        .documentUniqueId(docMetadata.uniqueId())
        .homeCommunityId(docMetadata.home())
        .repositoryUniqueId(docMetadata.repositoryUniqueId());
  }
}
