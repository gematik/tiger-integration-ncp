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

package de.gematik.test.ncp.ps.epaps;

import de.gematik.test.ncp.data.Practice;
import de.gematik.test.ncp.data.SmbCard;
import de.gematik.test.ncp.ps.PrimarySystemService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Class, who's only purpose is to mock the PS interface, in case no proper implementation shall be
 * used, like for example while running the tests on the Jenkins.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PrimarySystemServiceMockImpl implements PrimarySystemService {

  private static PrimarySystemServiceMockImpl instance;

  public static PrimarySystemService instance() {
    if (Objects.isNull(instance)) {
      instance = new PrimarySystemServiceMockImpl();
    }

    return instance;
  }

  private boolean epkaPresent = false;

  @Override
  public void authorizeLeForKvnr(final String telematikId, final String kvnr) {
    // Do nothing because it's a mock
  }

  @Override
  public String putDocument(final String kvnr, final Practice practice, final byte[] epka) {
    epkaPresent = true;
    return "mockTitle";
  }

  @Override
  public String replaceDocument(final String kvnr, final Practice practice, final byte[] epka) {
    return "mockTitle";
  }

  @Override
  public boolean aktenkontoIsPresent(final String kvnr) {
    return Boolean.TRUE;
  }

  @Override
  public byte[] signDocument(final byte[] document) {
    return new String(document, StandardCharsets.UTF_8).getBytes(StandardCharsets.ISO_8859_1);
  }

  @Override
  public byte[] signDocument(
      final byte[] document, final boolean asQES, final String signatureAlgorithm) {
    return signDocument(document);
  }

  @Override
  public List<String> findEpka(final String kvnr) {
    return epkaPresent ? List.of(UUID.randomUUID().toString()) : List.of();
  }

  @Override
  public List<String> findAllDocuments(final String kvnr) {
    return List.of();
  }

  @Override
  public void deleteExistingDocuments(final String kvnr, final List<String> documentEntryUUIDs) {
    epkaPresent = false;
  }

  @Override
  public SmbCard getSmbCard() {
    return new SmbCard("Praxis Münchhausen-BohlenTEST-ONLY", "1-883110000163990");
  }
}
