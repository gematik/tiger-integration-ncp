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

package de.gematik.test.ncp.erezept;

import de.gematik.test.ncp.erezept.impl.BaseDispenseInformation;
import de.gematik.test.ncp.gen.erp.fdv.model.Consent;
import de.gematik.test.ncp.gen.erp.fdv.model.EUAccessAuthorization;
import de.gematik.test.ncp.gen.erp.fdv.model.LoginSuccess;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription.StatusEnum;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ErpFdvService {
  /** Starts the remote FdV. */
  void start();

  /** Stops the remote FdV. */
  void stop();

  /**
   * Logs in to the remote FdV with the given KVNR.
   *
   * @param kvnr The identifier of the Aktenkonto.
   * @return Optional containing LoginSuccess if login was successful, empty otherwise.
   */
  Optional<LoginSuccess> login(String kvnr);

  /**
   * Creates a new consent to make E-prescriptions available for dispensation in the EU.
   *
   * @return Optional containing the created Consent if successful.
   */
  Optional<Consent> createEuDispensationConsent();

  /**
   * Deletes an existing consent for EU-wide access to E-prescriptions.
   *
   * @return true if deletion was successful, false otherwise.
   */
  Boolean revokeEuDispensationConsent();

  /**
   * Retrieves an existing consent for the specified category.
   *
   * @return Optional containing the Consent if it exists.
   */
  Optional<Consent> getEuDispensationConsent();

  /**
   * Creates a new EU access authorization for the specified country and access code.
   *
   * @param country The target country for authorization.
   * @param accessCode The access code for authorization.
   * @return Optional containing the created EUAccessAuthorization if successful.
   */
  Optional<EUAccessAuthorization> grantEuAccessPermission(String country, String accessCode);

  /**
   * Deletes the existing EU access authorization.
   *
   * @return true if deletion was successful, false otherwise.
   */
  Boolean revokeEuAccessPermission();

  /**
   * Retrieves the existing EU access authorization.
   *
   * @return Optional containing the found EUAccessAuthorization if present.
   */
  Optional<EUAccessAuthorization> getEuAccessPermission();

  /**
   * Retrieves a list (50 entries max.) of available prescriptions.
   *
   * @return Optional containing a set of up to 50 prescriptions or an empty Optional on error.
   */
  Optional<List<Prescription>> getPrescriptions();

  /**
   * Retrieves a list (50 entries max.) of available prescriptions, filtered to only include those
   * whose status is contained in {@code statusCodes}.
   *
   * <p>If {@code statusCodes} is {@code null} or empty, this behaves identically to {@link
   * #getPrescriptions()} and returns all prescriptions without filtering.
   *
   * @param statusCodes One or more {@link Prescription.StatusEnum} values to include in the result.
   * @return Optional containing the filtered list of prescriptions, or an empty Optional on error.
   */
  default Optional<List<Prescription>> getPrescriptions(final Collection<StatusEnum> statusCodes) {
    if (statusCodes == null || statusCodes.isEmpty()) {
      return getPrescriptions();
    }
    return getPrescriptions()
        .map(
            prescriptions ->
                prescriptions.stream().filter(p -> statusCodes.contains(p.getStatus())).toList());
  }

  /**
   * Retrieves a prescription by its unique ID.
   *
   * @param id The unique identifier of the prescription.
   * @return Optional containing the found Prescription if present.
   */
  Optional<Prescription> getPrescription(String id);

  /**
   * Deletes the prescription with the given id.
   *
   * @param id The unique identifier of the prescription.
   * @return {@code true} if deletion was successful, {@code false} otherwise
   */
  Boolean deletePrescription(String id);

  /**
   * Sets the EU redeemable flag for a prescription with the given ID.
   *
   * @param id The ID of the prescription to update.
   * @param euRedeemable true if the prescription should be EU-redeemable, false otherwise.
   * @return Optional containing the updated Prescription object if successful.
   */
  Optional<Prescription> setEuRedeemable(String id, boolean euRedeemable);

  /**
   * Retrieves a list of all dispense information for the patient.
   *
   * @return List of MedicationDispenseInformation objects containing dispense details.
   */
  List<BaseDispenseInformation> getMedicationDispenseInformation();
}
