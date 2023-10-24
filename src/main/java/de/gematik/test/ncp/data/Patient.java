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

package de.gematik.test.ncp.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDate;
import java.util.Objects;

/** Interface for objects holding the primary identification properties of patients */
@JsonDeserialize(as = PatientImpl.class)
public interface Patient {

  PersonName name();

  String kvnr();

  LocalDate birthDate();

  String accessCode();

  default boolean samePerson(Patient o) {
    return this.equals(o)
        || (Objects.nonNull(o)
            && name().equals(o.name())
            && kvnr().equals(o.kvnr())
            && birthDate().equals(o.birthDate()));
  }

  default String patientData() {
    return new StringBuilder()
        .append("Name: ")
        .append(Objects.requireNonNullElse(name(), ""))
        .append("; KVNR: ")
        .append(Objects.requireNonNullElse(kvnr(), ""))
        .append("; Birth Date: ")
        .append(Objects.requireNonNullElse(birthDate(), ""))
        .toString();
  }
}
