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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;

/** Consent Decisions Response */
@ApiModel(description = "Consent Decisions Response")
public class ConsentDecision {

  @ApiModelProperty(required = true, value = "")
  @Valid
  private FunctionIdType functionId;

  @ApiModelProperty(required = true, value = "")
  @Valid
  private DecisionType decision;

  /**
   * Get functionId
   *
   * @return functionId
   */
  @JsonProperty("functionId")
  @NotNull
  public FunctionIdType getFunctionId() {
    return functionId;
  }

  public void setFunctionId(FunctionIdType functionId) {
    this.functionId = functionId;
  }

  public ConsentDecision functionId(FunctionIdType functionId) {
    this.functionId = functionId;
    return this;
  }

  /**
   * Get decision
   *
   * @return decision
   */
  @JsonProperty("decision")
  @NotNull
  public DecisionType getDecision() {
    return decision;
  }

  public void setDecision(DecisionType decision) {
    this.decision = decision;
  }

  public ConsentDecision decision(DecisionType decision) {
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
    ConsentDecision consentDecision = (ConsentDecision) o;
    return Objects.equals(this.functionId, consentDecision.functionId)
        && Objects.equals(this.decision, consentDecision.decision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(functionId, decision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConsentDecision {\n");

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
