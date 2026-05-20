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

package de.gematik.test.ncp.gen.epa.api.authentication;

import de.gematik.test.ncp.gen.epa.api.authentication.dto.LoginResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.*;

/**
 * ePA SIM Authentication API
 *
 * <p>Authentication related REST API for the ePA Sim based on open API
 */
@Path("/epa/testdriver/api/v1/login")
@Api(value = "/", description = "")
public interface LoginLogoutApi {

  /**
   * Login user session
   *
   * <p>This operation establishes a user session. The user is identified by
   * \&quot;insurantId\&quot;.If \&quot;insurantId\&quot; is not set, the operation uses a given
   * default user.
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "Login user session",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = LoginResponseDTO.class)
      })
  public LoginResponseDTO login(
      @QueryParam("telematikId") String telematikId,
      @HeaderParam("recordId") String recordId,
      @QueryParam("fqdn") String fqdn);
}
