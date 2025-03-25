package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.RESERVATION_NOT_ACTIVE;

public class InvalidReservationStateException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = RESERVATION_NOT_ACTIVE;

	public InvalidReservationStateException() {
		super(DEFAULT_MESSAGE);
	}
}
