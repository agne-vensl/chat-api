package com.project.chatapi.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatapi.dto.LoginRequest;
import com.project.chatapi.dto.LoginResponse;
import com.project.chatapi.exception.InvalidCredentialsException;
import com.project.chatapi.service.AuthService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private AuthService authService;

  @Test
  void shouldReturnTokenWhenCredentialsValid() throws Exception {
    LoginRequest request = new LoginRequest("admin", "Password123!");
    LoginResponse response = new LoginResponse("jwt-token-here");

    when(authService.login(anyString(), anyString())).thenReturn(response);

    mockMvc.perform(post("/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("jwt-token-here"));

    verify(authService, times(1)).login("admin", "Password123!");
  }

  @Test
  void shouldReturnUnauthorizedWhenAuthServiceThrows() throws Exception {
    LoginRequest request = new LoginRequest("admin", "wrongpassword");

    when(authService.login(anyString(), anyString()))
      .thenThrow(new InvalidCredentialsException());

    mockMvc.perform(post("/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());

    verify(authService, times(1)).login("admin", "wrongpassword");
  }

  @Test
  void shouldReturnBadRequestWhenRequestInvalid() throws Exception {
    LoginRequest request = new LoginRequest("", "Password123!");

    mockMvc.perform(post("/auth/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verify(authService, never()).login(anyString(), anyString());
  }
}
