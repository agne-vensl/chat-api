package com.project.chatapi.security;

import java.util.UUID;

public record AuthenticatedUser(
  UUID publicId,
  String username,
  String role
) {}
