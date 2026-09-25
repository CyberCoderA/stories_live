package com.heydrian.stories_live.response;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.heydrian.stories_live.dto.response.ApiResponse;

public final class ResponseHandler {

    private ResponseHandler() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
            .body(ApiResponse.of(status.value(), message, data));
    }

    public static ResponseEntity<ApiResponse<Void>> success(HttpStatus status, String message) {
        return success(status, message, null);
    }

    public static ResponseEntity<ErrorResponse> error(HttpStatus status, String errorCode, String message) {
        return ResponseEntity.status(status)
            .body(ErrorResponse.of(status.value(), errorCode, message));
    }

    public static ResponseEntity<ErrorResponse> validationError(String message, Map<String, String> errors) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.validation(HttpStatus.BAD_REQUEST.value(), message, errors));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return success(HttpStatus.CREATED, message, data);
    }

    public static ResponseEntity<ApiResponse<Void>> created(String message) {
        return success(HttpStatus.CREATED, message);
    }
}
