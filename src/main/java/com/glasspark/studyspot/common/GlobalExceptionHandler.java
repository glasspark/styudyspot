package com.glasspark.studyspot.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ?page=abc 처럼 요청 파라미터의 타입이 맞지 않는 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        log.warn("Parameter type mismatch. name={}, value={}", exception.getName(), exception.getValue());
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode.getMessage()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn("CustomException occurred. errorCode={}, message={}", errorCode.name(), exception.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.isBindingFailure()
                        ? error.getField() + " 값의 형식이 올바르지 않습니다."   // ?page=abc 같은 타입 변환 실패
                        : error.getDefaultMessage())
                .orElse(ErrorCode.INVALID_REQUEST.getMessage());
        log.warn("Validation failed. message={}", message);
        return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getHttpStatus()).body(ApiResponse.fail(message));
    }

    // 중복검사(countBy...)를 다 통과한 직후, 거의 동시에 들어온 다른 요청과 경합해서
    // DB의 UNIQUE 제약에 걸리는 경우(TOCTOU)를 잡아내는 최후의 안전망.
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateKey(DuplicateKeyException exception) {
        log.warn("DuplicateKeyException occurred.", exception);
        ErrorCode errorCode = ErrorCode.DUPLICATE_MEMBER_INFO;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode.getMessage()));
    }

    // 본문이 비었거나 JSON 형식이 깨진 요청
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(HttpMessageNotReadableException exception) {
        log.warn("Request body not readable. message={}", exception.getMessage());
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode.getMessage()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResource(NoResourceFoundException exception) {
        ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception exception) {
        // 405, 415 등 Spring MVC가 이미 알맞은 상태코드를 들고 있는 예외는 500으로 덮어쓰지 않는다.
        if (exception instanceof ErrorResponse errorResponse) {
            log.warn("Framework exception. status={}, message={}", errorResponse.getStatusCode(), exception.getMessage());
            return ResponseEntity.status(errorResponse.getStatusCode())
                    .body(ApiResponse.fail(ErrorCode.INVALID_REQUEST.getMessage()));
        }
        log.error("Unexpected exception occurred.", exception);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode.getMessage()));
    }
}
