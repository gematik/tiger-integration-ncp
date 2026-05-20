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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.Objects;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "dispenseType",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = DeMedicationDispense.class, name = "DE"),
  @JsonSubTypes.Type(value = EUMedicationDispense.class, name = "EU"),
})
/** Includes reduced dispensing information for an e-prescription */
@ApiModel(description = "Includes reduced dispensing information for an e-prescription")
@JsonTypeName("MedicationDispense")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class MedicationDispense {
  public enum DispenseTypeEnum {
    EU(String.valueOf("EU")),
    DE(String.valueOf("DE"));

    private String value;

    DispenseTypeEnum(String v) {
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
    public static DispenseTypeEnum fromString(String s) {
      for (DispenseTypeEnum b : DispenseTypeEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static DispenseTypeEnum fromValue(String value) {
      for (DispenseTypeEnum b : DispenseTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DispenseTypeEnum dispenseType;
  private String prescriptionId;
  private Medication medication;
  private LocalDate whenhandedover;

  protected MedicationDispense(MedicationDispenseBuilder<?, ?> b) {
    this.dispenseType = b.dispenseType;
    this.prescriptionId = b.prescriptionId;
    this.medication = b.medication;
    this.whenhandedover = b.whenhandedover;
  }

  public MedicationDispense() {}

  /** Discriminator field to distinguish between EU and DE medication dispenses */
  public MedicationDispense dispenseType(DispenseTypeEnum dispenseType) {
    this.dispenseType = dispenseType;
    return this;
  }

  @ApiModelProperty(
      required = true,
      value = "Discriminator field to distinguish between EU and DE medication dispenses")
  @JsonProperty("dispenseType")
  public DispenseTypeEnum getDispenseType() {
    return dispenseType;
  }

  @JsonProperty("dispenseType")
  public void setDispenseType(DispenseTypeEnum dispenseType) {
    this.dispenseType = dispenseType;
  }

  /** See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid */
  public MedicationDispense prescriptionId(String prescriptionId) {
    this.prescriptionId = prescriptionId;
    return this;
  }

  @ApiModelProperty(
      example = "160.000.000.000.123.76",
      required = true,
      value = "See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid")
  @JsonProperty("prescriptionId")
  public String getPrescriptionId() {
    return prescriptionId;
  }

  @JsonProperty("prescriptionId")
  public void setPrescriptionId(String prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /** */
  public MedicationDispense medication(Medication medication) {
    this.medication = medication;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("medication")
  public Medication getMedication() {
    return medication;
  }

  @JsonProperty("medication")
  public void setMedication(Medication medication) {
    this.medication = medication;
  }

  /** */
  public MedicationDispense whenhandedover(LocalDate whenhandedover) {
    this.whenhandedover = whenhandedover;
    return this;
  }

  @ApiModelProperty(example = "Fri Mar 20 01:00:00 CET 2020", required = true, value = "")
  @JsonProperty("whenhandedover")
  public LocalDate getWhenhandedover() {
    return whenhandedover;
  }

  @JsonProperty("whenhandedover")
  public void setWhenhandedover(LocalDate whenhandedover) {
    this.whenhandedover = whenhandedover;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MedicationDispense medicationDispense = (MedicationDispense) o;
    return Objects.equals(this.dispenseType, medicationDispense.dispenseType)
        && Objects.equals(this.prescriptionId, medicationDispense.prescriptionId)
        && Objects.equals(this.medication, medicationDispense.medication)
        && Objects.equals(this.whenhandedover, medicationDispense.whenhandedover);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dispenseType, prescriptionId, medication, whenhandedover);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MedicationDispense {\n");

    sb.append("    dispenseType: ").append(toIndentedString(dispenseType)).append("\n");
    sb.append("    prescriptionId: ").append(toIndentedString(prescriptionId)).append("\n");
    sb.append("    medication: ").append(toIndentedString(medication)).append("\n");
    sb.append("    whenhandedover: ").append(toIndentedString(whenhandedover)).append("\n");
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

  public static MedicationDispenseBuilder<?, ?> builder() {
    return new MedicationDispenseBuilderImpl();
  }

  private static class MedicationDispenseBuilderImpl
      extends MedicationDispenseBuilder<MedicationDispense, MedicationDispenseBuilderImpl> {

    @Override
    protected MedicationDispenseBuilderImpl self() {
      return this;
    }

    @Override
    public MedicationDispense build() {
      return new MedicationDispense(this);
    }
  }

  public abstract static class MedicationDispenseBuilder<
      C extends MedicationDispense, B extends MedicationDispenseBuilder<C, B>> {
    private DispenseTypeEnum dispenseType;
    private String prescriptionId;
    private Medication medication;
    private LocalDate whenhandedover;

    protected abstract B self();

    public abstract C build();

    public B dispenseType(DispenseTypeEnum dispenseType) {
      this.dispenseType = dispenseType;
      return self();
    }

    public B prescriptionId(String prescriptionId) {
      this.prescriptionId = prescriptionId;
      return self();
    }

    public B medication(Medication medication) {
      this.medication = medication;
      return self();
    }

    public B whenhandedover(LocalDate whenhandedover) {
      this.whenhandedover = whenhandedover;
      return self();
    }
  }
}
