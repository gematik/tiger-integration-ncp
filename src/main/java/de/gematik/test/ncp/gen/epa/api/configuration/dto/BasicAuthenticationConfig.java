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

/**
 * Authentifizierungsinformationen wie sie z.B. für HTTP basic authentication verwendet werden
 * können
 */
@ApiModel(
    description =
        "Authentifizierungsinformationen wie sie z.B. für HTTP basic authentication verwendet werden können")
public class BasicAuthenticationConfig {

  @ApiModelProperty(value = "Benutzername für die Authentifizierung")
  /** Benutzername für die Authentifizierung */
  private String username;

  @ApiModelProperty(value = "Passwort für die Authentifizierung")
  /** Passwort für die Authentifizierung */
  private String password;

  @ApiModelProperty(
      value = "Schalter ob eine Authentifizierung erfolgen soll (true) oder nicht (false)")
  /** Schalter ob eine Authentifizierung erfolgen soll (true) oder nicht (false) */
  private Boolean enabled;

  /**
   * Benutzername für die Authentifizierung
   *
   * @return username
   */
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public BasicAuthenticationConfig username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Passwort für die Authentifizierung
   *
   * @return password
   */
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public BasicAuthenticationConfig password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Schalter ob eine Authentifizierung erfolgen soll (true) oder nicht (false)
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

  public BasicAuthenticationConfig enabled(Boolean enabled) {
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
    BasicAuthenticationConfig basicAuthenticationConfig = (BasicAuthenticationConfig) o;
    return Objects.equals(this.username, basicAuthenticationConfig.username)
        && Objects.equals(this.password, basicAuthenticationConfig.password)
        && Objects.equals(this.enabled, basicAuthenticationConfig.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BasicAuthenticationConfig {\n");

    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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
