package com.project.chatapi.dto;

import java.time.LocalDateTime;

public record MessageResponse(
  Long id,
  LocalDateTime postedAt,
  MessageAuthor author,
  String content
) {}
