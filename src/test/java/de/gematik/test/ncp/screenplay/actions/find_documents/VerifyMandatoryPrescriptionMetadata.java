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

package de.gematik.test.ncp.screenplay.actions.find_documents;

import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.EPED_TESTACTOR_NAME;
import static de.gematik.test.ncp.glue.epeda.ActorsInitializationSteps.PRACTITIONER_DE_NAME;
import static de.gematik.test.ncp.ncpeh.data.TestdataFactory.OID_AC_ERP_ASSIGNING_AUTHORITY;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.epa.conversion.internal.requests.factories.classification.ClassificationScheme;
import de.gematik.epa.conversion.internal.requests.factories.externalidentifier.ExternalIdentifierScheme;
import de.gematik.epa.conversion.internal.response.RegistryObjectListMapper;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.epa.ihe.model.response.ProxyFindResponse;
import de.gematik.epa.ihe.model.response.RegistryObjectLists;
import de.gematik.ncpeh.ehdsi.valuesets.EhdsiSubstitutionCode;
import de.gematik.test.ncp.data.PrescriptionCdaLevel;
import de.gematik.test.ncp.ncpeh.data.RequestBaseBuilder;
import de.gematik.test.ncp.screenplay.abilities.*;
import de.gematik.test.ncp.util.IheUtils;
import de.gematik.test.ncp.util.PrescriptionUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.*;
import org.hamcrest.Matchers;

/** Action to verify mandatory metadata for all listed prescription documents. */
public class VerifyMandatoryPrescriptionMetadata implements Performable {

  private static final String EVENT_CODE_OPEN = "urn:ihe:iti:xdw:2011:eventCode:open";
  private static final String FORMAT_CODE_CDA3 = "urn:epsos:ep:pre:2010";
  private static final String FORMAT_CODE_CDA1 = "urn:ihe:iti:xds-sd:pdf:2008";
  private static final String CLASS_CODE = "57833-6";
  private static final String HEALTHCARE_FACILITY_TYPE_CODE = "DE";
  private static final String ISO_SUFFIX = "&ISO";
  private static final String PATIENTID_FIRST_SEPARATOR = "|";
  private static final String PATIENTID_SECOND_SEPARATOR = "^^^&";
  public static final String ATC_CLASSIFICATION_SCHEME = "2.16.840.1.113883.6.73";
  public static final String EVENT_CODE_OPEN_LOCALIZED_STRING = "Open";
  public static final String HEALTHCARE_FACILITY_COUNTRY = "Germany";
  public static final String SLOT_NAME_AUTHOR_PERSON = "authorPerson";
  public static final String SLOT_NAME_AUTHOR_INSTITUTION = "authorInstitution";
  public static final String SLOT_NAME_REPOSITORY_UNIQUE_ID = "repositoryUniqueId";
  public static final String SLOT_NAME_CODING_SCHEME = "codingScheme";
  public static final String DOCUMENT_ENTRY_PATIENT_ID_LOCALIZED_STRING =
      "XDSDocumentEntry.patientId";
  public static final String HL7_SUBSTANCE_ADMIN_SUBSTITUTION_OID = "2.16.840.1.113883.5.1070";

  @Step("{0} verifies mandatory metadata for listed prescription documents")
  @Override
  public <T extends Actor> void performAs(final T actor) {
    final var patient = actor.usingAbilityTo(TreatPatient.class).getPatient();
    final var patientData = patient.usingAbilityTo(ProvidePatientData.class);
    final var patientRecord =
        actor.usingAbilityTo(ManagePatientRecords.class).getRecordFor(patient);
    final var documentsMetadata =
        getDocumentsMetadataFromRecord(patientRecord.getPrescriptionMetadata(), patient.getName());
    final var documentsByPrescriptionId = groupByPrescriptionId(documentsMetadata);

    actor.attemptsTo(Ensure.that(documentsMetadata).isNotEmpty());
    for (final var entry : documentsByPrescriptionId.entrySet()) {
      verifyPrescriptionDocuments(
          actor,
          patientData,
          entry.getKey(),
          patientRecord.getPrescriptionMetadata(),
          entry.getValue());
    }
  }

  private static List<DocumentMetadata> getDocumentsMetadataFromRecord(
      final AdhocQueryResponse prescriptionMetadata, final String patientName) {
    return Optional.ofNullable(prescriptionMetadata)
        .map(ResponseUtils::toProxyFindResponse)
        .map(ProxyFindResponse::registryObjectLists)
        .map(RegistryObjectLists::documentsMetadata)
        .orElseThrow(
            () ->
                new IllegalStateException(
                    "No prescription metadata stored for patient " + patientName));
  }

  private static Map<String, List<DocumentMetadata>> groupByPrescriptionId(
      final List<DocumentMetadata> documentsMetadata) {
    return documentsMetadata.stream()
        .filter(Objects::nonNull)
        .collect(
            Collectors.groupingBy(
                metadata ->
                    PrescriptionUtils.extractPrescriptionIdFromDocumentUid(metadata.uniqueId())));
  }

  private static void verifyPrescriptionDocuments(
      final Actor actor,
      final ProvidePatientData patientData,
      final String prescriptionId,
      final AdhocQueryResponse prescriptionMetadata,
      final List<DocumentMetadata> documents) {
    actor.attemptsTo(
        Ensure.that(prescriptionId).isNotBlank(), Ensure.that(documents.size()).isEqualTo(2));
    /*
    for every FDV-selected E-Rezept (-> ID) and every matching IntrinsicObject in the FindDocument-Response
    (-> prescriptionId) do following checks:
    (IntrinsicObject are expected to be listed twice per E-Rezept - format codes for CDA L1 & L3)
    (see https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27914)
    Identify Prescriptions from ExternalIdentifier@identificationScheme="urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab"
    with @value=<E-Rezept-Id>^<Suffix> (^eP.XML or ^eP.PDF)
       check content according to given metadata >>in every IntrinsicObject<< for
    */

    boolean hasXml = false;
    boolean hasPdf = false;

    Map<String, ExtrinsicObjectType> extrinsicObjects =
        IheUtils.getExtrinsicObjectsAsList(prescriptionMetadata).stream()
            .collect(
                Collectors.toMap(
                    o ->
                        RegistryObjectListMapper.externalIdentifierValue(
                            o, ExternalIdentifierScheme.DOCUMENT_ENTRY_UNIQUE),
                    o -> o));
    for (final var metadata : documents) {
      /*
            8. uniqueId: (ExternalIdentifier@identificationScheme="urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")
         check that every ExtrinsicObject contains an expected E-Rezept Id
         (ExternalIdentifier@@value=<E-Rezept-Id>^<Suffix> (^eP.XML or ^eP.PDF))
      */
      final var uniqueId = metadata.uniqueId();
      actor.attemptsTo(
          Ensure.that(uniqueId).isNotBlank(),
          Ensure.that(uniqueId)
              .matches(PrescriptionUtils.XDS_DOCUMENT_ENTRY_UNIQUE_ID_EPED_PATTERN),
          Ensure.that(uniqueId).contains(prescriptionId));
      var extrinsicObject = extrinsicObjects.get(uniqueId);
      if (extrinsicObject == null) {
        throw new IllegalStateException("ExtrinsicObject for uniqueId " + uniqueId + " not found");
      }
      /*
            5. formatCode: (Classification@classificationScheme="urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d")
               check
               * for CDA L3 (XML): Classification@nodeRepresentation="urn:epsos:ep:pre:2010"
               * for CDA L1 (PDF): Classification@nodeRepresentation="urn:ihe:iti:xds-sd:pdf:2008"
      */
      if (uniqueId.endsWith(PrescriptionCdaLevel.LEVEL_3.getSuffix())) {
        hasXml = true;
        actor.attemptsTo(Ensure.that(metadata.formatCode()).isEqualTo(FORMAT_CODE_CDA3));
      }
      if (uniqueId.endsWith(PrescriptionCdaLevel.LEVEL_1.getSuffix())) {
        hasPdf = true;
        actor.attemptsTo(Ensure.that(metadata.formatCode()).isEqualTo(FORMAT_CODE_CDA1));
      }

      verifyCommonMetadata(actor, patientData, metadata, extrinsicObject);
    }

    actor.attemptsTo(Ensure.that(hasXml).isTrue(), Ensure.that(hasPdf).isTrue());
  }

  private static void verifyCommonMetadata(
      final Actor actor,
      final ProvidePatientData patientData,
      final DocumentMetadata metadata,
      ExtrinsicObjectType extrinsicObject) {

    /*
     1. patient identifier string:
         check for patient identifier strings in every IntrinsicObject (detailed format requirements see A_27914)
         * ExternalIdentifier@identificationScheme="urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427"
           check ExternalIdentifier@value contains patient identifier string according to A_27914
           check ExternalIdentifier.Name.LocalizedString@value="XDSDocumentEntry.patientId"
         * Slot@name="sourcePatientId":
           check Slot.ValueList.Value contains patient identifier string according to A_27914
    */
    String accessCode = actor.usingAbilityTo(ProvidePatientAccessData.class).getAccessCode();

    // The character '&' is encoded as '&amp;' in the serialized message but is converted by the XML
    // parser, so we check for '&' in the patient id
    String expectedPatientId =
        patientData.kvnr()
            + PATIENTID_FIRST_SEPARATOR
            + accessCode
            + PATIENTID_SECOND_SEPARATOR
            + RequestBaseBuilder.KVNR_ASSIGNING_AUTHORITY
            + ISO_SUFFIX;
    String expectedSourcePatientId = "'" + expectedPatientId + "'";
    actor.attemptsTo(
        Ensure.that(metadata.patientId()).isEqualTo(expectedPatientId),
        Ensure.that(metadata.sourcePatientId()).isEqualTo(expectedSourcePatientId));
    List<ExternalIdentifierType> externalIdentifiers =
        extrinsicObject.getExternalIdentifier().stream()
            .filter(Objects::nonNull)
            .filter(
                (e) ->
                    e.getIdentificationScheme()
                        .equals(ExternalIdentifierScheme.DOCUMENT_ENTRY_PATIENT.getId()))
            .toList();
    actor.attemptsTo(
        Ensure.that(externalIdentifiers).hasSize(1),
        Ensure.that(externalIdentifiers.getFirst().getName().getLocalizedString()).hasSize(1),
        Ensure.that(
                externalIdentifiers.getFirst().getName().getLocalizedString().getFirst().getValue())
            .isEqualTo(DOCUMENT_ENTRY_PATIENT_ID_LOCALIZED_STRING));

    /*
          2. Description: (ExtrinsicObject.Description)
         check, that Description in every IntrinsicObject is not empty
    */
    actor.attemptsTo(Ensure.that(extrinsicObject.getDescription().toString()).isNotEmpty());
    /*
          3. ATC-Code: (Classification@classificationScheme="urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4")
             check, that Classification@nodeRepresentation is not empty
    */
    var atcCode =
        extrinsicObject.getClassification().stream()
            .filter(Objects::nonNull)
            .filter(
                e ->
                    e.getClassificationScheme()
                        .equals(ClassificationScheme.DOCUMENT_ENTRY_EVENT.getUrn()))
            .filter(
                e ->
                    e.getSlot().stream()
                        .anyMatch(
                            s ->
                                (s.getName().equals(SLOT_NAME_CODING_SCHEME)
                                    && s.getValueList()
                                        .getValue()
                                        .contains(ATC_CLASSIFICATION_SCHEME))))
            .findAny()
            .orElse(null);
    actor.attemptsTo(Ensure.that(atcCode != null).isTrue());
    actor.attemptsTo(Ensure.that(atcCode.getNodeRepresentation()).isNotEmpty());
    actor.attemptsTo(Ensure.that(atcCode.getNodeRepresentation().length()).isBetween(1, 7));
    /*
          4. ATC-Text: (Classification@classificationScheme="urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4")
         check, that Classification.Name.LocalizedString@value is not empty
    */
    actor.attemptsTo(
        Ensure.that(atcCode.getName().getLocalizedString()).hasSize(1),
        Ensure.that(atcCode.getName().getLocalizedString().getFirst().getValue()).isNotEmpty());
    /*
          5. repositoryUniqueId: (Slot@name="repositoryUniqueId")
         Slot.ValueList.Value contains the OID_AC_eRp_ASSIGNING_AUTHORITY
    */
    var repositoryUniqueIdValueList =
        extrinsicObject.getSlot().stream()
            .filter(s -> s.getName().equals(SLOT_NAME_REPOSITORY_UNIQUE_ID))
            .flatMap(s -> s.getValueList().getValue().stream())
            .toList();
    actor.attemptsTo(Ensure.that(repositoryUniqueIdValueList).isNotEmpty());
    actor.attemptsTo(
        Ensure.that(repositoryUniqueIdValueList).contains(OID_AC_ERP_ASSIGNING_AUTHORITY));

    /*
          6. classCode: (Classification@classificationScheme="urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a")
         check Classification@nodeRepresentation="57833-6"
    */

    var classCodes =
        extrinsicObject.getClassification().stream()
            .filter(Objects::nonNull)
            .filter(
                e ->
                    e.getClassificationScheme()
                        .equals(ClassificationScheme.DOCUMENT_ENTRY_CLASS.getUrn()))
            .filter(e -> e.getNodeRepresentation().equals(CLASS_CODE))
            .toList();
    actor.attemptsTo(Ensure.that(classCodes).isNotEmpty());
    /*
          7. eventCodeList: (Classification@classficationScheme="urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4")
         check entry eventCodeList
         * Classification@nodeRepresentation="urn:ihe:iti:xdw:2011:eventCode:open"
         * Classification.Name.LocalizedString@value="Open"
    */
    List<List<LocalizedStringType>> localizedStringsOfEntryEvents =
        extrinsicObject.getClassification().stream()
            .filter(Objects::nonNull)
            .filter(
                e ->
                    e.getClassificationScheme()
                        .equals(ClassificationScheme.DOCUMENT_ENTRY_EVENT.getUrn()))
            .filter(e -> e.getNodeRepresentation().equals(EVENT_CODE_OPEN))
            .map(ClassificationType::getName)
            .map(InternationalStringType::getLocalizedString)
            .toList();
    actor.attemptsTo(
        Ensure.that(localizedStringsOfEntryEvents).hasSize(1),
        Ensure.that(localizedStringsOfEntryEvents.getFirst()).hasSize(1),
        Ensure.that(localizedStringsOfEntryEvents.getFirst().getFirst().getValue())
            .isEqualTo(EVENT_CODE_OPEN_LOCALIZED_STRING));

    /*
          6. healthCareFacilityTypeCode: (Classification@classificationScheme="urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1")
             check entry healthCareFacilityTypeCode
             * Classification@nodeRepresentation="DE"
             * Classification.Name.LocalizedString@value="Germany"

    */
    List<List<LocalizedStringType>> healthCareFacilityTypeCodes =
        extrinsicObject.getClassification().stream()
            .filter(Objects::nonNull)
            .filter(
                e ->
                    e.getClassificationScheme()
                        .equals(ClassificationScheme.DOCUMENT_ENTRY_HEALTHCARE_FACILITY.getUrn()))
            .filter(e -> e.getNodeRepresentation().equals(HEALTHCARE_FACILITY_TYPE_CODE))
            .map(ClassificationType::getName)
            .map(InternationalStringType::getLocalizedString)
            .toList();
    actor.attemptsTo(
        Ensure.that(healthCareFacilityTypeCodes).hasSize(1),
        Ensure.that(healthCareFacilityTypeCodes.getFirst()).hasSize(1),
        Ensure.that(healthCareFacilityTypeCodes.getFirst().getFirst().getValue())
            .isEqualTo(HEALTHCARE_FACILITY_COUNTRY));

    /*       9. authorPerson: (Classification@classificationScheme="urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d" &
                           Classification.Slot@name="authorPerson")
             check Classification.Slot.ValueList.Value content according to A_27914

    */
    List<SlotType1> authorSlotName =
        extrinsicObject.getClassification().stream()
            .filter(Objects::nonNull)
            .filter(
                e ->
                    e.getClassificationScheme()
                        .equals(ClassificationScheme.DOCUMENT_ENTRY_AUTHOR.getUrn()))
            .map(ClassificationType::getSlot)
            .flatMap(List::stream)
            .filter(s -> s.getName().equals(SLOT_NAME_AUTHOR_PERSON))
            .toList();
    actor.attemptsTo(
        Ensure.that(authorSlotName).hasSize(1),
        Ensure.that(authorSlotName.getFirst().getValueList().getValue())
            .containsExactly(PRACTITIONER_DE_NAME));

    /*

         10. authorInstitution: (Classification@classificationScheme="urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d" &
                       Classification.Slot@name="authorInstitution")
         check Classification.Slot.ValueList.Value content according to A_27914
    */
    List<SlotType1> authorInstitutionName =
        extrinsicObject.getClassification().stream()
            .filter(Objects::nonNull)
            .filter(
                e ->
                    e.getClassificationScheme()
                        .equals(ClassificationScheme.DOCUMENT_ENTRY_AUTHOR.getUrn()))
            .map(ClassificationType::getSlot)
            .flatMap(List::stream)
            .filter(s -> s.getName().equals(SLOT_NAME_AUTHOR_INSTITUTION))
            .toList();
    var testActor = OnStage.theActorCalled(EPED_TESTACTOR_NAME);
    var prescriptionMap =
        testActor.usingAbilityTo(RememberAuthoredPrescriptions.class).getPrescriptions();
    var currentPrescription =
        prescriptionMap.get(
            PrescriptionUtils.extractPrescriptionIdFromDocumentUid(metadata.uniqueId()));
    actor.attemptsTo(Ensure.that(currentPrescription != null).isTrue());
    actor.attemptsTo(
        Ensure.that(authorInstitutionName).hasSize(1),
        Ensure.that(currentPrescription.getPractitioner().getOfficeName()).isNotBlank(),
        Ensure.that(authorInstitutionName.getFirst().getValueList().getValue())
            .containsExactly(currentPrescription.getPractitioner().getOfficeName()));

    // Test substitution
    List<ClassificationType> substanceSubstitutionClassifications =
        extrinsicObject.getClassification().stream()
            .filter(Objects::nonNull)
            .filter(
                e ->
                    e.getClassificationScheme()
                        .equals(ClassificationScheme.DOCUMENT_ENTRY_EVENT.getUrn()))
            .filter(
                c ->
                    c.getSlot().stream()
                        .filter(s -> s.getName().equals("codingScheme"))
                        .anyMatch(
                            s ->
                                s.getValueList()
                                    .getValue()
                                    .contains(HL7_SUBSTANCE_ADMIN_SUBSTITUTION_OID)))
            .toList();
    var isSubstitutionAllowed = currentPrescription.getMedicationRequest().isSubstitutionAllowed();
    String none = EhdsiSubstitutionCode.NONE.getCode();
    var isMissingOrHasNodeRepresentationQuestion =
        Question.about("the substitution flag")
            .answeredBy(
                a ->
                    Optional.ofNullable(substanceSubstitutionClassifications)
                        .filter(Objects::nonNull)
                        .filter(l -> !l.isEmpty())
                        .map(List::getFirst)
                        .map(ClassificationType::getNodeRepresentation)
                        .orElse(null));
    if (isSubstitutionAllowed) {
      actor.should(
          seeThat(isMissingOrHasNodeRepresentationQuestion, Matchers.not(Matchers.equalTo(none))));
    } else {
      actor.should(seeThat(isMissingOrHasNodeRepresentationQuestion, Matchers.equalTo(none)));
    }
  }

  public static VerifyMandatoryPrescriptionMetadata inFindDocumentsResponse() {
    return new VerifyMandatoryPrescriptionMetadata();
  }
}
