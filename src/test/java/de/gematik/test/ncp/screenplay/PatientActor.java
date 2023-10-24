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

import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.PatientInfo;
import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrievePatientSummaryDO;
import java.time.LocalDate;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.HasTeardown;
import net.serenitybdd.screenplay.RefersToActor;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.hl7.v3.ClinicalDocument;
import org.openqa.selenium.Pdf;

/** {@link Actor} specialization to be used for patients */
@Getter
@Setter
@ToString
public class PatientActor extends Actor<PatientActor> implements PatientInfo {

  private static final String NAME_KEY = "NAME";

  private static final String KVNR_KEY = "KVNR";

  private static final String BIRTH_DATE_KEY = "BIRTH_DATE";

  private static final String EPKA_ID_KEY = "EPKA_ID";

  private static final String EPKA_KEY = "EPKA";

  private static final String SIGNED_EPKA_KEY = EPKA_KEY + "_SIGNED";

  private static final String HAS_AKTENKONTO_KEY = "HAS_AKTENKONTO";

  private static final String PSA_METADATA_KEY = "PSA_METADATA";

  private static final String PATIENT_SUMMARY_DO_KEY = "PATIENT_SUMMARY_DO";

  private static final String PATIENT_SUMMARY_CDA3_KEY = "PATIENT_SUMMARY_CDA3";

  private static final String PATIENT_SUMMARY_CDA1_KEY = "PATIENT_SUMMARY_CDA1";

  private static final String ACCESS_CODE_KEY = "ACCESS_CODE";

  /**
   * Get an instance of {@link PatientActor} to the given {@link Patient}<br>
   * If the patient is already a PatientActor, it is simply returned, cast as PatientActor. If it is
   * not, a new PatientActor is created from the patient data.
   *
   * @param patient {@link Patient} the patient
   * @return {@link PatientActor}
   */
  public static PatientActor ofPatient(@NonNull Patient patient) {
    if (patient instanceof PatientActor patientActor) {
      return patientActor;
    } else {
      return new PatientActor(patient);
    }
  }

  public PatientActor(@NonNull String name, @NonNull String kvnr, @NonNull LocalDate birthDate) {
    this(PersonName.fromString(name), kvnr, birthDate);
  }

  public PatientActor(
      @NonNull PersonName name, @NonNull String kvnr, @NonNull LocalDate birthDate) {
    super(name.toString());
    remember(NAME_KEY, name);
    remember(KVNR_KEY, kvnr);
    remember(BIRTH_DATE_KEY, birthDate);
    can(new FdvAbility());
  }

  private PatientActor(@NonNull Patient patient) {
    this(patient.name(), patient.kvnr(), patient.birthDate());
  }

  @Override
  protected PatientActor that() {
    return this;
  }

  public PatientActor presentAuthorizationDataTo(LeiActor practitioner) {
    practitioner.patient(this);
    return this;
  }

  @Override
  public String getEpkaAktenkontoId() {
    return recall(EPKA_ID_KEY);
  }

  @Override
  public void setEpkaAktenkontoId(String epkaAktenkontoId) {
    remember(EPKA_ID_KEY, epkaAktenkontoId);
  }

  @Override
  public byte[] getEpka() {
    return recall(EPKA_KEY);
  }

  @Override
  public void setEpka(byte[] epka) {
    remember(EPKA_KEY, epka);
  }

  @Override
  public byte[] getSignedEpka() {
    return recall(SIGNED_EPKA_KEY);
  }

  @Override
  public void setSignedEpka(byte[] signedEpka) {
    remember(SIGNED_EPKA_KEY, signedEpka);
  }

  @Override
  public Boolean hasAktenkonto() {
    return recall(HAS_AKTENKONTO_KEY);
  }

  @Override
  public void hasAktenkonto(Boolean hasAktenkonto) {
    remember(HAS_AKTENKONTO_KEY, hasAktenkonto);
  }

  @Override
  public PersonName name() {
    return recall(NAME_KEY);
  }

  @Override
  public String kvnr() {
    return recall(KVNR_KEY);
  }

  @Override
  public LocalDate birthDate() {
    return recall(BIRTH_DATE_KEY);
  }

  @Override
  public AdhocQueryResponse getPsaMetadata() {
    return recall(PSA_METADATA_KEY);
  }

  @Override
  public void setPsaMetadata(AdhocQueryResponse psaMetadata) {
    remember(PSA_METADATA_KEY, psaMetadata);
  }

  @Override
  public RetrievePatientSummaryDO getPatientSummaryDO() {
    return recall(PATIENT_SUMMARY_DO_KEY);
  }

  @Override
  public void setPatientSummaryDO(RetrievePatientSummaryDO patientSummaryDO) {
    remember(PATIENT_SUMMARY_DO_KEY, patientSummaryDO);
    this.getAbilityThatExtends(FdvAbility.class).tearDown();
  }

  @Override
  public ClinicalDocument getPatientSummaryLvl3() {
    return Optional.ofNullable(_getPatientSummaryLvl3())
        .or(
            () -> {
              var psOptional =
                  Optional.ofNullable(getPatientSummaryDO()).map(DataUtils::readPatientSummaryLvl3);
              psOptional.ifPresent(this::setPatientSummaryLvl3);
              return psOptional;
            })
        .orElse(null);
  }

  @Override
  public Pdf getPatientSummaryLvl1() {
    return Optional.ofNullable(_getPatientSummaryLvl1())
        .or(
            () -> {
              var psOptional =
                  Optional.ofNullable(getPatientSummaryDO()).map(DataUtils::readPatientSummaryLvl1);
              psOptional.ifPresent(this::setPatientSummaryLvl1);
              return psOptional;
            })
        .orElse(null);
  }

  @Override
  public void setPatientSummaryLvl1(Pdf patientSummary) {
    remember(PATIENT_SUMMARY_CDA1_KEY, patientSummary);
  }

  @Override
  public void setPatientSummaryLvl3(ClinicalDocument patientSummary) {
    remember(PATIENT_SUMMARY_CDA3_KEY, patientSummary);
  }

  @Override
  public String accessCode() {
    return recall(ACCESS_CODE_KEY);
  }

  @Override
  public void setAccessCode(String accessCode) {
    remember(ACCESS_CODE_KEY, accessCode);
  }

  /** Ability to make PatientActor recognizable as such */
  public static class FdvAbility implements Ability, RefersToActor, HasTeardown {

    @Getter(value = AccessLevel.PROTECTED)
    private PatientActor patient;

    FdvAbility() {}

    @Override
    public String toString() {
      return "use PatientActions";
    }

    @Override
    public FdvAbility asActor(net.serenitybdd.screenplay.Actor actor) {
      if (actor instanceof PatientActor patientActor) {
        patient = patientActor;
      } else {
        throw new IllegalCallerException(
            String.format("Actor %s does not have the ability to %s", actor.getName(), this));
      }
      return this;
    }

    @Override
    public void tearDown() {
      patient.forget(PATIENT_SUMMARY_CDA3_KEY);
    }
  }

  private ClinicalDocument _getPatientSummaryLvl3() {
    return recall(PATIENT_SUMMARY_CDA3_KEY);
  }

  private Pdf _getPatientSummaryLvl1() {
    return recall(PATIENT_SUMMARY_CDA1_KEY);
  }
}
