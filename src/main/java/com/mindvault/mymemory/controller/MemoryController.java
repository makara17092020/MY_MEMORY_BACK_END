package com.mindvault.mymemory.controller;

import com.mindvault.mymemory.entity.Memory;
import com.mindvault.mymemory.dto.ApiResponse;
import com.mindvault.mymemory.service.MemoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;

    // Explicit Constructor for Dependency Injection (replaces @RequiredArgsConstructor)
    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

    //  Create Memory
    @PostMapping
    public ResponseEntity<ApiResponse<Memory>> createMemory(@RequestBody Memory memory) {
        Memory created = memoryService.createMemory(memory);
        ApiResponse<Memory> response = new ApiResponse<>(
                "Memory created successfully",
                true,
                java.util.List.of(created),
                new ApiResponse.Meta(0, 1, 1, 1, 1, 1)
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get All Memories (PAGINATION + SEARCH)
    @GetMapping
    public ResponseEntity<ApiResponse<Memory>> getMemories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        Page<Memory> memoryPage;
        if (keyword != null && !keyword.isBlank()) {
            memoryPage = memoryService.searchMemories(keyword, page, size);
        } else {
            memoryPage = memoryService.getAllUserMemories(page, size);
        }

        ApiResponse.Meta meta = new ApiResponse.Meta(
                memoryPage.getNumber(),
                memoryPage.getSize(),
                memoryPage.getTotalPages(),
                memoryPage.getTotalElements(),
                memoryPage.getTotalElements(),
                memoryPage.getSize()
        );

        ApiResponse<Memory> response = new ApiResponse<>(
                "success",
                true,
                memoryPage.getContent(),
                meta
        );

        return ResponseEntity.ok(response);
    }

    // Get Single Memory
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Memory>> getMemory(@PathVariable Long id) {
        Memory memory = memoryService.getMemoryById(id);
        ApiResponse<Memory> response = new ApiResponse<>(
                "success",
                true,
                java.util.List.of(memory),
                new ApiResponse.Meta(0, 1, 1, 1, 1, 1)
        );
        return ResponseEntity.ok(response);
    }

    // Update Memory
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Memory>> updateMemory(
            @PathVariable Long id,
            @RequestBody Memory memoryDetails
    ) {
        Memory updated = memoryService.updateMemory(id, memoryDetails);
        ApiResponse<Memory> response = new ApiResponse<>(
                "Memory updated successfully",
                true,
                java.util.List.of(updated),
                new ApiResponse.Meta(0, 1, 1, 1, 1, 1)
        );
        return ResponseEntity.ok(response);
    }

    // Delete Memory
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMemory(@PathVariable Long id) {
        memoryService.deleteMemory(id);
        ApiResponse<Void> response = new ApiResponse<>(
                "Memory deleted successfully",
                true,
                java.util.List.of(),
                new ApiResponse.Meta(0, 0, 0, 0, 0, 0)
        );
        return ResponseEntity.ok(response);
    }
}
