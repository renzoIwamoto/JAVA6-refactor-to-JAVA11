package com.example.legacy.repository;

import com.example.legacy.model.Order;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    public void save(Order o) { orders.add(o); }

    public List<Order> findAll() {
        return List.copyOf(orders);
    }

    public Order findById(String id) {
        for (Order o : orders) {
            if (o.getId().equals(id)) return o;
        }
        return null;
    }
}
