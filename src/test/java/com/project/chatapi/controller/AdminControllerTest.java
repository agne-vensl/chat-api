package com.project.chatapi.controller;

import com.project.chatapi.dto.UserStatisticsResponse;
import com.project.chatapi.model.enums.Role;
import com.project.chatapi.model.User;
import com.project.chatapi.repository.UserRepository;
import com.project.chatapi.security.JwtUtil;
import com.project.chatapi.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtil jwtUtil;

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private StatisticsService statisticsService;

  private String adminToken;
  private UUID testUserPublicId;

  @BeforeEach
  void setUp() {
    UUID publicId = UUID.randomUUID();
    User user = new User(1L, publicId, "admin", "password", Role.ADMIN, false);

    when(userRepository.findActiveUserByPublicId(publicId))
      .thenReturn(Optional.of(user));

    adminToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name(), publicId.toString());

    testUserPublicId = UUID.randomUUID();
  }

  @Test
  void shouldReturnUserStatisticsWhenAuthorized() throws Exception {
    UserStatisticsResponse stats = new UserStatisticsResponse(
      testUserPublicId,
      "testuser",
      Role.USER.name(),
      false,
      110L,
      LocalDateTime.now().minusDays(1),
      LocalDateTime.now(),
      85.4,
      "Last test message"
    );

    when(statisticsService.getUserStatistics(any(UUID.class))).thenReturn(stats);

    mockMvc.perform(get("/admin/statistics/{publicId}", testUserPublicId)
      .header("Authorization", "Bearer " + adminToken)
      .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.messageCount").value(110))
        .andExpect(jsonPath("$.averageMessageLength").value(85.4))
        .andExpect(jsonPath("$.lastMessageContent").value("Last test message"));

    verify(statisticsService, times(1)).getUserStatistics(testUserPublicId);
  }

  @Test
  void shouldReturnForbiddenWhenNoTokenProvided() throws Exception {
    mockMvc.perform(get("/admin/statistics/{publicId}", testUserPublicId))
      .andExpect(status().isForbidden());

    verify(statisticsService, never()).getUserStatistics(any());
  }

  @Test
  void shouldReturnUnauthorizedWhenTokenInvalid() throws Exception {
    mockMvc.perform(get("/admin/statistics/{publicId}", testUserPublicId)
      .header("Authorization", "Bearer invalid.token"))
        .andExpect(status().isUnauthorized());

    verify(statisticsService, never()).getUserStatistics(any());
  }

  @Test
  void shouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {
    User user = new User(1L, UUID.randomUUID(), "false-admin", "password", Role.USER, false);

    when(userRepository.findActiveUserByPublicId(user.getPublicId()))
      .thenReturn(Optional.of(user));

    String userToken = jwtUtil.generateToken(user.getUsername(), "USER", user.getPublicId().toString());

    mockMvc.perform(get("/admin/statistics/{publicId}", testUserPublicId)
      .header("Authorization", "Bearer " + userToken))
        .andExpect(status().isForbidden());

    verify(statisticsService, never()).getUserStatistics(any());
  }
}
