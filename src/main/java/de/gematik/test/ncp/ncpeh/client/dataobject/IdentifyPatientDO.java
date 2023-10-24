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

package de.gematik.test.ncp.ncpeh.client.dataobject;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.springframework.http.HttpStatus;

/**
 * First draft of a data object to hold the data needed for patient identification.<br>
 * Note: This might be subject to change in the future.
 */
public record IdentifyPatientDO(
    HttpStatus ncpehResponseStatus,
    HttpRequestData<PRPAIN201305UV02> ncpehFdRequest,
    HttpResponseData<PRPAIN201306UV02> ncpehFdResponse)
    implements NcpehInterfaceResponse<PRPAIN201305UV02, PRPAIN201306UV02> {}
