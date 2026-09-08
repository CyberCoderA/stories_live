package com.heydrian.stories_live.exception;

import java.util.Map;
import java.time.Instant;

public record ErrorResponse(
    int status,
    String error,
    String message,
    Instant timestamp,
    Map<String, String> errors
) {

    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(status, error, message, Instant.now(), Map.of());
    }

    public static ErrorResponse validation(int status, String message, Map<String, String> errors) {
        return new ErrorResponse(status, "VALIDATION_ERROR", message, Instant.now(), errors);
    }
}