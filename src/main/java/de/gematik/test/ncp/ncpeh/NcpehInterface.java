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

package de.gematik.test.ncp.ncpeh;

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.ncpeh.client.dataobject.FindPatientSummaryDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryDO;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Interface describing the functions to interact with the NCPeH (Simulation). This should define
 * the single point of interaction, between the screenplay / glue code of the test suite and the
 * NCPeH components like a simulator, a mock or even a real implementation. Thus, if functionality
 * is missing here, add it! Don't go sideways!
 */
public interface NcpehInterface {

  /**
   * Check whether the NCPeH object is running and usable
   *
   * @return true if it is, false otherwise
   */
  Boolean ncpehIsUpAndRunning();

  /**
   * Retrieve the patient information necessary for identification purposes.<br>
   * Note: This operation will likely be subject to non-backward compatible changes in the future
   *
   * @param patient The health insurance identification number of the patient (a.k.a. KVNR)
   * @param testdataProfileName key of the testdata profile configured in the testdata.yaml to use
   * @param leiCountry name of the country, where the EU-Lei is situated
   * @return {@link IdentifyPatientDO} the patient information necessary for identification purposes
   */
  IdentifyPatientDO identifyPatient(Patient patient, String testdataProfileName, String leiCountry);

  /**
   * Find the patients ePKA and return its metadata. Note: This operation will likely be subject to
   * non-backward compatible changes in the future
   *
   * @return The return type here is not yet defined and will likely change in the future
   */
  FindPatientSummaryDO findPatientSummary(
      Patient patient, String testdataProfileName, String leiCountry);

  /**
   * Retrieve the patients ePKA. Note: This operation will likely be subject to non-backward
   * compatible changes in the future
   *
   * @param metadata the metadata of the ePKA as returned by the {@link
   *     NcpehInterface#findPatientSummary(Patient, String, String)} operation
   * @return The return type here is not yet defined and will likely change in the future
   */
  RetrievePatientSummaryDO retrievePatientSummary(
      Patient patient,
      String testdataProfileName,
      String leiCountry,
      AdhocQueryResponse metadata,
      PatientSummaryLevel... patientSummaryLevels);

  @RequiredArgsConstructor
  @Getter
  @Accessors(fluent = true)
  enum PatientSummaryLevel {
    LEVEL_1(1, "^PS.PDF"),
    LEVEL_3(3, "^PS.XML");

    private final Integer level;

    private final String idMarker;

    public boolean documentIsOfLevel(String documentUniqueId) {
      return documentUniqueId.endsWith(idMarker());
    }

    public static PatientSummaryLevel fromDocumentUniqueId(@NonNull String documentUniqueId) {
      return Arrays.stream(PatientSummaryLevel.values())
          .filter(psl -> psl.documentIsOfLevel(documentUniqueId))
          .findFirst()
          .orElseThrow(
              () ->
                  new IllegalArgumentException(
                      "DocumentUniqueId "
                          + documentUniqueId
                          + " is of no known PatientSummaryLevel"));
    }

    public static PatientSummaryLevel fromValue(Integer value) {
      return Arrays.stream(PatientSummaryLevel.values())
          .filter(psl -> psl.level.equals(value))
          .findFirst()
          .orElseThrow(
              () ->
                  new IllegalArgumentException(
                      "Value "
                          + Objects.toString(value, "null")
                          + " is no known PatientSummaryLevel"));
    }
  }
}
