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

package de.gematik.test.ncp.ncpeh;

import static de.gematik.test.ncp.util.Utils.*;

import de.gematik.ncpeh.api.response.ErrorInformation;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

/**
 * Exception type to be thrown by the {@link NcpehInterface} operations. Its purpose is to indicate,
 * that something went wrong with either the processing of the client data submitted or in the
 * communication between NCPeH Simulation and NCPeH FD.
 */
public class NcpehException extends RuntimeException {

  @Getter private final Integer responseStatusCode;

  @Getter private final String additionalErrorInfo;

  public NcpehException(String message) {
    super(message);
    responseStatusCode = null;
    additionalErrorInfo = null;
  }

  public NcpehException(String message, Throwable cause) {
    super(message, cause);
    responseStatusCode = null;
    additionalErrorInfo = null;
  }

  public NcpehException(String message, @NonNull Response response) {
    super(message);
    responseStatusCode = response.getStatus();
    additionalErrorInfo = retrieveAdditionalErrorInfo(response);
  }

  private String retrieveAdditionalErrorInfo(Response response) {
    return Optional.ofNullable(response)
        .map(swallowExceptionFunction(resp -> resp.readEntity(ErrorInformation.class)))
        .map(ErrorInformation::errorMessage)
        .orElse(null);
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
