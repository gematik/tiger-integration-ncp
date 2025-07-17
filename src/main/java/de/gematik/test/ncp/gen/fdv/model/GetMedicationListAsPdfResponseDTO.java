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

/** Response including the eML as PDF/A. */
@ApiModel(description = "Response including the eML as PDF/A.")
public class GetMedicationListAsPdfResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  @ApiModelProperty(value = "The eML as PDF/A as an array of bytes.")
  /** The eML as PDF/A as an array of bytes. */
  private byte[] eml;

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

  public GetMedicationListAsPdfResponseDTO success(Boolean success) {
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

  public GetMedicationListAsPdfResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * The eML as PDF/A as an array of bytes.
   *
   * @return eml
   */
  @JsonProperty("eml")
  public byte[] getEml() {
    return eml;
  }

  public void setEml(byte[] eml) {
    this.eml = eml;
  }

  public GetMedicationListAsPdfResponseDTO eml(byte[] eml) {
    this.eml = eml;
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
    GetMedicationListAsPdfResponseDTO getMedicationListAsPdfResponseDTO =
        (GetMedicationListAsPdfResponseDTO) o;
    return Objects.equals(this.success, getMedicationListAsPdfResponseDTO.success)
        && Objects.equals(this.statusMessage, getMedicationListAsPdfResponseDTO.statusMessage)
        && Objects.equals(this.eml, getMedicationListAsPdfResponseDTO.eml);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, eml);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMedicationListAsPdfResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    eml: ").append(toIndentedString(eml)).append("\n");
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
