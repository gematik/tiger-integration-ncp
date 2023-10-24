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

package de.gematik.test.ncp.screenplay;

import de.gematik.test.ncp.data.KonnektorAddresses;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.ps.PsInterface;
import de.gematik.test.ncp.ps.PsMockImpl;
import de.gematik.test.ncp.ps.PsProvider;
import java.net.URI;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.BaseConsequence;
import net.serenitybdd.screenplay.Consequence;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Task;

/**
 * Methods to retrieve Screenplay objects, like tasks and questions to be used in the glue code,
 * with the focus on tasks run in the practice of a practitioner in Germany.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class GermanPracticeActions {

  @Accessors(fluent = true)
  @Getter(lazy = true, value = AccessLevel.PACKAGE)
  private static final PsInterface ps = getPsInterface();

  @Accessors(fluent = true)
  @Getter(lazy = true)
  private static final LeiActor lei = new LeiActor("Leistungserbringer DE").country("Germany");

  private static PsInterface getPsInterface() {
    PsProvider.getPsProvider().defaultPsImpl.set(PsMockImpl::instance);
    return PsProvider.getPsImplementation();
  }

  public static Question<Answer<Boolean>> psIsAvailable() {
    return actor ->
        Answer.booleanAnswer(ps().psIsUpAndRunning())
            .setFailureMessage("Primärsystem is nicht erreichbar");
  }

  public static Consequence<Boolean> leistungserbringerIsAuthorizedForAktenkonto() {
    return new BaseConsequence<>() {
      @Override
      public void evaluateFor(Actor actor) {
        var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
        leistungserbringer
            .asksFor(isLeistungserbringerAuthorizedForAktenkonto())
            .ifNotTry(
                () ->
                    leistungserbringer
                        .attemptsTo(authorizeLeistungserbringerForAktenkonto())
                        .acknowledgeThat(isLeistungserbringerAuthorizedForAktenkonto()));
      }
    };
  }

  public static Question<Answer<Boolean>> isLeistungserbringerAuthorizedForAktenkonto() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var answerValue =
          Optional.ofNullable(leistungserbringer.isAuthorizedForPatientsAktenkonto())
              .or(() -> Optional.of(ps().isLeAuthorizedForKvnr(patient.kvnr())))
              .get();
      leistungserbringer.isAuthorizedForPatientsAktenkonto(answerValue);
      return Answer.booleanAnswer(answerValue)
          .setFailureMessage(
              String.format(
                  "Leistungserbringer hat keine Berechtigung für das Aktenkonto von %s", patient));
    };
  }

  public static Task authorizeLeistungserbringerForAktenkonto() {
    return new Task() {
      @Override
      public <T extends Actor> void performAs(T actor) {
        var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();
        try {
          ps().authorizeLeForKvnr(patient.kvnr());
          leistungserbringer.isAuthorizedForPatientsAktenkonto(Boolean.TRUE);
        } catch (Exception e) {
          log.error(
              String.format(
                  "Authorizing the Leistungserbringer for the Aktenkonto of %s failed", patient),
              e);
          leistungserbringer.isAuthorizedForPatientsAktenkonto(Boolean.FALSE);
        }
      }
    };
  }

  public static Task selectKonnektor(String konnektorModel) {
    return new Task() {
      @SneakyThrows
      @Override
      public <T extends Actor> void performAs(T actor) {
        var konnektorAddress =
            Optional.ofNullable(Testdata.instance().konnektorAddresses())
                .map(kas -> kas.get(konnektorModel))
                .stream()
                .flatMap(Collection::stream)
                .findFirst()
                .orElse(null);
        if (konnektorAddress == null) {
          throw new MissingResourceException(
              "No konnektor address for given Konnektor model found",
              KonnektorAddresses.class.getSimpleName(),
              konnektorModel);
        }
        ps().configureKonnektor(URI.create(konnektorAddress).toURL());
      }
    };
  }

  public static Task signEpka() {
    return new Task() {
      @Override
      public <T extends Actor> void performAs(T actor) {
        var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();
        var signedEpka = ps().signDocument(patient.getEpka());
        patient.setSignedEpka(signedEpka);
      }
    };
  }

  public static Task signEpkaUsingConfiguration(boolean asQES, String signatureAlgorithm) {
    return new Task() {
      @Override
      public <T extends Actor> void performAs(T actor) {
        var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();
        var signedEpka = ps().signDocument(patient.getEpka(), asQES, signatureAlgorithm);
        patient.setSignedEpka(signedEpka);
      }
    };
  }

  public static Task storeEpkaInAktenkonto() {
    return new Task() {
      @Override
      public <T extends net.serenitybdd.screenplay.Actor> void performAs(T actor) {
        var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();
        var signedEpka = patient.getSignedEpka();
        var documentId = ps().putDocument(patient.kvnr(), signedEpka);
        patient.setEpkaAktenkontoId(documentId);
      }
    };
  }

  public static Question<Answer<Boolean>> epkaCanBeFoundInAktenkonto() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var canDocumentBeFound = !ps().findEpka(patient.kvnr()).isEmpty();
      return Answer.booleanAnswer(canDocumentBeFound, "ePKA counld not be found in Aktenkonto");
    };
  }

  public static Question<Answer<Boolean>> noEpkaIsPresentInAktenkonto() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var epkas = ps().findEpka(patient.kvnr());

      if (!epkas.isEmpty()) {
        ps().deleteExistingDocuments(patient.kvnr(), epkas);
        epkas = ps().findEpka(patient.kvnr());
      }

      return Answer.booleanAnswer(
          epkas.isEmpty(),
          "At least one ePKA document is present in the Aktenkonto, when there should be none");
    };
  }

  public static Task createEpka() {
    return new Task() {
      @Override
      public <T extends net.serenitybdd.screenplay.Actor> void performAs(T actor) {
        var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();
        var epka = Testdata.instance().createEpkaFromTemplate(patient);
        patient.setEpka(epka);
      }
    };
  }

  @SneakyThrows
  public static Task createEpkaFromTemplate(@NonNull String templateName) {
    return new Task() {
      @Override
      public <T extends net.serenitybdd.screenplay.Actor> void performAs(T actor) {
        var leistungserbringer = actor.usingAbilityTo(LeiActor.TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();
        var epka = Testdata.instance().createEpkaFromTemplate(patient, templateName);
        patient.setEpka(epka);
      }
    };
  }
}
