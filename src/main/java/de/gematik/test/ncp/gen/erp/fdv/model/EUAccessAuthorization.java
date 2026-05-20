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
import java.time.OffsetDateTime;
import java.util.Objects;

@JsonTypeName("EU-AccessAuthorization")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class EUAccessAuthorization {
  private String country;
  private String accessCode;
  private OffsetDateTime validUntil;
  private OffsetDateTime createdAt;

  protected EUAccessAuthorization(EUAccessAuthorizationBuilder<?, ?> b) {
    this.country = b.country;
    this.accessCode = b.accessCode;
    this.validUntil = b.validUntil;
    this.createdAt = b.createdAt;
  }

  public EUAccessAuthorization() {}

  /** iso:3166 */
  public EUAccessAuthorization country(String country) {
    this.country = country;
    return this;
  }

  @ApiModelProperty(example = "GR", value = "iso:3166")
  @JsonProperty("country")
  public String getCountry() {
    return country;
  }

  @JsonProperty("country")
  public void setCountry(String country) {
    this.country = country;
  }

  /** https://gemspec.gematik.de/docs/gemF/gemF_eRp_EU/gemF_eRp_EU_V1.0.0/#A_27097 */
  public EUAccessAuthorization accessCode(String accessCode) {
    this.accessCode = accessCode;
    return this;
  }

  @ApiModelProperty(
      example = "EU1234",
      value = "https://gemspec.gematik.de/docs/gemF/gemF_eRp_EU/gemF_eRp_EU_V1.0.0/#A_27097")
  @JsonProperty("accessCode")
  public String getAccessCode() {
    return accessCode;
  }

  @JsonProperty("accessCode")
  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  /** */
  public EUAccessAuthorization validUntil(OffsetDateTime validUntil) {
    this.validUntil = validUntil;
    return this;
  }

  @ApiModelProperty(example = "2025-10-01T16:29:00.434Z", value = "")
  @JsonProperty("validUntil")
  public OffsetDateTime getValidUntil() {
    return validUntil;
  }

  @JsonProperty("validUntil")
  public void setValidUntil(OffsetDateTime validUntil) {
    this.validUntil = validUntil;
  }

  /** */
  public EUAccessAuthorization createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  @ApiModelProperty(example = "2025-10-01T15:29:00.434Z", value = "")
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("createdAt")
  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EUAccessAuthorization euAccessAuthorization = (EUAccessAuthorization) o;
    return Objects.equals(this.country, euAccessAuthorization.country)
        && Objects.equals(this.accessCode, euAccessAuthorization.accessCode)
        && Objects.equals(this.validUntil, euAccessAuthorization.validUntil)
        && Objects.equals(this.createdAt, euAccessAuthorization.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(country, accessCode, validUntil, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EUAccessAuthorization {\n");

    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    accessCode: ").append(toIndentedString(accessCode)).append("\n");
    sb.append("    validUntil: ").append(toIndentedString(validUntil)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

  public static EUAccessAuthorizationBuilder<?, ?> builder() {
    return new EUAccessAuthorizationBuilderImpl();
  }

  private static class EUAccessAuthorizationBuilderImpl
      extends EUAccessAuthorizationBuilder<
          EUAccessAuthorization, EUAccessAuthorizationBuilderImpl> {

    @Override
    protected EUAccessAuthorizationBuilderImpl self() {
      return this;
    }

    @Override
    public EUAccessAuthorization build() {
      return new EUAccessAuthorization(this);
    }
  }

  public abstract static class EUAccessAuthorizationBuilder<
      C extends EUAccessAuthorization, B extends EUAccessAuthorizationBuilder<C, B>> {
    private String country;
    private String accessCode;
    private OffsetDateTime validUntil;
    private OffsetDateTime createdAt;

    protected abstract B self();

    public abstract C build();

    public B country(String country) {
      this.country = country;
      return self();
    }

    public B accessCode(String accessCode) {
      this.accessCode = accessCode;
      return self();
    }

    public B validUntil(OffsetDateTime validUntil) {
      this.validUntil = validUntil;
      return self();
    }

    public B createdAt(OffsetDateTime createdAt) {
      this.createdAt = createdAt;
      return self();
    }
  }
}
