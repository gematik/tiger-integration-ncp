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

import de.gematik.epa.conversion.ResponseUtils;
import de.gematik.epa.conversion.internal.requests.factories.externalidentifier.ExternalIdentifierScheme;
import de.gematik.epa.conversion.internal.response.RegistryObjectListMapper;
import de.gematik.epa.ihe.model.document.DocumentMetadata;
import de.gematik.epa.ihe.model.response.ProxyFindResponse;
import de.gematik.epa.ihe.model.response.RegistryObjectLists;
import de.gematik.test.ncp.ncpeh.NcpehInterface.PatientSummaryLevel;
import jakarta.xml.bind.JAXBElement;
import java.util.Collection;
import java.util.Optional;
import lombok.experimental.UtilityClass;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;

@UtilityClass
public class IheUtils {

  public static final String SUCCESS_RESPONSE =
      "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";

  public static final String STATUS_APPROVED =
      "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved";

  public static DocumentMetadata readDocumentMetadataFromAdhocQueryResponse(
      AdhocQueryResponse adhocQueryResponse, Integer cdaLevel) {
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
      AdhocQueryResponse adhocQueryResponse, Integer cdaLevel) {
    return Optional.ofNullable(adhocQueryResponse)
        .map(AdhocQueryResponse::getRegistryObjectList)
        .map(RegistryObjectListType::getIdentifiable)
        .stream()
        .flatMap(Collection::stream)
        .map(JAXBElement::getValue)
        .filter(identifiable -> ExtrinsicObjectType.class.isAssignableFrom(identifiable.getClass()))
        .map(ExtrinsicObjectType.class::cast)
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
}
