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
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class LegalPolicyResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean create;

  @ApiModelProperty(required = true, value = "")
  private Boolean read;

  @ApiModelProperty(required = true, value = "")
  private Boolean update;

  @ApiModelProperty(required = true, value = "")
  private Boolean delete;

  /**
   * Get create
   *
   * @return create
   */
  @JsonProperty("Create")
  @NotNull
  public Boolean getCreate() {
    return create;
  }

  public void setCreate(Boolean create) {
    this.create = create;
  }

  public LegalPolicyResponseDTO create(Boolean create) {
    this.create = create;
    return this;
  }

  /**
   * Get read
   *
   * @return read
   */
  @JsonProperty("Read")
  @NotNull
  public Boolean getRead() {
    return read;
  }

  public void setRead(Boolean read) {
    this.read = read;
  }

  public LegalPolicyResponseDTO read(Boolean read) {
    this.read = read;
    return this;
  }

  /**
   * Get update
   *
   * @return update
   */
  @JsonProperty("Update")
  @NotNull
  public Boolean getUpdate() {
    return update;
  }

  public void setUpdate(Boolean update) {
    this.update = update;
  }

  public LegalPolicyResponseDTO update(Boolean update) {
    this.update = update;
    return this;
  }

  /**
   * Get delete
   *
   * @return delete
   */
  @JsonProperty("Delete")
  @NotNull
  public Boolean getDelete() {
    return delete;
  }

  public void setDelete(Boolean delete) {
    this.delete = delete;
  }

  public LegalPolicyResponseDTO delete(Boolean delete) {
    this.delete = delete;
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
    LegalPolicyResponseDTO legalPolicyResponseDTO = (LegalPolicyResponseDTO) o;
    return Objects.equals(this.create, legalPolicyResponseDTO.create)
        && Objects.equals(this.read, legalPolicyResponseDTO.read)
        && Objects.equals(this.update, legalPolicyResponseDTO.update)
        && Objects.equals(this.delete, legalPolicyResponseDTO.delete);
  }

  @Override
  public int hashCode() {
    return Objects.hash(create, read, update, delete);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LegalPolicyResponseDTO {\n");

    sb.append("    create: ").append(toIndentedString(create)).append("\n");
    sb.append("    read: ").append(toIndentedString(read)).append("\n");
    sb.append("    update: ").append(toIndentedString(update)).append("\n");
    sb.append("    delete: ").append(toIndentedString(delete)).append("\n");
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
