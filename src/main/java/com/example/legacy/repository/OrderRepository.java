package com.example.legacy.repository;

import com.example.legacy.model.Order;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderRepository {
    private final List orders = Collections.synchronizedList(new ArrayList());

    public void save(Order o) { orders.add(o); }

    public List findAll() {
        List copy = new ArrayList();
        synchronized (orders) {
            for (int i = 0; i < orders.size(); i++) {
                copy.add(orders.get(i));
            }
        }
        return copy;
    }

    public Order findById(String id) {
        synchronized (orders) {
            for (int i = 0; i < orders.size(); i++) {
                Order o = (Order) orders.get(i);
                if (o.getId().equals(id)) return o;
            }
        }
        return null;
    }
}
