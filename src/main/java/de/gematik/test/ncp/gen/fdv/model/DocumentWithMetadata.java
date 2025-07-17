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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class DocumentWithMetadata {

  @ApiModelProperty(required = true, value = "")
  @Valid
  private DocumentMetadata metadata;

  @ApiModelProperty(required = true, value = "")
  @Valid
  private Document document;

  /**
   * Get metadata
   *
   * @return metadata
   */
  @JsonProperty("metadata")
  @NotNull
  public DocumentMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(DocumentMetadata metadata) {
    this.metadata = metadata;
  }

  public DocumentWithMetadata metadata(DocumentMetadata metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Get document
   *
   * @return document
   */
  @JsonProperty("document")
  @NotNull
  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public DocumentWithMetadata document(Document document) {
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
    DocumentWithMetadata documentWithMetadata = (DocumentWithMetadata) o;
    return Objects.equals(this.metadata, documentWithMetadata.metadata)
        && Objects.equals(this.document, documentWithMetadata.document);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadata, document);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentWithMetadata {\n");

    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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
