package com.mindvault.mymemory.service;

import com.mindvault.mymemory.entity.Memory;
import com.mindvault.mymemory.entity.User;
import com.mindvault.mymemory.repository.MemoryRepository;
import com.mindvault.mymemory.repository.UserRepository; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
public class MemoryService {

    private static final Logger log = LoggerFactory.getLogger(MemoryService.class);

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;

    public MemoryService(MemoryRepository memoryRepository, UserRepository userRepository) {
        this.memoryRepository = memoryRepository;
        this.userRepository = userRepository;
    }

    // ---------------- SAFER getCurrentAuthenticatedUser ----------------
    private User getCurrentAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            log.warn("No authenticated user found");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        String username = auth.getName();

        log.info("Authenticated username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found: " + username));
    }
    // ---------------------------------------------------------------------

    // Create Memory
    public Memory createMemory(Memory memory) {
        User currentUser = getCurrentAuthenticatedUser();
        memory.setUser(currentUser);

        log.info("Creating memory for user {}: title={}, content={}", currentUser.getId(), memory.getTitle(), memory.getContent());

        return memoryRepository.save(memory);
    }

    // Get Single Memory (ownership check)
    public Memory getMemoryById(Long memoryId) {
        Long userId = getCurrentAuthenticatedUser().getId();
        log.info("Getting memory {} for user {}", memoryId, userId);

        return memoryRepository.findByIdAndUser_Id(memoryId, userId)
                .orElseThrow(() -> {
                    log.warn("Memory {} not found or not owned by user {}", memoryId, userId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Memory not found or access denied");
                });
    }

    // Get All User Memories (PAGINATION)
    public Page<Memory> getAllUserMemories(int page, int size) {
        Long userId = getCurrentAuthenticatedUser().getId();
        Pageable pageable = PageRequest.of(page, size);

        log.info("Getting memories for user {}: page={}, size={}", userId, page, size);

        return memoryRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);
    }

    // Update Memory
    public Memory updateMemory(Long memoryId, Memory updatedDetails) {
        Memory existingMemory = getMemoryById(memoryId);
        existingMemory.setTitle(updatedDetails.getTitle());
        existingMemory.setContent(updatedDetails.getContent());
        existingMemory.setImageUrl(updatedDetails.getImageUrl());

        return memoryRepository.save(existingMemory);
    }

    // Delete Memory
    public void deleteMemory(Long memoryId) {
        Long userId = getCurrentAuthenticatedUser().getId();
        memoryRepository.deleteByIdAndUser_Id(memoryId, userId);
    }

    // Search Memories (PAGINATION)
    public Page<Memory> searchMemories(String keyword, int page, int size) {
        Long userId = getCurrentAuthenticatedUser().getId();
        Pageable pageable = PageRequest.of(page, size);

        if (keyword == null || keyword.isBlank()) {
            return getAllUserMemories(page, size);
        }

        return memoryRepository.searchUserMemories(userId, keyword.trim(), pageable);
    }

    public Page<Memory> searchMemories(
            String q,
            String category,
            Pageable pageable
    ) {
        Long userId = getCurrentAuthenticatedUser().getId();

        if ((q == null || q.isBlank()) && (category == null || category.isBlank())) {
            return memoryRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);
        }

        String keyword = q == null ? "" : q.trim();

        if (category != null && !category.isBlank()) {
            return memoryRepository.searchByTitleAndCategory(
                    userId,
                    keyword,
                    category.trim(),
                    pageable
            );
        }

        return memoryRepository.searchByTitleOrCategory(
                userId,
                keyword,
                pageable
        );
    }

}
