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
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Objects;

@JsonTypeName("TestFdV")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class TestFdV {
  public enum PlatformEnum {
    ANDROID(String.valueOf("Android")),
    I_OS(String.valueOf("iOS")),
    HUAWEI(String.valueOf("Huawei"));

    private String value;

    PlatformEnum(String v) {
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
    public static PlatformEnum fromString(String s) {
      for (PlatformEnum b : PlatformEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static PlatformEnum fromValue(String value) {
      for (PlatformEnum b : PlatformEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PlatformEnum platform;
  private String platformVersion;
  private Boolean isEmulated;

  public enum StatusEnum {
    RUNNING(String.valueOf("running")),
    STARTING(String.valueOf("starting")),
    STOPPED(String.valueOf("stopped"));

    private String value;

    StatusEnum(String v) {
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
    public static StatusEnum fromString(String s) {
      for (StatusEnum b : StatusEnum.values()) {
        // using Objects.toString() to be safe if value type non-object type
        // because types like 'int' etc. will be auto-boxed
        if (java.util.Objects.toString(b.value).equals(s)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected string value '" + s + "'");
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;
  private OffsetDateTime startTime;

  protected TestFdV(TestFdVBuilder<?, ?> b) {
    this.platform = b.platform;
    this.platformVersion = b.platformVersion;
    this.isEmulated = b.isEmulated;
    this.status = b.status;
    this.startTime = b.startTime;
  }

  public TestFdV() {}

  /** Mobile Device Platform */
  public TestFdV platform(PlatformEnum platform) {
    this.platform = platform;
    return this;
  }

  @ApiModelProperty(example = "Android", required = true, value = "Mobile Device Platform")
  @JsonProperty("platform")
  public PlatformEnum getPlatform() {
    return platform;
  }

  @JsonProperty("platform")
  public void setPlatform(PlatformEnum platform) {
    this.platform = platform;
  }

  /** */
  public TestFdV platformVersion(String platformVersion) {
    this.platformVersion = platformVersion;
    return this;
  }

  @ApiModelProperty(example = "14.0.0", required = true, value = "")
  @JsonProperty("platformVersion")
  public String getPlatformVersion() {
    return platformVersion;
  }

  @JsonProperty("platformVersion")
  public void setPlatformVersion(String platformVersion) {
    this.platformVersion = platformVersion;
  }

  /** */
  public TestFdV isEmulated(Boolean isEmulated) {
    this.isEmulated = isEmulated;
    return this;
  }

  @ApiModelProperty(example = "true", required = true, value = "")
  @JsonProperty("isEmulated")
  public Boolean getIsEmulated() {
    return isEmulated;
  }

  @JsonProperty("isEmulated")
  public void setIsEmulated(Boolean isEmulated) {
    this.isEmulated = isEmulated;
  }

  /** Current status of the test driver */
  public TestFdV status(StatusEnum status) {
    this.status = status;
    return this;
  }

  @ApiModelProperty(
      example = "running",
      required = true,
      value = "Current status of the test driver")
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  /** */
  public TestFdV startTime(OffsetDateTime startTime) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TestFdV testFdV = (TestFdV) o;
    return Objects.equals(this.platform, testFdV.platform)
        && Objects.equals(this.platformVersion, testFdV.platformVersion)
        && Objects.equals(this.isEmulated, testFdV.isEmulated)
        && Objects.equals(this.status, testFdV.status)
        && Objects.equals(this.startTime, testFdV.startTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(platform, platformVersion, isEmulated, status, startTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TestFdV {\n");

    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    platformVersion: ").append(toIndentedString(platformVersion)).append("\n");
    sb.append("    isEmulated: ").append(toIndentedString(isEmulated)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
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

  public static TestFdVBuilder<?, ?> builder() {
    return new TestFdVBuilderImpl();
  }

  private static class TestFdVBuilderImpl extends TestFdVBuilder<TestFdV, TestFdVBuilderImpl> {

    @Override
    protected TestFdVBuilderImpl self() {
      return this;
    }

    @Override
    public TestFdV build() {
      return new TestFdV(this);
    }
  }

  public abstract static class TestFdVBuilder<C extends TestFdV, B extends TestFdVBuilder<C, B>> {
    private PlatformEnum platform;
    private String platformVersion;
    private Boolean isEmulated;
    private StatusEnum status;
    private OffsetDateTime startTime;

    protected abstract B self();

    public abstract C build();

    public B platform(PlatformEnum platform) {
      this.platform = platform;
      return self();
    }

    public B platformVersion(String platformVersion) {
      this.platformVersion = platformVersion;
      return self();
    }

    public B isEmulated(Boolean isEmulated) {
      this.isEmulated = isEmulated;
      return self();
    }

    public B status(StatusEnum status) {
      this.status = status;
      return self();
    }

    public B startTime(OffsetDateTime startTime) {
      this.startTime = startTime;
      return self();
    }
  }
}
