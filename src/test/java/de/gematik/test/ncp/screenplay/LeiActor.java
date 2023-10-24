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

import de.gematik.test.ncp.data.PatientInfo;
import de.gematik.test.ncp.data.Practitioner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.RefersToActor;

/** {@link Actor} specialization to be used for practitioners */
public class LeiActor extends Actor<LeiActor> implements Practitioner {

  public static final Class<LeiAbility> TREAT_PATIENTS = LeiAbility.class;
  private static final String PATIENT_KEY = "PATIENT";

  private static final String AUTHORIZED_FOR_KVNR_KEY = "LE_AUTHORIZED_FOR_";

  private static final String COUNTRY_KEY = "COUNTRY";

  private static final String PROFILE_NAME_KEY = "PROFILE_NAME";

  public LeiActor(String name) {
    super(name);
    can(new LeiAbility());
  }

  public LeiActor(Practitioner practitioner) {
    this(practitioner.name());
    country(practitioner.country());
    profileName(practitioner.profileName());
  }

  @Override
  protected LeiActor that() {
    return this;
  }

  public LeiActor treat(PatientInfo patient) {
    return patient(patient);
  }

  protected LeiActor patient(@NonNull PatientInfo patient) {
    this.remember(PATIENT_KEY, patient);
    return this;
  }

  public PatientInfo patient() {
    return this.recall(PATIENT_KEY);
  }

  public Boolean isAuthorizedForPatientsAktenkonto() {
    return recall(AUTHORIZED_FOR_KVNR_KEY.concat(patient().kvnr()));
  }

  public LeiActor isAuthorizedForPatientsAktenkonto(Boolean answer) {
    remember(AUTHORIZED_FOR_KVNR_KEY.concat(patient().kvnr()), answer);
    return this;
  }

  @Override
  public String name() {
    return getName();
  }

  @Override
  public String country() {
    return recall(COUNTRY_KEY);
  }

  public LeiActor country(String country) {
    remember(COUNTRY_KEY, country);
    return this;
  }

  @Override
  public String profileName() {
    return recall(PROFILE_NAME_KEY);
  }

  public LeiActor profileName(String profileName) {
    remember(PROFILE_NAME_KEY, profileName);
    return this;
  }

  /** Ability to make LeiActor recognizable as such */
  public static class LeiAbility implements Ability, RefersToActor {

    @Getter(value = AccessLevel.PROTECTED)
    private LeiActor lei;

    LeiAbility() {}

    public LeiActor onPatient(PatientInfo patient) {
      return lei.patient(patient);
    }

    @Override
    public String toString() {
      return "treat patients";
    }

    @Override
    public LeiAbility asActor(net.serenitybdd.screenplay.Actor actor) {
      if (actor instanceof LeiActor leiActor) {
        lei = leiActor;
      } else {
        throw new IllegalCallerException(
            String.format("Actor %s does not have the ability to %s", actor.getName(), this));
      }
      return this;
    }
  }
}
