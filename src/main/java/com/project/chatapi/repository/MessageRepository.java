package com.project.chatapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.chatapi.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  @Query(value = """
    SELECT m.id, m.content, m.created_at, u.public_id as user_public_id, u.username 
    FROM messages m 
    JOIN users u ON m.user_id = u.id 
    ORDER BY m.created_at DESC
    """, nativeQuery = true)
  public List<Message> getAllMessages();

  @Modifying
  @Query(value = """
    INSERT INTO messages (user_id, content, created_at) 
    VALUES (:userId, :content, :createdAt)
    """, nativeQuery = true)
  public void insertMessage(
    @Param("userId") Long id,
    @Param("content") String content,
    @Param("createdAt") LocalDateTime createdAt
  );
}
