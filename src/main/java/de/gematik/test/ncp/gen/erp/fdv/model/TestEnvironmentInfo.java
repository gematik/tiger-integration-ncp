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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Information about the test environment that is used */
@ApiModel(description = "Information about the test environment that is used")
@JsonTypeName("TestEnvironmentInfo")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class TestEnvironmentInfo {
  public enum NameEnum {
    TU(String.valueOf("TU")),
    RU(String.valueOf("RU")),
    RU_DEV(String.valueOf("RU-DEV"));

    private String value;

    NameEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    /**
     * Convert a String into String, as specified in the <a
     * href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS
     * 2.0 Specification, section 3.2, p. 12</a>
     */
    public static NameEnum fromString(String s) {
      for (NameEnum b : NameEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static NameEnum fromValue(String value) {
      for (NameEnum b : NameEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private NameEnum name;

  public enum ServiceUrlEnum {
    TEST_APP_TI_DIENSTE_DE(String.valueOf("https://erp-test.app.ti-dienste.de")),
    REF_APP_TI_DIENSTE_DE(String.valueOf("https://erp-ref.app.ti-dienste.de")),
    DEV_APP_TI_DIENSTE_DE(String.valueOf("https://erp-dev.app.ti-dienste.de"));

    private String value;

    ServiceUrlEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    /**
     * Convert a String into String, as specified in the <a
     * href="https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-eval-spec/index.html">See JAX RS
     * 2.0 Specification, section 3.2, p. 12</a>
     */
    public static ServiceUrlEnum fromString(String s) {
      for (ServiceUrlEnum b : ServiceUrlEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static ServiceUrlEnum fromValue(String value) {
      for (ServiceUrlEnum b : ServiceUrlEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ServiceUrlEnum serviceUrl;

  protected TestEnvironmentInfo(TestEnvironmentInfoBuilder<?, ?> b) {
    this.name = b.name;
    this.serviceUrl = b.serviceUrl;
  }

  public TestEnvironmentInfo() {}

  /** */
  public TestEnvironmentInfo name(NameEnum name) {
    this.name = name;
    return this;
  }

  @ApiModelProperty(example = "TU", required = true, value = "")
  @JsonProperty("name")
  public NameEnum getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(NameEnum name) {
    this.name = name;
  }

  /** Service Endpoint */
  public TestEnvironmentInfo serviceUrl(ServiceUrlEnum serviceUrl) {
    this.serviceUrl = serviceUrl;
    return this;
  }

  @ApiModelProperty(
      example = "https://erp-test.app.ti-dienste.de",
      required = true,
      value = "Service Endpoint")
  @JsonProperty("serviceUrl")
  public ServiceUrlEnum getServiceUrl() {
    return serviceUrl;
  }

  @JsonProperty("serviceUrl")
  public void setServiceUrl(ServiceUrlEnum serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TestEnvironmentInfo testEnvironmentInfo = (TestEnvironmentInfo) o;
    return Objects.equals(this.name, testEnvironmentInfo.name)
        && Objects.equals(this.serviceUrl, testEnvironmentInfo.serviceUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, serviceUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TestEnvironmentInfo {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    serviceUrl: ").append(toIndentedString(serviceUrl)).append("\n");
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

  public static TestEnvironmentInfoBuilder<?, ?> builder() {
    return new TestEnvironmentInfoBuilderImpl();
  }

  private static class TestEnvironmentInfoBuilderImpl
      extends TestEnvironmentInfoBuilder<TestEnvironmentInfo, TestEnvironmentInfoBuilderImpl> {

    @Override
    protected TestEnvironmentInfoBuilderImpl self() {
      return this;
    }

    @Override
    public TestEnvironmentInfo build() {
      return new TestEnvironmentInfo(this);
    }
  }

  public abstract static class TestEnvironmentInfoBuilder<
      C extends TestEnvironmentInfo, B extends TestEnvironmentInfoBuilder<C, B>> {
    private NameEnum name;
    private ServiceUrlEnum serviceUrl;

    protected abstract B self();

    public abstract C build();

    public B name(NameEnum name) {
      this.name = name;
      return self();
    }

    public B serviceUrl(ServiceUrlEnum serviceUrl) {
      this.serviceUrl = serviceUrl;
      return self();
    }
  }
}
