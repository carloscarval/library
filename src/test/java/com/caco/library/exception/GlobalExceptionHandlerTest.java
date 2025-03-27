package com.caco.library.exception;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler classUnderTest = new GlobalExceptionHandler();

	@Nested
	class HandleReservationLimitExceededExceptionTest {

		@Test
		void returnsForbiddenResponse() {
			ReservationLimitExceededException ex = new ReservationLimitExceededException();

			ResponseEntity<ErrorResponse> response = classUnderTest.handleReservationLimitExceededException(ex);

			assertEquals(403, response.getStatusCode().value());
			assertEquals(ex.getMessage(), response.getBody().message());
			assertEquals(403, response.getBody().status());
			assertTrue(response.getBody().timestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
		}
	}

	@Nested
	class HandleBookNotAvailableExceptionTest {

		@Test
		void returnsConflictResponse() {
			BookNotAvailableException ex = new BookNotAvailableException();

			ResponseEntity<ErrorResponse> response = classUnderTest.handleBookNotAvailableException(ex);

			assertEquals(409, response.getStatusCode().value());
			assertEquals(409, response.getBody().status());
		}
	}

	@Nested
	class HandleInvalidCredentialsExceptionTest {

		@Test
		void returnsUnauthorizedResponse() {
			InvalidCredentialsException ex = new InvalidCredentialsException();

			ResponseEntity<ErrorResponse> response = classUnderTest.handleInvalidCredentialsException(ex);

			assertEquals(401, response.getStatusCode().value());
			assertEquals(401, response.getBody().status());
		}
	}

	@Nested
	class HandleGenericExceptionTest {

		@Test
		void returnsInternalServerError() {
			Exception ex = new RuntimeException("unexpected");

			ResponseEntity<ErrorResponse> response = classUnderTest.handleGenericException(ex);

			assertEquals(500, response.getStatusCode().value());
			assertEquals("unexpected", response.getBody().message());
		}
	}
}
