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

package de.gematik.test.ncp.reporting;

import static de.gematik.test.ncp.util.ReportingUtils.generateFileName;
import static de.gematik.test.ncp.util.ReportingUtils.getPerformanceLogFileName;

import java.util.Optional;
import lombok.Getter;

public class NcpehContext {

  @Getter(lazy = true)
  private final String fileName = getPerformanceLogFileName();

  @Getter private Optional<String> scenarioFileName = Optional.empty();

  public void setScenarioName(final String scenarioName) {
    this.scenarioFileName =
        scenarioName == null || scenarioName.isBlank()
            ? Optional.empty()
            : Optional.of(generateFileName(scenarioName));
  }
}
