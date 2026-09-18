package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.Product;
import com.ascrackers.backend.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    // Public - storefront uses this
    @GetMapping
    public List<Product> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin-protected (filter checks the token for non-GET requests)
    @PostMapping
    public Product create(@RequestBody Product product) {
        return repo.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product updated) {
        return repo.findById(id).map(existing -> {
            existing.setCategory(updated.getCategory());
            existing.setNameEn(updated.getNameEn());
            existing.setNameTa(updated.getNameTa());
            existing.setQtyUnit(updated.getQtyUnit());
            existing.setMrp(updated.getMrp());
            existing.setPrice(updated.getPrice());
            existing.setImage(updated.getImage());
            existing.setYoutubeId(updated.getYoutubeId());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
