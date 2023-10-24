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

import static de.gematik.test.ncp.ncpeh.data.TestdataFactory.NCPEH_SIGNING_AUTHORITY;
import static de.gematik.test.ncp.screenplay.LeiActor.TREAT_PATIENTS;

import de.gematik.epa.conversion.internal.requests.factories.slot.SlotFactory;
import de.gematik.epa.conversion.internal.requests.factories.slot.SlotName;
import de.gematik.test.ncp.data.PatientImpl;
import de.gematik.test.ncp.data.Practitioner;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.ncpeh.NcpehInterface;
import de.gematik.test.ncp.ncpeh.NcpehInterface.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.NcpehProvider;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientDO;
import de.gematik.test.ncp.ncpeh.client.dataobject.NcpehInterfaceResponse;
import de.gematik.test.ncp.ncpeh.data.TestdataFactory;
import de.gematik.test.ncp.util.ClinicalDocumentInformationProvider;
import de.gematik.test.ncp.util.IheUtils;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import de.gematik.test.tiger.lib.TigerDirector;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import jakarta.xml.bind.JAXBElement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.Task;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;

/**
 * Methods to retrieve Screenplay objects, like tasks and questions to be used in the glue code,
 * with the focus on tasks run in the practice of a practitioner in another EU country (not
 * Germany).
 */
@Slf4j
@UtilityClass
@Accessors(fluent = true)
public class EuPracticeActions {

  private static final String KVNR_PARAMETER_KEY = "kvnr";

  @Getter(lazy = true, value = AccessLevel.PRIVATE)
  private static final NcpehInterface ncpeh = NcpehProvider.getNcpehImplementation();

  @Getter(lazy = true)
  private static final Ability useNcpeh =
      new Ability() {
        @Override
        public String toString() {
          return "canUseNcpeh";
        }
      };

  @Getter(lazy = true)
  private static final Practitioner defaultEuPractitioner =
      Testdata.instance().euPractitioners().stream()
          .findFirst()
          .orElseThrow(
              () ->
                  new TigerConfigurationException(
                      "No EU practitioner could be loaded from the config data"));

  @Getter(lazy = true)
  private static final TaskWithAnswer<Answer<Boolean>> patientIdentification =
      new TaskWithAnswer<>() {
        @Override
        public <T extends Actor> Answer<Boolean> performWithAnswer(T actor) {
          log.debug("Perform PatientIdentification task");
          try {
            var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
            var patient = leistungserbringer.patient();
            var identifyPatientData =
                ncpeh()
                    .identifyPatient(
                        patient, leistungserbringer.profileName(), leistungserbringer.country());
            log.debug("Retrieved patient identification data: " + identifyPatientData);
            var actualPatientData =
                DataUtils.readPatientDataFromIdentifyPatientResponse(
                    identifyPatientData.ncpehFdResponseContent());

            var failureMessage =
                String.format(
                    "Patient data received in the patient identification response are not as expected! "
                        + "Expected: %s;\t Actual: %s",
                    patient.patientData(), actualPatientData.patientData());

            return Answer.booleanAnswer(
                patient.samePerson(actualPatientData)
                    && patient.accessCode().equals(actualPatientData.accessCode()),
                failureMessage);
          } catch (Exception e) {
            log.debug("PatientIdentification task encountered unexpected exception", e);
            return Answer.booleanAnswer(
                Boolean.FALSE, "PatientIdentification task failed with " + e.getMessage());
          }
        }
      };

  @Getter(lazy = true)
  private static final TaskWithAnswer<IdentifyPatientDO> patientIdentificationWithKvnrNotFound =
      new TaskWithAnswer<>() {
        @Override
        public <T extends Actor> IdentifyPatientDO performWithAnswer(T actor) {
          var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
          var origPatientInfo = leistungserbringer.patient();
          var patient =
              new PatientImpl()
                  .name(origPatientInfo.name())
                  .birthDate(origPatientInfo.birthDate())
                  .kvnr(this.getTypedParameter(KVNR_PARAMETER_KEY));
          return ncpeh()
              .identifyPatient(
                  patient, leistungserbringer.profileName(), leistungserbringer.country());
        }
      };

  public static Question<Answer<Boolean>> ncpehIsAvailable() {
    return actor -> Answer.booleanAnswer(ncpeh().ncpehIsUpAndRunning());
  }

  /**
   * Returns a task, which performs the retrieval of the patient identification data.<br>
   *
   * @return {@link Task} for the retrieval of the patient identification data
   */
  public static Task retrievePatientIdentificationData() {
    return patientIdentification();
  }

  /**
   * Returns the question, if the patient identification data are correct.<br>
   * Note: This question requires, that the task {@link #retrievePatientIdentificationData()} did
   * run beforehand.
   *
   * @return {@link Question} whether the patient identification data are correct
   */
  public static Question<Answer<Boolean>> patientDataAreCorrect() {
    return patientIdentification();
  }

  /**
   * Get a {@link Task} which runs a patient identification operation using a different KVNR, than
   * present in the patient data.
   *
   * @param kvnr {@link String} KVNR to use
   * @return {@link Task} which runs the patient identification using the given KVNR
   */
  public static Task retrievePatientIdentificationDataWithWrongKvnr(String kvnr) {
    return patientIdentificationWithKvnrNotFound().useParameter(KVNR_PARAMETER_KEY, kvnr);
  }

  /**
   * Get a {@link Question} which checks, that no patient data are present in the patient
   * identification data retrieved with task {@link
   * #retrievePatientIdentificationDataWithWrongKvnr(String)}
   *
   * @return {@link Question} which checks for the absence of patient identification data
   */
  public static Question<Answer<Boolean>> patientDataAreNotPresent() {
    return patientIdentificationWithKvnrNotFound()
        .map(
            identifyPatientDO -> {
              var patientIdentificationData = identifyPatientDO.ncpehFdResponseContent();
              return Answer.booleanAnswer(
                  Objects.isNull(
                      DataUtils.readPatientDataFromIdentifyPatientResponse(
                          patientIdentificationData)));
            });
  }

  /**
   * Get a {@link Question} which checks, that a certain reason encoding is present in the patient
   * identification data retrieved with task {@link
   * #retrievePatientIdentificationDataWithWrongKvnr(String)}
   *
   * @param expectedReason {@link String} the expected reason encoding
   * @return {@link Question} which checks for the presence of the reason encoding
   */
  public static Question<Answer<Boolean>> reasonEncodingIs(@NonNull String expectedReason) {
    return patientIdentificationWithKvnrNotFound()
        .map(
            identifyPatientDO -> {
              var patientIdentificationData = identifyPatientDO.ncpehFdResponseContent();
              var asExpected =
                  Optional.ofNullable(
                          DataUtils.readReasonEncodingFromIdentifyPatientResponse(
                              patientIdentificationData))
                      .filter(reasonEncList -> reasonEncList.contains(expectedReason))
                      .isPresent();
              return Answer.booleanAnswer(
                  asExpected,
                  String.format(
                      "Reason Encoding %s was not found in the the identifyPatient response",
                      expectedReason));
            });
  }

  /**
   * Returns a task to find the ePKA in the Aktenkonto of the patient<br>
   * Requires, that a task storing the KVNR in the actor notepad did run beforehand.<br>
   *
   * @return {@link Task} to find the ePKA
   */
  public static Task findPatientSummaryData() {
    return new Task() {
      @Override
      public <T extends net.serenitybdd.screenplay.Actor> void performAs(T actor) {
        log.info("Perform findDocument task");
        var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();

        var patientSummaryDo =
            ncpeh()
                .findPatientSummary(
                    patient, leistungserbringer.profileName(), leistungserbringer.country());

        patient.setPsaMetadata(patientSummaryDo.ncpehFdResponseContent());
        log.info("Found document meta data: " + patientSummaryDo);
      }
    };
  }

  public static Question<Answer<Boolean>> twoDocumentsAreFound() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();

      var psaMetadata = patient.getPsaMetadata();
      return Answer.booleanAnswer(
          psaMetadata.getRegistryObjectList().getIdentifiable().stream()
                  .map(JAXBElement::getValue)
                  .filter(identifiable -> identifiable instanceof ExtrinsicObjectType)
                  .count()
              == 2);
    };
  }

  public static Question<Answer<Boolean>> documentFoundOfLevel(Integer level) {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();

      var documentMetadata =
          IheUtils.readDocumentMetadataFromAdhocQueryResponse(patient.getPsaMetadata(), level);

      return Answer.booleanAnswer(
          Objects.nonNull(documentMetadata), "No patient summary of level " + level + " was found");
    };
  }

  public static Question<Answer<Boolean>> sourcePatientIdIsCorrectForDocumentOfLevel(
      Integer level) {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();

      var psaMetadata = patient.getPsaMetadata();

      var extrinsicObject = IheUtils.retrieveExtrinsicObjectToCdaLevel(psaMetadata, level);

      var sourcePatientdId =
          SlotFactory.getValueFromSlot(extrinsicObject.getSlot(), SlotName.SOURCE_PATIENT_ID);

      var patientIdParts = sourcePatientdId.split("[|^]");

      String failureMessage = "";

      if (patientIdParts.length < 3) {
        failureMessage =
            "SourcePatientId does not have the expected format! Received SourcePatientId: "
                + sourcePatientdId;
      }

      if (!patient.kvnr().equals(patientIdParts[0]))
        failureMessage =
            "KVNR does not have the expected value! Expected: "
                + patient.kvnr()
                + " Received: "
                + patientIdParts[0];

      if (!patient.accessCode().equals(patientIdParts[1]))
        failureMessage =
            "Access code does not have the expected value! Expected: "
                + patient.accessCode()
                + " Received: "
                + patientIdParts[1];

      var isoIndex = patientIdParts[patientIdParts.length - 1].indexOf("&ISO");

      if (isoIndex > 1) {
        var root = patientIdParts[patientIdParts.length - 1].substring(1, isoIndex);

        if (!NCPEH_SIGNING_AUTHORITY.equals(root))
          failureMessage =
              "Root does not have the expected value! Expected: "
                  + NCPEH_SIGNING_AUTHORITY
                  + " Received: "
                  + root;
      } else {
        failureMessage =
            "Unexpected format of the root! Expected: &"
                + NCPEH_SIGNING_AUTHORITY
                + "&ISO Received: "
                + patientIdParts[patientIdParts.length - 1];
      }

      return Answer.booleanAnswer(failureMessage.isEmpty(), failureMessage);
    };
  }

  public static Question<Answer<Boolean>> twoDocumentsAreRetrieved() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();

      var psaDO = patient.getPatientSummaryDO();
      return Answer.booleanAnswer(psaDO.ncpehFdResponseContent().getDocumentResponse().size() == 2);
    };
  }

  public static Question<Answer<Boolean>> oneDocumentIsRetrieved() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();

      var psaDO = patient.getPatientSummaryDO();
      return Answer.booleanAnswer(psaDO.ncpehFdResponseContent().getDocumentResponse().size() == 1);
    };
  }

  /**
   * Returns a task retrieving the Patient Summary from the Aktenkonto.<br>
   * Requires, that the metadata of the patient summary are present in the actor notepad (e.g. Task
   * {@link #findPatientSummaryData()}).<br>
   *
   * @return {@link Task} to retrieve the Patient Summary
   */
  public static Task retrieveAllPatientSummaryData() {
    return retrievePatientSummary(PatientSummaryLevel.LEVEL_1, PatientSummaryLevel.LEVEL_3);
  }

  public static Task retrievePatientSummaryLevel1() {
    return retrievePatientSummary(PatientSummaryLevel.LEVEL_1);
  }

  public static Task retrievePatientSummaryLevel3() {
    return retrievePatientSummary(PatientSummaryLevel.LEVEL_3);
  }

  public static Task retrievePatientSummary(Integer level) {
    return retrievePatientSummary(PatientSummaryLevel.fromValue(level));
  }

  public static Question<Answer<Boolean>> structuredDocumentBelongsToPatient() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var cda3Document = patient.getPatientSummaryLvl3();
      return Answer.booleanAnswer(
          patient.samePerson(
              new PatientImpl(
                  ClinicalDocumentInformationProvider.nameFromCDA3Document(cda3Document),
                  ClinicalDocumentInformationProvider.kvnrFromCDA3Document(cda3Document),
                  ClinicalDocumentInformationProvider.birthDataFromCDA3Document(cda3Document))));
    };
  }

  public static Question<Answer<Boolean>> pdfDocumentBelongsToPatient() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var cda1Document = patient.getPatientSummaryLvl1();

      var filePath = DataUtils.writePatientSummaryToLocalFile(patient, cda1Document);

      TigerDirector.pauseExecutionAndFailIfDesired(
          String.format(
              "Prüfe, dass die korrekten Patientendaten (%s) in der CDA 1 Patient Summary im Pfad %s enthalten sind",
              patient.patientData(), filePath),
          "Die Patientendaten in der Patient Summary entsprachen nicht den erwarteten Patientdaten");

      return Answer.booleanAnswer(true);
    };
  }

  public static Question<Answer<Boolean>> documentIsOfLevel(Integer level) {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var patientSummaryDO = patient.getPatientSummaryDO();

      return Answer.booleanAnswer(
          Optional.ofNullable(patientSummaryDO)
              .map(NcpehInterfaceResponse::ncpehFdResponseContent)
              .map(RetrieveDocumentSetResponseType::getDocumentResponse)
              .stream()
              .flatMap(Collection::stream)
              .anyMatch(
                  dr ->
                      PatientSummaryLevel.fromValue(level)
                          .documentIsOfLevel(dr.getDocumentUniqueId())));
    };
  }

  public static Question<Answer<Boolean>> noDocumentWasRetrieved() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var patientSummaryDO = patient.getPatientSummaryDO();

      return Answer.booleanAnswer(
          Optional.ofNullable(patientSummaryDO)
              .map(NcpehInterfaceResponse::ncpehFdResponseContent)
              .map(RetrieveDocumentSetResponseType::getDocumentResponse)
              .map(List::isEmpty)
              .orElse(Boolean.TRUE));
    };
  }

  public static Question<Answer<Boolean>> retrieveResponseStatusIsSuccess() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var patientSummaryDO = patient.getPatientSummaryDO();

      return Answer.booleanAnswer(
          Optional.ofNullable(patientSummaryDO)
              .map(NcpehInterfaceResponse::ncpehFdResponseContent)
              .map(RetrieveDocumentSetResponseType::getRegistryResponse)
              .filter(rr -> IheUtils.SUCCESS_RESPONSE.equals(rr.getStatus()))
              .isPresent(),
          "Response status in the retrieveDocument response is not " + IheUtils.SUCCESS_RESPONSE);
    };
  }

  public static Question<Answer<Boolean>> retrieveDocumentsHcidBelongsToNcpeh() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var patientSummaryDO = patient.getPatientSummaryDO();

      return Answer.booleanAnswer(
          Optional.ofNullable(patientSummaryDO)
              .map(NcpehInterfaceResponse::ncpehFdResponseContent)
              .map(RetrieveDocumentSetResponseType::getDocumentResponse)
              .stream()
              .flatMap(Collection::stream)
              .allMatch(dr -> dr.getHomeCommunityId().endsWith(TestdataFactory.NCPEH_HCID)),
          "Not all documentResponse have the correct Home Community ID");
    };
  }

  public static Question<Answer<Boolean>> retrievedDocumentsWereRequested() {
    return actor -> {
      var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
      var patient = leistungserbringer.patient();
      var patientSummaryDO =
          Objects.requireNonNull(patient.getPatientSummaryDO(), "No patientSummary data present");

      var retrieveDocumentsRequest =
          Objects.requireNonNull(
              patientSummaryDO.ncpehFdRequestContent(), "No retrieveDocuments request present");
      var retrieveDocumentsResponse =
          Objects.requireNonNull(
              patientSummaryDO.ncpehFdResponseContent(), "No retrieveDocuments response present");

      var result =
          Optional.ofNullable(retrieveDocumentsRequest.getDocumentRequest()).stream()
              .flatMap(List::stream)
              .allMatch(
                  dreq ->
                      Optional.ofNullable(retrieveDocumentsResponse.getDocumentResponse()).stream()
                          .flatMap(List::stream)
                          .anyMatch(
                              dresp ->
                                  dreq.getDocumentUniqueId().equals(dresp.getDocumentUniqueId())
                                      && dreq.getHomeCommunityId()
                                          .equals(dresp.getHomeCommunityId())
                                      && dreq.getRepositoryUniqueId()
                                          .equals(dresp.getRepositoryUniqueId())));

      if (!result) {
        log.debug(
            "Mismatch of requested and received document data!\nRequested:\n{}\nReceived:{}",
            Utils.toStringListElements(
                retrieveDocumentsRequest.getDocumentRequest(),
                dr ->
                    "hcid: "
                        + Objects.toString(dr.getHomeCommunityId(), "null")
                        + "; repositoryUniqueId: "
                        + Objects.toString(dr.getRepositoryUniqueId(), "null")
                        + "; documentUniqueId: "
                        + Objects.toString(dr.getDocumentUniqueId(), "null")),
            Utils.toStringListElements(
                retrieveDocumentsResponse.getDocumentResponse(),
                dr ->
                    "hcid: "
                        + Objects.toString(dr.getHomeCommunityId(), "null")
                        + "; repositoryUniqueId: "
                        + Objects.toString(dr.getRepositoryUniqueId(), "null")
                        + "; documentUniqueId: "
                        + Objects.toString(dr.getDocumentUniqueId(), "null")));
      }

      return Answer.booleanAnswer(
          result, "Request and Response data of the retrieveDocuments operation do not match");
    };
  }

  private static Task retrievePatientSummary(PatientSummaryLevel... patientSummaryLevel) {
    return new Task() {
      @Override
      public <T extends net.serenitybdd.screenplay.Actor> void performAs(T actor) {
        var leistungserbringer = actor.usingAbilityTo(TREAT_PATIENTS).getLei();
        var patient = leistungserbringer.patient();
        var psaMetadata = patient.getPsaMetadata();

        var patientSummary =
            ncpeh()
                .retrievePatientSummary(
                    patient,
                    leistungserbringer.profileName(),
                    leistungserbringer.country(),
                    psaMetadata,
                    patientSummaryLevel);

        patient.setPatientSummaryDO(patientSummary);
      }
    };
  }
}
