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

public class RetrieveDocumentElement {

  @ApiModelProperty(value = "")
  private String repositoryUniqueId;

  @ApiModelProperty(value = "")
  private String documentUniqueId;

  @ApiModelProperty(value = "")
  private String mimeType;

  @ApiModelProperty(value = "")
  private byte[] document;

  /**
   * Get repositoryUniqueId
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

  public RetrieveDocumentElement repositoryUniqueId(String repositoryUniqueId) {
    this.repositoryUniqueId = repositoryUniqueId;
    return this;
  }

  /**
   * Get documentUniqueId
   *
   * @return documentUniqueId
   */
  @JsonProperty("documentUniqueId")
  public String getDocumentUniqueId() {
    return documentUniqueId;
  }

  public void setDocumentUniqueId(String documentUniqueId) {
    this.documentUniqueId = documentUniqueId;
  }

  public RetrieveDocumentElement documentUniqueId(String documentUniqueId) {
    this.documentUniqueId = documentUniqueId;
    return this;
  }

  /**
   * Get mimeType
   *
   * @return mimeType
   */
  @JsonProperty("mimeType")
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public RetrieveDocumentElement mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Get document
   *
   * @return document
   */
  @JsonProperty("document")
  public byte[] getDocument() {
    return document;
  }

  public void setDocument(byte[] document) {
    this.document = document;
  }

  public RetrieveDocumentElement document(byte[] document) {
    this.document = document;
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
    RetrieveDocumentElement retrieveDocumentElement = (RetrieveDocumentElement) o;
    return Objects.equals(this.repositoryUniqueId, retrieveDocumentElement.repositoryUniqueId)
        && Objects.equals(this.documentUniqueId, retrieveDocumentElement.documentUniqueId)
        && Objects.equals(this.mimeType, retrieveDocumentElement.mimeType)
        && Objects.equals(this.document, retrieveDocumentElement.document);
  }

  @Override
  public int hashCode() {
    return Objects.hash(repositoryUniqueId, documentUniqueId, mimeType, document);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RetrieveDocumentElement {\n");

    sb.append("    repositoryUniqueId: ").append(toIndentedString(repositoryUniqueId)).append("\n");
    sb.append("    documentUniqueId: ").append(toIndentedString(documentUniqueId)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    document: ").append(toIndentedString(document)).append("\n");
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
