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

package de.gematik.test.ncp.gen.epa.api.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Konfigurationsdaten für eine TLS Verbindung */
@ApiModel(description = "Konfigurationsdaten für eine TLS Verbindung")
public class TlsConfig {

  @ApiModelProperty(value = "")
  private FileInfo keystorefile;

  @ApiModelProperty(value = "")
  private String keystorepassword;

  @ApiModelProperty(example = "PKCS12", value = "Art des Keystores, z.B. PKCS12")
  /** Art des Keystores, z.B. PKCS12 */
  private String keystoretype;

  @ApiModelProperty(
      example =
          "['TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384', 'TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256', 'TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384', 'TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256', 'TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA', 'TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA']",
      value = "Liste der zulässigen Ciphersuiten")
  /** Liste der zulässigen Ciphersuiten */
  private List<String> ciphersuites = new ArrayList<>();

  /**
   * Get keystorefile
   *
   * @return keystorefile
   */
  @JsonProperty("keystorefile")
  public FileInfo getKeystorefile() {
    return keystorefile;
  }

  public void setKeystorefile(FileInfo keystorefile) {
    this.keystorefile = keystorefile;
  }

  public TlsConfig keystorefile(FileInfo keystorefile) {
    this.keystorefile = keystorefile;
    return this;
  }

  /**
   * Get keystorepassword
   *
   * @return keystorepassword
   */
  @JsonProperty("keystorepassword")
  public String getKeystorepassword() {
    return keystorepassword;
  }

  public void setKeystorepassword(String keystorepassword) {
    this.keystorepassword = keystorepassword;
  }

  public TlsConfig keystorepassword(String keystorepassword) {
    this.keystorepassword = keystorepassword;
    return this;
  }

  /**
   * Art des Keystores, z.B. PKCS12
   *
   * @return keystoretype
   */
  @JsonProperty("keystoretype")
  public String getKeystoretype() {
    return keystoretype;
  }

  public void setKeystoretype(String keystoretype) {
    this.keystoretype = keystoretype;
  }

  public TlsConfig keystoretype(String keystoretype) {
    this.keystoretype = keystoretype;
    return this;
  }

  /**
   * Liste der zulässigen Ciphersuiten
   *
   * @return ciphersuites
   */
  @JsonProperty("ciphersuites")
  public List<String> getCiphersuites() {
    return ciphersuites;
  }

  public void setCiphersuites(List<String> ciphersuites) {
    this.ciphersuites = ciphersuites;
  }

  public TlsConfig ciphersuites(List<String> ciphersuites) {
    this.ciphersuites = ciphersuites;
    return this;
  }

  public TlsConfig addCiphersuitesItem(String ciphersuitesItem) {
    this.ciphersuites.add(ciphersuitesItem);
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
    TlsConfig tlsConfig = (TlsConfig) o;
    return Objects.equals(this.keystorefile, tlsConfig.keystorefile)
        && Objects.equals(this.keystorepassword, tlsConfig.keystorepassword)
        && Objects.equals(this.keystoretype, tlsConfig.keystoretype)
        && Objects.equals(this.ciphersuites, tlsConfig.ciphersuites);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keystorefile, keystorepassword, keystoretype, ciphersuites);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TlsConfig {\n");

    sb.append("    keystorefile: ").append(toIndentedString(keystorefile)).append("\n");
    sb.append("    keystorepassword: ").append(toIndentedString(keystorepassword)).append("\n");
    sb.append("    keystoretype: ").append(toIndentedString(keystoretype)).append("\n");
    sb.append("    ciphersuites: ").append(toIndentedString(ciphersuites)).append("\n");
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
