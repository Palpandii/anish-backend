package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.TaxRecord;
import com.ascrackers.backend.repository.TaxRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Admin-only (see AdminAuthFilter) — GST / income tax / other filing & payment tracker.
// "Overdue" is intentionally not a stored status — see TaxRecord for why.
@RestController
@RequestMapping("/api/taxes")
public class TaxController {

    private final TaxRecordRepository taxRepo;

    public TaxController(TaxRecordRepository taxRepo) {
        this.taxRepo = taxRepo;
    }

    @GetMapping
    public List<TaxRecord> getAll() {
        return taxRepo.findAll().stream()
                .sorted(Comparator.comparing(TaxRecord::getDueDate,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    @PostMapping
    public TaxRecord create(@RequestBody TaxRecord record) {
        record.setId(null);
        normalize(record);
        record.setCreatedAt(LocalDateTime.now());
        return taxRepo.save(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxRecord> update(@PathVariable Long id, @RequestBody TaxRecord updated) {
        normalize(updated);
        return taxRepo.findById(id).map(existing -> {
            existing.setTaxType(updated.getTaxType());
            existing.setPeriod(updated.getPeriod());
            existing.setAmount(updated.getAmount());
            existing.setDueDate(updated.getDueDate());
            existing.setPaidDate(updated.getPaidDate());
            existing.setStatus(updated.getStatus());
            existing.setReferenceNumber(updated.getReferenceNumber());
            existing.setNotes(updated.getNotes());
            return ResponseEntity.ok(taxRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!taxRepo.existsById(id)) return ResponseEntity.notFound().build();
        taxRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Status follows paidDate rather than trusting whatever the client sent.
    private void normalize(TaxRecord r) {
        r.setStatus(r.getPaidDate() != null ? "PAID" : "PENDING");
    }
}