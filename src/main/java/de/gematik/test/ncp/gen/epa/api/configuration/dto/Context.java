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

package de.gematik.test.ncp.gen.epa.api.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Werte für den Aufrufkontext in der Kommunikation mit dem Konnektor */
@ApiModel(description = "Werte für den Aufrufkontext in der Kommunikation mit dem Konnektor")
public class Context {

  @ApiModelProperty(value = "")
  private String mandantId;

  @ApiModelProperty(value = "")
  private String clientSystemId;

  @ApiModelProperty(value = "")
  private String workplaceId;

  @ApiModelProperty(value = "")
  private String userId;

  /**
   * Get mandantId
   *
   * @return mandantId
   */
  @JsonProperty("mandantId")
  public String getMandantId() {
    return mandantId;
  }

  public void setMandantId(String mandantId) {
    this.mandantId = mandantId;
  }

  public Context mandantId(String mandantId) {
    this.mandantId = mandantId;
    return this;
  }

  /**
   * Get clientSystemId
   *
   * @return clientSystemId
   */
  @JsonProperty("clientSystemId")
  public String getClientSystemId() {
    return clientSystemId;
  }

  public void setClientSystemId(String clientSystemId) {
    this.clientSystemId = clientSystemId;
  }

  public Context clientSystemId(String clientSystemId) {
    this.clientSystemId = clientSystemId;
    return this;
  }

  /**
   * Get workplaceId
   *
   * @return workplaceId
   */
  @JsonProperty("workplaceId")
  public String getWorkplaceId() {
    return workplaceId;
  }

  public void setWorkplaceId(String workplaceId) {
    this.workplaceId = workplaceId;
  }

  public Context workplaceId(String workplaceId) {
    this.workplaceId = workplaceId;
    return this;
  }

  /**
   * Get userId
   *
   * @return userId
   */
  @JsonProperty("userId")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Context userId(String userId) {
    this.userId = userId;
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
    Context context = (Context) o;
    return Objects.equals(this.mandantId, context.mandantId)
        && Objects.equals(this.clientSystemId, context.clientSystemId)
        && Objects.equals(this.workplaceId, context.workplaceId)
        && Objects.equals(this.userId, context.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mandantId, clientSystemId, workplaceId, userId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Context {\n");

    sb.append("    mandantId: ").append(toIndentedString(mandantId)).append("\n");
    sb.append("    clientSystemId: ").append(toIndentedString(clientSystemId)).append("\n");
    sb.append("    workplaceId: ").append(toIndentedString(workplaceId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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
