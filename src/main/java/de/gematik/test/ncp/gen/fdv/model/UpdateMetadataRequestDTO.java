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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class UpdateMetadataRequestDTO {

  @ApiModelProperty(example = "Z123456789", value = "")
  private String recordId;

  @ApiModelProperty(required = true, value = "")
  @Valid
  private ObjectSelectorItem _object;

  @ApiModelProperty(required = true, value = "")
  @Valid
  private DocumentMetadata documentMetadata;

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

  public UpdateMetadataRequestDTO recordId(String recordId) {
    this.recordId = recordId;
    return this;
  }

  /**
   * Get _object
   *
   * @return _object
   */
  @JsonProperty("object")
  @NotNull
  public ObjectSelectorItem getObject() {
    return _object;
  }

  public void setObject(ObjectSelectorItem _object) {
    this._object = _object;
  }

  public UpdateMetadataRequestDTO _object(ObjectSelectorItem _object) {
    this._object = _object;
    return this;
  }

  /**
   * Get documentMetadata
   *
   * @return documentMetadata
   */
  @JsonProperty("documentMetadata")
  @NotNull
  public DocumentMetadata getDocumentMetadata() {
    return documentMetadata;
  }

  public void setDocumentMetadata(DocumentMetadata documentMetadata) {
    this.documentMetadata = documentMetadata;
  }

  public UpdateMetadataRequestDTO documentMetadata(DocumentMetadata documentMetadata) {
    this.documentMetadata = documentMetadata;
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
    UpdateMetadataRequestDTO updateMetadataRequestDTO = (UpdateMetadataRequestDTO) o;
    return Objects.equals(this.recordId, updateMetadataRequestDTO.recordId)
        && Objects.equals(this._object, updateMetadataRequestDTO._object)
        && Objects.equals(this.documentMetadata, updateMetadataRequestDTO.documentMetadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordId, _object, documentMetadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateMetadataRequestDTO {\n");

    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    _object: ").append(toIndentedString(_object)).append("\n");
    sb.append("    documentMetadata: ").append(toIndentedString(documentMetadata)).append("\n");
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
