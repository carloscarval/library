package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.WRONG_USERNAME_OR_PASSWORD;

public class InvalidCredentialsException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = WRONG_USERNAME_OR_PASSWORD;

	public InvalidCredentialsException() {
		super(DEFAULT_MESSAGE);
	}
}
