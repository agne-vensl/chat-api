package com.project.chatapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.chatapi.dto.CreateUserRequest;
import com.project.chatapi.dto.UserResponse;
import com.project.chatapi.exception.UserNotFoundException;
import com.project.chatapi.exception.UsernameAlreadyTakenException;
import com.project.chatapi.model.User;
import com.project.chatapi.model.enums.Role;
import com.project.chatapi.repository.UserRepository;

@SpringBootTest
@Import(UserService.class)
public class UserServiceTest {
  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void shouldCreateUser() {
    String username = "tester";
    String role = Role.USER.name();
    CreateUserRequest request = new CreateUserRequest(username, "password", role);

    UserResponse response = userService.createUser(request);

    assertEquals(username, response.username());
    assertEquals(role, response.role());
  }

  @Test
  void shouldEncodePasswordWhenCreatingUser() {
    String username = "tester2";
    String password = "password";
    String role = Role.USER.name();
    CreateUserRequest request = new CreateUserRequest(username, password, role);

    userService.createUser(request);

    User saved = userRepository.findActiveUserByUsername("tester2").orElseThrow();
    assertNotEquals(password, saved.getPassword());
    assertTrue(passwordEncoder.matches(password, saved.getPassword()));
  }

  @Test
  void shouldThrowIfUsernameAlreadyTaken() {
    String username = "tester3";
    String role = Role.USER.name();
    CreateUserRequest request = new CreateUserRequest(username, "password", role);

    // First creation succeeds
    userService.createUser(request);

    // Second attempt fails
    assertThrows(
      UsernameAlreadyTakenException.class, 
      () -> userService.createUser(request)
    );
  }

  @Test
  void shouldThrowIfRoleInvalid() {
    String username = "tester";
    String role = "RANDOM";
    CreateUserRequest request = new CreateUserRequest(username, "password", role);

    assertThrows(
      IllegalArgumentException.class, 
      () -> userService.createUser(request)
    );
  }

  @Test
  void shouldFailToDeleteIfUserNotFound() {
    UUID publicId = UUID.randomUUID();
    
    assertThrows(
      UserNotFoundException.class, 
      () -> userService.softDeleteByPublicId(publicId)
    );
  }

  @Test
  void shouldDeleteUser() {
    String username = "tester4";
    String role = Role.USER.name();
    CreateUserRequest request = new CreateUserRequest(username, "password", role);

    UserResponse response = userService.createUser(request);

    userService.softDeleteByPublicId(response.publicId());

    Optional<User> deletedUser = userRepository.findByPublicId(response.publicId());

    assertTrue(deletedUser.isPresent());
    assertTrue(deletedUser.get().isDeleted());
    assertEquals("anonymous user", deletedUser.get().getUsername());
  }
}
