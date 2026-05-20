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

package de.gematik.test.ncp.gen.epa.api.documents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Ergebnisse einer Suche im Aktenkonto */
@ApiModel(description = "Ergebnisse einer Suche im Aktenkonto")
public class FindObjectsResponseDTO {

  @ApiModelProperty(
      required = true,
      value =
          "Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte")
  /**
   * Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte
   */
  private Boolean success;

  @ApiModelProperty(
      value =
          "Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.")
  /**
   * Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.
   */
  private String statusMessage;

  @ApiModelProperty(value = "")
  private RegistryObjectLists registryObjectLists;

  /**
   * Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte
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

  public FindObjectsResponseDTO success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.
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

  public FindObjectsResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Get registryObjectLists
   *
   * @return registryObjectLists
   */
  @JsonProperty("registryObjectLists")
  public RegistryObjectLists getRegistryObjectLists() {
    return registryObjectLists;
  }

  public void setRegistryObjectLists(RegistryObjectLists registryObjectLists) {
    this.registryObjectLists = registryObjectLists;
  }

  public FindObjectsResponseDTO registryObjectLists(RegistryObjectLists registryObjectLists) {
    this.registryObjectLists = registryObjectLists;
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
    FindObjectsResponseDTO findObjectsResponseDTO = (FindObjectsResponseDTO) o;
    return Objects.equals(this.success, findObjectsResponseDTO.success)
        && Objects.equals(this.statusMessage, findObjectsResponseDTO.statusMessage)
        && Objects.equals(this.registryObjectLists, findObjectsResponseDTO.registryObjectLists);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, registryObjectLists);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FindObjectsResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    registryObjectLists: ")
        .append(toIndentedString(registryObjectLists))
        .append("\n");
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
