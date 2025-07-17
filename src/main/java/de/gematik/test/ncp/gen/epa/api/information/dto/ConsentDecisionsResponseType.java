/*
 * Copyright 2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.gen.epa.api.information.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Consent Decisions Response */
@ApiModel(description = "Consent Decisions Response")
public class ConsentDecisionsResponseType {

  public enum FunctionIdEnum {
    MEDICATION(String.valueOf("medication")),
    ERP_SUBMISSION(String.valueOf("erp-submission"));

    private String value;

    FunctionIdEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static FunctionIdEnum fromValue(String value) {
      for (FunctionIdEnum b : FunctionIdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(
      required = true,
      value = "function id of consent related functions of a health record.")
  /** function id of consent related functions of a health record. */
  private FunctionIdEnum functionId;

  public enum DecisionEnum {
    PERMIT(String.valueOf("permit")),
    DENY(String.valueOf("deny"));

    private String value;

    DecisionEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static DecisionEnum fromValue(String value) {
      for (DecisionEnum b : DecisionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(required = true, value = "Decision")
  /** Decision */
  private DecisionEnum decision;

  /**
   * function id of consent related functions of a health record.
   *
   * @return functionId
   */
  @JsonProperty("functionId")
  public String getFunctionId() {
    if (functionId == null) {
      return null;
    }
    return functionId.value();
  }

  public void setFunctionId(FunctionIdEnum functionId) {
    this.functionId = functionId;
  }

  public ConsentDecisionsResponseType functionId(FunctionIdEnum functionId) {
    this.functionId = functionId;
    return this;
  }

  /**
   * Decision
   *
   * @return decision
   */
  @JsonProperty("decision")
  public String getDecision() {
    if (decision == null) {
      return null;
    }
    return decision.value();
  }

  public void setDecision(DecisionEnum decision) {
    this.decision = decision;
  }

  public ConsentDecisionsResponseType decision(DecisionEnum decision) {
    this.decision = decision;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConsentDecisionsResponseType consentDecisionsResponseType = (ConsentDecisionsResponseType) o;
    return Objects.equals(this.functionId, consentDecisionsResponseType.functionId)
        && Objects.equals(this.decision, consentDecisionsResponseType.decision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(functionId, decision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConsentDecisionsResponseType {\n");

    sb.append("    functionId: ").append(toIndentedString(functionId)).append("\n");
    sb.append("    decision: ").append(toIndentedString(decision)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
