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

package de.gematik.unittest.testutil;

import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.ncpeh.client.NcpehClientImpl;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.mockito.Mockito;

@Accessors(fluent = true)
public class NcpehClientImplForUnittest extends NcpehClientImpl {

  @Getter(lazy = true)
  private final NcpehSimulatorApi clientProxy = initializeNcpehApiMock();

  public NcpehClientImplForUnittest(ExternalServerConfig config) {
    super(config);
  }

  private NcpehSimulatorApi initializeNcpehApiMock() {
    var mock = Mockito.mock(NcpehSimulatorApi.class);

    Mockito.when(mock.identifyPatient(Mockito.any()))
        .thenReturn(Response.ok().entity(TestUtils.identifyPatientResponse()).build());
    Mockito.when(mock.findDocuments(Mockito.any()))
        .thenReturn(Response.ok().entity(TestUtils.findDocumentResponse()).build());
    Mockito.when(mock.retrieveDocument(Mockito.any()))
        .thenReturn(Response.ok().entity(TestUtils.retrieveDocumentResponse()).build());

    return mock;
  }
}
