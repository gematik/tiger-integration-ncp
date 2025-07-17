/*
 * Copyright 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class SearchSetBundleLinkInner {

  public enum RelationEnum {
    SELF(String.valueOf("self")),
    NEXT(String.valueOf("next")),
    PREVIOUS(String.valueOf("previous")),
    FIRST(String.valueOf("first")),
    LAST(String.valueOf("last"));

    private String value;

    RelationEnum(String v) {
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
    public static RelationEnum fromValue(String value) {
      for (RelationEnum b : RelationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "")
  private RelationEnum relation;

  @ApiModelProperty(
      value =
          "Reference details for the link, A Uniform Resource Locator (RFC 1738 ). Note URLs are accessed directly using the specified protocol. Common URL protocols are http{s}.")
  /**
   * Reference details for the link, A Uniform Resource Locator (RFC 1738 ). Note URLs are accessed
   * directly using the specified protocol. Common URL protocols are http{s}.
   */
  private String url;

  /**
   * Get relation
   *
   * @return relation
   */
  @JsonProperty("relation")
  public String getRelation() {
    if (relation == null) {
      return null;
    }
    return relation.value();
  }

  public void setRelation(RelationEnum relation) {
    this.relation = relation;
  }

  public SearchSetBundleLinkInner relation(RelationEnum relation) {
    this.relation = relation;
    return this;
  }

  /**
   * Reference details for the link, A Uniform Resource Locator (RFC 1738 ). Note URLs are accessed
   * directly using the specified protocol. Common URL protocols are http{s}.
   *
   * @return url
   */
  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public SearchSetBundleLinkInner url(String url) {
    this.url = url;
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
    SearchSetBundleLinkInner searchSetBundleLinkInner = (SearchSetBundleLinkInner) o;
    return Objects.equals(this.relation, searchSetBundleLinkInner.relation)
        && Objects.equals(this.url, searchSetBundleLinkInner.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relation, url);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchSetBundleLinkInner {\n");

    sb.append("    relation: ").append(toIndentedString(relation)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
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
