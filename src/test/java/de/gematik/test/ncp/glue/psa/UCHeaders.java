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

package de.gematik.test.ncp.glue.psa;

/**
 * Enumeration of UC headers corresponding to individual use cases defined in gemSpec_NCPeH_FD.
 *
 * @see <a
 *     href="https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#5">gemSpec_NCPeH_FD -
 *     5 Fachliche Anwendungsfälle</a>
 */
public enum UCHeaders {
  /** Identify insured person in the country of treatment for PS-A */
  UC1,
  /** List metadata about ePKA MIO */
  UC2,
  /** Retrieve ePKA MIO of the insured person (as PDF/A) */
  UC34,
  /** Identify insured person in the country of treatment for ePeD-A */
  UC9,
  /** List redeemable e-prescriptions of the insured person for ePeD-A */
  UC10,
  /** Retrieve e-prescriptions for ePeD-A */
  UC11,
  /** Dispense e-prescriptions for ePeD-A */
  UC12
}
