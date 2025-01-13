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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

/** Information über ein Befugnis welche eingestellt, gelöscht oder aufgelistet wird */
@ApiModel(
    description =
        "Information über ein Befugnis welche eingestellt, gelöscht oder aufgelistet wird")
public class Entitlement {

  @ApiModelProperty(required = true, value = "Identifier of an actor (kvnr or telematik-id)")
  /** Identifier of an actor (kvnr or telematik-id) */
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

  @ApiModelProperty(example = "2025-01-01T18:00Z", value = "end of the validity")
  /** end of the validity */
  private Date validTo;

  @ApiModelProperty(value = "")
  @Valid
  private EntitlementIssued issued;

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

  public Entitlement actorId(String actorId) {
    this.actorId = actorId;
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

  public Entitlement oid(String oid) {
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

  public Entitlement displayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * end of the validity
   *
   * @return validTo
   */
  @JsonProperty("validTo")
  public Date getValidTo() {
    return validTo;
  }

  public void setValidTo(Date validTo) {
    this.validTo = validTo;
  }

  public Entitlement validTo(Date validTo) {
    this.validTo = validTo;
    return this;
  }

  /**
   * Get issued
   *
   * @return issued
   */
  @JsonProperty("issued")
  public EntitlementIssued getIssued() {
    return issued;
  }

  public void setIssued(EntitlementIssued issued) {
    this.issued = issued;
  }

  public Entitlement issued(EntitlementIssued issued) {
    this.issued = issued;
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
    Entitlement entitlement = (Entitlement) o;
    return Objects.equals(this.actorId, entitlement.actorId)
        && Objects.equals(this.oid, entitlement.oid)
        && Objects.equals(this.displayName, entitlement.displayName)
        && Objects.equals(this.validTo, entitlement.validTo)
        && Objects.equals(this.issued, entitlement.issued);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actorId, oid, displayName, validTo, issued);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Entitlement {\n");

    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
    sb.append("    oid: ").append(toIndentedString(oid)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    validTo: ").append(toIndentedString(validTo)).append("\n");
    sb.append("    issued: ").append(toIndentedString(issued)).append("\n");
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
