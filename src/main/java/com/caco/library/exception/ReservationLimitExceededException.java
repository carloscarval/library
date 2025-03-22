package com.caco.library.exception;

public class ReservationLimitExceededException extends RuntimeException {

	public ReservationLimitExceededException(String message) {
		super(message);
	}
}
