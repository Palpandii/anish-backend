package com.ascrackers.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AdminAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean isLoginEndpoint = path.equals("/api/admin/login");
        boolean isAdminOnlyPath = path.startsWith("/api/orders") || path.startsWith("/api/estimates")
                || path.startsWith("/api/customers");
        boolean isPublicGet = "GET".equalsIgnoreCase(method) && path.startsWith("/api/") && !isAdminOnlyPath;
        boolean isPreflight = "OPTIONS".equalsIgnoreCase(method);
        boolean isPublicOrderCreate = "POST".equalsIgnoreCase(method) && path.equals("/api/orders");

        boolean needsAuth = path.startsWith("/api/") && !isLoginEndpoint && !isPublicGet
                && !isPreflight && !isPublicOrderCreate;

        if (needsAuth) {
            String authHeader = request.getHeader("Authorization");
            String token = (authHeader != null && authHeader.startsWith("Bearer "))
                    ? authHeader.substring(7) : null;

            if (token == null || !jwtUtil.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Unauthorized. Please log in again.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}