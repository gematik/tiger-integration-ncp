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

package de.gematik.test.ncp.ps;

import java.net.URL;
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
public final class PsMockImpl implements PsInterface {

  private static PsMockImpl instance;

  public static PsInterface instance() {
    if (Objects.isNull(instance)) {
      instance = new PsMockImpl();
    }

    return instance;
  }

  private boolean epkaPresent = false;

  private boolean isLeAuthorized = false;

  @Override
  public boolean psIsUpAndRunning() {
    return true;
  }

  @Override
  public boolean isLeAuthorizedForKvnr(String kvnr) {
    return isLeAuthorized;
  }

  @Override
  public void authorizeLeForKvnr(String kvnr) {
    isLeAuthorized = true;
  }

  @Override
  public String putDocument(String kvnr, byte[] epka) {
    epkaPresent = true;
    return "mockTitle";
  }

  @Override
  public boolean documentCanBeFound(String kvnr, String documentTitle) {
    return epkaPresent;
  }

  @Override
  public boolean aktenkontoIsPresent(String kvnr) {
    return Boolean.TRUE;
  }

  @Override
  public byte[] signDocument(byte[] document) {
    return new String(document, StandardCharsets.UTF_8).getBytes(StandardCharsets.ISO_8859_1);
  }

  @Override
  public byte[] signDocument(byte[] document, boolean asQES, String signatureAlgorithm) {
    return signDocument(document);
  }

  @Override
  public List<String> findEpka(String kvnr) {
    return epkaPresent ? List.of(UUID.randomUUID().toString()) : List.of();
  }

  @Override
  public void deleteExistingDocuments(String kvnr, List<String> documentEntryUUIDs) {
    epkaPresent = false;
  }

  @Override
  public void configureKonnektor(URL konnektorAddress) {}
}
