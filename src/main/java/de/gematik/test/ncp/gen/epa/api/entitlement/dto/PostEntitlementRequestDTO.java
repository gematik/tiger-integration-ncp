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

package de.gematik.test.ncp.gen.epa.api.entitlement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

public class PostEntitlementRequestDTO {

  @ApiModelProperty(example = "2-883110000118994", value = "")
  private String telematikId;

  @ApiModelProperty(example = "Z123456789", required = true, value = "")
  private String kvnr;

  public enum TestCaseEnum {
    VALID_HCV(String.valueOf("validHcv")),
    NO_HCV(String.valueOf("noHcv")),
    INVALID_HCV_STRUCTURE(String.valueOf("invalidHcvStructure")),
    INVALID_HCV_HASH(String.valueOf("invalidHcvHash"));

    private String value;

    TestCaseEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TestCaseEnum fromValue(String value) {
      for (TestCaseEnum b : TestCaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "Specifies the test case to trigger different behaviors")
  /** Specifies the test case to trigger different behaviors */
  private TestCaseEnum testCase;

  /**
   * Get telematikId
   *
   * @return telematikId
   */
  @JsonProperty("telematikId")
  public String getTelematikId() {
    return telematikId;
  }

  public void setTelematikId(String telematikId) {
    this.telematikId = telematikId;
  }

  public PostEntitlementRequestDTO telematikId(String telematikId) {
    this.telematikId = telematikId;
    return this;
  }

  /**
   * Get kvnr
   *
   * @return kvnr
   */
  @JsonProperty("kvnr")
  public String getKvnr() {
    return kvnr;
  }

  public void setKvnr(String kvnr) {
    this.kvnr = kvnr;
  }

  public PostEntitlementRequestDTO kvnr(String kvnr) {
    this.kvnr = kvnr;
    return this;
  }

  /**
   * Specifies the test case to trigger different behaviors
   *
   * @return testCase
   */
  @JsonProperty("testCase")
  public String getTestCase() {
    if (testCase == null) {
      return null;
    }
    return testCase.value();
  }

  public void setTestCase(TestCaseEnum testCase) {
    this.testCase = testCase;
  }

  public PostEntitlementRequestDTO testCase(TestCaseEnum testCase) {
    this.testCase = testCase;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostEntitlementRequestDTO postEntitlementRequestDTO = (PostEntitlementRequestDTO) o;
    return Objects.equals(this.telematikId, postEntitlementRequestDTO.telematikId)
        && Objects.equals(this.kvnr, postEntitlementRequestDTO.kvnr)
        && Objects.equals(this.testCase, postEntitlementRequestDTO.testCase);
  }

  @Override
  public int hashCode() {
    return Objects.hash(telematikId, kvnr, testCase);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostEntitlementRequestDTO {\n");

    sb.append("    telematikId: ").append(toIndentedString(telematikId)).append("\n");
    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
    sb.append("    testCase: ").append(toIndentedString(testCase)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private static String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
