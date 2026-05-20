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

package de.gematik.test.ncp.gen.epa.api.card.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetCardsInfoResponseDTO {

  @ApiModelProperty(value = "")
  private List<SmbInformationDTO> smbInfo = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<EgkInformationDTO> egkInfo = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<HbaInformationDTO> hbaInfo = new ArrayList<>();

  @ApiModelProperty(required = true, value = "")
  private Boolean success;

  @ApiModelProperty(value = "")
  private String statusMessage;

  /**
   * Get smbInfo
   *
   * @return smbInfo
   */
  @JsonProperty("smbInfo")
  public List<SmbInformationDTO> getSmbInfo() {
    return smbInfo;
  }

  public void setSmbInfo(List<SmbInformationDTO> smbInfo) {
    this.smbInfo = smbInfo;
  }

  public GetCardsInfoResponseDTO smbInfo(List<SmbInformationDTO> smbInfo) {
    this.smbInfo = smbInfo;
    return this;
  }

  public GetCardsInfoResponseDTO addSmbInfoItem(SmbInformationDTO smbInfoItem) {
    this.smbInfo.add(smbInfoItem);
    return this;
  }

  /**
   * Get egkInfo
   *
   * @return egkInfo
   */
  @JsonProperty("egkInfo")
  public List<EgkInformationDTO> getEgkInfo() {
    return egkInfo;
  }

  public void setEgkInfo(List<EgkInformationDTO> egkInfo) {
    this.egkInfo = egkInfo;
  }

  public GetCardsInfoResponseDTO egkInfo(List<EgkInformationDTO> egkInfo) {
    this.egkInfo = egkInfo;
    return this;
  }

  public GetCardsInfoResponseDTO addEgkInfoItem(EgkInformationDTO egkInfoItem) {
    this.egkInfo.add(egkInfoItem);
    return this;
  }

  /**
   * Get hbaInfo
   *
   * @return hbaInfo
   */
  @JsonProperty("hbaInfo")
  public List<HbaInformationDTO> getHbaInfo() {
    return hbaInfo;
  }

  public void setHbaInfo(List<HbaInformationDTO> hbaInfo) {
    this.hbaInfo = hbaInfo;
  }

  public GetCardsInfoResponseDTO hbaInfo(List<HbaInformationDTO> hbaInfo) {
    this.hbaInfo = hbaInfo;
    return this;
  }

  public GetCardsInfoResponseDTO addHbaInfoItem(HbaInformationDTO hbaInfoItem) {
    this.hbaInfo.add(hbaInfoItem);
    return this;
  }

  /**
   * Get success
   *
   * @return success
   */
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public GetCardsInfoResponseDTO success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get statusMessage
   *
   * @return statusMessage
   */
  @JsonProperty("statusMessage")
  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  public GetCardsInfoResponseDTO statusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
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
    GetCardsInfoResponseDTO getCardsInfoResponseDTO = (GetCardsInfoResponseDTO) o;
    return Objects.equals(this.smbInfo, getCardsInfoResponseDTO.smbInfo)
        && Objects.equals(this.egkInfo, getCardsInfoResponseDTO.egkInfo)
        && Objects.equals(this.hbaInfo, getCardsInfoResponseDTO.hbaInfo)
        && Objects.equals(this.success, getCardsInfoResponseDTO.success)
        && Objects.equals(this.statusMessage, getCardsInfoResponseDTO.statusMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(smbInfo, egkInfo, hbaInfo, success, statusMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCardsInfoResponseDTO {\n");

    sb.append("    smbInfo: ").append(toIndentedString(smbInfo)).append("\n");
    sb.append("    egkInfo: ").append(toIndentedString(egkInfo)).append("\n");
    sb.append("    hbaInfo: ").append(toIndentedString(hbaInfo)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusMessage: ").append(toIndentedString(statusMessage)).append("\n");
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
