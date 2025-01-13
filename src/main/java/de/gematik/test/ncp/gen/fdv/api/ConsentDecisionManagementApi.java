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

package de.gematik.test.ncp.gen.fdv.api;

import de.gematik.test.ncp.gen.fdv.model.FunctionIdType;
import de.gematik.test.ncp.gen.fdv.model.GetConsentDecisionListResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.PutConsentDecisionRequestDTO;
import de.gematik.test.ncp.gen.fdv.model.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import org.apache.cxf.jaxrs.ext.multipart.*;

/**
 * I_Test_Driver_FdV
 *
 * <p>REST Test Interface for the Insurant´s User Frontend (FdV)
 */
@Path("/epa/testdriver/api/v1/consentDecision")
@Api(value = "/", description = "")
public interface ConsentDecisionManagementApi {

  /**
   * Get a list of all user decision regarding a particular consent related function of a personal
   * health record.
   *
   * <p>This operation executes the use case \&quot;List all current consent decisions\&quot; in an
   * existing user session. By default, the health record of the user is addressed, otherwise the
   * health record is identified by \&quot;recordId\&quot;.
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Get a list of all user decision regarding a particular consent related function of a personal health record.",
      tags = {"Consent Decision Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = GetConsentDecisionListResponseDTO.class)
      })
  public GetConsentDecisionListResponseDTO getConsentDecisionList(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Update an entry of an user decision regarding a particular consent related function of a
   * personal health record.
   *
   * <p>This operation executes the use case \&quot;Update a consent decisions\&quot; in an existing
   * user session. By default, the health record of the user is addressed, otherwise the health
   * record is identified by \&quot;recordId\&quot;.
   */
  @PUT
  @Path("/{functionid}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Update an entry of an user decision regarding a particular consent related function of a personal health record.",
      tags = {"Consent Decision Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO putConsentDecision(
      @PathParam("functionid") FunctionIdType functionid,
      @Valid @NotNull PutConsentDecisionRequestDTO putConsentDecisionRequestDTO);
}
