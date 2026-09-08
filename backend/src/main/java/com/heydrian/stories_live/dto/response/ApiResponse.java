package com.heydrian.stories_live.dto.response;

import java.time.Instant;

public record ApiResponse<T>(
    int status,
    String message,
    Instant timestamp,
    T data
) {

    public static <T> ApiResponse<T> of(int status, String message, T data) {
        return new ApiResponse<>(status, message, Instant.now(), data);
    }
}