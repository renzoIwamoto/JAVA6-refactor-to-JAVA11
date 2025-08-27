package com.example.legacy.legacy;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.service.OrderService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
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
        Order o = service.createOrder(c, amount, new Date());
        return service.listByCustomer(c.getId()).size();
    }

    @WebMethod
    public int countAllOrders() {
        List all = service.listAll();
        return all.size();
    }
}
