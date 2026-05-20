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

@JsonTypeName("_erp_testdriver_api_v1_pharmacy_assignment_post_request")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class ErpTestdriverApiV1PharmacyAssignmentPostRequest {
  private String prescriptionId;
  private String telematikId;
  private SupplyOptionsType supplyOptionsType;

  protected ErpTestdriverApiV1PharmacyAssignmentPostRequest(
      ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilder<?, ?> b) {
    this.prescriptionId = b.prescriptionId;
    this.telematikId = b.telematikId;
    this.supplyOptionsType = b.supplyOptionsType;
  }

  public ErpTestdriverApiV1PharmacyAssignmentPostRequest() {}

  /** See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid */
  public ErpTestdriverApiV1PharmacyAssignmentPostRequest prescriptionId(String prescriptionId) {
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

  /** https://gematik.de/fhir/sid/telematik-id */
  public ErpTestdriverApiV1PharmacyAssignmentPostRequest telematikId(String telematikId) {
    this.telematikId = telematikId;
    return this;
  }

  @ApiModelProperty(
      example = "3-1.54.10123404",
      required = true,
      value = "https://gematik.de/fhir/sid/telematik-id")
  @JsonProperty("telematikId")
  public String getTelematikId() {
    return telematikId;
  }

  @JsonProperty("telematikId")
  public void setTelematikId(String telematikId) {
    this.telematikId = telematikId;
  }

  /** */
  public ErpTestdriverApiV1PharmacyAssignmentPostRequest supplyOptionsType(
      SupplyOptionsType supplyOptionsType) {
    this.supplyOptionsType = supplyOptionsType;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("supplyOptionsType")
  public SupplyOptionsType getSupplyOptionsType() {
    return supplyOptionsType;
  }

  @JsonProperty("supplyOptionsType")
  public void setSupplyOptionsType(SupplyOptionsType supplyOptionsType) {
    this.supplyOptionsType = supplyOptionsType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErpTestdriverApiV1PharmacyAssignmentPostRequest
        erpTestdriverApiV1PharmacyAssignmentPostRequest =
            (ErpTestdriverApiV1PharmacyAssignmentPostRequest) o;
    return Objects.equals(
            this.prescriptionId, erpTestdriverApiV1PharmacyAssignmentPostRequest.prescriptionId)
        && Objects.equals(
            this.telematikId, erpTestdriverApiV1PharmacyAssignmentPostRequest.telematikId)
        && Objects.equals(
            this.supplyOptionsType,
            erpTestdriverApiV1PharmacyAssignmentPostRequest.supplyOptionsType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(prescriptionId, telematikId, supplyOptionsType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErpTestdriverApiV1PharmacyAssignmentPostRequest {\n");

    sb.append("    prescriptionId: ").append(toIndentedString(prescriptionId)).append("\n");
    sb.append("    telematikId: ").append(toIndentedString(telematikId)).append("\n");
    sb.append("    supplyOptionsType: ").append(toIndentedString(supplyOptionsType)).append("\n");
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

  public static ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilder<?, ?> builder() {
    return new ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilderImpl();
  }

  private static class ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilderImpl
      extends ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilder<
          ErpTestdriverApiV1PharmacyAssignmentPostRequest,
          ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilderImpl> {

    @Override
    protected ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilderImpl self() {
      return this;
    }

    @Override
    public ErpTestdriverApiV1PharmacyAssignmentPostRequest build() {
      return new ErpTestdriverApiV1PharmacyAssignmentPostRequest(this);
    }
  }

  public abstract static class ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilder<
      C extends ErpTestdriverApiV1PharmacyAssignmentPostRequest,
      B extends ErpTestdriverApiV1PharmacyAssignmentPostRequestBuilder<C, B>> {
    private String prescriptionId;
    private String telematikId;
    private SupplyOptionsType supplyOptionsType;

    protected abstract B self();

    public abstract C build();

    public B prescriptionId(String prescriptionId) {
      this.prescriptionId = prescriptionId;
      return self();
    }

    public B telematikId(String telematikId) {
      this.telematikId = telematikId;
      return self();
    }

    public B supplyOptionsType(SupplyOptionsType supplyOptionsType) {
      this.supplyOptionsType = supplyOptionsType;
      return self();
    }
  }
}
