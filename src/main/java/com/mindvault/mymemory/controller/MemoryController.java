package com.mindvault.mymemory.controller;

import com.mindvault.mymemory.entity.Memory;
import com.mindvault.mymemory.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;

    // Explicit Constructor for Dependency Injection (replaces @RequiredArgsConstructor)
    @Autowired
    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    // Create Memory
    @PostMapping
    public ResponseEntity<Memory> createMemory(@RequestBody Memory memory) {
        Memory created = memoryService.createMemory(memory);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Get List and Search
    @GetMapping("")
    public ResponseEntity<List<Memory>> getMemories(
            @RequestParam(required = false) String keyword) {
        
        if (keyword != null && !keyword.isBlank()) {
            return ResponseEntity.ok(memoryService.searchMemories(keyword));
        }
        return ResponseEntity.ok(memoryService.getAllUserMemories());
    }
    
    // Get Single Memory
    @GetMapping("/{id}")
    public ResponseEntity<Memory> getMemory(@PathVariable Long id) {
        Memory memory = memoryService.getMemoryById(id);
        return ResponseEntity.ok(memory);
    }

    // Update Memory
    @PutMapping("/{id}")
    public ResponseEntity<Memory> updateMemory(@PathVariable Long id, @RequestBody Memory memoryDetails) {
        Memory updated = memoryService.updateMemory(id, memoryDetails);
        return ResponseEntity.ok(updated);
    }

    // Delete Memory
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMemory(@PathVariable Long id) {
        memoryService.deleteMemory(id);
        return ResponseEntity.noContent().build();
    }
}