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
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class BaseDeviceType {

  @ApiModelProperty(
      example = "74f09dbc-9268-411f-81e7-f1e4f3fb1b4f",
      value = "Unique identifier of a device registration")
  /** Unique identifier of a device registration */
  private UUID deviceIdentifier;

  @ApiModelProperty(value = "")
  @Valid
  private DeviceStatusType status;

  @ApiModelProperty(example = "Praxis Dr. Annamaria Heckhausén", value = "A readable name")
  /** A readable name */
  private String displayName;

  @ApiModelProperty(value = "timestamp of registration")
  /** timestamp of registration */
  private Date createdAt;

  /**
   * Unique identifier of a device registration
   *
   * @return deviceIdentifier
   */
  @JsonProperty("deviceIdentifier")
  public UUID getDeviceIdentifier() {
    return deviceIdentifier;
  }

  public void setDeviceIdentifier(UUID deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
  }

  public BaseDeviceType deviceIdentifier(UUID deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
    return this;
  }

  /**
   * Get status
   *
   * @return status
   */
  @JsonProperty("status")
  public DeviceStatusType getStatus() {
    return status;
  }

  public void setStatus(DeviceStatusType status) {
    this.status = status;
  }

  public BaseDeviceType status(DeviceStatusType status) {
    this.status = status;
    return this;
  }

  /**
   * A readable name
   *
   * @return displayName
   */
  @JsonProperty("displayName")
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public BaseDeviceType displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * timestamp of registration
   *
   * @return createdAt
   */
  @JsonProperty("createdAt")
  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public BaseDeviceType createdAt(Date createdAt) {
    this.createdAt = createdAt;
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
    BaseDeviceType baseDeviceType = (BaseDeviceType) o;
    return Objects.equals(this.deviceIdentifier, baseDeviceType.deviceIdentifier)
        && Objects.equals(this.status, baseDeviceType.status)
        && Objects.equals(this.displayName, baseDeviceType.displayName)
        && Objects.equals(this.createdAt, baseDeviceType.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceIdentifier, status, displayName, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseDeviceType {\n");

    sb.append("    deviceIdentifier: ").append(toIndentedString(deviceIdentifier)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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
