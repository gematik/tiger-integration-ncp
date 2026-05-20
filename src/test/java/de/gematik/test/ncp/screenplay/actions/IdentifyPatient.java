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

package de.gematik.test.ncp.screenplay.actions;

import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.test.ncp.data.Patient;
import de.gematik.test.ncp.data.Testdata;
import de.gematik.test.ncp.glue.psa.UCHeaders;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.ncpeh.client.dataobject.IdentifyPatientResponseDTO;
import de.gematik.test.ncp.ncpeh.data.IdentifyPatientRequestBuilder;
import de.gematik.test.ncp.ncpeh.data.TestdataFactory;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientAccessData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePatientData;
import de.gematik.test.ncp.screenplay.abilities.ProvidePractitionerData;
import de.gematik.test.ncp.screenplay.abilities.UseNcpeh;
import de.gematik.test.ncp.screenplay.questions.practitioner.CurrentPatient;
import java.util.function.Consumer;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;

/**
 * Action to perform an IdentifyPatient operation via an XCPD request.
 *
 * @see <a
 *     href="https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#5.2.1">gemSpec_NCPeH_FD
 *     - 5.2.1 NCPeH.UC_9 - Versicherten im Behandlungsland für ePeD-A identifizieren</a>
 */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Slf4j
public class IdentifyPatient implements Performable {

  private final String accessCodeAssigningAuthority;
  private final UCHeaders ucHeaders;

  private Consumer<IdentifyPatientRequestBuilder> requestCustomizer;

  /**
   * Create an IdentifyPatient action for the PSA scope using the required {@code
   * accessCodeAssigningAuthority} OID and associated UC headers for performance reporting.
   *
   * @see <a
   *     href="https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.1.1">gemSpec_NCPeH_FD
   *     - 6.1.1 Operation Cross_Gateway_Patient_Discovery::RespondingGateway_PRPA_IN201305UV02,
   *     Tabelle 38: TAB_NCPeH_Kriterien_Zuordnung_IHE_XCPD-Anfragen_zu_Anwendungsszenarien</a>
   */
  public static IdentifyPatient forPsa() {
    return new IdentifyPatient(TestdataFactory.OID_AC_EPKA_ASSIGNING_AUTHORITY, UCHeaders.UC1);
  }

  /**
   * Create an IdentifyPatient action for the eP/eD scope using the required {@code
   * accessCodeAssigningAuthority} OID and associated UC headers for performance reporting.
   *
   * @see <a
   *     href="https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.1.1">gemSpec_NCPeH_FD
   *     - 6.1.1 Operation Cross_Gateway_Patient_Discovery::RespondingGateway_PRPA_IN201305UV02,
   *     Tabelle 38: TAB_NCPeH_Kriterien_Zuordnung_IHE_XCPD-Anfragen_zu_Anwendungsszenarien</a>
   */
  public static IdentifyPatient forEpEd() {
    return new IdentifyPatient(TestdataFactory.OID_AC_ERP_ASSIGNING_AUTHORITY, UCHeaders.UC9);
  }

  public IdentifyPatient withModifiedRequest(
      final Consumer<IdentifyPatientRequestBuilder> requestCustomizer) {
    this.requestCustomizer = requestCustomizer;
    return this;
  }

  @Override
  @Step("{0} performs the IdentifyPatient action")
  public <T extends Actor> void performAs(final T actor) {
    var ncpeh = actor.usingAbilityTo(UseNcpeh.class);
    var practitionerData = actor.usingAbilityTo(ProvidePractitionerData.class);
    var patientAccessData = actor.usingAbilityTo(ProvidePatientAccessData.class);
    var patient = actor.asksFor(CurrentPatient.asActor());
    var patientRecord = actor.asksFor(CurrentPatient.patientRecord());
    Patient patientData = patient.usingAbilityTo(ProvidePatientData.class);

    patientRecord.setIdentifyPatientResponse(null);
    patientRecord.setPatient(null);

    try {
      IdentifyPatientResponseDTO response;
      String mockControlHeader = ncpeh.getNcpehMockControlRequestHeaders().get(ucHeaders);

      if (requestCustomizer == null) {
        // Standard path
        response =
            ncpeh.identifyPatient(
                patientAccessData,
                practitionerData.profileName(),
                practitionerData.country(),
                accessCodeAssigningAuthority,
                mockControlHeader,
                patientData);
      } else {
        // Non-standard path — build the request manually and apply the customizer
        var testdataProfile =
            Testdata.instance().getNcpehSimTestdataProfile(practitionerData.profileName());

        var requestBuilder = IdentifyPatientRequestBuilder.newInstance();
        // Set subclass field first (see RequestBaseBuilder Javadoc caveat)
        requestBuilder
            .accessCodeAssigningAuthority(accessCodeAssigningAuthority)
            .kvnr(patientAccessData.getKvnr())
            .accessCode(patientAccessData.getAccessCode())
            .euCountryCode(EuCountryCode.valueOf(practitionerData.country().toUpperCase()))
            .idaAssertionProfileName(testdataProfile.idaProfileName());

        // Apply customizations (role, permissions, hcid, etc.)
        requestCustomizer.accept(requestBuilder);

        response = ncpeh.identifyPatient(requestBuilder.build(), mockControlHeader, patientData);
      }

      log.debug("Retrieved patient identification data: {}", response);

      patientRecord.setIdentifyPatientResponse(response);
      var receivedPatientData =
          DataUtils.readPatientDataFromIdentifyPatientResponse(response.ncpehFdResponseContent());
      patientRecord.setPatient(receivedPatientData);
    } catch (final RuntimeException e) {
      log.error("IdentifyPatient task encountered unexpected exception", e);
    }
  }
}
