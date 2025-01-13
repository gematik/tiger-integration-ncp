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

import de.gematik.test.ncp.gen.fdv.model.OrganizationList;
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
@Path("/epa/testdriver/api/v1/find")
@Api(value = "/", description = "")
public interface DirectoryApi {

  /**
   * Search for organization at FHIR-VZD
   *
   * <p>This operation executes a search for an organization at the FHIR-VZD in an existing user
   * session.
   */
  @GET
  @Produces({"application/fhir+json", "application/json"})
  @ApiOperation(
      value = "Search for organization at FHIR-VZD",
      tags = {"Directory"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "A list of resources",
            response = OrganizationList.class),
        @ApiResponse(code = 200, message = "NOK", response = ResponseDTO.class)
      })
  public OrganizationList find(
      @QueryParam("type") @NotNull String type,
      @QueryParam("name") String name,
      @QueryParam("alias") String alias);
}
