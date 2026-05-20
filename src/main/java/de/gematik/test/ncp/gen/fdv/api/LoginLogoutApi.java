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

import de.gematik.test.ncp.gen.fdv.model.LoginResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
public interface LoginLogoutApi {

  /**
   * Login user session
   *
   * <p>This operation establishes a user session. The user is identified by
   * \&quot;insurantId\&quot;.If \&quot;insurantId\&quot; is not set, the operation uses a given
   * default user. If \&quot;recordId\&quot; is not set, recordId is equal insurantId. Based on
   * \&quot;recordId\&quot; the fqdn of the access gateway should be inferred. See configuration
   * interface.
   */
  @GET
  @Path("/login")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Login user session",
      tags = {"Login / Logout"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = LoginResponseDTO.class)
      })
  public LoginResponseDTO login(
      @QueryParam("insurantId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String insurantId,
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId,
      @QueryParam("authorizeRepresentative") Boolean authorizeRepresentative);

  /**
   * Logout user session
   *
   * <p>The currently established user session is stopped.
   */
  @GET
  @Path("/logout")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Logout user session",
      tags = {"Login / Logout"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO logout(@QueryParam("fqdn") String fqdn);
}
