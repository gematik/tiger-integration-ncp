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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Objects;

/** Reduced modeled by https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_AuditEvent */
@ApiModel(
    description =
        "Reduced modeled by https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_AuditEvent")
@JsonTypeName("AuditEvent")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class AuditEvent {
  private String text;
  private OffsetDateTime recorded;
  private String prescriptionId;

  public enum ActionEnum {
    C(String.valueOf("C")),
    R(String.valueOf("R")),
    U(String.valueOf("U")),
    D(String.valueOf("D")),
    E(String.valueOf("E"));

    private String value;

    ActionEnum(String v) {
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
    public static ActionEnum fromString(String s) {
      for (ActionEnum b : ActionEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;
  private String who;

  protected AuditEvent(AuditEventBuilder<?, ?> b) {
    this.text = b.text;
    this.recorded = b.recorded;
    this.prescriptionId = b.prescriptionId;
    this.action = b.action;
    this.who = b.who;
  }

  public AuditEvent() {}

  /** */
  public AuditEvent text(String text) {
    this.text = text;
    return this;
  }

  @ApiModelProperty(
      example = "Juliane Steinmeyer hat eine Liste von E-Rezepten heruntergeladen",
      required = true,
      value = "")
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  @JsonProperty("text")
  public void setText(String text) {
    this.text = text;
  }

  /** */
  public AuditEvent recorded(OffsetDateTime recorded) {
    this.recorded = recorded;
    return this;
  }

  @ApiModelProperty(example = "2024-11-25T14:30Z", required = true, value = "")
  @JsonProperty("recorded")
  public OffsetDateTime getRecorded() {
    return recorded;
  }

  @JsonProperty("recorded")
  public void setRecorded(OffsetDateTime recorded) {
    this.recorded = recorded;
  }

  /** See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid */
  public AuditEvent prescriptionId(String prescriptionId) {
    this.prescriptionId = prescriptionId;
    return this;
  }

  @ApiModelProperty(
      example = "160.000.000.000.123.76",
      required = true,
      value = "See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid")
  @JsonProperty("prescriptionId")
  public String getPrescriptionId() {
    return prescriptionId;
  }

  @JsonProperty("prescriptionId")
  public void setPrescriptionId(String prescriptionId) {
    this.prescriptionId = prescriptionId;
  }

  /** */
  public AuditEvent action(ActionEnum action) {
    this.action = action;
    return this;
  }

  @ApiModelProperty(example = "R", required = true, value = "")
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  @JsonProperty("action")
  public void setAction(ActionEnum action) {
    this.action = action;
  }

  /** Agent that triggered this event */
  public AuditEvent who(String who) {
    this.who = who;
    return this;
  }

  @ApiModelProperty(required = true, value = "Agent that triggered this event")
  @JsonProperty("who")
  public String getWho() {
    return who;
  }

  @JsonProperty("who")
  public void setWho(String who) {
    this.who = who;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuditEvent auditEvent = (AuditEvent) o;
    return Objects.equals(this.text, auditEvent.text)
        && Objects.equals(this.recorded, auditEvent.recorded)
        && Objects.equals(this.prescriptionId, auditEvent.prescriptionId)
        && Objects.equals(this.action, auditEvent.action)
        && Objects.equals(this.who, auditEvent.who);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, recorded, prescriptionId, action, who);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuditEvent {\n");

    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    recorded: ").append(toIndentedString(recorded)).append("\n");
    sb.append("    prescriptionId: ").append(toIndentedString(prescriptionId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    who: ").append(toIndentedString(who)).append("\n");
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

  public static AuditEventBuilder<?, ?> builder() {
    return new AuditEventBuilderImpl();
  }

  private static class AuditEventBuilderImpl
      extends AuditEventBuilder<AuditEvent, AuditEventBuilderImpl> {

    @Override
    protected AuditEventBuilderImpl self() {
      return this;
    }

    @Override
    public AuditEvent build() {
      return new AuditEvent(this);
    }
  }

  public abstract static class AuditEventBuilder<
      C extends AuditEvent, B extends AuditEventBuilder<C, B>> {
    private String text;
    private OffsetDateTime recorded;
    private String prescriptionId;
    private ActionEnum action;
    private String who;

    protected abstract B self();

    public abstract C build();

    public B text(String text) {
      this.text = text;
      return self();
    }

    public B recorded(OffsetDateTime recorded) {
      this.recorded = recorded;
      return self();
    }

    public B prescriptionId(String prescriptionId) {
      this.prescriptionId = prescriptionId;
      return self();
    }

    public B action(ActionEnum action) {
      this.action = action;
      return self();
    }

    public B who(String who) {
      this.who = who;
      return self();
    }
  }
}
