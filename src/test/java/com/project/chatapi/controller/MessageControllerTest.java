package com.project.chatapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatapi.dto.MessageAuthor;
import com.project.chatapi.dto.MessageResponse;
import com.project.chatapi.dto.PostMessageRequest;
import com.project.chatapi.model.enums.Role;
import com.project.chatapi.model.User;
import com.project.chatapi.security.JwtUtil;
import com.project.chatapi.service.MessageService;
import com.project.chatapi.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private MessageService messageService;

  private String userToken;
  private UUID publicId;

  @BeforeEach
  void setup() {
    publicId = UUID.randomUUID();
    User user = new User(1L, publicId, "admin", "password", Role.ADMIN, false);

    when(userRepository.findActiveUserByPublicId(publicId))
      .thenReturn(Optional.of(user));

    userToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), publicId.toString());
  }

  @Test
  void shouldReturnMessagesWhenAuthorized() throws Exception {
    List<MessageResponse> messages = List.of(
      new MessageResponse(1L, LocalDateTime.now(), new MessageAuthor(publicId, "testuser"), "Hello!")
    );

    when(messageService.getAllMessages()).thenReturn(messages);

    mockMvc.perform(get("/messages")
      .header("Authorization", "Bearer " + userToken)
      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].content").value("Hello!"))
        .andExpect(jsonPath("$[0].author.username").value("testuser"));

    verify(messageService, times(1)).getAllMessages();
  }

  @Test
  void shouldReturnForbiddenWhenGettingMessagesWithoutToken() throws Exception {
    mockMvc.perform(get("/messages"))
      .andExpect(status().isForbidden());

    verify(messageService, never()).getAllMessages();
  }

  @Test
  void shouldPostMessageWhenAuthorized() throws Exception {
    PostMessageRequest request = new PostMessageRequest("New message");

    doNothing().when(messageService).postMessage(any(UUID.class), any(PostMessageRequest.class));

    mockMvc.perform(post("/messages")
      .header("Authorization", "Bearer " + userToken)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNoContent());

    verify(messageService, times(1)).postMessage(eq(publicId), any(PostMessageRequest.class));
  }

  @Test
  void shouldReturnForbiddenWhenPostingMessageWithoutToken() throws Exception {
    PostMessageRequest request = new PostMessageRequest("Unauthorized post");

    mockMvc.perform(post("/messages")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());

    verify(messageService, never()).postMessage(any(), any());
  }

  @Test
  void shouldReturnUnauthorizedWhenTokenInvalid() throws Exception {
    PostMessageRequest request = new PostMessageRequest("Invalid token post");

    mockMvc.perform(post("/messages")
      .header("Authorization", "Bearer invalid.token")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());

    verify(messageService, never()).postMessage(any(), any());
  }
}