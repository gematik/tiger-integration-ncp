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
import java.time.OffsetDateTime;
import java.util.Objects;

@JsonTypeName("Info")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Info {
  private String title;
  private String testDriverVersion;
  private OffsetDateTime startTime;
  private TestFdV testFdV;
  private TestEnvironmentInfo testEnvironment;

  protected Info(InfoBuilder<?, ?> b) {
    this.title = b.title;
    this.testDriverVersion = b.testDriverVersion;
    this.startTime = b.startTime;
    this.testFdV = b.testFdV;
    this.testEnvironment = b.testEnvironment;
  }

  public Info() {}

  /** Title of the application */
  public Info title(String title) {
    this.title = title;
    return this;
  }

  @ApiModelProperty(
      example = "Test driver interface of ERP Test-FdV Module",
      required = true,
      value = "Title of the application")
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  /** Version of the implemented test driver version */
  public Info testDriverVersion(String testDriverVersion) {
    this.testDriverVersion = testDriverVersion;
    return this;
  }

  @ApiModelProperty(
      example = "1.0.0",
      required = true,
      value = "Version of the implemented test driver version")
  @JsonProperty("testDriverVersion")
  public String getTestDriverVersion() {
    return testDriverVersion;
  }

  @JsonProperty("testDriverVersion")
  public void setTestDriverVersion(String testDriverVersion) {
    this.testDriverVersion = testDriverVersion;
  }

  /** */
  public Info startTime(OffsetDateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  @ApiModelProperty(example = "2020-03-02T08:25:05Z", required = true, value = "")
  @JsonProperty("startTime")
  public OffsetDateTime getStartTime() {
    return startTime;
  }

  @JsonProperty("startTime")
  public void setStartTime(OffsetDateTime startTime) {
    this.startTime = startTime;
  }

  /** */
  public Info testFdV(TestFdV testFdV) {
    this.testFdV = testFdV;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("testFdV")
  public TestFdV getTestFdV() {
    return testFdV;
  }

  @JsonProperty("testFdV")
  public void setTestFdV(TestFdV testFdV) {
    this.testFdV = testFdV;
  }

  /** */
  public Info testEnvironment(TestEnvironmentInfo testEnvironment) {
    this.testEnvironment = testEnvironment;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("testEnvironment")
  public TestEnvironmentInfo getTestEnvironment() {
    return testEnvironment;
  }

  @JsonProperty("testEnvironment")
  public void setTestEnvironment(TestEnvironmentInfo testEnvironment) {
    this.testEnvironment = testEnvironment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Info info = (Info) o;
    return Objects.equals(this.title, info.title)
        && Objects.equals(this.testDriverVersion, info.testDriverVersion)
        && Objects.equals(this.startTime, info.startTime)
        && Objects.equals(this.testFdV, info.testFdV)
        && Objects.equals(this.testEnvironment, info.testEnvironment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, testDriverVersion, startTime, testFdV, testEnvironment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");

    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    testDriverVersion: ").append(toIndentedString(testDriverVersion)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    testFdV: ").append(toIndentedString(testFdV)).append("\n");
    sb.append("    testEnvironment: ").append(toIndentedString(testEnvironment)).append("\n");
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

  public static InfoBuilder<?, ?> builder() {
    return new InfoBuilderImpl();
  }

  private static class InfoBuilderImpl extends InfoBuilder<Info, InfoBuilderImpl> {

    @Override
    protected InfoBuilderImpl self() {
      return this;
    }

    @Override
    public Info build() {
      return new Info(this);
    }
  }

  public abstract static class InfoBuilder<C extends Info, B extends InfoBuilder<C, B>> {
    private String title;
    private String testDriverVersion;
    private OffsetDateTime startTime;
    private TestFdV testFdV;
    private TestEnvironmentInfo testEnvironment;

    protected abstract B self();

    public abstract C build();

    public B title(String title) {
      this.title = title;
      return self();
    }

    public B testDriverVersion(String testDriverVersion) {
      this.testDriverVersion = testDriverVersion;
      return self();
    }

    public B startTime(OffsetDateTime startTime) {
      this.startTime = startTime;
      return self();
    }

    public B testFdV(TestFdV testFdV) {
      this.testFdV = testFdV;
      return self();
    }

    public B testEnvironment(TestEnvironmentInfo testEnvironment) {
      this.testEnvironment = testEnvironment;
      return self();
    }
  }
}
