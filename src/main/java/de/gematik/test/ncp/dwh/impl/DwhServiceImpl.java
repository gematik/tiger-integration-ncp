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

package de.gematik.test.ncp.dwh.impl;

import static de.gematik.test.ncp.reporting.Environment.PU;
import static net.serenitybdd.rest.SerenityRest.given;

import com.fasterxml.jackson.core.type.TypeReference;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.dwh.DwhService;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceMessage;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReport;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportDWHResponseEntry;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceReportEntry;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import io.restassured.http.ContentType;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class DwhServiceImpl implements DwhService {

  private static final String DWH_CONFIG_KEY =
      ExternalServerConfig.EXTERNAL_SERVER_CONFIG_TIGER_KEY + ".dwh";

  private static final String DWH_LOGIN_URL_KEY = DWH_CONFIG_KEY + ".loginUrl";

  private static final String DWH_LOGIN_SCOPE_KEY = DWH_CONFIG_KEY + ".loginScope";

  private static final String DWH_LOGIN_GRANT_TYPE_KEY = DWH_CONFIG_KEY + ".loginGrantType";

  public static final String DWH_URL_ACTIVE_KEY = "tiger.servers.dwhUrl.active";

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneId.of("UTC"));

  private String token;

  @Getter(lazy = true)
  private final ExternalServerConfig config =
      Utils.loadConfig(ExternalServerConfig.class, DWH_CONFIG_KEY);

  @Getter(lazy = true)
  private final String baseUrl =
      UriComponentsBuilder.newInstance()
          .scheme(getConfig().getScheme())
          .host(getConfig().getHostname())
          .path(getConfig().getBasePath())
          .build()
          .toUriString();

  @Getter(lazy = true)
  private final String loginUrl = TigerGlobalConfiguration.readString(DWH_LOGIN_URL_KEY);

  @Getter(lazy = true)
  private final String loginScope = TigerGlobalConfiguration.readString(DWH_LOGIN_SCOPE_KEY);

  @Getter(lazy = true)
  private final String loginGrantType =
      TigerGlobalConfiguration.readString(DWH_LOGIN_GRANT_TYPE_KEY);

  static final String DWH_MICROSOFT_CLIENT_ID_VARIABLE_NAME = "DWH_MICROSOFT_CLIENT_ID";

  static final String DWH_MICROSOFT_CLIENT_SECRET_VARIABLE_NAME = "DWH_MICROSOFT_CLIENT_SECRET";

  @Getter(lazy = true)
  private final String clientId = System.getenv(DWH_MICROSOFT_CLIENT_ID_VARIABLE_NAME);

  @Getter(lazy = true)
  private final String clientSecret = System.getenv(DWH_MICROSOFT_CLIENT_SECRET_VARIABLE_NAME);

  private static DwhService instance;

  public static DwhService getInstance() {
    return Optional.ofNullable(instance)
        .orElseGet(
            () -> {
              instance = new DwhServiceImpl();
              return instance;
            });
  }

  @SneakyThrows
  @Override
  public NcpehPerformanceReport getPerformanceReport(final Long start, final Long end) {
    final var response =
        given()
            .baseUri(getBaseUrl())
            .basePath("/NCPeH")
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .header("Authorization", String.format("Bearer %s", getToken()))
            .queryParams(
                "from",
                formatter.format(Instant.ofEpochMilli(start)),
                "until",
                formatter.format(Instant.ofEpochMilli(end)),
                "bu",
                PU)
            .get()
            .getBody()
            .asString();
    log.debug("DWH Performance report: {}", response);

    return TigerGlobalConfiguration.getObjectMapper()
        .readValue(response, new TypeReference<List<NcpehPerformanceReportDWHResponseEntry>>() {})
        .stream()
        .map(
            entry ->
                new NcpehPerformanceReportEntry()
                    .timestamp(entry.getTimestamp().toEpochMilli())
                    .duration(entry.getDuration())
                    .operation(entry.getUsecase())
                    .status(entry.getStatuscode())
                    .message(
                        new NcpehPerformanceMessage(
                            entry.getReqc(), entry.getErr(), entry.getBkdur())))
        .collect(
            Collector.of(
                NcpehPerformanceReport::new,
                NcpehPerformanceReport::addEntry,
                (left, right) -> {
                  left.getEntries().addAll(right.getEntries());
                  return left;
                }));
  }

  private String getToken() {
    return TigerGlobalConfiguration.readBoolean(DWH_URL_ACTIVE_KEY)
        ? Optional.ofNullable(token)
            .orElseGet(
                () -> {
                  token = fetchToken();
                  return token;
                })
        : "";
  }

  @SneakyThrows
  String fetchToken() {
    final var response =
        given()
            .baseUri(getLoginUrl())
            .accept(ContentType.JSON)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .formParam("grant_type", getLoginGrantType())
            .formParam(
                "client_id",
                Optional.ofNullable(getClientId())
                    .orElseThrow(() -> new IllegalArgumentException("ClientID is not set")))
            .formParam(
                "client_secret",
                Optional.ofNullable(getClientSecret())
                    .orElseThrow(() -> new IllegalArgumentException("ClientSecret is not set")))
            .formParam("scope", getLoginScope())
            .post()
            .getBody()
            .asString();
    log.debug("DWH Token response: {}", response);
    return TigerGlobalConfiguration.getObjectMapper()
        .readTree(response)
        .get("access_token")
        .asText();
  }
}
