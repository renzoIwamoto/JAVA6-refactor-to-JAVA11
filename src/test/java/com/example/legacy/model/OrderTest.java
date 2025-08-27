package com.example.legacy.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void givenValidOrderData_whenCreatingOrder_thenOrderIsCreatedCorrectly() {
        // Given
        var id = "order-123";
        var customerId = "customer-456";
        var amount = 99.99;
        var createdAt = Instant.parse("2023-01-01T10:00:00Z");

        // When
        var order = new Order(id, customerId, amount, createdAt);

        // Then
        assertEquals(id, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(amount, order.getAmount());
        assertEquals(createdAt, order.getCreatedAt());
    }

    @Test
    void givenEmptyConstructor_whenCreatingOrder_thenOrderIsCreatedWithNullValues() {
        // Given & When
        var order = new Order();

        // Then
        assertNull(order.getId());
        assertNull(order.getCustomerId());
        assertEquals(0.0, order.getAmount());
        assertNull(order.getCreatedAt());
    }

    @Test
    void givenOrderWithSetters_whenSettingValues_thenValuesAreSetCorrectly() {
        // Given
        var order = new Order();
        var id = "new-id";
        var customerId = "new-customer";
        var amount = 150.75;
        var createdAt = Instant.now();

        // When
        order.setId(id);
        order.setCustomerId(customerId);
        order.setAmount(amount);
        order.setCreatedAt(createdAt);

        // Then
        assertEquals(id, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(amount, order.getAmount());
        assertEquals(createdAt, order.getCreatedAt());
    }

    @Test
    void givenZeroAmount_whenCreatingOrder_thenAmountIsZero() {
        // Given
        var amount = 0.0;

        // When
        var order = new Order("id", "customer", amount, Instant.now());

        // Then
        assertEquals(0.0, order.getAmount());
    }

    @Test
    void givenNegativeAmount_whenCreatingOrder_thenAmountIsNegative() {
        // Given
        var amount = -50.0;

        // When
        var order = new Order("id", "customer", amount, Instant.now());

        // Then
        assertEquals(-50.0, order.getAmount());
    }
}