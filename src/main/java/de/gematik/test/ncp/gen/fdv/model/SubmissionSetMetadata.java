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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

/** Provides a set of meta data for submission set */
@ApiModel(description = "Provides a set of meta data for submission set")
public class SubmissionSetMetadata {

  @ApiModelProperty(value = "")
  @Valid
  private Author author;

  @ApiModelProperty(value = "additional notes in free text")
  /** additional notes in free text */
  private String comments;

  @ApiModelProperty(value = "")
  private String contentTypeCode;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value = "date-time notation as defined by RFC 3339, section 5.6")
  /** date-time notation as defined by RFC 3339, section 5.6 */
  private Date submissionTime;

  @ApiModelProperty(value = "Entry UUID of the Submission Set, intern")
  /** Entry UUID of the Submission Set, intern */
  private String entryUUID;

  @ApiModelProperty(value = "Unique ID of the Submission Set, extern")
  /** Unique ID of the Submission Set, extern */
  private String uniqueId;

  /**
   * Get author
   *
   * @return author
   */
  @JsonProperty("author")
  public Author getAuthor() {
    return author;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public SubmissionSetMetadata author(Author author) {
    this.author = author;
    return this;
  }

  /**
   * additional notes in free text
   *
   * @return comments
   */
  @JsonProperty("comments")
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public SubmissionSetMetadata comments(String comments) {
    this.comments = comments;
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
   * date-time notation as defined by RFC 3339, section 5.6
   *
   * @return submissionTime
   */
  @JsonProperty("submissionTime")
  public Date getSubmissionTime() {
    return submissionTime;
  }

  public void setSubmissionTime(Date submissionTime) {
    this.submissionTime = submissionTime;
  }

  public SubmissionSetMetadata submissionTime(Date submissionTime) {
    this.submissionTime = submissionTime;
    return this;
  }

  /**
   * Entry UUID of the Submission Set, intern
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

  /**
   * Unique ID of the Submission Set, extern
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SubmissionSetMetadata submissionSetMetadata = (SubmissionSetMetadata) o;
    return Objects.equals(this.author, submissionSetMetadata.author)
        && Objects.equals(this.comments, submissionSetMetadata.comments)
        && Objects.equals(this.contentTypeCode, submissionSetMetadata.contentTypeCode)
        && Objects.equals(this.submissionTime, submissionSetMetadata.submissionTime)
        && Objects.equals(this.entryUUID, submissionSetMetadata.entryUUID)
        && Objects.equals(this.uniqueId, submissionSetMetadata.uniqueId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, comments, contentTypeCode, submissionTime, entryUUID, uniqueId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SubmissionSetMetadata {\n");

    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
    sb.append("    contentTypeCode: ").append(toIndentedString(contentTypeCode)).append("\n");
    sb.append("    submissionTime: ").append(toIndentedString(submissionTime)).append("\n");
    sb.append("    entryUUID: ").append(toIndentedString(entryUUID)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
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
