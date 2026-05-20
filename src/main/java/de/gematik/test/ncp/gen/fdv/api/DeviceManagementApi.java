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

import de.gematik.test.ncp.gen.fdv.model.GetRegisteredDevicesResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.PostConfirmPendingDeviceRequest;
import de.gematik.test.ncp.gen.fdv.model.PostRegisterDeviceDefaultResponse;
import de.gematik.test.ncp.gen.fdv.model.PostRegisterDeviceRequest;
import de.gematik.test.ncp.gen.fdv.model.PutRegisteredDeviceRequestDTO;
import de.gematik.test.ncp.gen.fdv.model.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import java.util.UUID;
import org.apache.cxf.jaxrs.ext.multipart.*;

/**
 * I_Test_Driver_FdV
 *
 * <p>REST Test Interface for the Insurant´s User Frontend (FdV)
 */
@Path("/epa/testdriver/api/v1/devicemanagement")
@Api(value = "/", description = "")
public interface DeviceManagementApi {

  /**
   * Delete a registered devices for a user (associated to kvnr of requestor).
   *
   * <p>This operation executes the use case \&quot;Delete a registered device\&quot; in an existing
   * user session. By default, the health record of the user is addressed, otherwise the health
   * record is identified by \&quot;recordId\&quot;.
   */
  @DELETE
  @Path("/{deviceidentifier}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Delete a registered devices for a user (associated to kvnr of requestor).",
      tags = {"Device Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO deleteRegisteredDevice(@PathParam("deviceidentifier") UUID deviceidentifier);

  /**
   * Get a list of all devices registered for a user (associated to kvnr of requestor).
   *
   * <p>This operation executes the use case \&quot;List all registered devices\&quot; in an
   * existing user session.
   */
  @GET
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get a list of all devices registered for a user (associated to kvnr of requestor).",
      tags = {"Device Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = GetRegisteredDevicesResponseDTO.class)
      })
  public GetRegisteredDevicesResponseDTO getRegisteredDevices();

  /**
   * Enter device registration code (Confirmation Code)
   *
   * <p>This request sends the confirmationCode to the Device Management Service, to confirm a
   * pending device registration. If the confirmation code was wrong, the number of remaining
   * retries is returned in the ResponseDTO.statusMessage.
   */
  @POST
  @Path("/confirmPendingDevice")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Enter device registration code (Confirmation Code)",
      tags = {"Device Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "ResponseDTO with success == true OR when the confirmation code was wrong, with success == false and statusMessage with the response from the Device Management with errorCode == invalidCode and errorDetail with the number of remaining retries.",
            response = ResponseDTO.class)
      })
  public ResponseDTO postConfirmPendingDevice(
      @Valid @NotNull PostConfirmPendingDeviceRequest postConfirmPendingDeviceRequest);

  /**
   * Get a new device registration
   *
   * <p>This operation initializes the registration of a new device with the Device Management
   */
  @POST
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Get a new device registration",
      tags = {"Device Management"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default",
            response = PostRegisterDeviceDefaultResponse.class)
      })
  public PostRegisterDeviceDefaultResponse postRegisterDevice(
      @Valid PostRegisterDeviceRequest postRegisterDeviceRequest);

  /**
   * Change display name for registered device
   *
   * <p>This operation executes the use case \&quot;Change display name for registered device\&quot;
   * in an existing user session.
   */
  @PUT
  @Path("/{deviceidentifier}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Change display name for registered device",
      tags = {"Device Management"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO putRegisteredDevice(
      @PathParam("deviceidentifier") UUID deviceidentifier,
      @Valid @NotNull PutRegisteredDeviceRequestDTO putRegisteredDeviceRequestDTO);
}
