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
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DirectoryEntryWithCertificates {

  @ApiModelProperty(required = true, value = "")
  @Valid
  private DirectoryEntry directoryEntry;

  @ApiModelProperty(required = true, value = "")
  private List<byte[]> certificates = new ArrayList<>();

  /**
   * Get directoryEntry
   *
   * @return directoryEntry
   */
  @JsonProperty("directoryEntry")
  @NotNull
  public DirectoryEntry getDirectoryEntry() {
    return directoryEntry;
  }

  public void setDirectoryEntry(DirectoryEntry directoryEntry) {
    this.directoryEntry = directoryEntry;
  }

  public DirectoryEntryWithCertificates directoryEntry(DirectoryEntry directoryEntry) {
    this.directoryEntry = directoryEntry;
    return this;
  }

  /**
   * Get certificates
   *
   * @return certificates
   */
  @JsonProperty("certificates")
  @NotNull
  @Size(min = 0, max = 50)
  public List<byte[]> getCertificates() {
    return certificates;
  }

  public void setCertificates(List<byte[]> certificates) {
    this.certificates = certificates;
  }

  public DirectoryEntryWithCertificates certificates(List<byte[]> certificates) {
    this.certificates = certificates;
    return this;
  }

  public DirectoryEntryWithCertificates addCertificatesItem(byte[] certificatesItem) {
    this.certificates.add(certificatesItem);
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
    DirectoryEntryWithCertificates directoryEntryWithCertificates =
        (DirectoryEntryWithCertificates) o;
    return Objects.equals(this.directoryEntry, directoryEntryWithCertificates.directoryEntry)
        && Objects.equals(this.certificates, directoryEntryWithCertificates.certificates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(directoryEntry, certificates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DirectoryEntryWithCertificates {\n");

    sb.append("    directoryEntry: ").append(toIndentedString(directoryEntry)).append("\n");
    sb.append("    certificates: ").append(toIndentedString(certificates)).append("\n");
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
