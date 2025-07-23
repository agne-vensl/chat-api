package com.project.chatapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.project.chatapi.constants.ErrorMessages.USERNAME_IS_REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;

import static com.project.chatapi.constants.ErrorMessages.PASSWORD_IS_REQUIRED;

public record LoginRequest(
  @NotBlank(message = USERNAME_IS_REQUIRED)
  @NotNull(message = USERNAME_IS_REQUIRED)
  @Schema(example = "username") String username,

  @NotBlank(message = PASSWORD_IS_REQUIRED)
  @NotNull(message = PASSWORD_IS_REQUIRED)
  @Schema(example = "password") String password
) {}
