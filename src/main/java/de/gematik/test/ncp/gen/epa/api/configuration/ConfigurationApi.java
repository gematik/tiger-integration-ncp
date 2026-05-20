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

package de.gematik.test.ncp.gen.epa.api.configuration;

import de.gematik.test.ncp.gen.epa.api.configuration.dto.KonnektorConfigurationRequestDTO;
import de.gematik.test.ncp.gen.epa.api.configuration.dto.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.*;

/**
 * ePA SIM Configuration API
 *
 * <p>Configuration of the Konnektor with which to communicate
 */
@Path("/configuration/configureKonnektor")
@Api(value = "/", description = "")
public interface ConfigurationApi {

  /**
   * Operation um die Konfigurationsdaten zum verwendeten Konnektor zur Laufzeit zu ändern
   *
   * <p>Soll zur Laufzeit des epa-ps der verwendete Konnektor geändert werden oder einzelne
   * Konfigurationsdaten für den Konnektor geändert werden, kann das mittels dieser Operation
   * erfolgen.
   */
  @POST
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Operation um die Konfigurationsdaten zum verwendeten Konnektor zur Laufzeit zu ändern",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Statusinformation, ob die Operation erfolgreich ausgeführt werden konnte.",
            response = ResponseDTO.class)
      })
  public ResponseDTO configureKonnektor(
      KonnektorConfigurationRequestDTO konnektorConfigurationRequestDTO);
}
