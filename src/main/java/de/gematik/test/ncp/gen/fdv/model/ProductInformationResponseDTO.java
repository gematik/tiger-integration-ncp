/*
 * Copyright (c) 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.Objects;

/** product information of epa fdv */
@ApiModel(description = "product information of epa fdv")
public class ProductInformationResponseDTO {

  @ApiModelProperty(value = "manufacturer ID")
  /** manufacturer ID */
  private String producerId;

  @ApiModelProperty(example = "1.0.0-0", required = true, value = "product version")
  /** product version */
  private String productVersion;

  @ApiModelProperty(required = true, value = "product Type, GS-A_3702")
  /** product Type, GS-A_3702 */
  private String productType;

  @ApiModelProperty(required = true, value = "product Type Version, GS-A_3702")
  /** product Type Version, GS-A_3702 */
  private String productTypeVersion;

  @ApiModelProperty(value = "Product identification, GS-A_3702")
  /** Product identification, GS-A_3702 */
  private String productIdentification;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value = "date-time notation as defined by RFC 3339, section 5.6")
  /** date-time notation as defined by RFC 3339, section 5.6 */
  private Date timeStamp;

  /**
   * manufacturer ID
   *
   * @return producerId
   */
  @JsonProperty("producerId")
  @Size(max = 5)
  public String getProducerId() {
    return producerId;
  }

  public void setProducerId(String producerId) {
    this.producerId = producerId;
  }

  public ProductInformationResponseDTO producerId(String producerId) {
    this.producerId = producerId;
    return this;
  }

  /**
   * product version
   *
   * @return productVersion
   */
  @JsonProperty("productVersion")
  @NotNull
  @Size(max = 12)
  public String getProductVersion() {
    return productVersion;
  }

  public void setProductVersion(String productVersion) {
    this.productVersion = productVersion;
  }

  public ProductInformationResponseDTO productVersion(String productVersion) {
    this.productVersion = productVersion;
    return this;
  }

  /**
   * product Type, GS-A_3702
   *
   * @return productType
   */
  @JsonProperty("productType")
  @NotNull
  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public ProductInformationResponseDTO productType(String productType) {
    this.productType = productType;
    return this;
  }

  /**
   * product Type Version, GS-A_3702
   *
   * @return productTypeVersion
   */
  @JsonProperty("productTypeVersion")
  @NotNull
  public String getProductTypeVersion() {
    return productTypeVersion;
  }

  public void setProductTypeVersion(String productTypeVersion) {
    this.productTypeVersion = productTypeVersion;
  }

  public ProductInformationResponseDTO productTypeVersion(String productTypeVersion) {
    this.productTypeVersion = productTypeVersion;
    return this;
  }

  /**
   * Product identification, GS-A_3702
   *
   * @return productIdentification
   */
  @JsonProperty("productIdentification")
  public String getProductIdentification() {
    return productIdentification;
  }

  public void setProductIdentification(String productIdentification) {
    this.productIdentification = productIdentification;
  }

  public ProductInformationResponseDTO productIdentification(String productIdentification) {
    this.productIdentification = productIdentification;
    return this;
  }

  /**
   * date-time notation as defined by RFC 3339, section 5.6
   *
   * @return timeStamp
   */
  @JsonProperty("timeStamp")
  public Date getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }

  public ProductInformationResponseDTO timeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
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
    ProductInformationResponseDTO productInformationResponseDTO = (ProductInformationResponseDTO) o;
    return Objects.equals(this.producerId, productInformationResponseDTO.producerId)
        && Objects.equals(this.productVersion, productInformationResponseDTO.productVersion)
        && Objects.equals(this.productType, productInformationResponseDTO.productType)
        && Objects.equals(this.productTypeVersion, productInformationResponseDTO.productTypeVersion)
        && Objects.equals(
            this.productIdentification, productInformationResponseDTO.productIdentification)
        && Objects.equals(this.timeStamp, productInformationResponseDTO.timeStamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        producerId,
        productVersion,
        productType,
        productTypeVersion,
        productIdentification,
        timeStamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductInformationResponseDTO {\n");

    sb.append("    producerId: ").append(toIndentedString(producerId)).append("\n");
    sb.append("    productVersion: ").append(toIndentedString(productVersion)).append("\n");
    sb.append("    productType: ").append(toIndentedString(productType)).append("\n");
    sb.append("    productTypeVersion: ").append(toIndentedString(productTypeVersion)).append("\n");
    sb.append("    productIdentification: ")
        .append(toIndentedString(productIdentification))
        .append("\n");
    sb.append("    timeStamp: ").append(toIndentedString(timeStamp)).append("\n");
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
