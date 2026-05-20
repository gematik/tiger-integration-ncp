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

package de.gematik.test.ncp.gen.epa.api.entitlement;

import de.gematik.test.ncp.gen.epa.api.entitlement.dto.GetBlockedUserListResponseDTO;
import de.gematik.test.ncp.gen.epa.api.entitlement.dto.PostEntitlementRequestDTO;
import de.gematik.test.ncp.gen.epa.api.entitlement.dto.PostEntitlementResponseDTO;
import de.gematik.test.ncp.gen.epa.api.entitlement.dto.ResponseDTO;
import de.gematik.test.ncp.gen.epa.api.entitlement.dto.SetBlockedUserRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.*;

/**
 * ePA SIM Entitlement API
 *
 * <p>Entitlement related REST API for the ePA Sim based on open API
 */
@Path("/epa/testdriver/api/v1")
@Api(value = "/", description = "")
public interface EntitlementApi {

  /**
   * Delete a single specific blocked user
   *
   * <p>This operation executes the use case \&quot;Delete an existing entry for a blocked
   * user\&quot; in an existing user session. By default, the health record of the user is
   * addressed, otherwise the health record is identified by \&quot;recordId.
   */
  @DELETE
  @Path("/blockedusers/{telematikid}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Delete a single specific blocked user",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO deleteBlockedUser(
      @HeaderParam("x-insurantid") String xInsurantid,
      @PathParam("telematikid") String telematikid);

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
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = GetBlockedUserListResponseDTO.class)
      })
  public GetBlockedUserListResponseDTO getBlockedUserList(
      @HeaderParam("x-insurantid") String xInsurantid);

  /**
   * Add a new or update an existing blocked user identified by telematikId
   *
   * <p>This operation executes the use case \&quot;Add a new blocked user\&quot; in an existing
   * user session. By default, the health record of the user is addressed, otherwise the health
   * record is identified by \&quot;recordId.
   */
  @POST
  @Path("/blockedUsers")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Add a new or update an existing blocked user identified by telematikId",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO postBlockedUser(
      @HeaderParam("x-insurantid") String xInsurantid,
      SetBlockedUserRequestDTO setBlockedUserRequestDTO);

  /**
   * Add a new or update an existing entitlement for an institution identified by displayname,
   * telematikId or insurantId
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
          "Add a new or update an existing entitlement for an institution identified by displayname, telematikId or insurantId",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = PostEntitlementResponseDTO.class)
      })
  public PostEntitlementResponseDTO postEntitlement(
      @HeaderParam("x-insurantid") String xInsurantid,
      PostEntitlementRequestDTO postEntitlementRequestDTO);
}
