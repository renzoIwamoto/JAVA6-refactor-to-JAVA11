package com.example.legacy.repository;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderRepositoryTest {
    private OrderRepository repository;
    private static final Instant FIXED_TIME = Instant.parse("2025-08-26T10:15:30.00Z");
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        repository = new OrderRepository();
        sampleOrder = new Order("test-123", "cust-456", 99.99, FIXED_TIME);
    }

    @Test
    void givenNewOrder_whenSaving_thenOrderIsStored() {
        // When
        repository.save(sampleOrder);
        List<Order> result = repository.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(sampleOrder);
    }

    @Test
    void givenMultipleOrders_whenFindingById_thenReturnsCorrectOrder() {
        // Given
        Order order1 = new Order("1", "cust-1", 100.0, FIXED_TIME);
        Order order2 = new Order("2", "cust-2", 200.0, FIXED_TIME);
        repository.save(order1);
        repository.save(order2);

        // When
        Order result = repository.findById("2");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("2");
        assertThat(result.getAmount()).isEqualTo(200.0);
    }

    @Test
    void givenNonExistentId_whenFindingById_thenReturnsNull() {
        // When
        Order result = repository.findById("non-existent");

        // Then
        assertThat(result).isNull();
    }

    @Test
    void givenEmptyRepository_whenFindingAll_thenReturnsEmptyList() {
        // When
        List<Order> result = repository.findAll();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultipleOrders_whenFindingAll_thenReturnsImmutableList() {
        // Given
        repository.save(new Order("1", "cust-1", 100.0, FIXED_TIME));
        repository.save(new Order("2", "cust-2", 200.0, FIXED_TIME));

        // When
        List<Order> result = repository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThrows(UnsupportedOperationException.class, () -> result.add(sampleOrder));
    }
}
