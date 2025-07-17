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
import java.util.Date;
import java.util.Objects;

/**
 * In case of ambiguous display names the telematikId is given. One of both, display name or
 * telematikId should be set.
 */
@ApiModel(
    description =
        "In case of ambiguous display names the telematikId is given. One of both, display name or telematikId should be set.")
public class PostEntitlementRequestInstDTO extends PostEntitlementRequestDTO {

  @ApiModelProperty(example = "Praxis Dr. Annamaria Heckhausén", value = "A readable name")
  /** A readable name */
  private String hciDisplayName;

  @ApiModelProperty(example = "2-883110000118994", value = "")
  private String hciTelematikId;

  @ApiModelProperty(example = "2025-01-01T18:00Z", required = true, value = "")
  private Date validTo;

  /**
   * A readable name
   *
   * @return hciDisplayName
   */
  @JsonProperty("hci_DisplayName")
  public String getHciDisplayName() {
    return hciDisplayName;
  }

  public void setHciDisplayName(String hciDisplayName) {
    this.hciDisplayName = hciDisplayName;
  }

  public PostEntitlementRequestInstDTO hciDisplayName(String hciDisplayName) {
    this.hciDisplayName = hciDisplayName;
    return this;
  }

  /**
   * Get hciTelematikId
   *
   * @return hciTelematikId
   */
  @JsonProperty("hci_TelematikId")
  @Pattern(regexp = "^[1-9][0-9]?-[\\x21-\\x7E]+$")
  @Size(max = 128)
  public String getHciTelematikId() {
    return hciTelematikId;
  }

  public void setHciTelematikId(String hciTelematikId) {
    this.hciTelematikId = hciTelematikId;
  }

  public PostEntitlementRequestInstDTO hciTelematikId(String hciTelematikId) {
    this.hciTelematikId = hciTelematikId;
    return this;
  }

  /**
   * Get validTo
   *
   * @return validTo
   */
  @JsonProperty("validTo")
  @NotNull
  public Date getValidTo() {
    return validTo;
  }

  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

  public PostEntitlementRequestInstDTO validTo(Date validTo) {
    this.validTo = validTo;
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
    PostEntitlementRequestInstDTO postEntitlementRequestInstDTO = (PostEntitlementRequestInstDTO) o;
    return Objects.equals(this.hciDisplayName, postEntitlementRequestInstDTO.hciDisplayName)
        && Objects.equals(this.hciTelematikId, postEntitlementRequestInstDTO.hciTelematikId)
        && Objects.equals(this.validTo, postEntitlementRequestInstDTO.validTo)
        && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hciDisplayName, hciTelematikId, validTo, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostEntitlementRequestInstDTO {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    hciDisplayName: ").append(toIndentedString(hciDisplayName)).append("\n");
    sb.append("    hciTelematikId: ").append(toIndentedString(hciTelematikId)).append("\n");
    sb.append("    validTo: ").append(toIndentedString(validTo)).append("\n");
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
