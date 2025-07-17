/*
 * Copyright 2024-2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.screenplay.abilities;

import de.gematik.test.ncp.data.PatientAccessData;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.serenitybdd.screenplay.Ability;

/** Ability to provide patient access data used to call NCPeH services */
@Data
@ToString
@NoArgsConstructor
public class ProvidePatientAccessData implements PatientAccessData, Ability {

  private String kvnr;
  private String accessCode;
}
