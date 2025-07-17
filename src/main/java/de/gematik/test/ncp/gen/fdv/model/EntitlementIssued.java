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
import java.util.Date;
import java.util.Objects;

public class EntitlementIssued {

  @ApiModelProperty(example = "2025-01-01T18:00Z", required = true, value = "Creation timestamp")
  /** Creation timestamp */
  private Date at;

  @ApiModelProperty(required = true, value = "Identifier of an actor (kvnr or telematik-id)")
  /** Identifier of an actor (kvnr or telematik-id) */
  private String actorId;

  @ApiModelProperty(
      example = "Praxis Dr. Annamaria Heckhausén",
      required = true,
      value = "A readable name")
  /** A readable name */
  private String displayName;

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

  public EntitlementIssued at(Date at) {
    this.at = at;
    return this;
  }

  /**
   * Identifier of an actor (kvnr or telematik-id)
   *
   * @return actorId
   */
  @JsonProperty("actorId")
  @NotNull
  @Pattern(regexp = "(^[A-Z]{1}\\d{9}$)|(^[1-9][0-9]?-[\\x21-\\x7E]+$)")
  public String getActorId() {
    return actorId;
  }

  public void setActorId(String actorId) {
    this.actorId = actorId;
  }

  public EntitlementIssued actorId(String actorId) {
    this.actorId = actorId;
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

  public EntitlementIssued displayName(String displayName) {
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
    EntitlementIssued entitlementIssued = (EntitlementIssued) o;
    return Objects.equals(this.at, entitlementIssued.at)
        && Objects.equals(this.actorId, entitlementIssued.actorId)
        && Objects.equals(this.displayName, entitlementIssued.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(at, actorId, displayName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntitlementIssued {\n");

    sb.append("    at: ").append(toIndentedString(at)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
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
