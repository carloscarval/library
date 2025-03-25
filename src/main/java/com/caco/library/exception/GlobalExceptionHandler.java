package com.caco.library.exception;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ReservationLimitExceededException.class)
	public ResponseEntity<ErrorResponse> handleReservationLimitExceededException(ReservationLimitExceededException ex) {
		return buildErrorResponse(ex, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(BookNotAvailableException.class)
	public ResponseEntity<ErrorResponse> handleBookNotAvailableException(BookNotAvailableException ex) {
		return buildErrorResponse(ex, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
		return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		logger.error("Unexpected error occurred", ex);
		return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {
		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), status.value());
		return new ResponseEntity<>(error, status);
	}
}
