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

package de.gematik.test.ncp.gen.epa.api.signature.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/**
 * Request Objekt um die Erstellung einer Signatur für das übergebene Dokument durch den Konnektor
 * auszulösen
 */
@ApiModel(
    description =
        "Request Objekt um die Erstellung einer Signatur für das übergebene Dokument durch den Konnektor auszulösen")
public class SignDocumentRequest {

  @ApiModelProperty(value = "Dokument welches signiert werden soll")
  /** Dokument welches signiert werden soll */
  private byte[] document;

  @ApiModelProperty(
      value =
          "Soll eine QES Signatur erstellt werden? true=ja (verwendet HBA), false=nein (verwendet SM-B)")
  /**
   * Soll eine QES Signatur erstellt werden? true=ja (verwendet HBA), false=nein (verwendet SM-B)
   */
  private Boolean performQES;

  public enum SignatureAlgorithmEnum {
    RSA_ECC(String.valueOf("RSA_ECC")),
    ECC(String.valueOf("ECC")),
    RSA(String.valueOf("RSA"));

    private String value;

    SignatureAlgorithmEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static SignatureAlgorithmEnum fromValue(String value) {
      for (SignatureAlgorithmEnum b : SignatureAlgorithmEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "Algorithmus für die Signatur (RSA oder ECC)")
  /** Algorithmus für die Signatur (RSA oder ECC) */
  private SignatureAlgorithmEnum signatureAlgorithm;

  /**
   * Dokument welches signiert werden soll
   *
   * @return document
   */
  @JsonProperty("document")
  public byte[] getDocument() {
    return document;
  }

  public void setDocument(byte[] document) {
    this.document = document;
  }

  public SignDocumentRequest document(byte[] document) {
    this.document = document;
    return this;
  }

  /**
   * Soll eine QES Signatur erstellt werden? true&#x3D;ja (verwendet HBA), false&#x3D;nein
   * (verwendet SM-B)
   *
   * @return performQES
   */
  @JsonProperty("performQES")
  public Boolean getPerformQES() {
    return performQES;
  }

  public void setPerformQES(Boolean performQES) {
    this.performQES = performQES;
  }

  public SignDocumentRequest performQES(Boolean performQES) {
    this.performQES = performQES;
    return this;
  }

  /**
   * Algorithmus für die Signatur (RSA oder ECC)
   *
   * @return signatureAlgorithm
   */
  @JsonProperty("signatureAlgorithm")
  public String getSignatureAlgorithm() {
    if (signatureAlgorithm == null) {
      return null;
    }
    return signatureAlgorithm.value();
  }

  public void setSignatureAlgorithm(SignatureAlgorithmEnum signatureAlgorithm) {
    this.signatureAlgorithm = signatureAlgorithm;
  }

  public SignDocumentRequest signatureAlgorithm(SignatureAlgorithmEnum signatureAlgorithm) {
    this.signatureAlgorithm = signatureAlgorithm;
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
    SignDocumentRequest signDocumentRequest = (SignDocumentRequest) o;
    return Objects.equals(this.document, signDocumentRequest.document)
        && Objects.equals(this.performQES, signDocumentRequest.performQES)
        && Objects.equals(this.signatureAlgorithm, signDocumentRequest.signatureAlgorithm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(document, performQES, signatureAlgorithm);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SignDocumentRequest {\n");

    sb.append("    document: ").append(toIndentedString(document)).append("\n");
    sb.append("    performQES: ").append(toIndentedString(performQES)).append("\n");
    sb.append("    signatureAlgorithm: ").append(toIndentedString(signatureAlgorithm)).append("\n");
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
