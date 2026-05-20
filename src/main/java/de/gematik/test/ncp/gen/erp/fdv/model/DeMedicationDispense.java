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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Includes reduced dispensing information for an e-prescription */
@ApiModel(description = "Includes reduced dispensing information for an e-prescription")
@JsonTypeName("DeMedicationDispense")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class DeMedicationDispense extends MedicationDispense {
  private Pharmacy pharmacy;

  protected DeMedicationDispense(DeMedicationDispenseBuilder<?, ?> b) {
    super(b);
    this.pharmacy = b.pharmacy;
  }

  public DeMedicationDispense() {}

  /** */
  public DeMedicationDispense pharmacy(Pharmacy pharmacy) {
    this.pharmacy = pharmacy;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("pharmacy")
  public Pharmacy getPharmacy() {
    return pharmacy;
  }

  @JsonProperty("pharmacy")
  public void setPharmacy(Pharmacy pharmacy) {
    this.pharmacy = pharmacy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeMedicationDispense deMedicationDispense = (DeMedicationDispense) o;
    return Objects.equals(this.pharmacy, deMedicationDispense.pharmacy) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pharmacy, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeMedicationDispense {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    pharmacy: ").append(toIndentedString(pharmacy)).append("\n");
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

  public static DeMedicationDispenseBuilder<?, ?> builder() {
    return new DeMedicationDispenseBuilderImpl();
  }

  private static class DeMedicationDispenseBuilderImpl
      extends DeMedicationDispenseBuilder<DeMedicationDispense, DeMedicationDispenseBuilderImpl> {

    @Override
    protected DeMedicationDispenseBuilderImpl self() {
      return this;
    }

    @Override
    public DeMedicationDispense build() {
      return new DeMedicationDispense(this);
    }
  }

  public abstract static class DeMedicationDispenseBuilder<
          C extends DeMedicationDispense, B extends DeMedicationDispenseBuilder<C, B>>
      extends MedicationDispenseBuilder<C, B> {
    private Pharmacy pharmacy;

    public B pharmacy(Pharmacy pharmacy) {
      this.pharmacy = pharmacy;
      return self();
    }
  }
}
