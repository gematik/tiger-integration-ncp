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

@JsonTypeName("_erp_testdriver_api_v1_prescription__id__patch_request")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class ErpTestdriverApiV1PrescriptionIdPatchRequest {
  private Boolean euRedeemableByPatient;

  protected ErpTestdriverApiV1PrescriptionIdPatchRequest(
      ErpTestdriverApiV1PrescriptionIdPatchRequestBuilder<?, ?> b) {
    this.euRedeemableByPatient = b.euRedeemableByPatient;
  }

  public ErpTestdriverApiV1PrescriptionIdPatchRequest() {}

  /** true, if an e-prescription has been marked by patient for redemption in another EU country */
  public ErpTestdriverApiV1PrescriptionIdPatchRequest euRedeemableByPatient(
      Boolean euRedeemableByPatient) {
    this.euRedeemableByPatient = euRedeemableByPatient;
    return this;
  }

  @ApiModelProperty(
      example = "false",
      required = true,
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErpTestdriverApiV1PrescriptionIdPatchRequest erpTestdriverApiV1PrescriptionIdPatchRequest =
        (ErpTestdriverApiV1PrescriptionIdPatchRequest) o;
    return Objects.equals(
        this.euRedeemableByPatient,
        erpTestdriverApiV1PrescriptionIdPatchRequest.euRedeemableByPatient);
  }

  @Override
  public int hashCode() {
    return Objects.hash(euRedeemableByPatient);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErpTestdriverApiV1PrescriptionIdPatchRequest {\n");

    sb.append("    euRedeemableByPatient: ")
        .append(toIndentedString(euRedeemableByPatient))
        .append("\n");
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

  public static ErpTestdriverApiV1PrescriptionIdPatchRequestBuilder<?, ?> builder() {
    return new ErpTestdriverApiV1PrescriptionIdPatchRequestBuilderImpl();
  }

  private static class ErpTestdriverApiV1PrescriptionIdPatchRequestBuilderImpl
      extends ErpTestdriverApiV1PrescriptionIdPatchRequestBuilder<
          ErpTestdriverApiV1PrescriptionIdPatchRequest,
          ErpTestdriverApiV1PrescriptionIdPatchRequestBuilderImpl> {

    @Override
    protected ErpTestdriverApiV1PrescriptionIdPatchRequestBuilderImpl self() {
      return this;
    }

    @Override
    public ErpTestdriverApiV1PrescriptionIdPatchRequest build() {
      return new ErpTestdriverApiV1PrescriptionIdPatchRequest(this);
    }
  }

  public abstract static class ErpTestdriverApiV1PrescriptionIdPatchRequestBuilder<
      C extends ErpTestdriverApiV1PrescriptionIdPatchRequest,
      B extends ErpTestdriverApiV1PrescriptionIdPatchRequestBuilder<C, B>> {
    private Boolean euRedeemableByPatient;

    protected abstract B self();

    public abstract C build();

    public B euRedeemableByPatient(Boolean euRedeemableByPatient) {
      this.euRedeemableByPatient = euRedeemableByPatient;
      return self();
    }
  }
}
