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

package de.gematik.test.ncp.gen.epa.api.documents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DocumentMetadata {

  @ApiModelProperty(value = "")
  private List<Author> author = new ArrayList<>();

  @ApiModelProperty(value = "")
  private String availabilityStatus;

  @ApiModelProperty(value = "")
  private List<String> confidentialityCode = new ArrayList<>();

  @ApiModelProperty(value = "")
  private String classCode;

  @ApiModelProperty(value = "")
  private String comments;

  @ApiModelProperty(value = "")
  private LocalDateTime creationTime;

  @ApiModelProperty(value = "")
  private String entryUUID;

  @ApiModelProperty(value = "")
  private List<String> eventCodeList = new ArrayList<>();

  @ApiModelProperty(value = "")
  private String formatCode;

  @ApiModelProperty(value = "")
  private String hash;

  @ApiModelProperty(value = "")
  private String healthcareFacilityTypeCode;

  @ApiModelProperty(value = "")
  private String languageCode;

  @ApiModelProperty(value = "")
  private String legalAuthenticator;

  @ApiModelProperty(value = "")
  private String mimeType;

  @ApiModelProperty(value = "")
  private String practiceSettingCode;

  @ApiModelProperty(value = "")
  private List<String> referenceIdList = new ArrayList<>();

  @ApiModelProperty(value = "")
  private LocalDateTime serviceStartTime;

  @ApiModelProperty(value = "")
  private LocalDateTime serviceStopTime;

  @ApiModelProperty(value = "")
  private Integer size;

  @ApiModelProperty(value = "")
  private String title;

  @ApiModelProperty(value = "")
  private String typeCode;

  @ApiModelProperty(value = "")
  private String uniqueId;

  @ApiModelProperty(value = "")
  private String uri;

  @ApiModelProperty(value = "")
  private String repositoryUniqueId;

  @ApiModelProperty(value = "")
  private String home;

  @ApiModelProperty(value = "")
  private String patientId;

  /**
   * Get author
   *
   * @return author
   */
  @JsonProperty("author")
  public List<Author> getAuthor() {
    return author;
  }

  public void setAuthor(List<Author> author) {
    this.author = author;
  }

  public DocumentMetadata author(List<Author> author) {
    this.author = author;
    return this;
  }

  public DocumentMetadata addAuthorItem(Author authorItem) {
    this.author.add(authorItem);
    return this;
  }

  /**
   * Get availabilityStatus
   *
   * @return availabilityStatus
   */
  @JsonProperty("availabilityStatus")
  public String getAvailabilityStatus() {
    return availabilityStatus;
  }

  public void setAvailabilityStatus(String availabilityStatus) {
    this.availabilityStatus = availabilityStatus;
  }

  public DocumentMetadata availabilityStatus(String availabilityStatus) {
    this.availabilityStatus = availabilityStatus;
    return this;
  }

  /**
   * Get confidentialityCode
   *
   * @return confidentialityCode
   */
  @JsonProperty("confidentialityCode")
  public List<String> getConfidentialityCode() {
    return confidentialityCode;
  }

  public void setConfidentialityCode(List<String> confidentialityCode) {
    this.confidentialityCode = confidentialityCode;
  }

  public DocumentMetadata confidentialityCode(List<String> confidentialityCode) {
    this.confidentialityCode = confidentialityCode;
    return this;
  }

  public DocumentMetadata addConfidentialityCodeItem(String confidentialityCodeItem) {
    this.confidentialityCode.add(confidentialityCodeItem);
    return this;
  }

  /**
   * Get classCode
   *
   * @return classCode
   */
  @JsonProperty("classCode")
  public String getClassCode() {
    return classCode;
  }

  public void setClassCode(String classCode) {
    this.classCode = classCode;
  }

  public DocumentMetadata classCode(String classCode) {
    this.classCode = classCode;
    return this;
  }

  /**
   * Get comments
   *
   * @return comments
   */
  @JsonProperty("comments")
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public DocumentMetadata comments(String comments) {
    this.comments = comments;
    return this;
  }

  /**
   * Get creationTime
   *
   * @return creationTime
   */
  @JsonProperty("creationTime")
  public LocalDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(LocalDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public DocumentMetadata creationTime(LocalDateTime creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  /**
   * Get entryUUID
   *
   * @return entryUUID
   */
  @JsonProperty("entryUUID")
  public String getEntryUUID() {
    return entryUUID;
  }

  public void setEntryUUID(String entryUUID) {
    this.entryUUID = entryUUID;
  }

  public DocumentMetadata entryUUID(String entryUUID) {
    this.entryUUID = entryUUID;
    return this;
  }

  /**
   * Get eventCodeList
   *
   * @return eventCodeList
   */
  @JsonProperty("eventCodeList")
  public List<String> getEventCodeList() {
    return eventCodeList;
  }

  public void setEventCodeList(List<String> eventCodeList) {
    this.eventCodeList = eventCodeList;
  }

  public DocumentMetadata eventCodeList(List<String> eventCodeList) {
    this.eventCodeList = eventCodeList;
    return this;
  }

  public DocumentMetadata addEventCodeListItem(String eventCodeListItem) {
    this.eventCodeList.add(eventCodeListItem);
    return this;
  }

  /**
   * Get formatCode
   *
   * @return formatCode
   */
  @JsonProperty("formatCode")
  public String getFormatCode() {
    return formatCode;
  }

  public void setFormatCode(String formatCode) {
    this.formatCode = formatCode;
  }

  public DocumentMetadata formatCode(String formatCode) {
    this.formatCode = formatCode;
    return this;
  }

  /**
   * Get hash
   *
   * @return hash
   */
  @JsonProperty("hash")
  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public DocumentMetadata hash(String hash) {
    this.hash = hash;
    return this;
  }

  /**
   * Get healthcareFacilityTypeCode
   *
   * @return healthcareFacilityTypeCode
   */
  @JsonProperty("healthcareFacilityTypeCode")
  public String getHealthcareFacilityTypeCode() {
    return healthcareFacilityTypeCode;
  }

  public void setHealthcareFacilityTypeCode(String healthcareFacilityTypeCode) {
    this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
  }

  public DocumentMetadata healthcareFacilityTypeCode(String healthcareFacilityTypeCode) {
    this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
    return this;
  }

  /**
   * Get languageCode
   *
   * @return languageCode
   */
  @JsonProperty("languageCode")
  public String getLanguageCode() {
    return languageCode;
  }

  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  public DocumentMetadata languageCode(String languageCode) {
    this.languageCode = languageCode;
    return this;
  }

  /**
   * Get legalAuthenticator
   *
   * @return legalAuthenticator
   */
  @JsonProperty("legalAuthenticator")
  public String getLegalAuthenticator() {
    return legalAuthenticator;
  }

  public void setLegalAuthenticator(String legalAuthenticator) {
    this.legalAuthenticator = legalAuthenticator;
  }

  public DocumentMetadata legalAuthenticator(String legalAuthenticator) {
    this.legalAuthenticator = legalAuthenticator;
    return this;
  }

  /**
   * Get mimeType
   *
   * @return mimeType
   */
  @JsonProperty("mimeType")
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public DocumentMetadata mimeType(String mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Get practiceSettingCode
   *
   * @return practiceSettingCode
   */
  @JsonProperty("practiceSettingCode")
  public String getPracticeSettingCode() {
    return practiceSettingCode;
  }

  public void setPracticeSettingCode(String practiceSettingCode) {
    this.practiceSettingCode = practiceSettingCode;
  }

  public DocumentMetadata practiceSettingCode(String practiceSettingCode) {
    this.practiceSettingCode = practiceSettingCode;
    return this;
  }

  /**
   * Get referenceIdList
   *
   * @return referenceIdList
   */
  @JsonProperty("referenceIdList")
  public List<String> getReferenceIdList() {
    return referenceIdList;
  }

  public void setReferenceIdList(List<String> referenceIdList) {
    this.referenceIdList = referenceIdList;
  }

  public DocumentMetadata referenceIdList(List<String> referenceIdList) {
    this.referenceIdList = referenceIdList;
    return this;
  }

  public DocumentMetadata addReferenceIdListItem(String referenceIdListItem) {
    this.referenceIdList.add(referenceIdListItem);
    return this;
  }

  /**
   * Get serviceStartTime
   *
   * @return serviceStartTime
   */
  @JsonProperty("serviceStartTime")
  public LocalDateTime getServiceStartTime() {
    return serviceStartTime;
  }

  public void setServiceStartTime(LocalDateTime serviceStartTime) {
    this.serviceStartTime = serviceStartTime;
  }

  public DocumentMetadata serviceStartTime(LocalDateTime serviceStartTime) {
    this.serviceStartTime = serviceStartTime;
    return this;
  }

  /**
   * Get serviceStopTime
   *
   * @return serviceStopTime
   */
  @JsonProperty("serviceStopTime")
  public LocalDateTime getServiceStopTime() {
    return serviceStopTime;
  }

  public void setServiceStopTime(LocalDateTime serviceStopTime) {
    this.serviceStopTime = serviceStopTime;
  }

  public DocumentMetadata serviceStopTime(LocalDateTime serviceStopTime) {
    this.serviceStopTime = serviceStopTime;
    return this;
  }

  /**
   * Get size
   *
   * @return size
   */
  @JsonProperty("size")
  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public DocumentMetadata size(Integer size) {
    this.size = size;
    return this;
  }

  /**
   * Get title
   *
   * @return title
   */
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public DocumentMetadata title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get typeCode
   *
   * @return typeCode
   */
  @JsonProperty("typeCode")
  public String getTypeCode() {
    return typeCode;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  public DocumentMetadata typeCode(String typeCode) {
    this.typeCode = typeCode;
    return this;
  }

  /**
   * Get uniqueId
   *
   * @return uniqueId
   */
  @JsonProperty("uniqueId")
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public DocumentMetadata uniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
    return this;
  }

  /**
   * Get uri
   *
   * @return uri
   */
  @JsonProperty("uri")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public DocumentMetadata uri(String uri) {
    this.uri = uri;
    return this;
  }

  /**
   * Get repositoryUniqueId
   *
   * @return repositoryUniqueId
   */
  @JsonProperty("repositoryUniqueId")
  public String getRepositoryUniqueId() {
    return repositoryUniqueId;
  }

  public void setRepositoryUniqueId(String repositoryUniqueId) {
    this.repositoryUniqueId = repositoryUniqueId;
  }

  public DocumentMetadata repositoryUniqueId(String repositoryUniqueId) {
    this.repositoryUniqueId = repositoryUniqueId;
    return this;
  }

  /**
   * Get home
   *
   * @return home
   */
  @JsonProperty("home")
  public String getHome() {
    return home;
  }

  public void setHome(String home) {
    this.home = home;
  }

  public DocumentMetadata home(String home) {
    this.home = home;
    return this;
  }

  /**
   * Get patientId
   *
   * @return patientId
   */
  @JsonProperty("patientId")
  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public DocumentMetadata patientId(String patientId) {
    this.patientId = patientId;
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
    DocumentMetadata documentMetadata = (DocumentMetadata) o;
    return Objects.equals(this.author, documentMetadata.author)
        && Objects.equals(this.availabilityStatus, documentMetadata.availabilityStatus)
        && Objects.equals(this.confidentialityCode, documentMetadata.confidentialityCode)
        && Objects.equals(this.classCode, documentMetadata.classCode)
        && Objects.equals(this.comments, documentMetadata.comments)
        && Objects.equals(this.creationTime, documentMetadata.creationTime)
        && Objects.equals(this.entryUUID, documentMetadata.entryUUID)
        && Objects.equals(this.eventCodeList, documentMetadata.eventCodeList)
        && Objects.equals(this.formatCode, documentMetadata.formatCode)
        && Objects.equals(this.hash, documentMetadata.hash)
        && Objects.equals(
            this.healthcareFacilityTypeCode, documentMetadata.healthcareFacilityTypeCode)
        && Objects.equals(this.languageCode, documentMetadata.languageCode)
        && Objects.equals(this.legalAuthenticator, documentMetadata.legalAuthenticator)
        && Objects.equals(this.mimeType, documentMetadata.mimeType)
        && Objects.equals(this.practiceSettingCode, documentMetadata.practiceSettingCode)
        && Objects.equals(this.referenceIdList, documentMetadata.referenceIdList)
        && Objects.equals(this.serviceStartTime, documentMetadata.serviceStartTime)
        && Objects.equals(this.serviceStopTime, documentMetadata.serviceStopTime)
        && Objects.equals(this.size, documentMetadata.size)
        && Objects.equals(this.title, documentMetadata.title)
        && Objects.equals(this.typeCode, documentMetadata.typeCode)
        && Objects.equals(this.uniqueId, documentMetadata.uniqueId)
        && Objects.equals(this.uri, documentMetadata.uri)
        && Objects.equals(this.repositoryUniqueId, documentMetadata.repositoryUniqueId)
        && Objects.equals(this.home, documentMetadata.home)
        && Objects.equals(this.patientId, documentMetadata.patientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        author,
        availabilityStatus,
        confidentialityCode,
        classCode,
        comments,
        creationTime,
        entryUUID,
        eventCodeList,
        formatCode,
        hash,
        healthcareFacilityTypeCode,
        languageCode,
        legalAuthenticator,
        mimeType,
        practiceSettingCode,
        referenceIdList,
        serviceStartTime,
        serviceStopTime,
        size,
        title,
        typeCode,
        uniqueId,
        uri,
        repositoryUniqueId,
        home,
        patientId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentMetadata {\n");

    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    availabilityStatus: ").append(toIndentedString(availabilityStatus)).append("\n");
    sb.append("    confidentialityCode: ")
        .append(toIndentedString(confidentialityCode))
        .append("\n");
    sb.append("    classCode: ").append(toIndentedString(classCode)).append("\n");
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
    sb.append("    creationTime: ").append(toIndentedString(creationTime)).append("\n");
    sb.append("    entryUUID: ").append(toIndentedString(entryUUID)).append("\n");
    sb.append("    eventCodeList: ").append(toIndentedString(eventCodeList)).append("\n");
    sb.append("    formatCode: ").append(toIndentedString(formatCode)).append("\n");
    sb.append("    hash: ").append(toIndentedString(hash)).append("\n");
    sb.append("    healthcareFacilityTypeCode: ")
        .append(toIndentedString(healthcareFacilityTypeCode))
        .append("\n");
    sb.append("    languageCode: ").append(toIndentedString(languageCode)).append("\n");
    sb.append("    legalAuthenticator: ").append(toIndentedString(legalAuthenticator)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    practiceSettingCode: ")
        .append(toIndentedString(practiceSettingCode))
        .append("\n");
    sb.append("    referenceIdList: ").append(toIndentedString(referenceIdList)).append("\n");
    sb.append("    serviceStartTime: ").append(toIndentedString(serviceStartTime)).append("\n");
    sb.append("    serviceStopTime: ").append(toIndentedString(serviceStopTime)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    typeCode: ").append(toIndentedString(typeCode)).append("\n");
    sb.append("    uniqueId: ").append(toIndentedString(uniqueId)).append("\n");
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    repositoryUniqueId: ").append(toIndentedString(repositoryUniqueId)).append("\n");
    sb.append("    home: ").append(toIndentedString(home)).append("\n");
    sb.append("    patientId: ").append(toIndentedString(patientId)).append("\n");
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
