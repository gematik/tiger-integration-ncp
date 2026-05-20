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

import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.epa.conversion.internal.requests.factories.externalidentifier.ExternalIdentifierScheme;
import de.gematik.epa.conversion.internal.response.RegistryObjectListMapper;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.epa.ihe.model.response.ProxyFindResponse;
import de.gematik.epa.ihe.model.response.RegistryObjectLists;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import jakarta.xml.bind.JAXBElement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;

@UtilityClass
public class IheUtils {

  public static final String SUCCESS_RESPONSE =
      "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";

  public static final String FAILURE_RESPONSE =
      "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";

  public static final String STATUS_APPROVED =
      "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";

  public static final String ERROR_SEVERITY_WARNING =
      "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Warning";

  /**
   * Pattern to identify the CDA level marker suffix in XDS Document Entry Unique IDs.
   *
   * <p>Matches the following markers: {@code |eP.PDF}, {@code |eP.XML}, {@code ^PS.PDF} or {@code
   * ^PS.XML}
   */
  public static final Pattern XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_PATTERN =
      Pattern.compile("(?:\\|eP|\\^PS)\\.(?:PDF|XML)$");

  public static DocumentMetadata readDocumentMetadataFromAdhocQueryResponse(
      final AdhocQueryResponse adhocQueryResponse, final Integer cdaLevel) {
    return Optional.ofNullable(adhocQueryResponse)
        .map(ResponseUtils::toProxyFindResponse)
        .map(ProxyFindResponse::registryObjectLists)
        .map(RegistryObjectLists::documentsMetadata)
        .stream()
        .flatMap(Collection::stream)
        .filter(dm -> PatientSummaryLevel.fromValue(cdaLevel).documentIsOfLevel(dm.uniqueId()))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No document metadata found for CDA level " + cdaLevel));
  }

  public static ExtrinsicObjectType retrieveExtrinsicObjectToCdaLevel(
      final AdhocQueryResponse adhocQueryResponse, final Integer cdaLevel) {
    return streamExtrinsicObjects(adhocQueryResponse)
        .filter(
            eo ->
                PatientSummaryLevel.fromValue(cdaLevel)
                    .documentIsOfLevel(
                        RegistryObjectListMapper.externalIdentifierValue(
                            eo, ExternalIdentifierScheme.DOCUMENT_ENTRY_UNIQUE)))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "No ExtrinsicObject found in AdhocQuery response to CDA level " + cdaLevel));
  }

  /**
   * Extracts all documentUniqueIds from the given AdhocQueryResponse
   *
   * @param adhocQueryResponse an AdhocQueryResponse
   * @return a list of documentUniqueIds
   */
  public static List<String> extractDocumentIdsFromAdhocQueryResponse(
      final AdhocQueryResponse adhocQueryResponse) {
    return streamExtrinsicObjects(adhocQueryResponse)
        .map(
            eo ->
                RegistryObjectListMapper.externalIdentifierValue(
                    eo, ExternalIdentifierScheme.DOCUMENT_ENTRY_UNIQUE))
        .toList();
  }

  /**
   * Counts the number of ExtrinsicObject entries in the given AdhocQueryResponse
   *
   * @param metadata an AdhocQueryResponse
   * @return the number of ExtrinsicObject entries
   */
  public static int countExtrinsicObjects(final AdhocQueryResponse metadata) {
    return streamExtrinsicObjects(metadata).mapToInt(i -> 1).sum();
  }

  /**
   * Strips the level marker from a document UID
   *
   * @param documentUid the document UID, e.g. {@code
   *     1.2.276.0.76.4.299^160.000.000.000.123.76|eP.PDF}
   * @return the document ID without level marker: {@code 1.2.276.0.76.4.299^160.000.000.000.123.76}
   * @throws IllegalArgumentException if the document ID does not have a suffix that matches the
   *     pattern {@linkplain IheUtils#XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_PATTERN}
   */
  public static String stripLevelMarkerFromDocumentUid(final String documentUid) {
    var matcher = XDS_DOCUMENT_ENTRY_UNIQUE_ID_LEVEL_MARKER_PATTERN.matcher(documentUid);
    if (matcher.find()) {
      return documentUid.substring(0, matcher.start());
    }
    throw new IllegalArgumentException("Invalid document ID format: " + documentUid);
  }

  /**
   * Extracts all ExtrinsicObject entries from the given AdhocQueryResponse as a List.
   *
   * @param adhocQueryResponse an AdhocQueryResponse
   * @return a List of ExtrinsicObject entries
   */
  public List<ExtrinsicObjectType> getExtrinsicObjectsAsList(
      final AdhocQueryResponse adhocQueryResponse) {
    return streamExtrinsicObjects(adhocQueryResponse).toList();
  }

  private static Stream<ExtrinsicObjectType> streamExtrinsicObjects(
      final AdhocQueryResponse adhocQueryResponse) {
    return Optional.ofNullable(adhocQueryResponse)
        .map(AdhocQueryResponse::getRegistryObjectList)
        .map(RegistryObjectListType::getIdentifiable)
        .stream()
        .flatMap(Collection::stream)
        .map(JAXBElement::getValue)
        .filter(ExtrinsicObjectType.class::isInstance)
        .map(ExtrinsicObjectType.class::cast);
  }
}
