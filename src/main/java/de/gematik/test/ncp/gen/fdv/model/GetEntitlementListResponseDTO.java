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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Returns a list of all entitlements */
@ApiModel(description = "Returns a list of all entitlements")
public class GetEntitlementListResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  @ApiModelProperty(required = true, value = "")
  @Valid
  private List<@Valid Entitlement> entitlements = new ArrayList<>();

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

  public GetEntitlementListResponseDTO success(Boolean success) {
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

  public GetEntitlementListResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Get entitlements
   *
   * @return entitlements
   */
  @JsonProperty("entitlements")
  @NotNull
  public List<@Valid Entitlement> getEntitlements() {
    return entitlements;
  }

  public void setEntitlements(List<@Valid Entitlement> entitlements) {
    this.entitlements = entitlements;
  }

  public GetEntitlementListResponseDTO entitlements(List<@Valid Entitlement> entitlements) {
    this.entitlements = entitlements;
    return this;
  }

  public GetEntitlementListResponseDTO addEntitlementsItem(Entitlement entitlementsItem) {
    this.entitlements.add(entitlementsItem);
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
    GetEntitlementListResponseDTO getEntitlementListResponseDTO = (GetEntitlementListResponseDTO) o;
    return Objects.equals(this.success, getEntitlementListResponseDTO.success)
        && Objects.equals(this.statusMessage, getEntitlementListResponseDTO.statusMessage)
        && Objects.equals(this.entitlements, getEntitlementListResponseDTO.entitlements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, entitlements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEntitlementListResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    entitlements: ").append(toIndentedString(entitlements)).append("\n");
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
