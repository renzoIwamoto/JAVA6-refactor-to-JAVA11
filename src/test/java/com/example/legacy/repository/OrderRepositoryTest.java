package com.example.legacy.repository;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryImplTest {

    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new OrderRepositoryImpl();
    }

    @Test
    void givenOrder_whenSave_thenOrderIsSaved() {
        // Given
        Order order = new Order(UUID.randomUUID().toString(), "customer-1", 100.0, Instant.now());

        // When
        repository.save(order);

        // Then
        List<Order> orders = repository.findAll();
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
    }

    @Test
    void givenNoOrders_whenFindAll_thenReturnEmptyList() {
        // Given

        // When
        List<Order> orders = repository.findAll();

        // Then
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void givenSavedOrder_whenFindById_thenReturnOrder() {
        // Given
        String orderId = UUID.randomUUID().toString();
        Order order = new Order(orderId, "customer-1", 100.0, Instant.now());
        repository.save(order);

        // When
        Order foundOrder = repository.findById(orderId);

        // Then
        assertNotNull(foundOrder);
        assertEquals(order, foundOrder);
    }

    @Test
    void givenNonExistentOrderId_whenFindById_thenReturnNull() {
        // Given
        String orderId = UUID.randomUUID().toString();

        // When
        Order foundOrder = repository.findById(orderId);

        // Then
        assertNull(foundOrder);
    }
}
