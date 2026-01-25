package com.anurag.task_flow.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.anurag.task_flow.security.SecurityErrorResponse;

// @ControllerAdvice + @ResponseBody
// ControllerAdvice -> Helps us enable global exception handling
@RestControllerAdvice
// these only runs when we have exception thrown from controller
public class GlobalExceptionHandler {

  // Tells to run this method if this Exception came written inside paranthesis
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
        .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequest(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationFailed(AuthenticationException ex) {
    String errorResponse = SecurityErrorResponse.build(401, "Authentication failed. Please provide valid credentials");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(errorResponse);
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<String> handleAuthorizationDenied(AuthorizationDeniedException ex) {
    String errorResponse = SecurityErrorResponse.build(403, "You don't have permission to access this resource");
    return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
