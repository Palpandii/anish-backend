package com.ascrackers.backend.controller;

import com.ascrackers.backend.model.Estimate;
import com.ascrackers.backend.model.EstimateItem;
import com.ascrackers.backend.repository.EstimateRepository;
import com.ascrackers.backend.service.PdfLine;
import com.ascrackers.backend.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estimates")
public class EstimateController {

    private final EstimateRepository repo;
    private final PdfService pdfService;

    public EstimateController(EstimateRepository repo, PdfService pdfService) {
        this.repo = repo;
        this.pdfService = pdfService;
    }

    @GetMapping
    public List<Estimate> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Estimate create(@RequestBody Estimate estimate) {
        return saveWithTotals(estimate);
    }

    @PutMapping("/{id}")
    public Estimate update(@PathVariable Long id, @RequestBody Estimate incoming) {
        Estimate existing = repo.findById(id).orElseThrow();

        existing.setCustomerName(incoming.getCustomerName());
        existing.setCustomerPhone(incoming.getCustomerPhone());
        existing.setCustomerCity(incoming.getCustomerCity());

        // Replace line items in place so cascade/orphanRemoval cleans up old rows.
        existing.getItems().clear();
        if (incoming.getItems() != null) {
            for (EstimateItem item : incoming.getItems()) {
                item.setId(null);
                item.setEstimate(existing);
                existing.getItems().add(item);
            }
        }

        return saveWithTotals(existing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id) {
        Estimate estimate = repo.findById(id).orElseThrow();
        List<PdfLine> lines = estimate.getItems().stream()
                .map(i -> new PdfLine(i.getProductName(), i.getQuantity(), i.getUnitPrice()))
                .collect(Collectors.toList());

        byte[] pdf = pdfService.generateDocument(
                "Estimate", estimate.getId(), estimate.getCustomerName(), estimate.getCustomerPhone(),
                null, estimate.getCreatedAt(), lines, estimate.getTotalAmount());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=estimate-" + id + ".pdf")
                .body(pdf);
    }

    private Estimate saveWithTotals(Estimate estimate) {
        double total = 0;
        if (estimate.getItems() != null) {
            for (EstimateItem item : estimate.getItems()) {
                item.setEstimate(estimate);
                total += item.getQuantity() * item.getUnitPrice();
            }
        }
        estimate.setTotalAmount(total);
        return repo.save(estimate);
    }
}