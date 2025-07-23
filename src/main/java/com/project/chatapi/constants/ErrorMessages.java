package com.project.chatapi.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorMessages {
  public static final String USERNAME_LENGTH_ERROR = "Username must be between 1 and 255 characters";
  public static final String USERNAME_IS_REQUIRED = "Username is a mandatory field";
  public static final String PASSWORD_IS_REQUIRED = "Password is a mandatory field";
  public static final String ROLE_IS_REQUIRED = "Role is a mandatory field";
  public static final String USERNAME_CAN_ONLY_CONTAIN = "Username can only contain letters, nuumbers and _, -, ?, !, ~, *";
  public static final String ROLE_MUST_BE = "Role must be USER or ADMIN";
  public static final String MESSAGE_LENGTH = "Message length must be between 1 and 1000 characters";
}
