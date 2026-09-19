package com.ascrackers.backend.dto;

import java.time.LocalDateTime;

public class CustomerSummary {
    private String name;
    private String phone;
    private String address;
    private int orderCount;
    private int estimateCount;
    private double totalSpent;
    private LocalDateTime lastActivity;

    public CustomerSummary(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public int getOrderCount() { return orderCount; }
    public void setOrderCount(int orderCount) { this.orderCount = orderCount; }

    public int getEstimateCount() { return estimateCount; }
    public void setEstimateCount(int estimateCount) { this.estimateCount = estimateCount; }

    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }

    public LocalDateTime getLastActivity() { return lastActivity; }
    public void setLastActivity(LocalDateTime lastActivity) { this.lastActivity = lastActivity; }

    public void addOrder(double amount, LocalDateTime when) {
        this.orderCount++;
        this.totalSpent += amount;
        bumpLastActivity(when);
    }

    public void addEstimate(double amount, LocalDateTime when) {
        this.estimateCount++;
        bumpLastActivity(when);
    }

    private void bumpLastActivity(LocalDateTime when) {
        if (when != null && (lastActivity == null || when.isAfter(lastActivity))) {
            lastActivity = when;
        }
    }
}