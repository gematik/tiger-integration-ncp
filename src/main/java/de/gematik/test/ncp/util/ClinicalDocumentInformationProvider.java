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

package de.gematik.test.ncp.util;

import de.gematik.test.ncp.data.PersonName;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.hl7.v3.ClinicalDocument;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Patient;
import org.hl7.v3.POCDMT000040PatientRole;
import org.hl7.v3.POCDMT000040RecordTarget;
import org.hl7.v3.TS;

@UtilityClass
public class ClinicalDocumentInformationProvider {

  public static String kvnrFromCDA3Document(ClinicalDocument document) {
    return Optional.ofNullable(getPatientRoleFromClinicalDocument(document))
        .map(POCDMT000040PatientRole::getElementId)
        .stream()
        .flatMap(Collection::stream)
        .map(II::getExtension)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }

  public static PersonName nameFromCDA3Document(ClinicalDocument document) {
    return Optional.ofNullable(getPatientRoleFromClinicalDocument(document))
        .map(POCDMT000040PatientRole::getPatient)
        .map(POCDMT000040Patient::getName)
        .map(DataUtils::createNameFromPNList)
        .orElse(null);
  }

  public static LocalDate birthDataFromCDA3Document(ClinicalDocument document) {
    return Optional.ofNullable(getPatientRoleFromClinicalDocument(document))
        .map(POCDMT000040PatientRole::getPatient)
        .map(POCDMT000040Patient::getBirthTime)
        .map(TS::getValue)
        .map(
            value ->
                LocalDate.parse(
                    value, new DateTimeFormatterBuilder().appendPattern("yyyyMMdd").toFormatter()))
        .orElse(null);
  }

  private static POCDMT000040PatientRole getPatientRoleFromClinicalDocument(
      @NonNull ClinicalDocument document) {
    return Optional.ofNullable(document.getRecordTarget()).stream()
        .flatMap(Collection::stream)
        .map(POCDMT000040RecordTarget::getPatientRole)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null);
  }
}
