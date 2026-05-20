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
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

/** Search related information */
@ApiModel(description = "Search related information")
public class SearchSetBundleEntryInnerSearch {

  public enum ModeEnum {
    MATCH(String.valueOf("match")),
    INCLUDE(String.valueOf("include")),
    OUTCOME(String.valueOf("outcome"));

    private String value;

    ModeEnum(String v) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(
      example = "match",
      value = "match | include | outcome - why this is in the result set")
  /** match | include | outcome - why this is in the result set */
  private ModeEnum mode;

  /**
   * match | include | outcome - why this is in the result set
   *
   * @return mode
   */
  @JsonProperty("mode")
  public String getMode() {
    if (mode == null) {
      return null;
    }
    return mode.value();
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public SearchSetBundleEntryInnerSearch mode(ModeEnum mode) {
    this.mode = mode;
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
    SearchSetBundleEntryInnerSearch searchSetBundleEntryInnerSearch =
        (SearchSetBundleEntryInnerSearch) o;
    return Objects.equals(this.mode, searchSetBundleEntryInnerSearch.mode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchSetBundleEntryInnerSearch {\n");

    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
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
