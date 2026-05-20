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
 * Request um Dokumente in ein Aktenkonto einzustellen. Das Aktenkonto wird dabei durch die
 * übergebene KVNR identifiziert.
 */
@ApiModel(
    description =
        "Request um Dokumente in ein Aktenkonto einzustellen. Das Aktenkonto wird dabei durch die übergebene KVNR identifiziert.")
public class PutDocumentsRequestDTO {

  @ApiModelProperty(
      required = true,
      value =
          "Identifikationsnummer (KVNR) des Aktenkonto. Für die Herausgeberidentifikation (Root) wird immer der Wert 1.2.276.0.76.4.8 verwendet")
  /**
   * Identifikationsnummer (KVNR) des Aktenkonto. Für die Herausgeberidentifikation (Root) wird
   * immer der Wert 1.2.276.0.76.4.8 verwendet
   */
  private String kvnr;

  @ApiModelProperty(
      value =
          "Liste der einzustellenden Dokumente inklusive Metadaten und Folderinformationen.Folderinformationen müssen nur übergeben werden, wenn ein Dokument in einen neuen oder bestehenden Folder eingestellt werden soll.Dabei gilt: Ist die entryUUID des Folders gesetzt, ist das Dokument in einen bestehenden Folder einzustellen, enthält die codeList mindestens einen Wert ist ein neuer Folder anzulegen, trifft keines von beiden zu, sind die Folder Metadaten ungültig.")
  /**
   * Liste der einzustellenden Dokumente inklusive Metadaten und
   * Folderinformationen.Folderinformationen müssen nur übergeben werden, wenn ein Dokument in einen
   * neuen oder bestehenden Folder eingestellt werden soll.Dabei gilt: Ist die entryUUID des Folders
   * gesetzt, ist das Dokument in einen bestehenden Folder einzustellen, enthält die codeList
   * mindestens einen Wert ist ein neuer Folder anzulegen, trifft keines von beiden zu, sind die
   * Folder Metadaten ungültig.
   */
  private List<Document> documentSets = new ArrayList<>();

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

  public PutDocumentsRequestDTO kvnr(String kvnr) {
    this.kvnr = kvnr;
    return this;
  }

  /**
   * Liste der einzustellenden Dokumente inklusive Metadaten und
   * Folderinformationen.Folderinformationen müssen nur übergeben werden, wenn ein Dokument in einen
   * neuen oder bestehenden Folder eingestellt werden soll.Dabei gilt: Ist die entryUUID des Folders
   * gesetzt, ist das Dokument in einen bestehenden Folder einzustellen, enthält die codeList
   * mindestens einen Wert ist ein neuer Folder anzulegen, trifft keines von beiden zu, sind die
   * Folder Metadaten ungültig.
   *
   * @return documentSets
   */
  @JsonProperty("documentSets")
  public List<Document> getDocumentSets() {
    return documentSets;
  }

  public void setDocumentSets(List<Document> documentSets) {
    this.documentSets = documentSets;
  }

  public PutDocumentsRequestDTO documentSets(List<Document> documentSets) {
    this.documentSets = documentSets;
    return this;
  }

  public PutDocumentsRequestDTO addDocumentSetsItem(Document documentSetsItem) {
    this.documentSets.add(documentSetsItem);
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
    PutDocumentsRequestDTO putDocumentsRequestDTO = (PutDocumentsRequestDTO) o;
    return Objects.equals(this.kvnr, putDocumentsRequestDTO.kvnr)
        && Objects.equals(this.documentSets, putDocumentsRequestDTO.documentSets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kvnr, documentSets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PutDocumentsRequestDTO {\n");

    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
    sb.append("    documentSets: ").append(toIndentedString(documentSets)).append("\n");
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
