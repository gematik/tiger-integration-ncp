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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonTypeName("Pharmacy")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Pharmacy {
  private String name;
  private List<Pharmacist> pharmacist = new ArrayList<>();
  private Address address;
  private PharmacyPosition position;

  protected Pharmacy(PharmacyBuilder<?, ?> b) {
    this.name = b.name;
    this.pharmacist = b.pharmacist;
    this.address = b.address;
    this.position = b.position;
  }

  public Pharmacy() {}

  /** */
  public Pharmacy name(String name) {
    this.name = name;
    return this;
  }

  @ApiModelProperty(example = "Adlerapotheke", required = true, value = "")
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /** Must contain at least one pharmacist */
  public Pharmacy pharmacist(List<Pharmacist> pharmacist) {
    this.pharmacist = pharmacist;
    return this;
  }

  @ApiModelProperty(value = "Must contain at least one pharmacist")
  @JsonProperty("pharmacist")
  public List<Pharmacist> getPharmacist() {
    return pharmacist;
  }

  @JsonProperty("pharmacist")
  public void setPharmacist(List<Pharmacist> pharmacist) {
    this.pharmacist = pharmacist;
  }

  public Pharmacy addPharmacistItem(Pharmacist pharmacistItem) {
    if (this.pharmacist == null) {
      this.pharmacist = new ArrayList<>();
    }

    this.pharmacist.add(pharmacistItem);
    return this;
  }

  public Pharmacy removePharmacistItem(Pharmacist pharmacistItem) {
    if (pharmacistItem != null && this.pharmacist != null) {
      this.pharmacist.remove(pharmacistItem);
    }

    return this;
  }

  /** */
  public Pharmacy address(Address address) {
    this.address = address;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("address")
  public Address getAddress() {
    return address;
  }

  @JsonProperty("address")
  public void setAddress(Address address) {
    this.address = address;
  }

  /** */
  public Pharmacy position(PharmacyPosition position) {
    this.position = position;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("position")
  public PharmacyPosition getPosition() {
    return position;
  }

  @JsonProperty("position")
  public void setPosition(PharmacyPosition position) {
    this.position = position;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pharmacy pharmacy = (Pharmacy) o;
    return Objects.equals(this.name, pharmacy.name)
        && Objects.equals(this.pharmacist, pharmacy.pharmacist)
        && Objects.equals(this.address, pharmacy.address)
        && Objects.equals(this.position, pharmacy.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, pharmacist, address, position);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pharmacy {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    pharmacist: ").append(toIndentedString(pharmacist)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
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

  public static PharmacyBuilder<?, ?> builder() {
    return new PharmacyBuilderImpl();
  }

  private static class PharmacyBuilderImpl extends PharmacyBuilder<Pharmacy, PharmacyBuilderImpl> {

    @Override
    protected PharmacyBuilderImpl self() {
      return this;
    }

    @Override
    public Pharmacy build() {
      return new Pharmacy(this);
    }
  }

  public abstract static class PharmacyBuilder<
      C extends Pharmacy, B extends PharmacyBuilder<C, B>> {
    private String name;
    private List<Pharmacist> pharmacist = new ArrayList<>();
    private Address address;
    private PharmacyPosition position;

    protected abstract B self();

    public abstract C build();

    public B name(String name) {
      this.name = name;
      return self();
    }

    public B pharmacist(List<Pharmacist> pharmacist) {
      this.pharmacist = pharmacist;
      return self();
    }

    public B address(Address address) {
      this.address = address;
      return self();
    }

    public B position(PharmacyPosition position) {
      this.position = position;
      return self();
    }
  }
}
