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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Contains a collection of resources + Rule: entry.search only when a search */
@ApiModel(
    description = "Contains a collection of resources + Rule: entry.search only when a search")
public class SearchSetBundle {

  public enum ResourceTypeEnum {
    BUNDLE(String.valueOf("Bundle"));

    private String value;

    ResourceTypeEnum(String v) {
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
    public static ResourceTypeEnum fromValue(String value) {
      for (ResourceTypeEnum b : ResourceTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "")
  private ResourceTypeEnum resourceType;

  @ApiModelProperty(
      example = "86604fc1-b356-57e3-8738-8ef36c8d608c",
      value =
          "The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.")
  /**
   * The logical id of the resource, as used in the URL for the resource. Once assigned, this value
   * never changes.
   */
  private String id;

  @ApiModelProperty(value = "")
  @Valid
  private SearchSetBundleMeta meta;

  public enum TypeEnum {
    SEARCHSET(String.valueOf("searchset"));

    private String value;

    TypeEnum(String v) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "")
  private TypeEnum type;

  @ApiModelProperty(example = "1", value = "")
  private Integer total;

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid SearchSetBundleLinkInner> link = new ArrayList<>();

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid SearchSetBundleEntryInner> entry = new ArrayList<>();

  /**
   * Get resourceType
   *
   * @return resourceType
   */
  @JsonProperty("resourceType")
  public String getResourceType() {
    if (resourceType == null) {
      return null;
    }
    return resourceType.value();
  }

  public void setResourceType(ResourceTypeEnum resourceType) {
    this.resourceType = resourceType;
  }

  public SearchSetBundle resourceType(ResourceTypeEnum resourceType) {
    this.resourceType = resourceType;
    return this;
  }

  /**
   * The logical id of the resource, as used in the URL for the resource. Once assigned, this value
   * never changes.
   *
   * @return id
   */
  @JsonProperty("id")
  @Pattern(regexp = "[A-Za-z0-9\\-\\.]{1,64}")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SearchSetBundle id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get meta
   *
   * @return meta
   */
  @JsonProperty("meta")
  public SearchSetBundleMeta getMeta() {
    return meta;
  }

  public void setMeta(SearchSetBundleMeta meta) {
    this.meta = meta;
  }

  public SearchSetBundle meta(SearchSetBundleMeta meta) {
    this.meta = meta;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @JsonProperty("type")
  public String getType() {
    if (type == null) {
      return null;
    }
    return type.value();
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public SearchSetBundle type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get total minimum: 0
   *
   * @return total
   */
  @JsonProperty("total")
  @Min(0)
  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public SearchSetBundle total(Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get link
   *
   * @return link
   */
  @JsonProperty("link")
  public List<@Valid SearchSetBundleLinkInner> getLink() {
    return link;
  }

  public void setLink(List<@Valid SearchSetBundleLinkInner> link) {
    this.link = link;
  }

  public SearchSetBundle link(List<@Valid SearchSetBundleLinkInner> link) {
    this.link = link;
    return this;
  }

  public SearchSetBundle addLinkItem(SearchSetBundleLinkInner linkItem) {
    this.link.add(linkItem);
    return this;
  }

  /**
   * Get entry
   *
   * @return entry
   */
  @JsonProperty("entry")
  public List<@Valid SearchSetBundleEntryInner> getEntry() {
    return entry;
  }

  public void setEntry(List<@Valid SearchSetBundleEntryInner> entry) {
    this.entry = entry;
  }

  public SearchSetBundle entry(List<@Valid SearchSetBundleEntryInner> entry) {
    this.entry = entry;
    return this;
  }

  public SearchSetBundle addEntryItem(SearchSetBundleEntryInner entryItem) {
    this.entry.add(entryItem);
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
    SearchSetBundle searchSetBundle = (SearchSetBundle) o;
    return Objects.equals(this.resourceType, searchSetBundle.resourceType)
        && Objects.equals(this.id, searchSetBundle.id)
        && Objects.equals(this.meta, searchSetBundle.meta)
        && Objects.equals(this.type, searchSetBundle.type)
        && Objects.equals(this.total, searchSetBundle.total)
        && Objects.equals(this.link, searchSetBundle.link)
        && Objects.equals(this.entry, searchSetBundle.entry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resourceType, id, meta, type, total, link, entry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchSetBundle {\n");

    sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    entry: ").append(toIndentedString(entry)).append("\n");
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
