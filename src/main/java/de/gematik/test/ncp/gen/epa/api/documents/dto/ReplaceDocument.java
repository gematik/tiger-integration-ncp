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

public class ReplaceDocument {

  @ApiModelProperty(value = "")
  private byte[] documentData;

  @ApiModelProperty(value = "")
  private DocumentMetadata documentMetadata;

  @ApiModelProperty(value = "")
  private String entryUUIDOfDocumentToReplace;

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

  public ReplaceDocument documentData(byte[] documentData) {
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

  public ReplaceDocument documentMetadata(DocumentMetadata documentMetadata) {
    this.documentMetadata = documentMetadata;
    return this;
  }

  /**
   * Get entryUUIDOfDocumentToReplace
   *
   * @return entryUUIDOfDocumentToReplace
   */
  @JsonProperty("entryUUIDOfDocumentToReplace")
  public String getEntryUUIDOfDocumentToReplace() {
    return entryUUIDOfDocumentToReplace;
  }

  public void setEntryUUIDOfDocumentToReplace(String entryUUIDOfDocumentToReplace) {
    this.entryUUIDOfDocumentToReplace = entryUUIDOfDocumentToReplace;
  }

  public ReplaceDocument entryUUIDOfDocumentToReplace(String entryUUIDOfDocumentToReplace) {
    this.entryUUIDOfDocumentToReplace = entryUUIDOfDocumentToReplace;
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
    ReplaceDocument replaceDocument = (ReplaceDocument) o;
    return Objects.equals(this.documentData, replaceDocument.documentData)
        && Objects.equals(this.documentMetadata, replaceDocument.documentMetadata)
        && Objects.equals(
            this.entryUUIDOfDocumentToReplace, replaceDocument.entryUUIDOfDocumentToReplace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentData, documentMetadata, entryUUIDOfDocumentToReplace);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReplaceDocument {\n");

    sb.append("    documentData: ").append(toIndentedString(documentData)).append("\n");
    sb.append("    documentMetadata: ").append(toIndentedString(documentMetadata)).append("\n");
    sb.append("    entryUUIDOfDocumentToReplace: ")
        .append(toIndentedString(entryUUIDOfDocumentToReplace))
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
