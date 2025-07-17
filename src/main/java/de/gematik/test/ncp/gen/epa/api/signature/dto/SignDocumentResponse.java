/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.gen.epa.api.signature.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Response der signDocument Operation mit der erzeugten Signatur */
@ApiModel(description = "Response der signDocument Operation mit der erzeugten Signatur")
public class SignDocumentResponse {

  @ApiModelProperty(
      required = true,
      value =
          "Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte")
  /**
   * Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte
   */
  private Boolean success;

  @ApiModelProperty(
      value =
          "Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.")
  /**
   * Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.
   */
  private String statusMessage;

  @ApiModelProperty(value = "Die erzeugte Signatur oder das Dokument mit Signatur")
  /** Die erzeugte Signatur oder das Dokument mit Signatur */
  private byte[] signatureObject;

  public enum SignatureFormEnum {
    SIGNATURE(String.valueOf("SIGNATURE")),
    DOCUMENT_WITH_SIGNATURE(String.valueOf("DOCUMENT_WITH_SIGNATURE"));

    private String value;

    SignatureFormEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static SignatureFormEnum fromValue(String value) {
      for (SignatureFormEnum b : SignatureFormEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "Format des zurückgegebenen Signaturobjektes")
  /** Format des zurückgegebenen Signaturobjektes */
  private SignatureFormEnum signatureForm;

  /**
   * Information, ob die Operation erfolgreich (true) oder nicht (false) ausgeführt werden konnte
   *
   * @return success
   */
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public SignDocumentResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Detailinformation zur Ausführung der Operation. Im Fehlerfall Details zu aufgetretenen Fehlern.
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

  public SignDocumentResponse statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
    return this;
  }

  /**
   * Die erzeugte Signatur oder das Dokument mit Signatur
   *
   * @return signatureObject
   */
  @JsonProperty("signatureObject")
  public byte[] getSignatureObject() {
    return signatureObject;
  }

  public void setSignatureObject(byte[] signatureObject) {
    this.signatureObject = signatureObject;
  }

  public SignDocumentResponse signatureObject(byte[] signatureObject) {
    this.signatureObject = signatureObject;
    return this;
  }

  /**
   * Format des zurückgegebenen Signaturobjektes
   *
   * @return signatureForm
   */
  @JsonProperty("signatureForm")
  public String getSignatureForm() {
    if (signatureForm == null) {
      return null;
    }
    return signatureForm.value();
  }

  public void setSignatureForm(SignatureFormEnum signatureForm) {
    this.signatureForm = signatureForm;
  }

  public SignDocumentResponse signatureForm(SignatureFormEnum signatureForm) {
    this.signatureForm = signatureForm;
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
    SignDocumentResponse signDocumentResponse = (SignDocumentResponse) o;
    return Objects.equals(this.success, signDocumentResponse.success)
        && Objects.equals(this.statusMessage, signDocumentResponse.statusMessage)
        && Objects.equals(this.signatureObject, signDocumentResponse.signatureObject)
        && Objects.equals(this.signatureForm, signDocumentResponse.signatureForm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusMessage, signatureObject, signatureForm);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignDocumentResponse {\n");

    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
    sb.append("    signatureObject: ").append(toIndentedString(signatureObject)).append("\n");
    sb.append("    signatureForm: ").append(toIndentedString(signatureForm)).append("\n");
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
