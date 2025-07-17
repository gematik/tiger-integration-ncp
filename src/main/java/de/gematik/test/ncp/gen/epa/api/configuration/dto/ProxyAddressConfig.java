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
import java.util.Objects;

/** Konfiguration einer Proxy-Adresse */
@ApiModel(description = "Konfiguration einer Proxy-Adresse")
public class ProxyAddressConfig {

  @ApiModelProperty(value = "Hostname oder IP des Proxies")
  /** Hostname oder IP des Proxies */
  private String address;

  @ApiModelProperty(value = "Port des Proxies")
  /** Port des Proxies */
  private Integer port;

  @ApiModelProperty(value = "Schalter ob ein Proxy verwendet werden soll (true) oder nicht (false)")
  /** Schalter ob ein Proxy verwendet werden soll (true) oder nicht (false) */
  private Boolean enabled;

  /**
   * Hostname oder IP des Proxies
   *
   * @return address
   */
  @JsonProperty("address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public ProxyAddressConfig address(String address) {
    this.address = address;
    return this;
  }

  /**
   * Port des Proxies
   *
   * @return port
   */
  @JsonProperty("port")
  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public ProxyAddressConfig port(Integer port) {
    this.port = port;
    return this;
  }

  /**
   * Schalter ob ein Proxy verwendet werden soll (true) oder nicht (false)
   *
   * @return enabled
   */
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public ProxyAddressConfig enabled(Boolean enabled) {
    this.enabled = enabled;
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
    ProxyAddressConfig proxyAddressConfig = (ProxyAddressConfig) o;
    return Objects.equals(this.address, proxyAddressConfig.address)
        && Objects.equals(this.port, proxyAddressConfig.port)
        && Objects.equals(this.enabled, proxyAddressConfig.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, port, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProxyAddressConfig {\n");

    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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
