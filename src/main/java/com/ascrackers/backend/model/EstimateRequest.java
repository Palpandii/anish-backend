package com.ascrackers.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// A customer-submitted "get a quote" request from the storefront — separate
// from the existing Estimate entity (which is the admin's own formal
// estimate/quotation). This is the raw inbound lead; the admin reviews it
// here and, once ready, creates the real Estimate manually the same way as
// today. Kept as its own table so this ships without touching the existing
// Estimate model/controller.
@Entity
@Table(name = "estimate_requests")
public class EstimateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerPhone;
    private String customerCity;

    // What they're celebrating — wedding, temple festival, birthday, etc.
    private String eventType;
    private LocalDate eventDate;

    // Free-text: what they want, rough budget, quantities in mind…
    @Column(length = 2000)
    private String requirements;

    // NEW, CONTACTED, CONVERTED, CLOSED
    private String status;

    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerCity() { return customerCity; }
    public void setCustomerCity(String customerCity) { this.customerCity = customerCity; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}