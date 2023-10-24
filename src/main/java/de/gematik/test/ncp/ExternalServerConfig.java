/*
 * Copyright 2023 gematik GmbH
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

package de.gematik.test.ncp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Data class to hold the configuration information for external servers, which are managed by
 * Tiger.
 */
@Data
public class ExternalServerConfig {

  /**
   * Base key, for infrastructure information. Must be the same as used as baseKey in the tiger.yaml
   * for the file holding the infrastructure information
   */
  public static final String INFRASTRUCTURE_KEY = "infrastructure";

  @JsonProperty private String version;

  @JsonProperty(required = true)
  private String hostname;

  @JsonProperty(defaultValue = "")
  private String basePath;
}
