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
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;
import java.util.UUID;

public class PostRegisterDeviceDefaultResponse {

  @ApiModelProperty(
      example = "74f09dbc-9268-411f-81e7-f1e4f3fb1b4f",
      value = "Unique identifier of a device registration")
  /** Unique identifier of a device registration */
  private UUID deviceIdentifier;

  @ApiModelProperty(
      example = "fe6b98ac23a3df214612fd089494eb2946589012a94e91ba6ed57f9213ba520a",
      value = "Device tokens are strings with 64 hexadecimal symbols.")
  /** Device tokens are strings with 64 hexadecimal symbols. */
  private String deviceToken;

  @ApiModelProperty(value = "")
  @Valid
  private PendingDeviceType data;

  @ApiModelProperty(example = "name@example.com", value = "email address")
  /** email address */
  private String emailNotification;

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

  public PostRegisterDeviceDefaultResponse deviceIdentifier(UUID deviceIdentifier) {
    this.deviceIdentifier = deviceIdentifier;
    return this;
  }

  /**
   * Device tokens are strings with 64 hexadecimal symbols.
   *
   * @return deviceToken
   */
  @JsonProperty("deviceToken")
  public String getDeviceToken() {
    return deviceToken;
  }

  public void setDeviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
  }

  public PostRegisterDeviceDefaultResponse deviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
    return this;
  }

  /**
   * Get data
   *
   * @return data
   */
  @JsonProperty("data")
  public PendingDeviceType getData() {
    return data;
  }

  public void setData(PendingDeviceType data) {
    this.data = data;
  }

  public PostRegisterDeviceDefaultResponse data(PendingDeviceType data) {
    this.data = data;
    return this;
  }

  /**
   * email address
   *
   * @return emailNotification
   */
  @JsonProperty("emailNotification")
  public String getEmailNotification() {
    return emailNotification;
  }

  public void setEmailNotification(String emailNotification) {
    this.emailNotification = emailNotification;
  }

  public PostRegisterDeviceDefaultResponse emailNotification(String emailNotification) {
    this.emailNotification = emailNotification;
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
    PostRegisterDeviceDefaultResponse postRegisterDeviceDefaultResponse =
        (PostRegisterDeviceDefaultResponse) o;
    return Objects.equals(this.deviceIdentifier, postRegisterDeviceDefaultResponse.deviceIdentifier)
        && Objects.equals(this.deviceToken, postRegisterDeviceDefaultResponse.deviceToken)
        && Objects.equals(this.data, postRegisterDeviceDefaultResponse.data)
        && Objects.equals(
            this.emailNotification, postRegisterDeviceDefaultResponse.emailNotification);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceIdentifier, deviceToken, data, emailNotification);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostRegisterDeviceDefaultResponse {\n");

    sb.append("    deviceIdentifier: ").append(toIndentedString(deviceIdentifier)).append("\n");
    sb.append("    deviceToken: ").append(toIndentedString(deviceToken)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    emailNotification: ").append(toIndentedString(emailNotification)).append("\n");
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
