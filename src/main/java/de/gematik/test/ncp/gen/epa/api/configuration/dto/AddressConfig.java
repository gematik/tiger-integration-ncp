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

/** Daten um die Address-URL eines Konnektors zu konfigurieren */
@ApiModel(description = "Daten um die Address-URL eines Konnektors zu konfigurieren")
public class AddressConfig {

  @ApiModelProperty(required = true, value = "Hostname oder IP des Konnektors")
  /** Hostname oder IP des Konnektors */
  private String host;

  @ApiModelProperty(
      value = "Portnummer des Ports unter dem die Dienste des Konnektors erreichbar sind")
  /** Portnummer des Ports unter dem die Dienste des Konnektors erreichbar sind */
  private Integer port;

  @ApiModelProperty(
      value =
          "Schema bzw. Protocol das zur Kommunikation mit dem Konnektor verwendet werden soll. Z.B. http oder https")
  /**
   * Schema bzw. Protocol das zur Kommunikation mit dem Konnektor verwendet werden soll. Z.B. http
   * oder https
   */
  private String protocol;

  @ApiModelProperty(
      value =
          "Pfad unter dem die Konnektordienste, bei der gegebenen Adresse zu erreichen sind. Kann weggelassen werden, wenn kein Pfad benötigt wird")
  /**
   * Pfad unter dem die Konnektordienste, bei der gegebenen Adresse zu erreichen sind. Kann
   * weggelassen werden, wenn kein Pfad benötigt wird
   */
  private String path;

  /**
   * Hostname oder IP des Konnektors
   *
   * @return host
   */
  @JsonProperty("host")
  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public AddressConfig host(String host) {
    this.host = host;
    return this;
  }

  /**
   * Portnummer des Ports unter dem die Dienste des Konnektors erreichbar sind
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

  public AddressConfig port(Integer port) {
    this.port = port;
    return this;
  }

  /**
   * Schema bzw. Protocol das zur Kommunikation mit dem Konnektor verwendet werden soll. Z.B. http
   * oder https
   *
   * @return protocol
   */
  @JsonProperty("protocol")
  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public AddressConfig protocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

  /**
   * Pfad unter dem die Konnektordienste, bei der gegebenen Adresse zu erreichen sind. Kann
   * weggelassen werden, wenn kein Pfad benötigt wird
   *
   * @return path
   */
  @JsonProperty("path")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public AddressConfig path(String path) {
    this.path = path;
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
    AddressConfig addressConfig = (AddressConfig) o;
    return Objects.equals(this.host, addressConfig.host)
        && Objects.equals(this.port, addressConfig.port)
        && Objects.equals(this.protocol, addressConfig.protocol)
        && Objects.equals(this.path, addressConfig.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(host, port, protocol, path);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AddressConfig {\n");

    sb.append("    host: ").append(toIndentedString(host)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
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
