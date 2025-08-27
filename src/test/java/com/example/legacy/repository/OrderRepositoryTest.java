package com.example.legacy.repository;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new OrderRepository();
    }

    @Test
    void givenEmptyRepository_whenFindingAll_thenReturnsEmptyList() {
        // Given - empty repository

        // When
        var orders = repository.findAll();

        // Then
        assertTrue(orders.isEmpty());
    }

    @Test
    void givenSavedOrder_whenFindingAll_thenReturnsOrderList() {
        // Given
        var order = new Order("id-1", "customer-1", 100.0, Instant.now());
        repository.save(order);

        // When
        var orders = repository.findAll();

        // Then
        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
    }

    @Test
    void givenMultipleSavedOrders_whenFindingAll_thenReturnsAllOrders() {
        // Given
        var order1 = new Order("id-1", "customer-1", 100.0, Instant.now());
        var order2 = new Order("id-2", "customer-2", 200.0, Instant.now());
        var order3 = new Order("id-3", "customer-1", 300.0, Instant.now());
        
        repository.save(order1);
        repository.save(order2);
        repository.save(order3);

        // When
        var orders = repository.findAll();

        // Then
        assertEquals(3, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
        assertTrue(orders.contains(order3));
    }

    @Test
    void givenEmptyRepository_whenFindingById_thenReturnsEmpty() {
        // Given - empty repository

        // When
        var result = repository.findById("non-existent-id");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenSavedOrder_whenFindingByExistingId_thenReturnsOrder() {
        // Given
        var order = new Order("existing-id", "customer-1", 100.0, Instant.now());
        repository.save(order);

        // When
        var result = repository.findById("existing-id");

        // Then
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    void givenSavedOrder_whenFindingByNonExistingId_thenReturnsEmpty() {
        // Given
        var order = new Order("existing-id", "customer-1", 100.0, Instant.now());
        repository.save(order);

        // When
        var result = repository.findById("non-existing-id");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleOrders_whenFindingBySpecificId_thenReturnsCorrectOrder() {
        // Given
        var order1 = new Order("id-1", "customer-1", 100.0, Instant.now());
        var order2 = new Order("id-2", "customer-2", 200.0, Instant.now());
        var order3 = new Order("id-3", "customer-3", 300.0, Instant.now());
        
        repository.save(order1);
        repository.save(order2);
        repository.save(order3);

        // When
        var result = repository.findById("id-2");

        // Then
        assertTrue(result.isPresent());
        assertEquals(order2, result.get());
        assertEquals("customer-2", result.get().getCustomerId());
        assertEquals(200.0, result.get().getAmount());
    }

    @Test
    void givenNullId_whenFindingById_thenReturnsEmpty() {
        // Given
        var order = new Order("id-1", "customer-1", 100.0, Instant.now());
        repository.save(order);

        // When
        var result = repository.findById(null);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenRepository_whenSavingOrderWithNullFields_thenOrderIsSaved() {
        // Given
        var orderWithNulls = new Order(null, null, 0.0, null);
        
        // When
        repository.save(orderWithNulls);
        var orders = repository.findAll();

        // Then
        assertEquals(1, orders.size());
        var savedOrder = orders.get(0);
        assertNull(savedOrder.getId());
        assertNull(savedOrder.getCustomerId());
        assertEquals(0.0, savedOrder.getAmount());
        assertNull(savedOrder.getCreatedAt());
    }

    @Test
    void givenRepository_whenFindAllIsCalledMultipleTimes_thenReturnsImmutableCopy() {
        // Given
        var order = new Order("id-1", "customer-1", 100.0, Instant.now());
        repository.save(order);

        // When
        var orders1 = repository.findAll();
        var orders2 = repository.findAll();

        // Then
        assertEquals(orders1, orders2);
        assertNotSame(orders1, orders2); // Different instances (immutable copies)
    }
}