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

package de.gematik.test.ncp.screenplay.actions;

import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.EPED_TESTACTOR_NAME;

import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.ncp.data.Medication;
import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.RememberAuthoredPrescriptions;
import de.gematik.test.ncp.screenplay.abilities.TreatPatient;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actors.OnStage;

/**
 * Action to perform a FindDocuments operation against the NCPeH (simulation).<br>
 * The found document metadata will be stored in the {@link
 * de.gematik.test.ncp.screenplay.abilities.ManagePatientRecords} ability of the actor performing
 * this action.
 */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Slf4j
public class FindDocuments implements Performable {

  /**
   * Semantic signifier for the ePrescription / eDispensation scenario.
   *
   * @see <a
   *     href="https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.1.2">gemSpec_NCPeH_FD
   *     - 6.1.2 Operation Cross_Gateway_Query::FindDocuments</a>
   */
  private static final String XDS_DOCUMENT_ENTRY_CLASS_CODE_EPED =
      "('57833-6^^2.16.840.1.113883.6.1')";

  /**
   * Semantic signifier for the Patient Summary (country A) scenario.
   *
   * @see <a
   *     href="https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.1.2">gemSpec_NCPeH_FD
   *     - 6.1.2 Operation Cross_Gateway_Query::FindDocuments</a>
   */
  private static final String XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA =
      "('60591-5^^2.16.840.1.113883.6.1')";

  private final String xdsDocumentEntryClassCode;
  private final UCHeaders ucHeaders;

  @Override
  @Step("{0} performs the findDocuments action")
  public <T extends Actor> void performAs(final T actor) {
    if (ucHeaders == null) {
      throw new NullPointerException("Use Case Headers must be set for FindDocuments action");
    }
    var practitionerData = actor.usingAbilityTo(ProvidePractitionerData.class);
    var patientAccessData = actor.usingAbilityTo(ProvidePatientAccessData.class);
    var ncpeh = actor.usingAbilityTo(UseNcpeh.class);

    var medication = ucHeaders == UCHeaders.UC10 ? getMedicationMap() : null;

    var response =
        ncpeh.findDocuments(
            patientAccessData,
            practitionerData.profileName(),
            practitionerData.country(),
            xdsDocumentEntryClassCode,
            ncpeh.getNcpehMockControlRequestHeaders().get(ucHeaders),
            medication);

    log.debug("Received document meta data: {}", response);
    var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    var patientRecord = actor.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);

    switch (ucHeaders) {
      case UC10 -> patientRecord.setPrescriptionMetadata(response.ncpehFdResponseContent()); // eped
      case UC2 -> patientRecord.setPsaDocumentMetadata(response.ncpehFdResponseContent()); // psa
      default -> throw new IllegalStateException("Unexpected Use Case: " + ucHeaders);
    }
  }

  private static Map<String, Medication> getMedicationMap() {
    var currentActor = OnStage.theActorInTheSpotlight();
    var testActor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    var prescriptions =
        testActor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions();
    OnStage.stage().shineSpotlightOn(currentActor.getName());

    return prescriptions.values().stream()
        .map(PrescriptionDto::getPrescriptionId)
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

  /**
   * Create a FindDocuments action for the ePrescription / eDispensation scenario.
   *
   * @return {@link FindDocuments} the action instance
   */
  public static FindDocuments forEped() {
    return new FindDocuments(XDS_DOCUMENT_ENTRY_CLASS_CODE_EPED, UCHeaders.UC10);
  }

  /**
   * Create a FindDocuments action for the Patient Summary (country A) scenario.
   *
   * @return {@link FindDocuments} the action instance
   */
  public static FindDocuments forPsa() {
    return new FindDocuments(XDS_DOCUMENT_ENTRY_CLASS_CODE_PSA, UCHeaders.UC2);
  }
}
