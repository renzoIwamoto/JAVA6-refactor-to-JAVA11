package com.example.legacy.repository;

import com.example.legacy.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderRepository {
    private final List<Order> orders = new CopyOnWriteArrayList<>();

    public void save(Order o) { 
        orders.add(o); 
    }

    public List<Order> findAll() {
        return List.copyOf(orders);
    }

    public Order findById(String id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
