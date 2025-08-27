package com.example.legacy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Order Model Tests")
class OrderTest {

    @Test
    @DisplayName("Given valid parameters, When creating Order with constructor, Then all fields are set correctly")
    void givenValidParameters_WhenCreatingOrderWithConstructor_ThenAllFieldsAreSetCorrectly() {
        // Given
        String id = "order-123";
        String customerId = "customer-456";
        double amount = 199.99;
        Instant createdAt = Instant.parse("2024-01-15T10:30:00Z");
        
        // When
        Order order = new Order(id, customerId, amount, createdAt);
        
        // Then
        assertThat(order.getId()).isEqualTo(id);
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getAmount()).isEqualTo(amount);
        assertThat(order.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Given default constructor, When creating Order, Then fields can be set via setters")
    void givenDefaultConstructor_WhenCreatingOrder_ThenFieldsCanBeSetViaSetters() {
        // Given
        Order order = new Order();
        String id = "order-789";
        String customerId = "customer-101";
        double amount = 75.50;
        Instant createdAt = Instant.parse("2024-02-01T14:15:30Z");
        
        // When
        order.setId(id);
        order.setCustomerId(customerId);
        order.setAmount(amount);
        order.setCreatedAt(createdAt);
        
        // Then
        assertThat(order.getId()).isEqualTo(id);
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getAmount()).isEqualTo(amount);
        assertThat(order.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Given zero amount, When creating Order, Then amount is zero")
    void givenZeroAmount_WhenCreatingOrder_ThenAmountIsZero() {
        // Given
        double amount = 0.0;
        
        // When
        Order order = new Order("id", "customer", amount, Instant.now());
        
        // Then
        assertThat(order.getAmount()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Given negative amount, When creating Order, Then negative amount is stored")
    void givenNegativeAmount_WhenCreatingOrder_ThenNegativeAmountIsStored() {
        // Given
        double amount = -100.0;
        
        // When
        Order order = new Order("id", "customer", amount, Instant.now());
        
        // Then
        assertThat(order.getAmount()).isEqualTo(-100.0);
    }

    @Test
    @DisplayName("Given null values, When creating Order, Then null values are stored")
    void givenNullValues_WhenCreatingOrder_ThenNullValuesAreStored() {
        // When
        Order order = new Order(null, null, 100.0, null);
        
        // Then
        assertThat(order.getId()).isNull();
        assertThat(order.getCustomerId()).isNull();
        assertThat(order.getCreatedAt()).isNull();
        assertThat(order.getAmount()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Given very large amount, When creating Order, Then large amount is stored correctly")
    void givenVeryLargeAmount_WhenCreatingOrder_ThenLargeAmountIsStoredCorrectly() {
        // Given
        double amount = Double.MAX_VALUE;
        
        // When
        Order order = new Order("id", "customer", amount, Instant.now());
        
        // Then
        assertThat(order.getAmount()).isEqualTo(Double.MAX_VALUE);
    }

    @Test
    @DisplayName("Given very small amount, When creating Order, Then small amount is stored correctly")
    void givenVerySmallAmount_WhenCreatingOrder_ThenSmallAmountIsStoredCorrectly() {
        // Given
        double amount = Double.MIN_VALUE;
        
        // When
        Order order = new Order("id", "customer", amount, Instant.now());
        
        // Then
        assertThat(order.getAmount()).isEqualTo(Double.MIN_VALUE);
    }
}