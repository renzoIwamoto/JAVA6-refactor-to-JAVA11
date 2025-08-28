package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void givenCustomerAmountAndInstant_whenCreateOrder_thenSavedAndReturnedWithSameInstant() {
        // Given
        OrderRepository repo = mock(OrderRepository.class);
        OrderService service = new OrderService(repo);
        Customer c = new Customer("C1", "Alice", "a@example.com");
        Instant fixed = Instant.parse("2021-03-04T12:34:56Z");

        // When
        Order o = service.createOrder(c, 99.5, fixed);

        // Then
        assertNotNull(o.getId());
        assertEquals("C1", o.getCustomerId());
        assertEquals(99.5, o.getAmount());
        assertEquals(fixed, o.getCreatedAt());
        verify(repo, times(1)).save(any(Order.class));
    }

    @Test
    void givenMixedOrders_whenListByCustomer_thenFiltersOnlyMatching() {
        // Given
        OrderRepository repo = mock(OrderRepository.class);
        OrderService service = new OrderService(repo);
        List<Order> all = Arrays.asList(
                new Order("1", "C1", 10.0, Instant.parse("2020-01-01T00:00:00Z")),
                new Order("2", "C2", 20.0, Instant.parse("2020-01-02T00:00:00Z")),
                new Order("3", "C1", 30.0, Instant.parse("2020-01-03T00:00:00Z"))
        );
        when(repo.findAll()).thenReturn(all);

        // When
        List<Order> result = service.listByCustomer("C1");

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(o -> o.getCustomerId().equals("C1")));
    }

    @Test
    void givenEmptyRepository_whenListAll_thenReturnsEmptyList() {
        OrderRepository repo = mock(OrderRepository.class);
        when(repo.findAll()).thenReturn(List.of());
        OrderService service = new OrderService(repo);

        List<Order> result = service.listAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

