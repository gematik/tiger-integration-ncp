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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;
import java.util.UUID;

/** Basic type for a deny policy response. */
@ApiModel(description = "Basic type for a deny policy response.")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "policyAssignmentType",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(
      value = GDPolicyAssignmentForCategoryTypeResponse.class,
      name = "FOR_CATEGORY"),
  @JsonSubTypes.Type(
      value = GDPolicyAssignmentForDocumentTypeResponse.class,
      name = "FOR_DOCUMENT"),
  @JsonSubTypes.Type(value = GDPolicyAssignmentForFolderTypeResponse.class, name = "FOR_FOLDER"),
})
public class DenyPolicyAssignmentResponseType {

  @ApiModelProperty(value = "")
  private String policyAssignmentType;

  @ApiModelProperty(
      example = "fa2bc929-f6c9-419f-ae75-cea040927651",
      value = "Server-assigned unique uuid of a particular deny policy assignment")
  /** Server-assigned unique uuid of a particular deny policy assignment */
  private UUID assignmentId;

  /**
   * Get policyAssignmentType
   *
   * @return policyAssignmentType
   */
  @JsonProperty("policyAssignmentType")
  public String getPolicyAssignmentType() {
    return policyAssignmentType;
  }

  public void setPolicyAssignmentType(String policyAssignmentType) {
    this.policyAssignmentType = policyAssignmentType;
  }

  public DenyPolicyAssignmentResponseType policyAssignmentType(String policyAssignmentType) {
    this.policyAssignmentType = policyAssignmentType;
    return this;
  }

  /**
   * Server-assigned unique uuid of a particular deny policy assignment
   *
   * @return assignmentId
   */
  @JsonProperty("assignmentId")
  public UUID getAssignmentId() {
    return assignmentId;
  }

  public void setAssignmentId(UUID assignmentId) {
    this.assignmentId = assignmentId;
  }

  public DenyPolicyAssignmentResponseType assignmentId(UUID assignmentId) {
    this.assignmentId = assignmentId;
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
    DenyPolicyAssignmentResponseType denyPolicyAssignmentResponseType =
        (DenyPolicyAssignmentResponseType) o;
    return Objects.equals(
            this.policyAssignmentType, denyPolicyAssignmentResponseType.policyAssignmentType)
        && Objects.equals(this.assignmentId, denyPolicyAssignmentResponseType.assignmentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policyAssignmentType, assignmentId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DenyPolicyAssignmentResponseType {\n");

    sb.append("    policyAssignmentType: ")
        .append(toIndentedString(policyAssignmentType))
        .append("\n");
    sb.append("    assignmentId: ").append(toIndentedString(assignmentId)).append("\n");
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
