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

import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.epa.ihe.model.response.ProxyFindResponse;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.request.FindDocumentsRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import de.gematik.test.ncp.data.NcpehSimTestdataProfile;
import de.gematik.test.ncp.data.PatientAccessData;
import de.gematik.test.ncp.ncpeh.NcpehService;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
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

  public static final String EPED_ACCESS_CODE_ASSIGNING_AUTHORITY = "1.2.276.0.76.4.299";

  /**
   * Build an IdentifyPatientRequest with the given KVNR and some fixed test data (see constants in
   * this class) for the remaining mandatory fields. All optional fields will be null.
   *
   * @param patientAccessData Patient Identification Number, a.k.a. KVNR
   * @param country code of the country, where the EU practitioner is located
   * @param testdata configured testdata profile, e.g. for information regarding the ida assertion
   *     profile
   * @return {@link IdentifyPatientRequest}
   */
  public static IdentifyPatientRequest buildStandardIdentifyPatientRequest(
      @NonNull final PatientAccessData patientAccessData,
      @NonNull final EuCountryCode country,
      @NonNull final NcpehSimTestdataProfile testdata) {
    return IdentifyPatientRequestBuilder.newInstance()
        .accessCode(patientAccessData.getAccessCode())
        .kvnr(patientAccessData.getKvnr())
        .euCountryCode(country)
        .idaAssertionProfileName(testdata.idaProfileName())
        .build();
  }

  /**
   * Build a FindDocumentsRequest with the given KVNR and some fixed test data (see constants in
   * this class) for the remaining mandatory fields. All optional fields will be null.
   *
   * @param patientAccessData patientAccessData information e.g. KVNR
   * @param country code of the country, where the EU practitioner is located
   * @param testdata configured testdata profile, e.g. for information regarding ida and trc
   *     assertion profile
   * @return {@link FindDocumentsRequest}
   */
  public static FindDocumentsRequest buildStandardFindDocumentsRequest(
      @NonNull final PatientAccessData patientAccessData,
      @NonNull final EuCountryCode country,
      @NonNull final NcpehSimTestdataProfile testdata) {
    return FindDocumentsRequestBuilder.newInstance()
        .trcAssertionProfileName(testdata.trcProfileName())
        .kvnr(patientAccessData.getKvnr())
        .accessCode(patientAccessData.getAccessCode())
        .euCountryCode(country)
        .idaAssertionProfileName(testdata.idaProfileName())
        .build();
  }

  /**
   * Build a RetrieveDocumentRequest with the given metadata and some fixed test data (see constants
   * in this class) for the remaining mandatory fields. All optional fields will be null.<br>
   *
   * @param patientAccessData patientAccessData information e.g. KVNR
   * @param country code of the country, where the EU practitioner is located
   * @param testdata configured testdata profile, e.g. for information regarding ida and trc
   *     assertion profile
   * @param metadata data as they are returned by the {@link
   *     NcpehService#findPatientSummary(PatientAccessData, String, String, String)} operation<br>
   *     Note: So far no data are read from metadata, but test data defined in the constants of this
   *     class are used instead
   * @param patientSummaryLevels List of patientAccessData summary levels (1 and 3), which shall be
   *     retrieved.
   * @return {@link RetrieveDocumentRequest}
   */
  public static RetrieveDocumentRequest buildStandardRetrieveDocumentRequest(
      @NonNull final PatientAccessData patientAccessData,
      @NonNull final EuCountryCode country,
      @NonNull final NcpehSimTestdataProfile testdata,
      final AdhocQueryResponse metadata,
      final PatientSummaryLevel... patientSummaryLevels) {

    final var structuredMetadata = ResponseUtils.toProxyFindResponse(metadata);

    final var builder = RetrieveDocumentRequestBuilder.newInstance();

    Optional.ofNullable(patientSummaryLevels).stream()
        .flatMap(Arrays::stream)
        .forEach(psl -> insertDocumentInformationForRetrieve(builder, structuredMetadata, psl));

    return builder
        .trcAssertionProfileName(testdata.trcProfileName())
        .kvnr(patientAccessData.getKvnr())
        .accessCode(patientAccessData.getAccessCode())
        .euCountryCode(country)
        .idaAssertionProfileName(testdata.idaProfileName())
        .build();
  }

  private static void insertDocumentInformationForRetrieve(
      final RetrieveDocumentRequestBuilder builder,
      final ProxyFindResponse metadata,
      final PatientSummaryLevel patientSummaryLevel) {

    final var docMetadata =
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
        .repositoryUniqueId(docMetadata.repositoryUniqueId());
  }
}
