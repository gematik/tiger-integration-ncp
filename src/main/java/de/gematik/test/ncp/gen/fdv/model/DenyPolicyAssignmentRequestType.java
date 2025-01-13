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

/** Basic type for a deny policy assignments. */
@ApiModel(description = "Basic type for a deny policy assignments.")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "policyAssignmentType",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = GDPolicyAssignmentForCategoryType.class, name = "FOR_CATEGORY"),
  @JsonSubTypes.Type(value = GDPolicyAssignmentForDocumentType.class, name = "FOR_DOCUMENT"),
  @JsonSubTypes.Type(value = GDPolicyAssignmentForFolderType.class, name = "FOR_FOLDER"),
})
public class DenyPolicyAssignmentRequestType {

  @ApiModelProperty(value = "")
  private String policyAssignmentType;

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

  public DenyPolicyAssignmentRequestType policyAssignmentType(String policyAssignmentType) {
    this.policyAssignmentType = policyAssignmentType;
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
    DenyPolicyAssignmentRequestType denyPolicyAssignmentRequestType =
        (DenyPolicyAssignmentRequestType) o;
    return Objects.equals(
        this.policyAssignmentType, denyPolicyAssignmentRequestType.policyAssignmentType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policyAssignmentType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DenyPolicyAssignmentRequestType {\n");

    sb.append("    policyAssignmentType: ")
        .append(toIndentedString(policyAssignmentType))
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
