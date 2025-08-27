package com.example.legacy.legacy;

import com.example.legacy.model.Customer;
import com.example.legacy.model.Order;
import com.example.legacy.service.OrderService;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.List;

@WebService(name = "LegacyOrderService", targetNamespace = "http://legacy.example.com/")
public class LegacySoapService {
    
    private final OrderService orderService;
    
    public LegacySoapService() {
        this.orderService = new OrderService();
    }
    
    @WebMethod(operationName = "createOrder")
    @WebResult(name = "orderId")
    public Long createOrder(
            @WebParam(name = "customerId") Long customerId,
            @WebParam(name = "customerName") String customerName,
            @WebParam(name = "customerEmail") String customerEmail,
            @WebParam(name = "productName") String productName,
            @WebParam(name = "quantity") Integer quantity,
            @WebParam(name = "unitPrice") String unitPriceStr) {
        
        try {
            Customer customer = new Customer(customerId, customerName, customerEmail);
            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            
            Order order = orderService.createOrder(customer, productName, quantity, unitPrice);
            return order.getId();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }
    
    @WebMethod(operationName = "getOrder")
    @WebResult(name = "orderXml")
    public String getOrderAsXml(@WebParam(name = "orderId") Long orderId) {
        try {
            Order order = orderService.findOrderById(orderId);
            if (order == null) {
                return "<error>Order not found with ID: " + orderId + "</error>";
            }
            
            XmlMarshaller marshaller = new XmlMarshaller();
            return marshaller.marshalOrderToXml(order);
            
        } catch (Exception e) {
            return "<error>Failed to retrieve order: " + e.getMessage() + "</error>";
        }
    }
    
    @WebMethod(operationName = "processOrder")
    @WebResult(name = "success")
    public boolean processOrder(@WebParam(name = "orderId") Long orderId) {
        try {
            Order order = orderService.processOrder(orderId);
            return order != null && "PROCESSING".equals(order.getStatus());
        } catch (Exception e) {
            return false;
        }
    }
    
    @WebMethod(operationName = "completeOrder")
    @WebResult(name = "success")
    public boolean completeOrder(@WebParam(name = "orderId") Long orderId) {
        try {
            Order order = orderService.completeOrder(orderId);
            return order != null && "COMPLETED".equals(order.getStatus());
        } catch (Exception e) {
            return false;
        }
    }
    
    @WebMethod(operationName = "getAllOrders")
    @WebResult(name = "orderCount")
    public int getAllOrdersCount() {
        try {
            List<Order> orders = orderService.findAllOrders();
            return orders.size();
        } catch (Exception e) {
            return -1;
        }
    }
    
    @WebMethod(operationName = "getOrderReport")
    @WebResult(name = "report")
    public String getOrderReport(@WebParam(name = "orderId") Long orderId) {
        try {
            return orderService.generateOrderReport(orderId);
        } catch (Exception e) {
            return "Error generating report: " + e.getMessage();
        }
    }
    
    @WebMethod(operationName = "getTotalRevenue")
    @WebResult(name = "revenue")
    public String getTotalRevenue() {
        try {
            BigDecimal revenue = orderService.calculateTotalRevenue();
            return revenue.toString();
        } catch (Exception e) {
            return "0.00";
        }
    }
    
    @WebMethod(operationName = "getServerInfo")
    @WebResult(name = "info")
    public String getServerInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== LEGACY SOAP SERVICE INFO ===\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("JAX-WS Implementation: javax.xml.ws (built into JDK 6)\n");
        info.append("SOAP Version: 1.1\n");
        info.append("Service: LegacyOrderService\n");
        info.append("Namespace: http://legacy.example.com/\n");
        info.append("WARNING: This uses JAX-WS from JDK 6\n");
        info.append("In Java 11+, JAX-WS was REMOVED from JDK\n");
        info.append("Migration required: Add JAX-WS RI or Metro\n");
        info.append("===============================\n");
        
        return info.toString();
    }
}