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

import de.gematik.ncpeh.api.common.BaseAssertionProfile;
import de.gematik.ncpeh.api.common.PrescriptionDispenseData;
import de.gematik.ncpeh.api.common.TrcAssertionProfile;
import de.gematik.ncpeh.api.request.ProvideAndRegisterSetOfDocumentsRequest;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Data(staticConstructor = "newInstance")
@EqualsAndHashCode(callSuper = true)
public final class ProvideAndRegisterSetOfDocumentsRequestBuilder
    extends RequestBaseBuilder<ProvideAndRegisterSetOfDocumentsRequest> {

  /** Profile to be used by the NCPeH country B simulation for generating a TRC assertion */
  private String trcAssertionProfileName;

  /**
   * Value of the {@code formatCode} attribute in the XDR request *
   *
   * @see <a href="https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.3.2">ebRIM DocumentEntry
   *     Attributes</a>
   */
  private String formatCode;

  /**
   * Value of the {@code typeCode} attribute in the XDR request
   *
   * @see <a href="https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.3.2">ebRIM DocumentEntry
   *     Attributes</a>
   */
  private String typeCode;

  /**
   * Value of the {@code classCode} attribute in the XDR request
   *
   * @see <a href="https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.3.2">ebRIM DocumentEntry
   *     Attributes</a>
   */
  private String classCode;

  /** Required prescription data used to generate DispenseDocuments */
  private List<PrescriptionDispenseData> dispensations;

  @Override
  public ProvideAndRegisterSetOfDocumentsRequest build() {
    var trcAssertionProfile =
        new TrcAssertionProfile(
            new BaseAssertionProfile(trcAssertionProfileName, null, null, null, null), null, null);

    return new ProvideAndRegisterSetOfDocumentsRequest(
        super.buildRequestBase(),
        trcAssertionProfile,
        super.buildPatientId(),
        accessCode(),
        formatCode,
        typeCode,
        classCode,
        dispensations);
  }
}
