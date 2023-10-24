/*
 * Copyright 2023 gematik GmbH
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

package de.gematik.test.ncp.ncpeh.data;

import de.gematik.ncpeh.api.common.BaseAssertionProfile;
import de.gematik.ncpeh.api.common.EuCountryCode;
import de.gematik.ncpeh.api.common.IdaAssertionProfile;
import de.gematik.ncpeh.api.common.PatientId;
import de.gematik.ncpeh.api.request.RequestBase;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.Builder;

/**
 * Abstract base builder class to build the common elements of the different request types of the
 * {@link de.gematik.ncpeh.api.NcpehSimulatorApi}, which are {@link RequestBase} & {@link
 * PatientId}.<br>
 * Warning: There is a caveat, when it comes to usage. The chained setters of this class, will
 * always return this class and not the actual builder class extending from this class. So order is
 * of importance:
 *
 * <pre>{@code
 * @Accessors(fluent = true)
 * @Data
 * class A extends RequestBaseBuilder<RequestType> {
 *   private String name;
 *   ...
 *   // Must implement build function
 * }
 * ...
 * public void functionUsingBuilder(String kvnr, String name) {
 *   RequestType reqA = new A().kvnr(kvnr).name(name).build(); // This won't work, because RequestBaseBuilder has no 'name' field
 *   RequestType reqB = new A().name(name).kvnr(kvnr).build(); // This will work!
 * }
 * }</pre>
 *
 * Summed Up: Fill fields defined in RequestBaseBuilder last!<br>
 * Note: Thus far only mandatory fields are set. Further fields might need to be added in the
 * future.
 *
 * @param <R> Class type of the class the actual builder (inheriting from this class) will generate,
 *     when its build method is called
 */
@Accessors(fluent = true)
@Data
public abstract class RequestBaseBuilder<R> implements Builder<R> {

  private EuCountryCode euCountryCode;

  private String idaAssertionProfileName;

  private String kvnr;

  private String oidAssigningAuthority;

  private String accessCode;

  public RequestBase buildRequestBase() {
    return new RequestBase(
        euCountryCode,
        new IdaAssertionProfile(
            new BaseAssertionProfile(idaAssertionProfileName, null, null, null, null), null, null));
  }

  public PatientId buildPatientId() {
    return new PatientId(kvnr, oidAssigningAuthority);
  }
}
