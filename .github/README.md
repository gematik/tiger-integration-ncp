<a href="https://www.gematik.de"><img align="right" width="250" height="47" src="../Gematik_Logo_Flag_With_Background.png" alt="Gematik Logo"/></a><br/>

# Tiger-Integration-NCP

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About the Project</a>
    </li>
    <li>
      <a href="#release-notes">Release Notes</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
        <li><a href="#test-execution">Test Execution</a></li>
      </ul>
    </li>
    <li>
      <a href="#scenario-overview-for-ehdsi-feature-patient-summary-country-a">Scenario Overview for eHDSI Feature &quot;Patient Summary Country A&quot;</a>
    </li>
    <li>
      <a href="#test-data-description">Test Data Description</a>
      <ul>
        <li><a href="#test-data-overview-for-ehdsi-feature-patient-summary-country-a">Test Data Overview for eHDSI Feature &quot;Patient Summary Country A&quot;</a></li>
        <li><a href="#data-about-the-test-insurant">Data about the Test Insurant</a></li>
        <li><a href="#data-about-the-german-test-practitioner">Data about the German Test Practitioner</a></li>
        <li><a href="#data-about-the-european-test-practitioner">Data about the European Test Practitioner</a></li>
        <li><a href="#providing-test-data">Providing Test Data</a></li>
      </ul>
    </li>
    <li>
      <a href="#test-environment">Test Environment</a>
      <ul>
        <li><a href="#test-components">Test Components</a></li>
        <li><a href="#configuration-files">Configuration Files</a></li>
        <li><a href="#configuring-the-test-components">Configuring the Test Components</a></li>
        <li><a href="#troubleshooting">Troubleshooting</a></li>
      </ul>
    </li>
    <li><a href="#license">License</a></li>
    <li><a href="#additional-notes-and-disclaimer-from-gematik-gmbh">Additional Notes and Disclaimer from gematik GmbH</a></li>
    <li><a href="#contact">Contact</a></li>
</ol>
<ul>
  <li>
    <a href="#appendix">Appendix</a>
    <ul>
      <li><a href="#links--references">Links / References</a></li>
      <li><a href="#glossary">Glossary</a></li>
    </ul>
  </li>
</ul>
</details>

## About the Project

This project provides implementations of test scenarios for the German National Contact Point for
eHealth (NCPeH) designed to ensure correct operation and compliance with the requirements for the
Use Case "Patient Summary Country A" (PSA) as defined in the
[gemSpec_NCPeH_FD_V2.0.1](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/)
specification. The scenarios focus on testing end-to-end functionality and interoperability with the
national product chain (Telematikinfrastruktur, TI). Additionally, a subset of scenarios is
dedicated to testing the NCPeH's performance reporting feature and validating recorded
performance metrics.  
The tests in this project are used for acceptance testing as part of the approval process for
the NCPeH by gematik GmbH. They are published to assist developers and testers responsible for
implementing the NCPeH with quality assurance in preparation for the approval process. All test
scenarios are described using Gherkin and are written in German.

> [!NOTE]
> The following aspects are **not** covered by the test scenarios:
> * Testing of transformation and transcoding rules with regard to the conformance of the resulting
    CDA document to eHDSI requirements and the correctness of the contained medical data
> * Detailed testing of the NCPeH's eHDSI interface, as the tests are executed within the national
    test environment and therefore do not involve real integration with any other country (country
    B).

## Release Notes

See [ReleaseNotes.md](../ReleaseNotes.md) for all information regarding the (newest) releases.

## Getting Started

### Prerequisites

* git
* Java JDK 21
* Maven (3.8.0 or newer)

### Installation

Before executing the first test case, be sure to have run the resources goal of maven, e.g. by
invoking

```mvn process-resources```

### Test Execution

To run the tests, execute the following command in the project root:

```mvn clean verify```

If you would like to have a maven log file, use the following command:

```mvn clean verify | tee reports/maven-$(date +%Y%m%d-%H%M%S).log```

## Scenario Overview for eHDSI Feature "Patient Summary Country A"

This project provides test scenarios for the eHDSI feature "Patient Summary Country A" (PSA), based
on the use cases defined in
[gemSpec_NCPeH_FD_V2.0.1](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/).
This feature leverages the national electronic health record system (German: elektronische
Patientenakte, ePA) to search for and retrieve the patient summary from the electronic health
record (EHR) of a German patient receiving treatment in another EU member state.

Five feature files cover end-to-end functionality and interoperability aspects of the four defined
use cases:

* [100_psaPatientIdentification.feature](../src/test/resources/features/PSA/100_psaPatientIdentification.feature)
    * Tests the first specified use
      case: ["NCPeH.UC_1 - Versicherten im Behandlungsland für PS-A
      identifizieren"](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.1)
    * Handles fault situations caused by the unavailability or invalid content of the required ePKA
      document.


* [110_psaFindDocuments.feature](../src/test/resources/features/PSA/110_psaFindDocuments.feature)
    * Tests the second specified use
      case: ["NCPeH.UC_2 - Metadaten über ePKA MIO auflisten"](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.2)
    * Handles fault situations caused by the unavailability or invalid content of the required ePKA
      document (no content checks).


* [120_psaRetrieveDocumentCDA3.feature](../src/test/resources/features/PSA/120_psaRetrieveDocumentCDA3.feature)
    * Tests the third specified use
      case: ["NCPeH.UC_3 - ePKA MIO des Versicherten abrufen"](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.3)
    * Handles fault situations caused by the unavailability or invalid content of the required ePKA
      document.


* [130_psaRetrieveDocumentCDA1.feature](../src/test/resources/features/PSA/130_psaRetrieveDocumentCDA1.feature)
    * Tests the fourth specified use
      case: ["NCPeH.UC_4 - ePKA MIO des Versicherten als PDF/A abrufen"](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.4)
    * Does not test fault situations, as these are covered by `psaRetrieveDocumentCDA3.feature`
    * Tests the retrieval of both PS documents at once (CDA level 1 and level 3)


* [140_psaEpaInteroperability.feature](../src/test/resources/features/PSA/140_psaEpaInteroperability.feature)
    * Focuses on testing interoperability between the NCPeH and the EHR system "ePA
      Aktensystem", particularly the following NCPeH capabilities:
        * Error handling when an ePA account does not exist or cannot be found
        * Handling of various possible states of an ePA account
        * Interoperability with different ePA Aktensystem backends
        * Handling scenarios with and without access permissions for the insurant's ePA account
        * Managing sessions between the NCPeH and the ePA Aktensystem

One feature file covers technical aspects of reporting performance metrics to the TI monitoring
system (Betriebsdatenerfassung, BDE):

* [190_psaPerfReporting.feature](../src/test/resources/features/PSA/190_psaPerfReporting.feature)
    * Tests the reporting of metrics about successfully executed use cases based on the test
      scenarios above
    * Tests the reporting of metrics about failed use cases based on the test scenarios above
    * Tests the reporting of product information

## Test Data Description

Part of the test data is generated dynamically during testing, whereas certain templates must be
prepared in advance as static data. This section provides a brief overview of the test data and
explains how to prepare the required static data.

> [!NOTE]
> A description of configuration data is **not** included in this section.

### Test Data Overview for eHDSI Feature "Patient Summary Country A"

The test cases for the eHDSI feature "Patient Summary country A" require the following data:

* Data to manage access rights of a German test practitioner to the ePA account of a test insurant:
    * Identity data of the insurant
    * Identity data (`telematikId`) of the practitioner ("LE-DE")
* Data to manage access rights of an EU country to the ePA account of the insurant:
    * NCPeH access code for the insurant's ePA account
    * EU country code
* Data about the ePA Aktensystem (ePA AS):
    * Name of the ePA AS provider (currently, "IBM" is the only supported provider)
    * KVNR identifier for an ePA account in the ePA AS
* An ePKA test document stored in the insurant's ePA account including:
    * Identity data of the insurant
    * Personal data of the insurant (i.e., name and birthdate)
    * Identity data of the German practitioner ("LE-DE")
    * ePKA template
* Data used to generate and send requests to the NCPeH simulation for country B:
    * Profile names referencing configuration profiles stored in the simulator acting as NCPeH
      country B
    * Personal information of the EU test practitioner ("LE-EU")
* Data used to validate responses from the NCPeH simulation for country B:
    * Metadata about the insurant's ePKA document
    * Personal data of the insurant
    * Access code for the insurant's ePA account

> [!TIP]
> For information on communicating with the NCPeH simulation, please refer to its GitHub
> project: [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API).

> [!NOTE]
> Configuration profiles and their contents must be coordinated with the NCPeH-B simulation
> provider (DVKA).

### Data about the Test Insurant

#### Identity Data

Identity information is linked to the insurant's unique identifier, the KVNR. This identifier
**MUST** be associated with an active ePA account that is available in an ePA Aktensystem.

Please note that one test scenario requires an active account for each ePA Aktensystem backend
variant (the current backend providers are IBM and Rise). Therefore, at least one test
insurant identity with corresponding accounts on all backend variants must be configured.

To manage access to the ePA account of a test insurant for both German and EU practitioners, an
*AUT certificate* with a private key for this insurant must be configured in the Test-FdV.

Additionally, an *eGK image* of the test insurant can be used in a card simulation together with a
_Konnektor_ of the Konnektor Service Platform (KSP) to grant ad hoc access to the LE-DE.
The eGK image also contains the *AUT certificate* of the test insurant. For this to work, the test
insurant must also be configured in the KSP.

#### Personal Data

The personal data associated with a test insurant includes the following **required** attributes:

* `kvnr`: Unique identifier
* `name`
    * `titles` (optional): Titles such as "Dr.", "Prof.", etc.
    * `givenNames`: First names of the insurant
    * `lastNames`: Last names of the insurant
* `birthDate`: Date of birth in the format `yyyy-mm-dd`

This information is used to generate an ePKA document for the test insurant's ePA account
and to verify the output of the NCPeH when performing use case #1 (patient identification)
and use cases #3 and #4 (retrieving the patient summary document).

Personal data is configured via the [testdata.yaml](../tiger/testdata.yaml) file in the `patients`
section.

#### Non-correlation of Identity and Personal Data for Test Insurants

In the test steps, insurant names are used to identify the corresponding personal data (such as
birthdate and KVNR). The name and birthdate of the test insurant do not need to match the identity
data included in the insurant's certificates. This is because the NCPeH does not process any
personal data from the insurant certificates assigned to the KVNR. The certificates are used solely
to manage access rights to the ePA account.

Therefore, for pre-configured personal data (name and birthDate), you can assign any available KVNR
identifier for which an ePA account has been prepared and is available
(see [testdata.yaml](../tiger/testdata.yaml), section `recordsystems`).

#### Dynamic Data

The access code is generated when access rights are granted to the NCPeH using the Test-FdV. When
the LE-DE writes the ePKA test document into the test insurant's ePA account, both the document and
its associated metadata in the ePA account are dynamically generated.

### Data about the German Test Practitioner

The identity data (SMC-B) is used to store the generated document in the test insurant's ePA
account. Any valid Test-SMC-B can be used with a _Konnektor_, either as a physical card or as a
virtual card image with a card simulation.  
Automated test execution uses a card image with a card terminal simulation, which is
connected to a _Konnektor_ of the KSP.

Personal data of the German test practitioner is not relevant for these tests.

The German test practitioner is configured in the [testdata.yaml](../tiger/testdata.yaml) file,
section `practitioners.de`, with the following attributes:

* `titles`: Titles of the practitioner, such as "Dr.", "Prof.", etc.
* `givenNames`: First names of the practitioner
* `lastNames`: Last names of the practitioner

### Data about the European Test Practitioner

The identity and personal data, and the associated EU country must be configured on
the NCPeH country B simulator. These are referenced by a profile name
(see [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API)).  
The country must be defined to allow the test insurant to grant access to it.

The name given to the European test practitioner is used solely to improve the readability of the
test steps. All relevant data is loaded from a configuration profile, which must be assigned to the
practitioner by referencing it in the `profileName` attribute.

The following attributes of the EU test practitioner are configured in the
[testdata.yaml](../tiger/testdata.yaml) file, section `practitioners.eu`:

* `name`: A readable name for the practitioner, e.g., "Dr. John Doe"
* `profileName`: The name of the configuration profile referencing both a TRC and an IDA profile in
  the NCPeH country B simulation.

The `profileName` must match a profile in the `profiles` section
of the [testdata.yaml](../tiger/testdata.yaml) file. A profile references TRC and IDA profiles via
the mandatory attributes `trcProfileName` and `idaProfileName`. Referenced profiles must be
configured in the NCPeH
simulation [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API).

### Providing Test Data

Test data is configured in the [testdata.yaml](../tiger/testdata.yaml) file as follows:

* `epka` - The ePKA test document to be generated during test execution
    * `templates` - Object containing the classpath locations of ePKA template files as attributes
* `patients` - Personal data of test insurants
    * `name` - Consists of:
        * `titles` (optional) - Titles, such as "Dr.", "Prof.", etc.
        * `givenNames` - First names
        * `lastNames` - Last names
    * `kvnr` - The assigned KVNR identifier
    * `birthdate` - Birthdate in the format *yyyy-mm-dd*
* `practitioners`
    * `eu` - Data about the EU practitioner profile available in the NCPeH country B simulation
        * `name` - A freely assignable name used for readability in the test steps
        * `profileName` - References a configuration profile (section `profiles`) providing data for
          the communication with the NCPeH country B simulation
    * `de` - Data about the German test practitioner
        * `titles` - Titles, such as "Dr.", "Prof.", etc.
        * `givenNames` - First names
        * `lastNames` - Last names
* `recordsystems`
    * Provider names of ePA Aktensystem backends with a list of KVNRs for existing
      ePA accounts. Each KVNR listed here must uniquely identify a patient in the `patients`
      section!
* `profiles` - Configuration profiles for the communication with the NCPeH country B simulation
    * \<_profile name_\> - Name by which the profile is identified (section
      `practitioners.eu.profileName`)
        * `trcProfileName` - Profile to be used by the NCPeH country B simulation to generate a TRC
          assertion
        * `idaProfileName` - Profile to be used by NCPeH country B simulation to generate an IDA
          assertion
* `config` - Miscellaneous configuration data
    * `names` - Allowed titles and prefixes
        * `titles` - List of allowed titles in the name of the test insurant
        * `prefixes` - List of allowed prefixes in the name of the test insurant
* `reporting` - Configuration for the reporting feature
    * `fileName` - Name of the file to write the reporting data to
    * `acceptableDelta` - Specifies the tolerated deviation, in milliseconds, between the scenario
      starting time recorded by the DWH service and that recorded by the testsuite. Reported
      performance metrics are only considered valid if their starting times fall within this
      acceptable delta.

## Test Environment

### Test Components

This Tiger & Cucumber-based test suite requires three test components in order to successfully
execute its test cases. These are:

* **epa-fdv**: A component that simulates the patient's interaction with their ePA account.
    * Required for:
        * Authorizing practitioners in another EU country to access the patient summary.
        * Providing the access code that a practitioner needs to access the patient's ePA account.
    * Find more information about the underlying API in
      the [OpenAPI description](https://github.com/gematik/api-ePA-Testtreiber/tree/epa3.1/src/openapi)
      of the ePA FdV test driver interface.
* **epa-ps-sim**: A simulation of the Health Professional Software used by a German practitioner
  to interact with a patient's ePA account.
    * Required for storing the patient summary in the patient's ePA account.
    * Accesses the ePA AS via a _Konnektor_. By default, it uses a Konnektor from the
      Konnektor-Service-Plattform (KSP).
* **ncpeh-simulation**: Simulates a practitioner in another EU country who interacts with
  the patient's ePA account.
    * Required for triggering these EU use cases:
        * Performing patient identification
        * Finding the patient summary in the patient's Aktenkonto
        * Retrieving the patient summary as a PDF (CDA Level 1) or as a structured XML
          document (CDA Level 3).
    * For more information about the underlying API and mock implementation, see the
      documentation in
      the [ncpeh-simulation-api GitHub repo](https://github.com/gematik/NCPeH-Simulation-API).

All of these test components are managed by the Tiger Test Environment manager. They can be provided
either as executable Java applications or as Docker images, which are started automatically at the
beginning of a test run, or as already running services (e.g., Docker containers) present before the
test run begins. The configuration for these test components is defined in configuration files,
which are described in the following section.

### Configuration Files

The configuration of this testsuite is file-based. Several configuration files exist at different
locations, each serving a specific purpose:

* [tiger.yaml](../tiger.yaml)
    * The main configuration file in a Tiger-based testsuite
    * Its purposes in this project are:
        * Enabling the Workflow UI (section `lib`)
        * Integrating additional configuration files (section `additionalConfigurationFiles`)
    * It is located in the project's root directory
* [infrastructure.yaml](../tiger/infrastructure.yaml)
    * Holds configurations specific to test components

> [!CAUTION]
> Changes to this file might require changes elsewhere.
> As configurations are loaded into a Java object of type
`de.gematik.test.ncp.ExternalServerConfig`, a change in the structure or naming of the test
> components in here will probably break the test suite.

* [testdata.yaml](../tiger/testdata.yaml)
    * File to provide test data
    * See section [Providing Test Data](#providing-test-data)
* [fdv.yaml](../tiger/fdv.yaml)
    * Main configuration of the ePA FDV component
* [ncpeh.yaml](../tiger/ncpeh.yaml)
    * Main configuration of the NCPeH Simulation component
* [psSim.yaml](../tiger/psSim.yaml)
    * Main configuration of the ePA Ps-Sim component
* [pom.xml](../pom.xml)
    * Maven build configuration for the testsuite and the place to configure which versions of the
      test components are used (section `properties`)
    * Located in the project's root directory

### Configuring the Test Components

> [!NOTE]
> Docker & Docker Compose should be installed on the machine where the tests are executed.
> See [Get Docker](https://docs.docker.com/get-started/get-docker/) for installation instructions.

All three test components are configured in a similar fashion. The relevant configuration files
are [psSim.yaml](../tiger/psSim.yaml), [ncpeh.yaml](../tiger/ncpeh.yaml)
and [fdv.yaml](../tiger/fdv.yaml).

* To select whether components should be started together with the test suite or are provided
  externally, the property `active` is relevant.
* For components of type `externalJar` Maven copies the jars of the test components into
  the folder `target/externalJars`.

If you make extensive changes to the configuration, changes to other files might be necessary.
The following explanations provide a bit more context and guidance on how to proceed:

For each test component, Tiger manages several configuration options. Some configurations enable
Tiger to start the component automatically alongside the test suite, while others are intended for
scenarios where the test component is already running when the test suite begins.

* Select how a component is started by using the already mentioned property `active`.
* Changes to the following properties should be avoided, as they might require code changes or
  lead to an inconsistent configuration: `type`, `hostname`
* Configuration of type `externalJar`:
    * To switch to another implementation, change the value in the `source` property. Be aware that
      this might require changes in `infrastructure.yaml`
    * To add additional configuration, use the sections `externalJarOptions.options`
      and `externalJarOptions.arguments`
    * Example: to activate another profile for a Spring Boot app (which all three test
      components are) add something like this to the `arguments` section:
      `- --spring.profiles.active=myprofile`.
      If you also have to provide the configuration file of the profile, add another
      line like this:
      `- --spring.config.additional-location=path/containing/profile/file`
* Configuration of type `externalUrl`:
    * To change the URL of the test component, change the value in the `source` property.
* Configuration of type `compose`:
    * To change the compose file, change the value in the `source` property.
* To set Tiger Docker options, use the `dockerOptions` section.
    * A compose file and all docker-related configurations are located in the folder `tiger/docker`.
* Section `additionalConfigurationFiles`: Do not simply change the value of the `baseKey`
  properties. Changing it here will require changes in the code as well.
* For further information please consult
  the [Tiger User Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.html).

Changes to [infrastructure.yaml](../tiger/infrastructure.yaml) should usually not be necessary, but
in case they are:

* Each test component is represented by a section
* The `hostname` property defines the hostname used to refer to the component by Tiger and
  in the code of the test suite.
* Changing it won't make any difference (except in log messages) but a valid hostname must be
  provided.
* The property `basePath` defines the path where the service endpoints are exposed.
* Its value is application-specific and thus should only need adjustment if a different
  implementation is used for a test component.

### Troubleshooting

In case you get a `java.nio.charset.UnmappableCharacterException`, set the `MAVEN_OPTS`
environment variable to `-Dfile.encoding=UTF-8`.

This can be done by adding the following line to your `.bashrc` or `.bash_profile`:

    export MAVEN_OPTS=-Dfile.encoding=UTF-8

## License

Copyright 2022-2025 gematik GmbH

Apache License, Version 2.0

See the [LICENSE](../LICENSE) for the specific language governing permissions and limitations under
the License

## Additional Notes and Disclaimer from gematik GmbH

1. Copyright notice: Each published work result is accompanied by an explicit statement of the
   license conditions for use. These are regularly typical conditions in connection with open source
   or free software. Programs described/provided/linked here are free software, unless otherwise
   stated.
2. Permission notice: Permission is hereby granted, free of charge, to any person obtaining a copy
   of this software and associated documentation files (the "Software"), to deal in the Software
   without restriction, including without limitation the rights to use, copy, modify, merge,
   publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to
   whom the Software is furnished to do so, subject to the following conditions:
    1. The copyright notice (Item 1) and the permission notice (Item 2) shall be included in all
       copies or substantial portions of the Software.
    2. The software is provided "as is" without warranty of any kind, either express or implied,
       including, but not limited to, the warranties of fitness for a particular purpose,
       merchantability, and/or non-infringement. The authors or copyright holders shall not be
       liable in any manner whatsoever for any damages or other claims arising from, out of or in
       connection with the software or the use or other dealings with the software, whether in an
       action of contract, tort, or otherwise.
    3. The software is the result of research and development activities, therefore not necessarily
       quality assured and without the character of a liable product. For this reason, gematik does
       not provide any support or other user assistance (unless otherwise stated in individual cases
       and without justification of a legal obligation). Furthermore, there is no claim to further
       development and adaptation of the results to a more current state of the art.
3. Gematik may remove published results temporarily or permanently from the place of publication at
   any time without prior notice or justification.
4. Please note: Parts of this code may have been generated using AI-supported technology. Please
   take this into account, especially when troubleshooting, for security analyses and possible
   adjustments.

## Contact

If you want to ask a question, report an issue or get in contact, please reach out via
our [gematik service portal for NCPeH-related inquiries](https://service.gematik.de/servicedesk/customer/portal/34).

## Appendix

### Links / References

- [Tiger User Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.html)
- [NCPeH Simulation API](https://github.com/gematik/NCPeH-Simulation-API)
- [gematik specification for the NCPeH](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/index.html)
- [gematik service portal for NCPeH-related inquiries](https://service.gematik.de/servicedesk/customer/portal/34)
- [OpenAPI Description of the ePA FdV test driver interface](https://github.com/gematik/api-ePA-Testtreiber/tree/epa3.1/src/openapi)

### Glossary

* KVNR - "Krankenversichertennummer". The unique identifier of an insurant in Germany. Its first 10
  characters adhere to the regex pattern `[A-Z][0-9]{9}`.
* KSP - "Konnektor Service Platform". A Service provided by gematik to make _Konnektor_ devices and
  simulated cards available to selected third-party organizations for testing purposes.
* ePA - "elektronische Patientenakte". Centrally stored electronic health record of a patient living
  and being insured in Germany (see 'ePA AS').
* ePKA - "elektronische Patientenkurzakte". Digital representation of a patient's health data
  summary. Equivalent to the eHDSI patient summary.
* FdV - "Frontend des Versicherten". Application used by an insurant to access their health data
  records and to manage access for health care professionals in Germany as well as for EU member
  states.
* ePA AS - "ePA Aktensystem". Electronic health record system (EHR) in Germany.
* eHDSI - "eHealth Digital Service Infrastructure". EU-wide infrastructure to share eHealth data
  across member states.
