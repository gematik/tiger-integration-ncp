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

package de.gematik.test.ncp.util;

import static de.gematik.test.ncp.util.Utils.supplyOrThrowSneaky;

import de.gematik.test.ncp.data.PersonName;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.xml.xpath.XPathExpression;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

/**
 * Class to take an EPKA template, change particular values using XPaths and then return the
 * modified EPKA.
 */
@Accessors(fluent = true)
public class EpkaProcessor extends XmlProcessor<EpkaProcessor> {

  public static final String KVNR_XPATH_IN_EPKA = "//Patient/identifier/value/@value";

  public static final String BIRTHDATE_XPATH_IN_EPKA = "//Patient/birthDate/@value";

  public static final String FULL_NAME_XPATH_IN_EPKA = "//Patient/name/text/@value";

  public static final String LAST_NAME_XPATH_IN_EPKA = "//Patient/name/family/@value";

  public static final String GIVEN_NAME_XPATH_IN_EPKA = "//Patient/name/given/@value";

  public static final String NAME_PREFIX_XPATH_IN_EPKA = "//Patient/name/prefix/@value";

  @Getter(lazy = true)
  private static final XPathExpression kvnrXpath =
      supplyOrThrowSneaky(() -> xpathProcessor().compile(KVNR_XPATH_IN_EPKA));

  @Getter(lazy = true)
  private static final XPathExpression birthDatePath =
      supplyOrThrowSneaky(() -> xpathProcessor().compile(BIRTHDATE_XPATH_IN_EPKA));

  @Getter(lazy = true)
  private static final XPathExpression fullNamePath =
      supplyOrThrowSneaky(() -> xpathProcessor().compile(FULL_NAME_XPATH_IN_EPKA));

  @Getter(lazy = true)
  private static final XPathExpression lastNamePath =
      supplyOrThrowSneaky(() -> xpathProcessor().compile(LAST_NAME_XPATH_IN_EPKA));

  @Getter(lazy = true)
  private static final XPathExpression givenNamePath =
      supplyOrThrowSneaky(() -> xpathProcessor().compile(GIVEN_NAME_XPATH_IN_EPKA));

  @Getter(lazy = true)
  private static final XPathExpression namePrefixPath =
      supplyOrThrowSneaky(() -> xpathProcessor().compile(NAME_PREFIX_XPATH_IN_EPKA));

  public EpkaProcessor(@NonNull URL epkaFileUrl) {
    super(epkaFileUrl);
  }

  @Override
  protected EpkaProcessor that() {
    return this;
  }

  @SneakyThrows
  public EpkaProcessor updateKvnr(@NonNull String kvnr) {
    return updateNode(kvnrXpath(), kvnr);
  }

  @SneakyThrows
  public EpkaProcessor updateBirthDate(@NonNull LocalDate birthDate) {
    return updateNode(birthDatePath(), birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
  }

  @SneakyThrows
  public EpkaProcessor updateName(@NonNull PersonName name) {
    updateNode(fullNamePath(), name.toString());

    updateNode(namePrefixPath(), Objects.requireNonNullElse(name.titles(), ""));

    updateNode(lastNamePath(), name.lastNames());

    return updateNode(givenNamePath(), name.givenNames());
  }
}
