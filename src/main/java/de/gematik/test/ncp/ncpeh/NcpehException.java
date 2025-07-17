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

package de.gematik.test.ncp.ncpeh;

import de.gematik.ncpeh.api.response.ErrorInformation;
import jakarta.ws.rs.core.Response;
import java.io.Serial;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * Exception type to be thrown by the {@link NcpehService} operations. Its purpose is to indicate,
 * that something went wrong with either the processing of the client data submitted or in the
 * communication between NCPeH Simulation and NCPeH FD.
 */
@Slf4j
@Getter
public class NcpehException extends RuntimeException {

  @Serial private static final long serialVersionUID = 1167757667761509869L;
  private final Integer responseStatusCode;

  private final String additionalErrorInfo;

  public NcpehException(final String message) {
    super(message);
    responseStatusCode = null;
    additionalErrorInfo = null;
  }

  public NcpehException(final String message, final Throwable cause) {
    super(message, cause);
    responseStatusCode = null;
    additionalErrorInfo = null;
  }

  public NcpehException(final String message, @NonNull final Response response) {
    super(message);
    responseStatusCode = response.getStatus();
    additionalErrorInfo = retrieveAdditionalErrorInfo(response);
  }

  private String retrieveAdditionalErrorInfo(final Response response) {
    try {
      return Optional.ofNullable(response)
          .map(resp -> resp.readEntity(ErrorInformation.class))
          .map(ErrorInformation::errorMessage)
          .orElse(null);
    } catch (final Exception e) {
      log.warn("Failed to retrieve additional error information from response", e);
      return null;
    }
  }

  @Override
  public String getMessage() {
    return super.getMessage()
        + Optional.ofNullable(getResponseStatusCode())
            .map(statusCode -> "; HTTP status: " + HttpStatus.valueOf(statusCode))
            .orElse("")
        + Optional.ofNullable(getAdditionalErrorInfo()).map(errInf -> "; " + errInf).orElse("");
  }
}
