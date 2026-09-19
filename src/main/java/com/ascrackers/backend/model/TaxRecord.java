package com.ascrackers.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// One tax filing/payment obligation — GST, income tax, professional tax, etc.
// "Overdue" is not stored; it's derived on the frontend from dueDate vs today
// so it's always accurate without a background job to flip a stale flag.
@Entity
@Table(name = "tax_records")
public class TaxRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taxType;     // GST, INCOME_TAX, PROFESSIONAL_TAX, OTHER
    private String period;      // e.g. "Sep 2026" or "Q2 2026-27"
    private Double amount;
    private LocalDate dueDate;
    private LocalDate paidDate; // null until paid
    private String status;      // PENDING, PAID  (OVERDUE is computed, not stored)
    private String referenceNumber; // GSTIN/challan/ARN etc.
    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();

    public TaxRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTaxType() { return taxType; }
    public void setTaxType(String taxType) { this.taxType = taxType; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}