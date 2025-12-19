package com.mindvault.mymemory.repository;

import com.mindvault.mymemory.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    //  Authorization
    Optional<Memory> findByIdAndUser_Id(Long id, Long userId);

    // All memories of user (pagination)
    Page<Memory> findByUser_Id(Long userId, Pageable pageable);

    // All memories of user (sorted by date DESC)
    Page<Memory> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // Search (pagination)
    @Query("""
        SELECT m FROM Memory m
        WHERE m.user.id = :userId
          AND (
              LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        ORDER BY m.createdAt DESC
    """)
    Page<Memory> searchUserMemories(
            @Param("userId") Long userId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT m FROM Memory m
        JOIN m.category c
        WHERE m.user.id = :userId
        AND (
            LOWER(m.title) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(c.name)  LIKE LOWER(CONCAT('%', :q, '%'))
        )
        ORDER BY m.createdAt DESC
    """)
    Page<Memory> searchByTitleOrCategory(
            @Param("userId") Long userId,
            @Param("q") String q,
            Pageable pageable
    );

    @Query("""
        SELECT m FROM Memory m
        JOIN m.category c
        WHERE m.user.id = :userId
        AND LOWER(c.name) = LOWER(:category)
        AND LOWER(m.title) LIKE LOWER(CONCAT('%', :q, '%'))
        ORDER BY m.createdAt DESC
    """)
    Page<Memory> searchByTitleAndCategory(
            @Param("userId") Long userId,
            @Param("q") String q,
            @Param("category") String category,
            Pageable pageable
    );

    // Check ownership
    boolean existsByIdAndUser_Id(Long id, Long userId);

    // Delete memory by user (safe delete)
    void deleteByIdAndUser_Id(Long id, Long userId);

    //  Count memories of a user
    long countByUserId(Long userId);
}
