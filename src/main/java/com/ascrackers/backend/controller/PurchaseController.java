package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.Purchase;
import com.ascrackers.backend.repository.PurchaseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Admin-only (see AdminAuthFilter) — raw material / stock purchases from suppliers
@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseRepository purchaseRepo;

    public PurchaseController(PurchaseRepository purchaseRepo) {
        this.purchaseRepo = purchaseRepo;
    }

    @GetMapping
    public List<Purchase> getAll() {
        return purchaseRepo.findAll().stream()
                .sorted(Comparator.comparing(Purchase::getPurchaseDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Purchase create(@RequestBody Purchase purchase) {
        purchase.setId(null);
        normalize(purchase);
        purchase.setCreatedAt(LocalDateTime.now());
        return purchaseRepo.save(purchase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Purchase> update(@PathVariable Long id, @RequestBody Purchase updated) {
        normalize(updated);
        return purchaseRepo.findById(id).map(existing -> {
            existing.setSupplierName(updated.getSupplierName());
            existing.setSupplierPhone(updated.getSupplierPhone());
            existing.setItemName(updated.getItemName());
            existing.setQuantity(updated.getQuantity());
            existing.setUnit(updated.getUnit());
            existing.setUnitPrice(updated.getUnitPrice());
            existing.setTotalAmount(updated.getTotalAmount());
            existing.setPurchaseDate(updated.getPurchaseDate());
            existing.setPaymentStatus(updated.getPaymentStatus());
            existing.setAmountPaid(updated.getAmountPaid());
            existing.setNotes(updated.getNotes());
            return ResponseEntity.ok(purchaseRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!purchaseRepo.existsById(id)) return ResponseEntity.notFound().build();
        purchaseRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Total is derived from qty * unit price server-side so a stale/tampered
    // client value can never drift from what was actually entered.
    private void normalize(Purchase p) {
        double qty = p.getQuantity() == null ? 0 : p.getQuantity();
        double price = p.getUnitPrice() == null ? 0 : p.getUnitPrice();
        p.setTotalAmount(qty * price);
        if (p.getPaymentStatus() == null || p.getPaymentStatus().isBlank()) {
            p.setPaymentStatus("UNPAID");
        }
        if (p.getAmountPaid() == null) p.setAmountPaid(0.0);
    }
}