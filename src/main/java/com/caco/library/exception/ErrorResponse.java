package com.caco.library.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, String message, int status) {
}
