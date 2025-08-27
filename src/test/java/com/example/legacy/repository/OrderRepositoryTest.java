package com.example.legacy.repository;

import com.example.legacy.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OrderRepository Tests")
class OrderRepositoryTest {

    private OrderRepository repository;
    private Instant fixedTime;

    @BeforeEach
    void setUp() {
        repository = new OrderRepository();
        fixedTime = Instant.parse("2024-01-15T10:30:00Z");
    }

    @Test
    @DisplayName("Given new repository, When finding all orders, Then empty list is returned")
    void givenNewRepository_WhenFindingAllOrders_ThenEmptyListIsReturned() {
        // When
        List<Order> result = repository.findAll();
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Given saved order, When finding all orders, Then order is returned")
    void givenSavedOrder_WhenFindingAllOrders_ThenOrderIsReturned() {
        // Given
        Order order = new Order("order-1", "customer-1", 100.0, fixedTime);
        repository.save(order);
        
        // When
        List<Order> result = repository.findAll();
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(order);
    }

    @Test
    @DisplayName("Given multiple saved orders, When finding all orders, Then all orders are returned")
    void givenMultipleSavedOrders_WhenFindingAllOrders_ThenAllOrdersAreReturned() {
        // Given
        Order order1 = new Order("order-1", "customer-1", 100.0, fixedTime);
        Order order2 = new Order("order-2", "customer-2", 200.0, fixedTime);
        Order order3 = new Order("order-3", "customer-1", 150.0, fixedTime);
        
        repository.save(order1);
        repository.save(order2);
        repository.save(order3);
        
        // When
        List<Order> result = repository.findAll();
        
        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(order1, order2, order3);
    }

    @Test
    @DisplayName("Given findAll returns immutable list, When modifying returned list, Then UnsupportedOperationException is thrown")
    void givenFindAllReturnsImmutableList_WhenModifyingReturnedList_ThenUnsupportedOperationExceptionIsThrown() {
        // Given
        Order order = new Order("order-1", "customer-1", 100.0, fixedTime);
        repository.save(order);
        
        // When
        List<Order> result = repository.findAll();
        
        // Then
        assertThat(result).hasSize(1);
        
        // Verify immutability
        try {
            result.add(new Order("order-2", "customer-2", 200.0, fixedTime));
            assertThat(false).isTrue(); // Should not reach here
        } catch (UnsupportedOperationException e) {
            // Expected behavior
            assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Test
    @DisplayName("Given existing order, When finding by valid ID, Then order is returned")
    void givenExistingOrder_WhenFindingByValidId_ThenOrderIsReturned() {
        // Given
        Order order = new Order("order-123", "customer-1", 99.99, fixedTime);
        repository.save(order);
        
        // When
        Order result = repository.findById("order-123");
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(order);
    }

    @Test
    @DisplayName("Given no matching order, When finding by ID, Then null is returned")
    void givenNoMatchingOrder_WhenFindingById_ThenNullIsReturned() {
        // Given
        Order order = new Order("order-123", "customer-1", 99.99, fixedTime);
        repository.save(order);
        
        // When
        Order result = repository.findById("nonexistent-id");
        
        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Given empty repository, When finding by ID, Then null is returned")
    void givenEmptyRepository_WhenFindingById_ThenNullIsReturned() {
        // When
        Order result = repository.findById("any-id");
        
        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Given multiple orders with different IDs, When finding by specific ID, Then correct order is returned")
    void givenMultipleOrdersWithDifferentIds_WhenFindingBySpecificId_ThenCorrectOrderIsReturned() {
        // Given
        Order order1 = new Order("order-111", "customer-1", 100.0, fixedTime);
        Order order2 = new Order("order-222", "customer-2", 200.0, fixedTime);
        Order order3 = new Order("order-333", "customer-3", 300.0, fixedTime);
        
        repository.save(order1);
        repository.save(order2);
        repository.save(order3);
        
        // When
        Order result = repository.findById("order-222");
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(order2);
        assertThat(result.getCustomerId()).isEqualTo("customer-2");
        assertThat(result.getAmount()).isEqualTo(200.0);
    }

    @Test
    @DisplayName("Given repository is thread-safe, When concurrent access, Then operations complete without errors")
    void givenRepositoryIsThreadSafe_WhenConcurrentAccess_ThenOperationsCompleteWithoutErrors() throws InterruptedException {
        // Given
        final int threadCount = 10;
        final int operationsPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        
        // When - concurrent saves
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    Order order = new Order(
                        "order-" + threadId + "-" + j,
                        "customer-" + threadId,
                        (double) (threadId * 100 + j),
                        fixedTime
                    );
                    repository.save(order);
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Then
        List<Order> allOrders = repository.findAll();
        assertThat(allOrders).hasSize(threadCount * operationsPerThread);
        
        // Verify all orders have unique IDs
        long uniqueIds = allOrders.stream()
            .map(Order::getId)
            .distinct()
            .count();
        assertThat(uniqueIds).isEqualTo(threadCount * operationsPerThread);
    }
}