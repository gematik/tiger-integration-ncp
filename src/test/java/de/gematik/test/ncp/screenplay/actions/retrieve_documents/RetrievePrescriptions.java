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

import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.EPED_TESTACTOR_NAME;

import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.ncpeh.api.request.DocumentRequest;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.erezept.impl.ErpFdvMockImpl;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum;
import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.RememberAuthoredPrescriptions;
import de.gematik.test.ncp.screenplay.abilities.UseErpFdv;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import de.gematik.test.ncp.util.PrescriptionUtils;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actors.OnStage;

/** Retrieve prescriptions for a patient */
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class RetrievePrescriptions implements Performable {

  private final Set<String> ids;

  @Override
  @Step("{0} performs the retrievePrescriptions action")
  public <T extends Actor> void performAs(final T actor) {
    var ncpeh = actor.usingAbilityTo(UseNcpeh.class);
    var practitionerData = actor.usingAbilityTo(ProvidePractitionerData.class);
    var patientAccessData = actor.usingAbilityTo(ProvidePatientAccessData.class);

    var patient = actor.asksFor(CurrentPatient.asActor());
    var patientRecord = actor.asksFor(CurrentPatient.patientRecord());
    var patientData = patient.usingAbilityTo(ProvidePatientData.class);

    var documentsMetadata =
        ResponseUtils.toProxyFindResponse(patientRecord.getPrescriptionMetadata())
            .registryObjectLists()
            .documentsMetadata();

    List<DocumentMetadata> targetDocuments;

    if (ids == null || ids.isEmpty()) {
      // retrieve all documents if no IDs are given
      targetDocuments = documentsMetadata;
    } else {
      // when IDs are given: extract only matching documents
      targetDocuments = getTargetDocuments(ids, documentsMetadata);
    }

    var documentRequestSet = getDocumentRequestSet(targetDocuments);
    patientRecord.setRequestedDocuments(documentRequestSet);
    log.atInfo()
        .setMessage("retrieving prescriptions with the following ids:\n{}")
        .addArgument(
            () ->
                documentRequestSet.stream()
                    .map(DocumentRequest::documentUniqueId)
                    .collect(Collectors.joining("\n")))
        .log();

    var response =
        ncpeh.retrieveDocuments(
            patientAccessData,
            new PatientImpl(patientData.name(), patientData.kvnr(), patientData.birthDate()),
            practitionerData.profileName(),
            practitionerData.country(),
            documentRequestSet,
            ncpeh.getNcpehMockControlRequestHeaders().get(UCHeaders.UC11),
            getMedicationByPrescriptionId(targetDocuments));

    patientRecord.setRetrievePrescriptionsResponse(response);

    if (response != null) {
      var documentsByUid =
          response.ncpehFdResponseContent().getDocumentResponse().stream()
              .collect(
                  Collectors.toMap(
                      DocumentResponse::getDocumentUniqueId,
                      dr -> DataUtils.readClinicalDocument(dr.getDocument())));

      patientRecord.setPrescriptionsByDocumentId(documentsByUid);
      updateFdvMock(patient, documentsByUid.keySet());
    }

    // preserve actor
    OnStage.stage().shineSpotlightOn(actor.getName());
  }

  private static List<DocumentMetadata> getTargetDocuments(
      final Set<String> ids, final List<DocumentMetadata> documentsMetadata) {
    // map metadata to unique ID for efficient lookup
    HashMap<String, DocumentMetadata> metadataByUid = HashMap.newHashMap(documentsMetadata.size());

    for (DocumentMetadata dm : documentsMetadata) {
      if (metadataByUid.put(dm.uniqueId(), dm) != null) {
        throw new IllegalStateException(
            "Duplicate document UID found in query response: " + dm.uniqueId());
      }
    }

    // collect target documents and track missing IDs
    List<DocumentMetadata> targetDocuments = new ArrayList<>(ids.size());
    List<String> missingIds = new ArrayList<>(ids.size());

    for (String id : ids) {
      var metadata = metadataByUid.get(id);
      if (metadata == null) {
        missingIds.add(id);
      } else {
        targetDocuments.add(metadata);
      }
    }

    if (!missingIds.isEmpty()) {
      throw new NoSuchElementException(
          "No document metadata found for IDs: " + String.join(", ", missingIds));
    }
    return targetDocuments;
  }

  /**
   * Maps the mock data of target prescriptions to their respective IDs. Only prescriptions that are
   * both in the list of target documents and were indeed issued by the German practitioner used in
   * our test scenarios are included in the mapping.
   *
   * @return Map of prescription IDs to Medication data
   */
  private static Map<String, Medication> getMedicationByPrescriptionId(
      final List<DocumentMetadata> targetDocuments) {
    var testactor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    var prescriptions =
        testactor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions();

    return targetDocuments.stream()
        .map(dm -> PrescriptionUtils.extractPrescriptionIdFromDocumentUid(dm.uniqueId()))
        .distinct()
        .filter(prescriptions::containsKey)
        .collect(
            Collectors.toMap(
                id -> id,
                id -> {
                  var targetPrescription = prescriptions.get(id);
                  return new Medication(
                      targetPrescription.getMedication().getName(),
                      targetPrescription.getMedication().getPzn(),
                      targetPrescription.getMedicationRequest().isSubstitutionAllowed());
                }));
  }

  private static Set<DocumentRequest> getDocumentRequestSet(
      final List<DocumentMetadata> targetDocuments) {
    return targetDocuments.stream()
        .map(dm -> new DocumentRequest(dm.home(), dm.repositoryUniqueId(), dm.uniqueId()))
        .collect(Collectors.toSet());
  }

  /** Retrieve all prescriptions for the patient */
  public static RetrievePrescriptions all() {
    return new RetrievePrescriptions();
  }

  /** Retrieve prescriptions by their DocumentEntry uniqueIDs */
  public static RetrievePrescriptions byId(final Set<String> ids) {
    return new RetrievePrescriptions(Set.copyOf(ids));
  }

  /**
   * Update prescription status in the FdV mock
   *
   * @param patient the patient actor for which the prescriptions were retrieved
   * @param retrievedPrescriptionUids the set of prescription IDs that were retrieved in this action
   *     and should be updated in the FdV mock
   */
  private <T extends Actor> void updateFdvMock(
      final T patient, final Set<String> retrievedPrescriptionUids) {

    var fdv = patient.usingAbilityTo(UseErpFdv.class).getClient();

    if (fdv instanceof ErpFdvMockImpl fdvMock) {
      log.atInfo()
          .setMessage("updating prescription status to '{}' in Erp FdV mock for prescriptions:\n{}")
          .addArgument(StatusEnum.IN_PROGRESS)
          .addArgument(() -> String.join("\n", retrievedPrescriptionUids))
          .log();

      retrievedPrescriptionUids.forEach(
          id ->
              fdvMock.setPrescriptionStatus(
                  PrescriptionUtils.extractPrescriptionIdFromDocumentUid(id),
                  StatusEnum.IN_PROGRESS));
    }
  }
}
