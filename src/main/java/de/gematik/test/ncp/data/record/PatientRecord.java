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

package de.gematik.test.ncp.data.record;

import de.gematik.ncpeh.api.request.DocumentRequest;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.ProvideAndRegisterDocumentSetResponseDTO;
import de.gematik.test.ncp.ncpeh.client.dataobject.RetrieveDocumentsResponseDTO;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.hl7.v3.ClinicalDocument;

/** Represents a record containing patient data and related metadata. */
@Data
public class PatientRecord {
  private Patient patient;
  private IdentifyPatientResponseDTO identifyPatientResponse;
  private AdhocQueryResponse prescriptionMetadata;
  private AdhocQueryResponse psaDocumentMetadata;
  private Set<DocumentRequest> requestedDocuments;
  private RetrieveDocumentsResponseDTO retrievePrescriptionsResponse;
  private Map<String, ClinicalDocument> prescriptionsByDocumentId;
  private Set<String> dispensedPrescriptionUids;
  private ProvideAndRegisterDocumentSetResponseDTO dispensePrescriptionsResponse;
}
