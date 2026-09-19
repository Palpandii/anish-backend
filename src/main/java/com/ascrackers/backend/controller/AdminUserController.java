package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.AdminUser;
import com.ascrackers.backend.repository.AdminUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Admin-only (see AdminAuthFilter) — staff directory. See AdminUser.java for
// why this doesn't touch login/auth yet.
@RestController
@RequestMapping("/api/admin-users")
public class AdminUserController {

    private final AdminUserRepository userRepo;

    public AdminUserController(AdminUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<AdminUser> getAll() {
        return userRepo.findAll().stream()
                .sorted(Comparator.comparing(AdminUser::getName,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .collect(Collectors.toList());
    }

    @PostMapping
    public AdminUser create(@RequestBody AdminUser user) {
        user.setId(null);
        normalize(user);
        user.setCreatedAt(LocalDateTime.now());
        return userRepo.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> update(@PathVariable Long id, @RequestBody AdminUser updated) {
        normalize(updated);
        return userRepo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setPhone(updated.getPhone());
            existing.setEmail(updated.getEmail());
            existing.setRole(updated.getRole());
            existing.setStatus(updated.getStatus());
            existing.setNotes(updated.getNotes());
            return ResponseEntity.ok(userRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!userRepo.existsById(id)) return ResponseEntity.notFound().build();
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void normalize(AdminUser u) {
        String role = u.getRole() == null ? "STAFF" : u.getRole().trim().toUpperCase();
        u.setRole(role);
        String status = u.getStatus() == null ? "ACTIVE" : u.getStatus().trim().toUpperCase();
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) status = "ACTIVE";
        u.setStatus(status);
    }
}