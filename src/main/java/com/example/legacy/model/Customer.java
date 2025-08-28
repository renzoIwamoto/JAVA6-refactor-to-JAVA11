package com.example.legacy.model;

import java.util.Objects;

public record Customer(String id, String name, String email) {
    public Customer {
        Objects.requireNonNull(id, "Customer ID cannot be null");
        Objects.requireNonNull(name, "Customer name cannot be null");
        Objects.requireNonNull(email, "Customer email cannot be null");
        
        if (id.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be blank");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("Customer email cannot be blank");
        }
    }
}
