package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.Expense;
import com.ascrackers.backend.repository.ExpenseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Admin-only (see AdminAuthFilter) — shop expenses (rent, salary, utilities, etc.)
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepo;

    public ExpenseController(ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
    }

    @GetMapping
    public List<Expense> getAll() {
        return expenseRepo.findAll().stream()
                .sorted(Comparator.comparing(Expense::getExpenseDate,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @PostMapping
    public Expense create(@RequestBody Expense expense) {
        expense.setId(null);
        expense.setCreatedAt(LocalDateTime.now());
        return expenseRepo.save(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable Long id, @RequestBody Expense updated) {
        return expenseRepo.findById(id).map(existing -> {
            existing.setCategory(updated.getCategory());
            existing.setTitle(updated.getTitle());
            existing.setPaidTo(updated.getPaidTo());
            existing.setAmount(updated.getAmount());
            existing.setExpenseDate(updated.getExpenseDate());
            existing.setPaymentMode(updated.getPaymentMode());
            existing.setNotes(updated.getNotes());
            return ResponseEntity.ok(expenseRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!expenseRepo.existsById(id)) return ResponseEntity.notFound().build();
        expenseRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}