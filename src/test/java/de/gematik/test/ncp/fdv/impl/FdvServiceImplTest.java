/*
 * Copyright (c) 2024-2025 gematik GmbH
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

package de.gematik.test.ncp.fdv.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.gematik.test.ncp.ExternalServerConfig;
import de.gematik.test.ncp.fdv.FdvProvider;
import de.gematik.test.ncp.gen.eu.fdv.api.EntitleManagementEuApi;
import de.gematik.test.ncp.gen.eu.fdv.model.GetEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehRequestDTO;
import de.gematik.test.ncp.gen.eu.fdv.model.PutEntitlementNcpehResponseDTO;
import de.gematik.test.ncp.gen.fdv.api.LoginLogoutApi;
import de.gematik.test.ncp.gen.fdv.model.LoginResponseDTO;
import de.gematik.test.ncp.gen.fdv.model.ResponseDTO;
import de.gematik.test.ncp.utils.TestUtils;
import org.junit.jupiter.api.Test;

class FdvServiceImplTest {

  private final ExternalServerConfig config = FdvProvider.getInstance().getEpaFdvConfig();

  @Test
  void isUpAndRunning() {
    // Arrange
    final var clientProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder().config(config).entitleManagementEuApiProxy(clientProxy).build();
    when(clientProxy.getEntitlementNcpeh(any()))
        .thenReturn(new GetEntitlementNcpehResponseDTO().success(true));

    // Act
    final var result = assertDoesNotThrow(fdvInterfaceImpl::isUpAndRunning);

    // Assert
    assertTrue(result);
  }

  @Test
  void loginTest() {
    // Arrange
    final var loginLogoutApiProxy = mock(LoginLogoutApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder().config(config).loginLogoutApiProxy(loginLogoutApiProxy).build();
    when(loginLogoutApiProxy.login("X1209120947", null, null))
        .thenReturn(
            TestUtils.loadFromJsonResource(
                LoginResponseDTO.class, this.getClass(), "LoginResponseDTO_success.json"));

    // Act
    final var testee =
        assertDoesNotThrow(
            () -> fdvInterfaceImpl.login("X1209120947"), "method login threw exception");

    // Assert
    assertTrue(testee.isPresent(), "Optional is empty");
    assertEquals("A123456789", testee.get());
  }

  @Test
  void loginFailureTest() {
    // Arrange
    final var loginLogoutApiProxy = mock(LoginLogoutApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder().config(config).loginLogoutApiProxy(loginLogoutApiProxy).build();
    when(loginLogoutApiProxy.login("X1209120947", null, null))
        .thenReturn(
            TestUtils.loadFromJsonResource(
                LoginResponseDTO.class, this.getClass(), "LoginResponseDTO_failure.json"));

    // Act
    final var testee =
        assertDoesNotThrow(
            () -> fdvInterfaceImpl.login("X1209120947"), "method login threw exception");

    // Assert
    assertFalse(testee.isPresent(), "Optional is not empty");
  }

  @Test
  void loginThrowExceptionTest() {
    // Arrange
    final var loginLogoutApiProxy = mock(LoginLogoutApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder().config(config).loginLogoutApiProxy(loginLogoutApiProxy).build();
    when(loginLogoutApiProxy.login("X1209120947", null, null))
        .thenThrow(new RuntimeException("Some thing went wrong"));

    // Act
    final var exception =
        assertThrows(RuntimeException.class, () -> fdvInterfaceImpl.login("X1209120947"));

    // Assert
    assertEquals("Some thing went wrong", exception.getMessage());
  }

  @Test
  void logoutTest() {
    // Arrange
    final var loginLogoutApiProxy = mock(LoginLogoutApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder().config(config).loginLogoutApiProxy(loginLogoutApiProxy).build();
    when(loginLogoutApiProxy.logout(any()))
        .thenReturn(
            TestUtils.loadFromJsonResource(
                ResponseDTO.class, this.getClass(), "ResponseDTO_success.json"));

    // Act & Assert
    assertDoesNotThrow(
        () -> fdvInterfaceImpl.logout("A1209120947"), "method logout threw exception");
    verify(loginLogoutApiProxy).logout("A1209120947");
  }

  @Test
  void logoutFailureTest() {
    // Arrange
    final var loginLogoutApiProxy = mock(LoginLogoutApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder().config(config).loginLogoutApiProxy(loginLogoutApiProxy).build();
    final var response =
        TestUtils.loadFromJsonResource(
            ResponseDTO.class, this.getClass(), "ResponseDTO_failure.json");
    when(loginLogoutApiProxy.logout(any())).thenReturn(response);

    // Act & Assert
    assertDoesNotThrow(
        () -> fdvInterfaceImpl.logout("A1209120947"), "method logout threw exception");
    verify(loginLogoutApiProxy).logout("A1209120947");
  }

  @Test
  void logoutThrowExceptionTest() {
    // Arrange
    final var loginLogoutApiProxy = mock(LoginLogoutApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder().config(config).loginLogoutApiProxy(loginLogoutApiProxy).build();
    when(loginLogoutApiProxy.logout(any()))
        .thenThrow(new RuntimeException("Some thing went wrong"));

    // Act
    final var exception =
        assertThrows(RuntimeException.class, () -> fdvInterfaceImpl.logout("A1209120947"));

    // Assert
    assertEquals("Some thing went wrong", exception.getMessage());
    verify(loginLogoutApiProxy).logout("A1209120947");
  }

  @Test
  void getEntitlementNcpehTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    final var response =
        TestUtils.loadFromJsonResource(
            GetEntitlementNcpehResponseDTO.class,
            this.getClass(),
            "GetEntitlementNcpehResponseDTO_success.json");
    when(entitleManagementEuApiProxy.getEntitlementNcpeh("X1209120947")).thenReturn(response);

    // Act
    final var testee =
        assertDoesNotThrow(
            () -> fdvInterfaceImpl.getEntitlementNcpeh("X1209120947"),
            "method getEntitlementNcpeh threw exception");

    // Assert
    assertTrue(testee.isPresent(), "Optional is empty");
    assertEquals(response, testee.get());
  }

  @Test
  void getEntitlementNcpehFailureTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    when(entitleManagementEuApiProxy.getEntitlementNcpeh("X1209120947"))
        .thenReturn(
            TestUtils.loadFromJsonResource(
                GetEntitlementNcpehResponseDTO.class,
                this.getClass(),
                "GetEntitlementNcpehResponseDTO_failure.json"));

    // Act
    final var testee =
        assertDoesNotThrow(
            () -> fdvInterfaceImpl.getEntitlementNcpeh("X1209120947"),
            "method getEntitlementNcpeh threw exception");

    // Assert
    assertFalse(testee.isPresent(), "Optional is not empty");
  }

  @Test
  void getEntitlementNcpehThrowExceptionTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    when(entitleManagementEuApiProxy.getEntitlementNcpeh("A1209120947"))
        .thenThrow(new RuntimeException("Some thing went wrong"));

    // Act
    final var exception =
        assertThrows(
            RuntimeException.class, () -> fdvInterfaceImpl.getEntitlementNcpeh("A1209120947"));

    // Assert
    assertEquals("Some thing went wrong", exception.getMessage());
  }

  @Test
  void authorizeEuCountryTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    final var response =
        TestUtils.loadFromJsonResource(
            PutEntitlementNcpehResponseDTO.class,
            this.getClass(),
            "PutEntitlementNcpehResponseDTO_success.json");
    when(entitleManagementEuApiProxy.putEntitlementNcpeh(
            new PutEntitlementNcpehRequestDTO().recordId("Z123456789").countryName("Spain (ES)")))
        .thenReturn(response);

    // Act
    final var testee =
        assertDoesNotThrow(() -> fdvInterfaceImpl.authorizeEuCountry("Z123456789", "Spain (ES)"));

    // Assert
    assertTrue(testee.isPresent(), "Optional is empty");
    assertEquals(response, testee.get());
  }

  @Test
  void authorizeEuCountryFailureTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    when(entitleManagementEuApiProxy.putEntitlementNcpeh(
            new PutEntitlementNcpehRequestDTO().recordId("Z123456789").countryName("Spain (ES)")))
        .thenReturn(
            TestUtils.loadFromJsonResource(
                PutEntitlementNcpehResponseDTO.class,
                this.getClass(),
                "PutEntitlementNcpehResponseDTO_failure.json"));

    // Act
    final var testee =
        assertDoesNotThrow(() -> fdvInterfaceImpl.authorizeEuCountry("Z123456789", "Spain (ES)"));

    // Assert
    assertFalse(testee.isPresent(), "Optional is not empty");
  }

  @Test
  void authorizeEuCountryThrowExceptionTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    when(entitleManagementEuApiProxy.putEntitlementNcpeh(
            new PutEntitlementNcpehRequestDTO().recordId("Z123456789").countryName("Spain (ES)")))
        .thenThrow(new RuntimeException("Some thing went wrong"));

    // Act
    final var exception =
        assertThrows(
            RuntimeException.class,
            () -> fdvInterfaceImpl.authorizeEuCountry("Z123456789", "Spain (ES)"));

    // Assert
    assertEquals("Some thing went wrong", exception.getMessage());
  }

  @Test
  void deleteEntitlementNcpehTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    when(entitleManagementEuApiProxy.deleteEntitlementNcpeh(any()))
        .thenReturn(
            TestUtils.loadFromJsonResource(
                de.gematik.test.ncp.gen.eu.fdv.model.ResponseDTO.class,
                this.getClass(),
                "ResponseDTO_success.json"));

    // Act & Assert
    assertDoesNotThrow(
        () -> fdvInterfaceImpl.deleteEntitlementNcpeh("Z1209120947"),
        "method deleteEntitlementNcpeh threw exception");
    verify(entitleManagementEuApiProxy).deleteEntitlementNcpeh("Z1209120947");
  }

  @Test
  void deleteEntitlementNcpehFailureTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    when(entitleManagementEuApiProxy.deleteEntitlementNcpeh(any()))
        .thenReturn(
            TestUtils.loadFromJsonResource(
                de.gematik.test.ncp.gen.eu.fdv.model.ResponseDTO.class,
                this.getClass(),
                "ResponseDTO_failure.json"));

    // Act & Assert
    assertDoesNotThrow(
        () -> fdvInterfaceImpl.deleteEntitlementNcpeh("A1209120947"),
        "method deleteEntitlementNcpeh threw exception");
    verify(entitleManagementEuApiProxy).deleteEntitlementNcpeh("A1209120947");
  }

  @Test
  void deleteEntitlementNcpehThrowExceptionTest() {
    // Arrange
    final var entitleManagementEuApiProxy = mock(EntitleManagementEuApi.class);
    final var fdvInterfaceImpl =
        FdvServiceImpl.builder()
            .config(config)
            .entitleManagementEuApiProxy(entitleManagementEuApiProxy)
            .build();
    when(entitleManagementEuApiProxy.deleteEntitlementNcpeh(any()))
        .thenThrow(new RuntimeException("Some thing went wrong"));

    // Act
    final var exception =
        assertThrows(
            RuntimeException.class, () -> fdvInterfaceImpl.deleteEntitlementNcpeh("A1209120947"));

    // Assert
    assertEquals("Some thing went wrong", exception.getMessage());
    verify(entitleManagementEuApiProxy).deleteEntitlementNcpeh("A1209120947");
  }
}
