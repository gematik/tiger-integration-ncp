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

package de.gematik.test.ncp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.gematik.ncpeh.api.NcpehSimulatorApi;
import de.gematik.test.ncp.reporting.NcpehContext;
import de.gematik.test.ncp.reporting.NcpehTimeLoggingHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.web.util.UriComponentsBuilder;

@UtilityClass
public class GeneralFactory {

  @Getter
  private static final ObjectMapper objectMapper =
      new ObjectMapper()
          .registerModule(new JodaModule())
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
          .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
          .setSerializationInclusion(JsonInclude.Include.NON_ABSENT);

  /** A Jackson JSON provider configured with custom serialization and deserialization settings. */
  private static final JacksonJsonProvider JACKSON_JSON_PROVIDER =
      new JacksonJsonProvider(objectMapper);

  /**
   * Creates a JAX-RS client proxy for the specified proxy class and server configuration.
   *
   * @param <T> the type of the proxy class
   * @param proxyClass the class of the proxy to create
   * @param config the server configuration
   * @return a JAX-RS client proxy instance
   */
  public static <T> T createJAXRSClientProxy(
      @NonNull final Class<T> proxyClass, @NonNull final ExternalServerConfig config) {
    return JAXRSClientFactory.create(
        UriComponentsBuilder.newInstance()
            .scheme(config.getScheme())
            .host(config.getHostname())
            .path(config.getBasePath())
            .build()
            .toUriString(),
        proxyClass,
        List.of(JACKSON_JSON_PROVIDER));
  }

  /**
   * Creates a dynamic proxy that logs the execution time of methods for the specified target
   * object.
   *
   * @param target the target object to proxy
   * @param interfaces the interfaces that the proxy should implement
   * @return a proxy instance that logs method execution times
   */
  public static NcpehSimulatorApi createNcpehTimeLoggingProxy(
      final NcpehSimulatorApi target,
      final NcpehContext ncpehContext,
      final Class<?>... interfaces) {
    return (NcpehSimulatorApi)
        Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            interfaces,
            new NcpehTimeLoggingHandler(target, ncpehContext));
  }
}
