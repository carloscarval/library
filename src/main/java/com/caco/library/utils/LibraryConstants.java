package com.caco.library.utils;

public class LibraryConstants {

	// Numbers
	public static final int MAXIMUM_ACTIVE_RESERVATIONS = 3;

	// API Constants
	public static final String API_BASE_URL = "/api";
	public static final String API_USERS = "/users";
	public static final String API_RESERVATIONS = "/reservations";
	public static final String API_LIBRARY_USER_ID = "/{libraryUserId}";
	public static final String API_RESERVATION_ID = "/{reservationId}";

	// API Security Constants
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_TOKEN_PREFIX = "Bearer ";
	public static final String API_AUTH = "/api/auth";
	public static final String API_LOGIN = "/login";
	public static final String API_REGISTER = "/register";
}
