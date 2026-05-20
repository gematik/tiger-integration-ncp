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

package de.gematik.test.ncp.gen.erp.fdv.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.*;
import java.util.Objects;

@JsonTypeName("Communication_sender")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class CommunicationSender {

  protected CommunicationSender(CommunicationSenderBuilder<?, ?> b) {}

  public CommunicationSender() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommunicationSender {\n");

    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public static CommunicationSenderBuilder<?, ?> builder() {
    return new CommunicationSenderBuilderImpl();
  }

  private static class CommunicationSenderBuilderImpl
      extends CommunicationSenderBuilder<CommunicationSender, CommunicationSenderBuilderImpl> {

    @Override
    protected CommunicationSenderBuilderImpl self() {
      return this;
    }

    @Override
    public CommunicationSender build() {
      return new CommunicationSender(this);
    }
  }

  public abstract static class CommunicationSenderBuilder<
      C extends CommunicationSender, B extends CommunicationSenderBuilder<C, B>> {
    protected abstract B self();

    public abstract C build();
  }
}
