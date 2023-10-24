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

package de.gematik.test.ncp.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.gematik.test.ncp.util.EpkaProcessor;
import de.gematik.test.ncp.util.Utils;
import de.gematik.test.tiger.common.config.TigerConfigurationException;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.util.ResourceUtils;

/**
 * Singleton class responsible for loading data from the testdata configuration and being the single
 * point of truth for configuration keys used in the testdata configuration
 */
@Slf4j
@Accessors(fluent = true)
public class Testdata {

  public static final String TESTDATA_BASEKEY = "testdata";

  public static final String PATIENTS_DATA_KEY = TESTDATA_BASEKEY + ".patients";

  public static final String EU_PRACTITIONERS_KEY = TESTDATA_BASEKEY + ".practitioners.eu";

  public static final String EPKA_TEMPLATES_CONFIG_PATH = TESTDATA_BASEKEY + ".epka.templates";

  public static final String NCPEHSIM_TESTDATA_PROFILES_KEY = TESTDATA_BASEKEY + ".profiles";

  public static final String CONFIG_BASEKEY = TESTDATA_BASEKEY + ".config";

  public static final String NAMES_CONFIG_BASEKEY = CONFIG_BASEKEY + ".names";

  public static final String TITLES_CONFIG_KEY = NAMES_CONFIG_BASEKEY + ".titles";

  public static final String PREFIXES_CONFIG_KEY = NAMES_CONFIG_BASEKEY + ".prefixes";

  public static final String RECORD_SYSTEMS_CONFIG_KEY = TESTDATA_BASEKEY + ".recordsystems";

  public static final String KONNEKTOR_ADDRESSES_CONFIG_KEY = TESTDATA_BASEKEY + ".konnektor";

  public static final String DO_NOT_FAIL_CONFIG_KEY = CONFIG_BASEKEY + ".doNotFail";

  // TODO: Remove once a proper FdV integration has been implemented
  public static final String HARD_CODED_ACCESS_CODE = "ABC123";

  @Getter(lazy = true)
  private static final Testdata instance = new Testdata();

  @Getter(lazy = true)
  private final Patients patientsTestdata = Utils.loadConfig(Patients.class, PATIENTS_DATA_KEY);

  @Getter(lazy = true)
  private final Practitioners euPractitioners =
      Utils.loadConfig(Practitioners.class, EU_PRACTITIONERS_KEY);

  @Getter(lazy = true)
  private final ArrayList<String> knownTitles =
      TigerGlobalConfiguration.instantiateConfigurationBean(
          new TypeReference<>() {}, TITLES_CONFIG_KEY);

  @Getter(lazy = true)
  private final ArrayList<String> knownPrefixes =
      TigerGlobalConfiguration.instantiateConfigurationBean(
          new TypeReference<>() {}, PREFIXES_CONFIG_KEY);

  @Getter(lazy = true)
  private final HashMap<String, String> epkaTemplates =
      TigerGlobalConfiguration.instantiateConfigurationBean(
          new TypeReference<>() {}, EPKA_TEMPLATES_CONFIG_PATH);

  @Getter(lazy = true)
  private final NcpehSimTestdataProfiles ncpehSimTestdataProfiles =
      Utils.loadConfig(NcpehSimTestdataProfiles.class, NCPEHSIM_TESTDATA_PROFILES_KEY);

  @Getter(lazy = true)
  private final RecordSystems recordSystems =
      Utils.loadConfig(RecordSystems.class, RECORD_SYSTEMS_CONFIG_KEY);

  @Getter(lazy = true)
  private final KonnektorAddresses konnektorAddresses =
      Utils.loadConfig(KonnektorAddresses.class, KONNEKTOR_ADDRESSES_CONFIG_KEY);

  @Getter(lazy = true)
  private final Boolean doNotFail = Utils.loadConfig(Boolean.class, DO_NOT_FAIL_CONFIG_KEY);

  private Testdata() {
    initialize();
  }

  protected void initialize() {
    TigerGlobalConfiguration.getObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  /**
   * For a config, that has a path to a file as value, this function can be used to load the file.
   *
   * @param configPath complete config path, e.g. {@literal epka.templates.default}
   * @return {@link File} the loaded file
   */
  @SneakyThrows
  public File loadFileFromPathInConfig(@NonNull String configPath) {
    var filePath = TigerGlobalConfiguration.readString(configPath);
    return ResourceUtils.getFile(filePath);
  }

  /**
   * Get the path of the default ePKA template.<br>
   * It's simply the path from the first ePKA template config found, which is probably the top one.
   *
   * @return {@link String} path to the ePKA template
   */
  public String getDefaultEpkaTemplatePath() {
    return epkaTemplates().values().stream()
        .findFirst()
        .orElseThrow(
            () ->
                new TigerConfigurationException(
                    "No path to an ePKA template configured in the testdata configuration"));
  }

  /**
   * Create the ePKA for the given patient, using the default ePKA template
   *
   * @param patient {@link Patient} the patient for which to create the ePKA
   * @return {@code byte[]} the create ePKA as byte array
   */
  @SneakyThrows
  public byte[] createEpkaFromTemplate(Patient patient) {
    return createEpkaFromTemplateFile(patient, ResourceUtils.getURL(getDefaultEpkaTemplatePath()));
  }

  /**
   * Create the ePKA for the given patient, using the ePKA template configured for the given config
   * name.<br>
   * E.g. if the config is like this epka.templates.default: /etc/epkatemplate.xml "default" must be
   * set as value for parameter epkaTemplateName to load the file /etc/epkatemplate.xml
   *
   * @param patient {@link Patient} the patient for which to create the ePKA
   * @param epkaTemplateName {@link String} the config key
   * @return {@code byte[]} the create ePKA as byte array
   */
  @SneakyThrows
  public byte[] createEpkaFromTemplate(Patient patient, @NonNull String epkaTemplateName) {
    var templateFile = ResourceUtils.getURL(epkaTemplates().get(epkaTemplateName));

    return createEpkaFromTemplateFile(patient, templateFile);
  }

  private byte[] createEpkaFromTemplateFile(@NonNull Patient patient, @NonNull URL templateFile) {
    return new EpkaProcessor(templateFile)
        .updateBirthDate(patient.birthDate())
        .updateKvnr(patient.kvnr())
        .updateName(patient.name())
        .toXmlString()
        .getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Split a name in the three parts title(s), firstname(s), lastname(s)
   *
   * @param name {@link String} the full name to split
   * @return {@link Triple} the three name parts
   */
  public Triple<String, String, String> getNameParts(@NonNull String name) {
    var nameParts = name.split("\\s");
    var lastLastName = nameParts[nameParts.length - 1];

    var titles =
        Arrays.stream(nameParts)
            .takeWhile(namepart -> knownTitles().contains(namepart))
            .reduce((result, namepart) -> result + " " + namepart)
            .orElse(null);

    var names =
        Arrays.stream(nameParts)
            .limit(nameParts.length - 1L)
            .dropWhile(namepart -> knownTitles().contains(namepart))
            .toList();

    var firstNames =
        names.stream()
            .takeWhile(namepart -> !knownPrefixes().contains(namepart))
            .reduce((result, namepart) -> result + " " + namepart)
            .orElseThrow(() -> new IllegalArgumentException("No first name was found"));

    var lastNames =
        names.stream()
            .dropWhile(namepart -> !knownPrefixes().contains(namepart))
            .reduce((result, namepart) -> result + " " + namepart)
            .map(ln -> ln + " " + lastLastName)
            .orElse(lastLastName);

    return new ImmutableTriple<>(titles, firstNames, lastNames);
  }
}
