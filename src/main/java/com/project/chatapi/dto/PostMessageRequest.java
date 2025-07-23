package com.project.chatapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.project.chatapi.constants.ErrorMessages.MESSAGE_LENGTH;

public record PostMessageRequest(
  @NotBlank
  @NotNull
  @Size(min = 1, max = 1000, message = MESSAGE_LENGTH)
  String content
) {}
