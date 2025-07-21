package com.project.chatapi.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(Map.of(
        "error", "Invalid Parameter",
        "message", ex.getMessage()
      ));
  }
}
