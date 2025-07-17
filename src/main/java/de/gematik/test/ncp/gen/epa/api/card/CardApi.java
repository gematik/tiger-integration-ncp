/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.gen.epa.api.card;

import de.gematik.test.ncp.gen.epa.api.card.dto.GetCardsInfoResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.*;

/**
 * ePA SIM Card API
 *
 * <p>Card related REST API for the ePA Sim based on open API
 */
@Path("/epa/testdriver/api/v1/cards")
@Api(value = "/", description = "")
public interface CardApi {

  /**
   * Get cards information
   *
   * <p>Get cards information
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get cards information",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Response containing the success status and the status message",
            response = GetCardsInfoResponseDTO.class)
      })
  public GetCardsInfoResponseDTO getCardsInfo();
}
