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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.Delegate;

@Getter
@EqualsAndHashCode
@Accessors(fluent = true)
public class PersonName implements CharSequence {

  @JsonProperty private String givenNames;

  @JsonProperty private String lastNames;

  @JsonProperty private String titles;

  @Delegate(types = CharSequence.class)
  @Exclude
  private String toString;

  public PersonName titles(String titles) {
    this.titles = titles;
    return updateToString();
  }

  public PersonName givenNames(String givenNames) {
    this.givenNames = givenNames;
    return updateToString();
  }

  public PersonName lastNames(String lastNames) {
    this.lastNames = lastNames;
    return updateToString();
  }

  @Override
  public String toString() {
    return Optional.ofNullable(toString).orElse(updateToString().toString);
  }

  private PersonName updateToString() {
    var builder = new StringBuilder();
    Optional.ofNullable(titles).ifPresent(t -> builder.append(t).append(" "));
    Optional.ofNullable(givenNames).ifPresent(gn -> builder.append(gn).append(" "));
    Optional.ofNullable(lastNames).ifPresent(builder::append);
    toString = builder.toString().trim();
    return this;
  }

  public static PersonName fromString(String name) {
    var nameParts = Testdata.instance().getNameParts(name);
    return new PersonName()
        .titles(nameParts.getLeft())
        .givenNames(nameParts.getMiddle())
        .lastNames(nameParts.getRight());
  }
}
