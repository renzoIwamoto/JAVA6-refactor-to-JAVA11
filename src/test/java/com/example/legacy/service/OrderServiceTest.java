package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository mockRepository;
    
    private Clock fixedClock;
    private OrderService orderService;
    private Customer testCustomer;
    private Instant fixedTime;

    @BeforeEach
    void setUp() {
        fixedTime = Instant.parse("2024-01-15T10:30:00Z");
        fixedClock = Clock.fixed(fixedTime, ZoneOffset.UTC);
        orderService = new OrderService(mockRepository, fixedClock);
        testCustomer = new Customer("customer-123", "John Doe", "john@example.com");
    }

    @Test
    @DisplayName("Given valid customer and amount, When creating order with specific time, Then order is created and saved")
    void givenValidCustomerAndAmount_WhenCreatingOrderWithSpecificTime_ThenOrderIsCreatedAndSaved() {
        // Given
        double amount = 199.99;
        Instant specificTime = Instant.parse("2024-02-01T14:00:00Z");
        
        // When
        Order result = orderService.createOrder(testCustomer, amount, specificTime);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("customer-123");
        assertThat(result.getAmount()).isEqualTo(199.99);
        assertThat(result.getCreatedAt()).isEqualTo(specificTime);
        
        verify(mockRepository).save(result);
    }

    @Test
    @DisplayName("Given valid customer and amount, When creating order with clock, Then order uses clock time")
    void givenValidCustomerAndAmount_WhenCreatingOrderWithClock_ThenOrderUsesClockTime() {
        // Given
        double amount = 99.50;
        
        // When
        Order result = orderService.createOrder(testCustomer, amount);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCustomerId()).isEqualTo("customer-123");
        assertThat(result.getAmount()).isEqualTo(99.50);
        assertThat(result.getCreatedAt()).isEqualTo(fixedTime);
        
        verify(mockRepository).save(result);
    }

    @Test
    @DisplayName("Given zero amount, When creating order, Then order is created with zero amount")
    void givenZeroAmount_WhenCreatingOrder_ThenOrderIsCreatedWithZeroAmount() {
        // Given
        double amount = 0.0;
        
        // When
        Order result = orderService.createOrder(testCustomer, amount);
        
        // Then
        assertThat(result.getAmount()).isEqualTo(0.0);
        verify(mockRepository).save(result);
    }

    @Test
    @DisplayName("Given negative amount, When creating order, Then order is created with negative amount")
    void givenNegativeAmount_WhenCreatingOrder_ThenOrderIsCreatedWithNegativeAmount() {
        // Given
        double amount = -50.0;
        
        // When
        Order result = orderService.createOrder(testCustomer, amount);
        
        // Then
        assertThat(result.getAmount()).isEqualTo(-50.0);
        verify(mockRepository).save(result);
    }

    @Test
    @DisplayName("Given repository, When listing all orders, Then repository findAll is called")
    void givenRepository_WhenListingAllOrders_ThenRepositoryFindAllIsCalled() {
        // Given
        List<Order> expectedOrders = List.of(
            new Order("1", "customer-1", 100.0, fixedTime),
            new Order("2", "customer-2", 200.0, fixedTime)
        );
        when(mockRepository.findAll()).thenReturn(expectedOrders);
        
        // When
        List<Order> result = orderService.listAll();
        
        // Then
        assertThat(result).isEqualTo(expectedOrders);
        verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("Given empty repository, When listing all orders, Then empty list is returned")
    void givenEmptyRepository_WhenListingAllOrders_ThenEmptyListIsReturned() {
        // Given
        when(mockRepository.findAll()).thenReturn(List.of());
        
        // When
        List<Order> result = orderService.listAll();
        
        // Then
        assertThat(result).isEmpty();
        verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("Given orders from multiple customers, When listing by specific customer, Then only customer orders are returned")
    void givenOrdersFromMultipleCustomers_WhenListingBySpecificCustomer_ThenOnlyCustomerOrdersAreReturned() {
        // Given
        String targetCustomerId = "customer-123";
        List<Order> allOrders = List.of(
            new Order("1", "customer-123", 100.0, fixedTime),
            new Order("2", "customer-456", 200.0, fixedTime),
            new Order("3", "customer-123", 150.0, fixedTime),
            new Order("4", "customer-789", 75.0, fixedTime)
        );
        when(mockRepository.findAll()).thenReturn(allOrders);
        
        // When
        List<Order> result = orderService.listByCustomer(targetCustomerId);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(Order::getCustomerId)
            .containsOnly("customer-123");
        assertThat(result)
            .extracting(Order::getId)
            .containsExactly("1", "3");
    }

    @Test
    @DisplayName("Given no orders for customer, When listing by customer, Then empty list is returned")
    void givenNoOrdersForCustomer_WhenListingByCustomer_ThenEmptyListIsReturned() {
        // Given
        String customerId = "nonexistent-customer";
        List<Order> allOrders = List.of(
            new Order("1", "other-customer", 100.0, fixedTime),
            new Order("2", "another-customer", 200.0, fixedTime)
        );
        when(mockRepository.findAll()).thenReturn(allOrders);
        
        // When
        List<Order> result = orderService.listByCustomer(customerId);
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Given empty repository, When listing by customer, Then empty list is returned")
    void givenEmptyRepository_WhenListingByCustomer_ThenEmptyListIsReturned() {
        // Given
        when(mockRepository.findAll()).thenReturn(List.of());
        
        // When
        List<Order> result = orderService.listByCustomer("any-customer");
        
        // Then
        assertThat(result).isEmpty();
    }
}