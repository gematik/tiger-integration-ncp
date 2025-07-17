/*
 * Copyright 2024-2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.test.ncp.ps.PrimarySystemService;
import de.gematik.test.ncp.ps.PsProvider;
import java.util.HashSet;
import java.util.Set;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.HasTeardown;

@Slf4j
public class UsePrimarySystem implements Ability, HasTeardown {

  private final Set<String> kvnrToCleanEpkas = new HashSet<>();

  @Delegate // lombok.experimental kann aber auch von Hand implementiert werden
  private final PrimarySystemService service;

  public UsePrimarySystem() {
    this(PsProvider.getPrimarySystemService());
  }

  public UsePrimarySystem(final PrimarySystemService service) {
    this.service = service;
  }

  public void addKvnrToCleanEpka(final String kvnr) {
    log.debug("Adding kvnr {} to list of ePKAs to clean up", kvnr);
    kvnrToCleanEpkas.add(kvnr);
  }

  @Override
  public void tearDown() {
    log.debug("Cleaning up ePKAs for kvnrs: {}", String.join(", ", kvnrToCleanEpkas));
    kvnrToCleanEpkas.forEach(
        kvnr -> {
          final var epkas = service.findEpka(kvnr);
          if (!epkas.isEmpty()) {
            log.debug("Cleaning up ePKAs for kvnr {}: {}", kvnr, String.join(", ", epkas));
            service.deleteExistingDocuments(kvnr, epkas);
          }
        });
  }
}
