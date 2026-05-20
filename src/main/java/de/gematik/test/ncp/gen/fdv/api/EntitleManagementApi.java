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

import de.gematik.test.ncp.gen.fdv.model.GetBlockedUserListResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.GetEntitlementListResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.PostEntitlementRequestDTO;
import de.gematik.test.ncp.gen.fdv.model.PostEntitlementResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.PutBlockedUserRequestDTO;
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
@Path("/epa/testdriver/api/v1")
@Api(value = "/", description = "")
public interface EntitleManagementApi {

  /**
   * Delete a single specific blocked user
   *
   * <p>This operation executes the use case \&quot;Delete an existing entry for a blocked
   * user\&quot; in an existing user session. By default, the health record of the user is
   * addressed, otherwise the health record is identified by \&quot;recordId.
   */
  @DELETE
  @Path("/blockedUsers/{telematikId}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Delete a single specific blocked user",
      tags = {"Entitle Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO deleteBlockedUser(
      @PathParam("telematikId") @Pattern(regexp = "^[1-9][0-9]?-[\\x21-\\x7E]+$") @Size(max = 128)
          String telematikId,
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Delete a single specific entitlement
   *
   * <p>This operation executes the use case \&quot;Delete an Entitlement\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId.
   */
  @DELETE
  @Path("/entitlements/{actorId}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Delete a single specific entitlement",
      tags = {"Entitle Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO deleteEntitlement(
      @PathParam("actorId") @Pattern(regexp = "(^[A-Z]{1}\\d{9}$)|(^[1-9][0-9]?-[\\x21-\\x7E]+$)")
          String actorId,
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Get a list of all blocked users and institutions, their related user and validity period
   *
   * <p>This operation executes the use case \&quot;List all blocked users\&quot; in an existing
   * user session. By default, the health record of the user is addressed, otherwise the health
   * record is identified by \&quot;recordId\&quot;.
   */
  @GET
  @Path("/blockedUsers")
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Get a list of all blocked users and institutions, their related user and validity period",
      tags = {"Entitle Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = GetBlockedUserListResponseDTO.class)
      })
  public GetBlockedUserListResponseDTO getBlockedUserList(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Get a list of all granted entitlements, their related user and validity period
   *
   * <p>This operation executes the use case \&quot;List all Entitlements\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @GET
  @Path("/entitlements")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get a list of all granted entitlements, their related user and validity period",
      tags = {"Entitle Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = GetEntitlementListResponseDTO.class)
      })
  public GetEntitlementListResponseDTO getEntitlementList(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Add a new or update an existing blocked user identified by telematikId
   *
   * <p>This operation executes the use case \&quot;Add a new blocked user\&quot; in an existing
   * user session. By default, the health record of the user is addressed, otherwise the health
   * record is identified by \&quot;recordId.
   */
  @PUT
  @Path("/blockedUsers/{telematikId}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Add a new or update an existing blocked user identified by telematikId",
      tags = {"Entitle Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO postBlockedUser(
      @PathParam("telematikId") @Pattern(regexp = "^[1-9][0-9]?-[\\x21-\\x7E]+$") @Size(max = 128)
          String telematikId,
      @Valid @NotNull PutBlockedUserRequestDTO putBlockedUserRequestDTO);

  /**
   * Add a new or update an existing entitlement for an institution or a representative identified
   * by displayname, telematikId or insurantId
   *
   * <p>This operation executes the use case \&quot;Add a new entitlement\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId.
   */
  @POST
  @Path("/entitlements")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Add a new or update an existing entitlement for an institution or a representative identified by displayname, telematikId or insurantId",
      tags = {"Entitle Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = PostEntitlementResponseDTO.class)
      })
  public PostEntitlementResponseDTO postEntitlement(
      @Valid @NotNull PostEntitlementRequestDTO postEntitlementRequestDTO);
}
