/*
 * Copyright 2025 gematik GmbH
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

package de.gematik.test.ncp.glue;

import static de.gematik.test.ncp.glue.psa.ActorsInitializationSteps.LE_DE_ACTOR_NAME;
import static net.serenitybdd.screenplay.GivenWhenThen.then;

import de.gematik.test.ncp.screenplay.actions.DeleteAllDocumentsInAktenkonto;
import de.gematik.test.ncp.screenplay.questions.GetAllDocumentsFromAktenkonto;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.actors.OnStage;

@Slf4j
public class HelpSteps {

  @Wenn("Es existieren Dokumente im Aktenkonto der versicherten Person")
  public void hasDocuments() {
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    final var documents = leDeActor.asksFor(GetAllDocumentsFromAktenkonto.instance());
    log.info("Found documents: {}", String.join(", ", documents));
    // don't make any asserts here, we will see always green tests
  }

  @Dann("lösche alle Dokumente aus dem Aktenkonto der versicherten Person")
  public void removeAllDocuments() {
    final var leDeActor = OnStage.theActorCalled(LE_DE_ACTOR_NAME);
    then(leDeActor).attemptsTo(DeleteAllDocumentsInAktenkonto.instance());
  }
}
