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

package de.gematik.test.ncp.gen.epa.api.information.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetConsentDecisionInformationResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  @ApiModelProperty(value = "")
  private List<ConsentDecisionsResponseType> consentDecisions = new ArrayList<>();

  /**
   * Get success
   *
   * @return success
   */
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public GetConsentDecisionInformationResponseDTO success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get statusMessage
   *
   * @return statusMessage
   */
  @JsonProperty("statusMessage")
  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public GetConsentDecisionInformationResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Get consentDecisions
   *
   * @return consentDecisions
   */
  @JsonProperty("consentDecisions")
  public List<ConsentDecisionsResponseType> getConsentDecisions() {
    return consentDecisions;
  }

  public void setConsentDecisions(List<ConsentDecisionsResponseType> consentDecisions) {
    this.consentDecisions = consentDecisions;
  }

  public GetConsentDecisionInformationResponseDTO consentDecisions(
      List<ConsentDecisionsResponseType> consentDecisions) {
    this.consentDecisions = consentDecisions;
    return this;
  }

  public GetConsentDecisionInformationResponseDTO addConsentDecisionsItem(
      ConsentDecisionsResponseType consentDecisionsItem) {
    this.consentDecisions.add(consentDecisionsItem);
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
    GetConsentDecisionInformationResponseDTO getConsentDecisionInformationResponseDTO =
        (GetConsentDecisionInformationResponseDTO) o;
    return Objects.equals(this.success, getConsentDecisionInformationResponseDTO.success)
        && Objects.equals(
            this.statusMessage, getConsentDecisionInformationResponseDTO.statusMessage)
        && Objects.equals(
            this.consentDecisions, getConsentDecisionInformationResponseDTO.consentDecisions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, consentDecisions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetConsentDecisionInformationResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    consentDecisions: ").append(toIndentedString(consentDecisions)).append("\n");
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
