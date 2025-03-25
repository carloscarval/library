package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.BOOK_NOT_AVAILABLE;

public class BookNotAvailableException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = BOOK_NOT_AVAILABLE;

	public BookNotAvailableException() {
		super(DEFAULT_MESSAGE);
	}
}
