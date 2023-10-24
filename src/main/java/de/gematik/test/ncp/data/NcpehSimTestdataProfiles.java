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
import java.util.HashMap;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Configuration object to hold all Ncpeh-Simulator testdata profiles loaded from the testdata
 * configuration
 */
public class NcpehSimTestdataProfiles extends HashMap<String, NcpehSimTestdataProfile> {

  @Data
  @Accessors(fluent = true)
  static class NcpehSimTestdataProfileImpl implements NcpehSimTestdataProfile {

    @JsonProperty private String trcProfileName;

    @JsonProperty private String idaProfileName;
  }
}
