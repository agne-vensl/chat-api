package com.project.chatapi.dto;

import java.util.UUID;

public record UserResponse(
  UUID publicId, 
  String username, 
  String role, 
  boolean deleted
) {}
