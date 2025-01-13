/*
 * Copyright (c) 2024-2025 gematik GmbH
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
 */

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistryObjectLists {

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid SubmissionSetMetadata> submissionSetsMetadata = new ArrayList<>();

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid DocumentMetadata> documentsMetadata = new ArrayList<>();

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid FolderMetadata> foldersMetadata = new ArrayList<>();

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid Association> associations = new ArrayList<>();

  /**
   * Get submissionSetsMetadata
   *
   * @return submissionSetsMetadata
   */
  @JsonProperty("submissionSetsMetadata")
  public List<@Valid SubmissionSetMetadata> getSubmissionSetsMetadata() {
    return submissionSetsMetadata;
  }

  public void setSubmissionSetsMetadata(List<@Valid SubmissionSetMetadata> submissionSetsMetadata) {
    this.submissionSetsMetadata = submissionSetsMetadata;
  }

  public RegistryObjectLists submissionSetsMetadata(
      List<@Valid SubmissionSetMetadata> submissionSetsMetadata) {
    this.submissionSetsMetadata = submissionSetsMetadata;
    return this;
  }

  public RegistryObjectLists addSubmissionSetsMetadataItem(
      SubmissionSetMetadata submissionSetsMetadataItem) {
    this.submissionSetsMetadata.add(submissionSetsMetadataItem);
    return this;
  }

  /**
   * Get documentsMetadata
   *
   * @return documentsMetadata
   */
  @JsonProperty("documentsMetadata")
  public List<@Valid DocumentMetadata> getDocumentsMetadata() {
    return documentsMetadata;
  }

  public void setDocumentsMetadata(List<@Valid DocumentMetadata> documentsMetadata) {
    this.documentsMetadata = documentsMetadata;
  }

  public RegistryObjectLists documentsMetadata(List<@Valid DocumentMetadata> documentsMetadata) {
    this.documentsMetadata = documentsMetadata;
    return this;
  }

  public RegistryObjectLists addDocumentsMetadataItem(DocumentMetadata documentsMetadataItem) {
    this.documentsMetadata.add(documentsMetadataItem);
    return this;
  }

  /**
   * Get foldersMetadata
   *
   * @return foldersMetadata
   */
  @JsonProperty("foldersMetadata")
  public List<@Valid FolderMetadata> getFoldersMetadata() {
    return foldersMetadata;
  }

  public void setFoldersMetadata(List<@Valid FolderMetadata> foldersMetadata) {
    this.foldersMetadata = foldersMetadata;
  }

  public RegistryObjectLists foldersMetadata(List<@Valid FolderMetadata> foldersMetadata) {
    this.foldersMetadata = foldersMetadata;
    return this;
  }

  public RegistryObjectLists addFoldersMetadataItem(FolderMetadata foldersMetadataItem) {
    this.foldersMetadata.add(foldersMetadataItem);
    return this;
  }

  /**
   * Get associations
   *
   * @return associations
   */
  @JsonProperty("associations")
  public List<@Valid Association> getAssociations() {
    return associations;
  }

  public void setAssociations(List<@Valid Association> associations) {
    this.associations = associations;
  }

  public RegistryObjectLists associations(List<@Valid Association> associations) {
    this.associations = associations;
    return this;
  }

  public RegistryObjectLists addAssociationsItem(Association associationsItem) {
    this.associations.add(associationsItem);
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
    RegistryObjectLists registryObjectLists = (RegistryObjectLists) o;
    return Objects.equals(this.submissionSetsMetadata, registryObjectLists.submissionSetsMetadata)
        && Objects.equals(this.documentsMetadata, registryObjectLists.documentsMetadata)
        && Objects.equals(this.foldersMetadata, registryObjectLists.foldersMetadata)
        && Objects.equals(this.associations, registryObjectLists.associations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(submissionSetsMetadata, documentsMetadata, foldersMetadata, associations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegistryObjectLists {\n");

    sb.append("    submissionSetsMetadata: ")
        .append(toIndentedString(submissionSetsMetadata))
        .append("\n");
    sb.append("    documentsMetadata: ").append(toIndentedString(documentsMetadata)).append("\n");
    sb.append("    foldersMetadata: ").append(toIndentedString(foldersMetadata)).append("\n");
    sb.append("    associations: ").append(toIndentedString(associations)).append("\n");
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
