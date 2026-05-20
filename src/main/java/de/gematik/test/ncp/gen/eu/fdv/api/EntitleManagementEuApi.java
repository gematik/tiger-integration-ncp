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

package de.gematik.test.ncp.gen.eu.fdv.api;

import de.gematik.test.ncp.gen.eu.fdv.model.GetEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehRequestDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import org.apache.cxf.jaxrs.ext.multipart.*;

/**
 * I_Test_Driver_FdV_EU
 *
 * <p>REST Test Interface for the Insurant´s User Frontend (FdV) for use case: authorize EU access.
 */
@Path("/epa/testdriver/api/v1/ncpeh/entitlement")
@Api(value = "/", description = "")
public interface EntitleManagementEuApi {

  /** Delete entitlement for ncpeh */
  @DELETE
  @Produces({"application/json"})
  @ApiOperation(
      value = "Delete entitlement for ncpeh",
      tags = {"Entitle Management EU"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response for deleteEntitlementNcpeh",
            response = ResponseDTO.class)
      })
  public ResponseDTO deleteEntitlementNcpeh(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /** Get entitlement for ncpeh */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get entitlement for ncpeh",
      tags = {"Entitle Management EU"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response for getEntitlementNcpeh",
            response = GetEntitlementNcpehResponseDTO.class)
      })
  public GetEntitlementNcpehResponseDTO getEntitlementNcpeh(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /** Set entitlement for ncpeh */
  @PUT
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Set entitlement for ncpeh",
      tags = {"Entitle Management EU"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response for postEntitlementNcpeh",
            response = PutEntitlementNcpehResponseDTO.class)
      })
  public PutEntitlementNcpehResponseDTO putEntitlementNcpeh(
      @Valid @NotNull PutEntitlementNcpehRequestDTO putEntitlementNcpehRequestDTO);
}
