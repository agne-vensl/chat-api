package com.project.chatapi.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(UsernameAlreadyTakenException.class)
  public ResponseEntity<Map<String, String>> handleUsernameAlreadyTaken(UsernameAlreadyTakenException ex) {
    return ResponseEntity
      .status(HttpStatus.CONFLICT)
      .body(Map.of(
        "error", "Username Already Taken",
        "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(Map.of(
        "error", "Not Found",
        "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(Map.of(
        "error", "Invalid Parameter",
        "message", ex.getMessage()
      ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors()
          .stream()
          .map(error -> error.getField() + ": " + error.getDefaultMessage())
          .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
          "error", "Invalid Parameter",
          "message", errors
        ));
  }
}
