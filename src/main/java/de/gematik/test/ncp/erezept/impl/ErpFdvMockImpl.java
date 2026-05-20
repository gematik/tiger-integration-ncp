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

package de.gematik.test.ncp.erezept.impl;

import static de.gematik.test.ncp.gen.erp.fdv.model.ConsentCategory.EUDISPCONS;

import de.gematik.test.erezept.primsys.data.PatientDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.PznMedicationDto;
import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.DoctorDto;
import de.gematik.test.erezept.primsys.data.actors.DoctorNumber;
import de.gematik.test.erezept.primsys.data.valuesets.InsuranceTypeDto;
import de.gematik.test.ncp.erezept.ErpFdvService;
import de.gematik.test.ncp.erp.primsys.PrimsysRestService;
import de.gematik.test.ncp.gen.erp.fdv.model.Consent;
import de.gematik.test.ncp.gen.erp.fdv.model.ConsentCategory;
import de.gematik.test.ncp.gen.erp.fdv.model.EUAccessAuthorization;
import de.gematik.test.ncp.gen.erp.fdv.model.LoginSuccess;
import de.gematik.test.ncp.gen.erp.fdv.model.Medication;
import de.gematik.test.ncp.gen.erp.fdv.model.Patient;
import de.gematik.test.ncp.gen.erp.fdv.model.Practitioner;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ErpFdvMockImpl implements ErpFdvService {

  private static final String DEFAULT_ACCESS_TOKEN = "abc123";
  private static final String DEFAULT_ACCESS_CODE = "ABC123";

  private final Map<ConsentCategory, Consent> consentMap = new EnumMap<>(ConsentCategory.class);
  private final Map<String, Boolean> prescriptionEuRedeemableMap = new HashMap<>();
  private final Map<String, StatusEnum> prescriptionStatus = new HashMap<>();

  private EUAccessAuthorization euAccessAuthorization;
  @Setter private PrimsysRestService primsysClient;
  @Getter private String kvnr;
  @Setter private Set<String> dispensedPrescriptionIds;

  @Override
  public void start() {
    // nothing to do here
  }

  @Override
  public void stop() {
    // nothing to do here
  }

  @Override
  public Optional<LoginSuccess> login(final String kvnr) {
    this.kvnr = kvnr;
    return Optional.of(new LoginSuccess().accessToken(DEFAULT_ACCESS_TOKEN));
  }

  @Override
  public Optional<Consent> createEuDispensationConsent() {
    final var consent =
        new Consent()
            .kvnr(kvnr)
            .category(EUDISPCONS)
            .dateTime(Instant.now().atOffset(ZoneOffset.UTC));
    consentMap.put(EUDISPCONS, consent);
    return Optional.of(consent);
  }

  @Override
  public Boolean revokeEuDispensationConsent() {
    return consentMap.remove(EUDISPCONS) != null;
  }

  @Override
  public Optional<Consent> getEuDispensationConsent() {
    return Optional.ofNullable(consentMap.get(EUDISPCONS));
  }

  @Override
  public Optional<EUAccessAuthorization> grantEuAccessPermission(
      final String country, final String accessCode) {
    if (country == null || country.isBlank()) {
      return Optional.empty();
    }
    final var now = Instant.now().atOffset(ZoneOffset.UTC);
    euAccessAuthorization =
        new EUAccessAuthorization()
            .country(country)
            .accessCode(Optional.ofNullable(accessCode).orElse(DEFAULT_ACCESS_CODE))
            .createdAt(now)
            .validUntil(now.plusHours(1));

    return Optional.of(euAccessAuthorization);
  }

  @Override
  public Boolean revokeEuAccessPermission() {
    if (euAccessAuthorization != null) {
      euAccessAuthorization = null;
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  @Override
  public Optional<EUAccessAuthorization> getEuAccessPermission() {
    return Optional.ofNullable(euAccessAuthorization);
  }

  @Override
  public Optional<List<Prescription>> getPrescriptions() {
    return getPrescribed().stream()
        .map(this::toPrescription)
        .collect(Collectors.collectingAndThen(Collectors.toList(), Optional::of));
  }

  @Override
  public Optional<Prescription> getPrescription(final String id) {
    return getPrescribed().stream()
        .filter(pdto -> pdto.getPrescriptionId().equalsIgnoreCase(id))
        .map(this::toPrescription)
        .findFirst();
  }

  @Override
  public Boolean deletePrescription(final String id) {
    prescriptionEuRedeemableMap.put(id, false);
    setPrescriptionStatus(id, StatusEnum.CANCELLED);
    return Boolean.TRUE;
  }

  @Override
  public Optional<Prescription> setEuRedeemable(final String id, final boolean euRedeemable) {
    final var prescription = getPrescription(id);
    prescription.ifPresent(
        p -> {
          prescriptionEuRedeemableMap.put(id, euRedeemable);
          p.setEuRedeemableByPatient(euRedeemable);
        });

    return prescription;
  }

  @Override
  public List<BaseDispenseInformation> getMedicationDispenseInformation() {
    if (dispensedPrescriptionIds == null || dispensedPrescriptionIds.isEmpty()) {
      return List.of();
    }

    return dispensedPrescriptionIds.stream()
        .map(
            id -> {
              var dispenseInfo = new EuDispenseInformation();
              dispenseInfo.setPrescriptionId(id);
              return dispenseInfo;
            })
        .collect(Collectors.toList());
  }

  public void setPrescriptionStatus(final String prescriptionId, final StatusEnum status) {
    prescriptionStatus.put(prescriptionId, status);
  }

  private List<PrescriptionDto> getPrescribed() {
    return primsysClient.getPrescribed(kvnr);
  }

  private Prescription toPrescription(final PrescriptionDto prescriptionDto) {
    var medicationOpt = Optional.ofNullable(prescriptionDto.getMedication());
    var patientOpt = Optional.ofNullable(prescriptionDto.getPatient());
    var practitionerOpt = Optional.ofNullable(prescriptionDto.getPractitioner());

    return Prescription.builder()
        .prescriptionId(prescriptionDto.getPrescriptionId())
        .accessCode(prescriptionDto.getAccessCode())
        .medication(
            Medication.builder()
                .type(Medication.TypeEnum.PZN)
                .isVaccine(medicationOpt.map(PznMedicationDto::isVaccine).orElse(false))
                .code(medicationOpt.map(PznMedicationDto::getPzn).orElse(null))
                .build())
        .patient(
            Patient.builder()
                .name(
                    patientOpt
                        .map(p -> String.join(" ", p.getFirstName(), p.getLastName()))
                        .orElseThrow(
                            () ->
                                new NoSuchElementException("missing patient info in prescription")))
                .kvnr(patientOpt.map(PatientDto::getKvnr).orElse(null))
                .insuranceType(
                    Patient.InsuranceTypeEnum.fromString(
                        patientOpt
                            .map(PatientDto::getInsuranceType)
                            .map(InsuranceTypeDto::getCode)
                            .orElse(null)))
                .build())
        .practitioner(
            Practitioner.builder()
                .name(practitionerOpt.map(ActorDto::getName).orElse(null))
                .anr(practitionerOpt.map(DoctorDto::getAnr).map(DoctorNumber::value).orElse(null))
                .anrType(
                    Practitioner.AnrTypeEnum.fromString(
                        practitionerOpt
                            .map(DoctorDto::getAnr)
                            .map(DoctorNumber::type)
                            .orElse(null)))
                .telematikId(practitionerOpt.map(ActorDto::getTid).orElse(null))
                .qualificationType(
                    practitionerOpt.map(DoctorDto::getQualificationType).orElse(null))
                .build())
        .euRedeemableByPatient(
            prescriptionEuRedeemableMap.getOrDefault(
                prescriptionDto.getPrescriptionId(), Boolean.FALSE))
        .authoredOn(
            Optional.ofNullable(prescriptionDto.getAuthoredOn())
                .map(date -> date.toInstant().atOffset(ZoneOffset.UTC))
                .orElse(null))
        .status(
            prescriptionStatus.getOrDefault(prescriptionDto.getPrescriptionId(), StatusEnum.READY))
        .build();
  }
}
