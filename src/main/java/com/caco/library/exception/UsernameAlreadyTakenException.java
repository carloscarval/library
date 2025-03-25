package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.USERNAME_ALREADY_TAKEN;

public class UsernameAlreadyTakenException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = USERNAME_ALREADY_TAKEN;

	public UsernameAlreadyTakenException() {
		super(DEFAULT_MESSAGE);
	}
}
