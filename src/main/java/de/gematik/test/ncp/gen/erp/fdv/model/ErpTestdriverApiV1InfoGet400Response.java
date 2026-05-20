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

@JsonTypeName("_erp_testdriver_api_v1_info_get_400_response")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class ErpTestdriverApiV1InfoGet400Response {
  private String detail;

  protected ErpTestdriverApiV1InfoGet400Response(
      ErpTestdriverApiV1InfoGet400ResponseBuilder<?, ?> b) {
    this.detail = b.detail;
  }

  public ErpTestdriverApiV1InfoGet400Response() {}

  /** */
  public ErpTestdriverApiV1InfoGet400Response detail(String detail) {
    this.detail = detail;
    return this;
  }

  @ApiModelProperty(example = "Test-FdV is currently not running", value = "")
  @JsonProperty("detail")
  public String getDetail() {
    return detail;
  }

  @JsonProperty("detail")
  public void setDetail(String detail) {
    this.detail = detail;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErpTestdriverApiV1InfoGet400Response erpTestdriverApiV1InfoGet400Response =
        (ErpTestdriverApiV1InfoGet400Response) o;
    return Objects.equals(this.detail, erpTestdriverApiV1InfoGet400Response.detail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(detail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErpTestdriverApiV1InfoGet400Response {\n");

    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
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

  public static ErpTestdriverApiV1InfoGet400ResponseBuilder<?, ?> builder() {
    return new ErpTestdriverApiV1InfoGet400ResponseBuilderImpl();
  }

  private static class ErpTestdriverApiV1InfoGet400ResponseBuilderImpl
      extends ErpTestdriverApiV1InfoGet400ResponseBuilder<
          ErpTestdriverApiV1InfoGet400Response, ErpTestdriverApiV1InfoGet400ResponseBuilderImpl> {

    @Override
    protected ErpTestdriverApiV1InfoGet400ResponseBuilderImpl self() {
      return this;
    }

    @Override
    public ErpTestdriverApiV1InfoGet400Response build() {
      return new ErpTestdriverApiV1InfoGet400Response(this);
    }
  }

  public abstract static class ErpTestdriverApiV1InfoGet400ResponseBuilder<
      C extends ErpTestdriverApiV1InfoGet400Response,
      B extends ErpTestdriverApiV1InfoGet400ResponseBuilder<C, B>> {
    private String detail;

    protected abstract B self();

    public abstract C build();

    public B detail(String detail) {
      this.detail = detail;
      return self();
    }
  }
}
