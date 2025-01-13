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

package de.gematik.test.ncp.gen.eu.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

/** EntitlementNcpeh object */
@ApiModel(description = "EntitlementNcpeh object")
public class EntitlementNcpeh {

  @ApiModelProperty(example = "Frankreich (FR)", value = "A readable name")
  /** A readable name */
  private String countryName;

  @ApiModelProperty(example = "2025-01-01T18:00Z", value = "")
  private Date validTo;

  @ApiModelProperty(example = "Z123456789", value = "")
  private String kvnr;

  /**
   * A readable name
   *
   * @return countryName
   */
  @JsonProperty("countryName")
  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public EntitlementNcpeh countryName(String countryName) {
    this.countryName = countryName;
    return this;
  }

  /**
   * Get validTo
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

  public EntitlementNcpeh validTo(Date validTo) {
    this.validTo = validTo;
    return this;
  }

  /**
   * Get kvnr
   *
   * @return kvnr
   */
  @JsonProperty("kvnr")
  @Pattern(regexp = "^[A-Z]{1}\\d{9}$")
  public String getKvnr() {
    return kvnr;
  }

  public void setKvnr(String kvnr) {
    this.kvnr = kvnr;
  }

  public EntitlementNcpeh kvnr(String kvnr) {
    this.kvnr = kvnr;
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
    EntitlementNcpeh entitlementNcpeh = (EntitlementNcpeh) o;
    return Objects.equals(this.countryName, entitlementNcpeh.countryName)
        && Objects.equals(this.validTo, entitlementNcpeh.validTo)
        && Objects.equals(this.kvnr, entitlementNcpeh.kvnr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(countryName, validTo, kvnr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EntitlementNcpeh {\n");

    sb.append("    countryName: ").append(toIndentedString(countryName)).append("\n");
    sb.append("    validTo: ").append(toIndentedString(validTo)).append("\n");
    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
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
