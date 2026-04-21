package me.calebeoliveira.springaopexample.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(objectError -> {
            final String fieldName = ((FieldError) objectError).getField();
            final String errorMessage = objectError.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(final ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        for(ConstraintViolation<?> violation: ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String field = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            String message = violation.getMessage();
            errors.put(field, message);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
