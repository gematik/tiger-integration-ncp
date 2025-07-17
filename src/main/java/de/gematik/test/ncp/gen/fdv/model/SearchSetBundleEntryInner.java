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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.Objects;

public class SearchSetBundleEntryInner {

  @ApiModelProperty(value = "URI for resource (Absolute URL server address or URI for UUID/OID)")
  /** URI for resource (Absolute URL server address or URI for UUID/OID) */
  private String fullUrl;

  @ApiModelProperty(value = "")
  @Valid
  private SearchSetBundleEntryInnerSearch search;

  /**
   * URI for resource (Absolute URL server address or URI for UUID/OID)
   *
   * @return fullUrl
   */
  @JsonProperty("fullUrl")
  public String getFullUrl() {
    return fullUrl;
  }

  public void setFullUrl(String fullUrl) {
    this.fullUrl = fullUrl;
  }

  public SearchSetBundleEntryInner fullUrl(String fullUrl) {
    this.fullUrl = fullUrl;
    return this;
  }

  /**
   * Get search
   *
   * @return search
   */
  @JsonProperty("search")
  public SearchSetBundleEntryInnerSearch getSearch() {
    return search;
  }

  public void setSearch(SearchSetBundleEntryInnerSearch search) {
    this.search = search;
  }

  public SearchSetBundleEntryInner search(SearchSetBundleEntryInnerSearch search) {
    this.search = search;
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
    SearchSetBundleEntryInner searchSetBundleEntryInner = (SearchSetBundleEntryInner) o;
    return Objects.equals(this.fullUrl, searchSetBundleEntryInner.fullUrl)
        && Objects.equals(this.search, searchSetBundleEntryInner.search);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullUrl, search);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchSetBundleEntryInner {\n");

    sb.append("    fullUrl: ").append(toIndentedString(fullUrl)).append("\n");
    sb.append("    search: ").append(toIndentedString(search)).append("\n");
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
