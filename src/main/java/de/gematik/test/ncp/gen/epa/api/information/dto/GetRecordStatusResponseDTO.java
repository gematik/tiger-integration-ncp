/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.gen.epa.api.information.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

public class GetRecordStatusResponseDTO {

  @ApiModelProperty(value = "")
  private String fqdn;

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  /**
   * Get fqdn
   *
   * @return fqdn
   */
  @JsonProperty("fqdn")
  public String getFqdn() {
    return fqdn;
  }

  public void setFqdn(String fqdn) {
    this.fqdn = fqdn;
  }

  public GetRecordStatusResponseDTO fqdn(String fqdn) {
    this.fqdn = fqdn;
    return this;
  }

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

  public GetRecordStatusResponseDTO success(Boolean success) {
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

  public GetRecordStatusResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
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
    GetRecordStatusResponseDTO getRecordStatusResponseDTO = (GetRecordStatusResponseDTO) o;
    return Objects.equals(this.fqdn, getRecordStatusResponseDTO.fqdn)
        && Objects.equals(this.success, getRecordStatusResponseDTO.success)
        && Objects.equals(this.statusMessage, getRecordStatusResponseDTO.statusMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fqdn, success, statusMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRecordStatusResponseDTO {\n");

    sb.append("    fqdn: ").append(toIndentedString(fqdn)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
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
