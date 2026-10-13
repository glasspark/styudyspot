package com.glasspark.studyspot.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    @JsonProperty("SUCCESS")
    private final boolean success;

    @JsonProperty("MESSAGE")
    private final String message;

    @JsonProperty("RESULT")
    private final T result;

    private ApiResponse(boolean success, String message, T result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>(true, "성공", result);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, "성공", null);
    }

    public static <T> ApiResponse<T> ok(String message, T result) {
        return new ApiResponse<>(true, message, result);
    }

    public static ApiResponse<Void> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}