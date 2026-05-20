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

package de.gematik.test.ncp.gen.epa.api.documents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Response, der die abgerufenen Dokumente enthält, oder Fehlerinformationen, für den Fall, dass ein
 * Fehler aufgetreten ist
 */
@ApiModel(
    description =
        "Response, der die abgerufenen Dokumente enthält, oder Fehlerinformationen, für den Fall, dass ein Fehler aufgetreten ist")
public class RetrieveDocumentsResponseDTO {

  @ApiModelProperty(
      required = true,
      value =
          "Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte")
  /**
   * Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte
   */
  private Boolean success;

  @ApiModelProperty(
      value =
          "Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.")
  /**
   * Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.
   */
  private String statusMessage;

  @ApiModelProperty(
      value =
          "Liste der abgerufenen Dokumente. Zu jedem Dokument (Base64 kodiert) sind auch uniqueId und mimeType enthalten. Leer, wenn keine Dokumente abgerufen werden konnten.")
  /**
   * Liste der abgerufenen Dokumente. Zu jedem Dokument (Base64 kodiert) sind auch uniqueId und
   * mimeType enthalten. Leer, wenn keine Dokumente abgerufen werden konnten.
   */
  private List<RetrieveDocumentElement> documents = new ArrayList<>();

  /**
   * Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte
   *
   * @return success
   */
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public RetrieveDocumentsResponseDTO success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.
   *
   * @return statusMessage
   */
  @JsonProperty("statusMessage")
  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public RetrieveDocumentsResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Liste der abgerufenen Dokumente. Zu jedem Dokument (Base64 kodiert) sind auch uniqueId und
   * mimeType enthalten. Leer, wenn keine Dokumente abgerufen werden konnten.
   *
   * @return documents
   */
  @JsonProperty("documents")
  public List<RetrieveDocumentElement> getDocuments() {
    return documents;
  }

  public void setDocuments(List<RetrieveDocumentElement> documents) {
    this.documents = documents;
  }

  public RetrieveDocumentsResponseDTO documents(List<RetrieveDocumentElement> documents) {
    this.documents = documents;
    return this;
  }

  public RetrieveDocumentsResponseDTO addDocumentsItem(RetrieveDocumentElement documentsItem) {
    this.documents.add(documentsItem);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RetrieveDocumentsResponseDTO retrieveDocumentsResponseDTO = (RetrieveDocumentsResponseDTO) o;
    return Objects.equals(this.success, retrieveDocumentsResponseDTO.success)
        && Objects.equals(this.statusMessage, retrieveDocumentsResponseDTO.statusMessage)
        && Objects.equals(this.documents, retrieveDocumentsResponseDTO.documents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, documents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveDocumentsResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    documents: ").append(toIndentedString(documents)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
