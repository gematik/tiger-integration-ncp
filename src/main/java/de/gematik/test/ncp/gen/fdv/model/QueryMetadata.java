/*
 * Copyright 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/** Metadaten Parameter für die Suche nach Dokumenten, Ordner, Submission Sets und Verknüpfungen */
@ApiModel(
    description =
        "Metadaten Parameter für die Suche nach Dokumenten, Ordner, Submission Sets und Verknüpfungen")
public class QueryMetadata {

  @ApiModelProperty(value = "Liste von EntryUUIDs von SubmissionSets")
  /** Liste von EntryUUIDs von SubmissionSets */
  private List<String> xdSSubmissionSetEntryUUID = new ArrayList<>();

  @ApiModelProperty(value = "Liste von uniqueIDs von SubmissionSets")
  /** Liste von uniqueIDs von SubmissionSets */
  private List<String> xdSSubmissionSetUniqueId = new ArrayList<>();

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value = "Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /** Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6 */
  private Date xdSSubmissionSetSubmissionTimeFrom;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "XDSDocumentEntryCreationTimeTo, Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /**
   * XDSDocumentEntryCreationTimeTo, Ende Zeitraum für Suche; date-time notation as defined by RFC
   * 3339, section 5.6
   */
  private Date xdSSubmissionSetSubmissionTimeTo;

  @ApiModelProperty(value = "Liste von EntryUUIDs von Dokumenten")
  /** Liste von EntryUUIDs von Dokumenten */
  private List<String> xdSDocumentEntryEntryUUID = new ArrayList<>();

  @ApiModelProperty(value = "Liste von uniqueIds von Dokumenten")
  /** Liste von uniqueIds von Dokumenten */
  private List<String> xdSDocumentEntryUniqueId = new ArrayList<>();

  @ApiModelProperty(value = "Liste von rootDocumentUniqueIds")
  /** Liste von rootDocumentUniqueIds */
  private List<String> xdSDocumentEntryReferenceIdList = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryClassCode = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryPracticeSettingCode = new ArrayList<>();

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value = "Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /** Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6 */
  private Date xdSDocumentEntryCreationTimeFrom;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value = "Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /** Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6 */
  private Date xdSDocumentEntryCreationTimeTo;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis begonnen wurde. Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis begonnen wurde. Beginn
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   */
  private Date xdSDocumentEntryServiceStartTimeFrom;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis begonnen wurde. Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis begonnen wurde. Ende
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   */
  private Date xdSDocumentEntryServiceStartTimeTo;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis beendet wurde. Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis beendet wurde. Beginn
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   */
  private Date xdSDocumentEntryServiceStopTimeFrom;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis beendet wurde. Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6")
  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis beendet wurde. Ende
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   */
  private Date xdSDocumentEntryServiceStopTimeTo;

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryHealthcareFacilityTypeCode = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryEventCodeList = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryConfidentialityCode = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryAuthorPerson = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryFormatCode = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryTypeCode = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryTitle = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> xdSDocumentEntryAuthorInstitution = new ArrayList<>();

  @ApiModelProperty(value = "Status des Dokumentes")
  /** Status des Dokumentes */
  private String xdSDocumentEntryStatus;

  @ApiModelProperty(value = "Folder Unique Id")
  /** Folder Unique Id */
  private String xdSFolderUniqueId;

  @ApiModelProperty(value = "Status der Ordner; nur anwendbar mit FindFolders")
  /** Status der Ordner; nur anwendbar mit FindFolders */
  private String xdSFolderStatus;

  @ApiModelProperty(value = "Liste von Codes (z.B. 'childrecord', 'patientdoc')")
  /** Liste von Codes (z.B. 'childrecord', 'patientdoc') */
  private List<String> xdSFolderCodeList = new ArrayList<>();

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6; nur anwendbar mit FindFolders")
  /**
   * Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6; nur
   * anwendbar mit FindFolders
   */
  private Date xdSFolderLastUpdateTimeFrom;

  @ApiModelProperty(
      example = "2017-07-21T17:32:28Z",
      value =
          "Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6; nur anwendbar mit FindFolders")
  /**
   * Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6; nur anwendbar
   * mit FindFolders
   */
  private Date xdSFolderLastUpdateTimeTo;

  @ApiModelProperty(value = "Folder UUID")
  /** Folder UUID */
  private String xdSFolderEntryUUID;

  /**
   * Liste von EntryUUIDs von SubmissionSets
   *
   * @return xdSSubmissionSetEntryUUID
   */
  @JsonProperty("XDSSubmissionSetEntryUUID")
  public List<String> getXdSSubmissionSetEntryUUID() {
    return xdSSubmissionSetEntryUUID;
  }

  public void setXdSSubmissionSetEntryUUID(List<String> xdSSubmissionSetEntryUUID) {
    this.xdSSubmissionSetEntryUUID = xdSSubmissionSetEntryUUID;
  }

  public QueryMetadata xdSSubmissionSetEntryUUID(List<String> xdSSubmissionSetEntryUUID) {
    this.xdSSubmissionSetEntryUUID = xdSSubmissionSetEntryUUID;
    return this;
  }

  public QueryMetadata addXdSSubmissionSetEntryUUIDItem(String xdSSubmissionSetEntryUUIDItem) {
    this.xdSSubmissionSetEntryUUID.add(xdSSubmissionSetEntryUUIDItem);
    return this;
  }

  /**
   * Liste von uniqueIDs von SubmissionSets
   *
   * @return xdSSubmissionSetUniqueId
   */
  @JsonProperty("XDSSubmissionSetUniqueId")
  public List<String> getXdSSubmissionSetUniqueId() {
    return xdSSubmissionSetUniqueId;
  }

  public void setXdSSubmissionSetUniqueId(List<String> xdSSubmissionSetUniqueId) {
    this.xdSSubmissionSetUniqueId = xdSSubmissionSetUniqueId;
  }

  public QueryMetadata xdSSubmissionSetUniqueId(List<String> xdSSubmissionSetUniqueId) {
    this.xdSSubmissionSetUniqueId = xdSSubmissionSetUniqueId;
    return this;
  }

  public QueryMetadata addXdSSubmissionSetUniqueIdItem(String xdSSubmissionSetUniqueIdItem) {
    this.xdSSubmissionSetUniqueId.add(xdSSubmissionSetUniqueIdItem);
    return this;
  }

  /**
   * Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return xdSSubmissionSetSubmissionTimeFrom
   */
  @JsonProperty("XDSSubmissionSetSubmissionTimeFrom")
  public Date getXdSSubmissionSetSubmissionTimeFrom() {
    return xdSSubmissionSetSubmissionTimeFrom;
  }

  public void setXdSSubmissionSetSubmissionTimeFrom(Date xdSSubmissionSetSubmissionTimeFrom) {
    this.xdSSubmissionSetSubmissionTimeFrom = xdSSubmissionSetSubmissionTimeFrom;
  }

  public QueryMetadata xdSSubmissionSetSubmissionTimeFrom(Date xdSSubmissionSetSubmissionTimeFrom) {
    this.xdSSubmissionSetSubmissionTimeFrom = xdSSubmissionSetSubmissionTimeFrom;
    return this;
  }

  /**
   * XDSDocumentEntryCreationTimeTo, Ende Zeitraum für Suche; date-time notation as defined by RFC
   * 3339, section 5.6
   *
   * @return xdSSubmissionSetSubmissionTimeTo
   */
  @JsonProperty("XDSSubmissionSetSubmissionTimeTo")
  public Date getXdSSubmissionSetSubmissionTimeTo() {
    return xdSSubmissionSetSubmissionTimeTo;
  }

  public void setXdSSubmissionSetSubmissionTimeTo(Date xdSSubmissionSetSubmissionTimeTo) {
    this.xdSSubmissionSetSubmissionTimeTo = xdSSubmissionSetSubmissionTimeTo;
  }

  public QueryMetadata xdSSubmissionSetSubmissionTimeTo(Date xdSSubmissionSetSubmissionTimeTo) {
    this.xdSSubmissionSetSubmissionTimeTo = xdSSubmissionSetSubmissionTimeTo;
    return this;
  }

  /**
   * Liste von EntryUUIDs von Dokumenten
   *
   * @return xdSDocumentEntryEntryUUID
   */
  @JsonProperty("XDSDocumentEntryEntryUUID")
  public List<String> getXdSDocumentEntryEntryUUID() {
    return xdSDocumentEntryEntryUUID;
  }

  public void setXdSDocumentEntryEntryUUID(List<String> xdSDocumentEntryEntryUUID) {
    this.xdSDocumentEntryEntryUUID = xdSDocumentEntryEntryUUID;
  }

  public QueryMetadata xdSDocumentEntryEntryUUID(List<String> xdSDocumentEntryEntryUUID) {
    this.xdSDocumentEntryEntryUUID = xdSDocumentEntryEntryUUID;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryEntryUUIDItem(String xdSDocumentEntryEntryUUIDItem) {
    this.xdSDocumentEntryEntryUUID.add(xdSDocumentEntryEntryUUIDItem);
    return this;
  }

  /**
   * Liste von uniqueIds von Dokumenten
   *
   * @return xdSDocumentEntryUniqueId
   */
  @JsonProperty("XDSDocumentEntryUniqueId")
  public List<String> getXdSDocumentEntryUniqueId() {
    return xdSDocumentEntryUniqueId;
  }

  public void setXdSDocumentEntryUniqueId(List<String> xdSDocumentEntryUniqueId) {
    this.xdSDocumentEntryUniqueId = xdSDocumentEntryUniqueId;
  }

  public QueryMetadata xdSDocumentEntryUniqueId(List<String> xdSDocumentEntryUniqueId) {
    this.xdSDocumentEntryUniqueId = xdSDocumentEntryUniqueId;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryUniqueIdItem(String xdSDocumentEntryUniqueIdItem) {
    this.xdSDocumentEntryUniqueId.add(xdSDocumentEntryUniqueIdItem);
    return this;
  }

  /**
   * Liste von rootDocumentUniqueIds
   *
   * @return xdSDocumentEntryReferenceIdList
   */
  @JsonProperty("XDSDocumentEntryReferenceIdList")
  public List<String> getXdSDocumentEntryReferenceIdList() {
    return xdSDocumentEntryReferenceIdList;
  }

  public void setXdSDocumentEntryReferenceIdList(List<String> xdSDocumentEntryReferenceIdList) {
    this.xdSDocumentEntryReferenceIdList = xdSDocumentEntryReferenceIdList;
  }

  public QueryMetadata xdSDocumentEntryReferenceIdList(
      List<String> xdSDocumentEntryReferenceIdList) {
    this.xdSDocumentEntryReferenceIdList = xdSDocumentEntryReferenceIdList;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryReferenceIdListItem(
      String xdSDocumentEntryReferenceIdListItem) {
    this.xdSDocumentEntryReferenceIdList.add(xdSDocumentEntryReferenceIdListItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryClassCode
   *
   * @return xdSDocumentEntryClassCode
   */
  @JsonProperty("XDSDocumentEntryClassCode")
  public List<String> getXdSDocumentEntryClassCode() {
    return xdSDocumentEntryClassCode;
  }

  public void setXdSDocumentEntryClassCode(List<String> xdSDocumentEntryClassCode) {
    this.xdSDocumentEntryClassCode = xdSDocumentEntryClassCode;
  }

  public QueryMetadata xdSDocumentEntryClassCode(List<String> xdSDocumentEntryClassCode) {
    this.xdSDocumentEntryClassCode = xdSDocumentEntryClassCode;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryClassCodeItem(String xdSDocumentEntryClassCodeItem) {
    this.xdSDocumentEntryClassCode.add(xdSDocumentEntryClassCodeItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryPracticeSettingCode
   *
   * @return xdSDocumentEntryPracticeSettingCode
   */
  @JsonProperty("XDSDocumentEntryPracticeSettingCode")
  public List<String> getXdSDocumentEntryPracticeSettingCode() {
    return xdSDocumentEntryPracticeSettingCode;
  }

  public void setXdSDocumentEntryPracticeSettingCode(
      List<String> xdSDocumentEntryPracticeSettingCode) {
    this.xdSDocumentEntryPracticeSettingCode = xdSDocumentEntryPracticeSettingCode;
  }

  public QueryMetadata xdSDocumentEntryPracticeSettingCode(
      List<String> xdSDocumentEntryPracticeSettingCode) {
    this.xdSDocumentEntryPracticeSettingCode = xdSDocumentEntryPracticeSettingCode;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryPracticeSettingCodeItem(
      String xdSDocumentEntryPracticeSettingCodeItem) {
    this.xdSDocumentEntryPracticeSettingCode.add(xdSDocumentEntryPracticeSettingCodeItem);
    return this;
  }

  /**
   * Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return xdSDocumentEntryCreationTimeFrom
   */
  @JsonProperty("XDSDocumentEntryCreationTimeFrom")
  public Date getXdSDocumentEntryCreationTimeFrom() {
    return xdSDocumentEntryCreationTimeFrom;
  }

  public void setXdSDocumentEntryCreationTimeFrom(Date xdSDocumentEntryCreationTimeFrom) {
    this.xdSDocumentEntryCreationTimeFrom = xdSDocumentEntryCreationTimeFrom;
  }

  public QueryMetadata xdSDocumentEntryCreationTimeFrom(Date xdSDocumentEntryCreationTimeFrom) {
    this.xdSDocumentEntryCreationTimeFrom = xdSDocumentEntryCreationTimeFrom;
    return this;
  }

  /**
   * Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return xdSDocumentEntryCreationTimeTo
   */
  @JsonProperty("XDSDocumentEntryCreationTimeTo")
  public Date getXdSDocumentEntryCreationTimeTo() {
    return xdSDocumentEntryCreationTimeTo;
  }

  public void setXdSDocumentEntryCreationTimeTo(Date xdSDocumentEntryCreationTimeTo) {
    this.xdSDocumentEntryCreationTimeTo = xdSDocumentEntryCreationTimeTo;
  }

  public QueryMetadata xdSDocumentEntryCreationTimeTo(Date xdSDocumentEntryCreationTimeTo) {
    this.xdSDocumentEntryCreationTimeTo = xdSDocumentEntryCreationTimeTo;
    return this;
  }

  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis begonnen wurde. Beginn
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return xdSDocumentEntryServiceStartTimeFrom
   */
  @JsonProperty("XDSDocumentEntryServiceStartTimeFrom")
  public Date getXdSDocumentEntryServiceStartTimeFrom() {
    return xdSDocumentEntryServiceStartTimeFrom;
  }

  public void setXdSDocumentEntryServiceStartTimeFrom(Date xdSDocumentEntryServiceStartTimeFrom) {
    this.xdSDocumentEntryServiceStartTimeFrom = xdSDocumentEntryServiceStartTimeFrom;
  }

  public QueryMetadata xdSDocumentEntryServiceStartTimeFrom(
      Date xdSDocumentEntryServiceStartTimeFrom) {
    this.xdSDocumentEntryServiceStartTimeFrom = xdSDocumentEntryServiceStartTimeFrom;
    return this;
  }

  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis begonnen wurde. Ende
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return xdSDocumentEntryServiceStartTimeTo
   */
  @JsonProperty("XDSDocumentEntryServiceStartTimeTo")
  public Date getXdSDocumentEntryServiceStartTimeTo() {
    return xdSDocumentEntryServiceStartTimeTo;
  }

  public void setXdSDocumentEntryServiceStartTimeTo(Date xdSDocumentEntryServiceStartTimeTo) {
    this.xdSDocumentEntryServiceStartTimeTo = xdSDocumentEntryServiceStartTimeTo;
  }

  public QueryMetadata xdSDocumentEntryServiceStartTimeTo(Date xdSDocumentEntryServiceStartTimeTo) {
    this.xdSDocumentEntryServiceStartTimeTo = xdSDocumentEntryServiceStartTimeTo;
    return this;
  }

  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis beendet wurde. Beginn
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return xdSDocumentEntryServiceStopTimeFrom
   */
  @JsonProperty("XDSDocumentEntryServiceStopTimeFrom")
  public Date getXdSDocumentEntryServiceStopTimeFrom() {
    return xdSDocumentEntryServiceStopTimeFrom;
  }

  public void setXdSDocumentEntryServiceStopTimeFrom(Date xdSDocumentEntryServiceStopTimeFrom) {
    this.xdSDocumentEntryServiceStopTimeFrom = xdSDocumentEntryServiceStopTimeFrom;
  }

  public QueryMetadata xdSDocumentEntryServiceStopTimeFrom(
      Date xdSDocumentEntryServiceStopTimeFrom) {
    this.xdSDocumentEntryServiceStopTimeFrom = xdSDocumentEntryServiceStopTimeFrom;
    return this;
  }

  /**
   * Zeitpunkt, an dem das im Dokument dokumentierte (Behandlungs-)Ereignis beendet wurde. Ende
   * Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6
   *
   * @return xdSDocumentEntryServiceStopTimeTo
   */
  @JsonProperty("XDSDocumentEntryServiceStopTimeTo")
  public Date getXdSDocumentEntryServiceStopTimeTo() {
    return xdSDocumentEntryServiceStopTimeTo;
  }

  public void setXdSDocumentEntryServiceStopTimeTo(Date xdSDocumentEntryServiceStopTimeTo) {
    this.xdSDocumentEntryServiceStopTimeTo = xdSDocumentEntryServiceStopTimeTo;
  }

  public QueryMetadata xdSDocumentEntryServiceStopTimeTo(Date xdSDocumentEntryServiceStopTimeTo) {
    this.xdSDocumentEntryServiceStopTimeTo = xdSDocumentEntryServiceStopTimeTo;
    return this;
  }

  /**
   * Get xdSDocumentEntryHealthcareFacilityTypeCode
   *
   * @return xdSDocumentEntryHealthcareFacilityTypeCode
   */
  @JsonProperty("XDSDocumentEntryHealthcareFacilityTypeCode")
  public List<String> getXdSDocumentEntryHealthcareFacilityTypeCode() {
    return xdSDocumentEntryHealthcareFacilityTypeCode;
  }

  public void setXdSDocumentEntryHealthcareFacilityTypeCode(
      List<String> xdSDocumentEntryHealthcareFacilityTypeCode) {
    this.xdSDocumentEntryHealthcareFacilityTypeCode = xdSDocumentEntryHealthcareFacilityTypeCode;
  }

  public QueryMetadata xdSDocumentEntryHealthcareFacilityTypeCode(
      List<String> xdSDocumentEntryHealthcareFacilityTypeCode) {
    this.xdSDocumentEntryHealthcareFacilityTypeCode = xdSDocumentEntryHealthcareFacilityTypeCode;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryHealthcareFacilityTypeCodeItem(
      String xdSDocumentEntryHealthcareFacilityTypeCodeItem) {
    this.xdSDocumentEntryHealthcareFacilityTypeCode.add(
        xdSDocumentEntryHealthcareFacilityTypeCodeItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryEventCodeList
   *
   * @return xdSDocumentEntryEventCodeList
   */
  @JsonProperty("XDSDocumentEntryEventCodeList")
  public List<String> getXdSDocumentEntryEventCodeList() {
    return xdSDocumentEntryEventCodeList;
  }

  public void setXdSDocumentEntryEventCodeList(List<String> xdSDocumentEntryEventCodeList) {
    this.xdSDocumentEntryEventCodeList = xdSDocumentEntryEventCodeList;
  }

  public QueryMetadata xdSDocumentEntryEventCodeList(List<String> xdSDocumentEntryEventCodeList) {
    this.xdSDocumentEntryEventCodeList = xdSDocumentEntryEventCodeList;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryEventCodeListItem(
      String xdSDocumentEntryEventCodeListItem) {
    this.xdSDocumentEntryEventCodeList.add(xdSDocumentEntryEventCodeListItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryConfidentialityCode
   *
   * @return xdSDocumentEntryConfidentialityCode
   */
  @JsonProperty("XDSDocumentEntryConfidentialityCode")
  public List<String> getXdSDocumentEntryConfidentialityCode() {
    return xdSDocumentEntryConfidentialityCode;
  }

  public void setXdSDocumentEntryConfidentialityCode(
      List<String> xdSDocumentEntryConfidentialityCode) {
    this.xdSDocumentEntryConfidentialityCode = xdSDocumentEntryConfidentialityCode;
  }

  public QueryMetadata xdSDocumentEntryConfidentialityCode(
      List<String> xdSDocumentEntryConfidentialityCode) {
    this.xdSDocumentEntryConfidentialityCode = xdSDocumentEntryConfidentialityCode;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryConfidentialityCodeItem(
      String xdSDocumentEntryConfidentialityCodeItem) {
    this.xdSDocumentEntryConfidentialityCode.add(xdSDocumentEntryConfidentialityCodeItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryAuthorPerson
   *
   * @return xdSDocumentEntryAuthorPerson
   */
  @JsonProperty("XDSDocumentEntryAuthorPerson")
  public List<String> getXdSDocumentEntryAuthorPerson() {
    return xdSDocumentEntryAuthorPerson;
  }

  public void setXdSDocumentEntryAuthorPerson(List<String> xdSDocumentEntryAuthorPerson) {
    this.xdSDocumentEntryAuthorPerson = xdSDocumentEntryAuthorPerson;
  }

  public QueryMetadata xdSDocumentEntryAuthorPerson(List<String> xdSDocumentEntryAuthorPerson) {
    this.xdSDocumentEntryAuthorPerson = xdSDocumentEntryAuthorPerson;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryAuthorPersonItem(
      String xdSDocumentEntryAuthorPersonItem) {
    this.xdSDocumentEntryAuthorPerson.add(xdSDocumentEntryAuthorPersonItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryFormatCode
   *
   * @return xdSDocumentEntryFormatCode
   */
  @JsonProperty("XDSDocumentEntryFormatCode")
  public List<String> getXdSDocumentEntryFormatCode() {
    return xdSDocumentEntryFormatCode;
  }

  public void setXdSDocumentEntryFormatCode(List<String> xdSDocumentEntryFormatCode) {
    this.xdSDocumentEntryFormatCode = xdSDocumentEntryFormatCode;
  }

  public QueryMetadata xdSDocumentEntryFormatCode(List<String> xdSDocumentEntryFormatCode) {
    this.xdSDocumentEntryFormatCode = xdSDocumentEntryFormatCode;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryFormatCodeItem(String xdSDocumentEntryFormatCodeItem) {
    this.xdSDocumentEntryFormatCode.add(xdSDocumentEntryFormatCodeItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryTypeCode
   *
   * @return xdSDocumentEntryTypeCode
   */
  @JsonProperty("XDSDocumentEntryTypeCode")
  public List<String> getXdSDocumentEntryTypeCode() {
    return xdSDocumentEntryTypeCode;
  }

  public void setXdSDocumentEntryTypeCode(List<String> xdSDocumentEntryTypeCode) {
    this.xdSDocumentEntryTypeCode = xdSDocumentEntryTypeCode;
  }

  public QueryMetadata xdSDocumentEntryTypeCode(List<String> xdSDocumentEntryTypeCode) {
    this.xdSDocumentEntryTypeCode = xdSDocumentEntryTypeCode;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryTypeCodeItem(String xdSDocumentEntryTypeCodeItem) {
    this.xdSDocumentEntryTypeCode.add(xdSDocumentEntryTypeCodeItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryTitle
   *
   * @return xdSDocumentEntryTitle
   */
  @JsonProperty("XDSDocumentEntryTitle")
  public List<String> getXdSDocumentEntryTitle() {
    return xdSDocumentEntryTitle;
  }

  public void setXdSDocumentEntryTitle(List<String> xdSDocumentEntryTitle) {
    this.xdSDocumentEntryTitle = xdSDocumentEntryTitle;
  }

  public QueryMetadata xdSDocumentEntryTitle(List<String> xdSDocumentEntryTitle) {
    this.xdSDocumentEntryTitle = xdSDocumentEntryTitle;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryTitleItem(String xdSDocumentEntryTitleItem) {
    this.xdSDocumentEntryTitle.add(xdSDocumentEntryTitleItem);
    return this;
  }

  /**
   * Get xdSDocumentEntryAuthorInstitution
   *
   * @return xdSDocumentEntryAuthorInstitution
   */
  @JsonProperty("XDSDocumentEntryAuthorInstitution")
  public List<String> getXdSDocumentEntryAuthorInstitution() {
    return xdSDocumentEntryAuthorInstitution;
  }

  public void setXdSDocumentEntryAuthorInstitution(List<String> xdSDocumentEntryAuthorInstitution) {
    this.xdSDocumentEntryAuthorInstitution = xdSDocumentEntryAuthorInstitution;
  }

  public QueryMetadata xdSDocumentEntryAuthorInstitution(
      List<String> xdSDocumentEntryAuthorInstitution) {
    this.xdSDocumentEntryAuthorInstitution = xdSDocumentEntryAuthorInstitution;
    return this;
  }

  public QueryMetadata addXdSDocumentEntryAuthorInstitutionItem(
      String xdSDocumentEntryAuthorInstitutionItem) {
    this.xdSDocumentEntryAuthorInstitution.add(xdSDocumentEntryAuthorInstitutionItem);
    return this;
  }

  /**
   * Status des Dokumentes
   *
   * @return xdSDocumentEntryStatus
   */
  @JsonProperty("XDSDocumentEntryStatus")
  public String getXdSDocumentEntryStatus() {
    return xdSDocumentEntryStatus;
  }

  public void setXdSDocumentEntryStatus(String xdSDocumentEntryStatus) {
    this.xdSDocumentEntryStatus = xdSDocumentEntryStatus;
  }

  public QueryMetadata xdSDocumentEntryStatus(String xdSDocumentEntryStatus) {
    this.xdSDocumentEntryStatus = xdSDocumentEntryStatus;
    return this;
  }

  /**
   * Folder Unique Id
   *
   * @return xdSFolderUniqueId
   */
  @JsonProperty("XDSFolderUniqueId")
  public String getXdSFolderUniqueId() {
    return xdSFolderUniqueId;
  }

  public void setXdSFolderUniqueId(String xdSFolderUniqueId) {
    this.xdSFolderUniqueId = xdSFolderUniqueId;
  }

  public QueryMetadata xdSFolderUniqueId(String xdSFolderUniqueId) {
    this.xdSFolderUniqueId = xdSFolderUniqueId;
    return this;
  }

  /**
   * Status der Ordner; nur anwendbar mit FindFolders
   *
   * @return xdSFolderStatus
   */
  @JsonProperty("XDSFolderStatus")
  public String getXdSFolderStatus() {
    return xdSFolderStatus;
  }

  public void setXdSFolderStatus(String xdSFolderStatus) {
    this.xdSFolderStatus = xdSFolderStatus;
  }

  public QueryMetadata xdSFolderStatus(String xdSFolderStatus) {
    this.xdSFolderStatus = xdSFolderStatus;
    return this;
  }

  /**
   * Liste von Codes (z.B. &#39;childrecord&#39;, &#39;patientdoc&#39;)
   *
   * @return xdSFolderCodeList
   */
  @JsonProperty("XDSFolderCodeList")
  public List<String> getXdSFolderCodeList() {
    return xdSFolderCodeList;
  }

  public void setXdSFolderCodeList(List<String> xdSFolderCodeList) {
    this.xdSFolderCodeList = xdSFolderCodeList;
  }

  public QueryMetadata xdSFolderCodeList(List<String> xdSFolderCodeList) {
    this.xdSFolderCodeList = xdSFolderCodeList;
    return this;
  }

  public QueryMetadata addXdSFolderCodeListItem(String xdSFolderCodeListItem) {
    this.xdSFolderCodeList.add(xdSFolderCodeListItem);
    return this;
  }

  /**
   * Beginn Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6; nur
   * anwendbar mit FindFolders
   *
   * @return xdSFolderLastUpdateTimeFrom
   */
  @JsonProperty("XDSFolderLastUpdateTimeFrom")
  public Date getXdSFolderLastUpdateTimeFrom() {
    return xdSFolderLastUpdateTimeFrom;
  }

  public void setXdSFolderLastUpdateTimeFrom(Date xdSFolderLastUpdateTimeFrom) {
    this.xdSFolderLastUpdateTimeFrom = xdSFolderLastUpdateTimeFrom;
  }

  public QueryMetadata xdSFolderLastUpdateTimeFrom(Date xdSFolderLastUpdateTimeFrom) {
    this.xdSFolderLastUpdateTimeFrom = xdSFolderLastUpdateTimeFrom;
    return this;
  }

  /**
   * Ende Zeitraum für Suche; date-time notation as defined by RFC 3339, section 5.6; nur anwendbar
   * mit FindFolders
   *
   * @return xdSFolderLastUpdateTimeTo
   */
  @JsonProperty("XDSFolderLastUpdateTimeTo")
  public Date getXdSFolderLastUpdateTimeTo() {
    return xdSFolderLastUpdateTimeTo;
  }

  public void setXdSFolderLastUpdateTimeTo(Date xdSFolderLastUpdateTimeTo) {
    this.xdSFolderLastUpdateTimeTo = xdSFolderLastUpdateTimeTo;
  }

  public QueryMetadata xdSFolderLastUpdateTimeTo(Date xdSFolderLastUpdateTimeTo) {
    this.xdSFolderLastUpdateTimeTo = xdSFolderLastUpdateTimeTo;
    return this;
  }

  /**
   * Folder UUID
   *
   * @return xdSFolderEntryUUID
   */
  @JsonProperty("XDSFolderEntryUUID")
  public String getXdSFolderEntryUUID() {
    return xdSFolderEntryUUID;
  }

  public void setXdSFolderEntryUUID(String xdSFolderEntryUUID) {
    this.xdSFolderEntryUUID = xdSFolderEntryUUID;
  }

  public QueryMetadata xdSFolderEntryUUID(String xdSFolderEntryUUID) {
    this.xdSFolderEntryUUID = xdSFolderEntryUUID;
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
    QueryMetadata queryMetadata = (QueryMetadata) o;
    return Objects.equals(this.xdSSubmissionSetEntryUUID, queryMetadata.xdSSubmissionSetEntryUUID)
        && Objects.equals(this.xdSSubmissionSetUniqueId, queryMetadata.xdSSubmissionSetUniqueId)
        && Objects.equals(
            this.xdSSubmissionSetSubmissionTimeFrom,
            queryMetadata.xdSSubmissionSetSubmissionTimeFrom)
        && Objects.equals(
            this.xdSSubmissionSetSubmissionTimeTo, queryMetadata.xdSSubmissionSetSubmissionTimeTo)
        && Objects.equals(this.xdSDocumentEntryEntryUUID, queryMetadata.xdSDocumentEntryEntryUUID)
        && Objects.equals(this.xdSDocumentEntryUniqueId, queryMetadata.xdSDocumentEntryUniqueId)
        && Objects.equals(
            this.xdSDocumentEntryReferenceIdList, queryMetadata.xdSDocumentEntryReferenceIdList)
        && Objects.equals(this.xdSDocumentEntryClassCode, queryMetadata.xdSDocumentEntryClassCode)
        && Objects.equals(
            this.xdSDocumentEntryPracticeSettingCode,
            queryMetadata.xdSDocumentEntryPracticeSettingCode)
        && Objects.equals(
            this.xdSDocumentEntryCreationTimeFrom, queryMetadata.xdSDocumentEntryCreationTimeFrom)
        && Objects.equals(
            this.xdSDocumentEntryCreationTimeTo, queryMetadata.xdSDocumentEntryCreationTimeTo)
        && Objects.equals(
            this.xdSDocumentEntryServiceStartTimeFrom,
            queryMetadata.xdSDocumentEntryServiceStartTimeFrom)
        && Objects.equals(
            this.xdSDocumentEntryServiceStartTimeTo,
            queryMetadata.xdSDocumentEntryServiceStartTimeTo)
        && Objects.equals(
            this.xdSDocumentEntryServiceStopTimeFrom,
            queryMetadata.xdSDocumentEntryServiceStopTimeFrom)
        && Objects.equals(
            this.xdSDocumentEntryServiceStopTimeTo, queryMetadata.xdSDocumentEntryServiceStopTimeTo)
        && Objects.equals(
            this.xdSDocumentEntryHealthcareFacilityTypeCode,
            queryMetadata.xdSDocumentEntryHealthcareFacilityTypeCode)
        && Objects.equals(
            this.xdSDocumentEntryEventCodeList, queryMetadata.xdSDocumentEntryEventCodeList)
        && Objects.equals(
            this.xdSDocumentEntryConfidentialityCode,
            queryMetadata.xdSDocumentEntryConfidentialityCode)
        && Objects.equals(
            this.xdSDocumentEntryAuthorPerson, queryMetadata.xdSDocumentEntryAuthorPerson)
        && Objects.equals(this.xdSDocumentEntryFormatCode, queryMetadata.xdSDocumentEntryFormatCode)
        && Objects.equals(this.xdSDocumentEntryTypeCode, queryMetadata.xdSDocumentEntryTypeCode)
        && Objects.equals(this.xdSDocumentEntryTitle, queryMetadata.xdSDocumentEntryTitle)
        && Objects.equals(
            this.xdSDocumentEntryAuthorInstitution, queryMetadata.xdSDocumentEntryAuthorInstitution)
        && Objects.equals(this.xdSDocumentEntryStatus, queryMetadata.xdSDocumentEntryStatus)
        && Objects.equals(this.xdSFolderUniqueId, queryMetadata.xdSFolderUniqueId)
        && Objects.equals(this.xdSFolderStatus, queryMetadata.xdSFolderStatus)
        && Objects.equals(this.xdSFolderCodeList, queryMetadata.xdSFolderCodeList)
        && Objects.equals(
            this.xdSFolderLastUpdateTimeFrom, queryMetadata.xdSFolderLastUpdateTimeFrom)
        && Objects.equals(this.xdSFolderLastUpdateTimeTo, queryMetadata.xdSFolderLastUpdateTimeTo)
        && Objects.equals(this.xdSFolderEntryUUID, queryMetadata.xdSFolderEntryUUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        xdSSubmissionSetEntryUUID,
        xdSSubmissionSetUniqueId,
        xdSSubmissionSetSubmissionTimeFrom,
        xdSSubmissionSetSubmissionTimeTo,
        xdSDocumentEntryEntryUUID,
        xdSDocumentEntryUniqueId,
        xdSDocumentEntryReferenceIdList,
        xdSDocumentEntryClassCode,
        xdSDocumentEntryPracticeSettingCode,
        xdSDocumentEntryCreationTimeFrom,
        xdSDocumentEntryCreationTimeTo,
        xdSDocumentEntryServiceStartTimeFrom,
        xdSDocumentEntryServiceStartTimeTo,
        xdSDocumentEntryServiceStopTimeFrom,
        xdSDocumentEntryServiceStopTimeTo,
        xdSDocumentEntryHealthcareFacilityTypeCode,
        xdSDocumentEntryEventCodeList,
        xdSDocumentEntryConfidentialityCode,
        xdSDocumentEntryAuthorPerson,
        xdSDocumentEntryFormatCode,
        xdSDocumentEntryTypeCode,
        xdSDocumentEntryTitle,
        xdSDocumentEntryAuthorInstitution,
        xdSDocumentEntryStatus,
        xdSFolderUniqueId,
        xdSFolderStatus,
        xdSFolderCodeList,
        xdSFolderLastUpdateTimeFrom,
        xdSFolderLastUpdateTimeTo,
        xdSFolderEntryUUID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryMetadata {\n");

    sb.append("    xdSSubmissionSetEntryUUID: ")
        .append(toIndentedString(xdSSubmissionSetEntryUUID))
        .append("\n");
    sb.append("    xdSSubmissionSetUniqueId: ")
        .append(toIndentedString(xdSSubmissionSetUniqueId))
        .append("\n");
    sb.append("    xdSSubmissionSetSubmissionTimeFrom: ")
        .append(toIndentedString(xdSSubmissionSetSubmissionTimeFrom))
        .append("\n");
    sb.append("    xdSSubmissionSetSubmissionTimeTo: ")
        .append(toIndentedString(xdSSubmissionSetSubmissionTimeTo))
        .append("\n");
    sb.append("    xdSDocumentEntryEntryUUID: ")
        .append(toIndentedString(xdSDocumentEntryEntryUUID))
        .append("\n");
    sb.append("    xdSDocumentEntryUniqueId: ")
        .append(toIndentedString(xdSDocumentEntryUniqueId))
        .append("\n");
    sb.append("    xdSDocumentEntryReferenceIdList: ")
        .append(toIndentedString(xdSDocumentEntryReferenceIdList))
        .append("\n");
    sb.append("    xdSDocumentEntryClassCode: ")
        .append(toIndentedString(xdSDocumentEntryClassCode))
        .append("\n");
    sb.append("    xdSDocumentEntryPracticeSettingCode: ")
        .append(toIndentedString(xdSDocumentEntryPracticeSettingCode))
        .append("\n");
    sb.append("    xdSDocumentEntryCreationTimeFrom: ")
        .append(toIndentedString(xdSDocumentEntryCreationTimeFrom))
        .append("\n");
    sb.append("    xdSDocumentEntryCreationTimeTo: ")
        .append(toIndentedString(xdSDocumentEntryCreationTimeTo))
        .append("\n");
    sb.append("    xdSDocumentEntryServiceStartTimeFrom: ")
        .append(toIndentedString(xdSDocumentEntryServiceStartTimeFrom))
        .append("\n");
    sb.append("    xdSDocumentEntryServiceStartTimeTo: ")
        .append(toIndentedString(xdSDocumentEntryServiceStartTimeTo))
        .append("\n");
    sb.append("    xdSDocumentEntryServiceStopTimeFrom: ")
        .append(toIndentedString(xdSDocumentEntryServiceStopTimeFrom))
        .append("\n");
    sb.append("    xdSDocumentEntryServiceStopTimeTo: ")
        .append(toIndentedString(xdSDocumentEntryServiceStopTimeTo))
        .append("\n");
    sb.append("    xdSDocumentEntryHealthcareFacilityTypeCode: ")
        .append(toIndentedString(xdSDocumentEntryHealthcareFacilityTypeCode))
        .append("\n");
    sb.append("    xdSDocumentEntryEventCodeList: ")
        .append(toIndentedString(xdSDocumentEntryEventCodeList))
        .append("\n");
    sb.append("    xdSDocumentEntryConfidentialityCode: ")
        .append(toIndentedString(xdSDocumentEntryConfidentialityCode))
        .append("\n");
    sb.append("    xdSDocumentEntryAuthorPerson: ")
        .append(toIndentedString(xdSDocumentEntryAuthorPerson))
        .append("\n");
    sb.append("    xdSDocumentEntryFormatCode: ")
        .append(toIndentedString(xdSDocumentEntryFormatCode))
        .append("\n");
    sb.append("    xdSDocumentEntryTypeCode: ")
        .append(toIndentedString(xdSDocumentEntryTypeCode))
        .append("\n");
    sb.append("    xdSDocumentEntryTitle: ")
        .append(toIndentedString(xdSDocumentEntryTitle))
        .append("\n");
    sb.append("    xdSDocumentEntryAuthorInstitution: ")
        .append(toIndentedString(xdSDocumentEntryAuthorInstitution))
        .append("\n");
    sb.append("    xdSDocumentEntryStatus: ")
        .append(toIndentedString(xdSDocumentEntryStatus))
        .append("\n");
    sb.append("    xdSFolderUniqueId: ").append(toIndentedString(xdSFolderUniqueId)).append("\n");
    sb.append("    xdSFolderStatus: ").append(toIndentedString(xdSFolderStatus)).append("\n");
    sb.append("    xdSFolderCodeList: ").append(toIndentedString(xdSFolderCodeList)).append("\n");
    sb.append("    xdSFolderLastUpdateTimeFrom: ")
        .append(toIndentedString(xdSFolderLastUpdateTimeFrom))
        .append("\n");
    sb.append("    xdSFolderLastUpdateTimeTo: ")
        .append(toIndentedString(xdSFolderLastUpdateTimeTo))
        .append("\n");
    sb.append("    xdSFolderEntryUUID: ").append(toIndentedString(xdSFolderEntryUUID)).append("\n");
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
