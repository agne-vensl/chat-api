package com.project.chatapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatapi.dto.MessageResponse;
import com.project.chatapi.dto.PostMessageRequest;
import com.project.chatapi.security.AuthenticatedUser;
import com.project.chatapi.service.MessageService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/messages")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {
  MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @GetMapping
  public List<MessageResponse> getMessages() {
    log.info("Fetching all messages");
    return messageService.getAllMessages();
  }

  @PostMapping
  public ResponseEntity<Void> postMessage(
    @AuthenticationPrincipal AuthenticatedUser user,
    @Valid @RequestBody PostMessageRequest request
  ) {
      log.info("User is posting a new message: {}", request.content());
      messageService.postMessage(user.publicId(), request);
      
      return ResponseEntity.noContent().build();
  }
}
