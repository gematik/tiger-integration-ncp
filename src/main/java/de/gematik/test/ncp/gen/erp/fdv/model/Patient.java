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

@JsonTypeName("Patient")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Patient {
  private String name;
  private String kvnr;

  public enum InsuranceTypeEnum {
    GKV(String.valueOf("GKV")),
    PKV(String.valueOf("PKV"));

    private String value;

    InsuranceTypeEnum(String v) {
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
    public static InsuranceTypeEnum fromString(String s) {
      for (InsuranceTypeEnum b : InsuranceTypeEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static InsuranceTypeEnum fromValue(String value) {
      for (InsuranceTypeEnum b : InsuranceTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private InsuranceTypeEnum insuranceType;

  protected Patient(PatientBuilder<?, ?> b) {
    this.name = b.name;
    this.kvnr = b.kvnr;
    this.insuranceType = b.insuranceType;
  }

  public Patient() {}

  /** */
  public Patient name(String name) {
    this.name = name;
    return this;
  }

  @ApiModelProperty(example = "Juliane Steinmeyer", required = true, value = "")
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /** */
  public Patient kvnr(String kvnr) {
    this.kvnr = kvnr;
    return this;
  }

  @ApiModelProperty(example = "A000500015", required = true, value = "")
  @JsonProperty("kvnr")
  public String getKvnr() {
    return kvnr;
  }

  @JsonProperty("kvnr")
  public void setKvnr(String kvnr) {
    this.kvnr = kvnr;
  }

  /** */
  public Patient insuranceType(InsuranceTypeEnum insuranceType) {
    this.insuranceType = insuranceType;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("insuranceType")
  public InsuranceTypeEnum getInsuranceType() {
    return insuranceType;
  }

  @JsonProperty("insuranceType")
  public void setInsuranceType(InsuranceTypeEnum insuranceType) {
    this.insuranceType = insuranceType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Patient patient = (Patient) o;
    return Objects.equals(this.name, patient.name)
        && Objects.equals(this.kvnr, patient.kvnr)
        && Objects.equals(this.insuranceType, patient.insuranceType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, kvnr, insuranceType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Patient {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
    sb.append("    insuranceType: ").append(toIndentedString(insuranceType)).append("\n");
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

  public static PatientBuilder<?, ?> builder() {
    return new PatientBuilderImpl();
  }

  private static class PatientBuilderImpl extends PatientBuilder<Patient, PatientBuilderImpl> {

    @Override
    protected PatientBuilderImpl self() {
      return this;
    }

    @Override
    public Patient build() {
      return new Patient(this);
    }
  }

  public abstract static class PatientBuilder<C extends Patient, B extends PatientBuilder<C, B>> {
    private String name;
    private String kvnr;
    private InsuranceTypeEnum insuranceType;

    protected abstract B self();

    public abstract C build();

    public B name(String name) {
      this.name = name;
      return self();
    }

    public B kvnr(String kvnr) {
      this.kvnr = kvnr;
      return self();
    }

    public B insuranceType(InsuranceTypeEnum insuranceType) {
      this.insuranceType = insuranceType;
      return self();
    }
  }
}
