package com.heydrian.stories_live.exception;

import java.util.Map;

public record ErrorResponse(
    String message,
    Map<String, String> errors
) {
}