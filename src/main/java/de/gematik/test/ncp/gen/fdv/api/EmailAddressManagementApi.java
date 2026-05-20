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

import de.gematik.test.ncp.gen.fdv.model.GetEmailResponseDTO;
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
@Path("/epa/testdriver/api/v1/emails")
@Api(value = "/", description = "")
public interface EmailAddressManagementApi {

  /**
   * get stored email address
   *
   * <p>get stored email address of an insurant
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "get stored email address",
      tags = {"Email Address Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = GetEmailResponseDTO.class)
      })
  public GetEmailResponseDTO getEmailAddress();

  /** update email address */
  @PUT
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "update email address",
      tags = {"Email Address Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "response with an email address identifier",
            response = ResponseDTO.class)
      })
  public ResponseDTO putEmailAddress(@Valid @NotNull String body);
}
