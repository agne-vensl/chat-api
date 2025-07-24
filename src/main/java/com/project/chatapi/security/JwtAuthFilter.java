package com.project.chatapi.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.chatapi.model.User;
import com.project.chatapi.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
  private UserRepository userRepository;
  private JwtUtil jwtUtil;

  public JwtAuthFilter(UserRepository userRepository, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      
      try {
        Jws<Claims> claims = jwtUtil.validateToken(token);

        String username = claims.getBody().getSubject();
        String publicId = claims.getBody().get("publicId").toString();
        String role = claims.getBody().get("role").toString();

        Optional<User> user = userRepository.findActiveUserByPublicId(UUID.fromString(publicId));

        if (
          user.isEmpty() || 
          !user.get().getUsername().equals(username) || 
          !user.get().getRole().toString().equals(role)
        ) {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");
          response.getWriter().write("{\"error\": \"Invalid Token\", \"message\": \"User no longer exists\"}");
          return;
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
          UUID.fromString(publicId),
          username,
          role
        );

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          authenticatedUser,
          null,
          List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
        
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (JwtException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Invalid Token\",\"message\": \"Invalid or expired token\"}");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
