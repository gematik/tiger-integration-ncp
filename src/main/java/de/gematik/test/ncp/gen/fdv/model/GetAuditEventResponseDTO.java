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

/**
 * Returns a list of all audit events using the FHIR representation as string array where the
 * content is JSON
 */
@ApiModel(
    description =
        "Returns a list of all audit events using the FHIR representation as string array where the content is JSON")
public class GetAuditEventResponseDTO {

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

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
  private List<@Valid AuditEventListAllOfEntry> entry = new ArrayList<>();

  /**
   * Get success
   *
   * @return success
   */
  @JsonProperty("success")
  @NotNull
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public GetAuditEventResponseDTO success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get statusMessage
   *
   * @return statusMessage
   */
  @JsonProperty("statusMessage")
  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public GetAuditEventResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

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

  public GetAuditEventResponseDTO resourceType(ResourceTypeEnum resourceType) {
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

  public GetAuditEventResponseDTO id(String id) {
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

  public GetAuditEventResponseDTO meta(SearchSetBundleMeta meta) {
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

  public GetAuditEventResponseDTO type(TypeEnum type) {
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

  public GetAuditEventResponseDTO total(Integer total) {
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

  public GetAuditEventResponseDTO link(List<@Valid SearchSetBundleLinkInner> link) {
    this.link = link;
    return this;
  }

  public GetAuditEventResponseDTO addLinkItem(SearchSetBundleLinkInner linkItem) {
    this.link.add(linkItem);
    return this;
  }

  /**
   * Get entry
   *
   * @return entry
   */
  @JsonProperty("entry")
  public List<@Valid AuditEventListAllOfEntry> getEntry() {
    return entry;
  }

  public void setEntry(List<@Valid AuditEventListAllOfEntry> entry) {
    this.entry = entry;
  }

  public GetAuditEventResponseDTO entry(List<@Valid AuditEventListAllOfEntry> entry) {
    this.entry = entry;
    return this;
  }

  public GetAuditEventResponseDTO addEntryItem(AuditEventListAllOfEntry entryItem) {
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
    GetAuditEventResponseDTO getAuditEventResponseDTO = (GetAuditEventResponseDTO) o;
    return Objects.equals(this.success, getAuditEventResponseDTO.success)
        && Objects.equals(this.statusMessage, getAuditEventResponseDTO.statusMessage)
        && Objects.equals(this.resourceType, getAuditEventResponseDTO.resourceType)
        && Objects.equals(this.id, getAuditEventResponseDTO.id)
        && Objects.equals(this.meta, getAuditEventResponseDTO.meta)
        && Objects.equals(this.type, getAuditEventResponseDTO.type)
        && Objects.equals(this.total, getAuditEventResponseDTO.total)
        && Objects.equals(this.link, getAuditEventResponseDTO.link)
        && Objects.equals(this.entry, getAuditEventResponseDTO.entry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, resourceType, id, meta, type, total, link, entry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAuditEventResponseDTO {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
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
