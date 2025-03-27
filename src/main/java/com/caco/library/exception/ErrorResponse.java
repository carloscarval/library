package com.caco.library.exception;

import java.time.LocalDateTime;

import lombok.Data;

public record ErrorResponse (LocalDateTime timestamp, String message, int status) {}
