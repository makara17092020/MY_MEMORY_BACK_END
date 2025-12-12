package com.mindvault.mymemory.service;

import com.mindvault.mymemory.entity.Memory;
import com.mindvault.mymemory.entity.User; 
import com.mindvault.mymemory.repository.MemoryRepository;
import com.mindvault.mymemory.repository.UserRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

@Service
@Transactional
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository; 

    @Autowired
    public MemoryService(MemoryRepository memoryRepository, UserRepository userRepository) {
        this.memoryRepository = memoryRepository;
        this.userRepository = userRepository;
    }

    
    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2. Look up the full User object using the username from the database
        // This links the current API request to the User entity.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found: " + username));
    }
    
    //  Create Memory
    public Memory createMemory(Memory memory) {
        User currentUser = getCurrentAuthenticatedUser();
        memory.setUser(currentUser); // Assign memory to the logged-in user
        return memoryRepository.save(memory);
    }

    //  Get Single Memory (Authorization Check)
    public Memory getMemoryById(Long memoryId) {
        Long userId = getCurrentAuthenticatedUser().getId();
        
        // Uses findByIdAndUserId to enforce ownership
        return memoryRepository.findByIdAndUserId(memoryId, userId)
               .orElseThrow(() -> new RuntimeException("Memory not found or access denied.")); // Use custom exception here
    }

    //  Get All User Memories (Authorization Check)
    public List<Memory> getAllUserMemories() {
        Long userId = getCurrentAuthenticatedUser().getId();
        return memoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // U: Update Memory (Authorization Check)
    public Memory updateMemory(Long memoryId, Memory updatedDetails) {
        // Enforces ownership via getMemoryById call
        Memory existingMemory = getMemoryById(memoryId); 

        existingMemory.setTitle(updatedDetails.getTitle());
        existingMemory.setContent(updatedDetails.getContent());
        existingMemory.setImageUrl(updatedDetails.getImageUrl());
        // User/owner field remains unchanged
        
        return memoryRepository.save(existingMemory);
    }

    // D: Delete Memory (Authorization Check)
    public void deleteMemory(Long memoryId) {
        // Enforces ownership via getMemoryById call
        Memory memoryToDelete = getMemoryById(memoryId); 
        memoryRepository.delete(memoryToDelete);
    }

    // --- Search Operation ---
    
    public List<Memory> searchMemories(String keyword) {
        Long userId = getCurrentAuthenticatedUser().getId();

        if (keyword == null || keyword.isBlank()) {
            return getAllUserMemories();
        }
        
        return memoryRepository.searchUserMemories(userId, keyword.trim());
    }
}