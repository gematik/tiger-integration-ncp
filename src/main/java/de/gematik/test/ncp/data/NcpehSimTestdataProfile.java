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

package de.gematik.test.ncp.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.gematik.ncpeh.api.request.FindDocumentsRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import de.gematik.test.ncp.data.NcpehSimTestdataProfiles.NcpehSimTestdataProfileImpl;

/**
 * Interface for the testdata to be used in generation of the requests for the three operations
 * {@link de.gematik.ncpeh.api.NcpehSimulatorApi#identifyPatient(IdentifyPatientRequest)}, {@link
 * de.gematik.ncpeh.api.NcpehSimulatorApi#findDocuments(FindDocumentsRequest)} & {@link
 * de.gematik.ncpeh.api.NcpehSimulatorApi#retrieveDocument(RetrieveDocumentRequest)} )}
 */
@JsonDeserialize(as = NcpehSimTestdataProfileImpl.class)
public interface NcpehSimTestdataProfile {

  String trcProfileName();

  String idaProfileName();
}
