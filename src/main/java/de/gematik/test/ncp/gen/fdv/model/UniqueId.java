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

public class UniqueId extends ObjectSelectorItem {

  @ApiModelProperty(value = "")
  private String uniqueId;

  /**
   * Get uniqueId
   *
   * @return uniqueId
   */
  @JsonProperty("uniqueId")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(final String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public UniqueId uniqueId(final String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final UniqueId uniqueId = (UniqueId) o;
    return Objects.equals(this.uniqueId, uniqueId.uniqueId) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uniqueId, super.hashCode());
  }

  @Override
  public String toString() {
    final String sb =
        "class UniqueId {\n"
            + "    "
            + toIndentedString(super.toString())
            + "\n"
            + "    uniqueId: "
            + toIndentedString(uniqueId)
            + "\n"
            + "}";
    return sb;
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private static String toIndentedString(final Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
