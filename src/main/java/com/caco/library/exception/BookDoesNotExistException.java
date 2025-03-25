package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.BOOK_DOES_NOT_EXIST;

public class BookDoesNotExistException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = BOOK_DOES_NOT_EXIST;

	public BookDoesNotExistException() {
		super(DEFAULT_MESSAGE);
	}
}
