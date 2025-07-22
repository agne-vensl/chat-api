package com.project.chatapi.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CreateUserRequestTest {
  private static Validator validator;

  private String username = "tester";
  private String password = "password";
  private String role = "USER";

  @BeforeAll
  static void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldSucceedWhenAllFieldsAreCorrect() {
    CreateUserRequest request = new CreateUserRequest(username, password, role);

    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldFailIfUsernameIsInvalid() {
    CreateUserRequest request = new CreateUserRequest("....", password, role);

    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

    assertFalse(violations.isEmpty());
    assertTrue(
      violations.stream().anyMatch(v -> 
        v.getPropertyPath().toString().equals("username")
      )
    );
  }

  @Test
  void shouldFailIfUsernameIsTooLong() {
    CreateUserRequest request = new CreateUserRequest(
      // random username longer than 255 characters
      UUID.randomUUID().toString().repeat(8), 
      password, 
      role
    );

    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertTrue(
      violations.stream().anyMatch(v -> 
        v.getPropertyPath().toString().equals("username")
      )
    );
  }

  @Test
  void shouldFailIfPasswordIsEmpty() {
    CreateUserRequest request = new CreateUserRequest(username, "", role);

    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

    assertFalse(violations.isEmpty());
    assertTrue(
      violations.stream().anyMatch(v -> 
        v.getPropertyPath().toString().equals("password")
      )
    );
  }

  @Test
  void shouldFailIfRoleIsEmpty() {
    CreateUserRequest request = new CreateUserRequest(username, password, "");

    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

    assertFalse(violations.isEmpty());
    assertTrue(
      violations.stream().anyMatch(v -> 
        v.getPropertyPath().toString().equals("role")
      )
    );
  }

  @Test
  void shouldFailIfRoleIsInvalid() {
    CreateUserRequest request = new CreateUserRequest(username, password, "SUPERADMIN");

    Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(request);

    assertFalse(violations.isEmpty());
    assertTrue(
      violations.stream().anyMatch(v -> 
        v.getPropertyPath().toString().equals("role")
      )
    );
  }
}
