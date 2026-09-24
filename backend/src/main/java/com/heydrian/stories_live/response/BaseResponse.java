package com.heydrian.stories_live.response;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.heydrian.stories_live.dto.response.ApiResponse;

public class BaseResponse {

    protected <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
            .body(ApiResponse.of(status.value(), message, data));
    }

    protected ResponseEntity<ErrorResponse> error(HttpStatus status, String errorCode, String message) {
        return ResponseEntity.status(status)
            .body(ErrorResponse.of(status.value(), errorCode, message));
    }

    protected ResponseEntity<ErrorResponse> validationError(String message, Map<String, String> errors) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.validation(HttpStatus.BAD_REQUEST.value(), message, errors));
    }

    protected <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return success(HttpStatus.CREATED, message, data);
    }
}
