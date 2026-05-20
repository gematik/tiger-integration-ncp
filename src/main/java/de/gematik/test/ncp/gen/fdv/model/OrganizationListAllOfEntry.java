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

public class OrganizationListAllOfEntry {

  @ApiModelProperty(
      value = "https://gematik.de/fhir/directory/StructureDefinition/OrganizationDirectory")
  /** https://gematik.de/fhir/directory/StructureDefinition/OrganizationDirectory */
  private Object resource;

  /**
   * https://gematik.de/fhir/directory/StructureDefinition/OrganizationDirectory
   *
   * @return resource
   */
  @JsonProperty("resource")
  public Object getResource() {
    return resource;
  }

  public void setResource(Object resource) {
    this.resource = resource;
  }

  public OrganizationListAllOfEntry resource(Object resource) {
    this.resource = resource;
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
    OrganizationListAllOfEntry organizationListAllOfEntry = (OrganizationListAllOfEntry) o;
    return Objects.equals(this.resource, organizationListAllOfEntry.resource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resource);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationListAllOfEntry {\n");

    sb.append("    resource: ").append(toIndentedString(resource)).append("\n");
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
