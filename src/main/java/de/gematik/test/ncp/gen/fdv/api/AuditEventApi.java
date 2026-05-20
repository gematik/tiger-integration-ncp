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

import de.gematik.test.ncp.gen.fdv.model.GetAuditEventListAsPdfResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.GetAuditEventResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import java.util.Date;
import org.apache.cxf.jaxrs.ext.multipart.*;

/**
 * I_Test_Driver_FdV
 *
 * <p>REST Test Interface for the Insurant´s User Frontend (FdV)
 */
@Path("/epa/testdriver/api/v1/audit")
@Api(value = "/", description = "")
public interface AuditEventApi {

  /**
   * Get an audit event list of a personal health record as FHIR objects.
   *
   * <p>This operation executes the use case \&quot;List Audit Events\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;. The Audit Events provided by the health record system are
   * delivered.
   */
  @GET
  @Produces({"application/fhir+json"})
  @ApiOperation(
      value = "Get an audit event list of a personal health record as FHIR objects.",
      tags = {"Audit Event"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "A list of resources",
            response = GetAuditEventResponseDTO.class)
      })
  public GetAuditEventResponseDTO getAuditEventListAsFHIRJson(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId,
      @QueryParam("sinceLastLogin") Boolean sinceLastLogin,
      @QueryParam("since") Date since,
      @QueryParam("until") Date until);

  /**
   * Get an audit event list rendered as PDF/A of a personal health record.
   *
   * <p>This operation executes the use case \&quot;List all audit events\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @GET
  @Path("/render/eml/pdf")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get an audit event list rendered as PDF/A of a personal health record.",
      tags = {"Audit Event"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "audit event list as PDF/A",
            response = GetAuditEventListAsPdfResponseDTO.class)
      })
  public GetAuditEventListAsPdfResponseDTO getAuditEventListAsPdf(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);
}
