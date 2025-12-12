package com.mindvault.mymemory.repository;

import com.mindvault.mymemory.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

      //Authorization Check: Finds a single memory by ID AND User ID.
    Optional<Memory> findByIdAndUserId(Long id, Long userId);

      //Read All: Gets all memories belonging to a specific user.
    List<Memory> findByUserIdOrderByCreatedAtDesc(Long userId);
    

     // Search: by title or content for the logged-in user.
    @Query("SELECT m FROM Memory m WHERE m.user.id = :userId AND (" +
           "  LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR" +
           "  LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
           ") ORDER BY m.createdAt DESC")
    List<Memory> searchUserMemories(@Param("userId") Long userId, @Param("keyword") String keyword);
}