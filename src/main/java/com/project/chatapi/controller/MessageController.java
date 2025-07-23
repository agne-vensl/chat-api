package com.project.chatapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatapi.dto.MessageResponse;
import com.project.chatapi.dto.PostMessageRequest;
import com.project.chatapi.security.AuthenticatedUser;
import com.project.chatapi.service.MessageService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/messages")
public class MessageController {
  MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @GetMapping
  public List<MessageResponse> getMessages() {
    return messageService.getAllMessages();
  }

  @PostMapping
  public ResponseEntity<Void> postMessage(
    @AuthenticationPrincipal AuthenticatedUser user,
    @Valid @RequestBody PostMessageRequest request
  ) {
      messageService.postMessage(user.publicId(), request);
      
      return ResponseEntity.noContent().build();
  }
}
