package com.ascrackers.backend.controller;

import com.ascrackers.backend.dto.LoginRequest;
import com.ascrackers.backend.dto.LoginResponse;
import com.ascrackers.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    @Value("${app.admin.password}")
    private String adminPassword;

    private final JwtUtil jwtUtil;

    public AdminAuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.getPassword() == null || !request.getPassword().equals(adminPassword)) {
            return ResponseEntity.status(401).body("{\"error\":\"Invalid password\"}");
        }
        String token = jwtUtil.generateToken("admin");
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
