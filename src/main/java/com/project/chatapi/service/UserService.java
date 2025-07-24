package com.project.chatapi.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.chatapi.dto.CreateUserRequest;
import com.project.chatapi.dto.UserResponse;
import com.project.chatapi.exception.UserNotFoundException;
import com.project.chatapi.exception.UsernameAlreadyTakenException;
import com.project.chatapi.model.User;
import com.project.chatapi.model.enums.Role;
import com.project.chatapi.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Optional<User> findActiveUserByPublicId(UUID publicId) {
    return userRepository.findActiveUserByPublicId(publicId);
  }

  public boolean isUsernameTaken(String username) {
    Optional<User> user = userRepository.findActiveUserByUsername(username);

    return user.isPresent() ? true : false;
  }

  public boolean userExists(UUID publicId) {
    Optional<User> user = userRepository.findActiveUserByPublicId(publicId);

    return user.isPresent() ? true : false;
  }

  @Transactional
  public UserResponse createUser(CreateUserRequest request) {
    Role role;
    try {
      role = Role.valueOf(request.role().toUpperCase());
    } catch (IllegalArgumentException e) {
      log.warn("Attempt to create user with invalid role: {}", request.role());
      throw new IllegalArgumentException("Invalid role: " + request.role());
    }

    if (isUsernameTaken(request.username())) {
      log.warn("Attempt to create duplicate user: {}", request.username());
      throw new UsernameAlreadyTakenException("Username '" + request.username() + "' is already taken");
    }
    
    UUID publicId = UUID.randomUUID();
    String hashedPassword = passwordEncoder.encode(request.password());

    log.info("Registering new user: {}", request.username());
    userRepository.insertUser(publicId, request.username(), hashedPassword, role.name(), false);

    return new UserResponse(publicId, request.username(), role.name(), false);
  }

  @Transactional
  public void softDeleteByPublicId(UUID publicId) {
    if(!userExists(publicId)) {
      log.warn("Attempt to delete user that does not exist");
      throw new UserNotFoundException("User with publicId '" + publicId + "' not found");
    }

    log.info("Deleting user with publicId: {}, reassigning messages to anonymous", publicId);
    userRepository.softDeleteByPublicId(publicId);
  }
}
