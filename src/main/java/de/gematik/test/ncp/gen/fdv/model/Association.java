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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

/** Association */
@ApiModel(description = "Association")
public class Association {

  @ApiModelProperty(
      example = "urn:uuid:7804b22f-7400-4fd7-a4de-d6a5cdd8352d",
      value = "Eindeutige Kennung der Assoziation in der Registry")
  /** Eindeutige Kennung der Assoziation in der Registry */
  private String id;

  @ApiModelProperty(
      example = "HasMember",
      value =
          "Association Type aus IHE_ITI_TF_Vol3.pdf, Tabelle 4.2.2-1. Angabe mit und ohne den urn:* Prefix valide. Siehe Beispiele ")
  /**
   * Association Type aus IHE_ITI_TF_Vol3.pdf, Tabelle 4.2.2-1. Angabe mit und ohne den urn:* Prefix
   * valide. Siehe Beispiele
   */
  private String associationType;

  @ApiModelProperty(
      example = "urn:uuid:32ec5972-a5a3-4dab-8322-e46c56656d70",
      value = "Eindeutige Kennung des Quell-Objekts der Assoziation")
  /** Eindeutige Kennung des Quell-Objekts der Assoziation */
  private String sourceObject;

  @ApiModelProperty(
      example = "urn:uuid:3277f905-6550-4160-bd3b-c8e4fdc70611",
      value = "Eindeutige Kennung des Ziel-Objekts der Assoziation")
  /** Eindeutige Kennung des Ziel-Objekts der Assoziation */
  private String targetObject;

  /**
   * Eindeutige Kennung der Assoziation in der Registry
   *
   * @return id
   */
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Association id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Association Type aus IHE_ITI_TF_Vol3.pdf, Tabelle 4.2.2-1. Angabe mit und ohne den urn:* Prefix
   * valide. Siehe Beispiele
   *
   * @return associationType
   */
  @JsonProperty("associationType")
  public String getAssociationType() {
    return associationType;
  }

  public void setAssociationType(String associationType) {
    this.associationType = associationType;
  }

  public Association associationType(String associationType) {
    this.associationType = associationType;
    return this;
  }

  /**
   * Eindeutige Kennung des Quell-Objekts der Assoziation
   *
   * @return sourceObject
   */
  @JsonProperty("sourceObject")
  public String getSourceObject() {
    return sourceObject;
  }

  public void setSourceObject(String sourceObject) {
    this.sourceObject = sourceObject;
  }

  public Association sourceObject(String sourceObject) {
    this.sourceObject = sourceObject;
    return this;
  }

  /**
   * Eindeutige Kennung des Ziel-Objekts der Assoziation
   *
   * @return targetObject
   */
  @JsonProperty("targetObject")
  public String getTargetObject() {
    return targetObject;
  }

  public void setTargetObject(String targetObject) {
    this.targetObject = targetObject;
  }

  public Association targetObject(String targetObject) {
    this.targetObject = targetObject;
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
    Association association = (Association) o;
    return Objects.equals(this.id, association.id)
        && Objects.equals(this.associationType, association.associationType)
        && Objects.equals(this.sourceObject, association.sourceObject)
        && Objects.equals(this.targetObject, association.targetObject);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, associationType, sourceObject, targetObject);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Association {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    associationType: ").append(toIndentedString(associationType)).append("\n");
    sb.append("    sourceObject: ").append(toIndentedString(sourceObject)).append("\n");
    sb.append("    targetObject: ").append(toIndentedString(targetObject)).append("\n");
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
