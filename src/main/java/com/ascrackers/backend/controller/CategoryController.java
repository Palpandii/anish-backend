package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.Category;
import com.ascrackers.backend.repository.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Category> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) {
        if (category.getNameEn() == null || category.getNameEn().isBlank()) {
            return ResponseEntity.badRequest().body("Name (EN) is required");
        }

        String baseId = category.getNameEn().trim().toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");

        String id = baseId;
        int suffix = 1;
        while (repo.existsById(id)) {
            id = baseId + "-" + suffix;
            suffix++;
        }

        category.setId(id);
        return ResponseEntity.ok(repo.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable String id, @RequestBody Category updated) {
        return repo.findById(id).map(existing -> {
            existing.setNameEn(updated.getNameEn());
            existing.setNameTa(updated.getNameTa());
            existing.setImage(updated.getImage());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}