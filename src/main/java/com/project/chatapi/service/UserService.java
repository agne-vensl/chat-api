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

@Service
public class UserService {
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public boolean isUsernameTaken(String username) {
    Optional<User> user = userRepository.findActiveUserByUsername(username);

    return user.isPresent() ? true : false;
  }

  public boolean userExists(UUID publicId) {
    Optional<User> user = userRepository.findByPublicId(publicId);

    return user.isPresent() ? true : false;
  }

  @Transactional
  public UserResponse createUser(CreateUserRequest request) {
    Role role;
    try {
        role = Role.valueOf(request.role().toUpperCase());
    } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Invalid role: " + request.role());
    }

    if (isUsernameTaken(request.username())) {
      throw new UsernameAlreadyTakenException("Username '" + request.username() + "' is already taken");
    }
    
    UUID publicId = UUID.randomUUID();
    String hashedPassword = passwordEncoder.encode(request.password());

    userRepository.insertUser(publicId, request.username(), hashedPassword, role.name(), false);

    return new UserResponse(publicId, request.username(), role.name(), false);
  }

  @Transactional
  public void softDeleteByPublicId(UUID publicId) {
    if(!userExists(publicId)) {
      throw new UserNotFoundException("User with publicId '" + publicId + "' not found");
    }

    userRepository.softDeleteByPublicId(publicId);
  }
}
