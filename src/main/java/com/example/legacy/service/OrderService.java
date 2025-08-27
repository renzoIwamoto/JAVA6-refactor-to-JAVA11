package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private final OrderRepository repository;
    private final Clock clock;

    public OrderService(OrderRepository repository) { 
        this(repository, Clock.systemUTC());
    }
    
    public OrderService(OrderRepository repository, Clock clock) { 
        this.repository = repository; 
        this.clock = clock;
    }

    public Order createOrder(Customer c, double amount, Instant when) {
        String id = UUID.randomUUID().toString();
        Order o = new Order(id, c.getId(), amount, when);
        repository.save(o);
        return o;
    }
    
    public Order createOrder(Customer c, double amount) {
        return createOrder(c, amount, clock.instant());
    }

    public List<Order> listAll() { return repository.findAll(); }

    public List<Order> listByCustomer(String customerId) {
        return repository.findAll().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .toList();
    }
}
