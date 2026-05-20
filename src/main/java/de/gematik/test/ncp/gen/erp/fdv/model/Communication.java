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
import java.util.UUID;

/**
 * See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Communication_InfoReq See
 * https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Communication_DispReq See
 * https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Communication_Reply
 */
@ApiModel(
    description =
        "See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Communication_InfoReq See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Communication_DispReq See https://gematik.de/fhir/erp/StructureDefinition/GEM_ERP_PR_Communication_Reply")
@JsonTypeName("Communication")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Communication {
  private UUID id;

  public enum TypeEnum {
    DISP_REQ(String.valueOf("DispReq")),
    INFO_REQ(String.valueOf("InfoReq")),
    REPLY(String.valueOf("Reply")),
    REPRESENTATIVE(String.valueOf("Representative"));

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

    /**
     * Convert a String into String, as specified in the <a
     * href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS
     * 2.0 Specification, section 3.2, p. 12</a>
     */
    public static TypeEnum fromString(String s) {
      for (TypeEnum b : TypeEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
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

  private TypeEnum type;
  private String reference;
  private CommunicationSender sender;
  private CommunicationSender recipient;
  private OffsetDateTime sent;
  private SupplyOptionsType supplyOptionsType;

  protected Communication(CommunicationBuilder<?, ?> b) {
    this.id = b.id;
    this.type = b.type;
    this.reference = b.reference;
    this.sender = b.sender;
    this.recipient = b.recipient;
    this.sent = b.sent;
    this.supplyOptionsType = b.supplyOptionsType;
  }

  public Communication() {}

  /** */
  public Communication id(UUID id) {
    this.id = id;
    return this;
  }

  @ApiModelProperty(example = "79cc4c08-0e7b-4e52-acee-6ec7519ce67f", value = "")
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /** */
  public Communication type(TypeEnum type) {
    this.type = type;
    return this;
  }

  @ApiModelProperty(example = "DispReq", required = true, value = "")
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(TypeEnum type) {
    this.type = type;
  }

  /** See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid */
  public Communication reference(String reference) {
    this.reference = reference;
    return this;
  }

  @ApiModelProperty(
      example = "160.000.000.000.123.76",
      value = "See https://simplifier.net/e-prescription-workflow/gem_erp_pr_prescriptionid")
  @JsonProperty("reference")
  public String getReference() {
    return reference;
  }

  @JsonProperty("reference")
  public void setReference(String reference) {
    this.reference = reference;
  }

  /** */
  public Communication sender(CommunicationSender sender) {
    this.sender = sender;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("sender")
  public CommunicationSender getSender() {
    return sender;
  }

  @JsonProperty("sender")
  public void setSender(CommunicationSender sender) {
    this.sender = sender;
  }

  /** */
  public Communication recipient(CommunicationSender recipient) {
    this.recipient = recipient;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("recipient")
  public CommunicationSender getRecipient() {
    return recipient;
  }

  @JsonProperty("recipient")
  public void setRecipient(CommunicationSender recipient) {
    this.recipient = recipient;
  }

  /** */
  public Communication sent(OffsetDateTime sent) {
    this.sent = sent;
    return this;
  }

  @ApiModelProperty(example = "2020-03-12T18:01:10Z", required = true, value = "")
  @JsonProperty("sent")
  public OffsetDateTime getSent() {
    return sent;
  }

  @JsonProperty("sent")
  public void setSent(OffsetDateTime sent) {
    this.sent = sent;
  }

  /** */
  public Communication supplyOptionsType(SupplyOptionsType supplyOptionsType) {
    this.supplyOptionsType = supplyOptionsType;
    return this;
  }

  @ApiModelProperty(value = "")
  @JsonProperty("supplyOptionsType")
  public SupplyOptionsType getSupplyOptionsType() {
    return supplyOptionsType;
  }

  @JsonProperty("supplyOptionsType")
  public void setSupplyOptionsType(SupplyOptionsType supplyOptionsType) {
    this.supplyOptionsType = supplyOptionsType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Communication communication = (Communication) o;
    return Objects.equals(this.id, communication.id)
        && Objects.equals(this.type, communication.type)
        && Objects.equals(this.reference, communication.reference)
        && Objects.equals(this.sender, communication.sender)
        && Objects.equals(this.recipient, communication.recipient)
        && Objects.equals(this.sent, communication.sent)
        && Objects.equals(this.supplyOptionsType, communication.supplyOptionsType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, reference, sender, recipient, sent, supplyOptionsType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Communication {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
    sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("    sent: ").append(toIndentedString(sent)).append("\n");
    sb.append("    supplyOptionsType: ").append(toIndentedString(supplyOptionsType)).append("\n");
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

  public static CommunicationBuilder<?, ?> builder() {
    return new CommunicationBuilderImpl();
  }

  private static class CommunicationBuilderImpl
      extends CommunicationBuilder<Communication, CommunicationBuilderImpl> {

    @Override
    protected CommunicationBuilderImpl self() {
      return this;
    }

    @Override
    public Communication build() {
      return new Communication(this);
    }
  }

  public abstract static class CommunicationBuilder<
      C extends Communication, B extends CommunicationBuilder<C, B>> {
    private UUID id;
    private TypeEnum type;
    private String reference;
    private CommunicationSender sender;
    private CommunicationSender recipient;
    private OffsetDateTime sent;
    private SupplyOptionsType supplyOptionsType;

    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }

    public B type(TypeEnum type) {
      this.type = type;
      return self();
    }

    public B reference(String reference) {
      this.reference = reference;
      return self();
    }

    public B sender(CommunicationSender sender) {
      this.sender = sender;
      return self();
    }

    public B recipient(CommunicationSender recipient) {
      this.recipient = recipient;
      return self();
    }

    public B sent(OffsetDateTime sent) {
      this.sent = sent;
      return self();
    }

    public B supplyOptionsType(SupplyOptionsType supplyOptionsType) {
      this.supplyOptionsType = supplyOptionsType;
      return self();
    }
  }
}
