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

package de.gematik.test.ncp.reporting;

import static de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils.parseHttpBody;
import static de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils.readAcknowledgementDetailFromIdentifyPatientResponse;

import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.ncpeh.api.common.WrappedHttpMessage;
import de.gematik.ncpeh.api.common.WrappedHttpResponse;
import de.gematik.ncpeh.api.request.FindDocumentsRequest;
import de.gematik.ncpeh.api.request.IdentifyPatientRequest;
import de.gematik.ncpeh.api.request.RetrieveDocumentRequest;
import de.gematik.ncpeh.api.response.SimulatorCommunicationData;
import de.gematik.test.ncp.data.AcknowledgementDetail;
import de.gematik.test.ncp.ncpeh.PatientSummaryLevel;
import de.gematik.test.ncp.ncpeh.client.dataobject.DataUtils;
import de.gematik.test.ncp.reporting.model.NcpehPerformanceMessage;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.hl7.v3.PRPAIN201306UV02;

/** A dynamic proxy handler that logs the execution time of NcpehSimulatorApi methods. */
@AllArgsConstructor
@Slf4j
public class NcpehTimeLoggingHandler implements InvocationHandler {

  private static final String RETRIEVE_DOCUMENT_METHOD_NAME = "retrieveDocument";
  private static final String RETRIEVE_DOCUMENT_PDF_KEY = "retrieveDocumentPdf";

  private static final Map<String, UseCase> OPERATION_MAPPING =
      Map.of(
          "identifyPatient",
          UseCase.NCPEH_UC_1,
          "findDocuments",
          UseCase.NCPEH_UC_2,
          RETRIEVE_DOCUMENT_METHOD_NAME,
          UseCase.NCPEH_UC_3,
          RETRIEVE_DOCUMENT_PDF_KEY,
          UseCase.NCPEH_UC_4);

  private final NcpehSimulatorApi target;

  private final NcpehContext ncpehContext;

  /**
   * Intercepts method calls on the proxy instance and logs the execution time.
   *
   * @param proxy the proxy instance that the method was invoked on
   * @param method the {@code Method} instance corresponding to the interface method invoked on the
   *     proxy instance
   * @param args an array of objects containing the values of the arguments passed in the method
   *     invocation on the proxy instance
   * @return the result of the method invocation
   * @throws Throwable if the method invocation throws an exception
   */
  @Override
  public Object invoke(final Object proxy, final Method method, final Object[] args)
      throws Throwable {
    final long startTime = System.currentTimeMillis();
    Object response = null;
    try {
      response = method.invoke(target, args);
      return response;
    } finally {
      final long endTime = System.currentTimeMillis();
      final long duration = endTime - startTime;
      log.info("Method {} took {} ms", method.getName(), duration);
      writePerformanceFiles(duration, method.getName(), args, response);
    }
  }

  /**
   * Writes performance data to an overall performance file and to a per-scenario log file.
   *
   * @param duration the duration of the method execution
   * @param methodName the name of the method
   * @param args the arguments passed to the method
   * @param response the response returned by the method
   */
  void writePerformanceFiles(
      final long duration, final String methodName, final Object[] args, final Object response) {

    final var data =
        getPerformanceData(System.currentTimeMillis(), duration, methodName, args, response);
    if (data.isEmpty()) {
      log.warn("No performance data found for method {}", methodName);
      return;
    }

    // Create the path object for the target file
    final Path filePath = Paths.get(ncpehContext.getFileName());

    final Path directoryPath = filePath.getParent();

    try {

      // Create the directory if it does not exist
      if (null != directoryPath) {
        Files.createDirectories(directoryPath);
      }

      // Write the content to the file
      Files.write(
          filePath,
          List.of(data.get()),
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);

      log.debug("File written successfully to {}", filePath.toAbsolutePath());

      // write the scenario file if present
      writeScenarioPerformanceLog(directoryPath, data.get());

    } catch (final IOException e) {
      log.error("An error occurred while writing the file: {}", e.getMessage());
    }
  }

  private void writeScenarioPerformanceLog(final Path directoryPath, final String data) {
    ncpehContext
        .getScenarioFileName()
        .ifPresent(
            scenarioFileName -> {
              final Path scenarioFilePath =
                  null != directoryPath
                      ? directoryPath.resolve(scenarioFileName)
                      : Paths.get(scenarioFileName);
              try {
                Files.write(
                    scenarioFilePath,
                    List.of(data),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
                log.debug("File written successfully to {}", scenarioFilePath.toAbsolutePath());
              } catch (final IOException e) {
                log.error("An error occurred while writing the file: {}", e.getMessage());
              }
            });
  }

  /**
   * Retrieves the performance data as a formatted string.
   *
   * @param timestamp the current timestamp
   * @param duration the duration of the method execution
   * @param methodName the name of the method
   * @param args the arguments passed to the method
   * @param response the response returned by the method
   * @return an optional containing the performance data as a formatted string
   */
  Optional<String> getPerformanceData(
      final long timestamp,
      final long duration,
      final String methodName,
      final Object[] args,
      final Object response) {
    final var operationName = getOperationName(methodName, args);
    if (operationName.isEmpty()) {
      log.info("No operation name found for method {}", methodName);
      return Optional.empty();
    }

    return Optional.ofNullable(response)
        .filter(Response.class::isInstance)
        .filter(res -> ((Response) res).bufferEntity())
        .map(
            res ->
                String.format(
                    "%d;%d;%s;%s;%s",
                    timestamp,
                    duration,
                    operationName.get().value(),
                    getStatus(((Response) res)),
                    getMessage(duration, args, ((Response) res))));
  }

  /**
   * Retrieves the status from responseReceived.statusLine of the response.
   *
   * @param response the response returned by the method
   * @return the status as a string, or an empty string if not present
   */
  private String getStatus(final Response response) {
    return Optional.ofNullable(response)
        .map(res -> res.readEntity(SimulatorCommunicationData.class))
        .map(SimulatorCommunicationData::responseReceived)
        .map(WrappedHttpResponse::statusLine)
        .map(NcpehTimeLoggingHandler::parseStatusLine)
        .orElse("");
  }

  /**
   * Parses the status line to extract the status code.
   *
   * @param statusLine the status line to be parsed
   * @return the status code if present, otherwise null
   */
  private static String parseStatusLine(final String statusLine) {
    return Optional.ofNullable(statusLine)
        .map(
            line -> {
              final var parts = line.split(" ");
              return parts.length > 1 ? parts[0] : null;
            })
        .orElse(null);
  }

  /**
   * Retrieves the operation name based on the method name and arguments.
   *
   * @param methodName the name of the method
   * @param args the arguments passed to the method
   * @return an optional containing the operation name
   */
  private Optional<UseCase> getOperationName(final String methodName, final Object[] args) {
    final var key =
        RETRIEVE_DOCUMENT_METHOD_NAME.equals(methodName) && isDocumentOfLevel1(args)
            ? RETRIEVE_DOCUMENT_PDF_KEY
            : methodName;
    return Optional.ofNullable(OPERATION_MAPPING.get(key));
  }

  /**
   * Checks if the document is of level 1.
   *
   * @param args the arguments passed to the method
   * @return {@code true} if the document is of level 1, {@code false} otherwise
   */
  private boolean isDocumentOfLevel1(final Object[] args) {
    if (args.length == 1 && args[0] instanceof final RetrieveDocumentRequest request) {
      return PatientSummaryLevel.LEVEL_1.documentIsOfLevel(request.documentUniqueId());
    }
    return false;
  }

  /**
   * Retrieves the performance message as a JSON string.
   *
   * @param duration the duration of the method execution
   * @param args the arguments passed to the method
   * @param response the response returned by the method
   * @return the performance message as a JSON string
   */
  @SneakyThrows
  private String getMessage(final long duration, final Object[] args, final Response response) {
    return TigerGlobalConfiguration.getObjectMapper()
        .writeValueAsString(
            new NcpehPerformanceMessage(getCountryCode(args), getErrorCode(response), duration));
  }

  /**
   * Retrieves the country code from the request.
   *
   * @param args the arguments passed to the method
   * @return the country code
   */
  private String getCountryCode(final Object[] args) {
    if (args.length == 1 && args[0] instanceof final IdentifyPatientRequest request) {
      return request.baseParameter().euCountryCode().getCountryCode();
    }
    if (args.length == 1 && args[0] instanceof final FindDocumentsRequest request) {
      return request.baseParameter().euCountryCode().getCountryCode();
    }
    if (args.length == 1 && args[0] instanceof final RetrieveDocumentRequest request) {
      return request.baseParameter().euCountryCode().getCountryCode();
    }
    return null;
  }

  /**
   * Retrieves the error code from the response.
   *
   * @param response the response returned by the method
   * @return the error code
   */
  private String getErrorCode(final Response response) {
    return Optional.ofNullable(response)
        .map(res -> res.readEntity(SimulatorCommunicationData.class))
        .map(SimulatorCommunicationData::responseReceived)
        .map(WrappedHttpResponse::messageContent)
        .map(
            message -> {
              try {
                return Optional.ofNullable(
                        readAcknowledgementDetailFromIdentifyPatientResponse(
                            parseHttpBody(message, PRPAIN201306UV02.class)))
                    .map(AcknowledgementDetail::code)
                    .orElseGet(
                        () ->
                            DataUtils.readRegistryErrorCodesFromAdhocQueryResponse(
                                    parseHttpBody(message, AdhocQueryResponse.class))
                                .stream()
                                .findFirst()
                                .orElseGet(
                                    () ->
                                        this
                                            .readRegistryErrorCodesFromRetrieveDocumentSetResponseType(
                                                message)));
              } catch (final Exception e) {
                // nothing to do
              }

              return null;
            })
        .orElse(null);
  }

  /**
   * Reads the registry error codes from the RetrieveDocumentSetResponseType.
   *
   * @param message the wrapped HTTP message
   * @return the error code
   */
  private String readRegistryErrorCodesFromRetrieveDocumentSetResponseType(
      final WrappedHttpMessage message) {
    return Optional.ofNullable(parseHttpBody(message, RetrieveDocumentSetResponseType.class))
        .map(RetrieveDocumentSetResponseType::getRegistryResponse)
        .map(DataUtils::readRegistryErrorCodesFromRegistryResponse)
        .orElseGet(List::of)
        .stream()
        .findFirst()
        .orElse(null);
  }
}
