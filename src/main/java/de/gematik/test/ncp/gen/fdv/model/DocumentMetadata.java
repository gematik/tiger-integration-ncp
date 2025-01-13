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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/** meta data of document */
@ApiModel(description = "meta data of document")
public class DocumentMetadata {

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid Author> author = new ArrayList<>();

  @ApiModelProperty(value = "Status of document")
  /** Status of document */
  private String availabilityStatus;

  @ApiModelProperty(value = "")
  private List<String> confidentialityCode = new ArrayList<>();

  @ApiModelProperty(value = "Code out of Value Set for DocumentEntry.classCode")
  /** Code out of Value Set for DocumentEntry.classCode */
  private String classCode;

  @ApiModelProperty(value = "additional notes in free text")
  /** additional notes in free text */
  private String comments;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value = "Creation time; date-time notation as defined by RFC 3339, section 5.6")
  /** Creation time; date-time notation as defined by RFC 3339, section 5.6 */
  private Date creationTime;

  @ApiModelProperty(value = "EntryUUID of a document")
  /** EntryUUID of a document */
  private String entryUUID;

  @ApiModelProperty(
      value =
          "Events, which lead to creation of the document, Code out of Value Set for DocumentEntry.eventCodeList or table in A_17540-XX")
  /**
   * Events, which lead to creation of the document, Code out of Value Set for
   * DocumentEntry.eventCodeList or table in A_17540-XX
   */
  private List<String> eventCodeList = new ArrayList<>();

  @ApiModelProperty(
      value = "Code out of Value Set for DocumentEntry.formatCode or table in A_14761-XX")
  /** Code out of Value Set for DocumentEntry.formatCode or table in A_14761-XX */
  private String formatCode;

  @ApiModelProperty(value = "")
  private String hash;

  @ApiModelProperty(value = "Code out of Value Set for DocumentEntry.healthcareFacilityTypeCode")
  /** Code out of Value Set for DocumentEntry.healthcareFacilityTypeCode */
  private String healthcareFacilityTypeCode;

  @ApiModelProperty(value = "Code out of Value Set for DocumentEntry.languageCode")
  /** Code out of Value Set for DocumentEntry.languageCode */
  private String languageCode;

  @ApiModelProperty(value = "legally responsible party")
  /** legally responsible party */
  private String legalAuthenticator;

  public enum MimeTypeEnum {
    APPLICATION_PDF(String.valueOf("application/pdf")),
    IMAGE_JPEG(String.valueOf("image/jpeg")),
    IMAGE_PNG(String.valueOf("image/png")),
    IMAGE_TIFF(String.valueOf("image/tiff")),
    TEXT_PLAIN(String.valueOf("text/plain")),
    TEXT_RTF(String.valueOf("text/rtf")),
    APPLICATION_XML(String.valueOf("application/xml")),
    APPLICATION_HL7_V3(String.valueOf("application/hl7-v3")),
    APPLICATION_PKCS7_MIME(String.valueOf("application/pkcs7-mime")),
    APPLICATION_FHIR_XML(String.valueOf("application/fhir+xml")),
    APPLICATION_XACML_XML(String.valueOf("application/xacml+xml"));

    private String value;

    MimeTypeEnum(String v) {
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

    @JsonCreator
    public static MimeTypeEnum fromValue(String value) {
      for (MimeTypeEnum b : MimeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @ApiModelProperty(value = "MIME-Type des Dokuments")
  /** MIME-Type des Dokuments */
  private MimeTypeEnum mimeType;

  @ApiModelProperty(
      value = "Code out of Value Set for DocumentEntry.practiceSettingCode or table in A_16944-XX")
  /** Code out of Value Set for DocumentEntry.practiceSettingCode or table in A_16944-XX */
  private String practiceSettingCode;

  @ApiModelProperty(value = "List of id associated with the document")
  /** List of id associated with the document */
  private List<String> referenceIdList = new ArrayList<>();

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Date of time, start of treatment; date-time notation as defined by RFC 3339, section 5.6")
  /** Date of time, start of treatment; date-time notation as defined by RFC 3339, section 5.6 */
  private Date serviceStartTime;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Date of time, end of treatment;; date-time notation as defined by RFC 3339, section 5.6")
  /** Date of time, end of treatment;; date-time notation as defined by RFC 3339, section 5.6 */
  private Date serviceStopTime;

  @ApiModelProperty(value = "Size of document in bytes")
  /** Size of document in bytes */
  private Integer size;

  @ApiModelProperty(value = "Title of document")
  /** Title of document */
  private String title;

  @ApiModelProperty(value = "Sort of document; Code out of Value Set for DocumentEntry.typeCode")
  /** Sort of document; Code out of Value Set for DocumentEntry.typeCode */
  private String typeCode;

  @ApiModelProperty(value = "Unique identification of document")
  /** Unique identification of document */
  private String uniqueId;

  @ApiModelProperty(value = "File name")
  /** File name */
  private String uri;

  /**
   * Get author
   *
   * @return author
   */
  @JsonProperty("author")
  public List<@Valid Author> getAuthor() {
    return author;
  }

  public void setAuthor(List<@Valid Author> author) {
    this.author = author;
  }

  public DocumentMetadata author(List<@Valid Author> author) {
    this.author = author;
    return this;
  }

  public DocumentMetadata addAuthorItem(Author authorItem) {
    this.author.add(authorItem);
    return this;
  }

  /**
   * Status of document
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
   * Code out of Value Set for DocumentEntry.classCode
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
   * additional notes in free text
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
   * Creation time; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return creationTime
   */
  @JsonProperty("creationTime")
  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public DocumentMetadata creationTime(Date creationTime) {
    this.creationTime = creationTime;
    return this;
  }

  /**
   * EntryUUID of a document
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
   * Events, which lead to creation of the document, Code out of Value Set for
   * DocumentEntry.eventCodeList or table in A_17540-XX
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
   * Code out of Value Set for DocumentEntry.formatCode or table in A_14761-XX
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
   * Code out of Value Set for DocumentEntry.healthcareFacilityTypeCode
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
   * Code out of Value Set for DocumentEntry.languageCode
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
   * legally responsible party
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
   * MIME-Type des Dokuments
   *
   * @return mimeType
   */
  @JsonProperty("mimeType")
  public String getMimeType() {
    if (mimeType == null) {
      return null;
    }
    return mimeType.value();
  }

  public void setMimeType(MimeTypeEnum mimeType) {
    this.mimeType = mimeType;
  }

  public DocumentMetadata mimeType(MimeTypeEnum mimeType) {
    this.mimeType = mimeType;
    return this;
  }

  /**
   * Code out of Value Set for DocumentEntry.practiceSettingCode or table in A_16944-XX
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
   * List of id associated with the document
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
   * Date of time, start of treatment; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return serviceStartTime
   */
  @JsonProperty("serviceStartTime")
  public Date getServiceStartTime() {
    return serviceStartTime;
  }

  public void setServiceStartTime(Date serviceStartTime) {
    this.serviceStartTime = serviceStartTime;
  }

  public DocumentMetadata serviceStartTime(Date serviceStartTime) {
    this.serviceStartTime = serviceStartTime;
    return this;
  }

  /**
   * Date of time, end of treatment;; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return serviceStopTime
   */
  @JsonProperty("serviceStopTime")
  public Date getServiceStopTime() {
    return serviceStopTime;
  }

  public void setServiceStopTime(Date serviceStopTime) {
    this.serviceStopTime = serviceStopTime;
  }

  public DocumentMetadata serviceStopTime(Date serviceStopTime) {
    this.serviceStopTime = serviceStopTime;
    return this;
  }

  /**
   * Size of document in bytes
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
   * Title of document
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
   * Sort of document; Code out of Value Set for DocumentEntry.typeCode
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
   * Unique identification of document
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
   * File name
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
        && Objects.equals(this.uri, documentMetadata.uri);
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
        uri);
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
