package com.caco.library.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {
	private LocalDateTime timestamp;
	private String message;
	private int status;

	public ErrorResponse(LocalDateTime timestamp, String message, int status) {
		this.timestamp = timestamp;
		this.message = message;
		this.status = status;
	}
}
