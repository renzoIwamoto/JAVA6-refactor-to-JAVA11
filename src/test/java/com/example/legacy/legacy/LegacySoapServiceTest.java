package com.example.legacy.legacy;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LegacySoapService Tests")
class LegacySoapServiceTest {

    @Mock
    private OrderService mockOrderService;
    
    private LegacySoapService soapService;

    @BeforeEach
    void setUp() {
        soapService = new LegacySoapService(mockOrderService);
    }

    @Test
    @DisplayName("Given name parameter, When calling sayHello, Then greeting message is returned")
    void givenNameParameter_WhenCallingSayHello_ThenGreetingMessageIsReturned() {
        // Given
        String name = "Renzo";
        
        // When
        String result = soapService.sayHello(name);
        
        // Then
        assertThat(result).isEqualTo("Hola, Renzo (desde JAX-WS del JDK 6)");
    }

    @Test
    @DisplayName("Given empty name, When calling sayHello, Then greeting with empty name is returned")
    void givenEmptyName_WhenCallingSayHello_ThenGreetingWithEmptyNameIsReturned() {
        // Given
        String name = "";
        
        // When
        String result = soapService.sayHello(name);
        
        // Then
        assertThat(result).isEqualTo("Hola,  (desde JAX-WS del JDK 6)");
    }

    @Test
    @DisplayName("Given null name, When calling sayHello, Then greeting with null is returned")
    void givenNullName_WhenCallingSayHello_ThenGreetingWithNullIsReturned() {
        // Given
        String name = null;
        
        // When
        String result = soapService.sayHello(name);
        
        // Then
        assertThat(result).isEqualTo("Hola, null (desde JAX-WS del JDK 6)");
    }

    @Test
    @DisplayName("Given special characters in name, When calling sayHello, Then special characters are preserved")
    void givenSpecialCharactersInName_WhenCallingSayHello_ThenSpecialCharactersArePreserved() {
        // Given
        String name = "José María ñandú @#$";
        
        // When
        String result = soapService.sayHello(name);
        
        // Then
        assertThat(result).isEqualTo("Hola, José María ñandú @#$ (desde JAX-WS del JDK 6)");
    }

    @Test
    @DisplayName("Given customer name and amount, When creating sample order, Then order is created and count is returned")
    void givenCustomerNameAndAmount_WhenCreatingSampleOrder_ThenOrderIsCreatedAndCountIsReturned() {
        // Given
        String customerName = "John Doe";
        double amount = 199.99;
        
        Order mockOrder = new Order("order-123", "customer-123", amount, Instant.now());
        List<Order> customerOrders = List.of(mockOrder);
        
        when(mockOrderService.createOrder(any(Customer.class), eq(amount)))
            .thenReturn(mockOrder);
        when(mockOrderService.listByCustomer(anyString()))
            .thenReturn(customerOrders);
        
        // When
        int result = soapService.createSampleOrder(customerName, amount);
        
        // Then
        assertThat(result).isEqualTo(1);
        
        // Verify interactions
        verify(mockOrderService).createOrder(argThat(customer -> 
            customer.getName().equals(customerName) &&
            customer.getEmail().equals(customerName + "@example.com")), 
            eq(amount));
        verify(mockOrderService).listByCustomer(anyString());
    }

    @Test
    @DisplayName("Given customer with existing orders, When creating sample order, Then correct count is returned")
    void givenCustomerWithExistingOrders_WhenCreatingSampleOrder_ThenCorrectCountIsReturned() {
        // Given
        String customerName = "Jane Smith";
        double amount = 99.50;
        
        Order newOrder = new Order("order-new", "customer-456", amount, Instant.now());
        List<Order> existingOrders = List.of(
            new Order("order-1", "customer-456", 100.0, Instant.now()),
            new Order("order-2", "customer-456", 200.0, Instant.now()),
            newOrder
        );
        
        when(mockOrderService.createOrder(any(Customer.class), eq(amount)))
            .thenReturn(newOrder);
        when(mockOrderService.listByCustomer(anyString()))
            .thenReturn(existingOrders);
        
        // When
        int result = soapService.createSampleOrder(customerName, amount);
        
        // Then
        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("Given zero amount, When creating sample order, Then order with zero amount is created")
    void givenZeroAmount_WhenCreatingSampleOrder_ThenOrderWithZeroAmountIsCreated() {
        // Given
        String customerName = "Zero Customer";
        double amount = 0.0;
        
        Order zeroOrder = new Order("order-zero", "customer-zero", 0.0, Instant.now());
        
        when(mockOrderService.createOrder(any(Customer.class), eq(0.0)))
            .thenReturn(zeroOrder);
        when(mockOrderService.listByCustomer(anyString()))
            .thenReturn(List.of(zeroOrder));
        
        // When
        int result = soapService.createSampleOrder(customerName, amount);
        
        // Then
        assertThat(result).isEqualTo(1);
        verify(mockOrderService).createOrder(any(Customer.class), eq(0.0));
    }

    @Test
    @DisplayName("Given negative amount, When creating sample order, Then order with negative amount is created")
    void givenNegativeAmount_WhenCreatingSampleOrder_ThenOrderWithNegativeAmountIsCreated() {
        // Given
        String customerName = "Refund Customer";
        double amount = -50.0;
        
        Order refundOrder = new Order("refund-1", "customer-refund", -50.0, Instant.now());
        
        when(mockOrderService.createOrder(any(Customer.class), eq(-50.0)))
            .thenReturn(refundOrder);
        when(mockOrderService.listByCustomer(anyString()))
            .thenReturn(List.of(refundOrder));
        
        // When
        int result = soapService.createSampleOrder(customerName, amount);
        
        // Then
        assertThat(result).isEqualTo(1);
        verify(mockOrderService).createOrder(any(Customer.class), eq(-50.0));
    }

    @Test
    @DisplayName("Given orders exist, When counting all orders, Then correct total count is returned")
    void givenOrdersExist_WhenCountingAllOrders_ThenCorrectTotalCountIsReturned() {
        // Given
        List<Order> allOrders = List.of(
            new Order("order-1", "customer-1", 100.0, Instant.now()),
            new Order("order-2", "customer-2", 200.0, Instant.now()),
            new Order("order-3", "customer-1", 150.0, Instant.now())
        );
        
        when(mockOrderService.listAll()).thenReturn(allOrders);
        
        // When
        int result = soapService.countAllOrders();
        
        // Then
        assertThat(result).isEqualTo(3);
        verify(mockOrderService).listAll();
    }

    @Test
    @DisplayName("Given no orders exist, When counting all orders, Then zero is returned")
    void givenNoOrdersExist_WhenCountingAllOrders_ThenZeroIsReturned() {
        // Given
        when(mockOrderService.listAll()).thenReturn(List.of());
        
        // When
        int result = soapService.countAllOrders();
        
        // Then
        assertThat(result).isEqualTo(0);
        verify(mockOrderService).listAll();
    }

    @Test
    @DisplayName("Given large number of orders, When counting all orders, Then correct large count is returned")
    void givenLargeNumberOfOrders_WhenCountingAllOrders_ThenCorrectLargeCountIsReturned() {
        // Given
        List<Order> largeOrderList = List.of(
            new Order("1", "c1", 1.0, Instant.now()),
            new Order("2", "c2", 2.0, Instant.now()),
            new Order("3", "c3", 3.0, Instant.now()),
            new Order("4", "c4", 4.0, Instant.now()),
            new Order("5", "c5", 5.0, Instant.now())
        );
        
        when(mockOrderService.listAll()).thenReturn(largeOrderList);
        
        // When
        int result = soapService.countAllOrders();
        
        // Then
        assertThat(result).isEqualTo(5);
        verify(mockOrderService).listAll();
    }

    @Test
    @DisplayName("Given customer name with special characters, When creating sample order, Then customer is created correctly")
    void givenCustomerNameWithSpecialCharacters_WhenCreatingSampleOrder_ThenCustomerIsCreatedCorrectly() {
        // Given
        String customerName = "José María Señor-Åström";
        double amount = 123.45;
        
        Order mockOrder = new Order("order-special", "customer-special", amount, Instant.now());
        
        when(mockOrderService.createOrder(any(Customer.class), eq(amount)))
            .thenReturn(mockOrder);
        when(mockOrderService.listByCustomer(anyString()))
            .thenReturn(List.of(mockOrder));
        
        // When
        int result = soapService.createSampleOrder(customerName, amount);
        
        // Then
        assertThat(result).isEqualTo(1);
        
        verify(mockOrderService).createOrder(argThat(customer -> 
            customer.getName().equals("José María Señor-Åström") &&
            customer.getEmail().equals("José María Señor-Åström@example.com")), 
            eq(amount));
    }
}