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

import de.gematik.test.tiger.common.config.TigerConfigurationException;
import java.util.ArrayList;
import lombok.NonNull;

/** Configuration object to hold all patient information loaded from the testdata configuration */
public class Patients extends ArrayList<Patient> {

  public Patient findByName(@NonNull PersonName name) {
    return this.stream()
        .filter(patient -> name.equals(patient.name()))
        .findFirst()
        .orElseThrow(() -> new TigerConfigurationException("No patient found with name " + name));
  }

  public Patient findByKvnr(@NonNull String kvnr) {
    return this.stream()
        .filter(patient -> kvnr.equals(patient.kvnr()))
        .findFirst()
        .orElseThrow(() -> new TigerConfigurationException("No patient found with KVNR " + kvnr));
  }
}
