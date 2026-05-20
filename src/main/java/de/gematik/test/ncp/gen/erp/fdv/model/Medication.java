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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@JsonTypeName("Medication")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Medication {
  public enum TypeEnum {
    FREITEXT(String.valueOf("freitext")),
    PZN(String.valueOf("pzn")),
    REZEPTUR(String.valueOf("rezeptur")),
    WIRKSTOFF(String.valueOf("wirkstoff"));

    private String value;

    TypeEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    /**
     * Convert a String into String, as specified in the <a
     * href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS
     * 2.0 Specification, section 3.2, p. 12</a>
     */
    public static TypeEnum fromString(String s) {
      for (TypeEnum b : TypeEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;
  private String code;
  private Boolean isVaccine;

  protected Medication(MedicationBuilder<?, ?> b) {
    this.type = b.type;
    this.code = b.code;
    this.isVaccine = b.isVaccine;
  }

  public Medication() {}

  /** */
  public Medication type(TypeEnum type) {
    this.type = type;
    return this;
  }

  @ApiModelProperty(example = "pzn", required = true, value = "")
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(TypeEnum type) {
    this.type = type;
  }

  /** Contains value for PZN, recipe, ingredient or a free text of a medication */
  public Medication code(String code) {
    this.code = code;
    return this;
  }

  @ApiModelProperty(
      example = "Nerisona 30g, Asche Basis 60g",
      required = true,
      value = "Contains value for PZN, recipe, ingredient or a free text of a medication")
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  @JsonProperty("code")
  public void setCode(String code) {
    this.code = code;
  }

  /** */
  public Medication isVaccine(Boolean isVaccine) {
    this.isVaccine = isVaccine;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("isVaccine")
  public Boolean getIsVaccine() {
    return isVaccine;
  }

  @JsonProperty("isVaccine")
  public void setIsVaccine(Boolean isVaccine) {
    this.isVaccine = isVaccine;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Medication medication = (Medication) o;
    return Objects.equals(this.type, medication.type)
        && Objects.equals(this.code, medication.code)
        && Objects.equals(this.isVaccine, medication.isVaccine);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, code, isVaccine);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Medication {\n");

    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    isVaccine: ").append(toIndentedString(isVaccine)).append("\n");
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

  public static MedicationBuilder<?, ?> builder() {
    return new MedicationBuilderImpl();
  }

  private static class MedicationBuilderImpl
      extends MedicationBuilder<Medication, MedicationBuilderImpl> {

    @Override
    protected MedicationBuilderImpl self() {
      return this;
    }

    @Override
    public Medication build() {
      return new Medication(this);
    }
  }

  public abstract static class MedicationBuilder<
      C extends Medication, B extends MedicationBuilder<C, B>> {
    private TypeEnum type;
    private String code;
    private Boolean isVaccine;

    protected abstract B self();

    public abstract C build();

    public B type(TypeEnum type) {
      this.type = type;
      return self();
    }

    public B code(String code) {
      this.code = code;
      return self();
    }

    public B isVaccine(Boolean isVaccine) {
      this.isVaccine = isVaccine;
      return self();
    }
  }
}
