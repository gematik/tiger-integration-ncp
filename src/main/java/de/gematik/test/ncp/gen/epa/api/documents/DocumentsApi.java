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

package de.gematik.test.ncp.gen.epa.api.documents;

import de.gematik.test.ncp.gen.epa.api.documents.dto.DeleteObjectsRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.FindObjectsResponseDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.FindRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.PutDocumentsRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.ReplaceDocumentsRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.ResponseDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.RetrieveDocumentsRequestDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.RetrieveDocumentsResponseDTO;
import de.gematik.test.ncp.gen.epa.api.documents.dto.UpdateDocumentsRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.ws.rs.*;

/**
 * ePA SIM Documents API
 *
 * <p>Documents management in AktenSystem
 */
@Path("/document")
@Api(value = "/", description = "")
public interface DocumentsApi {

  /**
   * Dokument(e) in einem Aktenkonto löschen
   *
   * <p>Mittels dieser Operation können Dokumente in einem Aktenkonto gelöscht werden
   */
  @POST
  @Path("/deleteObjects")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Dokument(e) in einem Aktenkonto löschen",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Statusinformation, ob die Operation erfolgreich ausgeführt werden konnte.",
            response = ResponseDTO.class)
      })
  public ResponseDTO deleteObjects(
      @HeaderParam("x-insurantid") String xInsurantid,
      DeleteObjectsRequestDTO deleteObjectsRequestDTO);

  /**
   * Objekte in einem Aktenkonto finden
   *
   * <p>Mittels dieser Operation können Dokumente, Folder, SubmissionSets und Associations in einem
   * Aktenkonto gefunden werden und deren Metadaten zurückgegeben werden
   */
  @POST
  @Path("/find")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Objekte in einem Aktenkonto finden",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "Metadaten zu den gefundenen Objekten oder ggf. Statusinformationen zu aufgetretenen Fehlern.",
            response = FindObjectsResponseDTO.class)
      })
  public FindObjectsResponseDTO find(
      @HeaderParam("x-insurantid") String xInsurantid, FindRequestDTO findRequestDTO);

  /**
   * Dokument(e) aus dem Aktenkonto abrufen
   *
   * <p>Mittels dieser Operation können Dokumente aus einem Aktenkonto abgerufen werden
   */
  @POST
  @Path("/getDocuments")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Dokument(e) aus dem Aktenkonto abrufen",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message =
                "Die abgerufenen Dokumente oder ggf. Statusinformationen, wenn Fehler aufgetreten sind.",
            response = RetrieveDocumentsResponseDTO.class)
      })
  public RetrieveDocumentsResponseDTO getDocuments(
      @HeaderParam("x-insurantid") String xInsurantid,
      RetrieveDocumentsRequestDTO retrieveDocumentsRequestDTO);

  /**
   * Dokument(e) in ein Aktenkonto einstellen
   *
   * <p>Mittels dieser Operation können Dokumente, ggf. mit Folder oder Referenz auf einen
   * bestehenden Folder in eine Aktenkonto (identifiziert mittels KVNR) eingestellt werden.
   */
  @POST
  @Path("/putDocuments")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Dokument(e) in ein Aktenkonto einstellen",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Statusinformation, ob die Operation erfolgreich ausgeführt werden konnte.",
            response = ResponseDTO.class)
      })
  public ResponseDTO putDocuments(
      @HeaderParam("x-insurantid") String xInsurantid,
      PutDocumentsRequestDTO putDocumentsRequestDTO);

  /**
   * Dokument(e) in einem Aktenkonto ersetzen
   *
   * <p>Mittels dieser Operation können Dokumente in einem Aktenkonto ersetzt werden
   */
  @POST
  @Path("/replaceDocuments")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Dokument(e) in einem Aktenkonto ersetzen",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Statusinformation, ob die Operation erfolgreich ausgeführt werden konnte.",
            response = ResponseDTO.class)
      })
  public ResponseDTO replaceDocuments(
      @HeaderParam("x-insurantid") String xInsurantid,
      ReplaceDocumentsRequestDTO replaceDocumentsRequestDTO);

  /**
   * Dokument(e) in ein Aktenkonto aktualisieren
   *
   * <p>Mittels dieser Operation können Dokumente in eine Aktenkonto (identifiziert mittels KVNR)
   * aktualisiert werden.
   */
  @POST
  @Path("/updateMetadata")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Dokument(e) in ein Aktenkonto aktualisieren",
      tags = {})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Statusinformation, ob die Operation erfolgreich ausgeführt werden konnte.",
            response = ResponseDTO.class)
      })
  public ResponseDTO updateDocuments(
      @HeaderParam("x-insurantid") String xInsurantid,
      UpdateDocumentsRequestDTO updateDocumentsRequestDTO);
}
