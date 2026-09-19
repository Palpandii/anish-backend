package com.ascrackers.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Staff directory for the Admin > Users tab. This is intentionally NOT wired
// into the login flow yet — today's AdminAuthFilter/JwtUtil checks a single
// admin credential, and swapping that for per-user DB login is a separate,
// higher-stakes change (touches AdminLogin.jsx + the login controller +
// JwtUtil). This module gives the shop owner a place to track who's on the
// team, their role and contact info; wiring individual logins can follow
// once you want it, without reshaping this table.
@Entity
@Table(name = "admin_users")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;

    // OWNER, MANAGER, STAFF, DELIVERY
    private String role;

    // ACTIVE, INACTIVE
    private String status;

    private String notes;

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}