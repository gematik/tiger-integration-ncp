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

package de.gematik.test.ncp.gen.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * provides Set of Meta data for author. In case of DocumentEntry is this the author of the
 * document, in case of Submission Set the providing person or system.
 */
@ApiModel(
    description =
        "provides Set of Meta data for author. In case of DocumentEntry is this the author of the document, in case of Submission Set the providing person or system.")
public class Author {

  @ApiModelProperty(
      example = "165746304",
      value =
          "authorPerson, for LE \"Lebenslange Identifikationsnummer eines Arztes\", for insurant the insurantID")
  /**
   * authorPerson, for LE \"Lebenslange Identifikationsnummer eines Arztes\", for insurant the
   * insurantID
   */
  private String identifier;

  @ApiModelProperty(example = "Weber", value = "authorPerson, surname")
  /** authorPerson, surname */
  private String familyName;

  @ApiModelProperty(example = "Thilo", value = "authorPerson, given name")
  /** authorPerson, given name */
  private String givenName;

  @ApiModelProperty(value = "authorPerson, other given name")
  /** authorPerson, other given name */
  private String otherName;

  @ApiModelProperty(value = "authorPerson, name affix")
  /** authorPerson, name affix */
  private String nameAffix;

  @ApiModelProperty(example = "Dr.", value = "authorPerson, title")
  /** authorPerson, title */
  private String title;

  @ApiModelProperty(value = "")
  @Valid
  private List<@Valid AuthorInstitution> authorInstitution = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> authorRole = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> authorSpecialty = new ArrayList<>();

  @ApiModelProperty(value = "")
  private List<String> authorTelecommunication = new ArrayList<>();

  /**
   * authorPerson, for LE \&quot;Lebenslange Identifikationsnummer eines Arztes\&quot;, for insurant
   * the insurantID
   *
   * @return identifier
   */
  @JsonProperty("identifier")
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Author identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  /**
   * authorPerson, surname
   *
   * @return familyName
   */
  @JsonProperty("familyName")
  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public Author familyName(String familyName) {
    this.familyName = familyName;
    return this;
  }

  /**
   * authorPerson, given name
   *
   * @return givenName
   */
  @JsonProperty("givenName")
  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public Author givenName(String givenName) {
    this.givenName = givenName;
    return this;
  }

  /**
   * authorPerson, other given name
   *
   * @return otherName
   */
  @JsonProperty("otherName")
  public String getOtherName() {
    return otherName;
  }

  public void setOtherName(String otherName) {
    this.otherName = otherName;
  }

  public Author otherName(String otherName) {
    this.otherName = otherName;
    return this;
  }

  /**
   * authorPerson, name affix
   *
   * @return nameAffix
   */
  @JsonProperty("nameAffix")
  public String getNameAffix() {
    return nameAffix;
  }

  public void setNameAffix(String nameAffix) {
    this.nameAffix = nameAffix;
  }

  public Author nameAffix(String nameAffix) {
    this.nameAffix = nameAffix;
    return this;
  }

  /**
   * authorPerson, title
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

  public Author title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get authorInstitution
   *
   * @return authorInstitution
   */
  @JsonProperty("authorInstitution")
  public List<@Valid AuthorInstitution> getAuthorInstitution() {
    return authorInstitution;
  }

  public void setAuthorInstitution(List<@Valid AuthorInstitution> authorInstitution) {
    this.authorInstitution = authorInstitution;
  }

  public Author authorInstitution(List<@Valid AuthorInstitution> authorInstitution) {
    this.authorInstitution = authorInstitution;
    return this;
  }

  public Author addAuthorInstitutionItem(AuthorInstitution authorInstitutionItem) {
    this.authorInstitution.add(authorInstitutionItem);
    return this;
  }

  /**
   * Get authorRole
   *
   * @return authorRole
   */
  @JsonProperty("authorRole")
  public List<String> getAuthorRole() {
    return authorRole;
  }

  public void setAuthorRole(List<String> authorRole) {
    this.authorRole = authorRole;
  }

  public Author authorRole(List<String> authorRole) {
    this.authorRole = authorRole;
    return this;
  }

  public Author addAuthorRoleItem(String authorRoleItem) {
    this.authorRole.add(authorRoleItem);
    return this;
  }

  /**
   * Get authorSpecialty
   *
   * @return authorSpecialty
   */
  @JsonProperty("authorSpecialty")
  public List<String> getAuthorSpecialty() {
    return authorSpecialty;
  }

  public void setAuthorSpecialty(List<String> authorSpecialty) {
    this.authorSpecialty = authorSpecialty;
  }

  public Author authorSpecialty(List<String> authorSpecialty) {
    this.authorSpecialty = authorSpecialty;
    return this;
  }

  public Author addAuthorSpecialtyItem(String authorSpecialtyItem) {
    this.authorSpecialty.add(authorSpecialtyItem);
    return this;
  }

  /**
   * Get authorTelecommunication
   *
   * @return authorTelecommunication
   */
  @JsonProperty("authorTelecommunication")
  public List<String> getAuthorTelecommunication() {
    return authorTelecommunication;
  }

  public void setAuthorTelecommunication(List<String> authorTelecommunication) {
    this.authorTelecommunication = authorTelecommunication;
  }

  public Author authorTelecommunication(List<String> authorTelecommunication) {
    this.authorTelecommunication = authorTelecommunication;
    return this;
  }

  public Author addAuthorTelecommunicationItem(String authorTelecommunicationItem) {
    this.authorTelecommunication.add(authorTelecommunicationItem);
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
    Author author = (Author) o;
    return Objects.equals(this.identifier, author.identifier)
        && Objects.equals(this.familyName, author.familyName)
        && Objects.equals(this.givenName, author.givenName)
        && Objects.equals(this.otherName, author.otherName)
        && Objects.equals(this.nameAffix, author.nameAffix)
        && Objects.equals(this.title, author.title)
        && Objects.equals(this.authorInstitution, author.authorInstitution)
        && Objects.equals(this.authorRole, author.authorRole)
        && Objects.equals(this.authorSpecialty, author.authorSpecialty)
        && Objects.equals(this.authorTelecommunication, author.authorTelecommunication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        identifier,
        familyName,
        givenName,
        otherName,
        nameAffix,
        title,
        authorInstitution,
        authorRole,
        authorSpecialty,
        authorTelecommunication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Author {\n");

    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
    sb.append("    familyName: ").append(toIndentedString(familyName)).append("\n");
    sb.append("    givenName: ").append(toIndentedString(givenName)).append("\n");
    sb.append("    otherName: ").append(toIndentedString(otherName)).append("\n");
    sb.append("    nameAffix: ").append(toIndentedString(nameAffix)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    authorInstitution: ").append(toIndentedString(authorInstitution)).append("\n");
    sb.append("    authorRole: ").append(toIndentedString(authorRole)).append("\n");
    sb.append("    authorSpecialty: ").append(toIndentedString(authorSpecialty)).append("\n");
    sb.append("    authorTelecommunication: ")
        .append(toIndentedString(authorTelecommunication))
        .append("\n");
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
