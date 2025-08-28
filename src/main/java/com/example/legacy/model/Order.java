package com.example.legacy.model;

import java.time.Instant;

public class Order {
    private String id;
    private String customerId;
    private double amount;
    private Instant createdAt;

    public Order() {}
    public Order(String id, String customerId, double amount, Instant createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public double getAmount() { return amount; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(String id) { this.id = id; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
