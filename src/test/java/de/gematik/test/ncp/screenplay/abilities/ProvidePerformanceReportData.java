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

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.test.ncp.reporting.model.NcpehPerformanceReport;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportEntry;
import de.gematik.test.ncp.reporting.model.Pair;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import net.serenitybdd.screenplay.Ability;

@Data
public class ProvidePerformanceReportData implements Ability {

  private NcpehPerformanceReport performanceReport;

  private NcpehPerformanceReport dwhPerformanceReport;

  private List<Pair<NcpehPerformanceReportEntry, NcpehPerformanceReportEntry>> matchedEntries =
      new ArrayList<>();
}
