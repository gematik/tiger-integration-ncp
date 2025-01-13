/*
 * Copyright (c) 2024-2025 gematik GmbH
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
 */

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Objects;

/**
 * Entry of the Configuration array. The configurationEntryId is the key of the configuration
 * parameter and the configurationEntryValue is the value of the configuration parameter.
 */
@ApiModel(
    description =
        "Entry of the Configuration array. The configurationEntryId is the key of the configuration parameter and the configurationEntryValue is the value of the configuration parameter.")
public class ConfigurationEntry {

  public enum ConfigurationEntryIdEnum {
    OWNER_INSURANT_ID(String.valueOf("Owner_InsurantId")),
    OWNER_FQDN_PROVIDER(String.valueOf("Owner_FqdnProvider")),
    ACCOUNT_REPRESENTED_NAME(String.valueOf("AccountRepresented_Name")),
    ACCOUNT_REPRESENTED_RECORD_ID(String.valueOf("AccountRepresented_RecordId")),
    ACCOUNT_REPRESENTED_FQDN_PROVIDER(String.valueOf("AccountRepresented_FqdnProvider")),
    DEVICE_NAME(String.valueOf("DeviceName")),
    SIGD_URL(String.valueOf("SigdUrl"));

    private String value;

    ConfigurationEntryIdEnum(String v) {
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

    @JsonCreator
    public static ConfigurationEntryIdEnum fromValue(String value) {
      for (ConfigurationEntryIdEnum b : ConfigurationEntryIdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "")
  private ConfigurationEntryIdEnum configurationEntryId;

  @ApiModelProperty(value = "")
  private String configurationEntryValue;

  /**
   * Get configurationEntryId
   *
   * @return configurationEntryId
   */
  @JsonProperty("configurationEntryId")
  public String getConfigurationEntryId() {
    if (configurationEntryId == null) {
      return null;
    }
    return configurationEntryId.value();
  }

  public void setConfigurationEntryId(ConfigurationEntryIdEnum configurationEntryId) {
    this.configurationEntryId = configurationEntryId;
  }

  public ConfigurationEntry configurationEntryId(ConfigurationEntryIdEnum configurationEntryId) {
    this.configurationEntryId = configurationEntryId;
    return this;
  }

  /**
   * Get configurationEntryValue
   *
   * @return configurationEntryValue
   */
  @JsonProperty("configurationEntryValue")
  public String getConfigurationEntryValue() {
    return configurationEntryValue;
  }

  public void setConfigurationEntryValue(String configurationEntryValue) {
    this.configurationEntryValue = configurationEntryValue;
  }

  public ConfigurationEntry configurationEntryValue(String configurationEntryValue) {
    this.configurationEntryValue = configurationEntryValue;
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
    ConfigurationEntry configurationEntry = (ConfigurationEntry) o;
    return Objects.equals(this.configurationEntryId, configurationEntry.configurationEntryId)
        && Objects.equals(this.configurationEntryValue, configurationEntry.configurationEntryValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(configurationEntryId, configurationEntryValue);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurationEntry {\n");

    sb.append("    configurationEntryId: ")
        .append(toIndentedString(configurationEntryId))
        .append("\n");
    sb.append("    configurationEntryValue: ")
        .append(toIndentedString(configurationEntryValue))
        .append("\n");
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
