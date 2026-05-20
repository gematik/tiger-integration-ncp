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

import de.gematik.test.ncp.gen.fdv.model.CategoryIdType;
import de.gematik.test.ncp.gen.fdv.model.LegalPolicyResponseDTO;
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
@Path("/epa/testdriver/api/v1/legalpolicy/{category}")
@Api(value = "/", description = "")
public interface LegalPolicyApi {

  /**
   * Get the principle permission to a category for a healthcare provider
   *
   * <p>This operation reads the Tabelle 17: Legal Policy from the gemSpec_Aktensystem_ePAfueralle #
   * A_19303 for a given LEI (with professionOID) and a data category and returns a list of CRUD
   * rights for the specific combination. The result solely bases on the legal policy and will not
   * consider constraints, blocked user, entitlement or consent decision.
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get the principle permission to a category for a healthcare provider",
      tags = {"Legal Policy"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Response with CRUD rights",
            response = LegalPolicyResponseDTO.class)
      })
  public LegalPolicyResponseDTO getLegalPolicy(
      @PathParam("category") CategoryIdType category,
      @QueryParam("professionOid") @NotNull @Pattern(regexp = "([0-2])((\\.0)|(\\.[1-9][0-9]*))*$")
          String professionOid);
}
