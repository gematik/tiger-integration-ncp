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
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

public class RestrictedUpdateDocument {

  @ApiModelProperty(value = "")
  private byte[] documentData;

  @ApiModelProperty(value = "")
  private DocumentMetadata documentMetadata;

  @ApiModelProperty(value = "")
  private String entryUUIDOfDocumentToUpdate;

  /**
   * Get documentData
   *
   * @return documentData
   */
  @JsonProperty("documentData")
  public byte[] getDocumentData() {
    return documentData;
  }

  public void setDocumentData(byte[] documentData) {
    this.documentData = documentData;
  }

  public RestrictedUpdateDocument documentData(byte[] documentData) {
    this.documentData = documentData;
    return this;
  }

  /**
   * Get documentMetadata
   *
   * @return documentMetadata
   */
  @JsonProperty("documentMetadata")
  public DocumentMetadata getDocumentMetadata() {
    return documentMetadata;
  }

  public void setDocumentMetadata(DocumentMetadata documentMetadata) {
    this.documentMetadata = documentMetadata;
  }

  public RestrictedUpdateDocument documentMetadata(DocumentMetadata documentMetadata) {
    this.documentMetadata = documentMetadata;
    return this;
  }

  /**
   * Get entryUUIDOfDocumentToUpdate
   *
   * @return entryUUIDOfDocumentToUpdate
   */
  @JsonProperty("entryUUIDOfDocumentToUpdate")
  public String getEntryUUIDOfDocumentToUpdate() {
    return entryUUIDOfDocumentToUpdate;
  }

  public void setEntryUUIDOfDocumentToUpdate(String entryUUIDOfDocumentToUpdate) {
    this.entryUUIDOfDocumentToUpdate = entryUUIDOfDocumentToUpdate;
  }

  public RestrictedUpdateDocument entryUUIDOfDocumentToUpdate(String entryUUIDOfDocumentToUpdate) {
    this.entryUUIDOfDocumentToUpdate = entryUUIDOfDocumentToUpdate;
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
    RestrictedUpdateDocument restrictedUpdateDocument = (RestrictedUpdateDocument) o;
    return Objects.equals(this.documentData, restrictedUpdateDocument.documentData)
        && Objects.equals(this.documentMetadata, restrictedUpdateDocument.documentMetadata)
        && Objects.equals(
            this.entryUUIDOfDocumentToUpdate, restrictedUpdateDocument.entryUUIDOfDocumentToUpdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentData, documentMetadata, entryUUIDOfDocumentToUpdate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RestrictedUpdateDocument {\n");

    sb.append("    documentData: ").append(toIndentedString(documentData)).append("\n");
    sb.append("    documentMetadata: ").append(toIndentedString(documentMetadata)).append("\n");
    sb.append("    entryUUIDOfDocumentToUpdate: ")
        .append(toIndentedString(entryUUIDOfDocumentToUpdate))
        .append("\n");
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
