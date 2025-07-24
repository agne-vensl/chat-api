package com.project.chatapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatapi.dto.CreateUserRequest;
import com.project.chatapi.dto.UserResponse;
import com.project.chatapi.model.User;
import com.project.chatapi.model.enums.Role;
import com.project.chatapi.repository.UserRepository;
import com.project.chatapi.security.JwtUtil;
import com.project.chatapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;

  @MockitoBean
  private UserRepository userRepository;

  private String adminToken;

  @BeforeEach
  void setUp() {
    UUID publicId = UUID.randomUUID();
    User user = new User(1L, publicId, "admin", "password", Role.ADMIN, false);

    when(userRepository.findActiveUserByPublicId(publicId))
      .thenReturn(Optional.of(user));

    adminToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), publicId.toString());
  }

  @Test
  void shouldCreateUserWhenAuthorized() throws Exception {
    CreateUserRequest request = new CreateUserRequest("newuser", "Password123!", "USER");
    UserResponse response = new UserResponse(UUID.randomUUID(), "newuser", "USER", false);

    when(userService.createUser(any(CreateUserRequest.class))).thenReturn(response);

    mockMvc.perform(post("/users")
      .header("Authorization", "Bearer " + adminToken)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("newuser"))
        .andExpect(jsonPath("$.role").value("USER"));

    verify(userService, times(1)).createUser(any(CreateUserRequest.class));
  }

  @Test
  void shouldReturnUnauthorizedWhenTokenIsInvalid() throws Exception {
    UUID publicId = UUID.randomUUID();

    mockMvc.perform(delete("/users/{publicId}", publicId)
      .header("Authorization", "Bearer invalid.token"))
        .andExpect(status().isUnauthorized());

    verify(userService, never()).softDeleteByPublicId(any());
  }
}
