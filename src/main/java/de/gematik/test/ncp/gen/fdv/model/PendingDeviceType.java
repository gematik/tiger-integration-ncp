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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class PendingDeviceType {

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

  @ApiModelProperty(
      example = "4",
      value = "Indication of acceptable consecutive failed confirmation attemps")
  /** Indication of acceptable consecutive failed confirmation attemps */
  private Integer remainingConfirmationRetries;

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

  public PendingDeviceType deviceIdentifier(UUID deviceIdentifier) {
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

  public PendingDeviceType status(DeviceStatusType status) {
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

  public PendingDeviceType displayName(String displayName) {
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

  public PendingDeviceType createdAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Indication of acceptable consecutive failed confirmation attemps minimum: 0 maximum: 4
   *
   * @return remainingConfirmationRetries
   */
  @JsonProperty("remainingConfirmationRetries")
  @Min(0)
  @Max(4)
  public Integer getRemainingConfirmationRetries() {
    return remainingConfirmationRetries;
  }

  public void setRemainingConfirmationRetries(Integer remainingConfirmationRetries) {
    this.remainingConfirmationRetries = remainingConfirmationRetries;
  }

  public PendingDeviceType remainingConfirmationRetries(Integer remainingConfirmationRetries) {
    this.remainingConfirmationRetries = remainingConfirmationRetries;
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
    PendingDeviceType pendingDeviceType = (PendingDeviceType) o;
    return Objects.equals(this.deviceIdentifier, pendingDeviceType.deviceIdentifier)
        && Objects.equals(this.status, pendingDeviceType.status)
        && Objects.equals(this.displayName, pendingDeviceType.displayName)
        && Objects.equals(this.createdAt, pendingDeviceType.createdAt)
        && Objects.equals(
            this.remainingConfirmationRetries, pendingDeviceType.remainingConfirmationRetries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        deviceIdentifier, status, displayName, createdAt, remainingConfirmationRetries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendingDeviceType {\n");

    sb.append("    deviceIdentifier: ").append(toIndentedString(deviceIdentifier)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    remainingConfirmationRetries: ")
        .append(toIndentedString(remainingConfirmationRetries))
        .append("\n");
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
