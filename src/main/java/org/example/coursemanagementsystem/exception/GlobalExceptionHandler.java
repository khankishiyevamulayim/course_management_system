package org.example.coursemanagementsystem.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(
            UserNotFoundException ex, HttpServletRequest request) {
        log.error("UserNotFoundException: {} - {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex, HttpServletRequest request) {
        log.error("EmailAlreadyExistsException: {} - {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(FinCodeAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleFinCodeAlreadyExists(
            FinCodeAlreadyExistsException ex, HttpServletRequest request) {
        log.error("FinCodeAlreadyExistsException: {} - {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidPassword(
            InvalidPasswordException ex, HttpServletRequest request) {
        log.error("InvalidPasswordException: {} - {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ApiResponse<String>> handleOtpExpired(
            OtpExpiredException ex, HttpServletRequest request) {
        log.error("OtpExpiredException: {} - {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleRoleNotFoundException(
            RoleNotFoundException ex, HttpServletRequest request) {
        log.error("RoleNotFoundException: {} - {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }



    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        log.error("BadCredentialsException: {} - {}", ex.getMessage(), request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Email və ya şifrə yanlışdır"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        log.error("Validation error: {} - {}", errors, request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("Validasiya xətası", errors, false));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: ", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Daxili server xətası: " + ex.getMessage()));
    }
}