<img align="right" width="200" height="37" src="Gematik_Logo_Flag.png" alt="Gematik Logo"/> <br/>

# Release Notes: Tiger-Integration-NCP

## Release 1.6.2 (2025-07-17)

### changed

- Moved project to groupId de.gematik.test.ncp
- Updated and improved README
- Updated LICENSE and Copyright Notice
- Updated dependency `ncpeh-simulation-api` to version 2.0.2

## Release 1.6.1 (2025-01-10)

This release aims to facilitate knowledge transfer from gematik test to the implementers and testers
of the German NCPeH service, providing a better understanding of the intended gematik tests. The
versioning of the test suite aligns with the published specification of the NCPeH-FD, maintaining
synchronization with the major and minor version numbers of the test suite (
see [gemSpec_NCPeH_FD_V1.6.0](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/)).

This release includes updates to the test scenarios based on the latest NCPeH service specification,
version 1.6.0 (
see [gemSpec_NCPeH_FD_V1.6.0](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/)).

### added

- New negative test cases have been implemented to align with the updated NCPeH specification.
- A mock implementation for the ePA FdV test driver interface has been added.

### changed

- Updated KVNRs to comply with ePA 3.0.
- Added support for SOAP envelopes in NCPeH responses.

### restrictions

- **Execution of the test suite is currently not possible** because the latest version of the ePA
  PSSim, required for ePA release 3.0, has not yet been made publicly available.
- Test suite validation with real products has not been performed due to the lack of integration
  with ePA v3.0 (the health record system), German NCPeH, IDP, FdV, and the simulator for NCPeH
  country B.

## Release 1.6.0 (2024-10-23)

This release aims to facilitate knowledge transfer from gematik test to the implementers and testers
of the German NCPeH service, providing a better understanding of the intended gematik tests. The
versioning of the test suite aligns with the published specification of the NCPeH-FD, maintaining
synchronization with the major and minor version numbers of the test suite (
see [gemSpec_NCPeH_FD_V1.6.0](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/)).

This release includes updates to the test scenarios based on the latest NCPeH service specification,
version 1.6.0 (
see [gemSpec_NCPeH_FD_V1.6.0](https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/)).

### added

- Several new test cases were introduced to align with the updated NCPeH specification.

### changed

- Test case IDs have been reassigned for better organization.
- Invalid test cases have been removed.
- Negative test cases related to the signature of ePKA documents have been converted to positive
  scenarios, as signature checks were removed from NCPeH functionality in version 1.6.0.
- Most negative test cases have been updated to reflect expected error results.
- The screenplay pattern implementation has been refined.

### restrictions

- **Execution of the test suite is currently not possible** because the latest version of the ePA
  PSSim, required for ePA release 3.0, has not yet been made publicly available.
- The test driver interface for ePA FdV (the app used by the insured to access ePA) is not yet
  integrated (
  see [openapi for FdV test driver interface](https://github.com/gematik/api-ePA-Testtreiber/tree/epa3.1/src/openapi)).
- Test suite validation with real products has not been conducted due to the lack of integration
  with ePA v3.0 (the health record system), German NCPeH, IDP, FdV, and the NCPeH simulator for
  country B.

## Release 0.2.0 (2023-09-25)

### new

This is a pre-release of the gematik test suite designed to test interoperability between the German
NCPeH service and components of the national telematics infrastructure.

This release is intended for implementers of the German NCPeH service, providing insights into the
intended gematik tests. The test suite is based on the NCPeH service specification version 1.5 (
see https://fachportal.gematik.de/hersteller-anbieter/komponenten-dienste/ncpeh).

A few test cases tagged with `@CI` can be executed using mocked counterparts. To run tests, use
Maven commands such as `mvn verify` or `mvn clean verify`. Please refer to
the [README.md](README.md) for instructions on setting up the test environment.
