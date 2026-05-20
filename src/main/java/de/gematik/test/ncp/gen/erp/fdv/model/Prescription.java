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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

/** See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Task */
@ApiModel(description = "See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Task")
@JsonTypeName("Prescription")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Prescription {
  private String prescriptionId;
  private String accessCode;
  private WorkFlow workFlow;
  private LocalDate acceptDate;
  private LocalDate expiryDate;
  private OffsetDateTime authoredOn;
  private OffsetDateTime lastMedicationDispense;

  public enum StatusEnum {
    READY(String.valueOf("ready")),
    IN_PROGRESS(String.valueOf("in-progress")),
    CANCELLED(String.valueOf("cancelled")),
    COMPLETED(String.valueOf("completed"));

    private String value;

    StatusEnum(String v) {
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
    public static StatusEnum fromString(String s) {
      for (StatusEnum b : StatusEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;
  private Boolean euRedeemableByProperties;
  private Boolean euRedeemableByPatient;
  private Patient patient;
  private Practitioner practitioner;
  private Medication medication;

  protected Prescription(PrescriptionBuilder<?, ?> b) {
    this.prescriptionId = b.prescriptionId;
    this.accessCode = b.accessCode;
    this.workFlow = b.workFlow;
    this.acceptDate = b.acceptDate;
    this.expiryDate = b.expiryDate;
    this.authoredOn = b.authoredOn;
    this.lastMedicationDispense = b.lastMedicationDispense;
    this.status = b.status;
    this.euRedeemableByProperties = b.euRedeemableByProperties;
    this.euRedeemableByPatient = b.euRedeemableByPatient;
    this.patient = b.patient;
    this.practitioner = b.practitioner;
    this.medication = b.medication;
  }

  public Prescription() {}

  /** See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid */
  public Prescription prescriptionId(String prescriptionId) {
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

  /** See https://gematik.de/fhir/erp/NamingSystem/GEM_ERP_NS_AccessCode */
  public Prescription accessCode(String accessCode) {
    this.accessCode = accessCode;
    return this;
  }

  @ApiModelProperty(
      example = "c3830cacdc32d8b521dbfc02e5f7102f879f53206b5fca1d80fee3bda969ce4d",
      value = "See https://gematik.de/fhir/erp/NamingSystem/GEM_ERP_NS_AccessCode")
  @JsonProperty("accessCode")
  public String getAccessCode() {
    return accessCode;
  }

  @JsonProperty("accessCode")
  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  /** */
  public Prescription workFlow(WorkFlow workFlow) {
    this.workFlow = workFlow;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("workFlow")
  public WorkFlow getWorkFlow() {
    return workFlow;
  }

  @JsonProperty("workFlow")
  public void setWorkFlow(WorkFlow workFlow) {
    this.workFlow = workFlow;
  }

  /** See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_EX_AcceptDate */
  public Prescription acceptDate(LocalDate acceptDate) {
    this.acceptDate = acceptDate;
    return this;
  }

  @ApiModelProperty(
      example = "Wed Apr 01 02:00:00 CEST 2020",
      required = true,
      value = "See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_EX_AcceptDate")
  @JsonProperty("acceptDate")
  public LocalDate getAcceptDate() {
    return acceptDate;
  }

  @JsonProperty("acceptDate")
  public void setAcceptDate(LocalDate acceptDate) {
    this.acceptDate = acceptDate;
  }

  /** See https://simplifier.net/e-prescription-workflow/gem_erp_ex_expirydate */
  public Prescription expiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
    return this;
  }

  @ApiModelProperty(
      example = "Tue Jun 02 02:00:00 CEST 2020",
      required = true,
      value = "See https://simplifier.net/e-prescription-workflow/gem_erp_ex_expirydate")
  @JsonProperty("expiryDate")
  public LocalDate getExpiryDate() {
    return expiryDate;
  }

  @JsonProperty("expiryDate")
  public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
  }

  /** */
  public Prescription authoredOn(OffsetDateTime authoredOn) {
    this.authoredOn = authoredOn;
    return this;
  }

  @ApiModelProperty(example = "2020-03-02T08:25:05Z", required = true, value = "")
  @JsonProperty("authoredOn")
  public OffsetDateTime getAuthoredOn() {
    return authoredOn;
  }

  @JsonProperty("authoredOn")
  public void setAuthoredOn(OffsetDateTime authoredOn) {
    this.authoredOn = authoredOn;
  }

  /** */
  public Prescription lastMedicationDispense(OffsetDateTime lastMedicationDispense) {
    this.lastMedicationDispense = lastMedicationDispense;
    return this;
  }

  @ApiModelProperty(example = "2024-11-25T14:30Z", value = "")
  @JsonProperty("lastMedicationDispense")
  public OffsetDateTime getLastMedicationDispense() {
    return lastMedicationDispense;
  }

  @JsonProperty("lastMedicationDispense")
  public void setLastMedicationDispense(OffsetDateTime lastMedicationDispense) {
    this.lastMedicationDispense = lastMedicationDispense;
  }

  /** */
  public Prescription status(StatusEnum status) {
    this.status = status;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  /** true, if an e-prescription may be redeemed in another EU country */
  public Prescription euRedeemableByProperties(Boolean euRedeemableByProperties) {
    this.euRedeemableByProperties = euRedeemableByProperties;
    return this;
  }

  @ApiModelProperty(
      example = "true",
      value = "true, if an e-prescription may be redeemed in another EU country")
  @JsonProperty("euRedeemableByProperties")
  public Boolean getEuRedeemableByProperties() {
    return euRedeemableByProperties;
  }

  @JsonProperty("euRedeemableByProperties")
  public void setEuRedeemableByProperties(Boolean euRedeemableByProperties) {
    this.euRedeemableByProperties = euRedeemableByProperties;
  }

  /** true, if an e-prescription has been marked by patient for redemption in another EU country */
  public Prescription euRedeemableByPatient(Boolean euRedeemableByPatient) {
    this.euRedeemableByPatient = euRedeemableByPatient;
    return this;
  }

  @ApiModelProperty(
      example = "false",
      value =
          "true, if an e-prescription has been marked by patient for redemption in another EU country")
  @JsonProperty("euRedeemableByPatient")
  public Boolean getEuRedeemableByPatient() {
    return euRedeemableByPatient;
  }

  @JsonProperty("euRedeemableByPatient")
  public void setEuRedeemableByPatient(Boolean euRedeemableByPatient) {
    this.euRedeemableByPatient = euRedeemableByPatient;
  }

  /** */
  public Prescription patient(Patient patient) {
    this.patient = patient;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("patient")
  public Patient getPatient() {
    return patient;
  }

  @JsonProperty("patient")
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  /** */
  public Prescription practitioner(Practitioner practitioner) {
    this.practitioner = practitioner;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("practitioner")
  public Practitioner getPractitioner() {
    return practitioner;
  }

  @JsonProperty("practitioner")
  public void setPractitioner(Practitioner practitioner) {
    this.practitioner = practitioner;
  }

  /** */
  public Prescription medication(Medication medication) {
    this.medication = medication;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("medication")
  public Medication getMedication() {
    return medication;
  }

  @JsonProperty("medication")
  public void setMedication(Medication medication) {
    this.medication = medication;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Prescription prescription = (Prescription) o;
    return Objects.equals(this.prescriptionId, prescription.prescriptionId)
        && Objects.equals(this.accessCode, prescription.accessCode)
        && Objects.equals(this.workFlow, prescription.workFlow)
        && Objects.equals(this.acceptDate, prescription.acceptDate)
        && Objects.equals(this.expiryDate, prescription.expiryDate)
        && Objects.equals(this.authoredOn, prescription.authoredOn)
        && Objects.equals(this.lastMedicationDispense, prescription.lastMedicationDispense)
        && Objects.equals(this.status, prescription.status)
        && Objects.equals(this.euRedeemableByProperties, prescription.euRedeemableByProperties)
        && Objects.equals(this.euRedeemableByPatient, prescription.euRedeemableByPatient)
        && Objects.equals(this.patient, prescription.patient)
        && Objects.equals(this.practitioner, prescription.practitioner)
        && Objects.equals(this.medication, prescription.medication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        prescriptionId,
        accessCode,
        workFlow,
        acceptDate,
        expiryDate,
        authoredOn,
        lastMedicationDispense,
        status,
        euRedeemableByProperties,
        euRedeemableByPatient,
        patient,
        practitioner,
        medication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Prescription {\n");

    sb.append("    prescriptionId: ").append(toIndentedString(prescriptionId)).append("\n");
    sb.append("    accessCode: ").append(toIndentedString(accessCode)).append("\n");
    sb.append("    workFlow: ").append(toIndentedString(workFlow)).append("\n");
    sb.append("    acceptDate: ").append(toIndentedString(acceptDate)).append("\n");
    sb.append("    expiryDate: ").append(toIndentedString(expiryDate)).append("\n");
    sb.append("    authoredOn: ").append(toIndentedString(authoredOn)).append("\n");
    sb.append("    lastMedicationDispense: ")
        .append(toIndentedString(lastMedicationDispense))
        .append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    euRedeemableByProperties: ")
        .append(toIndentedString(euRedeemableByProperties))
        .append("\n");
    sb.append("    euRedeemableByPatient: ")
        .append(toIndentedString(euRedeemableByPatient))
        .append("\n");
    sb.append("    patient: ").append(toIndentedString(patient)).append("\n");
    sb.append("    practitioner: ").append(toIndentedString(practitioner)).append("\n");
    sb.append("    medication: ").append(toIndentedString(medication)).append("\n");
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

  public static PrescriptionBuilder<?, ?> builder() {
    return new PrescriptionBuilderImpl();
  }

  private static class PrescriptionBuilderImpl
      extends PrescriptionBuilder<Prescription, PrescriptionBuilderImpl> {

    @Override
    protected PrescriptionBuilderImpl self() {
      return this;
    }

    @Override
    public Prescription build() {
      return new Prescription(this);
    }
  }

  public abstract static class PrescriptionBuilder<
      C extends Prescription, B extends PrescriptionBuilder<C, B>> {
    private String prescriptionId;
    private String accessCode;
    private WorkFlow workFlow;
    private LocalDate acceptDate;
    private LocalDate expiryDate;
    private OffsetDateTime authoredOn;
    private OffsetDateTime lastMedicationDispense;
    private StatusEnum status;
    private Boolean euRedeemableByProperties;
    private Boolean euRedeemableByPatient;
    private Patient patient;
    private Practitioner practitioner;
    private Medication medication;

    protected abstract B self();

    public abstract C build();

    public B prescriptionId(String prescriptionId) {
      this.prescriptionId = prescriptionId;
      return self();
    }

    public B accessCode(String accessCode) {
      this.accessCode = accessCode;
      return self();
    }

    public B workFlow(WorkFlow workFlow) {
      this.workFlow = workFlow;
      return self();
    }

    public B acceptDate(LocalDate acceptDate) {
      this.acceptDate = acceptDate;
      return self();
    }

    public B expiryDate(LocalDate expiryDate) {
      this.expiryDate = expiryDate;
      return self();
    }

    public B authoredOn(OffsetDateTime authoredOn) {
      this.authoredOn = authoredOn;
      return self();
    }

    public B lastMedicationDispense(OffsetDateTime lastMedicationDispense) {
      this.lastMedicationDispense = lastMedicationDispense;
      return self();
    }

    public B status(StatusEnum status) {
      this.status = status;
      return self();
    }

    public B euRedeemableByProperties(Boolean euRedeemableByProperties) {
      this.euRedeemableByProperties = euRedeemableByProperties;
      return self();
    }

    public B euRedeemableByPatient(Boolean euRedeemableByPatient) {
      this.euRedeemableByPatient = euRedeemableByPatient;
      return self();
    }

    public B patient(Patient patient) {
      this.patient = patient;
      return self();
    }

    public B practitioner(Practitioner practitioner) {
      this.practitioner = practitioner;
      return self();
    }

    public B medication(Medication medication) {
      this.medication = medication;
      return self();
    }
  }
}
