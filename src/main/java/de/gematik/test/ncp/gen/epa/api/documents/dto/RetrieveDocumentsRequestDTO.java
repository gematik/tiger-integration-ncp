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

/** Request um Dokumente aus einem Aktenkonto abrufen zu können */
@ApiModel(description = "Request um Dokumente aus einem Aktenkonto abrufen zu können")
public class RetrieveDocumentsRequestDTO {

  @ApiModelProperty(
      required = true,
      value =
          "Identifikationsnummer (KVNR) des Aktenkonto. Für die Herausgeberidentifikation (Root) wird immer der Wert 1.2.276.0.76.4.8 verwendet")
  /**
   * Identifikationsnummer (KVNR) des Aktenkonto. Für die Herausgeberidentifikation (Root) wird
   * immer der Wert 1.2.276.0.76.4.8 verwendet
   */
  private String kvnr;

  @ApiModelProperty(required = true, value = "Eine Repository uniqueId der abzurufenden Dokumente")
  /** Eine Repository uniqueId der abzurufenden Dokumente */
  private String repositoryUniqueId;

  @ApiModelProperty(required = true, value = "Liste der uniqueIDs der abzurufenden Dokumente")
  /** Liste der uniqueIDs der abzurufenden Dokumente */
  private List<String> documentUniqueIds = new ArrayList<>();

  /**
   * Identifikationsnummer (KVNR) des Aktenkonto. Für die Herausgeberidentifikation (Root) wird
   * immer der Wert 1.2.276.0.76.4.8 verwendet
   *
   * @return kvnr
   */
  @JsonProperty("kvnr")
  public String getKvnr() {
    return kvnr;
  }

  public void setKvnr(String kvnr) {
    this.kvnr = kvnr;
  }

  public RetrieveDocumentsRequestDTO kvnr(String kvnr) {
    this.kvnr = kvnr;
    return this;
  }

  /**
   * Eine Repository uniqueId der abzurufenden Dokumente
   *
   * @return repositoryUniqueId
   */
  @JsonProperty("repositoryUniqueId")
  public String getRepositoryUniqueId() {
    return repositoryUniqueId;
  }

  public void setRepositoryUniqueId(String repositoryUniqueId) {
    this.repositoryUniqueId = repositoryUniqueId;
  }

  public RetrieveDocumentsRequestDTO repositoryUniqueId(String repositoryUniqueId) {
    this.repositoryUniqueId = repositoryUniqueId;
    return this;
  }

  /**
   * Liste der uniqueIDs der abzurufenden Dokumente
   *
   * @return documentUniqueIds
   */
  @JsonProperty("documentUniqueIds")
  public List<String> getDocumentUniqueIds() {
    return documentUniqueIds;
  }

  public void setDocumentUniqueIds(List<String> documentUniqueIds) {
    this.documentUniqueIds = documentUniqueIds;
  }

  public RetrieveDocumentsRequestDTO documentUniqueIds(List<String> documentUniqueIds) {
    this.documentUniqueIds = documentUniqueIds;
    return this;
  }

  public RetrieveDocumentsRequestDTO addDocumentUniqueIdsItem(String documentUniqueIdsItem) {
    this.documentUniqueIds.add(documentUniqueIdsItem);
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
    RetrieveDocumentsRequestDTO retrieveDocumentsRequestDTO = (RetrieveDocumentsRequestDTO) o;
    return Objects.equals(this.kvnr, retrieveDocumentsRequestDTO.kvnr)
        && Objects.equals(this.repositoryUniqueId, retrieveDocumentsRequestDTO.repositoryUniqueId)
        && Objects.equals(this.documentUniqueIds, retrieveDocumentsRequestDTO.documentUniqueIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kvnr, repositoryUniqueId, documentUniqueIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveDocumentsRequestDTO {\n");

    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
    sb.append("    repositoryUniqueId: ").append(toIndentedString(repositoryUniqueId)).append("\n");
    sb.append("    documentUniqueIds: ").append(toIndentedString(documentUniqueIds)).append("\n");
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
