package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.USER_HAS_THREE_ACTIVE_RESERVATIONS;

public class ReservationLimitExceededException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = USER_HAS_THREE_ACTIVE_RESERVATIONS;

	public ReservationLimitExceededException() {
		super(DEFAULT_MESSAGE);
	}
}
