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

package de.gematik.test.ncp.gen.epa.api.information;

import de.gematik.test.ncp.gen.epa.api.information.dto.GetConsentDecisionInformationResponseDTO;
import de.gematik.test.ncp.gen.epa.api.information.dto.GetRecordStatusResponseDTO;
import de.gematik.test.ncp.gen.epa.api.information.dto.ResponseDTO;
import de.gematik.test.ncp.gen.epa.api.information.dto.SetFqdnRequestDTO;
import de.gematik.test.ncp.gen.epa.api.information.dto.SetUserExperienceResponseDTO;
import de.gematik.test.ncp.gen.epa.api.information.dto.UxRequestType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.*;

/**
 * ePA SIM Information API
 *
 * <p>Information Service related REST API for the ePA Sim based on open API
 */
@Path("/epa/testdriver/api/v1/information")
@Api(value = "/", description = "")
public interface InformationApi {

  /** Get current healthcare process relevant consent decisions */
  @GET
  @Path("/ehr/consentdecisions")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get current healthcare process relevant consent decisions",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Ok.",
            response = GetConsentDecisionInformationResponseDTO.class)
      })
  public GetConsentDecisionInformationResponseDTO getConsentDecisionInformation(
      @HeaderParam("x-insurantid") String xInsurantid);

  /**
   * Get status of a personal health record
   *
   * <p>Check the health record system for an existing health record matching _insurantid_.
   */
  @GET
  @Path("/ehr")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get status of a personal health record",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Response containing the success status and the status message",
            response = GetRecordStatusResponseDTO.class)
      })
  public GetRecordStatusResponseDTO getRecordStatus(
      @HeaderParam("x-insurantid") String xInsurantid);

  /**
   * Sets the FQDN for the given insurantId.
   *
   * <p>This endpoint can be used by test suites to set a FQDN. Especially for testing the use case
   * where the FQDN for a given insurantId does not exist.
   */
  @PUT
  @Path("/fqdn")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Sets the FQDN for the given insurantId.",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Response containing the success status and the status message",
            response = ResponseDTO.class)
      })
  public ResponseDTO setFqdn(SetFqdnRequestDTO setFqdnRequestDTO);

  /**
   * provide measurement for ux use cases
   *
   * <p>Provide measurements for ux use cases.&lt;/br&gt; **Client**&lt;/br&gt; This operation may
   * be invoked without login to any health record. &lt;/br&gt; Clients of health care institutions
   * shall use the use case identifiers: - UX_Login_PS - UX_Doc_Upload_PS - UX_Doc_Download_PS only.
   * The provision of parameter _useragent_ is mandatory. **Provider**&lt;/br&gt; The use case
   * measurements shall be associated to _useragent_ and collected, processed and distributed
   * according requirements defined in specification gemSpec_Perf | Conditions | Status code | Error
   * code | Remarks | |------------|-------------|------------|---------| | Successful operation |
   * 200 ||| | Request does not match schema | 400 | malformedRequest || | Any other error | 500 |
   * internalError | (see &#39;Retry interval&#39;) | &lt;/br&gt; | Postconditions | Remarks |
   * |---------------------------------------|---------| | data collected ||
   */
  @POST
  @Path("/userexperience")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "provide measurement for ux use cases",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Response containing the success status and the status message",
            response = SetUserExperienceResponseDTO.class)
      })
  public SetUserExperienceResponseDTO setUserExperienceResult(UxRequestType uxRequestType);
}
