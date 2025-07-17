/*
 * Copyright 2025 gematik GmbH
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
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.ncp.gen.epa.api.card.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

public class SmbInformationDTO {

  @ApiModelProperty(example = "2-883110000118994", value = "")
  private String telematikId;

  @ApiModelProperty(example = "80276883110000163990", value = "")
  private String iccsn;

  @ApiModelProperty(example = "Praxis Dr. Annamaria Heckhausén", value = "A readable name")
  /** A readable name */
  private String cardHolderName;

  @ApiModelProperty(example = "SMB or EGK", value = "")
  private String cardHandle;

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

  public SmbInformationDTO telematikId(String telematikId) {
    this.telematikId = telematikId;
    return this;
  }

  /**
   * Get iccsn
   *
   * @return iccsn
   */
  @JsonProperty("iccsn")
  public String getIccsn() {
    return iccsn;
  }

  public void setIccsn(String iccsn) {
    this.iccsn = iccsn;
  }

  public SmbInformationDTO iccsn(String iccsn) {
    this.iccsn = iccsn;
    return this;
  }

  /**
   * A readable name
   *
   * @return cardHolderName
   */
  @JsonProperty("cardHolderName")
  public String getCardHolderName() {
    return cardHolderName;
  }

  public void setCardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
  }

  public SmbInformationDTO cardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
    return this;
  }

  /**
   * Get cardHandle
   *
   * @return cardHandle
   */
  @JsonProperty("cardHandle")
  public String getCardHandle() {
    return cardHandle;
  }

  public void setCardHandle(String cardHandle) {
    this.cardHandle = cardHandle;
  }

  public SmbInformationDTO cardHandle(String cardHandle) {
    this.cardHandle = cardHandle;
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
    SmbInformationDTO smbInformationDTO = (SmbInformationDTO) o;
    return Objects.equals(this.telematikId, smbInformationDTO.telematikId)
        && Objects.equals(this.iccsn, smbInformationDTO.iccsn)
        && Objects.equals(this.cardHolderName, smbInformationDTO.cardHolderName)
        && Objects.equals(this.cardHandle, smbInformationDTO.cardHandle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(telematikId, iccsn, cardHolderName, cardHandle);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmbInformationDTO {\n");

    sb.append("    telematikId: ").append(toIndentedString(telematikId)).append("\n");
    sb.append("    iccsn: ").append(toIndentedString(iccsn)).append("\n");
    sb.append("    cardHolderName: ").append(toIndentedString(cardHolderName)).append("\n");
    sb.append("    cardHandle: ").append(toIndentedString(cardHandle)).append("\n");
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
