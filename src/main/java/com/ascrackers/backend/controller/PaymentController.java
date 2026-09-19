package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.Payment;
import com.ascrackers.backend.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Admin-only (see AdminAuthFilter) — money received from customers.
// Mirrors PurchaseController's shape; kept as its own table since a payment
// doesn't always map to a single Order/Estimate row (advances, walk-ins).
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentRepository paymentRepo;

    public PaymentController(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @GetMapping
    public List<Payment> getAll() {
        return paymentRepo.findAll().stream()
                .sorted(Comparator.comparing(Payment::getPaymentDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        payment.setId(null);
        normalize(payment);
        payment.setCreatedAt(LocalDateTime.now());
        return paymentRepo.save(payment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment updated) {
        normalize(updated);
        return paymentRepo.findById(id).map(existing -> {
            existing.setCustomerName(updated.getCustomerName());
            existing.setCustomerPhone(updated.getCustomerPhone());
            existing.setReferenceType(updated.getReferenceType());
            existing.setReferenceId(updated.getReferenceId());
            existing.setAmount(updated.getAmount());
            existing.setMethod(updated.getMethod());
            existing.setPaymentDate(updated.getPaymentDate());
            existing.setNotes(updated.getNotes());
            return ResponseEntity.ok(paymentRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!paymentRepo.existsById(id)) return ResponseEntity.notFound().build();
        paymentRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void normalize(Payment p) {
        if (p.getAmount() == null || p.getAmount() < 0) p.setAmount(0.0);
        String type = p.getReferenceType() == null ? "OTHER" : p.getReferenceType().trim().toUpperCase();
        if (!type.equals("ORDER") && !type.equals("ESTIMATE")) type = "OTHER";
        p.setReferenceType(type);
        if (type.equals("OTHER")) p.setReferenceId(null);

        String method = p.getMethod() == null ? "CASH" : p.getMethod().trim().toUpperCase();
        p.setMethod(method);
    }
}