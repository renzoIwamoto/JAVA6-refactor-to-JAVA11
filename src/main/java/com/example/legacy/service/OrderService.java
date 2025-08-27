package com.example.legacy.service;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderService {
    
    private final OrderRepository orderRepository;
    
    public OrderService() {
        this.orderRepository = new OrderRepository();
    }
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public Order createOrder(Customer customer, String productName, Integer quantity, BigDecimal unitPrice) {
        if (customer == null || productName == null || quantity == null || unitPrice == null) {
            throw new IllegalArgumentException("All parameters are required");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        
        Order order = new Order(null, customer, productName, quantity, unitPrice);
        order.setOrderDate(getCurrentDate());
        order.setStatus("CREATED");
        
        return orderRepository.save(order);
    }
    
    public Order processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }
        
        if (!"CREATED".equals(order.getStatus())) {
            throw new IllegalStateException("Order cannot be processed. Current status: " + order.getStatus());
        }
        
        order.setStatus("PROCESSING");
        return orderRepository.save(order);
    }
    
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + orderId);
        }
        
        if (!"PROCESSING".equals(order.getStatus())) {
            throw new IllegalStateException("Order cannot be completed. Current status: " + order.getStatus());
        }
        
        order.setStatus("COMPLETED");
        return orderRepository.save(order);
    }
    
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
    
    public List<Order> findOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
    
    public List<Order> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findOrdersFromLastDays(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days must be positive");
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        Date cutoffDate = calendar.getTime();
        
        List<Order> allOrders = orderRepository.findAll();
        List<Order> recentOrders = new java.util.ArrayList<Order>();
        
        for (int i = 0; i < allOrders.size(); i++) {
            Order order = allOrders.get(i);
            if (order.getOrderDate() != null && order.getOrderDate().after(cutoffDate)) {
                recentOrders.add(order);
            }
        }
        
        return recentOrders;
    }
    
    public BigDecimal calculateTotalRevenue() {
        List<Order> completedOrders = orderRepository.findByStatus("COMPLETED");
        BigDecimal total = BigDecimal.ZERO;
        
        for (int i = 0; i < completedOrders.size(); i++) {
            Order order = completedOrders.get(i);
            total = total.add(order.getTotalAmount());
        }
        
        return total;
    }
    
    public String generateOrderReport(Long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            return "Order not found";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== ORDER REPORT ===\n");
        report.append("Order ID: ").append(order.getId()).append("\n");
        report.append("Customer: ").append(order.getCustomer().getName()).append("\n");
        report.append("Email: ").append(order.getCustomer().getEmail()).append("\n");
        report.append("Product: ").append(order.getProductName()).append("\n");
        report.append("Quantity: ").append(order.getQuantity()).append("\n");
        report.append("Unit Price: $").append(order.getUnitPrice()).append("\n");
        report.append("Total Amount: $").append(order.getTotalAmount()).append("\n");
        report.append("Order Date: ").append(formatDate(order.getOrderDate())).append("\n");
        report.append("Status: ").append(order.getStatus()).append("\n");
        report.append("===================\n");
        
        return report.toString();
    }
    
    private Date getCurrentDate() {
        return new Date();
    }
    
    private String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return String.format("%04d-%02d-%02d %02d:%02d:%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }
}