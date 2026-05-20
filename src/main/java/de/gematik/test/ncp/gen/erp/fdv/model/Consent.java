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

package de.gematik.test.ncp.gen.erp.fdv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.*;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.Objects;

@JsonTypeName("Consent")
@jakarta.annotation.Generated(
    value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen",
    comments = "Generator version: 7.10.0")
public class Consent {
  private String kvnr;
  private ConsentCategory category;
  private OffsetDateTime dateTime;

  protected Consent(ConsentBuilder<?, ?> b) {
    this.kvnr = b.kvnr;
    this.category = b.category;
    this.dateTime = b.dateTime;
  }

  public Consent() {}

  /** */
  public Consent kvnr(String kvnr) {
    this.kvnr = kvnr;
    return this;
  }

  @ApiModelProperty(example = "A000500015", required = true, value = "")
  @JsonProperty("kvnr")
  public String getKvnr() {
    return kvnr;
  }

  @JsonProperty("kvnr")
  public void setKvnr(String kvnr) {
    this.kvnr = kvnr;
  }

  /** */
  public Consent category(ConsentCategory category) {
    this.category = category;
    return this;
  }

  @ApiModelProperty(required = true, value = "")
  @JsonProperty("category")
  public ConsentCategory getCategory() {
    return category;
  }

  @JsonProperty("category")
  public void setCategory(ConsentCategory category) {
    this.category = category;
  }

  /** */
  public Consent dateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
    return this;
  }

  @ApiModelProperty(example = "2020-03-12T18:01:10Z", required = true, value = "")
  @JsonProperty("dateTime")
  public OffsetDateTime getDateTime() {
    return dateTime;
  }

  @JsonProperty("dateTime")
  public void setDateTime(OffsetDateTime dateTime) {
    this.dateTime = dateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Consent consent = (Consent) o;
    return Objects.equals(this.kvnr, consent.kvnr)
        && Objects.equals(this.category, consent.category)
        && Objects.equals(this.dateTime, consent.dateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kvnr, category, dateTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Consent {\n");

    sb.append("    kvnr: ").append(toIndentedString(kvnr)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    dateTime: ").append(toIndentedString(dateTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public static ConsentBuilder<?, ?> builder() {
    return new ConsentBuilderImpl();
  }

  private static class ConsentBuilderImpl extends ConsentBuilder<Consent, ConsentBuilderImpl> {

    @Override
    protected ConsentBuilderImpl self() {
      return this;
    }

    @Override
    public Consent build() {
      return new Consent(this);
    }
  }

  public abstract static class ConsentBuilder<C extends Consent, B extends ConsentBuilder<C, B>> {
    private String kvnr;
    private ConsentCategory category;
    private OffsetDateTime dateTime;

    protected abstract B self();

    public abstract C build();

    public B kvnr(String kvnr) {
      this.kvnr = kvnr;
      return self();
    }

    public B category(ConsentCategory category) {
      this.category = category;
      return self();
    }

    public B dateTime(OffsetDateTime dateTime) {
      this.dateTime = dateTime;
      return self();
    }
  }
}
