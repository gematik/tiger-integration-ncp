/*
 * Copyright (c) 2024. gematik GmbH
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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.test.ncp.data.ConnectorAddresses;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.screenplay.abilities.UsePrimarySystem;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.Optional;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

// TODO: Clarify if needed
public class SelectConnector implements Performable {

  private final Testdata testData;
  private final String connectorModel;

  public SelectConnector(final Testdata testData, final String connectorModel) {
    this.testData = testData;
    this.connectorModel = connectorModel;
  }

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var konnektorAddress =
        Optional.ofNullable(testData.connectorAddresses())
            .map(kas -> kas.get(connectorModel))
            .stream()
            .flatMap(Collection::stream)
            .findFirst()
            .orElse(null);
    if (konnektorAddress == null) {
      throw new MissingResourceException(
          "No konnektor address for given Konnektor model found",
          ConnectorAddresses.class.getSimpleName(),
          connectorModel);
    }
    final var ps = actor.usingAbilityTo(UsePrimarySystem.class);
    try {
      ps.configureKonnektor(URI.create(konnektorAddress).toURL());
    } catch (final MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  public static SelectConnector fromTestdataAndConnectorModel(
      final Testdata testData, final String connectorModel) {
    return Instrumented.instanceOf(SelectConnector.class).withProperties(testData, connectorModel);
  }
}
