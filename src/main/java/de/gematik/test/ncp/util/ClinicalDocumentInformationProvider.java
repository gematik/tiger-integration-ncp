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

import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.hl7.v3.BIN;
import org.hl7.v3.ClinicalDocument;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040NonXMLBody;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.TS;

@UtilityClass
public class ClinicalDocumentInformationProvider {

  /**
   * Gets the patient's KVNR (Krankenversichertennummer) from the ClinicalDocument.
   *
   * @param document the ClinicalDocument
   * @return the patient's KVNR, or null if it cannot be found in the document
   */
  public static String getPatientKvnr(final ClinicalDocument document) {
    return Optional.ofNullable(getPatientRoleFromClinicalDocument(document))
        .map(POCDMT000040PatientRole::getElementId)
        .stream()
        .flatMap(Collection::stream)
        .map(II::getExtension)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  /**
   * Gets the patient's name from the ClinicalDocument.
   *
   * @param document the ClinicalDocument
   * @return the patient's name as a {@link PersonName}, or null if it cannot be found in the
   *     document
   */
  public static PersonName getPatientName(final ClinicalDocument document) {
    return Optional.ofNullable(getPatientRoleFromClinicalDocument(document))
        .map(POCDMT000040PatientRole::getPatient)
        .map(POCDMT000040Patient::getName)
        .map(DataUtils::createNameFromPNList)
        .orElse(null);
  }

  /**
   * Gets the patient's date of birth from the ClinicalDocument.
   *
   * @param document the ClinicalDocument
   * @return the patient's date of birth as a LocalDate, or null if it cannot be found in the
   *     document
   */
  public static LocalDate getPatientDateOfBirth(final ClinicalDocument document) {
    return Optional.ofNullable(getPatientRoleFromClinicalDocument(document))
        .map(POCDMT000040PatientRole::getPatient)
        .map(POCDMT000040Patient::getBirthTime)
        .map(TS::getValue)
        .map(value -> LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE))
        .orElse(null);
  }

  private static POCDMT000040PatientRole getPatientRoleFromClinicalDocument(
      @NonNull final ClinicalDocument document) {
    return Optional.ofNullable(document.getRecordTarget()).stream()
        .flatMap(Collection::stream)
        .map(POCDMT000040RecordTarget::getPatientRole)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  public static String getB64PdfPayload(final ClinicalDocument document) {
    return Optional.ofNullable(document)
        .map(ClinicalDocument::getComponent)
        .map(POCDMT000040Component2::getNonXMLBody)
        .map(POCDMT000040NonXMLBody::getText)
        .map(BIN::getContent)
        .map(List::getFirst)
        .map(String::trim)
        .orElse(null);
  }
}
