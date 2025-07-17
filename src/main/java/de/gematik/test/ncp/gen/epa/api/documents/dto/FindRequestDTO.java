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

package de.gematik.test.ncp.gen.epa.api.documents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Request um den Suchtyp und die Suchparameter für die Objektsuche in einem Aktenkonto zu übergeben
 */
@ApiModel(
    description =
        "Request um den Suchtyp und die Suchparameter für die Objektsuche in einem Aktenkonto zu übergeben")
public class FindRequestDTO {

  @ApiModelProperty(
      required = true,
      value =
          "Identifikationsnummer (KVNR) des Aktenkonto. Für die Herausgeberidentifikation (Root) wird immer der Wert 1.2.276.0.76.4.8 verwendet")
  /**
   * Identifikationsnummer (KVNR) des Aktenkonto. Für die Herausgeberidentifikation (Root) wird
   * immer der Wert 1.2.276.0.76.4.8 verwendet
   */
  private String kvnr;

  public enum QueryEnum {
    FIND_DOCUMENTS(String.valueOf("FindDocuments")),
    FIND_DOCUMENTS_BY_TITLE(String.valueOf("FindDocumentsByTitle")),
    FIND_FOLDERS(String.valueOf("FindFolders")),
    GET_ALL(String.valueOf("GetAll")),
    GET_FOLDER_AND_CONTENTS(String.valueOf("GetFolderAndContents")),
    GET_RELATED_APPROVED_DOCUMENTS(String.valueOf("GetRelatedApprovedDocuments")),
    FIND_DOCUMENTS_BY_COMMENT(String.valueOf("FindDocumentsByComment")),
    FIND_DOCUMENTS_BY_REFERENCE_ID(String.valueOf("FindDocumentsByReferenceId"));

    private String value;

    QueryEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static QueryEnum fromValue(String value) {
      for (QueryEnum b : QueryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(
      required = true,
      value =
          "Art der Suche. Unterstützt werden Suche nach Dokumenten (FindDocuments, FindDocumentsByComment, FindDocumentsByReferenceId, FindDocumentsByTitle), der aktuellen Version zu einem Dokument (GetRelatedApprovedDocuments), nach Foldern (FindFolders) und ihren Inhalten (GetFolderAndContents), sowie nach allen Objekttypen (GetAll).")
  /**
   * Art der Suche. Unterstützt werden Suche nach Dokumenten (FindDocuments, FindDocumentsByComment,
   * FindDocumentsByReferenceId, FindDocumentsByTitle), der aktuellen Version zu einem Dokument
   * (GetRelatedApprovedDocuments), nach Foldern (FindFolders) und ihren Inhalten
   * (GetFolderAndContents), sowie nach allen Objekttypen (GetAll).
   */
  private QueryEnum query;

  @ApiModelProperty(
      example =
          "{\"$XDSDocumentEntryStatus\":[\"urn:oasis:names:tc:ebxml-regrep:StatusType:Approved\"],\"$XDSDocumentEntryEntryUUID\":[\"urn:uuid:8299ca9b-065f-43fa-8fc0-e94f42726328\"]}",
      required = true,
      value = "Liste der Queryparameter und der dafür zu setzenden Werte.")
  /** Liste der Queryparameter und der dafür zu setzenden Werte. */
  private Map<String, List<String>> queryData = new HashMap<>();

  public enum ReturnTypeEnum {
    LEAF_CLASS(String.valueOf("LeafClass")),
    OBJECT_REF(String.valueOf("ObjectRef"));

    private String value;

    ReturnTypeEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ReturnTypeEnum fromValue(String value) {
      for (ReturnTypeEnum b : ReturnTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(
      required = true,
      value =
          "Art der Rückgabe der Suchergebnisse, als Liste von Objektreferenzen (ObjectRef) oder als vollständige Metadaten (LeafClass)")
  /**
   * Art der Rückgabe der Suchergebnisse, als Liste von Objektreferenzen (ObjectRef) oder als
   * vollständige Metadaten (LeafClass)
   */
  private ReturnTypeEnum returnType;

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

  public FindRequestDTO kvnr(String kvnr) {
    this.kvnr = kvnr;
    return this;
  }

  /**
   * Art der Suche. Unterstützt werden Suche nach Dokumenten (FindDocuments, FindDocumentsByComment,
   * FindDocumentsByReferenceId, FindDocumentsByTitle), der aktuellen Version zu einem Dokument
   * (GetRelatedApprovedDocuments), nach Foldern (FindFolders) und ihren Inhalten
   * (GetFolderAndContents), sowie nach allen Objekttypen (GetAll).
   *
   * @return query
   */
  @JsonProperty("query")
  public String getQuery() {
    if (query == null) {
      return null;
    }
    return query.value();
  }

  public void setQuery(QueryEnum query) {
    this.query = query;
  }

  public FindRequestDTO query(QueryEnum query) {
    this.query = query;
    return this;
  }

  /**
   * Liste der Queryparameter und der dafür zu setzenden Werte.
   *
   * @return queryData
   */
  @JsonProperty("queryData")
  public Map<String, List<String>> getQueryData() {
    return queryData;
  }

  public void setQueryData(Map<String, List<String>> queryData) {
    this.queryData = queryData;
  }

  public FindRequestDTO queryData(Map<String, List<String>> queryData) {
    this.queryData = queryData;
    return this;
  }

  public FindRequestDTO putQueryDataItem(String key, List<String> queryDataItem) {
    this.queryData.put(key, queryDataItem);
    return this;
  }

  /**
   * Art der Rückgabe der Suchergebnisse, als Liste von Objektreferenzen (ObjectRef) oder als
   * vollständige Metadaten (LeafClass)
   *
   * @return returnType
   */
  @JsonProperty("returnType")
  public String getReturnType() {
    if (returnType == null) {
      return null;
    }
    return returnType.value();
  }

  public void setReturnType(ReturnTypeEnum returnType) {
    this.returnType = returnType;
  }

  public FindRequestDTO returnType(ReturnTypeEnum returnType) {
    this.returnType = returnType;
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
    FindRequestDTO findRequestDTO = (FindRequestDTO) o;
    return Objects.equals(this.kvnr, findRequestDTO.kvnr)
        && Objects.equals(this.query, findRequestDTO.query)
        && Objects.equals(this.queryData, findRequestDTO.queryData)
        && Objects.equals(this.returnType, findRequestDTO.returnType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kvnr, query, queryData, returnType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FindRequestDTO {\n");

    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
    sb.append("    queryData: ").append(toIndentedString(queryData)).append("\n");
    sb.append("    returnType: ").append(toIndentedString(returnType)).append("\n");
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
