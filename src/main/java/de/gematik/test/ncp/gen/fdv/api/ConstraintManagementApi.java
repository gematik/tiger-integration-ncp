/*
 * Copyright 2024-2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.gen.fdv.api;

import de.gematik.test.ncp.gen.fdv.model.GetConstraintsResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.PostConstraintRequestDTO;
import de.gematik.test.ncp.gen.fdv.model.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import java.util.UUID;
import org.apache.cxf.jaxrs.ext.multipart.*;

/**
 * I_Test_Driver_FdV
 *
 * <p>REST Test Interface for the Insurant´s User Frontend (FdV)
 */
@Path("/epa/testdriver/api/v1/constraints")
@Api(value = "/", description = "")
public interface ConstraintManagementApi {

  /**
   * Delete a single specific assignment for a deny policy
   *
   * <p>This operation executes the use case \&quot;Delete an existing entry for a assignment for a
   * deny policy\&quot; in an existing user session. By default, the health record of the user is
   * addressed, otherwise the health record is identified by \&quot;recordId.
   */
  @DELETE
  @Path("/{assignmentId}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Delete a single specific assignment for a deny policy",
      tags = {"Constraint Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO deleteConstraint(
      @PathParam("assignmentId") UUID assignmentId,
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * List all current constraints
   *
   * <p>This operation executes the use case \&quot;List all current constraints\&quot; in an
   * existing user session. By default, the health record of the user is addressed, otherwise the
   * health record is identified by \&quot;recordId\&quot;.
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "List all current constraints",
      tags = {"Constraint Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = GetConstraintsResponseDTO.class)
      })
  public GetConstraintsResponseDTO getConstraints(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Set a new assignment for a deny policy for a personal health record.
   *
   * <p>This operation executes the use case \&quot;Add a new deny policy\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @POST
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Set a new assignment for a deny policy for a personal health record.",
      tags = {"Constraint Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO postConstraints(
      @Valid @NotNull PostConstraintRequestDTO postConstraintRequestDTO);
}
