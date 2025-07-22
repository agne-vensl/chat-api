package com.project.chatapi.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.chatapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query(value = "SELECT * FROM users u WHERE public_id = :publicId", nativeQuery = true)
  public Optional<User> findByPublicId(@Param("publicId") UUID publicId);

  @Query(value = "SELECT * FROM users u WHERE LOWER(u.username) = LOWER(:username) AND deleted = false",
    nativeQuery = true)
  public Optional<User> findActiveUserByUsername(@Param("username") String username);

  @Modifying
  @Query(value = """
    INSERT INTO users (public_id, username, password, role, deleted) VALUES (:publicId, :username, :password, :role, :deleted)
    """, nativeQuery = true)
  public void insertUser(
    @Param("publicId") UUID publicId,
    @Param("username") String username,
    @Param("password") String password,
    @Param("role") String role,
    @Param("deleted") boolean deleted
  );

  @Modifying
  @Query(value = "UPDATE users SET deleted = true, username = 'anonymous user' WHERE public_id = :publicId", 
    nativeQuery = true)
  public void softDeleteByPublicId(@Param("publicId") UUID publicId);
}
