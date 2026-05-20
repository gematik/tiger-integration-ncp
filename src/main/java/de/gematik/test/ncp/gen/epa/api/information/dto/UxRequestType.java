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

package de.gematik.test.ncp.gen.epa.api.information.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

public class UxRequestType {

  public enum UseCaseEnum {
    LOGIN_PS(String.valueOf("UX_Login_PS")),
    DOC_UPLOAD_PS(String.valueOf("UX_Doc_Upload_PS")),
    DOC_DOWNLOAD_PS(String.valueOf("UX_Doc_Download_PS")),
    LOGIN_V(String.valueOf("UX_Login_V")),
    DOC_UPLOAD_V(String.valueOf("UX_Doc_Upload_V")),
    DOC_DOWNLOAD_V(String.valueOf("UX_Doc_Download_V")),
    LEI_SEARCH(String.valueOf("UX_LEI_search"));

    private String value;

    UseCaseEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static UseCaseEnum fromValue(String value) {
      for (UseCaseEnum b : UseCaseEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(required = true, value = "Use case identifier")
  /** Use case identifier */
  private UseCaseEnum useCase;

  @ApiModelProperty(required = true, value = "Use case execution time in milliseconds")
  /** Use case execution time in milliseconds */
  private Integer measurement;

  /**
   * Use case identifier
   *
   * @return useCase
   */
  @JsonProperty("useCase")
  public String getUseCase() {
    if (useCase == null) {
      return null;
    }
    return useCase.value();
  }

  public void setUseCase(UseCaseEnum useCase) {
    this.useCase = useCase;
  }

  public UxRequestType useCase(UseCaseEnum useCase) {
    this.useCase = useCase;
    return this;
  }

  /**
   * Use case execution time in milliseconds
   *
   * @return measurement
   */
  @JsonProperty("measurement")
  public Integer getMeasurement() {
    return measurement;
  }

  public void setMeasurement(Integer measurement) {
    this.measurement = measurement;
  }

  public UxRequestType measurement(Integer measurement) {
    this.measurement = measurement;
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
    UxRequestType uxRequestType = (UxRequestType) o;
    return Objects.equals(this.useCase, uxRequestType.useCase)
        && Objects.equals(this.measurement, uxRequestType.measurement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(useCase, measurement);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UxRequestType {\n");

    sb.append("    useCase: ").append(toIndentedString(useCase)).append("\n");
    sb.append("    measurement: ").append(toIndentedString(measurement)).append("\n");
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
