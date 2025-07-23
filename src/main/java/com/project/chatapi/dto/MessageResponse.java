package com.project.chatapi.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageResponse(
  @Schema(example = "1") Long id,
  LocalDateTime postedAt,
  MessageAuthor author,
  @Schema(example = "Message content.") String content
) {}
