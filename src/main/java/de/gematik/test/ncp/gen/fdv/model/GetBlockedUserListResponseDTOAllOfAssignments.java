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
import java.util.Date;
import java.util.Objects;

public class GetBlockedUserListResponseDTOAllOfAssignments {

  @ApiModelProperty(example = "1.2.276.0.76.4.50", required = true, value = "A professionOID")
  /** A professionOID */
  private String oid;

  @ApiModelProperty(
      example = "Praxis Dr. Annamaria Heckhausén",
      required = true,
      value = "A readable name")
  /** A readable name */
  private String displayName;

  @ApiModelProperty(example = "2-883110000118994", required = true, value = "")
  private String telematikId;

  @ApiModelProperty(example = "2025-01-01T18:00Z", required = true, value = "Creation timestamp")
  /** Creation timestamp */
  private Date at;

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

  public GetBlockedUserListResponseDTOAllOfAssignments oid(String oid) {
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

  public GetBlockedUserListResponseDTOAllOfAssignments displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get telematikId
   *
   * @return telematikId
   */
  @JsonProperty("telematikId")
  @NotNull
  @Pattern(regexp = "^[1-9][0-9]?-[\\x21-\\x7E]+$")
  @Size(max = 128)
  public String getTelematikId() {
    return telematikId;
  }

  public void setTelematikId(String telematikId) {
    this.telematikId = telematikId;
  }

  public GetBlockedUserListResponseDTOAllOfAssignments telematikId(String telematikId) {
    this.telematikId = telematikId;
    return this;
  }

  /**
   * Creation timestamp
   *
   * @return at
   */
  @JsonProperty("at")
  @NotNull
  public Date getAt() {
    return at;
  }

  public void setAt(Date at) {
    this.at = at;
  }

  public GetBlockedUserListResponseDTOAllOfAssignments at(Date at) {
    this.at = at;
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
    GetBlockedUserListResponseDTOAllOfAssignments getBlockedUserListResponseDTOAllOfAssignments =
        (GetBlockedUserListResponseDTOAllOfAssignments) o;
    return Objects.equals(this.oid, getBlockedUserListResponseDTOAllOfAssignments.oid)
        && Objects.equals(
            this.displayName, getBlockedUserListResponseDTOAllOfAssignments.displayName)
        && Objects.equals(
            this.telematikId, getBlockedUserListResponseDTOAllOfAssignments.telematikId)
        && Objects.equals(this.at, getBlockedUserListResponseDTOAllOfAssignments.at);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oid, displayName, telematikId, at);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetBlockedUserListResponseDTOAllOfAssignments {\n");

    sb.append("    oid: ").append(toIndentedString(oid)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    telematikId: ").append(toIndentedString(telematikId)).append("\n");
    sb.append("    at: ").append(toIndentedString(at)).append("\n");
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
