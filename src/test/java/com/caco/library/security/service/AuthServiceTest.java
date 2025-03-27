package com.caco.library.security.service;

import java.util.ArrayList;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.caco.library.exception.InvalidCredentialsException;
import com.caco.library.model.dto.request.AuthenticationRequest;
import com.caco.library.model.dto.request.CreateUserRequest;
import com.caco.library.model.dto.response.Authentication;
import com.caco.library.model.entity.LibraryUserEntity;
import com.caco.library.security.util.JwtUtil;
import com.caco.library.service.LibraryUserService;

import static com.caco.library.utils.LibraryMessages.JWT_GENERATED_CORRECTLY;
import static com.caco.library.utils.LibraryMessages.USER_CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private LibraryUserService libraryUserService;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthService classUnderTest;

	@Nested
	class AuthenticateUserTest {

		@Test
		void validCredentials_ReturnsAuthResponse() {
			AuthenticationRequest request = new AuthenticationRequest("john", "pass");
			UserDetails userDetails = new User("john", "pass", new ArrayList<>());
			LibraryUserEntity userEntity = new LibraryUserEntity(1L, "john", "pass", "john@mail.com", null);
			String expectedJwt = "jwt-token";

			when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);
			when(jwtUtil.generateToken("john")).thenReturn(expectedJwt);
			when(libraryUserService.findByUsername("john")).thenReturn(userEntity);

			Authentication auth = classUnderTest.authenticateUser(request);

			assertNotNull(auth);
			assertEquals(1L, auth.libraryUserId());
			assertEquals(expectedJwt, auth.jwt());
			assertEquals(JWT_GENERATED_CORRECTLY, auth.message());
		}

		@Test
		void invalidCredentials_ThrowsException() {
			AuthenticationRequest request = new AuthenticationRequest("john", "wrong");

			doThrow(BadCredentialsException.class).when(authenticationManager)
					.authenticate(any(UsernamePasswordAuthenticationToken.class));

			assertThrows(InvalidCredentialsException.class, () -> classUnderTest.authenticateUser(request));
		}
	}

	@Nested
	class RegisterUserTest {

		@Test
		void validRequest_ReturnsAuthResponse() {
			CreateUserRequest request = new CreateUserRequest("john", "pass", "john@mail.com");
			LibraryUserEntity entity = new LibraryUserEntity(1L, "john", "encoded", "john@mail.com", null);

			when(libraryUserService.registerLibraryUser(request)).thenReturn(entity);

			Authentication auth = classUnderTest.registerUser(request);

			assertNotNull(auth);
			assertEquals(1L, auth.libraryUserId());
			assertNull(auth.jwt());
			assertEquals(USER_CREATED, auth.message());
		}
	}
}
