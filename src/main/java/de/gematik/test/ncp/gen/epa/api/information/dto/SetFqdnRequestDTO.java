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
import java.util.Objects;

public class SetFqdnRequestDTO {

  @ApiModelProperty(example = "Z123456789", required = true, value = "")
  private String insurantId;

  @ApiModelProperty(required = true, value = "An URI including protocol and port")
  /** An URI including protocol and port */
  private String fqdn;

  /**
   * Get insurantId
   *
   * @return insurantId
   */
  @JsonProperty("insurantId")
  public String getInsurantId() {
    return insurantId;
  }

  public void setInsurantId(String insurantId) {
    this.insurantId = insurantId;
  }

  public SetFqdnRequestDTO insurantId(String insurantId) {
    this.insurantId = insurantId;
    return this;
  }

  /**
   * An URI including protocol and port
   *
   * @return fqdn
   */
  @JsonProperty("fqdn")
  public String getFqdn() {
    return fqdn;
  }

  public void setFqdn(String fqdn) {
    this.fqdn = fqdn;
  }

  public SetFqdnRequestDTO fqdn(String fqdn) {
    this.fqdn = fqdn;
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
    SetFqdnRequestDTO setFqdnRequestDTO = (SetFqdnRequestDTO) o;
    return Objects.equals(this.insurantId, setFqdnRequestDTO.insurantId)
        && Objects.equals(this.fqdn, setFqdnRequestDTO.fqdn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(insurantId, fqdn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetFqdnRequestDTO {\n");

    sb.append("    insurantId: ").append(toIndentedString(insurantId)).append("\n");
    sb.append("    fqdn: ").append(toIndentedString(fqdn)).append("\n");
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
