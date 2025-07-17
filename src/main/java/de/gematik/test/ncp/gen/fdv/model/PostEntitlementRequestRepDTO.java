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
import jakarta.validation.constraints.*;
import java.util.Objects;

public class PostEntitlementRequestRepDTO extends PostEntitlementRequestDTO {

  @ApiModelProperty(example = "Z123456789", required = true, value = "")
  private String insurantId;

  @ApiModelProperty(example = "name@example.com", required = true, value = "")
  private String email;

  @ApiModelProperty(
      example = "Praxis Dr. Annamaria Heckhausén",
      required = true,
      value = "A readable name")
  /** A readable name */
  private String displayName;

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

  public PostEntitlementRequestRepDTO insurantId(String insurantId) {
    this.insurantId = insurantId;
    return this;
  }

  /**
   * Get email
   *
   * @return email
   */
  @JsonProperty("email")
  @NotNull
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public PostEntitlementRequestRepDTO email(String email) {
    this.email = email;
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

  public PostEntitlementRequestRepDTO displayName(String displayName) {
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
    PostEntitlementRequestRepDTO postEntitlementRequestRepDTO = (PostEntitlementRequestRepDTO) o;
    return Objects.equals(this.insurantId, postEntitlementRequestRepDTO.insurantId)
        && Objects.equals(this.email, postEntitlementRequestRepDTO.email)
        && Objects.equals(this.displayName, postEntitlementRequestRepDTO.displayName)
        && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(insurantId, email, displayName, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostEntitlementRequestRepDTO {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    insurantId: ").append(toIndentedString(insurantId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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
