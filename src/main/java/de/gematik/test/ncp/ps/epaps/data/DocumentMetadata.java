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

package de.gematik.test.ncp.ps.epaps.data;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.With;

@Builder
@With
public class DocumentMetadata {

  @Singular private List<Author> authors;

  private String availabilityStatus;

  @Singular private List<String> confidentialityCodes;

  private String classCode;

  private String comments;

  private LocalDateTime creationTime;

  private String entryUUID;

  @Singular private List<String> eventCodes;

  private String formatCode;

  private String hash;

  private String healthcareFacilityTypeCode;

  private String languageCode;

  private String legalAuthenticator;

  private String mimeType;

  private String practiceSettingCode;

  @Singular private List<String> referenceIds;

  private LocalDateTime serviceStartTime;

  private LocalDateTime serviceStopTime;

  private Integer size;

  private String title;

  private String typeCode;

  private String uniqueId;

  private String uri;

  private String repositoryUniqueId;

  private String home;

  private String patientId;

  public de.gematik.epa.ihe.model.document.DocumentMetadata toPsDocumentMetadata() {
    return new de.gematik.epa.ihe.model.document.DocumentMetadata(
        authors.stream().map(Author::toPsAuthor).toList(),
        availabilityStatus,
        confidentialityCodes,
        classCode,
        comments,
        creationTime,
        entryUUID,
        eventCodes,
        formatCode,
        hash,
        healthcareFacilityTypeCode,
        languageCode,
        legalAuthenticator,
        mimeType,
        practiceSettingCode,
        referenceIds,
        serviceStartTime,
        serviceStopTime,
        size,
        title,
        typeCode,
        uniqueId,
        uri,
        repositoryUniqueId,
        home,
        patientId);
  }
}
