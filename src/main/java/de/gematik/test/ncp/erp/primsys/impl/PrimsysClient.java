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

package de.gematik.test.ncp.erp.primsys.impl;

import static de.gematik.test.ncp.erp.primsys.PrimsysClientProvider.PRIMSYS_APIKEY_HEADER_NAME;
import static net.serenitybdd.rest.SerenityRest.given;

import de.gematik.test.erezept.primsys.PrimSysClientFactory;
import de.gematik.test.erezept.primsys.PrimSysDoctor;
import de.gematik.test.erezept.primsys.data.PrescribeRequestDto;
import de.gematik.test.erezept.primsys.data.PrescriptionDto;
import de.gematik.test.erezept.primsys.data.actors.ActorDto;
import de.gematik.test.erezept.primsys.data.actors.ActorType;
import de.gematik.test.erezept.primsys.rest.BasicRequests;
import de.gematik.test.erezept.primsys.rest.DoctorRequests;
import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.erp.primsys.PrimsysRestService;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Client for interacting with the PrimSys REST API extending the basic Kotlin client with
 * additional functionality.
 */
@Getter
@Slf4j
public class PrimsysClient implements PrimsysRestService {

  public static final List<Integer> ACCEPTED_STATUS_CODES_ON_ABORT =
      List.of(HttpStatus.NO_CONTENT.value(), HttpStatus.GONE.value());

  private static final String BASE_PATH_DOCTOR = "/doc";
  private static final String PATH_PARAM_ID = "id";
  private static final String QUERY_PARAM_TASK_ID = "taskId";
  private static final String QUERY_PARAM_ACCESS_CODE = "ac";
  private static final String PATH_ABORT = "/abort";

  private final PrimSysClientFactory clientFactory;
  private final ExternalServerConfig config;
  private final int port;
  private final String apikey;

  private final RequestSpecification requestSpecification;
  private PrimSysDoctor doctorClient;

  public PrimsysClient(
      final PrimSysClientFactory clientFactory,
      final ExternalServerConfig config,
      final int port,
      final String apikey) {
    this.clientFactory = clientFactory;
    this.config = config;
    this.port = port;
    this.apikey = apikey;

    // specify default parser in case the server does not return a content type
    RestAssured.defaultParser = Parser.JSON;

    requestSpecification =
        new RequestSpecBuilder()
            .setBaseUri(
                UriBuilder.newInstance()
                    .scheme(config.getScheme())
                    .host(config.getHostname())
                    .build())
            .setBasePath(config.getBasePath() == null ? "" : config.getBasePath())
            .addHeader(PRIMSYS_APIKEY_HEADER_NAME, apikey)
            .setAccept(MediaType.APPLICATION_JSON)
            .build();
  }

  @Override
  public List<ActorDto> getActors(final ActorType role, final String name) {
    Predicate<ActorDto> byRoleAndName = actor -> true;
    if (role != null) {
      byRoleAndName = byRoleAndName.and(actor -> role.equals(actor.getType()));
    }
    if (name != null) {
      byRoleAndName = byRoleAndName.and(actor -> name.equalsIgnoreCase(actor.getName()));
    }

    return clientFactory.getActorsInfo().stream().filter(byRoleAndName).toList();
  }

  @Override
  public PrescriptionDto issuePrescription(
      final String doctorId, final Boolean direct, final PrescribeRequestDto prescribeRequestDto) {
    doctorClient = clientFactory.getDoctorClient(doctorId);
    return doctorClient
        .performBlocking(DoctorRequests.prescribe(prescribeRequestDto, direct))
        .asExpectedPayload();
  }

  @SneakyThrows
  @Override
  public void abortPrescriptionAsDoc(final String doctorId, final String taskId, final String ac) {
    final var response =
        given()
            .spec(requestSpecification)
            .basePath(String.join("/", BASE_PATH_DOCTOR, "{" + PATH_PARAM_ID + "}"))
            .pathParam(PATH_PARAM_ID, doctorId)
            .queryParam(QUERY_PARAM_TASK_ID, taskId)
            .queryParam(QUERY_PARAM_ACCESS_CODE, ac)
            .delete(PATH_ABORT);

    if (!ACCEPTED_STATUS_CODES_ON_ABORT.contains(response.getStatusCode())) {
      throw new IOException("HTTP error: " + response.statusLine());
    }
  }

  @Override
  public List<PrescriptionDto> getPrescribed(final String kvnr) {
    final var response =
        clientFactory
            .performBlocking(BasicRequests.getReadyPrescriptions(kvnr))
            .asExpectedPayload();
    return new ArrayList<>(response);
  }
}
