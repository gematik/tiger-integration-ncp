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

/** State of consent(permit or deny) to a purpose referring §303e SGB V clause 2 */
@ApiModel(
    description = "State of consent(permit or deny) to a purpose referring §303e SGB V clause 2")
public class ResearchDataPurposeEntry {

  @ApiModelProperty(value = "")
  @Valid
  private PurposeIdType purposeId;

  @ApiModelProperty(value = "")
  @Valid
  private DecisionType decision;

  /**
   * Get purposeId
   *
   * @return purposeId
   */
  @JsonProperty("purposeId")
  public PurposeIdType getPurposeId() {
    return purposeId;
  }

  public void setPurposeId(PurposeIdType purposeId) {
    this.purposeId = purposeId;
  }

  public ResearchDataPurposeEntry purposeId(PurposeIdType purposeId) {
    this.purposeId = purposeId;
    return this;
  }

  /**
   * Get decision
   *
   * @return decision
   */
  @JsonProperty("decision")
  public DecisionType getDecision() {
    return decision;
  }

  public void setDecision(DecisionType decision) {
    this.decision = decision;
  }

  public ResearchDataPurposeEntry decision(DecisionType decision) {
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
    ResearchDataPurposeEntry researchDataPurposeEntry = (ResearchDataPurposeEntry) o;
    return Objects.equals(this.purposeId, researchDataPurposeEntry.purposeId)
        && Objects.equals(this.decision, researchDataPurposeEntry.decision);
  }

  @Override
  public int hashCode() {
    return Objects.hash(purposeId, decision);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResearchDataPurposeEntry {\n");

    sb.append("    purposeId: ").append(toIndentedString(purposeId)).append("\n");
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
