package com.project.chatapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.chatapi.dto.LoginResponse;
import com.project.chatapi.exception.InvalidCredentialsException;
import com.project.chatapi.model.User;
import com.project.chatapi.repository.UserRepository;
import com.project.chatapi.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private JwtUtil jwtUtil;

  public AuthService(
      UserRepository userRepository, 
      PasswordEncoder passwordEncoder, 
      JwtUtil jwtUtil
    ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public LoginResponse login(String username, String password) {
    User user = userRepository.findActiveUserByUsername(username)
      .orElseThrow(() -> {
        log.warn("Failed login for user: {}", username);
        return new InvalidCredentialsException();
      });

    if (!passwordEncoder.matches(password, user.getPassword())) {
      log.warn("Failed login for user: {}", username);
      throw new InvalidCredentialsException();
    }

    log.debug("Generated JWT for user: {}", username);
    String token = jwtUtil.generateToken(
      user.getUsername(),
      user.getRole().name(),
      user.getPublicId().toString()
    );

    return new LoginResponse(token);
  }
}
