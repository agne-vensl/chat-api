package com.project.chatapi.dto;

public record CreateUserRequest(
  String username, 
  String password, 
  String role
) {}
