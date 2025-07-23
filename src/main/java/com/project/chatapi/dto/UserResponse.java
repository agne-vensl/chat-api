package com.project.chatapi.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
  UUID publicId, 
  @Schema(example = "username") String username, 
  @Schema(example = "USER") String role, 
  @Schema(example = "false") boolean deleted
) {}
