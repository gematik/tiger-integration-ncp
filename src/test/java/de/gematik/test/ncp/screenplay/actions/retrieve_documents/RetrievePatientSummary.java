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

package de.gematik.test.ncp.screenplay.actions.retrieve_documents;

import static de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils.setRepositoryUniqueId;

import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrieveDocumentsResponseDTO;
import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import java.util.Optional;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

public class RetrievePatientSummary implements Performable {

  private final PatientSummaryLevel[] patientSummaryLevel;
  private final String repositoryUniqueId;

  public RetrievePatientSummary(
      final String repositoryUniqueId, final PatientSummaryLevel... patientSummaryLevel) {
    this.repositoryUniqueId = repositoryUniqueId;
    this.patientSummaryLevel = patientSummaryLevel;
  }

  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var ncpeh = actor.usingAbilityTo(UseNcpeh.class);
    final var practitionerData = actor.usingAbilityTo(ProvidePractitionerData.class);

    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var patientRecord =
        actor.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);
    final var psaMetadata = patientRecord.getPsaDocumentMetadata();
    final var patientAccessData = actor.usingAbilityTo(ProvidePatientAccessData.class);
    Optional.ofNullable(repositoryUniqueId).ifPresent(id -> setRepositoryUniqueId(psaMetadata, id));

    final var patientSummary =
        ncpeh.retrievePatientSummary(
            patientAccessData,
            new PatientImpl(patientData.name(), patientData.kvnr(), patientData.birthDate()),
            practitionerData.profileName(),
            practitionerData.country(),
            psaMetadata,
            ncpeh.getNcpehMockControlRequestHeaders().get(UCHeaders.UC34),
            patientSummaryLevel);

    patientData.setPatientSummaryDO(patientSummary);
    Optional.ofNullable(patientSummary)
        .map(RetrieveDocumentsResponseDTO::ncpehFdResponseContent)
        .map(DataUtils::readPatientSummaryLvl1)
        .ifPresent(patientData::setPatientSummaryLvl1);
    Optional.ofNullable(patientSummary)
        .map(RetrieveDocumentsResponseDTO::ncpehFdResponseContent)
        .map(DataUtils::readPatientSummaryLvl3)
        .ifPresent(patientData::setPatientSummaryLvl3);
  }

  public static RetrievePatientSummary ofLevel(final PatientSummaryLevel... patientSummaryLevel) {
    return new RetrievePatientSummary(null, patientSummaryLevel);
  }

  public static RetrievePatientSummary fromLevelAndRepositoryUniqueId(
      final String repositoryUniqueId, final PatientSummaryLevel... patientSummaryLevel) {
    return new RetrievePatientSummary(repositoryUniqueId, patientSummaryLevel);
  }
}
