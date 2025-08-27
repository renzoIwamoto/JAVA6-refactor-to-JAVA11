package com.example.legacy.repository;

import com.example.legacy.model.Order;
import com.example.legacy.model.Customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRepository {
    
    private final List<Order> orders;
    private final AtomicLong idGenerator;
    
    public OrderRepository() {
        this.orders = Collections.synchronizedList(new ArrayList<Order>());
        this.idGenerator = new AtomicLong(1);
    }
    
    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idGenerator.getAndIncrement());
        }
        
        synchronized (orders) {
            Order existingOrder = findById(order.getId());
            if (existingOrder != null) {
                orders.remove(existingOrder);
            }
            orders.add(order);
        }
        return order;
    }
    
    public Order findById(Long id) {
        if (id == null) {
            return null;
        }
        
        synchronized (orders) {
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                if (id.equals(order.getId())) {
                    return order;
                }
            }
        }
        return null;
    }
    
    public List<Order> findAll() {
        synchronized (orders) {
            return new ArrayList<Order>(orders);
        }
    }
    
    public List<Order> findByCustomerId(Long customerId) {
        if (customerId == null) {
            return new ArrayList<Order>();
        }
        
        List<Order> result = new ArrayList<Order>();
        synchronized (orders) {
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                if (order.getCustomer() != null && 
                    customerId.equals(order.getCustomer().getId())) {
                    result.add(order);
                }
            }
        }
        return result;
    }
    
    public List<Order> findByStatus(String status) {
        if (status == null) {
            return new ArrayList<Order>();
        }
        
        List<Order> result = new ArrayList<Order>();
        synchronized (orders) {
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                if (status.equals(order.getStatus())) {
                    result.add(order);
                }
            }
        }
        return result;
    }
    
    public boolean delete(Long id) {
        if (id == null) {
            return false;
        }
        
        synchronized (orders) {
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                if (id.equals(order.getId())) {
                    orders.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    public int count() {
        synchronized (orders) {
            return orders.size();
        }
    }
}