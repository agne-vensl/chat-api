package com.project.chatapi.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserStatisticsResponse(
  UUID userPublicId,
  @Schema(example = "username") String username,
  @Schema(example = "USER") String userRole,
  @Schema(example = "false") boolean userDeleted,
  @Schema(example = "156") Long messageCount,
  LocalDateTime firstMessagePostedAt,
  LocalDateTime lastMessagePostedAt,
  @Schema(example = "543") Double averageMessageLength,
  @Schema(example = "Last message.") String lastMessageContent
) {}
