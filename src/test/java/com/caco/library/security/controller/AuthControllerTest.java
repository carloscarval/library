package com.caco.library.security.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.caco.library.model.dto.request.AuthenticationRequest;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.dto.response.Authentication;
import com.caco.library.security.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthController classUnderTest;

	@Nested
	class CreateAuthenticationTokenTest {

		@Test
		void validLogin_ReturnsAuth() {
			AuthenticationRequest request = new AuthenticationRequest("john", "pass");
			Authentication expectedAuth = new Authentication(1L, "jwt-token", "success");

			when(authService.authenticateUser(request)).thenReturn(expectedAuth);

			ResponseEntity<?> response = classUnderTest.createAuthenticationToken(request);

			assertEquals(200, response.getStatusCode().value());
			assertTrue(response.getBody() instanceof Authentication);

			Authentication actualAuth = (Authentication) response.getBody();
			assertEquals(expectedAuth.libraryUserId(), actualAuth.libraryUserId());
			assertEquals(expectedAuth.jwt(), actualAuth.jwt());
			assertEquals(expectedAuth.message(), actualAuth.message());
		}
	}

	@Nested
	class RegisterUserTest {

		@Test
		void validUser_ReturnsAuth() {
			CreateUserRequest request = new CreateUserRequest("john", "pass", "john@mail.com");
			Authentication expectedAuth = new Authentication(1L, null, "created");

			when(authService.registerUser(request)).thenReturn(expectedAuth);

			ResponseEntity<?> response = classUnderTest.registerUser(request);

			assertEquals(200, response.getStatusCode().value());
			assertInstanceOf(Authentication.class, response.getBody());

			Authentication actualAuth = (Authentication) response.getBody();
			assertEquals(expectedAuth.libraryUserId(), actualAuth.libraryUserId());
			assertEquals(expectedAuth.jwt(), actualAuth.jwt());
			assertEquals(expectedAuth.message(), actualAuth.message());
		}
	}
}
