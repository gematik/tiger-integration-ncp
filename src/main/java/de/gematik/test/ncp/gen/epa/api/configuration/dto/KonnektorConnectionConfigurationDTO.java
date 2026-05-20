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

package de.gematik.test.ncp.gen.epa.api.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Konfigurationsinformationen um die Verbindung zum Konnektor zu konfigurieren */
@ApiModel(
    description = "Konfigurationsinformationen um die Verbindung zum Konnektor zu konfigurieren")
public class KonnektorConnectionConfigurationDTO {

  @ApiModelProperty(value = "")
  private AddressConfig address;

  @ApiModelProperty(value = "")
  private TlsConfig tlsConfig;

  @ApiModelProperty(value = "")
  private ProxyAddressConfig proxyAddress;

  @ApiModelProperty(value = "")
  private BasicAuthenticationConfig basicAuthentication;

  @ApiModelProperty(
      value =
          "Schalter, ob die übergebenen Daten die bereits vorhandenen aktualisieren (true) oder überschreiben (false) sollen. Der Unterschied besteht darin, dass Felder, die nicht übergeben werden (null), beim Aktualisieren ignoriert werden, sprich bereits gesetzte Werte bleiben erhalten, beim Überschreiben den bisherigen Wert überschreiben, sprich bereits gesetzte Werte werden durch null überschrieben. Dies gilt nur für die Elemente der gleichen Ebene. Subelemente eines gesetzten Elements werden immer übernommen auch wenn das Subelement nicht gesetzt ist (also den Wert null hat) und der Schalter auf true steht.")
  /**
   * Schalter, ob die übergebenen Daten die bereits vorhandenen aktualisieren (true) oder
   * überschreiben (false) sollen. Der Unterschied besteht darin, dass Felder, die nicht übergeben
   * werden (null), beim Aktualisieren ignoriert werden, sprich bereits gesetzte Werte bleiben
   * erhalten, beim Überschreiben den bisherigen Wert überschreiben, sprich bereits gesetzte Werte
   * werden durch null überschrieben. Dies gilt nur für die Elemente der gleichen Ebene. Subelemente
   * eines gesetzten Elements werden immer übernommen auch wenn das Subelement nicht gesetzt ist
   * (also den Wert null hat) und der Schalter auf true steht.
   */
  private Boolean asUpdate;

  /**
   * Get address
   *
   * @return address
   */
  @JsonProperty("address")
  public AddressConfig getAddress() {
    return address;
  }

  public void setAddress(AddressConfig address) {
    this.address = address;
  }

  public KonnektorConnectionConfigurationDTO address(AddressConfig address) {
    this.address = address;
    return this;
  }

  /**
   * Get tlsConfig
   *
   * @return tlsConfig
   */
  @JsonProperty("tlsConfig")
  public TlsConfig getTlsConfig() {
    return tlsConfig;
  }

  public void setTlsConfig(TlsConfig tlsConfig) {
    this.tlsConfig = tlsConfig;
  }

  public KonnektorConnectionConfigurationDTO tlsConfig(TlsConfig tlsConfig) {
    this.tlsConfig = tlsConfig;
    return this;
  }

  /**
   * Get proxyAddress
   *
   * @return proxyAddress
   */
  @JsonProperty("proxyAddress")
  public ProxyAddressConfig getProxyAddress() {
    return proxyAddress;
  }

  public void setProxyAddress(ProxyAddressConfig proxyAddress) {
    this.proxyAddress = proxyAddress;
  }

  public KonnektorConnectionConfigurationDTO proxyAddress(ProxyAddressConfig proxyAddress) {
    this.proxyAddress = proxyAddress;
    return this;
  }

  /**
   * Get basicAuthentication
   *
   * @return basicAuthentication
   */
  @JsonProperty("basicAuthentication")
  public BasicAuthenticationConfig getBasicAuthentication() {
    return basicAuthentication;
  }

  public void setBasicAuthentication(BasicAuthenticationConfig basicAuthentication) {
    this.basicAuthentication = basicAuthentication;
  }

  public KonnektorConnectionConfigurationDTO basicAuthentication(
      BasicAuthenticationConfig basicAuthentication) {
    this.basicAuthentication = basicAuthentication;
    return this;
  }

  /**
   * Schalter, ob die übergebenen Daten die bereits vorhandenen aktualisieren (true) oder
   * überschreiben (false) sollen. Der Unterschied besteht darin, dass Felder, die nicht übergeben
   * werden (null), beim Aktualisieren ignoriert werden, sprich bereits gesetzte Werte bleiben
   * erhalten, beim Überschreiben den bisherigen Wert überschreiben, sprich bereits gesetzte Werte
   * werden durch null überschrieben. Dies gilt nur für die Elemente der gleichen Ebene. Subelemente
   * eines gesetzten Elements werden immer übernommen auch wenn das Subelement nicht gesetzt ist
   * (also den Wert null hat) und der Schalter auf true steht.
   *
   * @return asUpdate
   */
  @JsonProperty("asUpdate")
  public Boolean getAsUpdate() {
    return asUpdate;
  }

  public void setAsUpdate(Boolean asUpdate) {
    this.asUpdate = asUpdate;
  }

  public KonnektorConnectionConfigurationDTO asUpdate(Boolean asUpdate) {
    this.asUpdate = asUpdate;
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
    KonnektorConnectionConfigurationDTO konnektorConnectionConfigurationDTO =
        (KonnektorConnectionConfigurationDTO) o;
    return Objects.equals(this.address, konnektorConnectionConfigurationDTO.address)
        && Objects.equals(this.tlsConfig, konnektorConnectionConfigurationDTO.tlsConfig)
        && Objects.equals(this.proxyAddress, konnektorConnectionConfigurationDTO.proxyAddress)
        && Objects.equals(
            this.basicAuthentication, konnektorConnectionConfigurationDTO.basicAuthentication)
        && Objects.equals(this.asUpdate, konnektorConnectionConfigurationDTO.asUpdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, tlsConfig, proxyAddress, basicAuthentication, asUpdate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KonnektorConnectionConfigurationDTO {\n");

    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    tlsConfig: ").append(toIndentedString(tlsConfig)).append("\n");
    sb.append("    proxyAddress: ").append(toIndentedString(proxyAddress)).append("\n");
    sb.append("    basicAuthentication: ")
        .append(toIndentedString(basicAuthentication))
        .append("\n");
    sb.append("    asUpdate: ").append(toIndentedString(asUpdate)).append("\n");
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
