package com.example.legacy.legacy;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.service.OrderService;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@WebService(targetNamespace = "http://legacy.example.com/")
public class LegacySoapService {
    private final OrderService service;

    public LegacySoapService(OrderService service) { this.service = service; }

    @WebMethod
    public String sayHello(String name) { return "Hola, " + name + " (desde JAX-WS del JDK 6)"; }

    @WebMethod
    public int createSampleOrder(String customerName, double amount) {
        Customer c = new Customer(UUID.randomUUID().toString(), customerName, customerName + "@example.com");
        Order o = service.createOrder(c, amount);
        return service.listByCustomer(c.getId()).size();
    }

    @WebMethod
    public int countAllOrders() {
        List<Order> all = service.listAll();
        return all.size();
    }
}
