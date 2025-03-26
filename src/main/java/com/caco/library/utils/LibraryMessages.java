package com.caco.library.utils;

public class LibraryMessages {

	// Error messages
	public static final String USER_DOES_NOT_EXIST = "User does not exist";
	public static final String BOOK_DOES_NOT_EXIST = "Book does not exist";
	public static final String RESERVATION_DOES_NOT_EXIST = "Reservation does not exist";

	public static final String USER_HAS_THREE_ACTIVE_RESERVATIONS = "User already has 3 active reservations";
	public static final String BOOK_NOT_AVAILABLE = "Book is not available for reservation";
	public static final String RESERVATION_NOT_ACTIVE = "Reservation is not active and cannot be cancelled";
	public static final String UNEXPECTED_EXCEPTION_WHILE_REGISTERING_USER = "Unexpected error while registering user";
	public static final String DATA_CHANGED_WHILE_PROCESSING_REQUEST_RETRY = "Data changed while processing request. Try again";

	// User messages
	public static final String JWT_GENERATED_CORRECTLY = "JWT generated successfully";
	public static final String USER_CREATED = "User created successfully";
	public static final String WRONG_USERNAME_OR_PASSWORD = "Wrong username or password";
	public static final String USERNAME_ALREADY_TAKEN = "Username already taken";
}
