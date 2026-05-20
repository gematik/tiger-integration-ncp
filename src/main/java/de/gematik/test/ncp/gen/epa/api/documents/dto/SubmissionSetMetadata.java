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
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubmissionSetMetadata {

  @ApiModelProperty(value = "")
  private List<Author> authors = new ArrayList<>();

  @ApiModelProperty(value = "")
  private String contentTypeCode;

  @ApiModelProperty(value = "")
  private LocalDateTime submissionTime;

  @ApiModelProperty(value = "")
  private String availabilityStatus;

  @ApiModelProperty(value = "")
  private String uniqueId;

  @ApiModelProperty(value = "")
  private String entryUUID;

  /**
   * Get authors
   *
   * @return authors
   */
  @JsonProperty("authors")
  public List<Author> getAuthors() {
    return authors;
  }

  public void setAuthors(List<Author> authors) {
    this.authors = authors;
  }

  public SubmissionSetMetadata authors(List<Author> authors) {
    this.authors = authors;
    return this;
  }

  public SubmissionSetMetadata addAuthorsItem(Author authorsItem) {
    this.authors.add(authorsItem);
    return this;
  }

  /**
   * Get contentTypeCode
   *
   * @return contentTypeCode
   */
  @JsonProperty("contentTypeCode")
  public String getContentTypeCode() {
    return contentTypeCode;
  }

  public void setContentTypeCode(String contentTypeCode) {
    this.contentTypeCode = contentTypeCode;
  }

  public SubmissionSetMetadata contentTypeCode(String contentTypeCode) {
    this.contentTypeCode = contentTypeCode;
    return this;
  }

  /**
   * Get submissionTime
   *
   * @return submissionTime
   */
  @JsonProperty("submissionTime")
  public LocalDateTime getSubmissionTime() {
    return submissionTime;
  }

  public void setSubmissionTime(LocalDateTime submissionTime) {
    this.submissionTime = submissionTime;
  }

  public SubmissionSetMetadata submissionTime(LocalDateTime submissionTime) {
    this.submissionTime = submissionTime;
    return this;
  }

  /**
   * Get availabilityStatus
   *
   * @return availabilityStatus
   */
  @JsonProperty("availabilityStatus")
  public String getAvailabilityStatus() {
    return availabilityStatus;
  }

  public void setAvailabilityStatus(String availabilityStatus) {
    this.availabilityStatus = availabilityStatus;
  }

  public SubmissionSetMetadata availabilityStatus(String availabilityStatus) {
    this.availabilityStatus = availabilityStatus;
    return this;
  }

  /**
   * Get uniqueId
   *
   * @return uniqueId
   */
  @JsonProperty("uniqueId")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public SubmissionSetMetadata uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

  /**
   * Get entryUUID
   *
   * @return entryUUID
   */
  @JsonProperty("entryUUID")
  public String getEntryUUID() {
    return entryUUID;
  }

  public void setEntryUUID(String entryUUID) {
    this.entryUUID = entryUUID;
  }

  public SubmissionSetMetadata entryUUID(String entryUUID) {
    this.entryUUID = entryUUID;
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
    SubmissionSetMetadata submissionSetMetadata = (SubmissionSetMetadata) o;
    return Objects.equals(this.authors, submissionSetMetadata.authors)
        && Objects.equals(this.contentTypeCode, submissionSetMetadata.contentTypeCode)
        && Objects.equals(this.submissionTime, submissionSetMetadata.submissionTime)
        && Objects.equals(this.availabilityStatus, submissionSetMetadata.availabilityStatus)
        && Objects.equals(this.uniqueId, submissionSetMetadata.uniqueId)
        && Objects.equals(this.entryUUID, submissionSetMetadata.entryUUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        authors, contentTypeCode, submissionTime, availabilityStatus, uniqueId, entryUUID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmissionSetMetadata {\n");

    sb.append("    authors: ").append(toIndentedString(authors)).append("\n");
    sb.append("    contentTypeCode: ").append(toIndentedString(contentTypeCode)).append("\n");
    sb.append("    submissionTime: ").append(toIndentedString(submissionTime)).append("\n");
    sb.append("    availabilityStatus: ").append(toIndentedString(availabilityStatus)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    entryUUID: ").append(toIndentedString(entryUUID)).append("\n");
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
