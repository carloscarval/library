package com.caco.library.security.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
		"jwt.secret=test_secret_jwt_key_1234567890123456",
		"token.expiration.time=30000"
})
class JwtUtilTest {

	private final String USERNAME = "john";
	@Autowired
	private JwtUtil classUnderTest;

	@Nested
	class GenerateTokenTest {

		@Test
		void shouldGenerateValidToken() {
			String token = classUnderTest.generateToken(USERNAME);

			assertNotNull(token);
		}
	}

	@Nested
	class ValidateTokenTest {

		@Test
		void shouldReturnTrueForValidToken() {
			String token = classUnderTest.generateToken(USERNAME);

			assertTrue(classUnderTest.validateToken(token));
		}

		@Test
		void shouldReturnFalseForInvalidToken() {
			String fakeToken = "invalid.jwt.token";

			assertFalse(classUnderTest.validateToken(fakeToken));
		}
	}

	@Nested
	class ExtractUsernameTest {

		@Test
		void shouldExtractUsernameFromValidToken() {
			String token = classUnderTest.generateToken(USERNAME);

			String extractedUsername = classUnderTest.extractUsername(token);

			assertEquals(USERNAME, extractedUsername);
		}
	}
}
