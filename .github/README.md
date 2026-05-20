<a href="https://www.gematik.de"><img align="right" width="250" height="47" src="../Gematik_Logo_Flag_With_Background.png" alt="Gematik Logo"/></a><br/>

# Tiger-Integration-NCP

<details>
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-project">About the Project</a></li>
    <li><a href="#release-notes">Release Notes</a></li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#running-the-tests">Running the Tests</a></li>
        <li><a href="#troubleshooting">Troubleshooting</a></li>
      </ul>
    </li>
    <li>
      <a href="#test-environment">Test Environment</a>
      <ul>
        <li>
          <a href="#test-components">Test Components</a>
        </li>
        <li>
          <a href="#environment-variables">Environment Variables</a>
        </li>
        <li>
          <a href="#configuration-files">Configuration Files</a>
          <ul>
            <li><a href="#overview">Overview</a></li>
            <li><a href="#adjusting-infrastructureyaml">Adjusting <code>infrastructure.yaml</code></a></li>
          </ul>
        </li>
        <li>
          <a href="#test-data">Test Data</a>
          <ul>
            <li><a href="#data-about-the-test-insurant">Data about the Test Insurant</a></li>
            <li><a href="#data-about-the-german-test-practitioner">Data about the German Test Practitioner</a></li>
            <li><a href="#data-about-the-european-test-practitioner">Data about the European Test Practitioner</a></li>
            <li><a href="#providing-test-data">Providing Test Data</a></li>
          </ul>
        </li>
      </ul>
    </li>
    <li>
      <a href="#use-case-eprescriptionedispensation-country-a-epeda">Use Case: ePrescription/eDispensation Country A (ePeDA)</a>
      <ul>
        <li><a href="#epeda-scenario-overview">ePeDA Scenario Overview</a></li>
        <li><a href="#epeda-specific-test-data">ePeDA-Specific Test Data</a></li>
      </ul>
    </li>
    <li>
      <a href="#use-case-patient-summary-country-a-psa">Use Case: Patient Summary Country A (PSA)</a>
      <ul>
        <li><a href="#psa-scenario-overview">PSA Scenario Overview</a></li>
        <li><a href="#psa-specific-test-data">PSA-Specific Test Data</a></li>
      </ul>
    </li>
    <li><a href="#license">License</a></li>
    <li><a href="#additional-notes-and-disclaimer-from-gematik-gmbh">Additional Notes and Disclaimer from gematik GmbH</a></li>
    <li><a href="#contact">Contact</a></li>
    <li>
      <a href="#appendix">Appendix</a>
      <ul>
        <li><a href="#links--references">Links / References</a></li>
        <li><a href="#glossary">Glossary</a></li>
      </ul>
    </li>
  </ol>
</details>

## About the Project

This project provides test scenarios for the German National Contact Point for eHealth (NCPeH),
designed to verify correct operation and compliance with the
[gemSpec_NCPeH_FD_V2.0.1](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/)
specification.  
The test scenarios cover two eHDSI use cases:

- **ePrescription/eDispensation Country A (ePeDA)** — enabling a German patient to fill
  e-prescriptions at a pharmacy in another EU member state.
- **Patient Summary Country A (PSA)** — searching for and retrieving the patient summary from the
  electronic health record (_elektronische Patientenakte_, ePA) of a German patient receiving
  treatment in another EU member state.

Each use case is tested for end-to-end functionality, interoperability with the national Telematics
Infrastructure (TI), and performance reporting to the TI monitoring
system (_Betriebsdatenerfassung_, BDE).

These tests serve as acceptance tests during the NCPeH approval process conducted by gematik GmbH.
They are published to help developers and testers working on the NCPeH with quality assurance ahead
of that process. All scenarios are written in German using Gherkin syntax.

> [!NOTE]
> The following aspects are **not** covered by the test scenarios:
> * Transformation and transcoding rules — i.e. conformance of the resulting CDA or FHIR documents
    to eHDSI requirements and correctness of the contained medical data
> * Detailed testing of the NCPeH's eHDSI interface, since tests are run within the national test
    environment and do not involve actual cross-border data transfer. A simulation is used to mimic
    the NCPeH in the country of treatment.

## Release Notes

See [ReleaseNotes.md](../ReleaseNotes.md) for all information regarding the latest releases.

## Getting Started

This section provides instructions on how to get started with the project, including setting up the
development environment and running the test suite.

### Prerequisites

* Git
* Java JDK 21
* Maven 3.8.0 or later
* Docker & Docker Compose

To run end-to-end tests using real TI services, you also need:

* A way of connecting to the TI (see
  [gematik.de/telematikinfrastruktur/ti-zugang](https://www.gematik.de/telematikinfrastruktur/ti-zugang)
  for details)
* Test identity for the German insurant (to authenticate to the insurant's personal client)

### Running the Tests

To execute the test suite (in local mode, i.e. without long-running tests and using mocks for
external components):

```bash
mvn clean verify
```

To capture Maven output in a log file:

```bash
mvn clean verify | tee reports/maven-$(date +%Y%m%d-%H%M%S).log
```

### Troubleshooting

If you encounter a `java.nio.charset.UnmappableCharacterException`, set the `MAVEN_OPTS`
environment variable:

```bash
export MAVEN_OPTS=-Dfile.encoding=UTF-8
```

You can add this to your `.bashrc` or `.bash_profile` to make it persistent.

## Test Environment

This test suite relies on the [Tiger Test Framework](https://github.com/gematik/app-Tiger) to manage
and run the Cucumber-based scenarios. Tiger ensures all required components are properly
started, configured, and available during test execution. It also captures and records the
communication exchanged between the participating components, enabling detailed analysis of test
runs.
For general setup, configuration options, and advanced usage of the framework, refer to the
official [Tiger User Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual).

### Test Components

Depending on the use case being tested, certain
external test components, which are involved in the end-to-end workflow, need to be integrated into
the test environment. For example, the e-prescription/e-dispensation use case requires that an
insurant authorizes the country of treatment for health data access via their personal client. This
project includes mocks which mimic the required components to allow for a
purely local test run in the default configuration.  
The end-to-end tests, which interact with services located in the TI, use the implementations listed
below. They need to be activated via their individual configurations files which are maintained in
the `tiger` directory of this repository. Test components can be provided as executable JAR
applications, Docker images which are started automatically at test launch, or as pre-running
services.

> [!NOTE]
> With the current configuration, end-to-end tests involving TI-services are only possible when run
> on gematik infrastructure. The following sections are meant to provide an overview of the required
> components and their configuration. If you are interested in running the tests on your own
> infrastructure, please contact the project maintainers to discuss the necessary adjustments.

#### Common (All Use Cases)

| Component                 | Description                                                                                                                                                                                       | Implementation                                                                                                                                                                                                              |
|---------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| NCPeH Country B Simulator | Simulates a practitioner in another EU country interacting with a German patient's health data. Used for patient identification, document discovery, and document retrieval across all use cases. | Until the fully-functional simulator is available, the `ncpeh-simulation-mock` implementation included in the [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API) project is used to run the scenarios. |

#### Use Case: ePeDA

| Component                                           | Description                                                                                                                                                                                             | Implementation                                                                      |
|-----------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|
| E-Prescription Personal Client Simulator            | Simulates an insurant's client application, enabling automated selection and authorization of e-prescriptions for cross-border dispensation, and providing the access code needed by the EU pharmacist. | [ref-erp-fdv-testdriver](https://github.com/gematik/ref-erp-fdv-testdriver)         |
| Health Professional E-Prescription Client Simulator | Simulates a German practitioner's client software for issuing e-prescriptions.                                                                                                                          | `primsys-rest` in [erp-e2e-testsuite](https://github.com/gematik/erp-e2e-testsuite) |

#### Use Case: PSA

| Component                                | Purpose                                                                                                                                                           | Implementation                                                                                |
|------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------|
| ePA Personal Client Simulator            | Simulates an insurant's ePA client application, enabling the authorization of EU practitioners for patient summary access and providing the required access code. | [api-ePA-Testtreiber](https://github.com/gematik/api-ePA-Testtreiber/tree/epa3.1/src/openapi) |
| Health Professional ePA Client Simulator | Simulates a German practitioner's software for storing the patient summary in the patient's ePA account.                                                          | [epa-ps-sim](https://github.com/gematik/epa-ps-sim)                                           |
| ePA VAU Proxy                            | Responsible for establishing an encrypted connection to ePA backends. Used by the HP ePA Client Simulator.                                                        | [epa-deployment](https://github.com/gematik/epa-deployment)                                   |

### Environment Variables

The following environment variables must be set when running the tests with the implementations
listed above:

#### Common (All Use Cases)

| Variable                       | Description                                                                      |
|--------------------------------|----------------------------------------------------------------------------------|
| `TIGER_DOCKER_HOST` (optional) | Remote Docker daemon URL (if Docker is not available locally)*                   |
| `PROXY_HOST` (optional)        | Corporate proxy host for components that require outbound internet connectivity. |
| `PROXY_PORT` (optional)        | Corporate proxy port for components that require outbound internet connectivity. |

*For details on configuring Tiger to set up a Docker node, see the
[Tiger User Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.html#docker-container-node).

#### Use Case: ePeDA

| Variable          | Description                                                                     |
|-------------------|---------------------------------------------------------------------------------|
| `ERPIONE_API_KEY` | API key for PrimSys REST (health professional e-prescription client simulator). |
| `ERP_FDV_API_KEY` | API key for the e-prescription personal client simulator.                       |

#### Use Case: PSA

| Variable                 | Description                 |
|--------------------------|-----------------------------|
| `PS_SIM_KONN17_USERNAME` | Username for KSP connector. |
| `PS_SIM_KONN17_PASSWORD` | Password for KSP connector. |

### Configuration Files

#### Overview

Configuration is entirely file-based. The key files are:

| File                                                | Location     | Purpose                                                                                                                |
|-----------------------------------------------------|--------------|------------------------------------------------------------------------------------------------------------------------|
| [pom.xml](../pom.xml)                               | Project root | Maven build configuration including maven profiles specifying which scenarios to run and which components to activate. |
| [tiger.yaml](../tiger.yaml)                         | Project root | Main Tiger configuration: enables the Workflow UI and integrates additional config files.                              |
| [dwh.yaml](../tiger/erpFdv.yaml)                    | `tiger/`     | Configuration for the data warehouse API of the *BDE* performance data reporting system ("dwh" component)              |
| [erpFdv.yaml](../tiger/erpFdv.yaml)                 | `tiger/`     | E-Prescription personal client configuration ("Erp-FdV" component).                                                    |
| [fdv.yaml](../tiger/fdv.yaml)                       | `tiger/`     | EHR personal client configuration ("ePA FdV" component).                                                               |
| [infrastructure.yaml](../tiger/infrastructure.yaml) | `tiger/`     | Hostnames and base paths for each test component.                                                                      |
| [ncpeh.yaml](../tiger/ncpeh.yaml)                   | `tiger/`     | NCPeH Simulation component configuration.                                                                              |
| [primsys-rest.yaml](../tiger/primsys-rest.yaml)     | `tiger/`     | Health professional e-prescription client configuration ("PrimSys REST" component).                                    |
| [psSim.yaml](../tiger/psSim.yaml)                   | `tiger/`     | Health professional EHR client configuration ("ePA PS-Sim" component).                                                 |
| [testdata.yaml](../tiger/testdata.yaml)             | `tiger/`     | Test data (patients, practitioners, profiles, ePKA templates, reporting settings).                                     |

> [!CAUTION]
> Configurations are loaded into a Java object of type `de.gematik.test.ncp.ExternalServerConfig`.
> Changing the structure or naming of test components in `infrastructure.yaml` will likely break
> the test suite.

Each test component is configured in a similar way via its respective YAML file:

* The `active` property controls whether the component should be activated at test launch. The test
  suite will fall back to mocks for any component that is not active.
* Jar files of `externalJar`-type test components will be copied into the folder
  `target/externalJars`.

For further information please consult
the [Tiger User Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.html).

#### Adjusting `infrastructure.yaml`

Changes to this file should rarely be necessary. If they are:

- Each test component has its own section.
- `hostname` — defines how Tiger and the test suite code refer to the component.
- `basePath` — the path at which the component's endpoints are exposed (application-specific;
  adjust only if switching implementations).

### Test Data

Part of the test data is generated dynamically at runtime; certain templates must be prepared in
advance as static data. This section describes the common data structures shared across use cases.

#### Data about the Test Insurant

##### Identity Data

Each test insurant is identified by a valid KVNR referring to a test identity in the TI. To manage
ePA / e-prescription access for both German and EU practitioners, the insurant's personal client
must be configured with an *AUT certificate* with a private key.

##### Personal Data

The following **required** personal-data attributes are configured in
[testdata.yaml](../tiger/testdata.yaml), section `patients`:

| Attribute         | Description                                      |
|-------------------|--------------------------------------------------|
| `kvnr`            | Unique insurant identifier                       |
| `name.titles`     | *(optional)* Titles such as "Dr.", "Prof.", etc. |
| `name.givenNames` | First names                                      |
| `name.lastNames`  | Last names                                       |
| `birthDate`       | Date of birth (`yyyy-mm-dd`)                     |

This data is used to verify NCPeH output during patient identification and document retrieval and to
generate ePKA documents in the PSA use case.

##### Non-Correlation of Identity and Personal Data

The name and birthdate of a test insurant do **not** need to match the identity data in the
insurant's certificates. The NCPeH does not process any personal data from the insurant certificates
assigned to a given KVNR. You can therefore associate any available KVNR with the pre-configured
personal data. Note that, for the PSA use case, an active ePA account must be prepared and available
for this KVNR. (see [testdata.yaml](../tiger/testdata.yaml), section
`recordsystems`)

##### Dynamic Data

Access codes are generated when access rights are granted via the insurant's personal client.

When the LE-DE writes the ePKA document into the insurant's ePA account, both the document and its
metadata are generated on the fly.

#### Data about the German Test Practitioner

The German practitioner's identity data (SMC-B) is used to store documents in the test insurant's
ePA account. Any valid Test-SMC-B may be used with a connector, either as a physical card or a
virtual card image. Automated test execution uses a card image with a card terminal simulation
connected to a *KSP* connector.

The practitioner's personal data is not relevant for these tests.

Configuration is in [testdata.yaml](../tiger/testdata.yaml), section `practitioners.de`:

| Attribute    | Description                         |
|--------------|-------------------------------------|
| `titles`     | Titles such as "Dr.", "Prof.", etc. |
| `givenNames` | First names                         |
| `lastNames`  | Last names                          |

#### Data about the European Test Practitioner

The EU practitioner's identity, personal data, and associated country must be configured on the
NCPeH country B simulator and are referenced by a profile name (see
[ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API)).

Configuration is in [testdata.yaml](../tiger/testdata.yaml), section `practitioners.eu`:

| Attribute     | Description                                                                                                        |
|---------------|--------------------------------------------------------------------------------------------------------------------|
| `name`        | A human-readable name for test-step readability (e.g., "Dr. John Doe").                                            |
| `profileName` | The name of the configuration profile referencing both a TRC and an IDA profile in the NCPeH country B simulation. |

The `profileName` must match a profile in the `profiles` section
of [testdata.yaml](../tiger/testdata.yaml). A profile references TRC and IDA profiles via the
mandatory attributes `trcProfileName` and `idaProfileName`. Referenced profiles must be configured
in the NCPeH simulation [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API).

> [!NOTE]
> Configuration profiles and their contents must be coordinated with the NCPeH-B simulation
> provider (DVKA).

#### Providing Test Data

All test data is configured in [testdata.yaml](../tiger/testdata.yaml). The file contains these
sections:

| Section                                       | Description                                                                                                                                                                                                                                                              |
|-----------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `epka.templates`                              | Object containing the classpath locations of ePKA template files as attributes.                                                                                                                                                                                          |
| `patients`                                    | Personal data and KVNRs of test insurants, see [Personal Data](#personal-data).                                                                                                                                                                                          |
| `practitioners`                               | `eu` and `de` sub-sections for EU and German practitioner data.                                                                                                                                                                                                          |
| `recordsystems`                               | Provider names of ePA Aktensystem backends with a list of KVNRs for existing ePA accounts. Each KVNR listed here must uniquely identify a patient in the `patients` section!                                                                                             |
| `profiles.`\<_profileName_\>                  | Configuration profiles for the communication with the NCPeH country B simulation. `profileName` must match a name from `practitioners.eu.profileName`.                                                                                                                   |
| `profiles.`\<_profileName_\>`.trcProfileName` | Profile to be used by the NCPeH country B simulation to generate a TRC assertion.                                                                                                                                                                                        |
| `profiles.`\<_profileName_\>`.idaProfileName` | Profile to be used by the NCPeH country B simulation to generate an IDA assertion.                                                                                                                                                                                       |
| `config.names.titles`                         | List of allowed titles in the name of the test insurant.                                                                                                                                                                                                                 |
| `config.names.prefixes`                       | List of allowed prefixes in the name of the test insurant.                                                                                                                                                                                                               |
| `reporting.fileName`                          | Name of the file to write performance reporting data to.                                                                                                                                                                                                                 |
| `reporting.acceptableDelta`                   | Specifies the tolerated deviation, in milliseconds, between the scenario starting time recorded by the DWH service and that recorded by the testsuite. Reported performance metrics are only considered valid if their starting times fall within this acceptable delta. |

---

## Use Case: ePrescription/eDispensation Country A (ePeDA)

The ePeDA use case enables a German patient to fill e-prescriptions at a pharmacy in another EU
member state (eHDSI scenario "ePrescription/eDispensation Country A"). The patient uses
a personal client application to select e-prescriptions for cross-border use, authorize the EU
country, and receive an access code. The EU pharmacist then uses the patient's KVNR and access code
to identify the patient, list redeemable prescriptions, retrieve selected prescriptions, and
dispense the medication.

### ePeDA Scenario Overview

Seven feature files cover this use case:

| Feature File                                                                                                         | Use Case                                        | Description                                                                                                                                                                                       |
|----------------------------------------------------------------------------------------------------------------------|-------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [200_epedaPatientIdentification.feature](../src/test/resources/features/ePeD/200_epedaPatientIdentification.feature) | NCPeH.UC_9 — Identify the insurant for ePeD-A   | Tests patient identification by the EU pharmacist using KVNR and access code.                                                                                                                     |
| [210_epedaFindDocuments.feature](../src/test/resources/features/ePeD/210_epedaFindDocuments.feature)                 | NCPeH.UC_10 — List redeemable e-prescriptions   | Tests listing of e-prescriptions available for cross-border dispensation.                                                                                                                         |
| [220_epedaRetrieveDocuments.feature](../src/test/resources/features/ePeD/220_epedaRetrieveDocuments.feature)         | NCPeH.UC_11 — Retrieve selected e-prescriptions | Tests retrieval of selected e-prescriptions by the EU pharmacist.                                                                                                                                 |
| [230_epedaDispensePrescription.feature](../src/test/resources/features/ePeD/230_epedaDispensePrescription.feature)   | NCPeH.UC_12 — Dispense medication               | Tests medication dispensation to the insurant in the country of treatment.                                                                                                                        |
| [240_epedaErpInteroperability.feature](../src/test/resources/features/ePeD/240_epedaErpInteroperability.feature)     | E-Rezept interoperability                       | Tests interoperability between the NCPeH and the E-Rezept Fachdienst, including authorization handling.                                                                                           |
| [250_epedaNcpInteroperability.feature](../src/test/resources/features/ePeD/250_epedaNcpInteroperability.feature)     | NCPeH interoperability                          | Tests security-related interoperability aspects: KVNR/access-code validation, LE-EU permission and role codes, assertion consistency, country-code verification, and service access restrictions. |
| [290_epedaPerfReporting.feature](../src/test/resources/features/ePeD/290_epedaPerfReporting.feature)                 | Performance reporting                           | Validates performance metrics for UC_9–UC_12 reported to the BDE, including operation counts, duration plausibility, and status reporting.                                                        |

### ePeDA-Specific Test Data

In addition to the common test data described [above](#test-data), ePeDA scenarios require:

- **E-prescriptions** issued by a German practitioner, which the insurant must **actively mark as
  available** for cross-border dispensation.
- An **access code** generated when the insurant authorizes the EU country via the
  e-prescription personal client.
- **NCPeH authorization** for the EU member state to access the insurant's e-prescriptions. Must be
  granted via the e-prescription personal client.

---

## Use Case: Patient Summary Country A (PSA)

The PSA use case covers searching for and retrieving the patient summary from a German patient's
ePA, as defined in
[gemSpec_NCPeH_FD_V2.0.1](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/).
It leverages the ePA to enable a practitioner in another EU member state to access the patient's
ePKA (elektronische Patientenkurzakte).

### PSA Scenario Overview

Six feature files cover this use case:

| Feature File                                                                                                    | Use Case                                                                                                                                                  | Description                                                                                                                                |
|-----------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| [100_psaPatientIdentification.feature](../src/test/resources/features/PSA/100_psaPatientIdentification.feature) | [NCPeH.UC_1](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.1) — Identify the insurant in the country of treatment | Tests patient identification; covers fault scenarios caused by unavailable or invalid ePKA documents.                                      |
| [110_psaFindDocuments.feature](../src/test/resources/features/PSA/110_psaFindDocuments.feature)                 | [NCPeH.UC_2](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.2) — List ePKA MIO metadata                            | Tests document metadata discovery; covers fault scenarios (no content checks).                                                             |
| [120_psaRetrieveDocumentCDA3.feature](../src/test/resources/features/PSA/120_psaRetrieveDocumentCDA3.feature)   | [NCPeH.UC_3](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.3) — Retrieve the ePKA MIO (CDA Level 3)               | Tests structured XML document retrieval; covers fault scenarios.                                                                           |
| [130_psaRetrieveDocumentCDA1.feature](../src/test/resources/features/PSA/130_psaRetrieveDocumentCDA1.feature)   | [NCPeH.UC_4](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#5.1.4) — Retrieve the ePKA MIO as PDF/A (CDA Level 1)      | Tests PDF retrieval and combined retrieval of both CDA levels. Fault scenarios are covered by the CDA Level 3 feature.                     |
| [140_psaEpaInteroperability.feature](../src/test/resources/features/PSA/140_psaEpaInteroperability.feature)     | Interoperability                                                                                                                                          | Tests NCPeH–ePA interoperability: missing/unfound accounts, account states, multiple backends, access permissions, and session management. |
| [190_psaPerfReporting.feature](../src/test/resources/features/PSA/190_psaPerfReporting.feature)                 | Performance reporting                                                                                                                                     | Validates performance metrics reported to the BDE for successful and failed use cases, and verifies product information.                   |

### PSA-Specific Test Data

In addition to the common test data described [above](#test-data), PSA scenarios require:

- At least one insurant with an active account on **every** ePA backend variant (currently IBM and
  Rise)
- An **ePKA test document** stored in the ePA account containing the insurant's identity and
  personal data, the German practitioner's identity, and an ePKA template.
- **Metadata** about the ePKA document for response validation.
- The **ePA access code** generated when granting cross-border access via the ePA personal client.

Note: Instead of using a personal client to grant access to the insurant's ePA account,
authorization of the German health professional can optionally happen ad-hoc by inserting the
patient's eGK into a card terminal. This process can be simulated using a KSP connector with a
card terminal simulation and an *eGK image* which contains the AUT certificate of the test insurant.

---

## License

Copyright 2022-2026 gematik GmbH

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

For questions, issues, or other inquiries regarding this project, please reach out via the
[gematik service portal](https://service.gematik.de/servicedesk/customer/portal/34).

## Appendix

### Links / References

- [Tiger User Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.html)
- [NCPeH Simulation API](https://github.com/gematik/NCPeH-Simulation-API)
- [gematik NCPeH Specification](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/index.html)
- [gematik Service Portal for NCPeH](https://service.gematik.de/servicedesk/customer/portal/34)
- [ePA FdV Test Driver OpenAPI Description](https://github.com/gematik/api-ePA-Testtreiber/tree/epa3.1/src/openapi)
- [E-Rezept FDV Test Driver](https://github.com/gematik/ref-erp-fdv-testdriver)
- [E-Rezept E2E Test Suite](https://github.com/gematik/erp-e2e-testsuite)

### Glossary

| Term   | Full Name                              | Description                                                                       |
|--------|----------------------------------------|-----------------------------------------------------------------------------------|
| BDE    | Betriebsdatenerfassung                 | TI monitoring system for performance data reporting.                              |
| eHDSI  | eHealth Digital Service Infrastructure | EU-wide infrastructure for cross-border eHealth data exchange.                    |
| ePA    | elektronische Patientenakte            | Centrally stored electronic health record for German insurants (see ePA AS).      |
| ePA AS | ePA Aktensystem                        | Electronic health record system (EHR) in Germany.                                 |
| ePeDA  | ePrescription/eDispensation Country A  | eHDSI use case for cross-border e-prescription dispensation.                      |
| ePKA   | elektronische Patientenkurzakte        | Digital patient health summary; equivalent to the eHDSI patient summary.          |
| FdV    | Frontend des Versicherten              | Application for insurants to access health records and manage access permissions. |
| KSP    | Konnektor Service Platform             | gematik service providing _Konnektor_ devices and simulated cards for testing.    |
| KVNR   | Krankenversichertennummer              | Unique insurant identifier in Germany. First 10 characters match `[A-Z][0-9]{9}`. |
| LE-DE  | Leistungserbringer Deutschland         | German healthcare professional.                                                   |
| LE-EU  | Leistungserbringer EU                  | European healthcare professional.                                                 |
| PSA    | Patient Summary Country A              | eHDSI use case for cross-border patient summary access.                           |
