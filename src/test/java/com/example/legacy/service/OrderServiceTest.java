package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    private final Clock fixedClock = Clock.fixed(Instant.parse("2023-10-27T10:00:00Z"), ZoneId.of("UTC"));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenCustomerAndAmount_whenCreateOrder_thenOrderIsCreatedAndSaved() {
        // Given
        Customer customer = new Customer(UUID.randomUUID().toString(), "Test Customer", "test@example.com");
        double amount = 150.0;
        Instant now = Instant.now(fixedClock);

        // When
        Order createdOrder = service.createOrder(customer, amount, now);

        // Then
        assertEquals(customer.getId(), createdOrder.getCustomerId());
        assertEquals(amount, createdOrder.getAmount());
        assertEquals(now, createdOrder.getCreatedAt());
        verify(repository, times(1)).save(createdOrder);
    }

    @Test
    void whenListAll_thenAllOrdersAreReturned() {
        // Given
        List<Order> expectedOrders = List.of(
                new Order(UUID.randomUUID().toString(), "customer-1", 100.0, Instant.now()),
                new Order(UUID.randomUUID().toString(), "customer-2", 200.0, Instant.now())
        );
        when(repository.findAll()).thenReturn(expectedOrders);

        // When
        List<Order> actualOrders = service.listAll();

        // Then
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void givenCustomerId_whenListByCustomer_thenOnlyMatchingOrdersAreReturned() {
        // Given
        String customerId = "customer-1";
        List<Order> allOrders = List.of(
                new Order(UUID.randomUUID().toString(), customerId, 100.0, Instant.now()),
                new Order(UUID.randomUUID().toString(), "customer-2", 200.0, Instant.now()),
                new Order(UUID.randomUUID().toString(), customerId, 300.0, Instant.now())
        );
        when(repository.findAll()).thenReturn(allOrders);

        // When
        List<Order> customerOrders = service.listByCustomer(customerId);

        // Then
        assertEquals(2, customerOrders.size());
        assertEquals(customerId, customerOrders.get(0).getCustomerId());
        assertEquals(customerId, customerOrders.get(1).getCustomerId());
    }
}
