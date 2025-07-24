package com.project.chatapi.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.chatapi.dto.UserStatisticsResponse;
import com.project.chatapi.exception.UserNotFoundException;
import com.project.chatapi.model.User;
import com.project.chatapi.repository.StatisticsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StatisticsService {
  private UserService userService;
  private StatisticsRepository statisticsRepository;

  public StatisticsService(
    UserService userService, 
    StatisticsRepository statisticsRepository
  ) {
    this.userService = userService;
    this.statisticsRepository = statisticsRepository;
  }

  public UserStatisticsResponse getUserStatistics(UUID publicId) {
    User user = userService.findActiveUserByPublicId(publicId).orElseThrow(() -> {
      log.warn(
        "Attempt to fetch statistics for user that does not exist with publicId: {}",
        publicId
      );
      return new UserNotFoundException("User with publicId '" + publicId + "' not found");
    });

    UserStatisticsResponse response = new UserStatisticsResponse(
      publicId,
      user.getUsername(),
      user.getRole().name(),
      user.isDeleted(),
      statisticsRepository.getTotalMessageCount(publicId),
      statisticsRepository.getFirstMessageTime(publicId),
      statisticsRepository.getLastMessageTime(publicId),
      statisticsRepository.getAverageMessageLength(publicId),
      statisticsRepository.getLastMessageContent(publicId)
    );

    return response;
  }
}
