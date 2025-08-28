package com.example.legacy.repository;

import com.example.legacy.model.Order;

import java.util.List;

public interface OrderRepository {
    void save(Order o);

    List<Order> findAll();

    Order findById(String id);
}