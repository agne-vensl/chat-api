package com.project.chatapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatapi.dto.CreateUserRequest;
import com.project.chatapi.dto.UserResponse;
import com.project.chatapi.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
    return userService.createUser(request);
  }
}
