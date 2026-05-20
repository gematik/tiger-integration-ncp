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

@JsonTypeName("Address")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Address {
  private String city;
  private String street;
  private String streetNr;
  private BigDecimal zipcode;
  private String country;

  protected Address(AddressBuilder<?, ?> b) {
    this.city = b.city;
    this.street = b.street;
    this.streetNr = b.streetNr;
    this.zipcode = b.zipcode;
    this.country = b.country;
  }

  public Address() {}

  /** */
  public Address city(String city) {
    this.city = city;
    return this;
  }

  @ApiModelProperty(example = "Berlin", value = "")
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  @JsonProperty("city")
  public void setCity(String city) {
    this.city = city;
  }

  /** */
  public Address street(String street) {
    this.street = street;
    return this;
  }

  @ApiModelProperty(example = "Friedrichstr.", value = "")
  @JsonProperty("street")
  public String getStreet() {
    return street;
  }

  @JsonProperty("street")
  public void setStreet(String street) {
    this.street = street;
  }

  /** */
  public Address streetNr(String streetNr) {
    this.streetNr = streetNr;
    return this;
  }

  @ApiModelProperty(example = "136", value = "")
  @JsonProperty("streetNr")
  public String getStreetNr() {
    return streetNr;
  }

  @JsonProperty("streetNr")
  public void setStreetNr(String streetNr) {
    this.streetNr = streetNr;
  }

  /** */
  public Address zipcode(BigDecimal zipcode) {
    this.zipcode = zipcode;
    return this;
  }

  @ApiModelProperty(example = "10117", value = "")
  @JsonProperty("zipcode")
  public BigDecimal getZipcode() {
    return zipcode;
  }

  @JsonProperty("zipcode")
  public void setZipcode(BigDecimal zipcode) {
    this.zipcode = zipcode;
  }

  /** */
  public Address country(String country) {
    this.country = country;
    return this;
  }

  @ApiModelProperty(example = "Germany", value = "")
  @JsonProperty("country")
  public String getCountry() {
    return country;
  }

  @JsonProperty("country")
  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return Objects.equals(this.city, address.city)
        && Objects.equals(this.street, address.street)
        && Objects.equals(this.streetNr, address.streetNr)
        && Objects.equals(this.zipcode, address.zipcode)
        && Objects.equals(this.country, address.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, street, streetNr, zipcode, country);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Address {\n");

    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    street: ").append(toIndentedString(street)).append("\n");
    sb.append("    streetNr: ").append(toIndentedString(streetNr)).append("\n");
    sb.append("    zipcode: ").append(toIndentedString(zipcode)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
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

  public static AddressBuilder<?, ?> builder() {
    return new AddressBuilderImpl();
  }

  private static class AddressBuilderImpl extends AddressBuilder<Address, AddressBuilderImpl> {

    @Override
    protected AddressBuilderImpl self() {
      return this;
    }

    @Override
    public Address build() {
      return new Address(this);
    }
  }

  public abstract static class AddressBuilder<C extends Address, B extends AddressBuilder<C, B>> {
    private String city;
    private String street;
    private String streetNr;
    private BigDecimal zipcode;
    private String country;

    protected abstract B self();

    public abstract C build();

    public B city(String city) {
      this.city = city;
      return self();
    }

    public B street(String street) {
      this.street = street;
      return self();
    }

    public B streetNr(String streetNr) {
      this.streetNr = streetNr;
      return self();
    }

    public B zipcode(BigDecimal zipcode) {
      this.zipcode = zipcode;
      return self();
    }

    public B country(String country) {
      this.country = country;
      return self();
    }
  }
}
