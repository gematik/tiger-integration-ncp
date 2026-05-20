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

package de.gematik.test.ncp.gen.epa.api.configuration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

/** Datei, repräsentiert entweder als Dateipfad oder direkt durch ihren Inhalt */
@ApiModel(
    description = "Datei, repräsentiert entweder als Dateipfad oder direkt durch ihren Inhalt")
public class FileInfo {

  @ApiModelProperty(value = "")
  private String filePath;

  @ApiModelProperty(value = "")
  private byte[] fileContent;

  /**
   * Get filePath
   *
   * @return filePath
   */
  @JsonProperty("filePath")
  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public FileInfo filePath(String filePath) {
    this.filePath = filePath;
    return this;
  }

  /**
   * Get fileContent
   *
   * @return fileContent
   */
  @JsonProperty("fileContent")
  public byte[] getFileContent() {
    return fileContent;
  }

  public void setFileContent(byte[] fileContent) {
    this.fileContent = fileContent;
  }

  public FileInfo fileContent(byte[] fileContent) {
    this.fileContent = fileContent;
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
    FileInfo fileInfo = (FileInfo) o;
    return Objects.equals(this.filePath, fileInfo.filePath)
        && Objects.equals(this.fileContent, fileInfo.fileContent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filePath, fileContent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileInfo {\n");

    sb.append("    filePath: ").append(toIndentedString(filePath)).append("\n");
    sb.append("    fileContent: ").append(toIndentedString(fileContent)).append("\n");
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
