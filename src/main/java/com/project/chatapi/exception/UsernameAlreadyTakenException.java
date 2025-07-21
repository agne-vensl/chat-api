package com.project.chatapi.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
  public UsernameAlreadyTakenException(String message) {
    super(message);
  }
}
