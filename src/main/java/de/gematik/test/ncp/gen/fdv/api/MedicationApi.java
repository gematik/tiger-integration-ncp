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

import de.gematik.test.ncp.gen.fdv.model.GetMedicationListAsPdfResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.GetMedicationListAsXhtmlResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.GetMedicationResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import org.apache.cxf.jaxrs.ext.multipart.*;
import org.joda.time.LocalDate;

/**
 * I_Test_Driver_FdV
 *
 * <p>REST Test Interface for the Insurant´s User Frontend (FdV)
 */
@Path("/epa/testdriver/api/v1/medication")
@Api(value = "/", description = "")
public interface MedicationApi {

  /**
   * Get a medication list of a personal health record as FHIR objects. Implementation is optional
   * as explained in A_24947*.
   *
   * <p>This operation executes the use case \&quot;List all medications\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @GET
  @Produces({"application/fhir+json"})
  @ApiOperation(
      value =
          "Get a medication list of a personal health record as FHIR objects. Implementation is optional as explained in A_24947*.",
      tags = {"Medication"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "A list of resources",
            response = GetMedicationResponseDTO.class)
      })
  public GetMedicationResponseDTO getMedicationListAsFHIRJson(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId,
      @QueryParam("count") Integer count,
      @QueryParam("offset") Integer offset,
      @QueryParam("total") String total,
      @QueryParam("id") String id,
      @QueryParam("lastUpdated") LocalDate lastUpdated,
      @QueryParam("identifier") String identifier,
      @QueryParam("code") String code,
      @QueryParam("status") String status);

  /**
   * Get a medication list (eML) rendered as PDF/A of a personal health record.
   *
   * <p>This operation executes the use case \&quot;List all medications\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @GET
  @Path("/render/eml/pdf")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get a medication list (eML) rendered as PDF/A of a personal health record.",
      tags = {"Medication"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "eML as PDF/A",
            response = GetMedicationListAsPdfResponseDTO.class)
      })
  public GetMedicationListAsPdfResponseDTO getMedicationListAsPdf(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);

  /**
   * Get a medication list (eML) rendered in xHTML of a personal health record.
   *
   * <p>This operation executes the use case \&quot;List all medications\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @GET
  @Path("/render/eml/xhtml")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get a medication list (eML) rendered in xHTML of a personal health record.",
      tags = {"Medication"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "eML as XHTML",
            response = GetMedicationListAsXhtmlResponseDTO.class)
      })
  public GetMedicationListAsXhtmlResponseDTO getMedicationListAsXhtml(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId);
}
