package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderService {
    private final OrderRepository orderRepository;
    private final Clock systemClock;

    public OrderService(OrderRepository orderRepository) { 
        this(orderRepository, Clock.systemUTC());
    }
    
    public OrderService(OrderRepository orderRepository, Clock systemClock) { 
        this.orderRepository = Objects.requireNonNull(orderRepository, "Order repository cannot be null"); 
        this.systemClock = Objects.requireNonNull(systemClock, "System clock cannot be null");
    }

    public Order createOrder(Customer customer, double orderAmount, Instant creationTimestamp) {
        Objects.requireNonNull(customer, "Customer cannot be null");
        Objects.requireNonNull(creationTimestamp, "Creation timestamp cannot be null");
        
        validateOrderAmount(orderAmount);
        
        var newOrderId = UUID.randomUUID().toString();
        var newOrder = new Order(newOrderId, customer.id(), orderAmount, creationTimestamp);
        orderRepository.save(newOrder);
        return newOrder;
    }
    
    public Order createOrder(Customer customer, double orderAmount) {
        return createOrder(customer, orderAmount, systemClock.instant());
    }

    public List<Order> getAllOrders() { 
        return orderRepository.findAll(); 
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        if (customerId.isBlank()) {
            throw new IllegalArgumentException("Customer ID cannot be blank");
        }
        
        return orderRepository.findAll().stream()
                .filter(order -> customerId.equals(order.getCustomerId()))
                .toList();
    }
    
    private void validateOrderAmount(double orderAmount) {
        if (Double.isNaN(orderAmount) || Double.isInfinite(orderAmount)) {
            throw new IllegalArgumentException("Order amount must be a valid number");
        }
    }
}
