package com.project.chatapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.project.chatapi.constants.ErrorMessages.USERNAME_IS_REQUIRED;
import static com.project.chatapi.constants.ErrorMessages.PASSWORD_IS_REQUIRED;
import static com.project.chatapi.constants.ErrorMessages.ROLE_IS_REQUIRED;
import static com.project.chatapi.constants.ErrorMessages.USERNAME_LENGTH_ERROR;

import io.swagger.v3.oas.annotations.media.Schema;

import static com.project.chatapi.constants.ErrorMessages.ROLE_MUST_BE;
import static com.project.chatapi.constants.ErrorMessages.USERNAME_CAN_ONLY_CONTAIN;

public record CreateUserRequest(
  @Size(min = 1, max = 255, message = USERNAME_LENGTH_ERROR)
  @NotBlank(message = USERNAME_IS_REQUIRED)
  @NotNull(message = USERNAME_IS_REQUIRED)
  @Pattern(
    regexp = "^[A-Za-z0-9_\\-!?~*]+$", 
    message = USERNAME_CAN_ONLY_CONTAIN)
  @Schema(example = "username") String username, 

  @NotBlank(message = PASSWORD_IS_REQUIRED)
  @NotNull(message = PASSWORD_IS_REQUIRED)
  @Schema(example = "password") String password, 

  @NotBlank(message = ROLE_IS_REQUIRED)
  @NotNull(message = ROLE_IS_REQUIRED)
  @Pattern(regexp = "ADMIN|USER", message = ROLE_MUST_BE)
  @Schema(example = "USER") String role
) {}
