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
import java.math.BigDecimal;
import java.util.Objects;

@JsonTypeName("OperationOutcome")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class OperationOutcome {
  private BigDecimal statusCode;
  private String details;

  public enum CodeEnum {
    INVALID(String.valueOf("invalid")),
    UNKNOWN(String.valueOf("unknown")),
    FORBIDDEN(String.valueOf("forbidden")),
    NOT_FOUND(String.valueOf("not-found")),
    NOT_SUPPORTED(String.valueOf("not-supported")),
    CONFLICT(String.valueOf("conflict")),
    PROCESSING(String.valueOf("processing")),
    VALUE(String.valueOf("value")),
    TRANSIENT(String.valueOf("transient"));

    private String value;

    CodeEnum(String v) {
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
    public static CodeEnum fromString(String s) {
      for (CodeEnum b : CodeEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static CodeEnum fromValue(String value) {
      for (CodeEnum b : CodeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CodeEnum code = CodeEnum.PROCESSING;
  private String errorCode;
  private String diagnostics;

  protected OperationOutcome(OperationOutcomeBuilder<?, ?> b) {
    this.statusCode = b.statusCode;
    this.details = b.details;
    this.code = b.code;
    this.errorCode = b.errorCode;
    this.diagnostics = b.diagnostics;
  }

  public OperationOutcome() {}

  /** Http StatusCode minimum: 300 maximum: 500 */
  public OperationOutcome statusCode(BigDecimal statusCode) {
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
  public OperationOutcome details(String details) {
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

  /** http status error type */
  public OperationOutcome code(CodeEnum code) {
    this.code = code;
    return this;
  }

  @ApiModelProperty(required = true, value = "http status error type")
  @JsonProperty("code")
  public CodeEnum getCode() {
    return code;
  }

  @JsonProperty("code")
  public void setCode(CodeEnum code) {
    this.code = code;
  }

  /** */
  public OperationOutcome errorCode(String errorCode) {
    this.errorCode = errorCode;
    return this;
  }

  @ApiModelProperty(example = "1231273123", required = true, value = "")
  @JsonProperty("errorCode")
  public String getErrorCode() {
    return errorCode;
  }

  @JsonProperty("errorCode")
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  /** additional details */
  public OperationOutcome diagnostics(String diagnostics) {
    this.diagnostics = diagnostics;
    return this;
  }

  @ApiModelProperty(example = "", value = "additional details")
  @JsonProperty("diagnostics")
  public String getDiagnostics() {
    return diagnostics;
  }

  @JsonProperty("diagnostics")
  public void setDiagnostics(String diagnostics) {
    this.diagnostics = diagnostics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperationOutcome operationOutcome = (OperationOutcome) o;
    return Objects.equals(this.statusCode, operationOutcome.statusCode)
        && Objects.equals(this.details, operationOutcome.details)
        && Objects.equals(this.code, operationOutcome.code)
        && Objects.equals(this.errorCode, operationOutcome.errorCode)
        && Objects.equals(this.diagnostics, operationOutcome.diagnostics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(statusCode, details, code, errorCode, diagnostics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperationOutcome {\n");

    sb.append("    statusCode: ").append(toIndentedString(statusCode)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    errorCode: ").append(toIndentedString(errorCode)).append("\n");
    sb.append("    diagnostics: ").append(toIndentedString(diagnostics)).append("\n");
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

  public static OperationOutcomeBuilder<?, ?> builder() {
    return new OperationOutcomeBuilderImpl();
  }

  private static class OperationOutcomeBuilderImpl
      extends OperationOutcomeBuilder<OperationOutcome, OperationOutcomeBuilderImpl> {

    @Override
    protected OperationOutcomeBuilderImpl self() {
      return this;
    }

    @Override
    public OperationOutcome build() {
      return new OperationOutcome(this);
    }
  }

  public abstract static class OperationOutcomeBuilder<
      C extends OperationOutcome, B extends OperationOutcomeBuilder<C, B>> {
    private BigDecimal statusCode;
    private String details;
    private CodeEnum code = CodeEnum.PROCESSING;
    private String errorCode;
    private String diagnostics;

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

    public B code(CodeEnum code) {
      this.code = code;
      return self();
    }

    public B errorCode(String errorCode) {
      this.errorCode = errorCode;
      return self();
    }

    public B diagnostics(String diagnostics) {
      this.diagnostics = diagnostics;
      return self();
    }
  }
}
