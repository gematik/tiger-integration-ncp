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

package de.gematik.test.ncp.gen.erp.fdv.api;

import de.gematik.test.ncp.gen.erp.fdv.model.AuditEvent;
import de.gematik.test.ncp.gen.erp.fdv.model.Communication;
import de.gematik.test.ncp.gen.erp.fdv.model.Consent;
import de.gematik.test.ncp.gen.erp.fdv.model.ConsentCategory;
import de.gematik.test.ncp.gen.erp.fdv.model.EUAccessAuthorization;
import de.gematik.test.ncp.gen.erp.fdv.model.ErpTestdriverApiV1InfoGet400Response;
import de.gematik.test.ncp.gen.erp.fdv.model.ErpTestdriverApiV1LoginPut400Response;
import de.gematik.test.ncp.gen.erp.fdv.model.ErpTestdriverApiV1MedicationdispenseGet200ResponseInner;
import de.gematik.test.ncp.gen.erp.fdv.model.ErpTestdriverApiV1PharmacyAssignmentPostRequest;
import de.gematik.test.ncp.gen.erp.fdv.model.ErpTestdriverApiV1PrescriptionIdPatchRequest;
import de.gematik.test.ncp.gen.erp.fdv.model.Info;
import de.gematik.test.ncp.gen.erp.fdv.model.LoginSuccess;
import de.gematik.test.ncp.gen.erp.fdv.model.OperationOutcome;
import de.gematik.test.ncp.gen.erp.fdv.model.Pharmacy;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import io.swagger.annotations.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.UUID;

/** Represents a collection of functions to interact with the API endpoints. */
@Path("/erp/testdriver/api/v1")
@Api(description = "the erp API")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public interface ErpApi {

  /**
   * UC: Protokolldaten anzeigen See
   * https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#einsicht-in-das-zugriffsprotokoll
   *
   * @return A list (max. 50 entries) of audit events, sorted in descending order by creation date
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @GET
  @Path("/auditEvents")
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Operation returns a list (max. 50 entries) of audit events, sorted in descending order by creation date",
      notes =
          "UC: Protokolldaten anzeigen See https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#einsicht-in-das-zugriffsprotokoll",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Audit Events"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "A list (max. 50 entries) of audit events, sorted in descending order by creation date",
            response = AuditEvent.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class,
            responseContainer = "List")
      })
  Response erpTestdriverApiV1AuditEventsGet();

  /**
   * UC: Nachrichten von Apotheke anzeigen
   * https://github.com/gematik/api-erp/blob/master/docs/erp_communication.adoc#anwendungsfall-alle-nachrichten-vom-e-rezept-fachdienst-abrufen
   *
   * @return
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @GET
  @Path("/communication")
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Operation returns a list (max. 50 entries) of communications for a patient, sorted in descending order by creation date",
      notes =
          "UC: Nachrichten von Apotheke anzeigen https://github.com/gematik/api-erp/blob/master/docs/erp_communication.adoc#anwendungsfall-alle-nachrichten-vom-e-rezept-fachdienst-abrufen",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Communication"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "",
            response = Communication.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class,
            responseContainer = "List")
      })
  Response erpTestdriverApiV1CommunicationGet();

  /**
   * UC: Nachrichten löschen
   * https://github.com/gematik/api-erp/blob/master/docs/erp_communication.adoc#anwendungsfall-nachricht-als-apotheke-l%C3%B6schen
   *
   * @param id
   * @return Operation was successful
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @DELETE
  @Path("/communication/{id}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to delete a communication by id",
      notes =
          "UC: Nachrichten löschen https://github.com/gematik/api-erp/blob/master/docs/erp_communication.adoc#anwendungsfall-nachricht-als-apotheke-l%C3%B6schen",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Communication"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Operation was successful", response = Void.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1CommunicationIdDelete(@PathParam("id") UUID id);

  /**
   * @param category
   * @return Operation was successful
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @DELETE
  @Path("/consent")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to delete a consent for a patient by category",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Consent", "EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Operation was successful", response = Void.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1ConsentDelete(@QueryParam("category") ConsentCategory category);

  /**
   * @param category
   * @return Consent for storing billing information or using e-prescriptions within the EU.
   * @return Forbidden - you are not allowed to do this
   * @return Consent not found for current patient and category
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @GET
  @Path("/consent")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to retrieve a consent for a patient by category",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Consent", "EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "Consent for storing billing information or using e-prescriptions within the EU.",
            response = Consent.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 404,
            message = "Consent not found for current patient and category",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1ConsentGet(@QueryParam("category") ConsentCategory category);

  /**
   * @param category
   * @return Consent for storing billing information or using e-prescriptions within the EU.
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   * @return Consent could not be created due to a conflict
   */
  @POST
  @Path("/consent")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to create a consent for a patient by category",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Consent", "EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 201,
            message =
                "Consent for storing billing information or using e-prescriptions within the EU.",
            response = Consent.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class),
        @ApiResponse(
            code = 409,
            message = "Consent could not be created due to a conflict",
            response = OperationOutcome.class)
      })
  Response erpTestdriverApiV1ConsentPost(@QueryParam("category") ConsentCategory category);

  /**
   * @return Operation was successful
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @DELETE
  @Path("/eu-access-authorization")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to revoke a EU-AccessAuthorization for a patient",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Operation was successful", response = Void.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1EuAccessAuthorizationDelete();

  /**
   * @return A access authorization for a country
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @GET
  @Path("/eu-access-authorization")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to retrieve a EU-AccessAuthorization for a patient",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "A access authorization for a country",
            response = EUAccessAuthorization.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1EuAccessAuthorizationGet();

  /**
   * @param country
   * @param accessCode The Remote FdV have to generate the access code if no access code was
   *     delivered
   * @return A access authorization for a country
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @POST
  @Path("/eu-access-authorization")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to grant a EU-AccessAuthorization for a country",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 201,
            message = "A access authorization for a country",
            response = EUAccessAuthorization.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1EuAccessAuthorizationPost(
      @QueryParam("country") String country,
      @QueryParam("accessCode")
          @ApiParam(
              "The Remote FdV have to generate the access code if no access code was delivered")
          String accessCode);

  /**
   * @return Test driver interface is ready for use
   * @return Error processing the request with the test driver
   * @return Forbidden - you are not allowed to do this
   */
  @GET
  @Path("/info")
  @Produces({"application/json"})
  @ApiOperation(
      value = "General information about this test driver interface and the test FdV",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Info"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Test driver interface is ready for use",
            response = Info.class),
        @ApiResponse(
            code = 400,
            message = "Error processing the request with the test driver",
            response = ErpTestdriverApiV1InfoGet400Response.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class)
      })
  Response erpTestdriverApiV1InfoGet();

  /**
   * @param body
   * @return
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   * @return Forbidden - you are not allowed to do this
   */
  @PUT
  @Path("/login")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to login with an kvnr",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Login"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "", response = LoginSuccess.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class)
      })
  Response erpTestdriverApiV1LoginPut(String body);

  /**
   * The response contains information on dispenses both in Germany and in other EU countries.
   *
   * @param whenhandedover
   * @return A list of all dispense information for the patient
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @GET
  @Path("/medicationdispense")
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Operation returns a list (max. 50 entries) of medication dispense information's, sorted in descending order by creation date",
      notes =
          "The response contains information on dispenses both in Germany and in other EU countries.",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Dispensing information", "EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "A list of all dispense information for the patient",
            response = ErpTestdriverApiV1MedicationdispenseGet200ResponseInner.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class,
            responseContainer = "List")
      })
  Response erpTestdriverApiV1MedicationdispenseGet(
      @QueryParam("whenhandedover") String whenhandedover);

  /**
   * Operation generates e-prescription token as 2D code
   *
   * @param body
   * @return Image (PNG) encoded as base64 string
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @POST
  @Path("/pharmacy/2dCode")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "",
      notes = "Operation generates e-prescription token as 2D code",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Pharmacy"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Image (PNG) encoded as base64 string",
            response = byte[].class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1Pharmacy2dCodePost(String body);

  /**
   * Operation is intended to assign an e-prescription to a pharmacy
   *
   * @param erpTestdriverApiV1PharmacyAssignmentPostRequest
   * @return
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @POST
  @Path("/pharmacy/assignment")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "",
      notes = "Operation is intended to assign an e-prescription to a pharmacy",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Pharmacy"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "", response = Communication.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1PharmacyAssignmentPost(
      ErpTestdriverApiV1PharmacyAssignmentPostRequest
          erpTestdriverApiV1PharmacyAssignmentPostRequest);

  /**
   * UC: Apotheke suchen See
   * https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#eine-apotheke-aus-dem-apotheken-verzeichnis-ausw%C3%A4hlen
   *
   * @param near
   * @param addressCity
   * @param name
   * @return A list (max. 50 entries) of pharmacies, sorted in order by name
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @GET
  @Path("/pharmacy/search")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to search for pharmacies based on specific attributes.",
      notes =
          "UC: Apotheke suchen See https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#eine-apotheke-aus-dem-apotheken-verzeichnis-ausw%C3%A4hlen",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Pharmacy"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "A list (max. 50 entries) of pharmacies, sorted in order by name",
            response = Pharmacy.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class,
            responseContainer = "List")
      })
  Response erpTestdriverApiV1PharmacySearchGet(
      @QueryParam("near") String near,
      @QueryParam("address-city") String addressCity,
      @QueryParam("name") String name);

  /**
   * UC: E-Rezepte empfangen See
   * https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#alle-e-rezepte-ansehen
   *
   * @return A list (max. 50 entries) of e-prescriptions, sorted in descending order by creation
   *     date
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   * @return Forbidden - you are not allowed to do this
   */
  @GET
  @Path("/prescription")
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Operation to retrieve a list (max. 50 entries) of e-prescriptions, sorted in descending order by creation date",
      notes =
          "UC: E-Rezepte empfangen See https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#alle-e-rezepte-ansehen",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"E-Prescription", "EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "A list (max. 50 entries) of e-prescriptions, sorted in descending order by creation date",
            response = Prescription.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class,
            responseContainer = "List"),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class,
            responseContainer = "List")
      })
  Response erpTestdriverApiV1PrescriptionGet();

  /**
   * UC: E-Rezepte am E-Rezept Fachdienst löschen See
   * https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#ein-e-rezept-l%C3%B6schen
   *
   * @param id
   * @return Operation was successful
   * @return Forbidden - you are not allowed to do this
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   */
  @DELETE
  @Path("/prescription/{id}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to delete an e-prescription",
      notes =
          "UC: E-Rezepte am E-Rezept Fachdienst löschen See https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#ein-e-rezept-l%C3%B6schen",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"E-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 204, message = "Operation was successful", response = Void.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class)
      })
  Response erpTestdriverApiV1PrescriptionIdDelete(@PathParam("id") String id);

  /**
   * UC: E-Rezepte empfangen See
   * https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#ein-einzelnes-e-rezept-abrufen-und-in-der-apotheke-einl%C3%B6sen
   *
   * @param id
   * @return See
   *     https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#alle-e-prescriptione-ansehen
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   * @return Forbidden - you are not allowed to do this
   */
  @GET
  @Path("/prescription/{id}")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Operation to download a single e-prescription",
      notes =
          "UC: E-Rezepte empfangen See https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#ein-einzelnes-e-rezept-abrufen-und-in-der-apotheke-einl%C3%B6sen",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"E-Prescription", "EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "See https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#alle-e-prescriptione-ansehen",
            response = Prescription.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class)
      })
  Response erpTestdriverApiV1PrescriptionIdGet(@PathParam("id") String id);

  /**
   * In the context of European e-prescriptions, an e-prescription must be activated. This can be
   * controlled via the attribute RedeemableByPatient.
   *
   * @param id
   * @param erpTestdriverApiV1PrescriptionIdPatchRequest
   * @return See
   *     https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#alle-e-prescriptione-ansehen
   * @return The error is used to describe an OperationOutcome or a technical error, e.g. VAU
   *     encryption error
   * @return Forbidden - you are not allowed to do this
   */
  @PATCH
  @Path("/prescription/{id}")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value =
          "Operation to change an e-prescription. Currently, an e-prescription can be activated for redemption in other European countries.",
      notes =
          "In the context of European e-prescriptions, an e-prescription must be activated. This can be controlled via the attribute RedeemableByPatient.",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"E-Prescription", "EU-Prescription"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "See https://github.com/gematik/api-erp/blob/master/docs/erp_versicherte.adoc#alle-e-prescriptione-ansehen",
            response = Prescription.class),
        @ApiResponse(
            code = 400,
            message =
                "The error is used to describe an OperationOutcome or a technical error, e.g. VAU encryption error",
            response = ErpTestdriverApiV1LoginPut400Response.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class)
      })
  Response erpTestdriverApiV1PrescriptionIdPatch(
      @PathParam("id") String id,
      ErpTestdriverApiV1PrescriptionIdPatchRequest erpTestdriverApiV1PrescriptionIdPatchRequest);

  /**
   * @return Operation was successful
   * @return Error processing the request with the test driver
   * @return Forbidden - you are not allowed to do this
   */
  @PUT
  @Path("/start")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Start die Test-FdV",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Info"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Operation was successful", response = Void.class),
        @ApiResponse(
            code = 400,
            message = "Error processing the request with the test driver",
            response = ErpTestdriverApiV1InfoGet400Response.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class)
      })
  Response erpTestdriverApiV1StartPut();

  /**
   * @return Operation was successful
   * @return Error processing the request with the test driver
   * @return Forbidden - you are not allowed to do this
   */
  @PUT
  @Path("/stop")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Stop die Test-FdV",
      notes = "",
      authorizations = {@Authorization(value = "apiKey")},
      tags = {"Info"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Operation was successful", response = Void.class),
        @ApiResponse(
            code = 400,
            message = "Error processing the request with the test driver",
            response = ErpTestdriverApiV1InfoGet400Response.class),
        @ApiResponse(
            code = 403,
            message = "Forbidden - you are not allowed to do this",
            response = Void.class)
      })
  Response erpTestdriverApiV1StopPut();
}
