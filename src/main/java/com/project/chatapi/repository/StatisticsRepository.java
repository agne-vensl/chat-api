package com.project.chatapi.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.chatapi.model.Message;

public interface StatisticsRepository extends JpaRepository<Message, Long> {
  @Query(value = """
    SELECT MIN(m.created_at)
    FROM messages m
    JOIN users u ON m.user_id = u.id
    WHERE u.public_id = :publicId
    """, nativeQuery = true)
  LocalDateTime getFirstMessageTime(@Param("publicId") UUID publicId);

  @Query(value = """
    SELECT MAX(m.created_at)
    FROM messages m
    JOIN users u ON m.user_id = u.id
    WHERE u.public_id = :publicId
    """, nativeQuery = true)
  LocalDateTime getLastMessageTime(@Param("publicId") UUID publicId);

  @Query(value = """
    SELECT COUNT(*)
    FROM messages m
    JOIN users u ON m.user_id = u.id
    WHERE u.public_id = :publicId
    """, nativeQuery = true)
  long getTotalMessageCount(@Param("publicId") UUID publicId);

  @Query(value = """
    SELECT AVG(LENGTH(m.content))
    FROM messages m
    JOIN users u ON m.user_id = u.id
    WHERE u.public_id = :publicId
    """, nativeQuery = true)
  Double getAverageMessageLength(@Param("publicId") UUID publicId);

  @Query(value = """
    SELECT m.content
    FROM messages m
    JOIN users u ON m.user_id = u.id
    WHERE u.public_id = :publicId
    ORDER BY m.created_at DESC
    LIMIT 1
    """, nativeQuery = true)
  String getLastMessageContent(@Param("publicId") UUID publicId);
}
