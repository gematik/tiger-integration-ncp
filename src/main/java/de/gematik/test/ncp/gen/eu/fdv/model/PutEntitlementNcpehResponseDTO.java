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

package de.gematik.test.ncp.gen.eu.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class PutEntitlementNcpehResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  @ApiModelProperty(required = true, value = "")
  @Valid
  private EntitlementNcpeh entitlement;

  @ApiModelProperty(example = "A2C4E6", required = true, value = "")
  private String accesscode;

  @ApiModelProperty(example = "9-883110000035981", required = true, value = "")
  private String tid;

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

  public PutEntitlementNcpehResponseDTO success(Boolean success) {
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

  public PutEntitlementNcpehResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Get entitlement
   *
   * @return entitlement
   */
  @JsonProperty("entitlement")
  @NotNull
  public EntitlementNcpeh getEntitlement() {
    return entitlement;
  }

  public void setEntitlement(EntitlementNcpeh entitlement) {
    this.entitlement = entitlement;
  }

  public PutEntitlementNcpehResponseDTO entitlement(EntitlementNcpeh entitlement) {
    this.entitlement = entitlement;
    return this;
  }

  /**
   * Get accesscode
   *
   * @return accesscode
   */
  @JsonProperty("accesscode")
  @NotNull
  public String getAccesscode() {
    return accesscode;
  }

  public void setAccesscode(String accesscode) {
    this.accesscode = accesscode;
  }

  public PutEntitlementNcpehResponseDTO accesscode(String accesscode) {
    this.accesscode = accesscode;
    return this;
  }

  /**
   * Get tid
   *
   * @return tid
   */
  @JsonProperty("tid")
  @NotNull
  @Pattern(regexp = "^[1-9][0-9]?-[\\x21-\\x7E]+$")
  @Size(max = 128)
  public String getTid() {
    return tid;
  }

  public void setTid(String tid) {
    this.tid = tid;
  }

  public PutEntitlementNcpehResponseDTO tid(String tid) {
    this.tid = tid;
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
    PutEntitlementNcpehResponseDTO putEntitlementNcpehResponseDTO =
        (PutEntitlementNcpehResponseDTO) o;
    return Objects.equals(this.success, putEntitlementNcpehResponseDTO.success)
        && Objects.equals(this.statusMessage, putEntitlementNcpehResponseDTO.statusMessage)
        && Objects.equals(this.entitlement, putEntitlementNcpehResponseDTO.entitlement)
        && Objects.equals(this.accesscode, putEntitlementNcpehResponseDTO.accesscode)
        && Objects.equals(this.tid, putEntitlementNcpehResponseDTO.tid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, entitlement, accesscode, tid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PutEntitlementNcpehResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    entitlement: ").append(toIndentedString(entitlement)).append("\n");
    sb.append("    accesscode: ").append(toIndentedString(accesscode)).append("\n");
    sb.append("    tid: ").append(toIndentedString(tid)).append("\n");
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
