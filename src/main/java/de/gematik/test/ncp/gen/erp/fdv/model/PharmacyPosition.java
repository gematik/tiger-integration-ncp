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

package de.gematik.test.ncp.gen.erp.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Objects;

@JsonTypeName("Pharmacy_position")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class PharmacyPosition {
  private BigDecimal latitude;
  private BigDecimal longitude;

  protected PharmacyPosition(PharmacyPositionBuilder<?, ?> b) {
    this.latitude = b.latitude;
    this.longitude = b.longitude;
  }

  public PharmacyPosition() {}

  /** */
  public PharmacyPosition latitude(BigDecimal latitude) {
    this.latitude = latitude;
    return this;
  }

  @ApiModelProperty(example = "52.522575", required = true, value = "")
  @JsonProperty("latitude")
  public BigDecimal getLatitude() {
    return latitude;
  }

  @JsonProperty("latitude")
  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  /** */
  public PharmacyPosition longitude(BigDecimal longitude) {
    this.longitude = longitude;
    return this;
  }

  @ApiModelProperty(example = "13.387884", required = true, value = "")
  @JsonProperty("longitude")
  public BigDecimal getLongitude() {
    return longitude;
  }

  @JsonProperty("longitude")
  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PharmacyPosition pharmacyPosition = (PharmacyPosition) o;
    return Objects.equals(this.latitude, pharmacyPosition.latitude)
        && Objects.equals(this.longitude, pharmacyPosition.longitude);
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PharmacyPosition {\n");

    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public static PharmacyPositionBuilder<?, ?> builder() {
    return new PharmacyPositionBuilderImpl();
  }

  private static class PharmacyPositionBuilderImpl
      extends PharmacyPositionBuilder<PharmacyPosition, PharmacyPositionBuilderImpl> {

    @Override
    protected PharmacyPositionBuilderImpl self() {
      return this;
    }

    @Override
    public PharmacyPosition build() {
      return new PharmacyPosition(this);
    }
  }

  public abstract static class PharmacyPositionBuilder<
      C extends PharmacyPosition, B extends PharmacyPositionBuilder<C, B>> {
    private BigDecimal latitude;
    private BigDecimal longitude;

    protected abstract B self();

    public abstract C build();

    public B latitude(BigDecimal latitude) {
      this.latitude = latitude;
      return self();
    }

    public B longitude(BigDecimal longitude) {
      this.longitude = longitude;
      return self();
    }
  }
}
