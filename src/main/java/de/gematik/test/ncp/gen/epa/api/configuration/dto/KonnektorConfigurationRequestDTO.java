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

/** Request um die Konnektor spezifische Konfiguration des epa-ps-sim zu ändern */
@ApiModel(
    description = "Request um die Konnektor spezifische Konfiguration des epa-ps-sim zu ändern")
public class KonnektorConfigurationRequestDTO {

  @ApiModelProperty(value = "")
  private KonnektorConnectionConfigurationDTO connection;

  @ApiModelProperty(value = "")
  private Context context;

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
   * Get connection
   *
   * @return connection
   */
  @JsonProperty("connection")
  public KonnektorConnectionConfigurationDTO getConnection() {
    return connection;
  }

  public void setConnection(KonnektorConnectionConfigurationDTO connection) {
    this.connection = connection;
  }

  public KonnektorConfigurationRequestDTO connection(
      KonnektorConnectionConfigurationDTO connection) {
    this.connection = connection;
    return this;
  }

  /**
   * Get context
   *
   * @return context
   */
  @JsonProperty("context")
  public Context getContext() {
    return context;
  }

  public void setContext(Context context) {
    this.context = context;
  }

  public KonnektorConfigurationRequestDTO context(Context context) {
    this.context = context;
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

  public KonnektorConfigurationRequestDTO asUpdate(Boolean asUpdate) {
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
    KonnektorConfigurationRequestDTO konnektorConfigurationRequestDTO =
        (KonnektorConfigurationRequestDTO) o;
    return Objects.equals(this.connection, konnektorConfigurationRequestDTO.connection)
        && Objects.equals(this.context, konnektorConfigurationRequestDTO.context)
        && Objects.equals(this.asUpdate, konnektorConfigurationRequestDTO.asUpdate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(connection, context, asUpdate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KonnektorConfigurationRequestDTO {\n");

    sb.append("    connection: ").append(toIndentedString(connection)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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
