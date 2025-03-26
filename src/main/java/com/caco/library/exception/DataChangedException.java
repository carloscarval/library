package com.caco.library.exception;

import static com.caco.library.utils.LibraryMessages.DATA_CHANGED_WHILE_PROCESSING_REQUEST_RETRY;

public class DataChangedException extends RuntimeException {

	private static final String DEFAULT_MESSAGE = DATA_CHANGED_WHILE_PROCESSING_REQUEST_RETRY;

	public DataChangedException() {
		super(DEFAULT_MESSAGE);
	}
}
