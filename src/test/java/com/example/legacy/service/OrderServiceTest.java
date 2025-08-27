package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository mockRepository;

    private OrderService orderService;
    private Clock fixedClock;
    private Instant fixedInstant;

    @BeforeEach
    void setUp() {
        fixedInstant = Instant.parse("2023-01-01T10:00:00Z");
        fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());
        orderService = new OrderService(mockRepository, fixedClock);
    }

    @Test
    void givenValidCustomerAndAmount_whenCreatingOrder_thenOrderIsCreatedAndSaved() {
        // Given
        var customer = new Customer("customer-123", "John Doe", "john@example.com");
        var amount = 99.99;

        // When
        var result = orderService.createOrder(customer, amount);

        // Then
        assertNotNull(result);
        assertEquals(customer.getId(), result.getCustomerId());
        assertEquals(amount, result.getAmount());
        assertEquals(fixedInstant, result.getCreatedAt());
        assertNotNull(result.getId());
        
        verify(mockRepository).save(any(Order.class));
    }

    @Test
    void givenValidCustomerAmountAndTime_whenCreatingOrderWithSpecificTime_thenOrderIsCreatedWithGivenTime() {
        // Given
        var customer = new Customer("customer-123", "John Doe", "john@example.com");
        var amount = 150.0;
        var specificTime = Instant.parse("2023-06-15T14:30:00Z");

        // When
        var result = orderService.createOrder(customer, amount, specificTime);

        // Then
        assertNotNull(result);
        assertEquals(customer.getId(), result.getCustomerId());
        assertEquals(amount, result.getAmount());
        assertEquals(specificTime, result.getCreatedAt());
        assertNotNull(result.getId());
        
        verify(mockRepository).save(any(Order.class));
    }

    @Test
    void givenZeroAmount_whenCreatingOrder_thenOrderIsCreatedWithZeroAmount() {
        // Given
        var customer = new Customer("customer-123", "John Doe", "john@example.com");
        var amount = 0.0;

        // When
        var result = orderService.createOrder(customer, amount);

        // Then
        assertEquals(0.0, result.getAmount());
        verify(mockRepository).save(any(Order.class));
    }

    @Test
    void givenNegativeAmount_whenCreatingOrder_thenOrderIsCreatedWithNegativeAmount() {
        // Given
        var customer = new Customer("customer-123", "John Doe", "john@example.com");
        var amount = -50.0;

        // When
        var result = orderService.createOrder(customer, amount);

        // Then
        assertEquals(-50.0, result.getAmount());
        verify(mockRepository).save(any(Order.class));
    }

    @Test
    void givenRepository_whenListingAllOrders_thenReturnsAllOrdersFromRepository() {
        // Given
        var order1 = new Order("id-1", "customer-1", 100.0, fixedInstant);
        var order2 = new Order("id-2", "customer-2", 200.0, fixedInstant);
        var expectedOrders = List.of(order1, order2);
        
        when(mockRepository.findAll()).thenReturn(expectedOrders);

        // When
        var result = orderService.listAll();

        // Then
        assertEquals(expectedOrders, result);
        verify(mockRepository).findAll();
    }

    @Test
    void givenEmptyRepository_whenListingAllOrders_thenReturnsEmptyList() {
        // Given
        when(mockRepository.findAll()).thenReturn(List.of());

        // When
        var result = orderService.listAll();

        // Then
        assertTrue(result.isEmpty());
        verify(mockRepository).findAll();
    }

    @Test
    void givenOrdersForMultipleCustomers_whenListingByCustomerId_thenReturnsOnlyOrdersForThatCustomer() {
        // Given
        var customerId = "customer-123";
        var order1 = new Order("id-1", customerId, 100.0, fixedInstant);
        var order2 = new Order("id-2", "other-customer", 200.0, fixedInstant);
        var order3 = new Order("id-3", customerId, 300.0, fixedInstant);
        var allOrders = List.of(order1, order2, order3);
        
        when(mockRepository.findAll()).thenReturn(allOrders);

        // When
        var result = orderService.listByCustomer(customerId);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order3));
        assertFalse(result.contains(order2));
        verify(mockRepository).findAll();
    }

    @Test
    void givenNoOrdersForCustomer_whenListingByCustomerId_thenReturnsEmptyList() {
        // Given
        var customerId = "non-existent-customer";
        var order1 = new Order("id-1", "other-customer-1", 100.0, fixedInstant);
        var order2 = new Order("id-2", "other-customer-2", 200.0, fixedInstant);
        var allOrders = List.of(order1, order2);
        
        when(mockRepository.findAll()).thenReturn(allOrders);

        // When
        var result = orderService.listByCustomer(customerId);

        // Then
        assertTrue(result.isEmpty());
        verify(mockRepository).findAll();
    }

    @Test
    void givenEmptyRepository_whenListingByCustomerId_thenReturnsEmptyList() {
        // Given
        var customerId = "any-customer";
        when(mockRepository.findAll()).thenReturn(List.of());

        // When
        var result = orderService.listByCustomer(customerId);

        // Then
        assertTrue(result.isEmpty());
        verify(mockRepository).findAll();
    }

    @Test
    void givenNullCustomerId_whenListingByCustomerId_thenReturnsEmptyList() {
        // Given
        var order1 = new Order("id-1", "customer-1", 100.0, fixedInstant);
        var order2 = new Order("id-2", null, 200.0, fixedInstant);
        var allOrders = List.of(order1, order2);
        
        when(mockRepository.findAll()).thenReturn(allOrders);

        // When
        var result = orderService.listByCustomer(null);

        // Then
        assertEquals(1, result.size());
        assertEquals(order2, result.get(0));
        verify(mockRepository).findAll();
    }

    @Test
    void givenDefaultConstructor_whenCreatingOrderService_thenUsesSystemClock() {
        // Given
        var repository = mock(OrderRepository.class);
        var service = new OrderService(repository);
        var customer = new Customer("customer-123", "John Doe", "john@example.com");

        // When
        var result = service.createOrder(customer, 100.0);

        // Then
        assertNotNull(result);
        assertTrue(result.getCreatedAt().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(result.getCreatedAt().isAfter(Instant.now().minusSeconds(1)));
    }
}