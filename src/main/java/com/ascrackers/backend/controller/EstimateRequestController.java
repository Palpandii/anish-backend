package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.EstimateRequest;
import com.ascrackers.backend.repository.EstimateRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// POST is public (customers submit this from the storefront with no login —
// same pattern as public order creation). GET/PUT/DELETE carry customer PII
// so they're admin-only: add "/api/estimate-requests" to AdminAuthFilter's
// isAdminOnlyPath, same as orders/estimates/customers, and add this class's
// POST to the isPublicOrderCreate-style allowlist (see AdminAuthFilter.java
// in this drop for the exact lines).
@RestController
@RequestMapping("/api/estimate-requests")
public class EstimateRequestController {

    private final EstimateRequestRepository requestRepo;

    public EstimateRequestController(EstimateRequestRepository requestRepo) {
        this.requestRepo = requestRepo;
    }

    @GetMapping
    public List<EstimateRequest> getAll() {
        return requestRepo.findAll().stream()
                .sorted(Comparator.comparing(EstimateRequest::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    // Public — the storefront "Get a free estimate" form hits this with no token.
    @PostMapping
    public EstimateRequest create(@RequestBody EstimateRequest request) {
        request.setId(null);
        request.setStatus("NEW");
        request.setCreatedAt(LocalDateTime.now());
        return requestRepo.save(request);
    }

    // Admin updates status as they work the lead (NEW -> CONTACTED -> CONVERTED/CLOSED).
    @PutMapping("/{id}/status")
    public ResponseEntity<EstimateRequest> updateStatus(@PathVariable Long id, @RequestBody StatusUpdate body) {
        String status = body.status == null ? "" : body.status.trim().toUpperCase();
        if (!List.of("NEW", "CONTACTED", "CONVERTED", "CLOSED").contains(status)) {
            return ResponseEntity.badRequest().build();
        }
        return requestRepo.findById(id).map(existing -> {
            existing.setStatus(status);
            return ResponseEntity.ok(requestRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!requestRepo.existsById(id)) return ResponseEntity.notFound().build();
        requestRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public static class StatusUpdate {
        public String status;
    }
}