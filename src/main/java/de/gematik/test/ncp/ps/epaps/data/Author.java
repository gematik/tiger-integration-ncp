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

package de.gematik.test.ncp.ps.epaps.data;

import de.gematik.epa.ihe.model.simple.AuthorInstitution;
import java.util.List;
import lombok.Builder;
import lombok.Singular;

@Builder
public class Author {

  private String identifier;

  private String familyName;

  private String givenName;

  private String otherName;

  private String nameAffix;

  private String title;

  @Singular private List<AuthorInstitution> authorInstitutions;

  @Singular private List<String> authorRoles;

  @Singular private List<String> authorSpecialties;

  @Singular private List<String> authorTelecommunications;

  public de.gematik.epa.ihe.model.Author toPsAuthor() {
    return new de.gematik.epa.ihe.model.Author(
        identifier,
        familyName,
        givenName,
        otherName,
        nameAffix,
        title,
        authorInstitutions,
        authorRoles,
        authorSpecialties,
        authorTelecommunications);
  }
}
