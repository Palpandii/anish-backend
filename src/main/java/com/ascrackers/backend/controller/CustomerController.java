package com.ascrackers.backend.controller;

import com.ascrackers.backend.dto.CustomerSummary;
import com.ascrackers.backend.model.Estimate;
import com.ascrackers.backend.model.Order;
import com.ascrackers.backend.repository.EstimateRepository;
import com.ascrackers.backend.repository.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final OrderRepository orderRepo;
    private final EstimateRepository estimateRepo;

    public CustomerController(OrderRepository orderRepo, EstimateRepository estimateRepo) {
        this.orderRepo = orderRepo;
        this.estimateRepo = estimateRepo;
    }

    @GetMapping
    public List<CustomerSummary> getAll() {
        Map<String, CustomerSummary> byKey = new LinkedHashMap<>();

        for (Order o : orderRepo.findAll()) {
            String key = key(o.getCustomerName(), o.getCustomerPhone());
            if (key == null) continue;
            CustomerSummary summary = byKey.computeIfAbsent(key,
                    k -> new CustomerSummary(o.getCustomerName(), o.getCustomerPhone(), o.getCustomerAddress()));
            summary.addOrder(o.getTotalAmount() == null ? 0 : o.getTotalAmount(), o.getCreatedAt());
        }

        for (Estimate e : estimateRepo.findAll()) {
            String key = key(e.getCustomerName(), e.getCustomerPhone());
            if (key == null) continue;
            CustomerSummary summary = byKey.computeIfAbsent(key,
                    k -> new CustomerSummary(e.getCustomerName(), e.getCustomerPhone(), e.getCustomerCity()));
            summary.addEstimate(e.getTotalAmount() == null ? 0 : e.getTotalAmount(), e.getCreatedAt());
        }

        return byKey.values().stream()
                .sorted(Comparator.comparing(CustomerSummary::getLastActivity,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    private String key(String name, String phone) {
        String n = name == null ? "" : name.trim().toLowerCase();
        String p = phone == null ? "" : phone.trim();
        if (n.isEmpty() && p.isEmpty()) return null;
        return p.isEmpty() ? "name:" + n : "phone:" + p;
    }
}