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

package de.gematik.test.ncp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import java.util.ArrayList;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * Configuration object to hold all practitioner information loaded from the testdata configuration
 */
public class Practitioners extends ArrayList<Practitioner> {

  public Practitioner findByName(@NonNull String name) {
    return this.stream()
        .filter(practitioner -> name.equals(practitioner.name()))
        .findFirst()
        .orElseThrow(
            () -> new TigerConfigurationException("No practitioner found with name " + name));
  }

  @Data
  @Accessors(fluent = true)
  static class PractitionerImpl implements Practitioner {

    @JsonProperty private String name;

    @JsonProperty private String country;

    @JsonProperty private String profileName;
  }
}
