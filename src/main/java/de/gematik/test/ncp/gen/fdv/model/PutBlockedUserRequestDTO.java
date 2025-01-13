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
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class PutBlockedUserRequestDTO {

  @ApiModelProperty(example = "Z123456789", value = "")
  private String recordId;

  @ApiModelProperty(example = "1.2.276.0.76.4.50", required = true, value = "A professionOID")
  /** A professionOID */
  private String oid;

  @ApiModelProperty(
      example = "Praxis Dr. Annamaria Heckhausén",
      required = true,
      value = "A readable name")
  /** A readable name */
  private String displayName;

  /**
   * Get recordId
   *
   * @return recordId
   */
  @JsonProperty("recordId")
  @Pattern(regexp = "^[A-Z]{1}\\d{9}$")
  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  public PutBlockedUserRequestDTO recordId(String recordId) {
    this.recordId = recordId;
    return this;
  }

  /**
   * A professionOID
   *
   * @return oid
   */
  @JsonProperty("oid")
  @NotNull
  @Pattern(regexp = "([0-2])((\\.0)|(\\.[1-9][0-9]*))*$")
  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  public PutBlockedUserRequestDTO oid(String oid) {
    this.oid = oid;
    return this;
  }

  /**
   * A readable name
   *
   * @return displayName
   */
  @JsonProperty("displayName")
  @NotNull
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public PutBlockedUserRequestDTO displayName(String displayName) {
    this.displayName = displayName;
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
    PutBlockedUserRequestDTO putBlockedUserRequestDTO = (PutBlockedUserRequestDTO) o;
    return Objects.equals(this.recordId, putBlockedUserRequestDTO.recordId)
        && Objects.equals(this.oid, putBlockedUserRequestDTO.oid)
        && Objects.equals(this.displayName, putBlockedUserRequestDTO.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordId, oid, displayName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PutBlockedUserRequestDTO {\n");

    sb.append("    recordId: ").append(toIndentedString(recordId)).append("\n");
    sb.append("    oid: ").append(toIndentedString(oid)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
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
