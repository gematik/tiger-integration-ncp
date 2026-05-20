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

package de.gematik.test.ncp.gen.epa.api.entitlement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Returns a list of all blocked users */
@ApiModel(description = "Returns a list of all blocked users")
public class GetBlockedUserListResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  @ApiModelProperty(required = true, value = "")
  private List<GetBlockedUserListResponseDTOAllOfAssignments> assignments = new ArrayList<>();

  /**
   * Get success
   *
   * @return success
   */
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public GetBlockedUserListResponseDTO success(Boolean success) {
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

  public GetBlockedUserListResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Get assignments
   *
   * @return assignments
   */
  @JsonProperty("assignments")
  public List<GetBlockedUserListResponseDTOAllOfAssignments> getAssignments() {
    return assignments;
  }

  public void setAssignments(List<GetBlockedUserListResponseDTOAllOfAssignments> assignments) {
    this.assignments = assignments;
  }

  public GetBlockedUserListResponseDTO assignments(
      List<GetBlockedUserListResponseDTOAllOfAssignments> assignments) {
    this.assignments = assignments;
    return this;
  }

  public GetBlockedUserListResponseDTO addAssignmentsItem(
      GetBlockedUserListResponseDTOAllOfAssignments assignmentsItem) {
    this.assignments.add(assignmentsItem);
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
    GetBlockedUserListResponseDTO getBlockedUserListResponseDTO = (GetBlockedUserListResponseDTO) o;
    return Objects.equals(this.success, getBlockedUserListResponseDTO.success)
        && Objects.equals(this.statusMessage, getBlockedUserListResponseDTO.statusMessage)
        && Objects.equals(this.assignments, getBlockedUserListResponseDTO.assignments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, assignments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetBlockedUserListResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    assignments: ").append(toIndentedString(assignments)).append("\n");
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
