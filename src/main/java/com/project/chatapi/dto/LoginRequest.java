package com.project.chatapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
  @NotBlank(message = "Username is a mandatory field")
  @NotNull(message = "Username is a mandatory field")
  String username,

  @NotBlank(message = "Password is a mandatory field")
  @NotNull(message = "Password is a mandatory field")
  String password
) {}
