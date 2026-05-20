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
import java.math.BigDecimal;
import java.util.Objects;

@JsonTypeName("Error")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Error {
  private BigDecimal statusCode;
  private String details;

  protected Error(ErrorBuilder<?, ?> b) {
    this.statusCode = b.statusCode;
    this.details = b.details;
  }

  public Error() {}

  /** Http StatusCode minimum: 300 maximum: 500 */
  public Error statusCode(BigDecimal statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  @ApiModelProperty(example = "401", required = true, value = "Http StatusCode")
  @JsonProperty("statusCode")
  public BigDecimal getStatusCode() {
    return statusCode;
  }

  @JsonProperty("statusCode")
  public void setStatusCode(BigDecimal statusCode) {
    this.statusCode = statusCode;
  }

  /** Short Description of the error */
  public Error details(String details) {
    this.details = details;
    return this;
  }

  @ApiModelProperty(
      example = "Missing header field",
      required = true,
      value = "Short Description of the error")
  @JsonProperty("details")
  public String getDetails() {
    return details;
  }

  @JsonProperty("details")
  public void setDetails(String details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Error error = (Error) o;
    return Objects.equals(this.statusCode, error.statusCode)
        && Objects.equals(this.details, error.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Error {\n");

    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

  public static ErrorBuilder<?, ?> builder() {
    return new ErrorBuilderImpl();
  }

  private static class ErrorBuilderImpl extends ErrorBuilder<Error, ErrorBuilderImpl> {

    @Override
    protected ErrorBuilderImpl self() {
      return this;
    }

    @Override
    public Error build() {
      return new Error(this);
    }
  }

  public abstract static class ErrorBuilder<C extends Error, B extends ErrorBuilder<C, B>> {
    private BigDecimal statusCode;
    private String details;

    protected abstract B self();

    public abstract C build();

    public B statusCode(BigDecimal statusCode) {
      this.statusCode = statusCode;
      return self();
    }

    public B details(String details) {
      this.details = details;
      return self();
    }
  }
}
