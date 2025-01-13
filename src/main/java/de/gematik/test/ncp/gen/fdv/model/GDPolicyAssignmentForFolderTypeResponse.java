/*
 * Copyright (c) 2024-2025 gematik GmbH
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
 */

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class GDPolicyAssignmentForFolderTypeResponse extends DenyPolicyAssignmentResponseType {

  @ApiModelProperty(
      example = "urn:uuid:4fa70820-2384-4001-80a9-7bbd5e085efb",
      required = true,
      value = "Folder.entryUUID")
  /** Folder.entryUUID */
  private String folderUUID;

  /**
   * Folder.entryUUID
   *
   * @return folderUUID
   */
  @JsonProperty("folderUUID")
  @NotNull
  public String getFolderUUID() {
    return folderUUID;
  }

  public void setFolderUUID(String folderUUID) {
    this.folderUUID = folderUUID;
  }

  public GDPolicyAssignmentForFolderTypeResponse folderUUID(String folderUUID) {
    this.folderUUID = folderUUID;
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
    GDPolicyAssignmentForFolderTypeResponse gdPolicyAssignmentForFolderTypeResponse =
        (GDPolicyAssignmentForFolderTypeResponse) o;
    return Objects.equals(this.folderUUID, gdPolicyAssignmentForFolderTypeResponse.folderUUID)
        && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(folderUUID, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GDPolicyAssignmentForFolderTypeResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    folderUUID: ").append(toIndentedString(folderUUID)).append("\n");
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
