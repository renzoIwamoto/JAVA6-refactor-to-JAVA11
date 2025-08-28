package com.example.legacy.repository;

import com.example.legacy.model.Order;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderRepository {
    private final List<Order> storedOrders = new CopyOnWriteArrayList<>();

    public void save(Order orderToSave) {
        Objects.requireNonNull(orderToSave, "Order to save cannot be null");
        storedOrders.add(orderToSave); 
    }

    public List<Order> findAll() {
        return List.copyOf(storedOrders);
    }

    public Optional<Order> findById(String orderId) {
        Objects.requireNonNull(orderId, "Order ID cannot be null");
        return storedOrders.stream()
                .filter(storedOrder -> orderId.equals(storedOrder.getId()))
                .findFirst();
    }
}
