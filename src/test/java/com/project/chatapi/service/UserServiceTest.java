package com.project.chatapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.project.chatapi.dto.CreateUserRequest;
import com.project.chatapi.dto.UserResponse;
import com.project.chatapi.exception.UsernameAlreadyTakenException;
import com.project.chatapi.model.User;
import com.project.chatapi.model.enums.Role;
import com.project.chatapi.repository.UserRepository;

@DataJpaTest
@Import(UserService.class)
public class UserServiceTest {
  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  private UUID testPublicId;
  private String username;

  void saveUser() {
    User user = new User();
    testPublicId = UUID.randomUUID();
    user.setPublicId(testPublicId);
    username = "tester";
    user.setUsername(username);
    user.setPassword("secret"); // Just a placeholder
    user.setRole(Role.USER);
    user.setDeleted(false);

    userRepository.insertUser(
      testPublicId, 
      username, 
      user.getPassword(), 
      user.getRole().name(), 
      false
    );
  }

  @Test
  void shouldCreateUser() {
    String username = "tester2";
    String role = Role.USER.name();
    CreateUserRequest request = new CreateUserRequest(username, "password2", role);

    UserResponse response = userService.createUser(request);

    assertEquals(username, response.username());
    assertEquals(role, response.role());
  }

  @Test
  void shouldThrowIfUsernameAlreadyTaken() {
    saveUser();

    String role = Role.USER.name();
    CreateUserRequest request = new CreateUserRequest(username.toUpperCase(), "password2", role);

    assertThrows(
      UsernameAlreadyTakenException.class, 
      () -> userService.createUser(request)
    );
  }

  @Test
  void shouldThrowIfRoleInvalid() {
    String username = "tester2";
    String role = "RANDOM";
    CreateUserRequest request = new CreateUserRequest(username, "password2", role);

    assertThrows(
      IllegalArgumentException.class, 
      () -> userService.createUser(request)
    );
  }
}
