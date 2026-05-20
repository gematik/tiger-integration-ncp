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

package de.gematik.test.ncp.reporting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.gematik.test.ncp.reporting.UseCase;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * see: <a href="https://gemspec.gematik.de/docs/gemSpec/gemSpec_Perf/latest/#A_21981-02">...</a>
 */
@NoArgsConstructor
@Data
@Accessors(fluent = true)
public class NcpehPerformanceReportEntry {

  @JsonProperty private Long timestamp;
  @JsonProperty private Long duration;
  @JsonProperty private UseCase operation;
  @JsonProperty private String status;
  @JsonProperty private NcpehPerformanceMessage message;
}
