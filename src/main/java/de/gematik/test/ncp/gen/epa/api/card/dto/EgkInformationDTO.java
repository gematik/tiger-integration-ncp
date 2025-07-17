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

public class EgkInformationDTO {

  @ApiModelProperty(example = "Z123456789", value = "")
  private String kvnr;

  @ApiModelProperty(example = "80276883110000163990", value = "")
  private String iccsn;

  @ApiModelProperty(example = "Praxis Dr. Annamaria Heckhausén", value = "A readable name")
  /** A readable name */
  private String cardHolderName;

  @ApiModelProperty(example = "SMB or EGK", value = "")
  private String cardHandle;

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

  public EgkInformationDTO kvnr(String kvnr) {
    this.kvnr = kvnr;
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

  public EgkInformationDTO iccsn(String iccsn) {
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

  public EgkInformationDTO cardHolderName(String cardHolderName) {
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

  public EgkInformationDTO cardHandle(String cardHandle) {
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
    EgkInformationDTO egkInformationDTO = (EgkInformationDTO) o;
    return Objects.equals(this.kvnr, egkInformationDTO.kvnr)
        && Objects.equals(this.iccsn, egkInformationDTO.iccsn)
        && Objects.equals(this.cardHolderName, egkInformationDTO.cardHolderName)
        && Objects.equals(this.cardHandle, egkInformationDTO.cardHandle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kvnr, iccsn, cardHolderName, cardHandle);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EgkInformationDTO {\n");

    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
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
