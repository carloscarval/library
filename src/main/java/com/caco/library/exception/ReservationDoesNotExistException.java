package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.RESERVATION_DOES_NOT_EXIST;

public class ReservationDoesNotExistException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = RESERVATION_DOES_NOT_EXIST;

	public ReservationDoesNotExistException() {
		super(DEFAULT_MESSAGE);
	}
}
