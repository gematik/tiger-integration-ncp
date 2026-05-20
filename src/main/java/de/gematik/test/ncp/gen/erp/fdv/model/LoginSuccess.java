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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@JsonTypeName("LoginSuccess")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class LoginSuccess {
  private String accessToken;

  protected LoginSuccess(LoginSuccessBuilder<?, ?> b) {
    this.accessToken = b.accessToken;
  }

  public LoginSuccess() {}

  /** IDP Token */
  public LoginSuccess accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  @ApiModelProperty(required = true, value = "IDP Token")
  @JsonProperty("accessToken")
  public String getAccessToken() {
    return accessToken;
  }

  @JsonProperty("accessToken")
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginSuccess loginSuccess = (LoginSuccess) o;
    return Objects.equals(this.accessToken, loginSuccess.accessToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginSuccess {\n");

    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
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

  public static LoginSuccessBuilder<?, ?> builder() {
    return new LoginSuccessBuilderImpl();
  }

  private static class LoginSuccessBuilderImpl
      extends LoginSuccessBuilder<LoginSuccess, LoginSuccessBuilderImpl> {

    @Override
    protected LoginSuccessBuilderImpl self() {
      return this;
    }

    @Override
    public LoginSuccess build() {
      return new LoginSuccess(this);
    }
  }

  public abstract static class LoginSuccessBuilder<
      C extends LoginSuccess, B extends LoginSuccessBuilder<C, B>> {
    private String accessToken;

    protected abstract B self();

    public abstract C build();

    public B accessToken(String accessToken) {
      this.accessToken = accessToken;
      return self();
    }
  }
}
