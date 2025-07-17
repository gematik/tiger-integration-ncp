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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class FindObjectsRequestDTO {

  @ApiModelProperty(example = "Z123456789", value = "")
  private String recordId;

  @ApiModelProperty(value = "")
  @Valid
  private QueryMetadata queryMetadata;

  public enum QueryEnum {
    FIND_DOCUMENTS(String.valueOf("FindDocuments")),
    FIND_DOCUMENTS_BY_TITLE(String.valueOf("FindDocumentsByTitle")),
    FIND_DOCUMENTS_BY_REFERENCE_ID(String.valueOf("FindDocumentsByReferenceId")),
    FIND_SUBMISSION_SETS(String.valueOf("FindSubmissionSets")),
    FIND_FOLDERS(String.valueOf("FindFolders")),
    GET_ALL(String.valueOf("GetAll")),
    GET_DOCUMENTS(String.valueOf("GetDocuments")),
    GET_SUBMISSION_SETS(String.valueOf("GetSubmissionSets")),
    GET_SUBMISSION_SET_AND_CONTENTS(String.valueOf("GetSubmissionSetAndContents")),
    GET_FOLDERS_FOR_DOCUMENT(String.valueOf("GetFoldersForDocument")),
    GET_FOLDER_AND_CONTENTS(String.valueOf("GetFolderAndContents"));

    private String value;

    QueryEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
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
      value =
          "optional; if no stored query is set, the FdV has to choose an appropriate one according to the given search parameters.")
  /**
   * optional; if no stored query is set, the FdV has to choose an appropriate one according to the
   * given search parameters.
   */
  private QueryEnum query;

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
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ReturnTypeEnum fromValue(String value) {
      for (ReturnTypeEnum b : ReturnTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "")
  private ReturnTypeEnum returnType = ReturnTypeEnum.LEAF_CLASS;

  /**
   * Get recordId
   *
   * @return recordId
   */
  @JsonProperty("recordId")
  @Pattern(regexp = "^[A-Z]{1}\\d{9}$")
  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  public FindObjectsRequestDTO recordId(String recordId) {
    this.recordId = recordId;
    return this;
  }

  /**
   * Get queryMetadata
   *
   * @return queryMetadata
   */
  @JsonProperty("queryMetadata")
  public QueryMetadata getQueryMetadata() {
    return queryMetadata;
  }

  public void setQueryMetadata(QueryMetadata queryMetadata) {
    this.queryMetadata = queryMetadata;
  }

  public FindObjectsRequestDTO queryMetadata(QueryMetadata queryMetadata) {
    this.queryMetadata = queryMetadata;
    return this;
  }

  /**
   * optional; if no stored query is set, the FdV has to choose an appropriate one according to the
   * given search parameters.
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

  public FindObjectsRequestDTO query(QueryEnum query) {
    this.query = query;
    return this;
  }

  /**
   * Get returnType
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

  public FindObjectsRequestDTO returnType(ReturnTypeEnum returnType) {
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
    FindObjectsRequestDTO findObjectsRequestDTO = (FindObjectsRequestDTO) o;
    return Objects.equals(this.recordId, findObjectsRequestDTO.recordId)
        && Objects.equals(this.queryMetadata, findObjectsRequestDTO.queryMetadata)
        && Objects.equals(this.query, findObjectsRequestDTO.query)
        && Objects.equals(this.returnType, findObjectsRequestDTO.returnType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordId, queryMetadata, query, returnType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FindObjectsRequestDTO {\n");

    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    queryMetadata: ").append(toIndentedString(queryMetadata)).append("\n");
    sb.append("    query: ").append(toIndentedString(query)).append("\n");
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
