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
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;

/** Global deny policy assigment for a single category. */
@ApiModel(description = "Global deny policy assigment for a single category.")
public class GDPolicyAssignmentForCategoryType extends DenyPolicyAssignmentRequestType {

  @ApiModelProperty(required = true, value = "technical identifier of category")
  @Valid
  /** technical identifier of category */
  private CategoryIdType categoryId;

  /**
   * technical identifier of category
   *
   * @return categoryId
   */
  @JsonProperty("categoryId")
  @NotNull
  public CategoryIdType getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(CategoryIdType categoryId) {
    this.categoryId = categoryId;
  }

  public GDPolicyAssignmentForCategoryType categoryId(CategoryIdType categoryId) {
    this.categoryId = categoryId;
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
    GDPolicyAssignmentForCategoryType gdPolicyAssignmentForCategoryType =
        (GDPolicyAssignmentForCategoryType) o;
    return Objects.equals(this.categoryId, gdPolicyAssignmentForCategoryType.categoryId)
        && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoryId, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GDPolicyAssignmentForCategoryType {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
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
