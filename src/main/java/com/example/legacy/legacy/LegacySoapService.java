package com.example.legacy.legacy;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.service.OrderService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class LegacySoapService {
    private final OrderService service;

    public LegacySoapService(OrderService service) { this.service = service; }

    public String sayHello(String name) { return "Hola, " + name + " (Java 21)"; }

    public int createSampleOrder(String customerName, double amount) {
        Customer c = new Customer(UUID.randomUUID().toString(), customerName, customerName + "@example.com");
        Order o = service.createOrder(c, amount, Instant.now());
        return service.listByCustomer(c.getId()).size();
    }

    public int countAllOrders() {
        List<Order> all = service.listAll();
        return all.size();
    }
}
