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

package de.gematik.test.ncp.gen.erp.fdv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@JsonTypeName("Practitioner")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Practitioner {
  private String name;
  private String anr;

  public enum AnrTypeEnum {
    LANR(String.valueOf("LANR")),
    ZANR(String.valueOf("ZANR"));

    private String value;

    AnrTypeEnum(String v) {
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

    /**
     * Convert a String into String, as specified in the <a
     * href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS
     * 2.0 Specification, section 3.2, p. 12</a>
     */
    public static AnrTypeEnum fromString(String s) {
      for (AnrTypeEnum b : AnrTypeEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static AnrTypeEnum fromValue(String value) {
      for (AnrTypeEnum b : AnrTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AnrTypeEnum anrType;
  private String qualificationType;
  private String telematikId;

  protected Practitioner(PractitionerBuilder<?, ?> b) {
    this.name = b.name;
    this.anr = b.anr;
    this.anrType = b.anrType;
    this.qualificationType = b.qualificationType;
    this.telematikId = b.telematikId;
  }

  public Practitioner() {}

  /** */
  public Practitioner name(String name) {
    this.name = name;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /** */
  public Practitioner anr(String anr) {
    this.anr = anr;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("anr")
  public String getAnr() {
    return anr;
  }

  @JsonProperty("anr")
  public void setAnr(String anr) {
    this.anr = anr;
  }

  /** */
  public Practitioner anrType(AnrTypeEnum anrType) {
    this.anrType = anrType;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("anrType")
  public AnrTypeEnum getAnrType() {
    return anrType;
  }

  @JsonProperty("anrType")
  public void setAnrType(AnrTypeEnum anrType) {
    this.anrType = anrType;
  }

  /** */
  public Practitioner qualificationType(String qualificationType) {
    this.qualificationType = qualificationType;
    return this;
  }

  @ApiModelProperty(
      example = "Fach&#228;rztin f&#252;r Innere Medizin",
      required = true,
      value = "")
  @JsonProperty("qualificationType")
  public String getQualificationType() {
    return qualificationType;
  }

  @JsonProperty("qualificationType")
  public void setQualificationType(String qualificationType) {
    this.qualificationType = qualificationType;
  }

  /** https://gematik.de/fhir/sid/telematik-id */
  public Practitioner telematikId(String telematikId) {
    this.telematikId = telematikId;
    return this;
  }

  @ApiModelProperty(example = "3-1.54.10123404", value = "https://gematik.de/fhir/sid/telematik-id")
  @JsonProperty("telematikId")
  public String getTelematikId() {
    return telematikId;
  }

  @JsonProperty("telematikId")
  public void setTelematikId(String telematikId) {
    this.telematikId = telematikId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Practitioner practitioner = (Practitioner) o;
    return Objects.equals(this.name, practitioner.name)
        && Objects.equals(this.anr, practitioner.anr)
        && Objects.equals(this.anrType, practitioner.anrType)
        && Objects.equals(this.qualificationType, practitioner.qualificationType)
        && Objects.equals(this.telematikId, practitioner.telematikId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, anr, anrType, qualificationType, telematikId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Practitioner {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    anr: ").append(toIndentedString(anr)).append("\n");
    sb.append("    anrType: ").append(toIndentedString(anrType)).append("\n");
    sb.append("    qualificationType: ").append(toIndentedString(qualificationType)).append("\n");
    sb.append("    telematikId: ").append(toIndentedString(telematikId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public static PractitionerBuilder<?, ?> builder() {
    return new PractitionerBuilderImpl();
  }

  private static class PractitionerBuilderImpl
      extends PractitionerBuilder<Practitioner, PractitionerBuilderImpl> {

    @Override
    protected PractitionerBuilderImpl self() {
      return this;
    }

    @Override
    public Practitioner build() {
      return new Practitioner(this);
    }
  }

  public abstract static class PractitionerBuilder<
      C extends Practitioner, B extends PractitionerBuilder<C, B>> {
    private String name;
    private String anr;
    private AnrTypeEnum anrType;
    private String qualificationType;
    private String telematikId;

    protected abstract B self();

    public abstract C build();

    public B name(String name) {
      this.name = name;
      return self();
    }

    public B anr(String anr) {
      this.anr = anr;
      return self();
    }

    public B anrType(AnrTypeEnum anrType) {
      this.anrType = anrType;
      return self();
    }

    public B qualificationType(String qualificationType) {
      this.qualificationType = qualificationType;
      return self();
    }

    public B telematikId(String telematikId) {
      this.telematikId = telematikId;
      return self();
    }
  }
}
