package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) { this.repository = repository; }

    public Order createOrder(Customer c, double amount, Instant when) {
        var id = UUID.randomUUID().toString();
        var order = new Order(id, c.getId(), amount, when);
        repository.save(order);
        return order;
    }

    public List<Order> listAll() { return repository.findAll(); }

    public List<Order> listByCustomer(String customerId) {
        return repository.findAll().stream()
                .filter(o -> o.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }
}
