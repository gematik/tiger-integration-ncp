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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

/** Returns the insurant id of the user that has logged in and the fqdn of access gateway */
@ApiModel(
    description =
        "Returns the insurant id of the user that has logged in and the fqdn of access gateway")
public class LoginResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  @ApiModelProperty(example = "Z123456789", required = true, value = "")
  private String insurantId;

  @ApiModelProperty(required = true, value = "")
  private String fqdn;

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

  public LoginResponseDTO success(Boolean success) {
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

  public LoginResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Get insurantId
   *
   * @return insurantId
   */
  @JsonProperty("insurantId")
  @NotNull
  @Pattern(regexp = "^[A-Z]{1}\\d{9}$")
  public String getInsurantId() {
    return insurantId;
  }

  public void setInsurantId(String insurantId) {
    this.insurantId = insurantId;
  }

  public LoginResponseDTO insurantId(String insurantId) {
    this.insurantId = insurantId;
    return this;
  }

  /**
   * Get fqdn
   *
   * @return fqdn
   */
  @JsonProperty("fqdn")
  @NotNull
  public String getFqdn() {
    return fqdn;
  }

  public void setFqdn(String fqdn) {
    this.fqdn = fqdn;
  }

  public LoginResponseDTO fqdn(String fqdn) {
    this.fqdn = fqdn;
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
    LoginResponseDTO loginResponseDTO = (LoginResponseDTO) o;
    return Objects.equals(this.success, loginResponseDTO.success)
        && Objects.equals(this.statusMessage, loginResponseDTO.statusMessage)
        && Objects.equals(this.insurantId, loginResponseDTO.insurantId)
        && Objects.equals(this.fqdn, loginResponseDTO.fqdn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, insurantId, fqdn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    insurantId: ").append(toIndentedString(insurantId)).append("\n");
    sb.append("    fqdn: ").append(toIndentedString(fqdn)).append("\n");
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
