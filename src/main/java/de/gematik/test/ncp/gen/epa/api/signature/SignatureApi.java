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

package de.gematik.test.ncp.gen.epa.api.signature;

import de.gematik.test.ncp.gen.epa.api.signature.dto.SignDocumentRequest;
import de.gematik.test.ncp.gen.epa.api.signature.dto.SignDocumentResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.*;

/**
 * ePA SIM Signature API
 *
 * <p>Signed the document by the connector. Currently, only CMS signature with embedded document is
 * supported.
 */
@Path("/signature/signDocument")
@Api(value = "/", description = "")
public interface SignatureApi {

  /**
   * Dokument signieren
   *
   * <p>Dokument durch den Konnektor signieren lassen. Gegenwärtig wird nur CMS Signatur mit
   * eingebettetem Dokument unterstützt
   */
  @POST
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Dokument signieren",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Erstellte Signatur im Erfolgsfall, andernfalls Fehlerinformationen",
            response = SignDocumentResponse.class)
      })
  public SignDocumentResponse signDocument(SignDocumentRequest signDocumentRequest);
}
