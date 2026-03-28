package com.barbo.barboapp.controller;

import com.barbo.barboapp.entity.User;
import com.barbo.barboapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users/register — Public
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User registered = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    // GET /api/users — ADMIN only
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/{id} — ADMIN or own profile
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id,
                                         Authentication authentication) {
        String loggedInEmail = authentication.getName();
        User existingUser = userService.getUserById(id);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !existingUser.getEmail().equals(loggedInEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only view your own profile");
        }

        return ResponseEntity.ok(existingUser);
    }

    // GET /api/users/email/{email} — ADMIN only
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email,
                                            Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN can search users by email");
        }

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    // PUT /api/users/{id} — ADMIN or own profile only
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id,
                                        @Valid @RequestBody User user,
                                        Authentication authentication) {
        String loggedInEmail = authentication.getName();
        User existingUser = userService.getUserById(id);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !existingUser.getEmail().equals(loggedInEmail)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only update your own profile");
        }

        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // DELETE /api/users/{id} — ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id,
                                        Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN can delete users");
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}