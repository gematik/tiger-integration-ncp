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

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.common.SubjectId;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Builder to build a request for the {@link
 * de.gematik.ncpeh.api.NcpehSimulatorApi#identifyPatient(IdentifyPatientRequest)} operation.<br>
 * Note: Thus far only mandatory fields are set. Further fields might need to be added in the
 * future.
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Data(staticConstructor = "newInstance")
public final class IdentifyPatientRequestBuilder
    extends RequestBaseBuilder<IdentifyPatientRequest> {

  // mandatory
  private String accessCodeAssigningAuthority;

  // optional
  private String hcidNcpCountryA;
  private String hcidNcpCountryB;
  private Set<SubjectId> additionalLivingSubjectIds;
  private EuCountryCode patientAddressCountry;

  public IdentifyPatientRequest build() {
    return new IdentifyPatientRequest(
        super.buildRequestBase(),
        super.buildPatientId(),
        accessCode(),
        accessCodeAssigningAuthority,
        hcidNcpCountryA,
        hcidNcpCountryB,
        additionalLivingSubjectIds,
        patientAddressCountry);
  }
}
