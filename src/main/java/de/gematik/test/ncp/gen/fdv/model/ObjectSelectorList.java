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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** Select a list of objects identified by title, entry uuid or unique id */
@ApiModel(description = "Select a list of objects identified by title, entry uuid or unique id")
public class ObjectSelectorList {

  @ApiModelProperty(required = true, value = "")
  @Valid
  private Set<@Valid ObjectSelectorItem> objectSelectorList = new LinkedHashSet<>();

  /**
   * Get objectSelectorList
   *
   * @return objectSelectorList
   */
  @JsonProperty("objectSelectorList")
  @NotNull
  @Size(min = 1)
  public Set<@Valid ObjectSelectorItem> getObjectSelectorList() {
    return objectSelectorList;
  }

  @JsonDeserialize(as = LinkedHashSet.class)
  public void setObjectSelectorList(Set<@Valid ObjectSelectorItem> objectSelectorList) {
    this.objectSelectorList = objectSelectorList;
  }

  public ObjectSelectorList objectSelectorList(Set<@Valid ObjectSelectorItem> objectSelectorList) {
    this.objectSelectorList = objectSelectorList;
    return this;
  }

  public ObjectSelectorList addObjectSelectorListItem(ObjectSelectorItem objectSelectorListItem) {
    this.objectSelectorList.add(objectSelectorListItem);
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
    ObjectSelectorList objectSelectorList = (ObjectSelectorList) o;
    return Objects.equals(this.objectSelectorList, objectSelectorList.objectSelectorList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectSelectorList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ObjectSelectorList {\n");

    sb.append("    objectSelectorList: ").append(toIndentedString(objectSelectorList)).append("\n");
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
