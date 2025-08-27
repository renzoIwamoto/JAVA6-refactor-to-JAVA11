package com.example.legacy.repository;

import com.example.legacy.model.Order;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderRepository {
    private final List<Order> orders = new CopyOnWriteArrayList<>();

    public void save(Order o) { orders.add(o); }

    public List<Order> findAll() {
        return List.copyOf(orders);
    }

    public Optional<Order> findById(String id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }
}
