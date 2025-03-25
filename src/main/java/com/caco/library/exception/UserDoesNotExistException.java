package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.USER_DOES_NOT_EXIST;

public class UserDoesNotExistException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = USER_DOES_NOT_EXIST;

	public UserDoesNotExistException() {
		super(DEFAULT_MESSAGE);
	}
}
