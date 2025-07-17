/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.gen.epa.api.entitlement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Properties to block one user */
@ApiModel(description = "Properties to block one user")
public class OidAndDisplayname {

  @ApiModelProperty(example = "2-883110000118994", value = "")
  private String actorId;

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
   * Get actorId
   *
   * @return actorId
   */
  @JsonProperty("actorId")
  public String getActorId() {
    return actorId;
  }

  public void setActorId(String actorId) {
    this.actorId = actorId;
  }

  public OidAndDisplayname actorId(String actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * A professionOID
   *
   * @return oid
   */
  @JsonProperty("oid")
  public String getOid() {
    return oid;
  }

  public void setOid(String oid) {
    this.oid = oid;
  }

  public OidAndDisplayname oid(String oid) {
    this.oid = oid;
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

  public OidAndDisplayname displayName(String displayName) {
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
    OidAndDisplayname oidAndDisplayname = (OidAndDisplayname) o;
    return Objects.equals(this.actorId, oidAndDisplayname.actorId)
        && Objects.equals(this.oid, oidAndDisplayname.oid)
        && Objects.equals(this.displayName, oidAndDisplayname.displayName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actorId, oid, displayName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OidAndDisplayname {\n");

    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
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
