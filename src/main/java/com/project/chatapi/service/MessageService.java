package com.project.chatapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.project.chatapi.dto.MessageAuthor;
import com.project.chatapi.dto.MessageResponse;
import com.project.chatapi.dto.PostMessageRequest;
import com.project.chatapi.model.Message;
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

  public List<MessageResponse> getAllMessages() {
    List<Message> messages = messageRepository.getAllMessages();

    List<MessageResponse> mappedMessages = messages.stream().map(message -> 
      new MessageResponse(
        message.getId(),
        message.getCreatedAt(),
        new MessageAuthor(message.getUserPublicId(), message.getUsername()),
        message.getContent()
      )
    ).toList();

    return mappedMessages;
  }

  @Transactional
  public void postMessage(UUID publicId, PostMessageRequest request) {
    User user = userService.findActiveUserByPublicId(publicId).get();

    messageRepository.insertMessage(user.getId(), request.content(), LocalDateTime.now());
  }
}
