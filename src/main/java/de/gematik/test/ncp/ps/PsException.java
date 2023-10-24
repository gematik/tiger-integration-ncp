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

import java.util.Objects;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;

/**
 * Exception type to be thrown by the {@link PsInterface} operations. It's purpose is to indicate,
 * that something went wrong with either the processing of the data submitted to the PS or in the
 * communication between PS and Konnektor.
 */
public class PsException extends RuntimeException {

  @Getter private final @Nullable String operation;

  public PsException(@NonNull String statusMessage) {
    super(statusMessage);
    this.operation = null;
  }

  public PsException(String statusMessage, @Nullable String operation) {
    super(statusMessage);
    this.operation = operation;
  }

  @Override
  public String getMessage() {
    return Objects.isNull(operation)
        ? super.getMessage()
        : String.format("Operation %s, Message: %s", operation, super.getMessage());
  }
}
