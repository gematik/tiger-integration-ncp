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

import de.gematik.test.ncp.fdv.FdvInterface;
import de.gematik.test.ncp.fdv.FdvProvider;
import de.gematik.test.ncp.ps.PsInterface;
import de.gematik.test.ncp.screenplay.PatientActor.FdvAbility;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.BaseConsequence;
import net.serenitybdd.screenplay.Consequence;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.facts.Fact;

/**
 * Methods to retrieve Screenplay objects, like tasks and questions to be used in the glue code,
 * with the focus on tasks run by the patient, e.g. using its FdV.
 */
@UtilityClass
@Slf4j
public class PatientActions {

  public interface PatientFact extends Fact {
    void setup(PatientActor actor);

    @Override
    default void setup(Actor actor) {
      if (actor instanceof PatientActor patientActor) {
        setup(patientActor);
      }
    }
  }

  /** This should become obsolete, once the FdV is fully integrated */
  @Accessors(fluent = true)
  @Getter(lazy = true)
  private static final PsInterface ps = GermanPracticeActions.ps();

  /** Interface to the PatientActions.<br> */
  @Accessors(fluent = true)
  @Getter(lazy = true)
  private static final FdvInterface fdv = FdvProvider.getDefaultFdvClient();

  public static final Class<FdvAbility> USE_FDV = PatientActor.FdvAbility.class;

  public static PatientFact activeAktenkonto() {
    return patient -> {
      if (!Optional.ofNullable(patient.hasAktenkonto())
          .orElseGet(
              () -> {
                patient.hasAktenkonto(ps().aktenkontoIsPresent(patient.kvnr()));
                return patient.hasAktenkonto();
              })) {
        throw new IllegalStateException(
            String.format(
                "Patient %s with KVNR %s does not seem to have an active Aktenkonto",
                patient.getName(), patient.kvnr()));
      }
    };
  }

  public static Question<Answer<Boolean>> isLeistungserbringerAuthorizedForAktenkonto(
      LeiActor leistungserbringer) {
    return actor -> {
      // TODO: Add proper implementation once FdV has been integrated
      return GermanPracticeActions.isLeistungserbringerAuthorizedForAktenkonto()
          .answeredBy(leistungserbringer.treat(actor.usingAbilityTo(USE_FDV).getPatient()));
    };
  }

  public static Task authorizeLeistungserbringerForAktenkonto(LeiActor leistungserbringer) {
    return new Task() {
      @Override
      public <T extends Actor> void performAs(T actor) {
        // TODO: Add proper implementation once FdV has been integrated
        GermanPracticeActions.authorizeLeistungserbringerForAktenkonto()
            .performAs(leistungserbringer.treat(actor.usingAbilityTo(USE_FDV).getPatient()));
      }
    };
  }

  public static Consequence<Boolean> leistungserbringerIsAuthorizedForAktenkonto(
      LeiActor leistungserbringer) {
    return new BaseConsequence<>() {
      @Override
      public void evaluateFor(Actor actor) {
        var patientActor = actor.usingAbilityTo(USE_FDV).getPatient();
        patientActor
            .asksFor(isLeistungserbringerAuthorizedForAktenkonto(leistungserbringer))
            .ifNotTry(
                () ->
                    patientActor
                        .attemptsTo(authorizeLeistungserbringerForAktenkonto(leistungserbringer))
                        .acknowledgeThat(
                            isLeistungserbringerAuthorizedForAktenkonto(leistungserbringer)));
      }
    };
  }

  public static Consequence<Boolean> euCountryIsAuthorizedForAktenkonto(String euCountry) {
    return new BaseConsequence<>() {
      @Override
      public void evaluateFor(Actor actor) {
        var patientActor = actor.usingAbilityTo(USE_FDV).getPatient();
        var accessCode = fdv().authorizeEuCountry(patientActor.kvnr(), euCountry);
        patientActor.setAccessCode(accessCode);
      }
    };
  }
}
