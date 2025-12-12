package com.mindvault.mymemory.controller;

import com.mindvault.mymemory.entity.User;
import com.mindvault.mymemory.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

   
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Create new user
    // @PostMapping
    // public ResponseEntity<User> create(@RequestBody User user) {
    //     User createdUser = userService.create(user);
    //     return ResponseEntity.ok(createdUser);
    // }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Get current authenticated user
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(currentUser);
    }
}
