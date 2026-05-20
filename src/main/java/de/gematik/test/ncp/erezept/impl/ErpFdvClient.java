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

package de.gematik.test.ncp.erezept.impl;

import static de.gematik.test.ncp.gen.erp.fdv.model.ConsentCategory.EUDISPCONS;
import static jakarta.ws.rs.core.Response.Status.Family.SUCCESSFUL;

import de.gematik.test.ncp.erezept.ErpFdvService;
import de.gematik.test.ncp.gen.erp.fdv.api.ErpApi;
import de.gematik.test.ncp.gen.erp.fdv.model.Consent;
import de.gematik.test.ncp.gen.erp.fdv.model.EUAccessAuthorization;
import de.gematik.test.ncp.gen.erp.fdv.model.ErpTestdriverApiV1PrescriptionIdPatchRequest;
import de.gematik.test.ncp.gen.erp.fdv.model.LoginSuccess;
import de.gematik.test.ncp.gen.erp.fdv.model.Prescription;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ErpFdvClient implements ErpFdvService {

  private final ErpApi erpApi;

  /**
   * @throws IllegalStateException if the start request returns null or has an unsuccessful status
   */
  @Override
  public void start() {
    log.info("Starting Erp FdV...");
    responseToVoid(erpApi::erpTestdriverApiV1StartPut);
  }

  /**
   * @throws IllegalStateException if the stop request returns null or has an unsuccessful status
   */
  @Override
  public void stop() {
    log.info("Stopping Erp FdV...");
    responseToVoid(erpApi::erpTestdriverApiV1StopPut);
  }

  @Override
  public Optional<LoginSuccess> login(final String kvnr) {
    return responseToOptional(
        () -> erpApi.erpTestdriverApiV1LoginPut(kvnr), new GenericType<>() {});
  }

  @Override
  public Optional<Consent> createEuDispensationConsent() {
    return responseToOptional(
        () -> erpApi.erpTestdriverApiV1ConsentPost(EUDISPCONS), new GenericType<>() {});
  }

  @Override
  public Boolean revokeEuDispensationConsent() {
    return responseToBoolean(() -> erpApi.erpTestdriverApiV1ConsentDelete(EUDISPCONS));
  }

  @Override
  public Optional<Consent> getEuDispensationConsent() {
    return responseToOptional(
        () -> erpApi.erpTestdriverApiV1ConsentGet(EUDISPCONS), new GenericType<>() {});
  }

  @Override
  public Optional<EUAccessAuthorization> grantEuAccessPermission(
      final String country, final String accessCode) {
    return responseToOptional(
        () -> erpApi.erpTestdriverApiV1EuAccessAuthorizationPost(country, accessCode),
        new GenericType<>() {});
  }

  @Override
  public Boolean revokeEuAccessPermission() {
    return responseToBoolean(erpApi::erpTestdriverApiV1EuAccessAuthorizationDelete);
  }

  @Override
  public Optional<EUAccessAuthorization> getEuAccessPermission() {
    return responseToOptional(
        erpApi::erpTestdriverApiV1EuAccessAuthorizationGet, new GenericType<>() {});
  }

  @Override
  public Optional<List<Prescription>> getPrescriptions() {
    return responseToOptional(erpApi::erpTestdriverApiV1PrescriptionGet, new GenericType<>() {});
  }

  @Override
  public Optional<Prescription> getPrescription(final String id) {
    return responseToOptional(
        () -> erpApi.erpTestdriverApiV1PrescriptionIdGet(id), new GenericType<>() {});
  }

  @Override
  public Boolean deletePrescription(final String id) {
    return responseToBoolean(() -> erpApi.erpTestdriverApiV1PrescriptionIdDelete(id));
  }

  @Override
  public Optional<Prescription> setEuRedeemable(final String id, final boolean euRedeemable) {
    final var request =
        ErpTestdriverApiV1PrescriptionIdPatchRequest.builder()
            .euRedeemableByPatient(euRedeemable)
            .build();
    return responseToOptional(
        () -> erpApi.erpTestdriverApiV1PrescriptionIdPatch(id, request), new GenericType<>() {});
  }

  @Override
  public List<BaseDispenseInformation> getMedicationDispenseInformation() {
    // prefix controls matching behavior, see https://www.hl7.org/fhir/search.html#date
    String dateSearch = "eq" + LocalDate.now();
    try (var response = erpApi.erpTestdriverApiV1MedicationdispenseGet(dateSearch)) {
      boolean hasErrors = checkAndLogErrors(response);
      if (!hasErrors) {
        return response.readEntity(new GenericType<>() {});
      }
    } catch (final ProcessingException pe) {
      log.error(pe.getMessage(), pe);
    }
    return List.of();
  }

  private <T> Optional<T> responseToOptional(
      final Supplier<Response> apiCall, final GenericType<T> expectedResponse) {
    try (final var response = apiCall.get()) {
      boolean hasErrors = checkAndLogErrors(response);
      if (!hasErrors) {
        return Optional.of(response.readEntity(expectedResponse));
      }
    } catch (final ProcessingException pe) {
      log.error(pe.getMessage(), pe);
    }
    return Optional.empty();
  }

  private Boolean responseToBoolean(final Supplier<Response> apiCall) {
    try (final var response = apiCall.get()) {
      boolean hasErrors = checkAndLogErrors(response);
      return !hasErrors;
    }
  }

  private void responseToVoid(final Supplier<Response> apiCall) {
    try (final var response = apiCall.get()) {
      if (response == null) throw new IllegalStateException("Response was null");
      if (!SUCCESSFUL.equals(response.getStatusInfo().getFamily()))
        throw new IllegalStateException(
            "Unexpected response status: %d - %s"
                .formatted(
                    response.getStatusInfo().getStatusCode(),
                    response.getStatusInfo().getReasonPhrase()));
      log.info("Success!");
    }
  }

  private boolean checkAndLogErrors(final Response response) {
    if (response == null) {
      log.error("Response was null");
      return true;
    } else if (!SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
      log.error(
          "Request failed with status: {} - {}",
          response.getStatusInfo().getStatusCode(),
          response.getStatusInfo().getReasonPhrase());
      return true;
    }
    return false;
  }
}
