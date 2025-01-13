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

public class PostConfirmPendingDeviceRequest {

  @ApiModelProperty(example = "123456", required = true, value = "")
  private String confirmationCode;

  /**
   * Get confirmationCode
   *
   * @return confirmationCode
   */
  @JsonProperty("confirmationCode")
  @NotNull
  @Pattern(regexp = "^\\d{6}$")
  public String getConfirmationCode() {
    return confirmationCode;
  }

  public void setConfirmationCode(String confirmationCode) {
    this.confirmationCode = confirmationCode;
  }

  public PostConfirmPendingDeviceRequest confirmationCode(String confirmationCode) {
    this.confirmationCode = confirmationCode;
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
    PostConfirmPendingDeviceRequest postConfirmPendingDeviceRequest =
        (PostConfirmPendingDeviceRequest) o;
    return Objects.equals(this.confirmationCode, postConfirmPendingDeviceRequest.confirmationCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(confirmationCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostConfirmPendingDeviceRequest {\n");

    sb.append("    confirmationCode: ").append(toIndentedString(confirmationCode)).append("\n");
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
