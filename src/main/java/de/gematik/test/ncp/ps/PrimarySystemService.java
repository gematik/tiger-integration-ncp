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

package de.gematik.test.ncp.ps;

import de.gematik.test.ncp.data.Practice;
import de.gematik.test.ncp.data.SmbCard;
import java.util.List;

/**
 * Interface describing the functions to interact with the PS (Simulation). This should define the
 * single point of interaction, between the screenplay / glue code of the test suite and the PS like
 * a simulator, a mock or even a real implementation. Thus, if functionality is missing here, add
 * it! Don't go sideways!
 */
public interface PrimarySystemService {

  /**
   * Authorize the LE to have right to write an ePKA to the Aktenkonto of the patient, identified by
   * the given KVNR.
   *
   * @param kvnr the patient identification number, a.k.a. KVNR
   */
  void authorizeLeForKvnr(String telematikId, String kvnr);

  /**
   * Upload an ePKA document for the patient, identified by the given KVNR.
   *
   * @param kvnr the patient identification number, a.k.a. KVNR
   * @param practice the german practice
   * @param epka document (ePKA) to upload
   * @return the title of the uploaded document (which is unique for every upload)
   */
  String putDocument(String kvnr, final Practice practice, byte[] epka);

  /**
   * Replace an ePKA document for the patient, identified by the given KVNR.
   *
   * @param kvnr the patient identification number, a.k.a. KVNR
   * @param practice the german practice
   * @param epka document (ePKA) to upload
   * @return the title of the uploaded document (which is unique for every upload)
   */
  String replaceDocument(String kvnr, final Practice practice, byte[] epka);

  /**
   * Check, whether for the given KVNR an Aktenkonto exists.
   *
   * @param kvnr {@link String} KVNR of the patient
   * @return {@code boolean} true, if an Aktenkonto exists, false otherwise
   */
  boolean aktenkontoIsPresent(String kvnr);

  /**
   * Create a CMS signature of a document, using the HBA in the Konnektor. This means a QES
   * signature will be created.
   *
   * @param document {@code byte[]} byte stream of the document, which shall be signed.
   * @return {@code byte[]} Signature including the signed document.
   */
  byte[] signDocument(byte[] document);

  byte[] signDocument(byte[] document, boolean asQES, String signatureAlgorithm);

  /**
   * Find ePKA documents with ClassCode.MEDIZINISCHER_AUSWEIS, FormatCode.PATIENTEN_KURZ_AKTE &amp;
   * TypeCode.ERGEBNISSE_DIAGNOSTIK in the Aktenkonto belonging to the given kvnr and return their
   * entryUUIDs.
   *
   * @param kvnr the PatientID used to identify the Aktenkonto
   * @return {@link List} of entryUUIDs
   */
  List<String> findEpka(String kvnr);

  /**
   * Find all documents in the Aktenkonto belonging to the given kvnr and return their entryUUIDs.
   *
   * @param kvnr the PatientID used to identify the Aktenkonto
   * @return {@link List} of entryUUIDs
   */
  List<String> findAllDocuments(String kvnr);

  /**
   * Delete the documents with the entryUUIDs provided in the Aktenkonto identified by the given
   * kvnr.<br>
   * If the operation fails for any reason, a {@link PsException} is thrown
   *
   * @param kvnr the PatientID used to identify the Aktenkonto
   * @param documentEntryUUIDs {@link List} of entryUUIDs
   */
  void deleteExistingDocuments(String kvnr, List<String> documentEntryUUIDs);

  /**
   * Retrieves the practice information by calling the card API proxy.
   *
   * @return the practice information
   * @throws PsException if the card API response is unsuccessful or no practice is found
   */
  SmbCard getSmbCard();
}
