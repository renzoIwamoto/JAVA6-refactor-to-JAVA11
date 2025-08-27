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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository repository;
    
    private OrderService service;
    private Clock fixedClock;
    private static final Instant FIXED_TIME = Instant.parse("2025-08-26T10:15:30.00Z");
    
    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(FIXED_TIME, ZoneId.of("UTC"));
        service = new OrderService(repository);
    }

    @Test
    void givenCustomerAndAmount_whenCreatingOrder_thenOrderIsSaved() {
        // Given
        Customer customer = new Customer("customer-123", "John Doe", "john@example.com");
        double amount = 99.99;
        Order expectedOrder = new Order(null, customer.getId(), amount, FIXED_TIME);

        // When
        Order result = service.createOrder(customer, amount, FIXED_TIME);

        // Then
        verify(repository).save(any(Order.class));
        assertThat(result.getCustomerId()).isEqualTo(customer.getId());
        assertThat(result.getAmount()).isEqualTo(amount);
        assertThat(result.getCreatedAt()).isEqualTo(FIXED_TIME);
    }

    @Test
    void givenMultipleOrders_whenListingAll_thenAllOrdersAreReturned() {
        // Given
        List<Order> orders = Arrays.asList(
            new Order("1", "customer-1", 100.0, FIXED_TIME),
            new Order("2", "customer-2", 200.0, FIXED_TIME)
        );
        when(repository.findAll()).thenReturn(orders);

        // When
        List<Order> result = service.listAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(orders);
    }

    @Test
    void givenCustomerId_whenListingByCustomer_thenOnlyCustomerOrdersAreReturned() {
        // Given
        String customerId = "customer-1";
        List<Order> allOrders = Arrays.asList(
            new Order("1", customerId, 100.0, FIXED_TIME),
            new Order("2", "other-customer", 200.0, FIXED_TIME),
            new Order("3", customerId, 300.0, FIXED_TIME)
        );
        when(repository.findAll()).thenReturn(allOrders);

        // When
        List<Order> result = service.listByCustomer(customerId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(order -> order.getCustomerId().equals(customerId));
    }

    @Test
    void givenNoOrders_whenListingByCustomer_thenEmptyListIsReturned() {
        // Given
        when(repository.findAll()).thenReturn(List.of());

        // When
        List<Order> result = service.listByCustomer("any-customer");

        // Then
        assertThat(result).isEmpty();
    }
}
