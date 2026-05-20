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

package de.gematik.test.ncp.util;

import static de.gematik.test.ncp.ncpeh.data.TestdataFactory.OID_AC_ERP_ASSIGNING_AUTHORITY;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.hl7.pharm.COCTMT230100UV01MaterialKind;
import org.hl7.pharm.COCTMT230100UV01SpecializedKind;
import org.hl7.v3.CD;
import org.hl7.v3.CE;
import org.hl7.v3.ClinicalDocument;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040Consumable;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040ManufacturedProduct;
import org.hl7.v3.POCDMT000040Material;
import org.hl7.v3.POCDMT000040Section;
import org.hl7.v3.POCDMT000040StructuredBody;
import org.hl7.v3.POCDMT000040SubstanceAdministration;

@UtilityClass
public class PrescriptionUtils {

  /**
   * Pattern to identify the CDA level marker suffix of prescriptions in XDS Document Entry Unique
   * IDs.
   *
   * <p>Matches the following markers: {@code |eP.PDF} or {@code |eP.XML}
   */
  public static final Pattern XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_EPED_PATTERN =
      Pattern.compile("\\|eP\\.(?:PDF|XML)$");

  private static final String XDS_DOCUMENT_ENTRY_UNIQUE_ID_DELIMITER_REGEX = "\\^";

  /**
   * The first three digits of a prescription id indicate the erp flow type
   *
   * @see <a
   *     href="https://gematik.de/fhir/erp/CodeSystem/GEM_ERP_CS_FlowType">GEM_ERP_CS_FlowType</a>
   */
  // currently hardcoded to avoid dependency on the entire erp-fhir module
  private static final String PRESCRIPTION_FLOW_TYPE_REGEX = "(?:160|162|169|200|209)";

  /**
   * Pattern to identify XDS Document Entry Unique IDs of prescriptions.
   *
   * <p>Matches both CDA level 1 and 3 IDs, e.g.:<br>
   * {@code 1.2.276.0.76.4.299^160.000.000.000.123.76|eP.XML}<br>
   * {@code 1.2.276.0.76.4.299^160.000.000.000.123.76|eP.PDF}
   *
   * <p>The regex pattern looks like this: {@code
   * ^\Q1.2.276.0.76.4.299\E\^(?:160|162|169|200|209)(?:\.\d{3}){4}\.\d{2}\|eP\.(?:PDF|XML)$}
   */
  public static final Pattern XDS_DOCUMENT_ENTRY_UNIQUE_ID_EPED_PATTERN =
      Pattern.compile(
          "^" // start of line
              + Pattern.quote(OID_AC_ERP_ASSIGNING_AUTHORITY)
              + XDS_DOCUMENT_ENTRY_UNIQUE_ID_DELIMITER_REGEX
              + PRESCRIPTION_FLOW_TYPE_REGEX

              // prescription number consisting of 4 groups of 3 digits, followed by a 2-digit
              // checksum
              + "(?:\\.\\d{3}){4}\\.\\d{2}"

              // CDA level marker for eP/eD
              + XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_EPED_PATTERN);

  /**
   * Extracts the prescription ID from a document UID
   *
   * @param documentUid the document UID, e.g. {@code
   *     1.2.276.0.76.4.299^160.000.000.000.123.76|eP.PDF}
   * @return the prescription ID: {@code 160.000.000.000.123.76}
   * @throws IllegalArgumentException if the document ID does not have a suffix that matches the
   *     pattern {@linkplain IheUtils#XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_PATTERN} or if the
   *     document ID does not contain a prescription ID part
   */
  public static String extractPrescriptionIdFromDocumentUid(final String documentUid) {
    var strippedUid = stripPrescriptionLevelMarkerFromDocumentUid(documentUid);
    var parts = strippedUid.split(XDS_DOCUMENT_ENTRY_UNIQUE_ID_DELIMITER_REGEX);
    if (parts.length == 2) {
      return parts[1];
    }
    throw new IllegalArgumentException("Invalid document ID format: " + documentUid);
  }

  /**
   * Strips the CDA level marker from a document UID of a prescription document
   *
   * @param documentUid the document UID, e.g. {@code
   *     1.2.276.0.76.4.299^160.000.000.000.123.76|eP.PDF}
   * @return the document ID without level marker: {@code 1.2.276.0.76.4.299^160.000.000.000.123.76}
   * @throws IllegalArgumentException if the document ID does not have a suffix that matches the
   *     pattern {@linkplain
   *     PrescriptionUtils#XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_EPED_PATTERN}
   */
  public static String stripPrescriptionLevelMarkerFromDocumentUid(final String documentUid) {
    var matcher = XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_EPED_PATTERN.matcher(documentUid);
    if (matcher.find()) {
      return documentUid.substring(0, matcher.start());
    }
    throw new IllegalArgumentException(
        "Not a valid Document UID of a prescription: " + documentUid);
  }

  /**
   * Gets the value describing which type of substitution is allowed for the prescribed medication
   *
   * @param clinicalDocument the ClinicalDocument
   * @return the coded substitution value as CE
   */
  public static CE getSubstitutionValueFromCda3(final ClinicalDocument clinicalDocument) {
    return (CE)
        getSection(clinicalDocument)
            .getEntry()
            .getFirst()
            .getSubstanceAdministration()
            .getEntryRelationship()
            .stream()
            .filter(
                rel ->
                    Optional.ofNullable(rel.getObservation())
                        .map(
                            obs -> {
                              var code = obs.getCode();
                              return "SUBST".equals(code.getCode())
                                  && "2.16.840.1.113883.5.6".equals(code.getCodeSystem());
                            })
                        .orElse(false))
            .map(rel -> rel.getObservation().getValue().getFirst())
            .findFirst()
            .orElse(null);
  }

  /**
   * Gets the DocumentEntry.UniqueId from the ClinicalDocument
   *
   * @param clinicalDocument the ClinicalDocument
   * @return the DocumentEntry.UniqueId
   */
  public static String getDocumentUidFromCda3(final ClinicalDocument clinicalDocument) {
    return getSection(clinicalDocument).getElementId().getExtension();
  }

  /**
   * Gets the PZN (Pharmazentralnummer) of the prescribed medication from the ClinicalDocument
   *
   * @param clinicalDocument the ClinicalDocument
   * @return the PZN
   */
  public static String getPznFromCda3(final ClinicalDocument clinicalDocument) {
    return getManufacturedMaterial(clinicalDocument).getCode().getCode();
  }

  /**
   * Gets the display name associated with the PZN of the prescribed medication from the
   * ClinicalDocument
   *
   * @param clinicalDocument the ClinicalDocument
   * @return the PZN display name
   */
  public static String getPznDisplayNameFromCda3(final ClinicalDocument clinicalDocument) {
    return getManufacturedMaterial(clinicalDocument).getCode().getDisplayName();
  }

  /**
   * Gets the name of the prescribed medication from the ClinicalDocument
   *
   * @param clinicalDocument the ClinicalDocument
   * @return the medication name
   */
  public static String getMedicationNameFromCda3(final ClinicalDocument clinicalDocument) {
    return getManufacturedMaterial(clinicalDocument).getName().getContent().getFirst().toString();
  }

  /**
   * Extract the ATC code from the given clinical document.
   *
   * @param clinicalDocument the clinical document to extract the ATC code from
   * @return the ATC code extracted from the clinical document
   * @throws NoSuchElementException if the code could not be extracted from the clinical document
   */
  public static String getAtcCode(final ClinicalDocument clinicalDocument) {
    return Optional.ofNullable(clinicalDocument)
        .map(ClinicalDocument::getComponent)
        .map(POCDMT000040Component2::getStructuredBody)
        .map(POCDMT000040StructuredBody::getComponent)
        .map(List::getFirst)
        .map(POCDMT000040Component3::getSection)
        .map(POCDMT000040Section::getEntry)
        .map(List::getFirst)
        .map(POCDMT000040Entry::getSubstanceAdministration)
        .map(POCDMT000040SubstanceAdministration::getConsumable)
        .map(POCDMT000040Consumable::getManufacturedProduct)
        .map(POCDMT000040ManufacturedProduct::getManufacturedMaterial)
        .map(POCDMT000040Material::getAsSpecializedKind)
        .map(List::getFirst)
        .map(COCTMT230100UV01SpecializedKind::getGeneralizedMaterialKind)
        .map(COCTMT230100UV01MaterialKind::getCode)
        .map(CD::getCode)
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "Could not extract ATC Code from prescription document"));
  }

  private static POCDMT000040Material getManufacturedMaterial(
      final ClinicalDocument clinicalDocument) {
    return getSection(clinicalDocument)
        .getEntry()
        .getFirst()
        .getSubstanceAdministration()
        .getConsumable()
        .getManufacturedProduct()
        .getManufacturedMaterial();
  }

  private static POCDMT000040Section getSection(final ClinicalDocument clinicalDocument) {
    return clinicalDocument
        .getComponent()
        .getStructuredBody()
        .getComponent()
        .getFirst()
        .getSection();
  }
}
