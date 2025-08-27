package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class OrderService {
    private final OrderRepository repository;

    public OrderService(OrderRepository repository) { this.repository = repository; }

    public Order createOrder(Customer c, double amount, Date when) {
        String id = UUID.randomUUID().toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(when);
        Order o = new Order(id, c.getId(), amount, cal.getTime());
        repository.save(o);
        return o;
    }

    public List listAll() { return repository.findAll(); }

    public List listByCustomer(String customerId) {
        List all = repository.findAll();
        List result = new ArrayList();
        for (int i = 0; i < all.size(); i++) {
            Order o = (Order) all.get(i);
            if (o.getCustomerId().equals(customerId)) { result.add(o); }
        }
        return result;
    }
}
