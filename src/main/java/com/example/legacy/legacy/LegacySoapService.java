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
    private final OrderService orderService;

    public LegacySoapService(OrderService orderService) { 
        this.orderService = orderService; 
    }

    @WebMethod
    public String sayHello(String customerName) { 
        return "Hola, " + customerName + " (desde JAX-WS del JDK 6)"; 
    }

    @WebMethod
    public int createSampleOrder(String customerName, double orderAmount) {
        var customerId = UUID.randomUUID().toString();
        var customerEmail = customerName + "@example.com";
        var sampleCustomer = new Customer(customerId, customerName, customerEmail);
        
        var createdOrder = orderService.createOrder(sampleCustomer, orderAmount);
        return orderService.getOrdersByCustomer(sampleCustomer.id()).size();
    }

    @WebMethod
    public int countAllOrders() {
        var allOrders = orderService.getAllOrders();
        return allOrders.size();
    }
}
