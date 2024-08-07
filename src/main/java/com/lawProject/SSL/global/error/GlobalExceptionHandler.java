package com.lawProject.SSL.global.error;

import com.lawProject.SSL.domain.token.exception.TokenException;
import com.lawProject.SSL.domain.user.exception.UserException;
import com.lawProject.SSL.global.common.code.ErrorCode;
import com.lawProject.SSL.global.common.response.ApiResponse;
import com.lawProject.SSL.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * MethodArgumentNotValidException 예외 처리
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("handleMethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode._INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ApiResponse<ErrorCode>> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        return ApiResponse.onFailure(ErrorCode._FORBIDDEN);
    }

    /**
     * Header
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingHeaderException(MissingRequestHeaderException ex) {
        String errorMessage = "Required header '" + ex.getHeaderName() + "' is missing";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }
//
    /**
     * BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<ErrorCode>> handleBusinessException(final BusinessException e) {
        log.error("handleEntityNotFoundException", e);
        ErrorCode errorCode = e.getErrorCode();
        return ApiResponse.onFailure(errorCode);
    }

    /**
     * Token
     */
    @ExceptionHandler(TokenException.class)
    protected ResponseEntity<ApiResponse<ErrorCode>> handleTokenException(final TokenException e) {
        log.error("handleTokenException", e);
        ErrorCode errorCode = e.getErrorCode();
        return ApiResponse.onFailure(errorCode);
    }

    /**
     * User
     */
    @ExceptionHandler(UserException.class)
    protected ResponseEntity<ApiResponse<ErrorCode>> handleUserException(final UserException e) {
        log.error("handleTokenException", e);
        ErrorCode errorCode = e.getErrorCode();
        return ApiResponse.onFailure(errorCode);
    }
}

