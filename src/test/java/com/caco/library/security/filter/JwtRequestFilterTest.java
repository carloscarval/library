package com.caco.library.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.caco.library.security.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

	@Mock
	private JwtUtil jwtUtil;

	@Mock
	private UserDetailsService userDetailsService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@InjectMocks
	private JwtRequestFilter classUnderTest;

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Nested
	class DoFilterInternalTest {

		@Test
		void validJwt_SetsAuthentication() throws ServletException, IOException {
			String tokenHeader = "Bearer valid.jwt.token";
			String token = "valid.jwt.token";
			String username = "john";
			UserDetails userDetails = new User(username, "pass", new ArrayList<>());

			when(request.getHeader("Authorization")).thenReturn(tokenHeader);
			when(jwtUtil.extractUsername(token)).thenReturn(username);
			when(jwtUtil.validateToken(token)).thenReturn(true);
			when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

			classUnderTest.doFilterInternal(request, response, filterChain);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			assertNotNull(authentication);
			assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);

			UsernamePasswordAuthenticationToken tokenAuth = (UsernamePasswordAuthenticationToken) authentication;
			assertEquals(userDetails, tokenAuth.getPrincipal());
			assertNull(tokenAuth.getCredentials());
			assertTrue(tokenAuth.isAuthenticated());

			verify(filterChain).doFilter(request, response);
		}

		@Test
		void invalidJwt_DoesNotAuthenticate() throws ServletException, IOException {
			when(request.getHeader("Authorization")).thenReturn("Bearer invalid");
			when(jwtUtil.extractUsername("invalid")).thenReturn("john");
			when(jwtUtil.validateToken("invalid")).thenReturn(false);

			classUnderTest.doFilterInternal(request, response, filterChain);

			assertNull(SecurityContextHolder.getContext().getAuthentication());
			verify(filterChain).doFilter(request, response);
		}

		@Test
		void noToken_ProceedsFilterChain() throws ServletException, IOException {
			when(request.getHeader("Authorization")).thenReturn(null);

			classUnderTest.doFilterInternal(request, response, filterChain);

			assertNull(SecurityContextHolder.getContext().getAuthentication());
			verify(filterChain).doFilter(request, response);
		}
	}

	@Nested
	class ShouldNotFilterTest {

		@Test
		void loginPath_ReturnsTrue() {
			when(request.getServletPath()).thenReturn("/api/v1/auth/login");

			boolean result = classUnderTest.shouldNotFilter(request);

			assertTrue(result);
		}

		@Test
		void registerPath_ReturnsTrue() {
			when(request.getServletPath()).thenReturn("/api/v1/auth/register");

			boolean result = classUnderTest.shouldNotFilter(request);

			assertTrue(result);
		}

		@Test
		void otherPath_ReturnsFalse() {
			when(request.getServletPath()).thenReturn("/api/v1/reservations");

			boolean result = classUnderTest.shouldNotFilter(request);

			assertFalse(result);
		}
	}
}
