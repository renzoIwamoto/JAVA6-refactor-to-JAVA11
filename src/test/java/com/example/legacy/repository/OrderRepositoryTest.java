package com.example.legacy.repository;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    @Test
    void givenEmptyRepository_whenFindAll_thenReturnsEmptyUnmodifiableList() {
        OrderRepository repo = new OrderRepository();
        List<Order> snapshot = repo.findAll();
        assertNotNull(snapshot);
        assertTrue(snapshot.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(new Order()));
    }

    @Test
    void givenMultipleOrders_whenFindById_thenReturnsExpectedOrder() {
        OrderRepository repo = new OrderRepository();
        Order a = new Order("A", "C1", 10.0, Instant.parse("2020-01-01T00:00:00Z"));
        Order b = new Order("B", "C2", 20.0, Instant.parse("2020-01-02T00:00:00Z"));
        repo.save(a);
        repo.save(b);

        assertEquals("A", repo.findById("A").getId());
        assertEquals("B", repo.findById("B").getId());
        assertNull(repo.findById("NOPE"));
        assertEquals(2, repo.findAll().size());
    }
}

