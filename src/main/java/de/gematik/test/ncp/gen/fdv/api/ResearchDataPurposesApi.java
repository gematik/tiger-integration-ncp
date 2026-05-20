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

package de.gematik.test.ncp.gen.fdv.api;

import de.gematik.test.ncp.gen.fdv.model.GetResearchDataPurposesResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.PurposeIdType;
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
@Path("/epa/testdriver/api/v1/researchdatapurposes")
@Api(value = "/", description = "")
public interface ResearchDataPurposesApi {

  /**
   * Get a list of all research data purposes
   *
   * <p>This operation executes the use case \&quot;List all research data purposes\&quot; in an
   * existing user session. By default, the health record of the user is addressed, otherwise the
   * health record is identified by \&quot;recordId\&quot;.
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get a list of all research data purposes",
      tags = {"ResearchDataPurposes"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = GetResearchDataPurposesResponseDTO.class)
      })
  public GetResearchDataPurposesResponseDTO getResearchDataPurposes(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Update decision for a research data purpose
   *
   * <p>This operation executes the use case \&quot;Update decision for a research data
   * purpose\&quot; in an existing user session. By default, the health record of the user is
   * addressed, otherwise the health record is identified by \&quot;recordId\&quot;.
   */
  @PUT
  @Path("/{purposeId}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Update decision for a research data purpose",
      tags = {"ResearchDataPurposes"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO putResearchDataPurpose(
      @PathParam("purposeId") PurposeIdType purposeId, @Valid @NotNull String body);
}
