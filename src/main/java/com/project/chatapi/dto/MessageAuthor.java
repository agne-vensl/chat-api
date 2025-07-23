package com.project.chatapi.dto;

import java.util.UUID;

public record MessageAuthor(UUID userId, String username) {}
