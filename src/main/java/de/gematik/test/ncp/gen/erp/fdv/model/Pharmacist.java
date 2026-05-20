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
import java.util.Objects;

@JsonTypeName("Pharmacist")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Pharmacist {
  private String name;
  private PharmacistIdentifier identifier;

  protected Pharmacist(PharmacistBuilder<?, ?> b) {
    this.name = b.name;
    this.identifier = b.identifier;
  }

  public Pharmacist() {}

  /** */
  public Pharmacist name(String name) {
    this.name = name;
    return this;
  }

  @ApiModelProperty(example = "Max Mustermann", value = "")
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /** */
  public Pharmacist identifier(PharmacistIdentifier identifier) {
    this.identifier = identifier;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("identifier")
  public PharmacistIdentifier getIdentifier() {
    return identifier;
  }

  @JsonProperty("identifier")
  public void setIdentifier(PharmacistIdentifier identifier) {
    this.identifier = identifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pharmacist pharmacist = (Pharmacist) o;
    return Objects.equals(this.name, pharmacist.name)
        && Objects.equals(this.identifier, pharmacist.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, identifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pharmacist {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
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

  public static PharmacistBuilder<?, ?> builder() {
    return new PharmacistBuilderImpl();
  }

  private static class PharmacistBuilderImpl
      extends PharmacistBuilder<Pharmacist, PharmacistBuilderImpl> {

    @Override
    protected PharmacistBuilderImpl self() {
      return this;
    }

    @Override
    public Pharmacist build() {
      return new Pharmacist(this);
    }
  }

  public abstract static class PharmacistBuilder<
      C extends Pharmacist, B extends PharmacistBuilder<C, B>> {
    private String name;
    private PharmacistIdentifier identifier;

    protected abstract B self();

    public abstract C build();

    public B name(String name) {
      this.name = name;
      return self();
    }

    public B identifier(PharmacistIdentifier identifier) {
      this.identifier = identifier;
      return self();
    }
  }
}
