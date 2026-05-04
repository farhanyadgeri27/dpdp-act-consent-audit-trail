package com.internship.tool.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── 404 ──────────────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // ── 409 ──────────────────────────────────────────────────
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(
            DuplicateResourceException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // ── 400 custom ───────────────────────────────────────────
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            ValidationException ex) {
        return build(HttpStatus.BAD_REQUEST,
                     "Validation failed", ex.getErrors());
    }

    // ── 400 Spring @Valid ────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
          .getFieldErrors()
          .forEach(fe -> errors.put(fe.getField(),
                                    fe.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    // ── 401 ──────────────────────────────────────────────────
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), null);
    }

    // ── 410 ──────────────────────────────────────────────────
    @ExceptionHandler(ConsentExpiredException.class)
    public ResponseEntity<ErrorResponse> handleExpired(
            ConsentExpiredException ex) {
        return build(HttpStatus.GONE, ex.getMessage(), null);
    }

    // ── 503 ──────────────────────────────────────────────────
    @ExceptionHandler(AiServiceException.class)
    public ResponseEntity<ErrorResponse> handleAiService(
            AiServiceException ex) {
        log.error("AI service error: {}", ex.getMessage());
        return build(HttpStatus.SERVICE_UNAVAILABLE,
                     ex.getMessage(), null);
    }

    // ── 500 fallback ─────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        log.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please try again later.",
                null);
    }

    // ── builder ──────────────────────────────────────────────
    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String message,
            Map<String, String> fieldErrors) {

        ErrorResponse body = new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            fieldErrors,
            LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(body);
    }
}