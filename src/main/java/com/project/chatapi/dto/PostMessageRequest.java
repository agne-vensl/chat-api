package com.project.chatapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostMessageRequest(
  @NotBlank
  @NotNull
  @Size(min = 1, max = 1000, message = "Message length must be between 1 and 1000 characters")
  String content
) {}
