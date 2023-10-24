<img align="right" width="200" height="37" src="Gematik_Logo_Flag.png" alt="Gematik Logo"/> <br/>

# Release notes Tiger-Integration-NCP

## Release 0.2.0 (2023-09-25)

### new
This is a pre-release of the gematik test suite to test interoperability between german NCPeH service and components of national telematik infrastructure. 
This release is informative for the implementors of the german NCPeH service to provide a better understanding of the intended gematik tests.

The test suite is based on the specification of NCPeH service version 1.5 (see https://fachportal.gematik.de/hersteller-anbieter/komponenten-dienste/ncpeh)

A few test cases tagged with "@CI" can be run using mocked counterparts. Please start tests with maven, i. e. 'mvn verify' or 'mvn clean verify'. Please see README.md for getting started with the test setup. 

