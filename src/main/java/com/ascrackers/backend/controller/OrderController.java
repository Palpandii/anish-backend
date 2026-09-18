package com.ascrackers.backend.controller;

import com.ascrackers.backend.dto.StatusUpdateRequest;
import com.ascrackers.backend.model.Order;
import com.ascrackers.backend.model.OrderItem;
import com.ascrackers.backend.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    // Public - customer's storefront checkout creates an order
    @PostMapping
    public Order create(@RequestBody Order order) {
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                item.setOrder(order);
            }
        }
        return repo.save(order);
    }

    // Admin views orders
    @GetMapping
    public List<Order> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOne(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Admin updates order status
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        return repo.findById(id).map(order -> {
            order.setStatus(request.getStatus());
            return ResponseEntity.ok(repo.save(order));
        }).orElse(ResponseEntity.notFound().build());
    }
}
