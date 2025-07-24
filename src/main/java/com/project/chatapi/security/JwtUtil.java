package com.project.chatapi.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
  private final Key key;
  @Value("${jwt.expiration}")
  private long expirationMs;
  
  public JwtUtil(@Value("${jwt.secret}") String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String generateToken(String username, String role, String publicId) {
    return Jwts.builder()
      .setSubject(username)
      .addClaims(Map.of(
              "role", role,
              "publicId", publicId
      ))
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
      .signWith(key)
      .compact();
  }

  public Jws<Claims> validateToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(key)
      .build()
      .parseClaimsJws(token);
  }
}
