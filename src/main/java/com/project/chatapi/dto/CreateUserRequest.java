package com.project.chatapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
  @Size(min = 1, max = 255, message = "Username must be between 1 and 255 characters")
  @NotBlank(message = "Username is a mandatory field")
  @NotNull(message = "Username is a mandatory field")
  @Pattern(
    regexp = "^[A-Za-z0-9_\\-!?~*]+$", 
    message = "Username can only contain letters, nuumbers and _, -, ?, !, ~, *")
  String username, 

  @NotBlank(message = "Password is a mandatory field")
  @NotNull(message = "Password is a mandatory field")
  String password, 

  @NotBlank(message = "Role is a mandatory field")
  @NotNull(message = "Role is a mandatory field")
  @Pattern(regexp = "ADMIN|USER", message = "Role must be USER or ADMIN")
  String role
) {}
