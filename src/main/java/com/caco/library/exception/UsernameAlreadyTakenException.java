package com.caco.library.exception;

public class UsernameAlreadyTakenException extends RuntimeException {

	public UsernameAlreadyTakenException(String message) {
		super(message);
	}
}
