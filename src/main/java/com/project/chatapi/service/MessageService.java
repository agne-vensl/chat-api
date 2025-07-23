package com.project.chatapi.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.chatapi.dto.PostMessageRequest;
import com.project.chatapi.model.User;
import com.project.chatapi.repository.MessageRepository;

import jakarta.transaction.Transactional;

@Service
public class MessageService {
  UserService userService;
  MessageRepository messageRepository;

  public MessageService(UserService userService, MessageRepository messageRepository) {
    this.userService = userService;
    this.messageRepository = messageRepository;
  }

  @Transactional
  public void postMessage(UUID publicId, PostMessageRequest request) {
    User user = userService.findActiveUserByPublicId(publicId).get();

    messageRepository.insertMessage(user.getId(), request.content(), LocalDateTime.now());
  }
}
