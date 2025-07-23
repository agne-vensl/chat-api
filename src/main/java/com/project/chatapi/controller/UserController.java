package com.project.chatapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatapi.dto.CreateUserRequest;
import com.project.chatapi.dto.UserResponse;
import com.project.chatapi.service.UserService;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
    return userService.createUser(request);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{publicId}")
  public ResponseEntity<Void> deleteUser(@PathVariable("publicId") UUID publicId) {
    userService.softDeleteByPublicId(publicId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
