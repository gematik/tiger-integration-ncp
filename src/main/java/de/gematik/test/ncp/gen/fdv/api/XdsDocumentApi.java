/*
 * Copyright 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.gen.fdv.api;

import de.gematik.test.ncp.gen.fdv.model.FindObjectsRequestDTO;
import de.gematik.test.ncp.gen.fdv.model.FindObjectsResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.ObjectSelectorList;
import de.gematik.test.ncp.gen.fdv.model.ReplaceDocumentRequestDTO;
import de.gematik.test.ncp.gen.fdv.model.ResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.RetrieveDocumentsResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.StoreDocumentRequestDTO;
import de.gematik.test.ncp.gen.fdv.model.UpdateMetadataRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.ws.rs.*;
import org.apache.cxf.jaxrs.ext.multipart.*;

/**
 * I_Test_Driver_FdV
 *
 * <p>REST Test Interface for the Insurant´s User Frontend (FdV)
 */
@Path("/epa/testdriver/api/v1")
@Api(value = "/", description = "")
public interface XdsDocumentApi {

  /**
   * Delete documents or folders in the health record
   *
   * <p>This operation executes the use case \&quot;Delete documents\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId. The documents to delete are referenced by their id, found before
   * by finddocuments.
   */
  @DELETE
  @Path("/objects")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Delete documents or folders in the health record",
      tags = {"XDS Document"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO deleteObjects(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId,
      @Valid ObjectSelectorList objectSelectorList);

  /**
   * Find documents, folders, submission sets in a health record
   *
   * <p>This operation executes the use case \&quot;Find documents/folders\&quot; in an existing
   * user session. By default, the health record of the user is addressed, otherwise the health
   * record is identified by recordId. Depending on the value of the returnType whole metadata
   * (LeafClass) of found registry objects or references only (ObjectRef) should be returned. The
   * parameters startIndex and maxResults are used for paging. The parameter \&quot;query\&quot;
   * determines the Stored Query which to use. If not set, the FdV has to choose one appropriate.
   */
  @POST
  @Path("/findObjects")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Find documents, folders, submission sets in a health record",
      tags = {"XDS Document"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = FindObjectsResponseDTO.class)
      })
  public FindObjectsResponseDTO findObjects1(@Valid FindObjectsRequestDTO findObjectsRequestDTO);

  /**
   * Replace document
   *
   * <p>This operation executes the use case \&quot;Replace document\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @POST
  @Path("/replaceDocument")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Replace document",
      tags = {"XDS Document"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO replaceDocument(@Valid ReplaceDocumentRequestDTO replaceDocumentRequestDTO);

  /**
   * Download documents from health record
   *
   * <p>This operation executes the use case \&quot;Download documents\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId. The documents to download are referenced by their id, found
   * before by finddocuments.
   */
  @GET
  @Path("/documents")
  @Produces({"application/json"})
  @ApiOperation(
      value = "Download documents from health record",
      tags = {"XDS Document"})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "default response",
            response = RetrieveDocumentsResponseDTO.class)
      })
  public RetrieveDocumentsResponseDTO retrieveDocuments(
      @QueryParam("recordId") @Pattern(regexp = "^[A-Z]{1}\\d{9}$") String recordId,
      @QueryParam("entryUUID") String entryUUID,
      @QueryParam("uniqueId") String uniqueId,
      @QueryParam("title") String title);

  /**
   * Store documents in health record
   *
   * <p>This operation executes the use case \&quot;Store Documents\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @POST
  @Path("/storeDocuments")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Store documents in health record",
      tags = {"XDS Document"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO storeDocuments(@Valid StoreDocumentRequestDTO storeDocumentRequestDTO);

  /**
   * Updates the meta data of documents
   *
   * <p>This operation executes the use case \&quot;Update meta data\&quot; in an existing user
   * session. By default, the health record of the user is addressed, otherwise the health record is
   * identified by \&quot;recordId\&quot;.
   */
  @POST
  @Path("/updateMetadata")
  @Consumes({"application/json"})
  @Produces({"application/json"})
  @ApiOperation(
      value = "Updates the meta data of documents",
      tags = {"XDS Document"})
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "default response", response = ResponseDTO.class)
      })
  public ResponseDTO updateMetadata1(@Valid UpdateMetadataRequestDTO updateMetadataRequestDTO);
}
