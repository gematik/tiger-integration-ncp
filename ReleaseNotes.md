<img align="right" width="200" height="37" src="Gematik_Logo_Flag.png" alt="Gematik Logo"/> <br/>

# Release notes Tiger-Integration-NCP

## Release 1.6.0 (2024-10-23)

This release version is intended as know-how transfer from gematik test to the implementors and testers of the german
NCPeH service to provide a better understanding of the intended gematik tests.
The versioning of the test suite is synchronized to the published specification of NCPeH-FD in respect to the major and
minor version number of the test suite (
see [gemSpec_NCPeH_FD_V1.6.0](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/)).

This release provides the update of the test scenarios based on the latest specification of NCPeH service in version
1.6.0 (
see [gemSpec_NCPeH_FD_V1.6.0](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/)).

### added

* some new testcases have been added to fit the updated specification of the NCPeH

### changed

* assignment of testcase IDs has been updated
* some testcases became invalid and have been deleted
* negative test cases dealing with signature of ePKA documents did change to positive scenarios, as signature check has
  been removed from NCPeH functionality in version 1.6.0
* most testcases checking negative scenarios got updated in respect to expected error results.
* screenplay pattern implementation has been refined

### restrictions

* execution of test suite is currently not possible, because the latest version of ePA PSSim to suite ePA release 3.0 is
  not yet available as public release
* test driver interface for ePA FdV (the app to access ePA for the insurant) is not yet integrated (
  see [openapi for FdV test driver interface](https://github.com/gematik/api-ePA-Testtreiber/tree/epa3.1/src/openapi))
* test suite validation with real products was not possible yet (no integration with ePA v3.0 (the health record
  system), german NCPeH, IDP, FdV and the simulator of NCPeH country B)

## Release 0.2.0 (2023-09-25)

### new

This is a pre-release of the gematik test suite to test interoperability between german NCPeH service and components of
national telematik infrastructure.
This release is informative for the implementors of the german NCPeH service to provide a better understanding of the
intended gematik tests.

The test suite is based on the specification of NCPeH service version 1.5 (
see https://fachportal.gematik.de/hersteller-anbieter/komponenten-dienste/ncpeh)

A few test cases tagged with "@CI" can be run using mocked counterparts. Please start tests with maven, i. e. 'mvn
verify' or 'mvn clean verify'. Please see README.md for getting started with the test setup. 
