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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/** Meta data of a folder */
@ApiModel(description = "Meta data of a folder")
public class FolderMetadata {

  @ApiModelProperty(value = "Ergänzende Hinweise in Freitext")
  /** Ergänzende Hinweise in Freitext */
  private String comments;

  @ApiModelProperty(value = "Liste von Codes, die mit dem Ordner assoziiert werden.")
  /** Liste von Codes, die mit dem Ordner assoziiert werden. */
  private List<String> codeList = new ArrayList<>();

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Zeitstempel, an dem der Ordner das letzte mal geändert wurde; date-time notation as defined by RFC 3339, section 5.6")
  /**
   * Zeitstempel, an dem der Ordner das letzte mal geändert wurde; date-time notation as defined by
   * RFC 3339, section 5.6
   */
  private Date lastUpdateTime;

  @ApiModelProperty(value = "Titel des Ordner")
  /** Titel des Ordner */
  private String title;

  @ApiModelProperty(value = "Eindeutige Kennung des Ordner (von Client erzeugt)")
  /** Eindeutige Kennung des Ordner (von Client erzeugt) */
  private String uniqueId;

  @ApiModelProperty(
      example = "Folder-1",
      value = "Eindeutige Kennung des Ordner in der Registry (von Aktensystem gesetzt)")
  /** Eindeutige Kennung des Ordner in der Registry (von Aktensystem gesetzt) */
  private String entryUUID;

  @ApiModelProperty(
      example = "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved, Approved",
      value = "Status der Ordner")
  /** Status der Ordner */
  private String availabilityStatus;

  /**
   * Ergänzende Hinweise in Freitext
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

  public FolderMetadata comments(String comments) {
    this.comments = comments;
    return this;
  }

  /**
   * Liste von Codes, die mit dem Ordner assoziiert werden.
   *
   * @return codeList
   */
  @JsonProperty("codeList")
  public List<String> getCodeList() {
    return codeList;
  }

  public void setCodeList(List<String> codeList) {
    this.codeList = codeList;
  }

  public FolderMetadata codeList(List<String> codeList) {
    this.codeList = codeList;
    return this;
  }

  public FolderMetadata addCodeListItem(String codeListItem) {
    this.codeList.add(codeListItem);
    return this;
  }

  /**
   * Zeitstempel, an dem der Ordner das letzte mal geändert wurde; date-time notation as defined by
   * RFC 3339, section 5.6
   *
   * @return lastUpdateTime
   */
  @JsonProperty("lastUpdateTime")
  public Date getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public FolderMetadata lastUpdateTime(Date lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
    return this;
  }

  /**
   * Titel des Ordner
   *
   * @return title
   */
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public FolderMetadata title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Eindeutige Kennung des Ordner (von Client erzeugt)
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

  public FolderMetadata uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

  /**
   * Eindeutige Kennung des Ordner in der Registry (von Aktensystem gesetzt)
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

  public FolderMetadata entryUUID(String entryUUID) {
    this.entryUUID = entryUUID;
    return this;
  }

  /**
   * Status der Ordner
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

  public FolderMetadata availabilityStatus(String availabilityStatus) {
    this.availabilityStatus = availabilityStatus;
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
    FolderMetadata folderMetadata = (FolderMetadata) o;
    return Objects.equals(this.comments, folderMetadata.comments)
        && Objects.equals(this.codeList, folderMetadata.codeList)
        && Objects.equals(this.lastUpdateTime, folderMetadata.lastUpdateTime)
        && Objects.equals(this.title, folderMetadata.title)
        && Objects.equals(this.uniqueId, folderMetadata.uniqueId)
        && Objects.equals(this.entryUUID, folderMetadata.entryUUID)
        && Objects.equals(this.availabilityStatus, folderMetadata.availabilityStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        comments, codeList, lastUpdateTime, title, uniqueId, entryUUID, availabilityStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FolderMetadata {\n");

    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
    sb.append("    codeList: ").append(toIndentedString(codeList)).append("\n");
    sb.append("    lastUpdateTime: ").append(toIndentedString(lastUpdateTime)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    entryUUID: ").append(toIndentedString(entryUUID)).append("\n");
    sb.append("    availabilityStatus: ").append(toIndentedString(availabilityStatus)).append("\n");
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
