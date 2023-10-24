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

package de.gematik.test.ncp.ps;

import java.net.URL;
import java.util.List;

/**
 * Interface describing the functions to interact with the PS (Simulation). This should define the
 * single point of interaction, between the screenplay / glue code of the test suite and the PS like
 * a simulator, a mock or even a real implementation. Thus, if functionality is missing here, add
 * it! Don't go sideways!
 */
public interface PsInterface {

  /**
   * Check if the PS is running and its operations ready to be used
   *
   * @return true if it is, false otherwise
   */
  boolean psIsUpAndRunning();

  /**
   * Check if the LE using the Konnektor (identified by its SM-B) has the right to write an ePKA in
   * the Aktenkonto of the patient, identified by the given KVNR.
   *
   * @param kvnr the patient identification number, a.k.a. KVNR
   * @return true, if the LE is authorized, false otherwise
   */
  boolean isLeAuthorizedForKvnr(String kvnr);

  /**
   * Authorize the LE to have right to write an ePKA to the Aktenkonto of the patient, identified by
   * the given KVNR.
   *
   * @param kvnr the patient identification number, a.k.a. KVNR
   */
  void authorizeLeForKvnr(String kvnr);

  /**
   * Upload an ePKA document for the patient, identified by the given KVNR.
   *
   * @param kvnr the patient identification number, a.k.a. KVNR
   * @param epka document (ePKA) to upload
   * @return the title of the uploaded document (which is unique for every upload)
   */
  String putDocument(String kvnr, byte[] epka);

  /**
   * Search for the document in the Aktenkonto of a patient, identified by the given KVNR, having
   * the given title
   *
   * @param kvnr the patient identification number, a.k.a. KVNR
   * @param documentTitle the title of the document to find
   * @return true, if the document was found, false otherwise
   */
  boolean documentCanBeFound(String kvnr, String documentTitle);

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
   * Find all ePKA documents in the Aktenkonto belonging to the given kvnr and return their
   * entryUUIDs.
   *
   * @param kvnr the PatientID used to identify the Aktenkonto
   * @return {@link List} of entryUUIDs
   */
  List<String> findEpka(String kvnr);

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
   * In the PS switch to the Konnektor with the given address.
   *
   * @param konnektorAddress {@link URL} address of the Konnektor to switch to.
   */
  void configureKonnektor(URL konnektorAddress);
}
