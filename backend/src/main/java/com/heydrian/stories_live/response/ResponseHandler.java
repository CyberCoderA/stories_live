package com.heydrian.stories_live.response;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.heydrian.stories_live.dto.response.ApiResponse;

public final class ResponseHandler {

    private static final BaseResponse BASE_RESPONSE = new BaseResponse();

    private ResponseHandler() {
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        return BASE_RESPONSE.success(status, message, data);
    }

    public static ResponseEntity<ErrorResponse> error(HttpStatus status, String errorCode, String message) {
        return BASE_RESPONSE.error(status, errorCode, message);
    }

    public static ResponseEntity<ErrorResponse> validationError(String message, Map<String, String> errors) {
        return BASE_RESPONSE.validationError(message, errors);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return BASE_RESPONSE.created(message, data);
    }
}
