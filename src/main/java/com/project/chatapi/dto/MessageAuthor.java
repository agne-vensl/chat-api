package com.project.chatapi.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageAuthor(
  UUID userId, 
  @Schema(example = "username") String username
) {}
