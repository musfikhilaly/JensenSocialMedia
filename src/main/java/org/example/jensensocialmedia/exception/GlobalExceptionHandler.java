package org.example.jensensocialmedia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler to manage and respond to various exceptions across the application.
 */
@RestControllerAdvice(basePackages = "org.example.jensensocialmedia.controller")
public class GlobalExceptionHandler {
    /**
     * Handles validation errors and constructs a response with field-specific error messages.
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return a ResponseEntity with a map of field errors and a BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError ->
                        errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles UnauthorizedException and constructs a response with an UNAUTHORIZED status.
     *
     * @param ex the UnauthorizedException
     * @return a ResponseEntity with the exception message and an UNAUTHORIZED status
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Handles UserNotFoundException and constructs a response with a NOT_FOUND status.
     *
     * @param ex the UserNotFoundException
     * @return a ResponseEntity with the exception message and a NOT_FOUND status
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles SecurityException and constructs a response with a FORBIDDEN status.
     *
     * @param ex the SecurityException
     * @return a ResponseEntity with the exception message and a FORBIDDEN status
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    /**
     * Handles IllegalArgumentException and constructs a response with a BAD_REQUEST status.
     *
     * @param ex the IllegalArgumentException
     * @return a ResponseEntity with the exception message and a BAD_REQUEST status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    
}
