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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "selectorType",
    visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = EntryUUID.class, name = "ENTRY_ID"),
  @JsonSubTypes.Type(value = Title.class, name = "TITLE"),
  @JsonSubTypes.Type(value = UniqueId.class, name = "UNIQUE_ID"),
})
public class ObjectSelectorItem {

  public enum SelectorTypeEnum {
    TITLE(String.valueOf("TITLE")),
    ENTRY_ID(String.valueOf("ENTRY_ID")),
    UNIQUE_ID(String.valueOf("UNIQUE_ID"));

    private String value;

    SelectorTypeEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SelectorTypeEnum fromValue(String value) {
      for (SelectorTypeEnum b : SelectorTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(required = true, value = "")
  private SelectorTypeEnum selectorType;

  /**
   * Get selectorType
   *
   * @return selectorType
   */
  @JsonProperty("selectorType")
  @NotNull
  public String getSelectorType() {
    if (selectorType == null) {
      return null;
    }
    return selectorType.value();
  }

  public void setSelectorType(SelectorTypeEnum selectorType) {
    this.selectorType = selectorType;
  }

  public ObjectSelectorItem selectorType(SelectorTypeEnum selectorType) {
    this.selectorType = selectorType;
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
    ObjectSelectorItem objectSelectorItem = (ObjectSelectorItem) o;
    return Objects.equals(this.selectorType, objectSelectorItem.selectorType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(selectorType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ObjectSelectorItem {\n");

    sb.append("    selectorType: ").append(toIndentedString(selectorType)).append("\n");
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
