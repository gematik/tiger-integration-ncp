/*
 * Copyright (c) 2024. gematik GmbH
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

package de.gematik.test.ncp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.gematik.test.ncp.util.Utils;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.joda.time.DateTime;

@UtilityClass
public class GeneralFactory {

  public static final JacksonJsonProvider JACKSON_JSON_PROVIDER = createJacksonJsonProvider();

  private static JacksonJsonProvider createJacksonJsonProvider() {
    final var provider =
        new JacksonJsonProvider()
            .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    provider
        .locateMapper(DateTime.class, MediaType.APPLICATION_JSON_TYPE)
        .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
        //        .registerModule(new JodaModule())
        .registerModule(new JavaTimeModule());

    return provider;
  }

  public static <T> T createJAXRSClientProxy(
      @NonNull final Class<T> proxyClass, final ExternalServerConfig config) {
    return JAXRSClientFactory.create(
        Utils.buildUri(config.getHostname(), config.getBasePath()).toString(),
        proxyClass,
        List.of(JACKSON_JSON_PROVIDER));
  }
}
