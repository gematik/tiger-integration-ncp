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
@JsonTypeName("EU-MedicationDispense")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class EUMedicationDispense extends MedicationDispense {
  private Pharmacist pharmacist;

  protected EUMedicationDispense(EUMedicationDispenseBuilder<?, ?> b) {
    super(b);
    this.pharmacist = b.pharmacist;
  }

  public EUMedicationDispense() {}

  /** */
  public EUMedicationDispense pharmacist(Pharmacist pharmacist) {
    this.pharmacist = pharmacist;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("pharmacist")
  public Pharmacist getPharmacist() {
    return pharmacist;
  }

  @JsonProperty("pharmacist")
  public void setPharmacist(Pharmacist pharmacist) {
    this.pharmacist = pharmacist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EUMedicationDispense euMedicationDispense = (EUMedicationDispense) o;
    return Objects.equals(this.pharmacist, euMedicationDispense.pharmacist) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pharmacist, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EUMedicationDispense {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    pharmacist: ").append(toIndentedString(pharmacist)).append("\n");
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

  public static EUMedicationDispenseBuilder<?, ?> builder() {
    return new EUMedicationDispenseBuilderImpl();
  }

  private static class EUMedicationDispenseBuilderImpl
      extends EUMedicationDispenseBuilder<EUMedicationDispense, EUMedicationDispenseBuilderImpl> {

    @Override
    protected EUMedicationDispenseBuilderImpl self() {
      return this;
    }

    @Override
    public EUMedicationDispense build() {
      return new EUMedicationDispense(this);
    }
  }

  public abstract static class EUMedicationDispenseBuilder<
          C extends EUMedicationDispense, B extends EUMedicationDispenseBuilder<C, B>>
      extends MedicationDispenseBuilder<C, B> {
    private Pharmacist pharmacist;

    public B pharmacist(Pharmacist pharmacist) {
      this.pharmacist = pharmacist;
      return self();
    }
  }
}
