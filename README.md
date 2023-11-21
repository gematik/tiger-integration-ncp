<img align="right" width="200" height="37" src="Gematik_Logo_Flag.png" alt="Gematik Logo"/> <br/> 

# Tiger-Integration-NCP

Project for tiger based testcase implementations for testing the german NCPeH.

### Release-Notes
The Release-Notes can be found in the file [ReleaseNotes.md](ReleaseNotes.md)

## Scope and Content

General scope of this test project are test scenarios for testing NCPeHs End-to-End and interoperability integration into the national product chain.
The test scenarios are described with gherkin in german language.

Not scope of the test scenarios are
* the test of transformation and transcoding rules to form a CDA document that fits the requirements of eHDSI and that correct medical outcome is produced.
* a detailed test of the eHDSI interface of the NCPeH, as no real integration with country B can be done within a pure german test environment

### eHDSI feature "Patient Summary country A" (PSA)

Covers the eHDSI feature "Patient Summary country A" (PSA) with the defined and testable use cases in gemSpec_NCPeH_FD.
Five feature files cover certain aspects of this feature:
* patientIdentification.feature:
  * performs tests for the first specified UseCase "NCPeH.UC_1 - Versicherten im Behandlungsland identifizieren" of gemSpec_NCPeH_FD
  * handles fault situations caused by failures of availability and content of the required ePKA document
* psaFindDocuments.feature:
  * performs tests for the second specified UseCase "NCPeH.UC_2 - Verfügbare Versichertendatensätze des ePKA MIO auflisten" of gemSpec_NCPeH_FD
  * handles fault situations caused by failures of availability of the required ePKA document (no content checks)
* psaRetrieveDocumentCDA3.feature:
  * performs tests for the third specified UseCase "NCPeH.UC_3 - Versichertendatensatz abrufen" of gemSpec_NCPeH_FD
  * handles fault situations caused by failures of availability and content of the required ePKA document
* psaRetrieveDocumentCDA1.feature:
  * performs tests for the forth specified UseCase "NCPeH.UC_4 - Versichertendatensatz als PDF abrufen" of gemSpec_NCPeH_FD
  * no test of fault situations, as they are all covered within psaRetrieveDocumentCDA3.feature
  * test of retrieving both PS documents at once
* psaEpaInteroperability.feature:
  * concentrates on testing interoperability between NCPeH and the document database "ePA Aktensystem" with these scenarios 
    * ePA account does not exist or is not found
    * different possible states of an ePA account
    * all two different existing products of ePA Aktensystem are functioning with NCPeH
    * check that different ePKA Documents signed by different "Konnektoren" can be properly processed in the NCPeH
    * check different situations, that can occur with available or missing access permission to the ePA account of the insurant
    * check different situation with the session handling between NCPeH and ePA Aktensystem

## Requirements

* git
* Java JDK 17
* Maven (3.8.0 or newer)

## Getting started

Before executing the first test case be sure to have run the resources goal of maven, e.g. ```mvn process-resources```

## Testdata description

Some test data will be generated during test progress and some data have to be prepared before running the scenarios (static data).
This chapter will give a short overview of the data and explain, how to prepare the static data.

Scope: This chapter will NOT include a description of configuration data.

### Testdata overview for eHDSI feature "Patient Summary country A"

The testcases for the eHDSI feature "Patient Summary country A" do require the following data (input)

* Data to manage access rights for an ePA account of a test insurant
  * Identity data of test insurant
  * (optional) Identity data of german test practitioner (LE-DE)
  * NCPeH AccessCode for ePA account of a test insurant
* Data about the ePA Aktensystem
  * provider name of the ePA Aktensystem
  * ***KVNR*** identifiers for ePA accounts, being held on the ePA Aktensystem
* An ePKA test document within an ePA account of a test insurant
  * Identity data of test insurant
  * Personal data of test insurant
  * Identity data of german test practitioner (LE-DE)
  * ePKA template
* Data to generate and send requests to the simulation of NCPeH country B
  * Profile names for simulator of NCPeH country B
  * Profile data on the simulator of NCPeH country B, identified by profile name
  * Personal information of EU test practitioner (LE-EU)  
* Data to validate the responses of the simulator of NCPeH country B
  * Metadata about the ePKA document of the test insurant
  * Personal data of test insurant
  * AccessCode for ePA account of a test insurant

For communication with the simulation of NCPeH country B, please also see [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API).<br>
Data profiles on the simulator of NCPeH country B and their content will have to be agreed with the
provider of the simulator (DVKA).

### Data of the test insurant

#### Identity data of test insurant

The identity information uses as key the identifier ***KVNR***. It MUST belong to an
ePA account, which has to be available in an ePA Aktensystem.<br>
In one scenario an account per instance of ePA Aktensystem is required (current providers are: IBM, Rise).
For this scenario (at least) one test insurant identity with a matching account per ePA Aktensystem 
instance is required.<br>
To generally manage access to the ePA accounts, an *AUT certificate* with private key is required to
be used by the Test-FdV.

> ToDo: Description of identity data configuration to be used in FdV, once it is implemented

*Alternatively an eGK image* of the test insurant can be used in a card simulation together with a 
Konnektor of the Konnektor Service Platform (KSP) to grant ad hoc access to the LE-DE. 
The eGK image also contains the *AUT certificate* of the test insurant.
In this case, the test insurant must be additionally configured in the KSP.

#### Personal data of test insurant

Required personal data of the test insurant is
* Identifier of test insurant, ***KVNR***
* Name of the test insurant
  * title(s)
  * given name(s) and
  * family name(s)
* Birthdate

This data will be used to generate the ePKA document for the ePA account as well as to verify the output
of the NCPeH-FD when performing the use case #1 for patient identification and the usecases #3 and #4
for retrieving the patient summary document.

The personal data is being configured in the file [testdata.yaml](testdata.yaml) within this project.

#### Non-correlation of identity and personal data of test insurant

In the test steps, insurant names are used to identify the related personal data (birthdate, KVNR). The name and birthDate of the test insurant do not need to fit the information of the identity data within the certificates of the test insurant, because the NCPeH-FD will not process any data from the insurant certificates assigned to the KVNR. The certificates of the test insurant are only being used to manage the access rights of its ePA account.

This means, for the pre-configured personal data (name and birthDate) just assign an available KVNR identifier, for which an ePA account is prepared and available too (see [testdata.yaml](testdata.yaml)).

#### Dynamic data of test insurant

The AccessCode is being generated, when granting access rights to the NCPeH-FD using the Test-FdV.

When LE-DE writes the ePKA test document into the ePA account of the test insurant, 
the document as well as its metadata in the ePA account are dynamically generated.

### Data of the german test practitioner

The identity data (SMC-B) is being used to sign the ePKA document as well as to store the generated
document in the eP account of the test insurant. Any valid Test-SMC-B can be used with a Konnektor
(physical or as card image with a card simulation).<br>
The automated test execution will use a Konnektor of the KSP together with a card simulation.

Personal data of the german test practitioner do not play any role here.
The german test practitioner is configured within the KSP.

### Data of the european test practitioner

The identity data and the personal data as well as the related EU country have to be configured on the 
NCPeH country B simulator. They are referenced by a profile name. (see [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API)).<br>
The country must be defined for the test insurant to be able to grant access to it.<br>
The name of the european test practitioner in these skripts is (currently) only used for readability
of the test steps. The name gets a profile name assigned, which will be used to identify further relevant test data.

The following data of the european test practitioner are being configured in the file testdata.yaml:
* name
* country
* profileName

### Overview of test data configuration in testdata.yaml

Following test data are being configured in the file [testdata.yaml](testdata.yaml) in this project:
* *epka* - basis of the ePKA test document to be generated during test execution
  * *templates* - ePKA template path and file name
* *patients* - personal data of test insurants
  * *name*, consisting of the name parts
    * *titles*
    * *givenNames*
    * *lastNames*
  * *kvnr* - the assigned ***KVNR*** identifier of the test insurant
  * *birthdate* - format *yyyy-mm-dd*
* *practitioners*
  * *eu* - data about the eu practioner profile available on the NCPeH country B simulation
    * *name* - used for readability in the test steps
    * *country* - has to fit the configuration of the assigned profile on the NCPeH country B simulation
    * *profileName* - references a profile (section "profiles") holding data for the communication with the NCPeH country B simulation
* *recordsystems*
  * provider names of ePA Aktensystem instances with a list of assigned ***KVNRs*** for existing ePA accounts. The KVNRs listed here must be given in the patients configuration as well!
* *profiles* - profiles for communication with the NCPeH country B simulation
  * profile name - name of the profile to identify it (element "practitioners.eu.profileName")
    * *trcProfileName* - name to address a profile to be used by NCPeH country B simulation to generate a TRC assertion
    * *idaProfileName* - name to address a profile to be used by NCPeH country B simulation to generate an IDA assertion
* *config* - further configuration data
  * *names* - allowed titles and prefixes
    * *titles* - allowed titles in the name of the test insurant
    * *prefixes* - allowed prefixes in the name of the test insurant
  * *doNotFail* - 
>    ToDo: doNotFail to be removed (behavioral configuration)


## Test Environment

### Test Components

This Tiger & Cucumber based test suite requires three test components in order to successfully execute its test cases.
These are:
* epa-fdv: A component to simulate the patients interaction with its Aktenkonto.
  * It is required to:
    * Authorize the practitioners in an EU country to access the patient summary of the patient.
    * Provide the access code which the practitioner needs the moment it wants to access the patients Aktenkonto.
  * Find more information about the underlying API in its [OpenAPI description](https://github.com/gematik/api-ePA/blob/ePA-2.6/src/openapi/testtreiber_fdv.yaml)
* epa-ps-sim: A component to simulate the interaction of a german practitioner with the Aktenkonto of the patient.
  * It is required to store the signed patient summary in the Aktenkonto of the patient.
  * To facilitate this it uses a Konnektor and per default is configured to use a Konnektor from the Konnektor-Service-Plattform (KSP)
    * It does not require further configuration to successfully communicate with a KSP-Konnektor,
      but can be configured like any other Spring Boot application and thus connect to any Konnektor in reach.
  * Find more information about epa-ps-sim in its [Readme](https://github.com/gematik/epa-ps-sim/blob/main/README.adoc)
* ncpeh-simulation: A component to simulate the interactions of a practitioner in an EU country with the Aktenkonto of the patient.
  * It is required to:
    * Perform the patient identification
    * Find the patient summary in the Aktenkonto of the patient
    * Retrieve the patient summary of the patient in format PDF (CDA Level 1) or structured XML (CDA Level 3).
  * Find more information about the underlying ncpeh-simulation-api and the mock currently in use in its [Readme](https://github.com/gematik/NCPeH-Simulation-API/blob/main/README.md) 

All of these test components are managed by the Tiger Test Environment manager and can be supplied either as an executable
Java application, which will be started on start of a test run, or as running services (e.g. Docker container),
which are already present on start of a test run.
The configuration of the test components happens in configuration files, which are described in the subsequent section.


### Configuration Files

The configuration of this testsuite is file based and several configuration files exist, which are:
* [tiger.yaml](tiger.yaml): This is the primary configuration file for a Tiger based testsuite. 
  * Its purposes in this project are:
    * configuration of test components (section `servers`)
    * Enabling of the Workflow UI (section `lib`)
    * Integration of additional configuration files (section `additionalYamls`)
  * It is located in the primary directory of the project 
* [infrastructure.yaml](src/main/resources/not-in-jar/templates/infrastructure.yaml): Holding test component specific configurations.
  * Its purpose is:
    * Readable presentation of specific configuration data of the test components
  * This means, changes to this file must be made cautiously, as they might require changes elsewhere
    * For example, in the test suite the contents of each entry are loaded into a Java object of type `de.gematik.test.ncp.ExternalServerConfig`,
      so changing the structure in the file, or the names of the test components here, will probably break the test suite.
  * It is located in the folder `src/main/resources/not-in-jar/templates`
  * The file in this location is not used directly, but processed by maven in the resource phase, replacing placeholders with information taken from the pom
    * The processed file is written to the folder `target/generated-not-in-jar`
    * As a consequence, for successful test runs, a maven build must have been completed successfully, before starting the test suite.
* [localConfigDefault.yaml](localConfigDefault.yaml): This is the workplace configuration file holding information, which are specific to the host, where the tests are run.
  * `localConfigDefault.yaml` is merely a template and default. To use a host specific file: 
    * Create a file `tiger-<hostname of the machine where to run the tests>.yaml`
    * In it create the section
    ```
    additionalYamls:
      - filename: <name of the workplace configuration file>.yaml
        baseKey: workplace
    ```
    * This way the workplace configurations will be read from the provided file and not the default workplace file anymore
    * See [tiger-HOSTNAME.yaml](tiger-HOSTNAME.yaml) for an example
  * This template is also used for unit tests and integration tests in Jenkins during maven builds.
* **testdata.yaml**: File to configure test data.
  * See section [Overview of test data configuration in testdata.yaml](#overview-of-test-data-configuration-in-testdatayaml)
* [pom.xml](pom.xml): Maven build configuration for the testsuite and the place where the version numbers of the test components are configured (section `properties`).
  * It is located in the primary directory of the project.


### Configuring the test components

The configuration for all three test components is analogous.
To configure a test component it does usually suffice to change things in the host specific configuration file.
* To switch between starting with the test suite, and an instance of the test component already running,
  the property `isStarted` is relevant, with `true` meaning the test component is already running and `false` meaning it will be started with the test suite.
* Maven copies the jars of the test components in the folder `target/externalJars`, but if the jar of a test component is located elsewhere
  the property `jarPath` can be changed to point to the different folder.

If configuration changes are more severe, changes to other files might be necessary.
Therefore, some in depth explanations are expedient:
* For each test component the tiger.yaml holds two configurations, one to start it with the test suite and one for the case the test component is already running when starting the testsuite
  * Switch between those using the already mentioned property `isStarted`.
  * The configuration for the local start is always of type `externalJar` and its name ends on _Jar_
  * The configuration for the already running test component is always of type `externalUrl` and its name ends on _Url_
  * Changes to the following properties should be avoided, as they might require code changes or lead to inconsistent configuration:
    `type`, `active`, `hostname`
  * Configuration of type externalJar:
    * To switch to another implementation change the value in the `source` property.
      * Be aware, this might require changes in the infrastructure.yaml
      * To simply change the folder, change the property `jarPath`, as explained above.
    * To add additional configuration, use the two sections `externalJarOptions/options` and `externalJarOptions/arguments`
      * E.g. to activate another profile, for a Spring Boot app (which all three test components are), to the arguments section add a line like:
        `- --spring.profiles.active=myprofile`
        and if you also have to provide the configuration file of the profile, add another line like this:
        `- --spring.config.additional-location=path/containing/profile/file`
  * Configuration of type externalUrl:
    * To change the address of the test component, change the value in the `source` property.
  * In the section `additionalYamls` do not simply change the value of the `baseKey` properties. Changing them here, requires changes in the code as well.
  * For further information please consult the documentation of the [Tiger User Manual](https://gematik.github.io/app-Tiger/Tiger-User-Manual.html).
* Changes to the infrastructure.yaml should usually not be necessary, but in case they are:
  * For each test component an analogous configuration exists.
  * The property `version` defines the version of the test component and is usually taken from the pom, thus making sure the same version is used for code compilation and test running.
  * The property `hostname` defines the hostname under which the component is known to Tiger and in the code of the test suite.
    * Changing it won't make any difference, except in log messages, but it must contain a hostname conform to URL requirements
  * The property `basePath` defines the path part of the test components address under which the service endpoints are exposed.
    * Its value is application specific and thus should only need adjustment, if a different implementation is used for a test component.

## gematik external links

- [ncpeh-simulation-api](https://github.com/gematik/NCPeH-Simulation-API)
- [Documentation of german NCPeH](https://fachportal.gematik.de/hersteller-anbieter/komponenten-dienste/ncpeh)

## Glossary

* KVNR - "Krankenversichertennummer". The unique identifier of an insurant, pattern of the relevant 
first 10 characters: \[A-Z]\[0-9]{9}
* KSP - "Konnektor Service Platform". A Service provided by gematik to make Konnektor devices and
simulated cards (SMC-B, eGK, HBA) available for testing purposes. (available within gematik
and to selected 3rd party organisations)
* ePA - "elektonische Patientenakte". Centrally stored (see ePA Aktensystem) electronic patient record
* ePKA - "elektronische Patientenkurzakte". Primary health data of a patient in electronic form. Equivalent of the eHDSI patient summary
* ePA Aktensystem - service to store electronic patient records (ePA)
* eHDSI - "eHealth Digital Service Infrastructure". EU wide infrastructure to share eHealth data across member states
