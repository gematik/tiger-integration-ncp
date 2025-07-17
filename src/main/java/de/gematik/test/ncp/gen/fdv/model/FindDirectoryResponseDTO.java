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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FindDirectoryResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid DirectoryEntryWithCertificates> directoryEntries = new ArrayList<>();

  /**
   * Get success
   *
   * @return success
   */
  @JsonProperty("success")
  @NotNull
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public FindDirectoryResponseDTO success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get statusMessage
   *
   * @return statusMessage
   */
  @JsonProperty("statusMessage")
  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public FindDirectoryResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Get directoryEntries
   *
   * @return directoryEntries
   */
  @JsonProperty("directoryEntries")
  public List<@Valid DirectoryEntryWithCertificates> getDirectoryEntries() {
    return directoryEntries;
  }

  public void setDirectoryEntries(List<@Valid DirectoryEntryWithCertificates> directoryEntries) {
    this.directoryEntries = directoryEntries;
  }

  public FindDirectoryResponseDTO directoryEntries(
      List<@Valid DirectoryEntryWithCertificates> directoryEntries) {
    this.directoryEntries = directoryEntries;
    return this;
  }

  public FindDirectoryResponseDTO addDirectoryEntriesItem(
      DirectoryEntryWithCertificates directoryEntriesItem) {
    this.directoryEntries.add(directoryEntriesItem);
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
    FindDirectoryResponseDTO findDirectoryResponseDTO = (FindDirectoryResponseDTO) o;
    return Objects.equals(this.success, findDirectoryResponseDTO.success)
        && Objects.equals(this.statusMessage, findDirectoryResponseDTO.statusMessage)
        && Objects.equals(this.directoryEntries, findDirectoryResponseDTO.directoryEntries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, directoryEntries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FindDirectoryResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    directoryEntries: ").append(toIndentedString(directoryEntries)).append("\n");
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
